package is.yranac.canary.util;

import android.os.SystemClock;

/**
 * Created by Schroeder on 9/30/15.
 */
public class TouchTimeUtil {

    private static long lastClickTime = 0;
    public static boolean dontAllowTouch(){
        if (SystemClock.elapsedRealtime() - lastClickTime < 250) {
            return true;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        return false;
    }
}
