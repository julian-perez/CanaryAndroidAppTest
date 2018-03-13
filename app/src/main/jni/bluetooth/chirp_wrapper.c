#include "CHIRP/chirp.h"
#include "chirp_wrapper.h"
#include <jni.h>
#include <string.h>
#include <android/log.h>

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "NDK-BLACK_BOX", __VA_ARGS__))


static JavaVM *JVM;

extern int chirp_msg_cb_wrapper(uint8 type, const uint8 *firstCharPointer, uint16 length) {
    JNIEnv *env;
    int result = (*JVM)->GetEnv(JVM, (void **) &env, JNI_VERSION_1_6);
    if (result != JNI_OK) {
        LOGI("couldnt get JVM.");
        return 1;
    }

    jbyteArray array = (*env)->NewByteArray (env, length);
    (*env)->SetByteArrayRegion (env, array, 0, length, (jbyte*) firstCharPointer);

    jclass clazz =  (*env)->FindClass(env, "is/yranac/canary/nativelibs/ChirpManager");
    jmethodID mid;
    if(type == CPT_SPP_IV){
        mid = (*env)->GetStaticMethodID(env, clazz, "chirpEncryptionIVRecieved", "([B)V");
    } else  {
        mid = (*env)->GetStaticMethodID(env, clazz, "chirpMessageReceived", "([B)V");
    }
    (*env)->CallStaticVoidMethod(env, clazz, mid, array);
    return true;
}

extern int chirp_msg_send_wrapper(const uint8 *firstCharPointer, uint8 length) {
    JNIEnv *env;
    int result = (*JVM)->GetEnv(JVM, (void **) &env, JNI_VERSION_1_6);
    if (result != JNI_OK) {
        LOGI("couldnt get JVM.");
        return 1;
    }

    jbyteArray array = (*env)->NewByteArray (env, length);
    (*env)->SetByteArrayRegion (env, array, 0, length, (jbyte*) firstCharPointer);

    jclass clazz = (*env)->FindClass(env, "is/yranac/canary/nativelibs/ChirpManager");
    jmethodID mid = (*env)->GetStaticMethodID(env, clazz, "sendChunk", "([B)V");
    (*env)->CallStaticVoidMethod(env, clazz, mid, array);
    return 0;
}

uint32 chirp_time_ms_wrapper() {
    JNIEnv *env;
    int result = (*JVM)->GetEnv(JVM, (void **) &env, JNI_VERSION_1_6);
    if (result != JNI_OK) {
        LOGI("couldnt get JVM.");
        return 1;
    }
    jclass clazz = (*env)->FindClass(env, "is/yranac/canary/nativelibs/ChirpManager");
    jmethodID mid = (*env)->GetStaticMethodID(env, clazz, "getTimeMs", "()J");
    uint32 milliseconds = (uint32)(*env)->CallStaticLongMethod(env, clazz, mid);
    return (uint32)milliseconds;
}

void chirp_tx_cb_wrapper() {
    JNIEnv *env;
    int result = (*JVM)->GetEnv(JVM, (void **) &env, JNI_VERSION_1_6);
    if (result != JNI_OK) {
        LOGI("couldnt get JVM.");
        return;
    }

    jclass clazz = (*env)->FindClass(env, "is/yranac/canary/nativelibs/ChirpManager");
    jmethodID mid = (*env)->GetStaticMethodID(env, clazz, "messageGoneThrough", "()V");

    (*env)->CallStaticVoidMethod(env, clazz, mid);
}

void set_java_env(JavaVM *vm) {
    JVM = vm;
}