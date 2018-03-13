package is.yranac.canary.model.devicetoken;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/13/14.
 */
public class ChangeDeviceToken {

    @SerializedName("active")
    public boolean active;

    @SerializedName("channel")
    public String channel;

    public ChangeDeviceToken(boolean active, String channel){
        this.active = active;
        this.channel = channel;
    }
}
