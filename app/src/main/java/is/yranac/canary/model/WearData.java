package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import is.yranac.canary.messages.LocationAndEntry;
import is.yranac.canary.model.customer.Customer;

/**
 * Created by michaelschroeder on 10/7/16.
 */

public class WearData {
    @SerializedName("locationAndEntries")
    public List<LocationAndEntry> locationAndEntries;

    @SerializedName("customer")
    public Customer currentCustomer;
}
