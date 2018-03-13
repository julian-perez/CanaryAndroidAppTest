package is.yranac.canary.model.location;

import com.google.gson.annotations.SerializedName;

/**
 * Created by michaelschroeder on 5/10/17.
 */

public class LocationGeofencePosition {


    @SerializedName("lat")
    public final double lat;

    @SerializedName("lng")
    public final double lng;

    public LocationGeofencePosition(double lat, double lng) {

        this.lat = lat;
        this.lng = lng;
    }
}
