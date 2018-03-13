package is.yranac.canary.model.location;

import com.google.gson.annotations.SerializedName;

/**
 * Created by michaelschroeder on 5/12/17.
 */

public class LocationGeofenceRadius {

    @SerializedName("geofence_radius")
    public int geofenceRadius;

    public LocationGeofenceRadius(int geofenceRadius) {

        this.geofenceRadius = geofenceRadius;
    }
}
