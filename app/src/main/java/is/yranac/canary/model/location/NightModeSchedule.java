package is.yranac.canary.model.location;

import android.text.format.DateUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import is.yranac.canary.util.DateUtil;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by Schroeder on 4/29/16.
 */
public class NightModeSchedule {

    @SerializedName("create_at")
    public Date creatAt;

    @SerializedName("day_of_week")
    public int dayOfWeek;

    @SerializedName("end_time")
    public String endTime;

    @SerializedName("id")
    public int id;

    @SerializedName("location")
    public String location;

    @SerializedName("start_time")
    public String startTime;


    public static NightModeSchedule createNew() {
        NightModeSchedule schedule = new NightModeSchedule();
        final TimeZone tz = TimeZone.getDefault();
        final long offsetFromUtc = tz.getOffset(new Date().getTime());
        int start = 22;
        int end = 6;
        schedule.startTime = String.format(Locale.ENGLISH, "%02d:00", start - MILLISECONDS.toHours(offsetFromUtc));
        schedule.endTime = String.format(Locale.ENGLISH, "%02d:00", end - MILLISECONDS.toHours(offsetFromUtc));
        return schedule;
    }

    @Override
    public boolean equals(Object obj) {


        if (obj == null)
            return false;

        if (!(obj instanceof NightModeSchedule))
            return false;

        if (obj == this)
            return true;

        NightModeSchedule nightModeSchedule = (NightModeSchedule) obj;

        if (!nightModeSchedule.startTime.equalsIgnoreCase(this.startTime)) {
            return false;
        }

        if (!nightModeSchedule.endTime.equalsIgnoreCase(this.endTime)) {
            return false;
        }

        return true;
    }

    public String localStartTime() {

        return getTimeInLocale(startTime);
    }

    public String localEndTime() {

        return getTimeInLocale(endTime);
    }

    private String getTimeInLocale(String time) {
        String[] parts = time.split(":");

        if (parts.length < 2) {
            return null;
        }


        final TimeZone tz = TimeZone.getDefault();
        final long offsetFromUtc = tz.getRawOffset();

        int totalMinutesOffset = (int) MILLISECONDS.toMinutes(offsetFromUtc);

        final int hoursOffset = totalMinutesOffset / 60;
        final int minutesOffset = totalMinutesOffset - (hoursOffset * 60);


        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        int localHour = hour + hoursOffset;
        int localMinute = minute + minutesOffset;

        if (localMinute < 0) {
            localMinute += 60;
            localHour -= 1;
        }

        if (localMinute >= 60) {
            localMinute -= 60;
            localHour += 1;
        }

        if (localHour < 0) {
            localHour += 24;
        }

        if (localHour >= 24) {
            localHour -= 24;
        }

        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, localHour);
        now.set(Calendar.MINUTE, localMinute);
        return DateUtil.utcDateToDisplayString(now.getTime());

    }
}
