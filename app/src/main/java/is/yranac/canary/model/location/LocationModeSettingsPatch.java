package is.yranac.canary.model.location;

import com.google.gson.annotations.SerializedName;

public class LocationModeSettingsPatch {

    @SerializedName("auto_mode_enabled")
    public boolean autoModeEnabled;

    public LocationModeSettingsPatch(boolean onOff) {
        this.autoModeEnabled = onOff;
    }
}
