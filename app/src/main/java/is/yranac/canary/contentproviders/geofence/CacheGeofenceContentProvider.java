package is.yranac.canary.contentproviders.geofence;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.HashSet;

import is.yranac.canary.Constants;
import is.yranac.canary.contentproviders.CanaryBaseContentProvider;

/**
 * Created by Schroeder on 8/3/15.
 */
public class CacheGeofenceContentProvider  extends CanaryBaseContentProvider {


    private static final String LOG_TAG = "CacheGeofenceContentProvider";

    public static final String TABLE_CACHE_GEOFENCE = "cache_geofence_table";

    public static final String CACHE_GEOFENCE_ID        = "_id";
    public static final String COLUMN_ACCURACY          = "accuracy";
    public static final String COLUMN_BATTERY           = "battery";
    public static final String COLUMN_DATE              = "date";
    public static final String COLUMN_LOCATION_DATE     = "location_date";
    public static final String COLUMN_LAT               = "lat";
    public static final String COLUMN_LNG               = "lng";
    public static final String COLUMN_TRANSITION_TYPE   = "transition_type";
    public static final String COLUMN_UUID              = "uuid";
    public static final String COLUMN_GPS               = "gps";
    public static final String COLUMN_WIFI              = "wifi";

    public static final String DATABASE_CREATE_CAHCE_GEOFENCE = " CREATE TABLE " + TABLE_CACHE_GEOFENCE + "(" +
            CACHE_GEOFENCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ACCURACY + " DOUBLE, " +
            COLUMN_BATTERY + " DOUBLE, " +
            COLUMN_DATE + " TEXT UNIQUE ON CONFLICT REPLACE, " +
            COLUMN_LOCATION_DATE + " TEXT, " +
            COLUMN_LAT + " DOUBLE, " +
            COLUMN_LNG + " DOUBLE, " +
            COLUMN_TRANSITION_TYPE + " TEXT NOT NULL, " +
            COLUMN_GPS + " BOOLEAN, " +
            COLUMN_WIFI + " BOOLEAN, " +
            COLUMN_UUID + " TEXT NOT NULL" + ");";

    private static final String BASE_PATH   = "cahcegeofencedata";
    public static final  Uri    CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_CACHE_GEOFENCE + "/" + BASE_PATH);

    public static final String CONTENT_TYPE      = ContentResolver.CURSOR_DIR_BASE_TYPE + "/cachegeofences";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/cachegeofence";

    private static final String[] defaultProjectionCacheGeofence = new String[]{CACHE_GEOFENCE_ID, COLUMN_ACCURACY, COLUMN_BATTERY,
            COLUMN_DATE, COLUMN_LAT, COLUMN_LNG, COLUMN_TRANSITION_TYPE, COLUMN_UUID, COLUMN_LOCATION_DATE, COLUMN_GPS, COLUMN_WIFI};

    private static final int CACHE_GEOFENCE = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_CACHE_GEOFENCE, BASE_PATH, CACHE_GEOFENCE);
    }


    @Override
    protected void checkColumns(String[] projection, int uriType) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns;
            switch (uriType) {
                case CACHE_GEOFENCE:
                    availableColumns = new HashSet<>(
                            Arrays.asList(defaultProjectionCacheGeofence));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uriType);

            }

            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int uriType = sURIMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriType) {
            case CACHE_GEOFENCE:
                queryBuilder.setTables(TABLE_CACHE_GEOFENCE);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case CACHE_GEOFENCE:
                    projection = defaultProjectionCacheGeofence;
                    break;

                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);

            }
        } else {
            // check if the caller has requested a column which does not exists
            checkColumns(projection, uriType);
        }

        Cursor cursor = queryBuilder.query(sqlDatabase, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        long id;
        switch (uriType) {
            case CACHE_GEOFENCE:
                id = sqlDatabase.insert(TABLE_CACHE_GEOFENCE, "nullhack", values);
                if (id <= 0) {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);

        int rowsDeleted;
        switch (uriType) {
            case CACHE_GEOFENCE:
                rowsDeleted = sqlDatabase.delete(TABLE_CACHE_GEOFENCE, selection,
                        selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        int rowsUpdated;
        switch (uriType) {
            case CACHE_GEOFENCE:
                rowsUpdated = sqlDatabase.update(TABLE_CACHE_GEOFENCE, values,
                        selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
