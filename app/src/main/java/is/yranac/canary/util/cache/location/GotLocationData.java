package is.yranac.canary.util.cache.location;

import java.util.List;

import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.insurance.InsurancePolicy;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.subscription.Subscription;

/**
 * Created by michaelschroeder on 5/11/17.
 */

public class GotLocationData {
    public final Location location;
    public final Subscription subscription;
    public final InsurancePolicy insurancePolicy;
    public final List<Device> deviceList;
    public final List<Customer> customers;

    public GotLocationData(Location location, Subscription subscription, InsurancePolicy insurancePolicy,
                           List<Device> deviceList, List<Customer> customers) {

        this.location = location;
        this.subscription = subscription;
        this.insurancePolicy = insurancePolicy;
        this.deviceList = deviceList;

        this.customers = customers;
    }
}
