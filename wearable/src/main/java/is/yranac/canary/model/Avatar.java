package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/8/14.
 */
public class Avatar {

    @SerializedName("customer")
    public String customer;

    @SerializedName("id")
    public int id;

    @SerializedName("image")
    public String image;

    @SerializedName("resource_uri")
    public String resourceUri;

    @SerializedName("thumbnail_url")
    public String thumbnailUrl;

}
