package is.yranac.canary.model.invitation;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/10/14.
 */
public class Invitation {

    @SerializedName("email")
    public String email;


    @SerializedName("first_name")
    public String firstName;


    @SerializedName("id")
    public int id;

    @SerializedName("inviter")
    public String inviter;


    @SerializedName("last_name")
    public String lastName;


    @SerializedName("location")
    public String locationUri;


    @SerializedName("phone")
    public String phone;


    @SerializedName("resource_uri")
    public String resourceUri;

    @SerializedName("status")
    public String status;

    @SerializedName("user_type")
    public String userType;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getInitials() {

        String initials = "";
        if (firstName == null || firstName.length() == 0)
            initials += "";
        else
            initials += firstName.substring(0, 1);

        if (lastName == null || lastName.length() == 0)
            initials += "";
        else
            initials += lastName.substring(0, 1);

        return initials;

    }
}
