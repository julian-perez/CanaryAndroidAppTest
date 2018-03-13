package is.yranac.canary.services.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.contentproviders.geofence.CacheGeofenceContentProvider;
import is.yranac.canary.model.locationchange.ClientLocation;

/**
 * Created by Schroeder on 8/3/15.
 */
public class CacheGeofenceDatabaseService extends BaseDatabaseService{

    public static void insertClientLocation(ClientLocation clientLocation){

        ContentValues contentValues = createContentValuesFromClientLocation(clientLocation);

        if (clientLocation.id != 0) {
            String where = CacheGeofenceContentProvider.CACHE_GEOFENCE_ID  + " == ?";
            String[] whereArgs = {String.valueOf(clientLocation.id)};
            contentResolver.update(CacheGeofenceContentProvider.CONTENT_URI, contentValues, where, whereArgs);
        } else{
            contentResolver.insert(CacheGeofenceContentProvider.CONTENT_URI, contentValues);
        }
    }

    public static ContentValues createContentValuesFromClientLocation(ClientLocation clientLocation){

        ContentValues contentValues = new ContentValues();
        contentValues.put(CacheGeofenceContentProvider.COLUMN_ACCURACY, clientLocation.accuracy);
        contentValues.put(CacheGeofenceContentProvider.COLUMN_BATTERY, clientLocation.batteryPct);
        contentValues.put(CacheGeofenceContentProvider.COLUMN_DATE, clientLocation.timeReceivedClient);
        contentValues.put(CacheGeofenceContentProvider.COLUMN_LAT, clientLocation.lat);
        contentValues.put(CacheGeofenceContentProvider.COLUMN_LNG, clientLocation.lng);
        contentValues.put(CacheGeofenceContentProvider.COLUMN_TRANSITION_TYPE, clientLocation.type);
        contentValues.put(CacheGeofenceContentProvider.COLUMN_UUID, clientLocation.uuid);
        contentValues.put(CacheGeofenceContentProvider.COLUMN_LOCATION_DATE, clientLocation.locationDate);
        contentValues.put(CacheGeofenceContentProvider.COLUMN_GPS, clientLocation.gpsOn);
        contentValues.put(CacheGeofenceContentProvider.COLUMN_WIFI, clientLocation.wifiOn);

        return contentValues;
    }


    public static List<ClientLocation> getAllCacheClientLocations(int offset, int size){

        List<ClientLocation> clientLocations = new ArrayList<>();
        Cursor cursor = contentResolver.query(CacheGeofenceContentProvider.CONTENT_URI, null, null, null, null);

        if (cursor.moveToPosition(offset)){
            do {
                 clientLocations.add(getClientLocationFromCursor(cursor));
            }while (cursor.moveToNext() && clientLocations.size() < size);
        }

        cursor.close();

        return clientLocations;
    }

    // *****************************************************
    public static ClientLocation getClientLocationFromCursor(Cursor cursor) {
        ClientLocation clientLocation = new ClientLocation();

        clientLocation.id = cursor.getInt(cursor.getColumnIndex(CacheGeofenceContentProvider.CACHE_GEOFENCE_ID));
        clientLocation.accuracy = cursor.getFloat(cursor.getColumnIndex(CacheGeofenceContentProvider.COLUMN_ACCURACY));
        clientLocation.batteryPct = cursor.getFloat(cursor.getColumnIndex(CacheGeofenceContentProvider.COLUMN_BATTERY));
        clientLocation.timeReceivedClient = cursor.getString(cursor.getColumnIndex(CacheGeofenceContentProvider.COLUMN_DATE));
        clientLocation.locationDate = cursor.getString(cursor.getColumnIndex(CacheGeofenceContentProvider.COLUMN_LOCATION_DATE));
        clientLocation.lat = cursor.getDouble(cursor.getColumnIndex(CacheGeofenceContentProvider.COLUMN_LAT));
        clientLocation.lng = cursor.getDouble(cursor.getColumnIndex(CacheGeofenceContentProvider.COLUMN_LNG));
        clientLocation.type = cursor.getString(cursor.getColumnIndex(CacheGeofenceContentProvider.COLUMN_TRANSITION_TYPE));
        clientLocation.uuid = cursor.getString(cursor.getColumnIndex(CacheGeofenceContentProvider.COLUMN_UUID));
        clientLocation.gpsOn = cursor.getInt(cursor.getColumnIndex(CacheGeofenceContentProvider.COLUMN_GPS)) > 0;
        clientLocation.wifiOn = cursor.getInt(cursor.getColumnIndex(CacheGeofenceContentProvider.COLUMN_WIFI)) > 0;

        return clientLocation;
    }

    public static void deleteClientLocation(ClientLocation clientLocation) {
        if (clientLocation.id != 0){
            String where = CacheGeofenceContentProvider.CACHE_GEOFENCE_ID + " == ?";
            String[] args = {String.valueOf(clientLocation.id)};
            contentResolver.delete(CacheGeofenceContentProvider.CONTENT_URI, where, args);
        }
    }
}
