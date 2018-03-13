package is.yranac.canary.services.api;

import is.yranac.canary.Constants;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.model.subscription.SubscriptionPriceResponse;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import is.yranac.canary.services.database.SubscriptionPlanDatabaseService;
import is.yranac.canary.services.database.SubscriptionPricesDatabaseService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Schroeder on 5/5/15.
 */
public class SubscriptionAPIService {

    public static Subscription getSubscription(int locationId) throws RetrofitError {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        SubscriptionService planTypeService = restAdapter.create(SubscriptionService.class);
        Subscription subscription = planTypeService.getSubscription(locationId);
        SubscriptionPlanDatabaseService.insertSubscriptionTypes(subscription);
        return subscription;

    }


    public static SubscriptionPriceResponse getPrices() throws RetrofitError {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        SubscriptionService planTypeService = restAdapter.create(SubscriptionService.class);
        SubscriptionPriceResponse response = planTypeService.getSubscriptionPrices();
        SubscriptionPricesDatabaseService.insertSubscriptionPricesData(response.prices);
        return response;

    }


    public static void getSubscription(final int locationId, final Callback<Subscription> callback) {


        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        SubscriptionService planTypeService = restAdapter.create(SubscriptionService.class);

        planTypeService.getSubscription(locationId, new Callback<Subscription>() {
            @Override
            public void success(Subscription subscription, Response response) {
                SubscriptionPlanDatabaseService.insertSubscriptionTypes(subscription);
                callback.success(subscription, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });

    }


    interface SubscriptionService {

        @GET(Constants.SUBSCRIPTIONS_URI + "{id}/")
        Subscription getSubscription(
                @Path("id") int locationId);


        @GET(Constants.SUBSCRIPTIONS_URI + "{id}/")
        void getSubscription(
                @Path("id") int locationId,
                Callback<Subscription> callback);


        @GET(Constants.PRICES_URI)
        SubscriptionPriceResponse getSubscriptionPrices();
    }
}