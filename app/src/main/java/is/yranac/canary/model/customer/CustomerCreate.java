package is.yranac.canary.model.customer;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/8/14.
 */
public class CustomerCreate {
    @SerializedName("first_name")
    public String firstName;

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("email")
    public String email;

    @SerializedName("phone")
    public String mobilePhone;

    @SerializedName("password1")
    public String password1;

    @SerializedName("password2")
    public String password2;

    @SerializedName("country_identifier")
    public String dialCode;

    @SerializedName("celsius")
    public boolean celsius;

    @SerializedName("language_preference")
    public String languagePreference;


    public CustomerCreate(Customer customer, String password) {
        this.firstName = customer.firstName;
        this.lastName = customer.lastName;
        this.email = customer.email;
        this.mobilePhone = customer.phone;
        this.password1 = password;
        this.password2 = password;
        this.dialCode = customer.dialCode;
        this.celsius = customer.celsius;
        this.languagePreference = customer.languagePreference;
    }
}
