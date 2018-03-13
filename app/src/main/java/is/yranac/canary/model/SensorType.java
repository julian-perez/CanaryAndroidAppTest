package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cumisnic on 8/4/14.
 */
public class SensorType {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("resource_uri")
    public String resourceUri;
}
