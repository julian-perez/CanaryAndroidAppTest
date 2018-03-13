package is.yranac.canary.model.masking;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergeymorozov on 11/29/16.
 */

public class DeviceMasks {

    @SerializedName("objects")
    public List<CVMask> deviceMasks;

    public DeviceMasks() {
        deviceMasks = new ArrayList<>();
    }

}
