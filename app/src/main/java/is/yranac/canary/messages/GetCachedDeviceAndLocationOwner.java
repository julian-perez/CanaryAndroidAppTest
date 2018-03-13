package is.yranac.canary.messages;

/**
 * Created by sergeymorozov on 11/16/15.
 */
public class GetCachedDeviceAndLocationOwner {
    int deviceId;

    public GetCachedDeviceAndLocationOwner(int deviceId){
        this.deviceId = deviceId;
    }

    public int getDeviceId() {
        return deviceId;
    }
}
