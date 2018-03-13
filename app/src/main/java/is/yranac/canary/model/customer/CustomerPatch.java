package is.yranac.canary.model.customer;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/8/14.
 */
public class CustomerPatch {
    @SerializedName("first_name")
    String firstName;

    @SerializedName("last_name")
    String lastName;

    @SerializedName("customers")
    String email;

    @SerializedName("phone")
    String mobilePhone;

    @SerializedName("celsius")
    boolean celsius;

    @SerializedName("country_identifier")
    public String dialCode;

    @SerializedName("language_preference")
    public String languagePreference;

    @SerializedName("has_seen_data_share_prompt")
    public boolean seenSharePrompt;


    public CustomerPatch(Customer customer) {

        this.firstName = customer.firstName;
        this.lastName = customer.lastName;
        this.email = customer.email;
        this.mobilePhone = customer.phone;
        this.celsius = customer.celsius;
        this.dialCode = customer.dialCode;
        this.languagePreference = customer.languagePreference;
        this.seenSharePrompt = customer.seenSharePrompt;
    }
}
