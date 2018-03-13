package is.yranac.canary.nativelibs.models.messages.setup;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sergeymorozov on 10/8/15.
 */
public class BLESetupWiFiCredsRequest extends BLESetupRequest {
    @SerializedName("SSID")
    public String SSID;

    @SerializedName("PASSWORD")
    public String PASSWORD;

    @SerializedName("D")
    public int D;

    public BLESetupWiFiCredsRequest(String ssid, String password, boolean deactivate) {
        super("03");
        this.SSID = ssid;
        this.PASSWORD = password;
        this.D = deactivate ? 1 : 0;
    }
}
