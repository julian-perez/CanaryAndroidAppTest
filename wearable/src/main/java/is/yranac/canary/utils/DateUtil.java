package is.yranac.canary.utils;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import is.yranac.canary.CanaryWearApplication;

/**
 * Created by Schroeder on 9/16/15.
 */
public class DateUtil {

    public static String utcDateToDisplayString(Date apiDateString) {

        SimpleDateFormat dateFormatter;

        if (DateFormat.is24HourFormat(CanaryWearApplication.getContext())) {
            dateFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        } else {
            dateFormatter = new SimpleDateFormat("h:mm aa", Locale.getDefault());
        }
        dateFormatter.setTimeZone(TimeZone.getDefault());
        return dateFormatter.format(apiDateString);
    }

}
