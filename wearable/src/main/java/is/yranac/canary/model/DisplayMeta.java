package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 10/6/15.
 */
public class DisplayMeta {

    @SerializedName("notified")
    public Notified notified;

    @SerializedName("location_mode_name")
    public String locationMode;

    @SerializedName("location_is_private")
    public boolean locationIsPrivate;
}
