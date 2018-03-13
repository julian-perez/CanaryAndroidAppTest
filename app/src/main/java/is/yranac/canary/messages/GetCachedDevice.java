package is.yranac.canary.messages;

/**
 * Created by sergeymorozov on 12/2/15.
 */
public class GetCachedDevice {

    public final String deviceUri;

    public GetCachedDevice(String deviceUri) {
        this.deviceUri = deviceUri;
    }

}
