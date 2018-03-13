#include <string.h>
#include <jni.h>
#include <android/log.h>

extern "C" {
    #include "CHIRP/chirp.h"
    #include "chirp_wrapper.h"
}

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "NDK-BLACK_BOX", __VA_ARGS__))

extern "C" {

    static jboolean isJni();
    chirp_t chirpT;

    JNIEXPORT jint
    JNI_OnLoad(JavaVM *vm, void *reserved) {
        set_java_env(vm);
        return JNI_VERSION_1_6;
    }

    JNIEXPORT jint JNICALL
    Java_is_yranac_canary_nativelibs_nativeclasses_Chirp_chirpInit(JNIEnv * env, jobject obj){

        int resultInit = chirpInit(&chirpT, chirp_time_ms_wrapper, chirp_msg_send_wrapper, chirp_msg_cb_wrapper);
        int resultRegisterCallback = chirpReadyCallback(&chirpT, chirp_tx_cb_wrapper);
        return resultInit;
    }

    JNIEXPORT jint JNICALL
    Java_is_yranac_canary_nativelibs_nativeclasses_Chirp_chirpRx(JNIEnv * env, jobject obj, jbyteArray chunk){
        jboolean copy = isJni();
        const char *firstCharPointer = (char*)env->GetByteArrayElements(chunk, &copy);

        uint16 stringLength = env->GetArrayLength(chunk);

        int resultCode = chirpRecvChunk(&chirpT, (uint8*)firstCharPointer, stringLength);
        env -> ReleaseByteArrayElements(chunk, (jbyte*)firstCharPointer, JNI_ABORT);
        return (jint)resultCode;
    }

    JNIEXPORT jint JNICALL
    Java_is_yranac_canary_nativelibs_nativeclasses_Chirp_chirpTx(JNIEnv * env, jobject obj, jbyteArray message, jboolean isIv){
        jboolean copy = isJni();
        const char *firstCharPointer = (char*)env->GetByteArrayElements(message, &copy);

        uint16 stringLength = env->GetArrayLength(message);

        uint8 messageType = isIv ? CPT_SPP_IV : CPT_SPP_DATA;

        int resultCode = chirpSendMsg(&chirpT, messageType, (uint8*)firstCharPointer, stringLength);
        env -> ReleaseByteArrayElements(message, (jbyte*)firstCharPointer, JNI_ABORT);
        return (jint)resultCode;
    }

    JNIEXPORT jlong JNICALL
    Java_is_yranac_canary_nativelibs_nativeclasses_Chirp_chirpNextTOms(JNIEnv * env, jobject obj){
        uint32 msLest = chirpNextTOms(&chirpT);
        return msLest;
    }

    JNIEXPORT void JNICALL
    Java_is_yranac_canary_nativelibs_nativeclasses_Chirp_chirpHandleTO(JNIEnv * env, jobject obj){
        chirpHandleTO(&chirpT);
    }

    static jboolean isJni(){
        return JNI_TRUE;
    }
}