//
//  Created by Moshe Ben-Ezra on 7/16/14.
//

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <memory.h>
#include "modem.h"
#include "decode.h"
#include <ctime>
#include <android/log.h>

const char *checksumErrorString = "ChecksumError";
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "NDK-DECODE", __VA_ARGS__))

int64 Decode::corrPulse(int bit, short *buf, int idx)
{
  int64 sum = 0;
  int i;

  for (i=0; i<bitlen; i++) {
    sum += modem.bitwave[bit][i] * buf[idx+i];
  }

  return sum/bitlen; //normalize
}


int Decode::findSync(short *buf, int idx, int n, int nbits)
{
  int64 val, max;
  int i;

  // sync to carrier wave best in sec second
  int p=0;
  max=0;

  for (i=0; i<nbits*bitlen; i++) {
    val = corrPulse(0,buf,idx+i);
    if (val > max) {
      max = val;
      p = idx+i;
    }
  }
  idx = p;
  //fprintf(stderr,"Sync to carrier at %d [%g seconds]\n",idx, (float) idx / speed);
  //fprintf(stderr,"Sync signal %lld\n",max);

  // find sync bit at full wavelenth steps.
  while (idx < n-8*bitlen) {
    int64 b1 = corrPulse(0,buf,idx);
    int64 b2 = corrPulse(0,buf,idx+wlen);
    int64 b3 = corrPulse(0,buf,idx+2*wlen);

    //printf("%d: %lld %lld\n",idx, b1-b2, b2-b3);

    // check phor phase reversal
    if ((b1-b2) > MIN_SIGNAL && (b2-b3) < -MIN_SIGNAL) break;

    idx += wlen;
  }
  idx += wlen;
  //fprintf(stderr,"Sync bit at %d [%g seconds]\n",idx, (float) idx / speed);

  idx+=bitlen; // skip 1
  idx+=bitlen; // skip 0

  return idx;
}

void Decode::processInit()
{
  state = INIT;
  //  isBetweenBuffer = false;
}
bool Decode::decode(short* audio, int audio_len, unsigned char **decoded_buffer, int *decoded_buffer_length, float factor, int dir)
{
  modem.init(dir);
  short *buf;
  //  int64 val, max;
  int n=0;
  int audio_length_read = 0;
  int idx=0;
  int maxsize = SPEED*BUF_SIZE_SEC;
  int i;
  int cnt=0;
  n = std::min(maxsize,audio_len);
  bool retval = false;

  buf = new short[sizeof(short) * n];
  memcpy(buf,audio, sizeof(short) * n);
  LOGI("Read %d samples\n",n);

  // skip first samples
  idx = SKIP_SEC*SPEED; 

  //  std::clock_t start_time = std::clock();
  // skip silence'

  LOGI("IDX %d", idx);
  LOGI("N %d", n);

   while (idx < n && corrPulse(0,buf,idx) < MIN_SIGNAL * factor) idx++;

//  while (idx < n &&  idx < (audio_len/2 + 1) && corrPulse(0,buf,idx) < MIN_SIGNAL) idx++;
//  while (idx < n && corrPulse(0,buf,idx) < MIN_SIGNAL) idx++;
  //  printf("Time Taken to skip silence = %f sec\n", (std::clock() - start_time)/(double)CLOCKS_PER_SEC);

  if(idx > (n-bitlen * BITRATE))
  {
    delete[] buf;
    buf = NULL;
    return retval;
  }

    LOGI("Start signal (in win) at %d [%g seconds]\n",idx, (float) idx / SPEED);

  // sync carrier for 1 sec
  idx = findSync(buf, idx, n, BITRATE);
  // and refine short sync
  idx = findSync(buf, idx, n, 2);

  //decode
  cnt = 0;

  //  printf("After syncing idx=%d, max=%d\n",idx,n-16*bitlen);

  while (idx < n-16*bitlen)
  {
    char c=0;
    for (i=0; i<8; i++) {
      int bit = (corrPulse(1,buf,idx) > 0);
      c = c | (bit << i);
      idx+=bitlen;
    }
//    LOGI("%c", c);



    //        putchar(c);
    //        printf("%c", c);

    proceessByte(c, decoded_buffer, decoded_buffer_length);

    //Assuming its only reading one message at a time, get out as soon as message is finished
    if(*decoded_buffer_length > 0)
    {

      if(strncmp((const char*)*decoded_buffer, checksumErrorString, strlen(checksumErrorString)) != 0)//success 
      {
        retval = true;
        break;
      }
	else//checksum
      {
        retval = false;
        break;
      }
    }
    cnt++;
    if(cnt >= 8) {
      idx = findSync(buf, idx, n, 2);
      cnt=0;
    }

    // refill buffer
    if ((n == maxsize) && (idx >= n-16*bitlen)){

      audio_length_read += n;

      //for (i=0; i<maxsize-idx; i++) buf[i] = buf[i+idx];
      memcpy(buf,buf+idx, (maxsize-idx) * sizeof(short));

      n = std::min((maxsize-(maxsize-idx)), (audio_len - audio_length_read));
      if(n<=0)break; //msg finished

      memcpy(buf+(maxsize-idx),audio+maxsize,sizeof(short)*n);

      LOGI("Read %d samples\n",n);
      n += (maxsize-idx);
      idx = 0;
    }


  }

  delete[] buf;
  buf = NULL;
  return retval;
}

void Decode::processMessage(unsigned char *msg, int length)
{
  LOGI("Message:<");
  for (int i = 0; i<length; i++)    {
    LOGI("%c", msg[i]);
  }
  LOGI(">\n");
}

void Decode::proceessByte(char c, unsigned char **buffer, int *length)
{

  static unsigned char lenMSB;
  static unsigned char lenLSB;
  static unsigned short len;

  static unsigned char data[MAX_DECODE_MSG_LEN];
  static int idx;
  static unsigned short checksum;
  static unsigned char checkMSB;
  static unsigned char checkLSB;
  static unsigned short mesgChecksum;

  switch(state) {

  case INIT:
    idx = 0;
    checksum = 0;
    if(c == MAGIC1)
      state = WAIT2;
    else
      state = WAIT1;
    break;
  case WAIT1:
    if(c == MAGIC1)
      state = WAIT2;
  break;

  case WAIT2:
    if (c==MAGIC2)
      state = LEN1;
    else
      state = WAIT1;
    break;

  case LEN1:
    lenLSB = c;
    state = LEN2;
    LOGI("Len1\n");
    break;

  case LEN2:
    lenMSB = c;
    len = (lenMSB << 8) | lenLSB;
    LOGI("Len = %d\n",len);
    state = CHECK1;
    break;

  case CHECK1:
    checkLSB = c;
    state = CHECK2;
    break;

  case CHECK2:
    checkMSB = c;
    mesgChecksum = (checkMSB << 8) | checkLSB;
    state = PROCESS;
    break;

  case PROCESS:
    data[idx] = c;
    checksum = (checksum << 1) ^ c;
    idx ++;

    if (idx >= len) 
    {
      //printf("\nchecksum: %x %x\n",checksum, mesgChecksum);
      if (checksum != mesgChecksum)
      {
        LOGI("Checksum error\n");
        LOGI("data @Checksum error: %s\n",data);

        *length = strlen(checksumErrorString);
        if(NULL != *buffer) delete[] *buffer;
        *buffer = new unsigned char[sizeof(unsigned char)*(*length)];
        memcpy(*buffer, checksumErrorString, sizeof(char)*(*length));
      }
      else
      {
        LOGI("\n=====Printing Message=====\n");
        processMessage(data, len);
       LOGI("\n processMessage(data, len);\n");

        *length = len;
        if(NULL != *buffer) delete[] *buffer;

        *buffer = new unsigned char[sizeof(unsigned char)*len];
        memcpy(*buffer, data, sizeof(unsigned char)*len);
    }
      state = INIT;
    }

    break;


  default:
    LOGI("Internal Error unknown state!\n");
    break;
  }

}

