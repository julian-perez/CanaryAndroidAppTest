package is.yranac.canary.model;

import com.google.android.gms.wearable.Asset;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Schroeder on 8/8/14.
 */
public class LocationAndEntry {

    @SerializedName("location")
    public Location location;

    @SerializedName("subscription")
    public Subscription subscription;

    @SerializedName("entry")
    public Entry entry;

    @SerializedName("battery")
    public Reading battery;

    @SerializedName("wifi")
    public Reading wifi;

    @SerializedName("temp")
    public Reading temp;

    @SerializedName("air")
    public Reading air;

    @SerializedName("humidity")
    public Reading humidity;

    public Asset asset;
}
