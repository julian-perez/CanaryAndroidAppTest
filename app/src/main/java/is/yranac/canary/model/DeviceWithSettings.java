package is.yranac.canary.model;

import android.support.annotation.NonNull;

import java.security.InvalidParameterException;

import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceSettings;
import is.yranac.canary.model.device.DeviceType;

/**
 * Created by sergeymorozov on 5/17/16.
 */
public class DeviceWithSettings {

    private final Device device;
    private final DeviceSettings deviceSettings;

    public DeviceWithSettings(@NonNull Device device, @NonNull DeviceSettings settings) {

        this.device = device;
        this.deviceSettings = settings;
    }

    public Device getDevice() {
        return device;
    }

    public DeviceSettings getDeviceSettings() {
        return deviceSettings;
    }

    public DeviceType getDeviceType() {

        return device.deviceType;
    }
}

