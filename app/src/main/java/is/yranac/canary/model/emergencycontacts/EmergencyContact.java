package is.yranac.canary.model.emergencycontacts;

import android.telephony.PhoneNumberUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 10/4/15.
 */
public class EmergencyContact {



    public enum ContactType {
        ems ("ems"),
        police ("police"),
        fire ("fire");

        private final String name;

        ContactType(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName != null) && name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    @SerializedName("id")
    public int id;

    @SerializedName("contact_type")
    public String contactType;

    @SerializedName("phone_number")
    public String phoneNumber;

    @SerializedName("location")
    public String locationUri;

    @SerializedName("provider")
    public String provider;

    public EmergencyContact(){
    }

    public EmergencyContact(String locationUri, String contactType, String phoneNumber, String provider) {
        this.locationUri = locationUri;
        this.contactType = contactType;
        this.phoneNumber = PhoneNumberUtils.stripSeparators(phoneNumber);
        this.provider = provider;

    }
}
