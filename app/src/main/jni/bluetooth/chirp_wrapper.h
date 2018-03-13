#ifndef CHIRP_HELPER_H_
#define CHIRP_HELPER_H_

#include "CHIRP/chirp.h"
#include <jni.h>

#define CPT_SPP_DATA          (0x07)
#define CPT_SPP_IV            (0x08)
/**
 * Defines methods Chirp needs to callback into Java
 */

extern void set_java_env(JavaVM *);

/* Static wrapper function for chirp.c chirp_msg_cb
 */
extern int chirp_msg_cb_wrapper(uint8, const uint8 *, uint16);


/* Static wrapper function for chirp.c chirp_msg_send
 */
extern int chirp_msg_send_wrapper(const uint8 *, uint8);


/* Static wrapper function for chirp.c chirp_time_ms
 */
extern uint32 chirp_time_ms_wrapper();

/* Static wrapper function for chirp_tx_cb
 */
extern void chirp_tx_cb_wrapper();

#endif /* CHIRP_HELPER_H_ */