package is.yranac.canary.model;

import com.google.android.gms.wearable.Asset;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Schroeder on 8/8/14.
 */
public class WearData {


    @SerializedName("locationAndEntries")
    public List<LocationAndEntry> locationAndEntries;

    @SerializedName("customer")
    public Customer currentCustomer;
}
