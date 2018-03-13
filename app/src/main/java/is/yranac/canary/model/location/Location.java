package is.yranac.canary.model.location;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.insurance.InsurancePolicy;
import is.yranac.canary.model.mode.Mode;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 8/8/14.
 */
public class Location {

    private static final String LOG_TAG = "Location";
    @SerializedName("address")
    public String address;

    @SerializedName("address2")
    public String address2;

    @SerializedName("created_at")
    public Date created;

    @SerializedName("city")
    public String city;

    @SerializedName("country")
    public String country;

    @SerializedName("mode")
    public Mode currentMode;

    @SerializedName("customers")
    public List<Customer> customers;

    @SerializedName("customers_present")
    public List<String> customersPresent;

    @SerializedName("devices")
    public List<Device> devices;

    @SerializedName("geofence_radius")
    public int geofenceRadius;

    @SerializedName("id")
    public int id;

    @SerializedName("lat")
    public double lat;

    @SerializedName("lng")
    public double lng;

    @SerializedName("locationOwner")
    public String locationOwner;

    @SerializedName("name")
    public String name;

    @SerializedName("night_mode_enabled")
    public boolean nightModeEnabled;

    @SerializedName("resource_uri")
    public String resourceUri;

    @SerializedName("state")
    public String state;

    @SerializedName("auto_mode_enabled")
    public boolean autoModeEnabled;

    @SerializedName("zip")
    public String zip;

    @SerializedName("is_sharing_data")
    public boolean isSharingData;

    public Date lastModified;

    @SerializedName("is_private")
    public boolean isPrivate;

    @SerializedName("insurance_policy")
    public InsurancePolicy insurancePolicy;

    public boolean trailJustExpired;

    public boolean showCVMaskTutorial;

    public boolean isEighteenDaysOld() {
        long timeDiff = new Date().getTime() - created.getTime();
        return timeDiff > TimeUnit.DAYS.toMillis(18);

    }


    public boolean recentlyUpdated() {
        long timeDiff = new Date().getTime() - lastModified.getTime();
        return timeDiff < TimeUnit.MINUTES.toMillis(5);
    }

    //TODO - Would like to rethink this
    public boolean isUnitedStates() {
        return isUnitedStates(country);
    }

    public static boolean isUnitedStates(@NonNull String country) {
        if (country.equalsIgnoreCase("United States"))
            return true;

        if (country.equalsIgnoreCase("Vereinigte Staaten"))
            return true;

        if (country.equalsIgnoreCase("États-Unis"))
            return true;

        if (country.equalsIgnoreCase("Estados Unidos"))
            return true;

        if (country.equalsIgnoreCase("USA"))
            return true;

        return false;
    }

    public boolean isFrance() {
        if (country.equalsIgnoreCase("France"))
            return true;

        if (country.equalsIgnoreCase("Frankreich"))
            return true;

        return false;
    }

    public boolean isFinland() {
        if (country.equalsIgnoreCase("Finland"))
            return true;


        if (country.equalsIgnoreCase("Finnland"))
            return true;

        if (country.equalsIgnoreCase("Finlande"))
            return true;

        return false;
    }


    public boolean isNetherlands() {
        if (country.equalsIgnoreCase("Netherlands"))
            return true;

        if (country.equalsIgnoreCase("Niederlande"))
            return true;

        if (country.equalsIgnoreCase("Niue"))
            return true;

        return false;
    }

    public boolean isLuxemburg() {
        if (country.equalsIgnoreCase("Luxembourg"))
            return true;

        if (country.equalsIgnoreCase("Luxemburg"))
            return true;

        return false;
    }

    public boolean isUK() {
        if (country.equalsIgnoreCase("United Kingdom"))
            return true;

        if (country.equalsIgnoreCase("Vereinigtes Königreich"))
            return true;

        if (country.equalsIgnoreCase("Royaume-Uni"))
            return true;

        return false;
    }

    public boolean isCanada() {
        if (country.equalsIgnoreCase("Kanada"))
            return true;

        if (country.equalsIgnoreCase("Canada"))
            return true;

        return false;
    }

    public boolean isDenmark() {
        if (country.equalsIgnoreCase("Denmark"))
            return true;

        if (country.equalsIgnoreCase("Dänemark"))
            return true;

        if (country.equalsIgnoreCase("Danemark"))
            return true;

        return false;
    }

    public boolean isAustralia() {

        if (country.equalsIgnoreCase("Australia"))
            return true;

        if (country.equalsIgnoreCase("Australien"))
            return true;

        if (country.equalsIgnoreCase("Australie"))
            return true;

        return false;
    }

    public boolean isSweden() {

        if (country.equalsIgnoreCase("Sweden"))
            return true;

        if (country.equalsIgnoreCase("Schweden"))
            return true;

        if (country.equalsIgnoreCase("Suède"))
            return true;

        return false;
    }

    public boolean isNorway() {

        if (country.equalsIgnoreCase("Norway"))
            return true;

        if (country.equalsIgnoreCase("Norwegen"))
            return true;

        if (country.equalsIgnoreCase("Norvège"))
            return true;

        return false;
    }

    public boolean isBelgium() {

        if (country.equalsIgnoreCase("Belgium"))
            return true;

        if (country.equalsIgnoreCase("Belgique"))
            return true;

        if (country.equalsIgnoreCase("Belgien"))
            return true;

        return false;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }

    public boolean isValidLatLng() {
        if (lat < -90 || lat > 90) {
            return false;
        } else if (lng < -180 || lng > 180) {
            return false;
        }
        return true;
    }

    private int getOwnerCustomerID() {
        return Utils.getIntFromResourceUri(locationOwner);
    }

    public boolean isOwner(Customer customer) {
        if (customer == null
                || TextUtils.isEmpty(this.locationOwner))
            return false;
        return customer.id == this.getOwnerCustomerID();
    }

    private static final String grandfatherDateString = "08/14/2017";
    private static Date grandfatherDate;

    @NonNull
    private static Date grandfatherDate() {
        if (grandfatherDate != null)
            return grandfatherDate;

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date convertedDate = new Date();
        try {
            grandfatherDate = dateFormat.parse(grandfatherDateString);
            convertedDate = grandfatherDate;
        } catch (ParseException e) {
        }

        return convertedDate;
    }

    public boolean createdAfterGrandfather() {
        return grandfatherDate().before(created);
    }
}
