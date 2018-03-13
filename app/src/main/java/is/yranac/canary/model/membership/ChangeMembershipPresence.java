package is.yranac.canary.model.membership;

import com.google.gson.annotations.SerializedName;

/**
 * Created by michaelschroeder on 1/17/18.
 */

public class ChangeMembershipPresence {

    @SerializedName("send_presence_notifications")
    public boolean presence;

    public ChangeMembershipPresence(boolean presence) {

        this.presence = presence;
    }
}
