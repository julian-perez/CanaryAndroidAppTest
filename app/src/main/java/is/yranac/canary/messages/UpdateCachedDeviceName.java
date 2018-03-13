package is.yranac.canary.messages;

/**
 * Created by sergeymorozov on 6/9/15.
 */
public class UpdateCachedDeviceName {
    public int deviceId;
    public String newDeviceName;

    public UpdateCachedDeviceName(int deviceId, String newDeviceName){
        this.deviceId = deviceId;
        this.newDeviceName = newDeviceName;
    }
}
