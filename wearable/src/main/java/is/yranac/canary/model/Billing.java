package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by michaelschroeder on 10/3/16.
 */
public class Billing {


    public static final String MONTHLY = "monthly";

    public static final String ANNUAL = "annual";

    @SerializedName("currentPrice")
    public int price;

    @SerializedName("currency")
    public String currency;

    @SerializedName("recurrence")
    public String period;
}
