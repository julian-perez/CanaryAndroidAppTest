package is.yranac.canary.messages;

import is.yranac.canary.model.device.DeviceSettings;

/**
 * Created by Schroeder on 5/2/16.
 */
public class NewDeviceSettings {
    public DeviceSettings deviceSettings;

    public NewDeviceSettings(DeviceSettings deviceSettings) {

        this.deviceSettings = deviceSettings;
    }
}
