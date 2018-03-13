package is.yranac.canary.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import is.yranac.canary.R;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by Schroeder on 7/14/16.
 */
public class NightModeUtil {

    private static final String LOG_TAG = "NightModeUtil";

    public interface NightModeCallback {
        void onComplete(String time);
    }


    public static void setUpNightModeDialog(@NonNull String currentTime, @NonNull Context context, @NonNull final NightModeCallback callback) {
        setUpNightModeDialog(currentTime, context, null, callback);
    }

    public static void setUpNightModeDialog(@NonNull String currentTime, @NonNull Context context, String title, @NonNull final NightModeCallback callback) {
        final TimeZone tz = TimeZone.getDefault();
        final long offsetFromUtc = tz.getRawOffset();

        String[] timeParts = currentTime.split(":");

        int totalMinutesOffset = (int) MILLISECONDS.toMinutes(offsetFromUtc);

        final int hoursOffset = totalMinutesOffset / 60;
        final int minutesOffset = totalMinutesOffset -  (hoursOffset * 60);


        int hour = Integer.parseInt(timeParts[0]) + hoursOffset;
        int minute = Integer.parseInt(timeParts[1]) + minutesOffset;


        if (minute < 0) {
            minute += 60;
            hour -= 1;
        }

        if (minute >= 60) {
            minute -= 60;
            hour += 1;
        }

        if (hour < 0) {
            hour += 24;
        }

        if (hour >= 24) {
            hour -= 24;
        }
        Log.i(LOG_TAG,"Locale hour " + String.valueOf(hour));
        Log.i(LOG_TAG, "Locale minute " +String.valueOf(minute));

        final View timePicker = LayoutInflater.from(context).inflate(R.layout.alert_dialog_time_picker, null, false);
        final AlertDialog alertDialog = AlertUtils.buildAlert(context, timePicker, true);
        final TimePicker timePicker1 = (TimePicker) timePicker.findViewById(R.id.time_picker);
        timePicker1.setCurrentHour(hour);
        timePicker1.setCurrentMinute(minute);
        timePicker1.setIs24HourView(DateUtil.is24HourFormat());
        alertDialog.show();
        Button okay = (Button) timePicker.findViewById(R.id.alert_button_right);

        TextView textView = (TextView) timePicker.findViewById(R.id.alert_header);
        if (title != null) {
            textView.setText(title);

        }

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


                int hour = timePicker1.getCurrentHour() - hoursOffset;
                int minute = timePicker1.getCurrentMinute() - minutesOffset;

                if (minute < 0) {
                    minute += 60;
                    hour -= 1;
                }

                if (minute >= 60) {
                    minute -= 60;
                    hour += 1;
                }

                if (hour < 0) {
                    hour += 24;
                }

                if (hour >= 24) {
                    hour -= 24;
                }


                String time = String.format(Locale.ENGLISH, "%02d:%02d", hour, minute);

                callback.onComplete(time);

            }
        });
        Button cancel = (Button) timePicker.findViewById(R.id.alert_button_left);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}
