package is.yranac.canary.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Schroeder on 10/8/15.
 */
public class MathUtils {


    public static float round(float i, float v) {

        float rounded = Math.round(i/v) * v;


        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.ENGLISH);
        formatter.setMaximumFractionDigits(1);
        String output = formatter.format(rounded);
        return Float.parseFloat(output);
    }

}
