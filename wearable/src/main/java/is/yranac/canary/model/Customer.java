package is.yranac.canary.model;

import com.google.android.gms.wearable.Asset;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/8/14.
 */
public class Customer {

    @SerializedName("avatar")
    public Avatar avatar;

    @SerializedName("created")
    public String created;

    @SerializedName("current_location")
    public String currentLocation;

    @SerializedName("date_joined")
    public String dateJoined;

    @SerializedName("default_location")
    public String defaultLocation;

    @SerializedName("email")
    public String email;

    @SerializedName("first_name")
    public String firstName;

    @SerializedName("has_seen_data_share_prompt")
    public boolean seenSharePrompt;

    @SerializedName("id")
    public int id;

    @SerializedName("is_active")
    public boolean isActive;

    @SerializedName("last_location_change")
    public String lastLocationChange;

    @SerializedName("language_preference")
    public String languagePreference;

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("notification_preference")
    public String notificationPreference;

    @SerializedName("notifications_enabled")
    public boolean notificationsEnabled;

    @SerializedName("phone")
    public String phone;

    @SerializedName("resource_uri")
    public String resourceUri;

    @SerializedName("username")
    public String username;

    @SerializedName("celsius")
    public boolean celsius;

    @SerializedName("country_identifier")
    public String dialCode;

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

    public Asset asset;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object object){
        if(object == null || !(object instanceof Customer))
            return false;
        return this.id == ((Customer)object).id;
    }
}
