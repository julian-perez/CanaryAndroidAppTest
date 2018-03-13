package is.yranac.canary.model.google;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Schroeder on 9/17/14.
 */
public class GooglePlace {

    @SerializedName("geometry")
    public Geometry geometry;

    @SerializedName("icon")
    public String icon;

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("place_id")
    public String placeId;

    @SerializedName("reference")
    public String reference;

    @SerializedName("scope")
    public String scope;

    @SerializedName("types")
    public List<String> types;

    @SerializedName("vicinity")
    public String vicinity;


}
