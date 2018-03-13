//
//  encode.c
//  
//
//  Created by Moshe Ben-Ezra on 7/14/14.
//
//

#include <stdio.h>
#include <math.h>
#include "encode.h"
//#include "modem.h"

message_t Encode::makeMessage(unsigned char *data, int length)
{
  static message_t message;
  unsigned short checksum = 0;
  int i;
  message.magic = MAGIC;
  message.length = length;
  memset(message.payload,0,sizeof(message.payload));
  for (i=0; i<length; i++) {
    message.payload[i] = data[i];
    checksum = (checksum << 1) ^ data[i];
  }
  message.checksum = checksum;
  return message;
}

//int idx = 0;
void Encode::sendBit(int bit, FILE* fp)
{ 
  fwrite(modem.bitwave[bit],sizeof(short),bitlen,fp);
  //  memcpy(output[bitlen*idx], modem.bitwave[bit], sizeof(short)*bitlen);
  //  idx += 1;
}

void Encode::sendSync(int n, FILE *outp)
{
  int i;

  //send carrier 0 wave for n bits
  for (i=0; i< n; i++) {
    sendBit(0,outp);
  }

  //send sync bits
  sendBit(1,outp);
  sendBit(0,outp);
}
/*
void Encode::sendSync(int n, short* output)
{
    int i;

    //send carrier 0 wave for n bits  
    for (i=0; i< n; i++) {
      sendBit(0,output,i);
    }

    //send sync bits
    sendBit(1,output);
    sendBit(0,output);
}
 */

void Encode::encode(unsigned char* in_msg, int in_msg_len, FILE *outp)
{
  int cnt=0;
  int i;

  modem.init(1);

  // send 2 sec carrier wave
  sendSync(2*BITRATE, outp);

  //short sync to compensate for drift
  sendSync(2, outp);

  //encode
  for(int idx=0; idx<in_msg_len; idx++)
  {
    int c = in_msg[idx];
    if (c < 0) break;

    for (i=0; i<8; i++)
    {
      int bit = c & 0x1;
      sendBit(bit,outp);
      c >>= 1;
    }

    cnt++;
    if (cnt >= 8) {
      sendSync(2, outp); // send 2 zero bits and sync bits
      cnt = 0;
    }
  }

  //send two null bytes;
  for (i=0; i<16; i++)
    sendBit(0,outp);
  //    free(bitwave[0]);
  //    free(bitwave[1]);

}


/*
void Encode::encode(char *input, char* output, int out_len)
{
    int cnt=0;
    int i;

    modem.init();

    // send 2 sec carrier wave
    sendSync(2*BITRATE, output);

    //short sync to compensate for drift
    sendSync(2, outp);

    //encode
    while (!feof(inp)) {
        int c = getc(inp);
        if (c < 0) break;

        for (i=0; i<8; i++) {
            int bit = c & 0x1;
            sendBit(bit,outp);
            c >>= 1;
        }

        cnt++;
        if (cnt >= 8) {
            sendSync(2, outp); // send 2 zero bits and sync bits
            cnt = 0;
        }
    }

    //send two null byts;
    for (i=0; i<16; i++)
            sendBit(0,outp);
    //    free(bitwave[0]);
    //    free(bitwave[1]);

}
 */
