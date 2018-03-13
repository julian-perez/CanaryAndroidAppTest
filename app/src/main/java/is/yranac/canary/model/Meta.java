package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cumisnic on 8/4/14.
 */
public class Meta {
    @SerializedName("offset")
    public int    offset;

    @SerializedName("next")
    public String next;

    @SerializedName("limit")
    public int    limit;

    @SerializedName("previous")
    public String previous;

    @SerializedName("total_count")
    public int    totalCount;
}
