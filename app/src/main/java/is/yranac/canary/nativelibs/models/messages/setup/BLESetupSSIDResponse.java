package is.yranac.canary.nativelibs.models.messages.setup;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergeymorozov on 10/8/15.
 */
public class BLESetupSSIDResponse extends BLESetupResponce {

    @SerializedName("NETWORKS")
    public List<String> NETWORKS;

    public BLESetupSSIDResponse() {
        super("11");
    }

    public List<String> getNetworks(){
        if(this.NETWORKS == null)
            return new ArrayList<String>();
        return NETWORKS;
    }
}
