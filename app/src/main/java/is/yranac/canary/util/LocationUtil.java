package is.yranac.canary.util;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.view.View;

import android.support.v4.content.ContextCompat;
import is.yranac.canary.R;

public class LocationUtil {

    private static final String LOG_TAG = "LocationUtil";

    public static boolean canGetLocation(Context mContext) {

        LocationManager locationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);
        return locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public static AlertDialog showSettingsAlert(final Context mContext) {

        String gpsNotEnabled = mContext.getString(R.string.gps_not_enabled);
        String gpsNotEnabledDsc = mContext.getString(R.string.gps_not_enabled_dsc);
        String settings = mContext.getString(R.string.go_to_settings);
        String cancel = mContext.getString(R.string.cancel);

        return AlertUtils.showGenericAlert(mContext, gpsNotEnabled, gpsNotEnabledDsc, 0, settings, cancel, 0, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            }
        }, null);

    }


    public static boolean doesNotHaveLocationPermission(Context context) {
        boolean accessFineLocation = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean accessCoarseLocation = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;

        return accessFineLocation &&
                accessCoarseLocation;
    }
}

    
