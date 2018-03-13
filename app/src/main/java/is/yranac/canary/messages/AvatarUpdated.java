package is.yranac.canary.messages;

import is.yranac.canary.model.customer.Customer;

/**
 * Created by Schroeder on 10/20/14.
 */


public class AvatarUpdated {
    public Customer customer;

    public AvatarUpdated(Customer customer) {
        this.customer = customer;
    }
}
