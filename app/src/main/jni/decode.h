#ifndef AUDIO_DECODE_H_
#define AUDIO_DECODE_H_

#include "modem.h"
#include <algorithm>

#define MAX_DECODE_MSG_LEN 2048
#define MAGIC1 0x42
#define MAGIC2 0x42


typedef enum {INIT, WAIT1, WAIT2, LEN1, LEN2, PROCESS, CHECK1, CHECK2} state_t;

class Decode
{

    Modem modem;
    int64 corrPulse(int bit, short *buf, int idx);

  
 public:
    int findSync(short *buf, int idx, int n, int nbits);
    const char* decodedAddress;
    state_t state;
    void processInit();
    void proceessByte(char c, unsigned char **buffer, int *length);
    void processMessage(unsigned char *msg, int length);

    bool decode(short* audio, int audio_len, unsigned char **decoded_buffer, int *decoded_buffer_length, float factor, int dir);

};


#endif /* AUDIO_DECODE_H_ */
