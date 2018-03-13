package is.yranac.canary.model;

/**
 * Created by Schroeder on 3/8/16.
 */
public class CurrentCustomer {

    private static final String LOG_TAG = "CurrentCustomer";
    private static Customer customer = null;


    public static Customer getCurrentCustomer() {
        return customer;
    }

    public static void setCustomer(Customer customer1) {
        customer = customer1;
    }

}
