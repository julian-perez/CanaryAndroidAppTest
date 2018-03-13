package is.yranac.canary.model.subscription;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by michaelschroeder on 10/23/16.
 */
public class BillingInfo {


    @SerializedName("nextBillingDate")
    public Date nextBillingDate;
}
