package is.yranac.canary.messages;

import is.yranac.canary.model.device.Device;

/**
 * Created by sergeymorozov on 5/20/16.
 */
public class GetDeviceSettings {
    public Device device;

    public GetDeviceSettings(Device device) {
        this.device = device;
    }
}
