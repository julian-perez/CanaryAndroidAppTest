package is.yranac.canary.model.device;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 7/20/16.
 */
public class DeviceMotionSettings {

    @SerializedName("detection_threshold")
    public float motionSettings;

    public DeviceMotionSettings(float motionSettings) {

        this.motionSettings = motionSettings;
    }
}
