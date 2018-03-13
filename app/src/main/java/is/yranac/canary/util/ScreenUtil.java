package is.yranac.canary.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import is.yranac.canary.CanaryApplication;

/**
 * Created by michaelschroeder on 10/13/16.
 */

public class ScreenUtil {

    //By definition, a tablet is 7" or greater.
    private static final double TABLET_SCREEN = 7.0;

    public static boolean isTablet() {
        Context context = CanaryApplication.getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        float widthInches = metrics.widthPixels / metrics.xdpi;
        float heightInches = metrics.heightPixels / metrics.ydpi;
        double diagonalInches = Math.sqrt(Math.pow(widthInches, 2) + Math.pow(heightInches, 2));
        return diagonalInches >= TABLET_SCREEN;
    }
}
