package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by narendramanoharan on 6/20/16.
 */
public class Location {

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

    @SerializedName("auto_mode")
    public Mode autoMode;


    @SerializedName("zip")
    public String zip;

    @SerializedName("is_sharing_data")
    public boolean isSharingData;

    public Date lastModified;

    @SerializedName("is_private")
    public boolean isPrivate;

}
