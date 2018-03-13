package is.yranac.canary.model.device;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/8/14.
 */
public class DeviceStreaming {

    @SerializedName("command_name")
    String commandName;

    @SerializedName("command_args")
    String commandArgs;

    @SerializedName("device")
    String deviceUri;

    public DeviceStreaming(boolean uploader, String deviceUri) {

        this.commandName = "video_upload";
        this.deviceUri = deviceUri;
        this.commandArgs = uploader ? "live" : "false";

    }
}
