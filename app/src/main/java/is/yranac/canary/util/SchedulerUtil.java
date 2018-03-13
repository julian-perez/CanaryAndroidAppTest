package is.yranac.canary.util;

import android.os.Handler;
import android.os.HandlerThread;

import java.security.InvalidParameterException;

import is.yranac.canary.messages.scheduler.SchedulerCallback;
import is.yranac.canary.nativelibs.BTLEMessageBus;

/**
 * Created by sergeymorozov on 10/20/15.
 */
public class SchedulerUtil {
    private static Handler mHandler;
    private static HandlerThread mHandlerThread;
    private static Runnable exclusiveCallbackRunnable;
    private static SchedulerCallback.SchedulerType exclusiveSchedulerType;

    /**
     * <p>This function cancels previously scheduled exclusive callback (if exists)
     * to schedule a new one. It'll call {@link BTLEMessageBus#schedulerCallback(SchedulerCallback)}
     * when the time is up.</p>
     * @param callbackType Type of a callback to receive
     * @param delayFromNow Time from now to receive a callback
     */
    public static void scheduleExclusiveCallback(SchedulerCallback.SchedulerType callbackType, long delayFromNow) {

        if(delayFromNow <= 0){
            Log.e("SchedulerUtil error", "invalid delay: " + delayFromNow);
            throw new InvalidParameterException("delay is 0 or less");
        }

        resetLastCallback();
        exclusiveSchedulerType = callbackType;
        getHandler().postDelayed(getRunnable(), delayFromNow);
    }

    private static Handler getHandler() {

        if(mHandler == null)
            mHandler = new Handler(getHandlerThread().getLooper());
        return mHandler;
    }

    private static HandlerThread getHandlerThread(){
        if(mHandlerThread == null) {
            mHandlerThread = new HandlerThread("HandlerThread");
            mHandlerThread.start();
        }
        return mHandlerThread;
    }

    private static void resetLastCallback(){

        if(exclusiveCallbackRunnable != null && mHandler != null) {
            mHandler.removeCallbacks(exclusiveCallbackRunnable);
            mHandler = null;
            exclusiveCallbackRunnable = null;
            if(mHandlerThread!= null && mHandlerThread.isAlive())
                mHandlerThread.quit();
            mHandlerThread = null;
        }
    }

    private static Runnable getRunnable() {

        if(exclusiveCallbackRunnable == null)
            exclusiveCallbackRunnable = new Runnable() {
                @Override
                public void run() {
                    BTLEMessageBus.schedulerCallback(new SchedulerCallback(exclusiveSchedulerType));
                }
            };
        return exclusiveCallbackRunnable;
    }
}
