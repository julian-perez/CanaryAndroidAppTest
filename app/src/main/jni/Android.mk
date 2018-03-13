LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_LDLIBS += -llog -ldl
LOCAL_MODULE    := blackbox
LOCAL_SRC_FILES := blackbox.cpp decode.cpp modem.cpp encode.cpp setup.cpp
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_LDLIBS += -llog -ldl
LOCAL_MODULE    := bluetooth
LOCAL_SRC_FILES := bluetooth/bluetooth.cpp bluetooth/chirp_wrapper.c bluetooth/CHIRP/chirp.c
include $(BUILD_SHARED_LIBRARY)