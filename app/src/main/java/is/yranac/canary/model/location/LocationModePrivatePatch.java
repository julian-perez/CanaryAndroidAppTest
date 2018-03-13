package is.yranac.canary.model.location;

import com.google.gson.annotations.SerializedName;

public class LocationModePrivatePatch {

    @SerializedName("is_private")
    public boolean isPrivate;


    public LocationModePrivatePatch(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
