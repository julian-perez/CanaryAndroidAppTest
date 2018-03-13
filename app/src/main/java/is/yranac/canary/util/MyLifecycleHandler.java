package is.yranac.canary.util;

import android.app.Activity;
import android.content.Intent;

import com.snowplowanalytics.snowplow.tracker.tracker.LifecycleHandler;

import is.yranac.canary.services.intent.GlobalAPIntentServiceManager;
import is.yranac.canary.ui.LaunchActivity;
import is.yranac.canary.ui.LockActivity;

/**
 * Created by Schroeder on 10/8/14.
 */
public class MyLifecycleHandler extends LifecycleHandler{
    private static final String LOG_TAG = "MyLifecycleHandler";
    // I use four separate variables here. You can, of course, just use two and
    // increment/decrement them instead of using four and incrementing them all.
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;
    private static long timeWhenToBackground = 0;


    @Override
    public void onActivityResumed(Activity activity) {
        super.onActivityResumed(activity);
        ++resumed;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        super.onActivityPaused(activity);
        ++paused;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        super.onActivityStarted(activity);
        if (started == stopped || started == 0) {
            if (PreferencesUtils.hasPassCode()) {
                showPassCodeScreen(activity);


            }


        }
        ++started;
        Log.i(LOG_TAG, String.valueOf(started - stopped));
        if (started - stopped == 1)
            applicationCameToForeground(activity);
    }

    private void showPassCodeScreen(Activity activity) {
        long backgroundDuration = DateUtil.getCurrentTime().getTime() - timeWhenToBackground;
        timeWhenToBackground = 0;

        if (backgroundDuration < 60 * 1000) {
            return;
        }

        Intent intent = activity.getIntent();


        intent.putExtra("entryId", activity.getIntent().getIntExtra("entryId", 0));
        intent.putExtra("from_launch", activity instanceof LaunchActivity);

        intent.putExtra("start_lock", true);
        activity.setIntent(intent);

    }

    @Override
    public void onActivityStopped(Activity activity) {
        super.onActivityStopped(activity);
        ++stopped;
        applicationInBackground(activity);
    }

    public static boolean applicationInBackground(Activity activity) {

        if (started == stopped) {
            if (!(activity instanceof LockActivity) && !(activity instanceof LaunchActivity)) {
                timeWhenToBackground = DateUtil.getCurrentTime().getTime();
            }
            GlobalAPIntentServiceManager.cancelAlarms(activity.getApplicationContext());
            return true;
        }
        return false;
    }


    public static boolean applicationInBackground() {
        return paused == resumed;
    }

    private void applicationCameToForeground(Activity activity) {
        GlobalAPIntentServiceManager.resetAlarms(activity.getApplicationContext());
    }

    public static boolean appTerminated(){
        return paused == 0;
    }

}