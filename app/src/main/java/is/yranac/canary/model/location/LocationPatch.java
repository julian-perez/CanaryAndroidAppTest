package is.yranac.canary.model.location;

import com.google.gson.annotations.SerializedName;

public class LocationPatch {
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


    public LocationPatch(Location location) {
        this.name = location.name;
        this.address = location.address;
        this.address2 = location.address2;
        this.state = location.state;
        this.country = location.country;
        this.lat = location.lat;
        this.lng = location.lng;
        this.zip = location.zip;
    }

    // return types on setters are to simplify setting of database values
    public String setCity(String city) {
        return this.city = city;
    }

    public String setCountry(String country) {
        return this.country = country;
    }

    public String setName(String name) {
        return this.name = name;
    }

    public String setAddress(String address) {
        return this.address = address;
    }

    public String setAddress2(String address2) {
        return this.address2 = address2;
    }

    public String setState(String state) {
        return this.state = state;
    }

    public String setZip(String zip) {
        return this.zip = zip;
    }
}
