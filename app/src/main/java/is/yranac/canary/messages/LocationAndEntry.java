package is.yranac.canary.messages;

import com.google.android.gms.wearable.Asset;
import com.google.gson.annotations.SerializedName;

import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.reading.Reading;
import is.yranac.canary.model.subscription.Subscription;

/**
 * Created by Schroeder on 1/6/16.
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


