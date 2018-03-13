package is.yranac.canary.util;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.R;

/**
 * Created by Schroeder on 9/16/15.
 */
public class DateUtil {

    public static Date getCurrentTime() {

        if (Utils.isDemo()) {
            return convertApiStringToDate("2015-02-24T21:45:00");
        } else {
            return new Date();
        }
    }

    public static Calendar getCalanderInstance() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(getCurrentTime());
        return calendar;
    }


    public static long findDayForDate(Date date) {
        Calendar cal = DateUtil.getCalanderInstance();

        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

    public static boolean isToday(long when) {
        Time time = new Time();
        time.set(when);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(getCurrentTime().getTime());
        return (thenYear == time.year)
                && (thenMonth == time.month)
                && (thenMonthDay == time.monthDay);
    }

    private static String[] suffixes = {"th", "st", "nd", "rd", "th", "th",
            "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
            "th", "th", "th", "th", "st", "nd", "rd", "th", "th", "th", "th",
            "th", "th", "th", "st"};

    public static String dateToDay(Context mContext, Date date) {

        if (DateUtil.isToday(date.getTime())) {
            return mContext.getString(R.string.today);

        }

        Calendar cal = DateUtil.getCalanderInstance();
        cal.add(Calendar.DATE, -1);

        Calendar cal2 = DateUtil.getCalanderInstance();
        cal2.setTime(date);

        boolean yesterday = cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        if (yesterday) {
            return mContext.getString(R.string.yesterday);
        }
        cal.add(Calendar.DATE, -7);

        boolean withinWeek = cal.get(Calendar.YEAR) < cal2.get(Calendar.YEAR) || (cal.get(Calendar.YEAR) <= cal2.get(Calendar.YEAR) &&
                cal.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR));


        if (withinWeek) {
            String format = DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEE");
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
            return formatter.format(date);

        }

        SimpleDateFormat formatDayOfMonth = new SimpleDateFormat("d", Locale.getDefault());

        String format = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMMMd");

        SimpleDateFormat formatter = new SimpleDateFormat(format,
                Locale.getDefault());
        int day = Integer.parseInt(formatDayOfMonth.format(date));

        if (Locale.getDefault().getLanguage().equalsIgnoreCase(Locale.ENGLISH.getLanguage()))
            return formatter.format(date) + suffixes[day];
        else
            return formatter.format(date);
    }


    public static String convertToCalendarDate(int date) {
        return String.valueOf(date) + suffixes[date];
    }

    public static String convertDateToLongDisplayFormat(Date inputDate) {

        String format = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMMMdyyyy");

        SimpleDateFormat dateFormatter = new SimpleDateFormat(format,
                Locale.getDefault());
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormatter.format(inputDate);
    }

    public static String convertDateToMonthYearDisplayFormat(Date inputDate) {

        String format = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMMMyyyy");

        SimpleDateFormat dateFormatter = new SimpleDateFormat(format,
                Locale.getDefault());
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormatter.format(inputDate);
    }

    public static String utcDateToDisplayStringSeconds(Date apiDateString) {
        SimpleDateFormat dateFormatter;
        if (!DateFormat.is24HourFormat(CanaryApplication.getContext())) {
            dateFormatter = new SimpleDateFormat("h:mm:ss aa", Locale.getDefault());
        } else {
            dateFormatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        }
        dateFormatter.setTimeZone(TimeZone.getDefault());
        return dateFormatter.format(apiDateString);
    }

    public static String utcDateToDisplayString(Date apiDateString) {

        SimpleDateFormat dateFormatter;

        if (DateFormat.is24HourFormat(CanaryApplication.getContext())) {
            dateFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        } else {
            dateFormatter = new SimpleDateFormat("h:mm aa", Locale.getDefault());
        }
        dateFormatter.setTimeZone(TimeZone.getDefault());
        return dateFormatter.format(apiDateString);
    }

    public static Date convertApiStringToDate(String dateString) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss", Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return dateFormatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertDateToApiString(Date date) {

        SimpleDateFormat dateFormatter;

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);


        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormatter.format(date);
    }

    public static String convertDateToHour(Date date) {
        SimpleDateFormat dateFormatter;

        if (!DateFormat.is24HourFormat(CanaryApplication.getContext())) {
            dateFormatter = new SimpleDateFormat("ha", Locale.getDefault());
        } else {
            dateFormatter = new SimpleDateFormat("HH:00", Locale.getDefault());
        }
        return dateFormatter.format(date);

    }


    public static boolean is24HourFormat() {
        return DateFormat.is24HourFormat(CanaryApplication.getContext());
    }

    public static Date convertSqlStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String convertDateToDiagnosticString(Date date) {

//        January 7, 2017 at 11:00AM

        Context context = CanaryApplication.getContext();

        String builder = convertDateToLongDisplayFormat(date) +
                " " +
                context.getString(R.string.at) +
                " " +
                utcDateToDisplayString(date);

        return builder;
    }

    public static String convertDateToInstallString(Date date) {

//        2:00AM on January 7th, 201

        Context context = CanaryApplication.getContext();

        String builder = utcDateToDisplayString(date) +
                " " +
                context.getString(R.string.on) +
                " " +
                convertDateToLongDisplayFormat(date);

        return builder;
    }
}