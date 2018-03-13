package is.yranac.canary.model.invitation;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import is.yranac.canary.model.Meta;

/**
 * Created by Schroeder on 8/10/14.
 */
public class InvitationResponse {


    @SerializedName("meta")
    public Meta meta;

    @SerializedName("objects")
    public List<Invitation> invitations;
}
