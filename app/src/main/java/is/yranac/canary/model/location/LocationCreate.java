package is.yranac.canary.model.location;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Schroeder on 8/8/14.
 */
public class LocationCreate {

    @SerializedName("city")
    public String city;

    @SerializedName("country")
    public String country;

    @SerializedName("lat")
    public double lat;

    @SerializedName("lng")
    public double lng;

    @SerializedName("address")
    public String address;

    @SerializedName("address2")
    public String address2;

    @SerializedName("state")
    public String state;

    @SerializedName("name")
    public String name;

    @SerializedName("zip")
    public String zip;

    public LocationCreate(Location location) {
        this.city = location.city;
        this.country = location.country;
        this.lat = round(location.lat, 6);
        this.lng = round(location.lng, 6);
        this.name = location.name;
        this.address = location.address;
        this.address2 = location.address2;
        this.state = location.state;
        this.zip = location.zip;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
