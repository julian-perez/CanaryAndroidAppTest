package is.yranac.canary.services.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.text.TextUtils;

import com.google.android.gms.location.LocationResult;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.services.api.LocationChangeAPIServices;
import is.yranac.canary.services.database.CustomerDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.database.LocationNetworkDatabaseService;
import is.yranac.canary.services.jobs.APILocationJobService;
import is.yranac.canary.util.DateUtil;
import is.yranac.canary.util.MyLifecycleHandler;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import is.yranac.canary.util.geofence.GeofenceUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Receiver for handling location updates.
 * <p>
 * For apps targeting API level O
 * {@link android.app.PendingIntent#getBroadcast(Context, int, Intent, int)} should be used when
 * requesting location updates. Due to limits on background services,
 * {@link android.app.PendingIntent#getService(Context, int, Intent, int)} should not be used.
 * <p>
 * Note: Apps running on "O" devices (regardless of targetSdkVersion) may receive updates
 * less frequently than the interval specified in the
 * {@link com.google.android.gms.location.LocationRequest} when the app is no longer in the
 * foreground.
 */
public class LocationUpdatesBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "LUBroadcastReceiver";

    static final String SLC_UPDATES = "SLC_UPDATES";
    static final String ACTIVE_MONITORING_UPDATES = "MONITORING_UPDATES";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!PreferencesUtils.getGeofencingEnabled())
            return;

        if (intent != null) {
            final String action = intent.getAction();
            if (SLC_UPDATES.equals(action) || ACTIVE_MONITORING_UPDATES.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {


                    Location location = result.getLastLocation();

                    if (location == null)
                        return;


                    GeofenceUtils.sendNotification(String.format(Locale.ENGLISH, "Location Update %1$s %2$s", action, DateUtil.utcDateToDisplayString(new Date(location.getTime()))), context);


                    List<is.yranac.canary.model.location.Location> locationList = LocationDatabaseService.getLocationList();
                    checkLocations(context, location, locationList, action);
                }
            }
        }
    }

    private void checkLocations(Context context, Location location,
                                List<is.yranac.canary.model.location.Location> locationList, String action) {

        double closestDistanceWithAccuracy = Double.MAX_VALUE;

        double closestGeofenceSize = GeofenceUtils.SMALL;

        try {


            String type = SLC_UPDATES.equals(action) ? "SLC" : "ACTIVE_MONITORING";
            Customer customer = CurrentCustomer.getCurrentCustomer();

            if (GeofenceUtils.isFromMockProvider(location)) {
                return;
            }

            if (locationList.isEmpty()) {
                APILocationJobService.fetchLocations(context.getApplicationContext());
                return;
            }


            if (location.getTime() - new Date().getTime() > TimeUnit.MINUTES.toMillis(5)) {
                return;
            }


            final double lat = location.getLatitude();
            final double lng = location.getLongitude();

            if (lat == 0.0 && lng == 0.0)
                return;


            for (final is.yranac.canary.model.location.Location location1 : locationList) {

                final double distance = GeofenceUtils.distance(location1.lat, lat, location1.lng, lng);
                final double distanceWithAccuracy = GeofenceUtils.distanceWithAccuracy(distance, location.getAccuracy());

                if (distanceWithAccuracy < closestDistanceWithAccuracy) {
                    closestDistanceWithAccuracy = distanceWithAccuracy;
                    closestGeofenceSize = location1.geofenceRadius;

                }

                if (distanceWithAccuracy < location1.geofenceRadius) {

                    closestGeofenceSize = location1.geofenceRadius;

                    if (customer != null && !customer.currentLocation.equalsIgnoreCase(location1.resourceUri)) {

                        if (location.getAccuracy() > location1.geofenceRadius) {
                            GeofenceUtils.postClientLocation(context, location, type + "_Bad", false);
                            return;
                        }
                        GeofenceUtils.postClientLocation(context, location, type + "_Enter", false);
                        GoogleAnalyticsHelper.trackGeofenceEvent(location1.id, true, MyLifecycleHandler.applicationInBackground());
                        LocationChangeAPIServices.changeLocation(location1.resourceUri, lat, lng, true, new Callback<Void>() {
                            @Override
                            public void success(Void aVoid, Response response) {
                                CustomerDatabaseService.updateCustomerLocation(location1.resourceUri);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                            }
                        });

                    } else {
                        GeofenceUtils.postClientLocation(context, location, type + "_Refresh", false);
                    }
                    return;
                }
            }

            if (customer != null && !TextUtils.isEmpty(customer.currentLocation)) {
                GeofenceUtils.postClientLocation(context, location, type + "_Exit", false);
                GoogleAnalyticsHelper.trackGeofenceEvent(Utils.getIntFromResourceUri(customer.currentLocation), false, MyLifecycleHandler.applicationInBackground());
                LocationChangeAPIServices.changeLocation(customer.currentLocation, lat, lng, false, new Callback<Void>() {
                    @Override
                    public void success(Void aVoid, Response response) {
                        CustomerDatabaseService.updateCustomerLocation(null);

                        LocationNetworkDatabaseService.deleteLocationNetworks();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                    }
                });

                return;

            }

            GeofenceUtils.postClientLocation(context, location, type + "_Refresh", false);
        } finally {
            if (location.getTime() - new Date().getTime() <= TimeUnit.MINUTES.toMillis(5)) {
                checkActiveMonitoring(context, action, closestGeofenceSize, closestDistanceWithAccuracy);
            }
        }

    }

    private void checkActiveMonitoring(Context context, String action, double maxGeofence, double distance) {
        double activeMonitoringRadius = maxGeofence * 3;
        if (distance < activeMonitoringRadius && action.equalsIgnoreCase(SLC_UPDATES)) {
            SetUpGeofence.startActiveMonitoringService(context);
        } else if (distance > activeMonitoringRadius && action.equalsIgnoreCase(ACTIVE_MONITORING_UPDATES)) {
            SetUpGeofence.stopActiveMonitoringService(context);
        }
    }
}