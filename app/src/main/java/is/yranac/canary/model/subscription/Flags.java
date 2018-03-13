package is.yranac.canary.model.subscription;

import com.google.gson.annotations.SerializedName;

/**
 * Created by michaelschroeder on 10/25/16.
 */
public class Flags {

    @SerializedName("canaryEmployee")
    public boolean canaryEmployee;

    @SerializedName("legacyFreePlan")
    public boolean legacyFreePlan;
}
