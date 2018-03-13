#include "modem.h"
#include<math.h>


// fill bit wave buffers.
void Modem::mkBitwave(int dir)
{    
  // prepare bitwaves
  bitwave[0] = new short[sizeof(short) * (bitlen+1)];
  bitwave[1] = new short[sizeof(short) * (bitlen+1)];

  float fval;
  int i;
  
  for (i=0; i<bitlen; i++) {
    fval = AMP * sin(i * 2.0*M_PI / wlen) * maxval;
    bitwave[0][i] = (short)( +1 * fval * dir);
    bitwave[1][i] = (short)( -1 * fval * dir);
  }
}


void Modem::init(int dir)
{
    mkBitwave(dir);
}


Modem::~Modem()
{
  delete[] bitwave[0];
  delete[] bitwave[1];
}
