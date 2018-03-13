
#include <string.h>
#include <jni.h>
#include "decode.h"
#include "encode.h"
#include "setup.h"
#include <android/log.h>
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "NDK-BLACK_BOX", __VA_ARGS__))

extern "C" {

    JNIEXPORT void JNICALL
    Java_is_yranac_canary_nativelibs_nativeclasses_BlackBox_encode (JNIEnv *env, jobject obj, jstring destination, jstring message){
         const char *nativeString = env->GetStringUTFChars(message, 0);
         const char *nativeDestination = env->GetStringUTFChars(destination, 0);
         LOGI( "The message was: %s\n", nativeString);

         int length = strlen(nativeString);
         unsigned char buffer [length];
         FILE* file = fopen(nativeDestination,"w");

             if (file != NULL)
             {
                Encode encoder;

                message_t msg = encoder.makeMessage( (unsigned char*)nativeString,length);
                int header_length = sizeof(msg.length) + sizeof(msg.magic) + sizeof(msg.checksum);
                int message_length =  header_length + length;

                //convert message to byte array
                char msg_bytes[message_length];// = static_cast<unsigned char*>(static_cast<void*>(&msg));

                memcpy(msg_bytes, &msg, header_length);
                memcpy(&msg_bytes[header_length], nativeString, length);

                //Encode message to file
                encoder.encode((unsigned char*)msg_bytes, message_length + 1,file);
                fclose(file);


             }
      }



    JNIEXPORT jstring JNICALL
    Java_is_yranac_canary_nativelibs_nativeclasses_BlackBox_decode (JNIEnv *env, jobject obj, jshortArray encodedData, jfloat factor, jint direction) {

        int size = env->GetArrayLength(encodedData);
        jboolean isCopy = JNI_TRUE;

        jshort *encoded_buf = env->GetShortArrayElements(encodedData, &isCopy);

        short *encodedPointer;
        encodedPointer = encoded_buf;
    	unsigned char *decoded_buffer = NULL;
        int decoded_buffer_length  = 0;

        float nFactor = factor;
        int nDirection = direction;
         LOGI( "Direction %d\n", direction);
         LOGI( "Factor %f\n", factor);
         LOGI( "The message was called");

    	Decode decode;
    	decode.processInit();
        bool decodedRight = decode.decode(encodedPointer, size, &decoded_buffer, &decoded_buffer_length, nFactor, nDirection);
        jstring decodedString;
         LOGI( "The message was called");

        if(decodedRight){
         LOGI( "The message was decoded");
            /* Canary Device sends strings that aren't null terminated. Need to add a null at the
             * last position manually because NewStringUTF expects a "c string" which are null
             * terminated.
             */
            decoded_buffer[decoded_buffer_length] = '\0';
           decodedString = env->NewStringUTF((const char *)decoded_buffer);

        } else  {
                 LOGI( "The message was not decoded");

            decodedString = env->NewStringUTF("");
        }

        return decodedString;
      }
}