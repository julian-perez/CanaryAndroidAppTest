package is.yranac.canary.messages;

import is.yranac.canary.model.device.DeviceSettings;

/**
 * Created by sergeymorozov on 5/20/16.
 */
public class GotDeviceSettings {
    public DeviceSettings deviceSettings;

    public GotDeviceSettings(DeviceSettings deviceSettings) {
        this.deviceSettings = deviceSettings;
    }
}
