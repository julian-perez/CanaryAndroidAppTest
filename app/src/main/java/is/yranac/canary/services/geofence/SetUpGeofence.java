package is.yranac.canary.services.geofence;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.util.LocationUtil;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.geofence.GeofenceUtils;

/**
 * Created by michaelschroeder on 9/1/17.
 */
public class SetUpGeofence {

    private static final String SERVICE_START = "service_start";

    private static final String SERVICE_STOP = "service_stop";


    private static final String MONITORING_START = "monitoring_start";

    private static final String MONITORING_STOP = "monitoring_stop";
    private static final String ACTION = "action";

    private static final long SLC_UPDATE_INTERVAL = TimeUnit.MINUTES.toMillis(15);
    private static final long SLC_FASTEST_UPDATE_INTERVAL = SLC_UPDATE_INTERVAL / 2;
    private static final long SLC_MAX_WAIT_TIME = SLC_UPDATE_INTERVAL * 3;
    private static final long SLC_SMALLEST_DISPLACEMENT = 750;

    private static final long UPDATE_INTERVAL = TimeUnit.MINUTES.toMillis(5);
    private static final long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;
    private static final long MAX_WAIT_TIME = UPDATE_INTERVAL * 3;
    private static final long SMALLEST_DISPLACEMENT = 150;

    public static void startService(Context context) {
        if (Utils.isDemo())
            return;

        Context applicationContext = context.getApplicationContext();
        setupLocationServices(SERVICE_START, applicationContext);
    }


    public static void stopService(Context context) {
        Context applicationContext = context.getApplicationContext();
        setupLocationServices(SERVICE_STOP, applicationContext);

    }

    public static void startActiveMonitoringService(Context context) {
        if (Utils.isDemo())
            return;

        Context applicationContext = context.getApplicationContext();
        setupLocationServices(MONITORING_START, applicationContext);
    }


    public static void stopActiveMonitoringService(Context context) {
        Context applicationContext = context.getApplicationContext();
        setupLocationServices(MONITORING_STOP, applicationContext);
    }


    private static void setupLocationServices(String action, Context context) {

        if (LocationUtil.doesNotHaveLocationPermission(context))
            return;

        Context appContext = context.getApplicationContext();

        FusedLocationProviderClient locationProviderClient = new FusedLocationProviderClient(appContext);


        if (action != null) {
            GeofenceUtils.sendNotification(action, appContext);
            switch (action) {
                case SERVICE_START:
                    setupGeofences(appContext);
                    setupLocationChecking(appContext);
                    break;
                case SERVICE_STOP:
                    GeofencingClient geofencingClient = new GeofencingClient(appContext);
                    geofencingClient.removeGeofences(getGeofencePendingIntent(appContext));
                    locationProviderClient.removeLocationUpdates(getSLCPendingIntent(appContext));
                    locationProviderClient.removeLocationUpdates(getMonitoringPendingIntent(appContext));
                    break;
                case MONITORING_START:
                    setupActiveLocationChecking(context);
                    break;
                case MONITORING_STOP:
                    locationProviderClient.removeLocationUpdates(getMonitoringPendingIntent(appContext));
                    break;
            }
        }

    }

    private static void setupLocationChecking(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        FusedLocationProviderClient locationProviderClient = new FusedLocationProviderClient(context.getApplicationContext());
        locationProviderClient.removeLocationUpdates(getSLCPendingIntent(context));

        locationProviderClient.requestLocationUpdates(
                createSLCLocationRequest(), getSLCPendingIntent(context));
    }

    private static void setupActiveLocationChecking(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        FusedLocationProviderClient locationProviderClient = new FusedLocationProviderClient(context.getApplicationContext());

        locationProviderClient.removeLocationUpdates(getMonitoringPendingIntent(context));
        locationProviderClient.requestLocationUpdates(createMonitoringLocationRequest(), getMonitoringPendingIntent(context));
    }

    private static void setupGeofences(Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        GeofencingClient geofencingClient = new GeofencingClient(context.getApplicationContext());

        geofencingClient.removeGeofences(getGeofencePendingIntent(context));

        List<Location> locations = LocationDatabaseService.getLocationList();

        if (locations.isEmpty())
            return;

        List<Geofence> geofenceList = new ArrayList<>();
        for (is.yranac.canary.model.location.Location location : locations) {

            if (location.geofenceRadius == 0 ||
                    !location.isValidLatLng()) {
                continue;
            }

            geofenceList.add(new Geofence.Builder()
                    .setRequestId(String.valueOf(location.id))
                    .setCircularRegion(
                            location.lat,
                            location.lng,
                            location.geofenceRadius
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                    .build());

        }
        geofencingClient.addGeofences(
                getGeofencingRequest(geofenceList),
                getGeofencePendingIntent(context)
        );
    }

    private static GeofencingRequest getGeofencingRequest(List<Geofence> geofenceList) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private static PendingIntent getGeofencePendingIntent(Context context) {
        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
        intent.setAction(GeofenceTransitionsIntentService.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private static LocationRequest createSLCLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();

        locationRequest.setInterval(SLC_UPDATE_INTERVAL)
                .setFastestInterval(SLC_FASTEST_UPDATE_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setSmallestDisplacement(SLC_SMALLEST_DISPLACEMENT)
                .setMaxWaitTime(SLC_MAX_WAIT_TIME);

        return locationRequest;
    }

    private static LocationRequest createMonitoringLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();

        locationRequest.setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setSmallestDisplacement(SMALLEST_DISPLACEMENT)
                .setMaxWaitTime(MAX_WAIT_TIME);

        return locationRequest;
    }


    private static PendingIntent getSLCPendingIntent(Context context) {
        Intent intent = new Intent(context, LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.SLC_UPDATES);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private static PendingIntent getMonitoringPendingIntent(Context context) {
        Intent intent = new Intent(context, LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTIVE_MONITORING_UPDATES);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


}
