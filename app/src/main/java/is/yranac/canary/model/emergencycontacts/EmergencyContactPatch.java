package is.yranac.canary.model.emergencycontacts;

import android.telephony.PhoneNumberUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 10/6/15.
 */
public class EmergencyContactPatch {
    @SerializedName("contact_type")
    public String contactType;

    @SerializedName("phone_number")
    public String phoneNumber;

    @SerializedName("location")
    public String locationUri;


    @SerializedName("provider")
    public String provider;

    public EmergencyContactPatch(EmergencyContact contact) {
        this.locationUri = contact.locationUri;
        this.contactType = contact.contactType;
        this.phoneNumber = PhoneNumberUtils.stripSeparators(contact.phoneNumber);
        this.provider = contact.provider;


    }
}
