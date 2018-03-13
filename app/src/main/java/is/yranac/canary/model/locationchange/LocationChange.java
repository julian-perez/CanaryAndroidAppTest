package is.yranac.canary.model.locationchange;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/8/14.
 */
public class LocationChange {

    @SerializedName("location")
    String location;

    @SerializedName("lat")
    double lat;

    @SerializedName("lng")
    double lng;

    @SerializedName("arrival")
    boolean arrival;

    public LocationChange(String locationUri, double lat, double lng, boolean arrival) {
        this.location = locationUri;
        this.lat = lat;
        this.lng = lng;
        this.arrival = arrival;

    }
}
