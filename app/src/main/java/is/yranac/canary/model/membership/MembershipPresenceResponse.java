package is.yranac.canary.model.membership;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by michaelschroeder on 1/17/18.
 */

public class MembershipPresenceResponse {

    @SerializedName("objects")
    public List<MembershipPresence> membershipPresences;
}
