package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

import is.yranac.canary.Constants;
import is.yranac.canary.model.mode.Mode;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 4/22/16.
 */
public class DeviceModeSettings {

    @SerializedName("home_mode")
    public String homeMode;

    @SerializedName("night_mode")
    public String nightMode;

    public DeviceModeSettings(Mode homeMode, Mode nightMode) {
        if (homeMode != null)
            this.homeMode = Utils.buildResourceUri(Constants.MODES_URI, homeMode.id);
        if (nightMode != null)
            this.nightMode = Utils.buildResourceUri(Constants.MODES_URI, nightMode.id);
    }
}
