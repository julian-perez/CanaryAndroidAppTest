package is.yranac.canary.model.customer;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.messages.UpdateCurrentCustomer;
import is.yranac.canary.services.database.CustomerDatabaseService;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.UserUtils;

/**
 * Created by Schroeder on 3/8/16.
 */
public class CurrentCustomer {

    private static final String LOG_TAG = "CurrentCustomer";
    private static Customer customer = null;
    private static CurrentCustomer currentCustomer;


    public static Customer getCurrentCustomer() {
        if (customer == null) {
            customer = CustomerDatabaseService.getCurrentCustomer();
        }
        return customer;
    }

    public static void initCache(){
        currentCustomer = new CurrentCustomer();

        TinyMessageBus.register(currentCustomer);
    }

    @Subscribe(mode = Subscribe.Mode.Background)
    public void updateCurrentCustomer(UpdateCurrentCustomer updateCurrentCustomer){
        customer = CustomerDatabaseService.getCurrentCustomer();
    }

    public static void clearCache() {
        customer = null;
    }
}
