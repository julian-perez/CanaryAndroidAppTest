package is.yranac.canary.model.device;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/8/14.
 */
public class DeviceNamePatch {
    @SerializedName("name")
    String name;

    public DeviceNamePatch(String name){
        this.name = name;
    }
}
