package is.yranac.canary.model.device;



import com.google.gson.annotations.SerializedName;

import java.util.List;

import is.yranac.canary.model.Meta;

/**
 * Created by Nick on 8/16/14.
 */
public class DeviceResponse {

    @SerializedName("meta")
    public Meta meta;

    @SerializedName("objects")
    public List<Device> devices;
}
