package is.yranac.canary.model.subscription;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by michaelschroeder on 10/3/16.
 */
public class SubscriptionPriceResponse {

    @SerializedName("objects")
    public List<SubscriptionPrice> prices;
}
