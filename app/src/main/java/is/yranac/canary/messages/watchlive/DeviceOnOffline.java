package is.yranac.canary.messages.watchlive;

/**
 * Created by Schroeder on 7/15/15.
 */
public class DeviceOnOffline {
    public final String device;
    public final boolean online;

    public DeviceOnOffline(String device, boolean online) {
        this.device = device;
        this.online = online;
    }
}
