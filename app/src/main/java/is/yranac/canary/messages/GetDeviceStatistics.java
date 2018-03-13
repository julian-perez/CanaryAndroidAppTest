package is.yranac.canary.messages;

import is.yranac.canary.model.device.Device;

/**
 * Created by sergeymorozov on 6/1/16.
 */
public class GetDeviceStatistics {

    public Device device;

    public GetDeviceStatistics(Device device) {
        this.device = device;
    }
}
