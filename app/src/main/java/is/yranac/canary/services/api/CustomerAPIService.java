package is.yranac.canary.services.api;

import is.yranac.canary.Constants;
import is.yranac.canary.messages.UpdateCurrentCustomer;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.customer.CustomerChangePassword;
import is.yranac.canary.model.customer.CustomerCreate;
import is.yranac.canary.model.customer.CustomerHasSeenSharePrompt;
import is.yranac.canary.model.customer.CustomerPatch;
import is.yranac.canary.model.customer.CustomerResetPassword;
import is.yranac.canary.model.customer.SoundPatch;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import is.yranac.canary.services.database.CustomerDatabaseService;
import is.yranac.canary.util.TinyMessageBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Schroeder on 8/8/14.
 */
public class CustomerAPIService {

    public static void createCustomer(CustomerCreate customerCreate, Callback<Void> createCallback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(false);

        CustomerService customerService = restAdapter.create(CustomerService.class);
        customerService.createCustomer(customerCreate, createCallback);
    }


    public static void editCustomer(Customer customer, Callback<Void> patchCallback) {
        CustomerPatch customerPatch = new CustomerPatch(customer);

        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        CustomerService customerService = restAdapter.create(CustomerService.class);

        customerService.updateCustomer(customer.id, customerPatch, patchCallback);

    }

    public static void resetCustomerPassword(String email, Callback<Void> callback) {
        CustomerResetPassword customerResetPassword = new CustomerResetPassword(email);

        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(false);
        CustomerService customerService = restAdapter.create(CustomerService.class);
        customerService.resetCustomerPassword(customerResetPassword, callback);

    }

    public static void changeCustomerPassword(int customerId, CustomerChangePassword customerChangePassword, Callback<Void> callback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);

        CustomerService customerService = restAdapter.create(CustomerService.class);
        customerService.changeCustomerPassword(customerId, customerChangePassword, callback);

    }

    public static void setHasSeenSharePrompt(final Customer customer) {
        customer.seenSharePrompt = true;
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);

        CustomerService customerService = restAdapter.create(CustomerService.class);
        customerService.changeHasSeenSharePrompt(customer.id, new CustomerHasSeenSharePrompt(true), new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                CustomerDatabaseService.updateCustomer(customer);
                TinyMessageBus.post(new UpdateCurrentCustomer());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static void editCustomerSound(final String sound) {
        SoundPatch soundPatch = new SoundPatch(sound);

        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        CustomerService customerService = restAdapter.create(CustomerService.class);

        Customer customer = CurrentCustomer.getCurrentCustomer();
        customerService.changeNotificationSound(customer.id, soundPatch, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {

                Customer customer = CurrentCustomer.getCurrentCustomer();
                customer.notificationsSound = sound;

            }

            @Override
            public void failure(RetrofitError error) {


            }
        });

    }

    public interface CustomerService {

        @POST(Constants.CUSTOMER_URI)
        void createCustomer(
                @Body CustomerCreate customerCreate,
                Callback<Void> createCallback
        );

        @PATCH(Constants.CUSTOMER_URI + "{id}/")
        void updateCustomer(@Path("id") int id,
                            @Body CustomerPatch customerPatch,
                            Callback<Void> patchCallback
        );

        @POST(Constants.CUSTOMER_URI)
        void resetCustomerPassword(
                @Body CustomerResetPassword customerResetPassword,
                Callback<Void> callback
        );

        @PATCH(Constants.CUSTOMER_URI + "{id}/")
        void changeCustomerPassword(
                @Path("id") int id,
                @Body CustomerChangePassword customerChangePassword,
                Callback<Void> callback
        );

        @PATCH(Constants.CUSTOMER_URI + "{id}/")
        void changeHasSeenSharePrompt(
                @Path("id") int id,
                @Body CustomerHasSeenSharePrompt customerChangePassword,
                Callback<Void> callback
        );

        @PATCH(Constants.CUSTOMER_URI + "{id}/")
        void changeNotificationSound(
                @Path("id") int id,
                @Body SoundPatch soundPatch,
                Callback<Void> callback
        );
    }
}
