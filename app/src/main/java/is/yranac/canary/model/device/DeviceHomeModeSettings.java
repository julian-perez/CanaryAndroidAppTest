package is.yranac.canary.model.device;

import com.google.gson.annotations.SerializedName;

import is.yranac.canary.Constants;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 7/20/16.
 */
public class DeviceHomeModeSettings {

    @SerializedName("home_mode")
    public String homeMode;


    public DeviceHomeModeSettings(int mode) {
        this.homeMode = Utils.buildResourceUri(Constants.MODES_URI, mode);
    }
}
