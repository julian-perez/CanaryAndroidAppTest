package is.yranac.canary.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import is.yranac.canary.R;

/**
 * Created by Schroeder on 9/1/16.
 */
public class ColorUtil {

    private static int darkGray = 0;

    public static int darkGray(Context context) {
        if (darkGray == 0) {
            darkGray = ContextCompat.getColor(context, R.color.dark_gray);
        }
        return darkGray;
    }

    private static int brightSkyBlueTwo = 0;

    public static int brightSkyBlueTwo(Context context) {
        if (brightSkyBlueTwo == 0) {
            brightSkyBlueTwo = ContextCompat.getColor(context, R.color.bright_sky_blue_two);
        }
        return brightSkyBlueTwo;
    }

    private static int brightBlueSky = 0;

    public static int brightBlueSky(Context context) {
        if (brightBlueSky == 0) {
            brightBlueSky = ContextCompat.getColor(context, R.color.bright_blue_sky);
        }
        return brightBlueSky;
    }
    private static int darkModerateCyanThirty = 0;

    public static int darkModerateCyanThirty(Context context) {
        if (darkModerateCyanThirty == 0) {
            darkModerateCyanThirty = ContextCompat.getColor(context, R.color.dark_moderate_cyan_thirty);
        }
        return darkModerateCyanThirty;
    }


    private static int transparent = 0;

    public static int transparent(Context context) {
        if (transparent == 0) {
            transparent = ContextCompat.getColor(context, R.color.transparent);
        }
        return transparent;
    }

}
