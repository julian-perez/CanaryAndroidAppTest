package is.yranac.canary.contentproviders;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.Arrays;
import java.util.HashSet;

import is.yranac.canary.Constants;

/**
 * Created by Schroeder on 9/25/15.
 */
public class CanaryDeviceSettingsContentProvider extends CanaryBaseContentProvider {
    private static final String LOG_TAG = "CanaryDeviceContentProvider";

    public static final String TABLE_DEVICE_SETTINGS = "device_settings_table";

    public static final String DEVICE_SETTINGS_ID = "_id"; // Primary Key of local
    // SQLite

    public static final String COLUMN_AIR_QUALITY_THRESHOLD = "air_quality_threshold";
    public static final String COLUMN_CREATED = "created";
    public static final String COLUMN_DETECTION_THRESHOLD = "detection_threshold";
    public static final String COLUMN_ID = "device_id";
    public static final String COLUMN_HUMIDITY_THRESHOLD_MAX = "humidity_max";
    public static final String COLUMN_HUMIDITY_THRESHOLD_MIN = "humidity_min";
    public static final String COLUMN_LAST_MODIFIED = "last_modified";
    public static final String COLUMN_SEND_HUMIDITY_MAX = "send_humidity_max_notifications";
    public static final String COLUMN_SEND_HUMIDITY_MIN = "send_humidity_min_notifications";
    public static final String COLUMN_SEND_CONNECTIVITY_NOTIFICATIONS = "connectivity_notifications";
    public static final String COLUMN_SEND_HOMEHEALTH_NOTIFICATIONS = "home_health_notifications";
    public static final String COLUMN_SEND_AIR_QUALITY_NOTIFICATIONS = "send_air_quality_notifications";
    public static final String COLUMN_SEND_POWER_SOURCE_NOTIFICATIONS = "send_power_source_notifications";
    public static final String COLUMN_SEND_BATTERY_FULL_NOTIFICATIONS = "send_battery_full_notifications";
    public static final String COLUMN_BATTERY_SAVER_USE = "battery_saver_use";
    public static final String COLUMN_PIR_RECORDING_RANGE = "pir_recording_range";
    public static final String COLUMN_TEMP_THRESHOLD_MAX = "temp_max";
    public static final String COLUMN_TEMP_THRESHOLD_MIN = "temp_min";
    public static final String COLUMN_SEND_TEMP_MAX = "send_temp_max_notifications";
    public static final String COLUMN_SEND_TEMP_MIN = "send_temp_min_notifications";
    public static final String COLUMN_HOME_MODE = "home_mode";
    public static final String COLUMN_NIGHT_MODE = "night_mode";
    public static final String COLUMN_UPDATED = "updated";
    public static final String COLUMN_BACKPACK_DATA_USAGE_START_DAY = "backpack_data_usage_start_day";

    public static final String DATABASE_CREATE_DEVICE_SETTINGS = " CREATE TABLE " + TABLE_DEVICE_SETTINGS + "("
            + DEVICE_SETTINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_AIR_QUALITY_THRESHOLD + " REAL, "
            + COLUMN_CREATED + " LONG, "
            + COLUMN_HUMIDITY_THRESHOLD_MAX + " REAL, "
            + COLUMN_DETECTION_THRESHOLD + " REAL, "
            + COLUMN_ID + " BIGINT "
            + " REFERENCES " + CanaryDeviceContentProvider.TABLE_DEVICES
            + "(" + CanaryDeviceContentProvider.COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_HUMIDITY_THRESHOLD_MIN + " BOOLEAN, "
            + COLUMN_LAST_MODIFIED + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + COLUMN_SEND_CONNECTIVITY_NOTIFICATIONS + " BOOLEAN, "
            + COLUMN_SEND_HOMEHEALTH_NOTIFICATIONS + " BOOLEAN, "
            + COLUMN_SEND_HUMIDITY_MAX + " BOOLEAN, "
            + COLUMN_SEND_HUMIDITY_MIN + " BOOLEAN, "
            + COLUMN_SEND_TEMP_MAX + " BOOLEAN, "
            + COLUMN_SEND_TEMP_MIN + " BOOLEAN, "
            + COLUMN_SEND_AIR_QUALITY_NOTIFICATIONS + " BOOLEAN, "
            + COLUMN_SEND_POWER_SOURCE_NOTIFICATIONS + " BOOLEAN, "
            + COLUMN_SEND_BATTERY_FULL_NOTIFICATIONS + " BOOLEAN, "
            + COLUMN_BATTERY_SAVER_USE + " BOOLEAN, "
            + COLUMN_TEMP_THRESHOLD_MAX + " REAL, "
            + COLUMN_TEMP_THRESHOLD_MIN + " REAL, "
            + COLUMN_HOME_MODE + " INTEGER, "
            + COLUMN_NIGHT_MODE + " INTEGER, "
            + COLUMN_PIR_RECORDING_RANGE + " INTEGER, "
            + COLUMN_UPDATED + " LONG, "
            + COLUMN_BACKPACK_DATA_USAGE_START_DAY + " INTEGER, "
            + " UNIQUE (" + COLUMN_ID + ") ON CONFLICT REPLACE "
            + ");";


    public static final String UPDATE_TIME_TRIGGER =
            "CREATE TRIGGER update_device_settings_trigger" +
                    "  AFTER UPDATE ON " + TABLE_DEVICE_SETTINGS + " FOR EACH ROW" +
                    "  BEGIN " +
                    "UPDATE " + TABLE_DEVICE_SETTINGS +
                    "  SET " + COLUMN_LAST_MODIFIED + " = datetime()" +
                    "  WHERE " + COLUMN_ID + " = old." + COLUMN_ID + ";" +
                    "  END";
    private static final String BASE_PATH = "devicedata";
    public static final Uri CONTENT_URI = Uri.parse(
            "content://" + Constants.AUTHORITY_DEVICE_SETTINGS + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/devices";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/device";

    public static final String[] defaultProjectionDevices = new String[]{DEVICE_SETTINGS_ID, COLUMN_AIR_QUALITY_THRESHOLD, COLUMN_DETECTION_THRESHOLD,
            COLUMN_ID, COLUMN_HUMIDITY_THRESHOLD_MAX, COLUMN_HUMIDITY_THRESHOLD_MIN, COLUMN_SEND_HOMEHEALTH_NOTIFICATIONS,
            COLUMN_SEND_CONNECTIVITY_NOTIFICATIONS, COLUMN_TEMP_THRESHOLD_MAX, COLUMN_CREATED, COLUMN_UPDATED,
            COLUMN_TEMP_THRESHOLD_MIN, COLUMN_SEND_HUMIDITY_MAX, COLUMN_SEND_HUMIDITY_MIN, COLUMN_SEND_AIR_QUALITY_NOTIFICATIONS,
            COLUMN_SEND_POWER_SOURCE_NOTIFICATIONS, COLUMN_SEND_BATTERY_FULL_NOTIFICATIONS, COLUMN_BATTERY_SAVER_USE, COLUMN_PIR_RECORDING_RANGE,
            COLUMN_SEND_TEMP_MAX, COLUMN_SEND_TEMP_MIN, COLUMN_HOME_MODE, COLUMN_NIGHT_MODE, COLUMN_LAST_MODIFIED, COLUMN_BACKPACK_DATA_USAGE_START_DAY};

    private static final int DEVICE_SETTINGS = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_DEVICE_SETTINGS, BASE_PATH, DEVICE_SETTINGS);
    }

    @Override
    protected void checkColumns(String[] projection, int uriType) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns;
            switch (uriType) {
                case DEVICE_SETTINGS:
                    availableColumns = new HashSet<>(
                            Arrays.asList(defaultProjectionDevices));
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
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int uriType = sURIMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriType) {
            case DEVICE_SETTINGS:
                queryBuilder.setTables(TABLE_DEVICE_SETTINGS);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case DEVICE_SETTINGS:
                    projection = defaultProjectionDevices;
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
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        long id = 0;
        switch (uriType) {
            case DEVICE_SETTINGS:
                id = sqlDatabase.insert(TABLE_DEVICE_SETTINGS, "nullhack", values);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);

        int rowsDeleted = 0;
        switch (uriType) {
            case DEVICE_SETTINGS:
                rowsDeleted = sqlDatabase.delete(TABLE_DEVICE_SETTINGS, selection,
                        selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        int rowsUpdated = 0;
        switch (uriType) {
            case DEVICE_SETTINGS:
                rowsUpdated = sqlDatabase.update(TABLE_DEVICE_SETTINGS, values, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
