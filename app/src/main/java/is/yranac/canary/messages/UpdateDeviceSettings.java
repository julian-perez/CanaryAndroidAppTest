package is.yranac.canary.messages;

import is.yranac.canary.model.device.Device;

/**
 * Created by Schroeder on 4/29/16.
 */
public class UpdateDeviceSettings {
    public final Device device;

    public UpdateDeviceSettings(Device device){
        this.device = device;
    }
}
