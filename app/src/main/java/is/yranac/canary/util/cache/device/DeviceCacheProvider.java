package is.yranac.canary.util.cache.device;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.messages.GetDeviceSettings;
import is.yranac.canary.messages.GetDeviceStatistics;
import is.yranac.canary.messages.GotDeviceSettings;
import is.yranac.canary.messages.GotDeviceStatistics;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceSettings;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.model.reading.Reading;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.ReadingDatabaseService;
import is.yranac.canary.util.TinyMessageBus;

/**
 * Created by michaelschroeder on 3/2/17.
 */

public class DeviceCacheProvider {

    private static DeviceCacheProvider cacheProvider;

    public static void register() {
        TinyMessageBus.register(getCacheProvider());
    }

    public static void unregister() {
        TinyMessageBus.unregister(getCacheProvider());
    }

    private static DeviceCacheProvider getCacheProvider() {
        if (cacheProvider == null)
            cacheProvider = new DeviceCacheProvider();
        return cacheProvider;
    }

    //================================================================================
    // Device statistics
    //================================================================================
    @Subscribe(mode = Subscribe.Mode.Background)
    public void getDeviceStats(GetDeviceStatistics deviceStatistics) {
        Device device = deviceStatistics.device;
        if (device == null || !device.hasSensors())
            return;

        if (device.getDeviceType() == DeviceType.FLEX) {
            Reading readingWifi = ReadingDatabaseService.getLatestReadingForDevice(device.resourceUri, Reading.READING_WIFI);
            Reading readingBattery = ReadingDatabaseService.getLatestReadingForDevice(device.resourceUri, Reading.READING_BATTERY);
            DeviceSettings deviceSettings = DeviceDatabaseService.getDeviceSettingsForDevice(device.id);
            TinyMessageBus.post(new GotDeviceStatistics(readingWifi, readingBattery, deviceSettings, device));
        } else {
            Reading readingTemp = ReadingDatabaseService.getLatestReadingForDevice(device.resourceUri, Reading.READING_TEMPERATURE);
            Reading readingHumidity = ReadingDatabaseService.getLatestReadingForDevice(device.resourceUri, Reading.READING_HUMIDITY);
            Reading readingAirQuality = ReadingDatabaseService.getLatestReadingForDevice(device.resourceUri, Reading.READING_AIR_QUALITY);
            DeviceSettings deviceSettings = DeviceDatabaseService.getDeviceSettingsForDevice(device.id);
            TinyMessageBus.post(new GotDeviceStatistics(readingTemp, readingHumidity, readingAirQuality, deviceSettings, device));
        }
    }

    //================================================================================
    // Device settings
    //================================================================================

    @Subscribe(mode = Subscribe.Mode.Background)
    public void getDeviceSettings(GetDeviceSettings getDeviceSettings) {
        Device device = getDeviceSettings.device;
        DeviceSettings setting = DeviceDatabaseService.getDeviceSettingsForDevice(device.id);

        TinyMessageBus.post(new GotDeviceSettings(setting));
    }


}
