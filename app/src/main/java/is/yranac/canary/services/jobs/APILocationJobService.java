package is.yranac.canary.services.jobs;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.JobParameters;

import java.util.Map;

import is.yranac.canary.messages.LocationTableUpdated;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.emergencycontacts.EmergencyContact;
import is.yranac.canary.model.insurance.InsuranceProvider;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.location.LocationResponse;
import is.yranac.canary.model.masking.DeviceMasks;
import is.yranac.canary.model.mode.Mode;
import is.yranac.canary.model.mode.ModeCache;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.api.EmergencyContctsAPIService;
import is.yranac.canary.services.api.InsuranceAPIService;
import is.yranac.canary.services.api.LocationAPIService;
import is.yranac.canary.services.api.ModeAPIService;
import is.yranac.canary.services.api.SubscriptionAPIService;
import is.yranac.canary.services.database.EmergencyContactDatabaseService;
import is.yranac.canary.services.database.InsuranceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.database.ModeDatabaseService;
import is.yranac.canary.services.database.SubscriptionPlanDatabaseService;
import is.yranac.canary.util.MyLifecycleHandler;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.keystore.KeyStoreHelper;

/**
 * Created by Schroeder on 8/8/14.
 */
public class APILocationJobService extends BaseJobService {
    private static final String TAG = "APILocationJobService";

    private GetLocations getLocationsAsync;

    @Override
    public boolean onStartJob(final JobParameters params) {
        getLocationsAsync = new GetLocations(getBaseContext()) {
            @Override

            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                jobFinished(params, !MyLifecycleHandler.applicationInBackground() && KeyStoreHelper.hasGoodOauthToken());
            }
        };
        getLocationsAsync.execute();
        return true;
    }

    @Override
    public boolean onStopJob(final JobParameters params) {
        if (getLocationsAsync != null) {
            getLocationsAsync.cancel(true);
        }
        return true;
    }

    public static void fetchLocations(@NonNull Context context) {
        GetLocations getLocation = new GetLocations(context);
        getLocation.execute();
    }


    private static class GetLocations extends AsyncTask<Void, Void, Void> {


        private final Context context;

        public GetLocations(Context context) {

            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {


            if (!KeyStoreHelper.hasGoodOauthToken())
                return null;


            LocationResponse locationResponse = null;
            try {
                locationResponse = LocationAPIService.getLocationRecords();
                LocationDatabaseService.rebuildLocations(context, locationResponse.locations);

            } catch (Exception ignored) {
            }

            if (ModeDatabaseService.getAllModes().size() < Mode.TOTAL_MODES) {
                try {
                    ModeAPIService.getModes();
                } catch (Exception ignored) {

                }
            }

            ModeCache.geModeCache().updateModeCache();

            if (InsuranceDatabaseService.getAllProviders().size() < InsuranceProvider.TOTAL_PROVIDERS) {
                try {
                    InsuranceAPIService.getInsuranceProviders();
                } catch (Exception ignored) {

                }
            }


            //updating masks
            if (locationResponse != null && locationResponse.locations != null) {
                for (Location location : locationResponse.locations) {

                    if (location.devices == null)
                        continue;

                    for (Device device : location.devices) {
                        if (!device.deviceActivated)
                            continue;

                        try {
                            DeviceMasks masks = DeviceAPIService.getDeviceMasks(device.id);
                            PreferencesUtils.setDeviceMasks(masks, device.id);
                        } catch (Exception ignored) {

                        }
                    }

                    Subscription subscription = SubscriptionPlanDatabaseService.getNullableServicePlanForLocation(location.id);

                    if (subscription == null) {
                        try {
                            SubscriptionAPIService.getSubscription(location.id);
                        } catch (Exception ignored) {

                        }
                    }

                    final Map<EmergencyContact.ContactType, EmergencyContact> map = EmergencyContactDatabaseService.getEmergencyContacts(location.id);

                    if (map.isEmpty()) {
                        try {
                            EmergencyContctsAPIService.getEmergencyContactsForLocation(location.id);
                        } catch (Exception ignored) {

                        }
                    }

                }
            }

            TinyMessageBus.post(new LocationTableUpdated());

            return null;
        }
    }

    public static void rescheduleIntent(Context context) {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        GetLocations getLocation = new GetLocations(context);
        getLocation.execute();
        scheduleJob(APILocationJobService.class, TAG, dispatcher, null);
    }

}
