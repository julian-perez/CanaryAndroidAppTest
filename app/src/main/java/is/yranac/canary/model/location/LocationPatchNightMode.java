package is.yranac.canary.model.location;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/8/14.
 */
public class LocationPatchNightMode {


    @SerializedName("night_mode_enabled")
    public boolean nightModeEnabled;

    public LocationPatchNightMode(boolean nightModeEnabled) {
        this.nightModeEnabled = nightModeEnabled;
    }
}
