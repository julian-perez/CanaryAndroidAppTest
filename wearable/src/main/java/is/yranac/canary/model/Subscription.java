package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Schroeder on 5/5/15.
 */
public class Subscription {


    @SerializedName("hasMembership")
    public boolean hasMembership;

    @SerializedName("locationId")
    public int locationId;

    @SerializedName("isTrial")
    public boolean onTrial;

    @SerializedName("currentServiceProfile")
    public ServiceProfile currentServiceProfile;

    @SerializedName("billing")
    public Billing billing;

    @SerializedName("flags")
    public Flags flags;

    public boolean isEmployee() {
        return flags.canaryEmployee;
    }

    public boolean isCsatOverride() {
        return currentServiceProfile.csatOverride;
    }

    public int remainingDays() {


        // Get the represented date in milliseconds
        long milis1 = this.currentServiceProfile.expiresOn.getTime();
        long milis2 = new Date().getTime();

        long days = TimeUnit.MILLISECONDS.toDays(milis1);
        long day2 = TimeUnit.MILLISECONDS.toDays(milis2);

        return (int) (days - day2);
    }


    public boolean haveFullVideo() {
        return hasMembership || (flags != null && flags.legacyFreePlan);
    }

    public boolean doesNotHaveModeConfigs() {
        return !hasMembership && (flags != null && flags.legacyFreePlan);
    }
}
