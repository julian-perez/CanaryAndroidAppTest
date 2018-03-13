package is.yranac.canary.util;

import android.view.View;

/**
 * Created by michaelschroeder on 6/7/17.
 */

public class TouchUtil {

    public static boolean insideButton(View view, float xPoint, float yPoint) {

        if (view.getVisibility() == View.VISIBLE) {
            int[] l = new int[2];
            view.getLocationOnScreen(l);
            int x = l[0];
            int y = l[1];
            int w = view.getWidth();
            int h = view.getHeight();

            if (xPoint < x || xPoint > x + w || yPoint < y || yPoint > y + h) {
                return false;
            }
        } else {
            return false;
        }
        return true;

    }
}
