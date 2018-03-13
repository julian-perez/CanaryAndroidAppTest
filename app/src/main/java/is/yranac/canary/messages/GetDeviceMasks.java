package is.yranac.canary.messages;

/**
 * Created by sergeymorozov on 11/29/16.
 */

public class GetDeviceMasks {
    public int deviceId;
    public boolean refreshCachedData;

    public GetDeviceMasks(int deviceId, boolean refreshCachedData) {
        this.deviceId = deviceId;
        this.refreshCachedData = refreshCachedData;
    }
}
