package is.yranac.canary.util.geofence;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;

import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.Random;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.services.api.ClientLocationApiService;
import is.yranac.canary.util.ColorUtil;
import is.yranac.canary.util.DateUtil;
import is.yranac.canary.util.ImageUtils;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 7/28/14.
 */
public final class GeofenceUtils {


    public static final int SMALL = 150;
    public static final int MEDIUM = 300;
    public static final int LARGE = 450;

    public static double distance(double lat1, double lat2, double lng1, double lng2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = deg2rad(lat2 - lat1);
        Double lonDistance = deg2rad(lng2 - lng1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }


    public static void changeOffsetCenter(MapView view, GoogleMap googleMap,
                                          double latitude, double longitude, float zoomFactor) {
        Point mappoint = googleMap.getProjection().toScreenLocation(new LatLng(latitude, longitude));
        int quaterHeight = view.getHeight() / 4;
        mappoint.set(mappoint.x, mappoint.y + quaterHeight);
        googleMap.moveCamera(CameraUpdateFactory.
                newLatLngZoom(googleMap.getProjection().fromScreenLocation(mappoint), zoomFactor));
    }

    public static LatLng getOffsetCenter(MapView view, GoogleMap googleMap) {
        Point mappoint = googleMap.getProjection().toScreenLocation(googleMap.getCameraPosition().target);
        int quaterHeight = view.getHeight() / 4;
        mappoint.set(mappoint.x, mappoint.y - quaterHeight);

        return googleMap.getProjection().fromScreenLocation(mappoint);

    }


    public static void addMarker(Context context, LatLng latLng, GoogleMap googleMap) {


        Bitmap icon = ImageUtils.getBitmapFromVectorDrawable(context,
                R.drawable.ic_combined_shape);

        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(icon)));
    }

    public static void addGeofence(Context context, LatLng latLng,
                                   GoogleMap googleMap, int geofenceRadius) {


        final int colorGreen = ColorUtil.darkModerateCyanThirty(context);
        final int colorClear = ColorUtil.transparent(context);
        googleMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(geofenceRadius)
                .strokeColor(colorClear)
                .fillColor(colorGreen));

    }

    public static double distanceWithAccuracy(double closestDistance, float accuracy) {
        double newDistance = closestDistance - accuracy;
        return newDistance < 0 ? 0 : newDistance;
    }

    public static boolean isFromMockProvider(Location location) {
        return location.isFromMockProvider() && !Utils.isNotProd();
    }

    public static void sendNotification(String notificationDetails, Context context) {
        if (!PreferencesUtils.locationNotificationEnabled(context)) {
            return;
        }
        // Create an explicit content Intent that starts the main Activity.

        // Get a notification builder that's compatible with platform versions >= 4
        Notification.Builder builder;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context, Constants.CANARY_NOTIFICATION_CHANNEL);
        } else {
            builder = new Notification.Builder(context);
        }
        // Define the notification settings.
        builder.setSmallIcon(
                R.drawable.notification_logo)
                .setContentTitle(notificationDetails);
        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(new Random().nextInt(), builder.build());

    }

    public static void postClientLocation(Context context, Location location, String type, boolean gps) {
        if (!PreferencesUtils.locationServicesDebuggingEnabled(context))
            return;

        double lat = location.getLatitude();
        double lng = location.getLongitude();

        float accuracy = location.getAccuracy();

        if (accuracy < 0)
            accuracy = 0;

        float batteryPct = 0;
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.getApplicationContext().registerReceiver(null, ifilter);
        if (batteryStatus != null) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            batteryPct = level / (float) scale;

        }

        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        Date locationTime = new Date(location.getTime());

        String uuid = InstanceID.getInstance(context).getId();

        ClientLocationApiService.updateLocation(lat, lng, type, accuracy, batteryPct, uuid, DateUtil.getCurrentTime(), locationTime
                , wifiManager.isWifiEnabled(), gps);
    }
}
