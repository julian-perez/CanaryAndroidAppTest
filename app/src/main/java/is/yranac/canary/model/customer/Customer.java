package is.yranac.canary.model.customer;

import com.google.gson.annotations.SerializedName;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import is.yranac.canary.model.avatar.Avatar;
import is.yranac.canary.util.Utils;

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

    @SerializedName("email")
    public String email;

    @SerializedName("first_name")
    public String firstName;

    @SerializedName("has_seen_data_share_prompt")
    public boolean seenSharePrompt;

    @SerializedName("id")
    public int id;

    @SerializedName("last_location_change")
    public String lastLocationChange;

    @SerializedName("language_preference")
    public String languagePreference;

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("notification_sound")
    public String notificationsSound;

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
        if (firstName != null && firstName.length() != 0)
            initials += firstName.substring(0, 1);

        if (lastName != null && lastName.length() != 0)
            initials += lastName.substring(0, 1);

        return initials;

    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Customer))
            return false;
        return this.id == ((Customer) object).id;
    }


    /**
     * CHF: 1099,
     * DKK: 7900,
     * EUR: 999,
     * GBP: 799,
     * NOK: 9900,
     * SEK: 9900,
     * USD: 999
     *
     * @return
     */
    public String getCurrency(double price) {

        Locale locale = new Locale(this.languagePreference, this.dialCode);
        Currency currency = Currency.getInstance(locale);
        if (!supportedCurrency(currency)) {
            currency = Currency.getInstance("USD");
        }

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(currency);


        return format.format(price);
    }


    public String getCurrency(double price, String currencyCode) {

        Currency currency = Currency.getInstance(currencyCode);
        if (!supportedCurrency(currency)) {
            currency = Currency.getInstance("USD");
        }

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(currency);


        return format.format(price);
    }


    private boolean supportedCurrency(Currency currency) {
        switch (currency.getCurrencyCode()) {

            case "CHF":
            case "DKK":
            case "EUR":
            case "GBP":
            case "NOK":
            case "SEK":
            case "USD":
                return true;
        }
        return false;
    }


    public int getCurrentLocation() {
        return Utils.getIntFromResourceUri(currentLocation);
    }
}
