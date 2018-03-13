package is.yranac.canary.messages;

import is.yranac.canary.model.masking.DeviceMasks;

/**
 * Created by sergeymorozov on 11/29/16.
 */

public class GotDeviceMasks {
    public DeviceMasks deviceMasks;

    public GotDeviceMasks(DeviceMasks masks) {
        this.deviceMasks = masks;
    }
}
