package is.yranac.canary.messages;

import is.yranac.canary.model.device.Device;

/**
 * Created by sergeymorozov on 11/13/15.
 */
public class UpdateCachedDevice {
    public Device device;

    public UpdateCachedDevice(Device device){
        this.device = device;
    }
}
