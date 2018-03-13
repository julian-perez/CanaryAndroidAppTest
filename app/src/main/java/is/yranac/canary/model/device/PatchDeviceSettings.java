package is.yranac.canary.model.device;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 5/11/16.
 */
public class PatchDeviceSettings {

    @SerializedName("air_quality_threshold")
    public float airQualityThreshold;

    @SerializedName("battery_saver_use")
    public boolean useBatterySaver;

    @SerializedName("detection_threshold")
    public float detectionThreshold;

    @SerializedName("humidity_threshold_max")
    public float humidityThresholdMax;

    @SerializedName("humidity_threshold_min")
    public float humidityThresholdMin;

    @SerializedName("send_air_quality_notifications")
    public boolean sendAirQualityNotifications;

    @SerializedName("send_battery_full_notifications")
    public boolean sendBatteryFullNotifications;

    @SerializedName("send_connectivity_notifications")
    public boolean sendConnectivityNotifications;

    @SerializedName("send_homehealth_notifications")
    public boolean sendHomehealthNotifications;

    @SerializedName("send_humidity_max_notifications")
    public boolean sendHumidityMaxNotifications;

    @SerializedName("send_humidity_min_notifications")
    public boolean sendHumidityMinNotifications;

    @SerializedName("send_temp_max_notifications")
    public boolean sendTempMaxNotifications;

    @SerializedName("send_temp_min_notifications")
    public boolean sendTempMinNotifications;

    @SerializedName("temp_battery_saver_use")
    public boolean tempBatterySaverUse;

    @SerializedName("temp_threshold_max")
    public float tempThresholdMax;

    @SerializedName("temp_threshold_min")
    public float tempThresholdMin;

    @SerializedName("send_power_source_notifications")
    public boolean sendPowerSourceNotifications;

    @SerializedName("pir_sensitivity")
    public int pirRecordingRange;

    @SerializedName("backpack_data_usage_start_day")
    public int backpackDataUsageStartDay;

    public PatchDeviceSettings(DeviceSettings deviceSettings) {
        this.airQualityThreshold = deviceSettings.airQualityThreshold;

        this.useBatterySaver = deviceSettings.useBatterySaver;

        this.detectionThreshold = deviceSettings.detectionThreshold;

        this.humidityThresholdMax = deviceSettings.humidityThresholdMax;

        this.humidityThresholdMin = deviceSettings.humidityThresholdMin;

        this.sendConnectivityNotifications = deviceSettings.sendConnectivityNotifications;

        this.sendHomehealthNotifications = deviceSettings.sendHomehealthNotifications;

        this.tempThresholdMax = deviceSettings.tempThresholdMax;

        this.tempThresholdMin = deviceSettings.tempThresholdMin;

        this.sendTempMaxNotifications = deviceSettings.sendTempMaxNotifications;

        this.sendTempMinNotifications = deviceSettings.sendTempMinNotifications;

        this.sendHumidityMaxNotifications = deviceSettings.sendHumidityMaxNotifications;

        this.sendHumidityMinNotifications = deviceSettings.sendHumidityMinNotifications;

        this.sendAirQualityNotifications = deviceSettings.sendAirQualityNotifications;

        this.sendBatteryFullNotifications = deviceSettings.sendBatteryFullNotifications;

        this.sendPowerSourceNotifications = deviceSettings.sendPowerSourceNotifications;

        this.tempBatterySaverUse = deviceSettings.tempBatterySaverUse;

        this.pirRecordingRange = deviceSettings.pirRecordingRange;

        this.backpackDataUsageStartDay = deviceSettings.backpackDataUsageStartDay;
    }
}
