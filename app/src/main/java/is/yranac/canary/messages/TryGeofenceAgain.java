package is.yranac.canary.messages;

/**
 * Created by michaelschroeder on 4/14/17.
 */

public class TryGeofenceAgain {
    public final String locationUri;
    public final double latitude;
    public final double longitude;
    public final boolean arrival;
    public final int retryCount;

    public TryGeofenceAgain(String locationUri, double latitude, double longitude, boolean arrival, int retryCount) {
        this.locationUri = locationUri;
        this.latitude = latitude;
        this.longitude = longitude;
        this.arrival = arrival;
        this.retryCount = retryCount;
    }
}
