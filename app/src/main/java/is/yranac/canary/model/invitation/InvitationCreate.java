package is.yranac.canary.model.invitation;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/10/14.
 */
public class InvitationCreate {

    public InvitationCreate(Invitation invitation){
        this.email = invitation.email;
        this.firstName = invitation.firstName;
        this.lastName = invitation.lastName;
        this.locationUri = invitation.locationUri;
        this.phone = invitation.phone;
        this.userType = invitation.userType;
    }
    @SerializedName("email")
    public String email;

    @SerializedName("first_name")
    public String firstName;

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("location")
    public String locationUri;

    @SerializedName("phone")
    public String phone;

    @SerializedName("user_type")
    public String userType;
}

