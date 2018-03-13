package is.yranac.canary.model.google;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 9/17/14.
 */
public class Location {

    @SerializedName("lat")
    public double lat;

    @SerializedName("lng")
    public double lng;
}
