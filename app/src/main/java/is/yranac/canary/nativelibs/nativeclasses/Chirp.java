package is.yranac.canary.nativelibs.nativeclasses;

import android.util.Log;

/**
 * Created by sergeymorozov on 10/1/15.
 */
public class Chirp {
    static {
        System.loadLibrary("bluetooth");
    }

    /**
     * Initializes/resets Chirp.
     * @return 0 on success, non-zero on failure
     */
    private native synchronized int chirpInit();

    /**
     * When a device sends a smaller message chunk to the app, this function passes it into Chirp to be
     * assembled into a full JSON string. When Chirp assmbles the full JSON string,
     * {@link is.yranac.canary.nativelibs.ChirpManager#chirpMessageReceived(byte[])} is called with
     * full JSON message.
     * @param chunk Part of the full JSON message
     * @return the number of bytes consumed, this should equal to the length of chunk that was
     * passed in.
     */
    public native synchronized int chirpRx(byte[] chunk);

    /**
     * Sends a message to the Canary device by breaking it up into smaller pieces and sending those
     * individually using {@link is.yranac.canary.nativelibs.ChirpManager#sendChunk(byte[])}.
     * @param message to send, in UTF-8 format
     * @return 0 if the given message has been accepted for transmit. -1 if NULL/invalid context.
     * -2 if length greater than CHIRP_MAX_MESSAGE. Returns -3 if invalid type. -4 if message queue
     * full (e.g. prior chirpTx() not yet done). -5 if NULL tx buffer with non-zero length
     */
    public native synchronized int chirpTx(byte[] message, boolean isIV);

    /**
     * Time to wait before  telling Chirp to timeout by calling {@link Chirp#chirpHandleTO()}
     * @return 0 if no deadlines are pending, or 1 to time out.
     * *Limitation:* if the deadline is now or overdue, the return value is 1
     * since negative is invalid and 0 means no timeout.
     * This makes "overdue" and "1ms remaining" cases
     * indistinguishable.
     */
    public native synchronized long chirpNextTOms();

    /**
     * Tells Chirp to timeout and retry things that might not have gone through
     */
    public native synchronized void chirpHandleTO();

    private static Chirp chirpLib;

    public static Chirp instance() {
        if (chirpLib == null) {
            chirpLib = new Chirp();
            int chirpCode = chirpLib.chirpInit();
            Log.i("chirp init", "code is: " + chirpCode);
        }
        return chirpLib;
    }

    public void reset(){
        instance().chirpInit();
        chirpLib = null;
    }
}
