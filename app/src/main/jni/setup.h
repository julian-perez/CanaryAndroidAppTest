
#include <stdlib.h>
#include <unistd.h>


#ifndef _setup_h
#define _setup_h

class Setup
{
    public:
        void readFile(const char* filename, short** encoded_buf, int* len);
};

#endif