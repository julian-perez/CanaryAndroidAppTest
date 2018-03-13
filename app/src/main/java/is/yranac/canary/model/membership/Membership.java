package is.yranac.canary.model.membership;

import com.google.gson.annotations.SerializedName;

import is.yranac.canary.model.customer.Customer;

/**
 * Created by Schroeder on 8/8/14.
 */
public class Membership {

    @SerializedName("resource_uri")
    public String resourceUri;

    @SerializedName("customer")
    public Customer customer;

    @SerializedName("location")
    public String location;

}
