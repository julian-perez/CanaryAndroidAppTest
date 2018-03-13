package is.yranac.canary.model.device;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sergeymorozov on 11/11/15.
 */
public class DeviceActivation {

    public static final String activation_status_activated = "activated";
    public static final String activation_status_deactivated = "deactivated";

    @SerializedName("activation_status")
    String activationStatus;

    @SerializedName("password")
    String password;

    public DeviceActivation(String activationStatus, String password) {
        this.activationStatus = activationStatus;
        this.password = password;
    }
}
