//
//  modem.h
//  
//
//  Created by Moshe Ben-Ezra on 7/17/14.
//
//

#ifndef _modem_h
#define _modem_h

//start with very conservative values
#define SPEED           44100           // sample rate
#define BITRATE         294             // ~36 byte/sec (was 147)
//#define FREQ            1650            // carrier frequncy
#define FREQ            1764            // carrier frequncy
#define AMP             1             // of max value

#define BUF_SIZE_SEC    (60)            // buf message size - one minutes
#define SKIP_SEC        1               // skip the start of recording noise
#define MIN_SIGNAL      (1000*1000*10)     // minimum signal threshold used
//#define M_PI 3.141592653589793

const int   bitlen    = SPEED/BITRATE;     // make sure it remains int
const int   wlen      = SPEED/FREQ;        // make sure it remains int
const short maxval    = (1<<15)-1;


typedef long long int64;


class Modem
{
 private:
  
 public:
  void mkBitwave(int dir);

  // wave pattern of 0 and 1 bits.
  short *bitwave[2];
  void init(int dir);
  ~Modem();
};

#endif
