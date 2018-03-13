package is.yranac.canary.model.location;



import com.google.gson.annotations.SerializedName;

import java.util.List;

import is.yranac.canary.model.Meta;

/**
 * Created by Schroeder on 8/8/14.
 */
public class LocationResponse {

    @SerializedName("meta")
    public Meta meta;

    @SerializedName("objects")
    public List<Location> locations;
}
