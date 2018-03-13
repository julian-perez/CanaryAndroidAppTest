package is.yranac.canary.model.device;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import is.yranac.canary.model.mode.Mode;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.TemperatureScale;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 9/25/15.
 */
public class DeviceSettings {

    public static final float celsiusMax = 40.0f;
    public static final float celsiusMin = 0.0f;
    public static final float celsiusMinRange = 6.0f;

    public static final float fahrenheitMax = 104.0f;
    public static final float fahrenheitMin = 32.0f;
    public static final float fahrenheitMinRange = 10.0f;

    public static final int pirRecordingRangeLow = 2;
    public static final int pirRecordingRangeMedium = 4;
    public static final int pirRecordingRangeHigh = 7;


    private static final int maxNumberOfSigDigitsAllowed = 1;

    @SerializedName("air_quality_threshold")
    public float airQualityThreshold;

    @SerializedName("battery_saver_use")
    public boolean useBatterySaver;

    @SerializedName("created_at")
    public Date created;

    @SerializedName("detection_threshold")
    public float detectionThreshold;

    @SerializedName("home_mode")
    public Mode homeMode;

    @SerializedName("humidity_threshold_max")
    public float humidityThresholdMax;

    @SerializedName("humidity_threshold_min")
    public float humidityThresholdMin;

    @SerializedName("night_mode")
    public Mode nightMode;

    @SerializedName("resource_uri")
    public String resourceUri;

    @SerializedName("pir_sensitivity")
    public int pirRecordingRange;

    @SerializedName("send_air_quality_notifications")
    public boolean sendAirQualityNotifications;

    @SerializedName("send_battery_full_notifications")
    public boolean sendBatteryFullNotifications;

    @SerializedName("send_connectivity_notifications")
    public boolean sendConnectivityNotifications;

    @SerializedName("send_homehealth_notifications")
    public boolean sendHomehealthNotifications;

    @SerializedName("send_power_source_notifications")
    public boolean sendPowerSourceNotifications;

    @SerializedName("send_temp_max_notifications")
    public boolean sendTempMaxNotifications;

    @SerializedName("send_temp_min_notifications")
    public boolean sendTempMinNotifications;

    @SerializedName("send_humidity_max_notifications")
    public boolean sendHumidityMaxNotifications;

    @SerializedName("send_humidity_min_notifications")
    public boolean sendHumidityMinNotifications;

    @SerializedName("temp_battery_saver_use")
    public boolean tempBatterySaverUse;

    @SerializedName("temp_threshold_max")
    public float tempThresholdMax;

    @SerializedName("temp_threshold_min")
    public float tempThresholdMin;

    @SerializedName("updated_at")
    public Date updated;

    @SerializedName("backpack_data_usage_start_day")
    public int backpackDataUsageStartDay;

    public Date lastModified;

    public DeviceSettings(DeviceSettings deviceSettings) {
        this.airQualityThreshold = deviceSettings.airQualityThreshold;
        this.detectionThreshold = deviceSettings.detectionThreshold;
        this.humidityThresholdMax = deviceSettings.humidityThresholdMax;
        this.humidityThresholdMin = deviceSettings.humidityThresholdMin;
        this.resourceUri = deviceSettings.resourceUri;
        this.sendConnectivityNotifications = deviceSettings.sendConnectivityNotifications;
        this.sendHomehealthNotifications = deviceSettings.sendHomehealthNotifications;
        this.tempThresholdMax = deviceSettings.tempThresholdMax;
        this.tempThresholdMin = deviceSettings.tempThresholdMin;
        this.sendTempMaxNotifications = deviceSettings.sendTempMaxNotifications;
        this.sendTempMinNotifications = deviceSettings.sendTempMinNotifications;
        this.sendHumidityMaxNotifications = deviceSettings.sendHumidityMaxNotifications;
        this.sendHumidityMinNotifications = deviceSettings.sendHumidityMinNotifications;
        this.sendAirQualityNotifications = deviceSettings.sendAirQualityNotifications;
        this.sendPowerSourceNotifications = deviceSettings.sendPowerSourceNotifications;
        this.sendBatteryFullNotifications = deviceSettings.sendBatteryFullNotifications;
        this.useBatterySaver = deviceSettings.useBatterySaver;
        this.pirRecordingRange = deviceSettings.pirRecordingRange;

        if (deviceSettings.homeMode != null)
            this.homeMode = new Mode(deviceSettings.homeMode.id);
        if (deviceSettings.nightMode != null)
            this.nightMode = new Mode(deviceSettings.nightMode.id);
    }

    public DeviceSettings() {
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || !(obj instanceof DeviceSettings))
            return false;

        DeviceSettings deviceSettings = (DeviceSettings) obj;

        if (this.detectionThreshold != deviceSettings.detectionThreshold)
            return false;
        if (this.sendConnectivityNotifications != deviceSettings.sendConnectivityNotifications)
            return false;
        if (this.resourceUri == null || !this.resourceUri.equals(deviceSettings.resourceUri))
            return false;
        if (this.sendPowerSourceNotifications != deviceSettings.sendPowerSourceNotifications)
            return false;
        if (this.sendBatteryFullNotifications != deviceSettings.sendBatteryFullNotifications)
            return false;
        if (this.useBatterySaver != deviceSettings.useBatterySaver)
            return false;

        if (!areHomeHealthSettingsSame(deviceSettings))
            return false;

        if (deviceSettings.homeMode != null && !deviceSettings.homeMode.equals(this.homeMode))
            return false;

        if (deviceSettings.nightMode != null && !deviceSettings.nightMode.equals(this.nightMode))
            return false;

        if (this.useBatterySaver != deviceSettings.useBatterySaver)
            return false;

        if (this.pirRecordingRange != deviceSettings.pirRecordingRange)
            return false;


        return true;

    }

    public boolean areHomeHealthSettingsSame(DeviceSettings deviceSettings) {
        if (deviceSettings == null)
            return false;

        if (this.airQualityThreshold != deviceSettings.airQualityThreshold)
            return false;
        if (this.humidityThresholdMax != deviceSettings.humidityThresholdMax)
            return false;
        if (this.humidityThresholdMin != deviceSettings.humidityThresholdMin)
            return false;
        if (this.sendHomehealthNotifications != deviceSettings.sendHomehealthNotifications)
            return false;
        if (this.tempThresholdMax != deviceSettings.tempThresholdMax)
            return false;
        if (this.tempThresholdMin != deviceSettings.tempThresholdMin)
            return false;
        if (this.sendTempMaxNotifications != deviceSettings.sendTempMaxNotifications)
            return false;
        if (this.sendTempMinNotifications != deviceSettings.sendTempMinNotifications)
            return false;
        if (this.sendHumidityMaxNotifications != deviceSettings.sendHumidityMaxNotifications)
            return false;
        if (this.sendHumidityMinNotifications != deviceSettings.sendHumidityMinNotifications)
            return false;
        if (this.sendAirQualityNotifications != deviceSettings.sendAirQualityNotifications)
            return false;

        return true;
    }

    public void setTempNotificationPreferences(float minValue, float maxValue, boolean isCelcius) {

        float allowedMin = getMinAllowedTempVal(isCelcius);
        float allowedMax = getMaxAllowedTempVal(isCelcius);

        if (minValue < allowedMin) {
            this.tempThresholdMin =
                    UserUtils.getTemperatureInPreferredUnits(allowedMin,
                            isCelcius ? TemperatureScale.CELSIUS : TemperatureScale.FAHRENHEIT,
                            TemperatureScale.CELSIUS,
                            maxNumberOfSigDigitsAllowed);
            this.sendTempMinNotifications = false;
        } else {
            this.tempThresholdMin = UserUtils.getTemperatureInPreferredUnits(minValue,
                    isCelcius ? TemperatureScale.CELSIUS : TemperatureScale.FAHRENHEIT,
                    TemperatureScale.CELSIUS,
                    maxNumberOfSigDigitsAllowed);
            this.sendTempMinNotifications = true;
        }

        if (maxValue > allowedMax) {
            this.tempThresholdMax = UserUtils.getTemperatureInPreferredUnits(allowedMax,
                    isCelcius ? TemperatureScale.CELSIUS : TemperatureScale.FAHRENHEIT,
                    TemperatureScale.CELSIUS,
                    maxNumberOfSigDigitsAllowed);
            this.sendTempMaxNotifications = false;
        } else {
            this.tempThresholdMax = UserUtils.getTemperatureInPreferredUnits(maxValue,
                    isCelcius ? TemperatureScale.CELSIUS : TemperatureScale.FAHRENHEIT,
                    TemperatureScale.CELSIUS,
                    maxNumberOfSigDigitsAllowed);
            this.sendTempMaxNotifications = true;
        }
    }

    public void setHumidityNotificationPreferences(float minValue, float maxValue) {
        if (minValue <= 0) {
            this.humidityThresholdMin = 0;
            this.sendHumidityMinNotifications = false;
        } else {
            this.humidityThresholdMin = UserUtils.formatFloatDecimalPlaces(minValue, 2);
            this.sendHumidityMinNotifications = true;
        }

        if (maxValue >= 1) {
            this.humidityThresholdMax = 1;
            this.sendHumidityMaxNotifications = false;
        } else {
            this.humidityThresholdMax = UserUtils.formatFloatDecimalPlaces(maxValue, 2);
            this.sendHumidityMaxNotifications = true;
        }
    }

    public void setAirQualityNotificationPreferences(float maxVal) {
        if (maxVal == 0) {
            this.airQualityThreshold = 0;
            this.sendAirQualityNotifications = false;
        } else {
            this.airQualityThreshold = maxVal;
            this.sendAirQualityNotifications = true;
        }
    }

    public static float getMaxAllowedTempVal(boolean needCelsius) {
        return needCelsius ? celsiusMax : fahrenheitMax;
    }

    public static float getMinAllowedTempVal(boolean needCelsus) {
        return needCelsus ? celsiusMin : fahrenheitMin;
    }

    public static float getAllowedTempRange(boolean needCelsius) {
        return needCelsius ? celsiusMinRange : fahrenheitMinRange;
    }

    public float getTempThresholdMax(boolean needCesius) {
        return UserUtils.getTemperatureInPreferredUnits(tempThresholdMax,
                TemperatureScale.CELSIUS,
                needCesius ? TemperatureScale.CELSIUS : TemperatureScale.FAHRENHEIT,
                1);
    }

    public float getTempThresholdMin(boolean needCesius) {
        return UserUtils.getTemperatureInPreferredUnits(tempThresholdMin,
                TemperatureScale.CELSIUS,
                needCesius ? TemperatureScale.CELSIUS : TemperatureScale.FAHRENHEIT,
                1);
    }

    public int getCorrespondingDeviceId() {
        if (StringUtils.isNullOrEmpty(this.resourceUri))
            return -1;

        return Utils.getIntFromResourceUri(this.resourceUri);
    }

    public boolean modeSettingsSame(DeviceSettings deviceSettings) {
        if (deviceSettings.homeMode != null && !deviceSettings.homeMode.equals(this.homeMode))
            return false;

        if (deviceSettings.nightMode != null && !deviceSettings.nightMode.equals(this.nightMode))
            return false;

        return true;
    }

    public boolean isInBatterySaver() {
        return useBatterySaver || tempBatterySaverUse;
    }

    public boolean recentlyUpdate() {
        return new Date().getTime() - lastModified.getTime() > TimeUnit.MINUTES.toMillis(5);
    }
}
