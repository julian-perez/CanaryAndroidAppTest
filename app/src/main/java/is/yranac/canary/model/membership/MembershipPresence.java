package is.yranac.canary.model.membership;

import com.google.gson.annotations.SerializedName;

/**
 * Created by michaelschroeder on 1/17/18.
 */

public class MembershipPresence {

    @SerializedName("customer")
    public String customerUri;

    @SerializedName("location")
    public String locationUri;

    @SerializedName("send_presence_notifications")
    public boolean sendPresenceNotifications;
}
