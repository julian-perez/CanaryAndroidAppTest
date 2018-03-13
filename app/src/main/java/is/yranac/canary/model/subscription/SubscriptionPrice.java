package is.yranac.canary.model.subscription;

import com.google.gson.annotations.SerializedName;

/**
 * Created by michaelschroeder on 10/3/16.
 */
public class SubscriptionPrice {

    @SerializedName("deviceCount")
    public int deviceCount;

    @SerializedName("pricing")
    public Prices prices;
}
