package is.yranac.canary.services.api.google;

import java.util.Locale;

import is.yranac.canary.Constants;
import is.yranac.canary.model.google.GooglePlaceDetailResponse;
import is.yranac.canary.model.google.GooglePlacesResponse;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Schroeder on 9/17/14.
 */
public class GooglePlacesAPIService {
    public static GooglePlacesResponse searchForPlaces(double lat, double lng, String type, String name) {

        String location = String.format(Locale.ENGLISH, "%f,%f", lat, lng);
        // Option to duplicate iOS results
//        String rankby = "prominence";
        String radius = "8047";

        // Alternative to get better results
        // remove the radius parameter from the API call if used
        String rankby = "distance";

        RestAdapter restAdapter = new RetroFitAdapterFactory().getGooglePlacesAdapter(false);

        GooglePlacesService googlePlacesService = restAdapter.create(GooglePlacesService.class);

        return googlePlacesService.searchForPlaces(location, type, name, rankby, Constants.GOOGLE_API_KEY);
    }

    public static GooglePlaceDetailResponse getPlaceDetail(String placeId) {

        RestAdapter restAdapter = new RetroFitAdapterFactory().getGooglePlacesAdapter(false);

        GooglePlacesService googlePlacesService = restAdapter.create(GooglePlacesService.class);
        return googlePlacesService.getPlaceDetail(placeId, Constants.GOOGLE_API_KEY);
    }

    public interface GooglePlacesService {
        @GET(Constants.GOOGLE_PLACES_NEARBY_URI)
        GooglePlacesResponse searchForPlaces(
                @Query("location") String location,
                @Query("type") String types,
                @Query("name") String name,
                @Query("rankby") String rankby,
                @Query("key") String key
        );

        @GET(Constants.GOOGLE_PLACES_DETAIL_URI)
        GooglePlaceDetailResponse getPlaceDetail(
                @Query("placeid") String placeId,
                @Query("key") String key
        );
    }
}
