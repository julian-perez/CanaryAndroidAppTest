package is.yranac.canary.messages;

import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceSettings;
import is.yranac.canary.model.reading.Reading;

/**
 * Created by sergeymorozov on 6/1/16.
 */
public class GotDeviceStatistics {
    public final Reading batteryReading;
    public final Reading wifiReading;
    public final Reading readingTemp;
    public final Reading readingHumidity;
    public final Reading readingAirQuality;

    public final DeviceSettings deviceSettings;
    public final Device device;

    public GotDeviceStatistics(Reading wifiReading, Reading batteryReading, DeviceSettings deviceSettings,
                               Device device) {
        this.device = device;
        this.readingTemp = null;
        this.readingHumidity = null;
        this.readingAirQuality = null;
        this.batteryReading = batteryReading;
        this.wifiReading = wifiReading;
        this.deviceSettings = deviceSettings;

    }


    public GotDeviceStatistics(Reading readingTemp, Reading readingHumidity, Reading readingAirQuality,
                               DeviceSettings deviceSettings, Device device) {

        this.readingTemp = readingTemp;
        this.readingHumidity = readingHumidity;
        this.readingAirQuality = readingAirQuality;
        this.deviceSettings = deviceSettings;
        this.device = device;
        this.batteryReading = null;
        this.wifiReading = null;
    }
}
