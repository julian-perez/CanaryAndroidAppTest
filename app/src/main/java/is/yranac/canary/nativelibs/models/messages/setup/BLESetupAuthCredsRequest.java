package is.yranac.canary.nativelibs.models.messages.setup;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sergeymorozov on 10/8/15.
 */
public class BLESetupAuthCredsRequest extends BLESetupRequest {

    @SerializedName("TOKEN")
    public String TOKEN;

    @SerializedName("D")
    public int D;
    public BLESetupAuthCredsRequest(String token, boolean deactivate){
        super("05");
        this.TOKEN = token;
        this.D = deactivate ? 1 : 0;
    }
}
