package is.yranac.canary.messages;

import is.yranac.canary.model.device.Device;

/**
 * Created by sergeymorozov on 6/10/15.
 */
public class RefreshedDevice {
    public Device newDevice;
    public RefreshedDevice(Device newDevice){
        this.newDevice = newDevice;
    }
}
