package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/15/14.
 */
public class Thumbnail {

    private static final String LOG_TAG = "Thumbnail";
    @SerializedName("image_url")
    public String imageUrl;

    @SerializedName("device")
    public String device;

    @SerializedName("length")
    public int length = 0;

    public int entryId;

    public String imageUrl() {
        return imageUrl;
    }

}
