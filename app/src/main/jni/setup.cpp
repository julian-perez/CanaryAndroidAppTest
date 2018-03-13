#include "setup.h"
#include <stdio.h>

void Setup::readFile(const char* filename, short** encoded_buf, int* len)
{
  FILE* fp = fopen (filename, "r");
  int maxsize=999999;

  short buffer[maxsize];

  int count = fread(buffer,sizeof(short),maxsize,fp);
  *len = count;

  *encoded_buf = (short*)calloc(count, sizeof(short));
  memcpy(*encoded_buf, buffer, sizeof(short) * count);

  fclose(fp);
}