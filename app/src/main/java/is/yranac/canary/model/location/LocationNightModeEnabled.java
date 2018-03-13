package is.yranac.canary.model.location;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 5/2/16.
 */
public class LocationNightModeEnabled {

    @SerializedName("night_mode_enabled")
    public boolean nightModeEnabled;

    public LocationNightModeEnabled(boolean nightModeEnabled){
        this.nightModeEnabled = nightModeEnabled;
    }

}
