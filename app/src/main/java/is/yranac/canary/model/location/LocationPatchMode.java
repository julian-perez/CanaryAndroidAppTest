package is.yranac.canary.model.location;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/8/14.
 */
public class LocationPatchMode {

    @SerializedName("mode")
    public String currentMode;

    @SerializedName("is_private")
    public boolean isPrivate = false;


    public LocationPatchMode(String currentMode) {
        this.currentMode = currentMode;
    }
}
