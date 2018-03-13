package is.yranac.canary.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import is.yranac.canary.R;
import is.yranac.canary.model.SensorType;


public class Reading {

    public static final int READING_HUMIDITY = 1;
    public static final int READING_TEMPERATURE = 2;
    public static final int READING_AIR_QUALITY = 3;
    public static final int READING_BATTERY = 7;
    public static final int READING_WIFI = 8;
    private static final String LOG_TAG = "Reading";

    @SerializedName("created")
    public Date created;

    @SerializedName("device")
    public String deviceUri;

    @SerializedName("sensor_type")
    public SensorType sensorType;

    @SerializedName("status")
    public String status;

    @SerializedName("value")
    public float value;


    public boolean isOlderThan(long millis) {
        return new Date().getTime() - created.getTime() > millis;
    }


    public enum BatteryState {
        CHARGHING,
        FULL,
        ISSUE,
        OFFLINE,
        DISCHARGING;

        private static BatteryState[] allValues = values();

        public static BatteryState fromOrdinal(int n) {
            if (allValues.length >= n) {
                return FULL;
            }
            return allValues[n];
        }
    }

    public enum WifiConnectionLevel {
        MIN,
        ONE_BAR,
        TWO_BARS,
        FULL;

        private static WifiConnectionLevel[] allValues = values();

        public static WifiConnectionLevel fromOrdinal(int n) {
            if (allValues.length >= n) {
                return FULL;
            }
            return allValues[n];
        }
    }

    public BatteryState getBatteryState() {

        switch (status) {
            case "chrg":
                return BatteryState.CHARGHING;
            case "not_chrg_atchd":
            case "not_chrg":
                return BatteryState.DISCHARGING;
        }
        return BatteryState.ISSUE;
    }


    public String getWifiLevelLabel(@NonNull Context context) {

        if (status == null || status.equalsIgnoreCase(""))
            return "--";

        switch (status) {
            case "0":
                return "--";
            case "5":
                return context.getString(R.string.low);
            case "10":
                return context.getString(R.string.med);
            case "15":
                return context.getString(R.string.high);
            default:
                return "--";
        }
    }

    public WifiConnectionLevel getWifiConnectionLevel() {
        switch (status) {
            case "0":
                return WifiConnectionLevel.MIN;
            case "5":
                return WifiConnectionLevel.ONE_BAR;
            case "10":
                return WifiConnectionLevel.TWO_BARS;
            case "15":
                return WifiConnectionLevel.FULL;
            default:
                return WifiConnectionLevel.TWO_BARS;
        }
    }

}