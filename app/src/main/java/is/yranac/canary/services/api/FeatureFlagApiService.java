package is.yranac.canary.services.api;

import is.yranac.canary.Constants;
import is.yranac.canary.model.featureflag.FeatureFlagResponse;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;

import is.yranac.canary.services.database.FeatureFlagDatabaseService;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Schroeder on 3/1/16.
 */
public class FeatureFlagApiService {

    // ******************************************
    // *** Entry Records
    // ******************************************
    // Synchronous call for Entry Records
    public static FeatureFlagResponse getFeatureFlags(int locationId) throws RetrofitError {

        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        FeatureFlagService featureFlagService = restAdapter.create(FeatureFlagService.class);

        FeatureFlagResponse featureFlagResponse = featureFlagService.getFeatureFlags(locationId, 0);

        FeatureFlagDatabaseService.insertFeatureFlags(featureFlagResponse.objects, locationId);
        return featureFlagResponse;
    }

    public interface FeatureFlagService {
        @GET(Constants.FEATUREFLAG_URI)
        FeatureFlagResponse getFeatureFlags(
                @Query("location") int locationId,
                @Query("limit") int limit);
    }
}
