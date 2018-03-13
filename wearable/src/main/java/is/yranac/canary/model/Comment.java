package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by cumisnic on 8/7/14.
 */
public class Comment {
    @SerializedName("id")
    public int id;

    @SerializedName("body")
    public String body;

    @SerializedName("created")
    public Date created;

    @SerializedName("customer")
    public String customerUri;

    @SerializedName("entry")
    public String entryUri;

    @SerializedName("modified")
    public Date modified;

    @SerializedName("resource_uri")
    public String resourceUri;

}
