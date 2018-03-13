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
 * Created by Schroeder on 10/21/14.
 */

public class CanaryReadingContentProvider extends CanaryBaseContentProvider {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = "CanaryReadingContentProvider";

    public static final int READING_HUMIDITY = 1;
    public static final int READING_TEMPERATURE = 2;
    public static final int READING_AIR_QUALITY = 3;
    public static final int READING_BATTERY = 7;
    public static final int READING_WIFI= 8;

    public static final String TABLE_READINGS = "reading_table";

    public static final String READING_ID = "_id";
    public static final String COLUMN_DEVICE_UUID = "device";
    public static final String COLUMN_SENSOR_ID = "sensor_id";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_CREATED = "created";
    public static final String COLUMN_STATUS = "status";

    public static final String DATABASE_CREATE_READINGS = "CREATE TABLE " + TABLE_READINGS + "("
            + READING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DEVICE_UUID + " TEXT NOT NULL  "
            + " REFERENCES " + CanaryDeviceContentProvider.TABLE_DEVICES
            + "(" + CanaryDeviceContentProvider.COLUMN_UUID + ") ON DELETE CASCADE, "
            + COLUMN_SENSOR_ID + " INTEGER, "
            + COLUMN_VALUE + " REAL, "
            + COLUMN_CREATED + " LONG, "
            + COLUMN_STATUS + " TEXT, "
            + " UNIQUE(" + COLUMN_DEVICE_UUID + "," + COLUMN_SENSOR_ID
            + "," + COLUMN_CREATED + ") ON CONFLICT REPLACE "
            + ");";

    private static final String BASE_PATH = "readingdata";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_READING + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/readings";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/reading";

    private static final String[] defaultProjectionAvatars = new String[]{READING_ID, COLUMN_CREATED,
            COLUMN_DEVICE_UUID, COLUMN_SENSOR_ID, COLUMN_VALUE, COLUMN_STATUS};

    private static final int READINGS = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_READING, BASE_PATH, READINGS);
    }


    @Override
    protected void checkColumns(String[] projection, int uriType) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = null;
            switch (uriType) {
                case READINGS:
                    availableColumns = new HashSet<String>(
                            Arrays.asList(defaultProjectionAvatars));
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
            case READINGS:
                queryBuilder.setTables(TABLE_READINGS);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case READINGS:
                    projection = defaultProjectionAvatars;
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
        long id;
        switch (uriType) {
            case READINGS:
                id = sqlDatabase.insert(TABLE_READINGS, "nullhack", values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        int nrInserted = 0;
        String TABLE;
        int uriType = sURIMatcher.match(uri);

        switch (uriType) {
            case READINGS:
                TABLE = TABLE_READINGS;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        //Begin inner transaction
        sqlDatabase.beginTransaction();

        for (ContentValues cv : values) {
            sqlDatabase.insert(TABLE, null, cv);
            nrInserted++;
        }

        sqlDatabase.setTransactionSuccessful();
        getContext().getContentResolver().notifyChange(uri, null);

        sqlDatabase.endTransaction();

        return nrInserted;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);

        int rowsDeleted = 0;
        switch (uriType) {
            case READINGS:
                rowsDeleted = sqlDatabase.delete(TABLE_READINGS, selection,
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
            case READINGS:
                rowsUpdated = sqlDatabase.update(TABLE_READINGS, values, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    public static boolean isValidReadingType(int readingType) {
        return readingType == READING_TEMPERATURE
                || readingType == READING_HUMIDITY
                || readingType == READING_AIR_QUALITY;
    }
}
