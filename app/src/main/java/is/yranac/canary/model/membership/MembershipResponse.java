package is.yranac.canary.model.membership;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import is.yranac.canary.model.Meta;

/**
 * Created by Schroeder on 8/8/14.
 */
public class MembershipResponse {

    @SerializedName("meta")
    public Meta meta;

    @SerializedName("objects")
    public List<Membership> memberships;
}
