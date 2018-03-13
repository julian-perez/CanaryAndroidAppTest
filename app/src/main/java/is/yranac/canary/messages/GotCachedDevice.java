package is.yranac.canary.messages;

import is.yranac.canary.model.device.Device;

/**
 * Created by sergeymorozov on 6/10/15.
 */
public class GotCachedDevice {
    public Device device;
    public GotCachedDevice(Device device){
        this.device = device;
    }
}
