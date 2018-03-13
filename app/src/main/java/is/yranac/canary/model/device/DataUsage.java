package is.yranac.canary.model.device;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by michaelschroeder on 4/3/17.
 */

public class DataUsage {

    @SerializedName("usage")
    public float usage;

    @SerializedName("timestamp")
    public Date timestamp;
}
