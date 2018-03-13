package is.yranac.canary.services.intent;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.location.NightModeResponse;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.api.InvitationsAPIService;
import is.yranac.canary.services.api.LocationAPIService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.util.keystore.KeyStoreHelper;
import retrofit.RetrofitError;

/**
 * Created by Schroeder on 8/8/14.
 */

public class APIIntentServiceSettingsInfo extends IntentService {
    private static final String TAG = "APIIntentServiceSettingsInfo";

    private static final String EXTRA_LOCATION = "is.yranac.canary.services.extra.LOCATION";

    public APIIntentServiceSettingsInfo() {
        super(TAG);
    }

    /**
     * Starts the APIEntryJobService to completely replace the invitations for a location
     */
    public static void updateSettingsForLocation(@NonNull Context context, int locationId) {
        Intent intent = new Intent(context, APIIntentServiceSettingsInfo.class);
        intent.putExtra(EXTRA_LOCATION, locationId);
        context.getApplicationContext().startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int locationId = intent.getIntExtra(EXTRA_LOCATION, 0);


        if (!KeyStoreHelper.hasGoodOauthToken()) {
            return;
        }


        try {
            NightModeResponse nightModeResponse = LocationAPIService.getNightModeForLocation();
            LocationDatabaseService.insertNightModes(nightModeResponse.nightmodes);
        } catch (RetrofitError ignored) {
        }
        if (locationId > 0) {

            try {
                InvitationsAPIService.getInvitationsForLocation(locationId);
            } catch (RetrofitError ignored) {
            }

            for (Device device : DeviceDatabaseService.getActivatedDevicesAtLocation(locationId)) {
                try {
                    DeviceAPIService.getDeviceSettings(device.id);
                } catch (RetrofitError ignored) {
                }
            }
        }

    }
}

