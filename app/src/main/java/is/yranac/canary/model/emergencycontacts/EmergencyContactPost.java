package is.yranac.canary.model.emergencycontacts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 10/6/15.
 */
public class EmergencyContactPost {
    @SerializedName("contact_type")
    public String contactType;

    @SerializedName("phone_number")
    public String phoneNumber;

    @SerializedName("location")
    public String locationUri;

    @SerializedName("provider")
    public String provider;

    public EmergencyContactPost(EmergencyContact contact) {
        this.locationUri = contact.locationUri;
        this.contactType = contact.contactType;
        if (contact.phoneNumber != null)
            this.phoneNumber = contact.phoneNumber.replaceAll("[^\\d.]", "");

        this.provider = contact.provider;

    }

}
