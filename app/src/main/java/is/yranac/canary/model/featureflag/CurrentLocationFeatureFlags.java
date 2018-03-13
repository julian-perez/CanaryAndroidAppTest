package is.yranac.canary.model.featureflag;

import java.util.List;
import java.util.Map;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.messages.UpdateFeatureConfig;
import is.yranac.canary.services.database.FeatureFlagDatabaseService;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.UserUtils;

/**
 * Created by Schroeder on 3/2/16.
 */
public class CurrentLocationFeatureFlags {


    private static final String LOG_TAG = "CurrentLocationFeatureFlags";
    private static CurrentLocationFeatureFlags currentLocationFeatureFlags = null;


    public static CurrentLocationFeatureFlags getInstance() {
        if (currentLocationFeatureFlags == null) {
            currentLocationFeatureFlags = new CurrentLocationFeatureFlags();
            TinyMessageBus.register(currentLocationFeatureFlags);
        }

        return currentLocationFeatureFlags;
    }

    @Subscribe(mode = Subscribe.Mode.Background)
    public void updateFeatureConfig(UpdateFeatureConfig featureConfig) {

        int locationID;
        if (featureConfig == null || featureConfig.locationID == null)
            locationID = UserUtils.getLastViewedLocationId();
        else
            locationID = featureConfig.locationID;

        loadDefaults();
        List<FeatureFlag> featureFlags = FeatureFlagDatabaseService.getFeatureFlagsForLocation(locationID);

        if (featureFlags.size() == 0) {
            loadDefaults();
            return;
        }
    }

    private void loadDefaults() {
    }
}
