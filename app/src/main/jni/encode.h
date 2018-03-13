#ifndef AUDIO_ENCODE_H_
#define AUDIO_ENCODE_H_

#include "modem.h"
#include <stdio.h>
#include <string.h>

#define MAX_ENCODE_MSG_LEN 2048
#define MAGIC 0x4242

typedef struct {
    unsigned short magic;
    unsigned short length;
    unsigned short checksum;
  unsigned char payload[MAX_ENCODE_MSG_LEN];
} message_t;

class Encode
{
  Modem modem;
  void sendBit(int bit, FILE *fp);
  void sendSync(int n, FILE *outp);
  
 public:
  message_t makeMessage(unsigned char *data, int length);
  void encode(unsigned char* in_msg, int in_msg_len, FILE *outp);
  message_t getMessage();
};

#endif /* AUDIO_ENCODE_H_ */
