package is.yranac.canary.model.device;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/8/14.
 */

public class DeviceSiren {

    @SerializedName("command_name")
    String commandName;

    @SerializedName("command_args")
    String commandArgs;

    @SerializedName("device")
    String deviceUri;


    public DeviceSiren(String state, String deviceUri) {

        this.commandName = "siren";
        this.deviceUri = deviceUri;
        this.commandArgs = state;

    }

}
