package is.yranac.canary.nativelibs.models.messages.setup;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sergeymorozov on 10/8/15.
 */
public class BLESetupInitResponse extends BLESetupResponce {

    @SerializedName("SERIAL")
    public String SERIAL;

    @SerializedName("VERSION")
    public String VERSION;

    public BLESetupInitResponse(){
        super("02");
    }

    public String getSERIAL() {
        return SERIAL;
    }

    public String getVERSION() {
        return VERSION;
    }
}
