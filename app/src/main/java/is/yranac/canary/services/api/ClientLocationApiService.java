package is.yranac.canary.services.api;

import java.util.Date;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.model.locationchange.ClientLocation;
import is.yranac.canary.model.locationchange.ClientLocationList;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import is.yranac.canary.services.database.CacheGeofenceDatabaseService;
import is.yranac.canary.util.TinyMessageBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.PATCH;
import retrofit.http.POST;

/**
 * Created by Schroeder on 7/31/15.
 */
public class ClientLocationApiService {

    public static void updateLocation(double lat, double lng, String type, float accuracy, float batteryPct,
                                      String uuid, Date date, Date locationDate, boolean wifi, boolean gps) {
        final ClientLocation clientLocation = new ClientLocation(lat, lng, type, accuracy, batteryPct, uuid, date, locationDate
                , wifi, gps);
        updateLocation(clientLocation);
    }

    public static void updateLocation(final ClientLocation clientLocation) {

        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        ClientLocationChange entryService = restAdapter.create(ClientLocationChange.class);
        entryService.createClientLocationChange(clientLocation, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
            }

            @Override
            public void failure(RetrofitError error) {
                TinyMessageBus.post(clientLocation);

            }
        });

    }

    public static void updateLocation(final List<ClientLocation> clientLocations) {
        RestAdapter restAdapter =  RetroFitAdapterFactory.getNewDefaultAdapter(true);
        ClientLocationChange entryService = restAdapter.create(ClientLocationChange.class);
        entryService.createClientLocationChanges(new ClientLocationList(clientLocations), new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                for (ClientLocation clientLocation : clientLocations) {
                    CacheGeofenceDatabaseService.deleteClientLocation(clientLocation);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    public interface ClientLocationChange {


        @POST(Constants.CLIENT_LOCATION)
        void createClientLocationChange(
                @Body ClientLocation clientLocationPost,
                Callback<Void> locationPostCallback
        );

        @PATCH(Constants.CLIENT_LOCATION)
        void createClientLocationChanges(
                @Body ClientLocationList clientLocationList,
                Callback<Void> locationPostCallback
        );

    }
}
