package is.yranac.canary.model.subscription;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by michaelschroeder on 10/3/16.
 */
public class Prices {

    @SerializedName("monthly")
    public int monthlyPrices;

    @SerializedName("annual")
    public int annualPrices;
}
