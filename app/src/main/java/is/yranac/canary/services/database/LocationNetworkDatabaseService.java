package is.yranac.canary.services.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.wifi.WifiInfo;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.contentproviders.CanaryLocationNetworkContentProvider;
import is.yranac.canary.model.locationnetwork.LocationNetwork;

/**
 * Created by Schroeder on 10/20/15.
 */
public class LocationNetworkDatabaseService extends BaseDatabaseService {

    public static void insertNetworkForLocation(WifiInfo info, int locationId) {
        if (info.getNetworkId() == -1)
            return;
        ContentValues contentValues = contentValuesFromNetwork(info, locationId);
        contentResolver.insert(CanaryLocationNetworkContentProvider.CONTENT_URI, contentValues);

    }

    public static ContentValues contentValuesFromNetwork(WifiInfo info, int locationId) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(CanaryLocationNetworkContentProvider.COLUMN_LOCATION_ID, locationId);
        contentValues.put(CanaryLocationNetworkContentProvider.COLUMN_MAC_ADDRESS, info.getBSSID());
        contentValues.put(CanaryLocationNetworkContentProvider.COLUMN_NETWORK_NAME, info.getSSID());
        return contentValues;
    }

    public static List<LocationNetwork> locationNetworkForLocation(int locationId) {

        String where = CanaryLocationNetworkContentProvider.COLUMN_LOCATION_ID + " == ?";

        String[] args = {String.valueOf(locationId)};

        Cursor cursor = contentResolver.query(CanaryLocationNetworkContentProvider.CONTENT_URI, null, where, args, null);

        List<LocationNetwork> locationNetworks = new ArrayList<>();

        if (cursor == null)
            return locationNetworks;

        if (cursor.moveToFirst()) {

            do {

                locationNetworks.add(locationNetworkFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return locationNetworks;

    }

    public static LocationNetwork locationNetworkFromCursor(Cursor cursor) {
        LocationNetwork locationNetwork = new LocationNetwork();
        locationNetwork.macAddress = cursor.getString(cursor.getColumnIndex(CanaryLocationNetworkContentProvider.COLUMN_MAC_ADDRESS));
        locationNetwork.locationId = cursor.getInt(cursor.getColumnIndex(CanaryLocationNetworkContentProvider.COLUMN_LOCATION_ID));
        locationNetwork.networkName = cursor.getString(cursor.getColumnIndex(CanaryLocationNetworkContentProvider.COLUMN_NETWORK_NAME));
        return locationNetwork;
    }

    public static void deleteLocationNetworks() {
        contentResolver.delete(CanaryLocationNetworkContentProvider.CONTENT_URI, null, null);
    }
}
