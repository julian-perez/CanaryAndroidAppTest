package is.yranac.canary.services.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.text.TextUtils;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import is.yranac.canary.Constants;
import is.yranac.canary.messages.TryGeofenceAgain;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.services.api.LocationChangeAPIServices;
import is.yranac.canary.services.database.CustomerDatabaseService;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.MyLifecycleHandler;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import is.yranac.canary.util.geofence.GeofenceUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by michaelschroeder on 4/14/17.
 */

public class GeofenceTransitionsIntentService extends BroadcastReceiver {

    protected static final String TAG = "GeofenceTransitionsIS";
    public static final String ACTION_PROCESS_UPDATES =
            "com.google.android.gms.location.sample.backgroundlocationupdates.action" +
                    ".PROCESS_UPDATES";

    private static final String GEOFENCE_ENTER = "Geofence_Enter";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "onReceive");
        if (intent != null) {
            final String action = intent.getAction();
            if (!ACTION_PROCESS_UPDATES.equals(action)) {
                return;
            }
        }


        if (!PreferencesUtils.getGeofencingEnabled()) {
            return;
        }
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent == null || geofencingEvent.hasError()) {
            return;
        }

        final Location location = geofencingEvent.getTriggeringLocation();


        if (GeofenceUtils.isFromMockProvider(location)) {
            return;
        }
        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();


        if (!isValidGeofenceTransition(geofenceTransition))
            return;

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    geofenceTransition,
                    triggeringGeofences
            );

            // Send notification and log the transition details.
            GeofenceUtils.sendNotification(geofenceTransitionDetails, context);
        }

        final boolean arrival = geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER;

        if (!arrival) {
            SetUpGeofence.startActiveMonitoringService(context);
            return;
        }

        for (Geofence geofence : geofencingEvent.getTriggeringGeofences()) {
            int locationId = Integer.parseInt(geofence.getRequestId());
            final String locationUri = Utils.buildResourceUri(Constants.LOCATIONS_URI, locationId);

            Customer customer = CurrentCustomer.getCurrentCustomer();

            if (customer != null) {
                if (locationUri.equalsIgnoreCase(customer.currentLocation)) {
                    continue;
                }
            }

            GeofenceUtils.postClientLocation(context, location, GEOFENCE_ENTER, false);
            GoogleAnalyticsHelper.trackGeofenceEvent(locationId, arrival,
                    MyLifecycleHandler.applicationInBackground());

            LocationChangeAPIServices.changeLocation(locationUri, location.getLatitude(), location.getLongitude(),
                    arrival, new Callback<Void>() {
                        @Override
                        public void success(Void aVoid, Response response) {
                            CustomerDatabaseService.updateCustomerLocation(locationUri);
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            if (retrofitError.getCause() instanceof SocketTimeoutException) {
                                TinyMessageBus.postDelayed(new TryGeofenceAgain(locationUri, location.getLatitude(), location.getLongitude(),
                                        arrival, 1), TimeUnit.SECONDS.toMillis(10));
                            }
                        }
                    });

        }
    }

    private boolean isValidGeofenceTransition(int geofenceTransition) {
        return geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT;
    }

    /**
     * Gets transition details and returns them as a formatted string.
     *
     * @param geofenceTransition  The ID of the geofence transition.
     * @param triggeringGeofences The geofence(s) triggered.
     * @return The transition details formatted as String.
     */
    private String getGeofenceTransitionDetails(
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     *
     * @param transitionType A transition type constant defined in Geofence
     * @return A String indicating the type of transition
     */
    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "Entered geofence";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "Exited geofence";
            default:
                return "Geofence WTF";
        }
    }
}