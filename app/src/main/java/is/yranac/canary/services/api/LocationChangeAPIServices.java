package is.yranac.canary.services.api;

import is.yranac.canary.Constants;
import is.yranac.canary.model.locationchange.LocationChange;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Schroeder on 8/8/14.
 */
public class LocationChangeAPIServices {

    public static void changeLocation(final String locationUri, final double lat, final double lng, final boolean arrival, Callback<Void> callback) {

        LocationChange locationChange = new LocationChange(locationUri, lat, lng, arrival);
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        LocationChangeService locationChangeService = restAdapter.create(LocationChangeService.class);
        locationChangeService.postLocationChange(locationChange, callback);

    }

    public interface LocationChangeService {
        @POST(Constants.LOCATION_CHANGES_URI)
        void postLocationChange(
                @Body LocationChange locationChange,
                Callback<Void> locationPostCallback

        );

    }
}
