package is.yranac.canary.contentproviders;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.HashSet;

import is.yranac.canary.Constants;

public class CanaryNightModeScheduleContentProvider extends CanaryBaseContentProvider {

    private static final String LOG_TAG = "CanaryNightModeScheduleContentProvider";

    public static final String TABLE_NIGHT_MODES = "night_mode_table";

    public static final String LOCATION_ID = "_id"; // Primary Key
    public static final String COLUMN_ID = "night_mode_id";
    public static final String COLUMN_LOCATION_ID = "location_id";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_END_TIME = "end_time";
    public static final String COLUMN_START_TIME = "start_time";

    public static final String DATABASE_CREATE_NIGHT_MODE_SCHEDULE = " CREATE TABLE " + TABLE_NIGHT_MODES + "("
            + LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID + " BIGINT UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_DAY + " INTEGER, "
            + COLUMN_LOCATION_ID + " BIGINT "
            + " REFERENCES " + CanaryLocationContentProvider.TABLE_LOCATIONS
            + "(" + CanaryLocationContentProvider.COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_END_TIME + " TEXT, "
            + COLUMN_START_TIME + " TEXT "
            + ");";


    private static final String[] defaultProjectionLocations = new String[]{LOCATION_ID, COLUMN_ID,
            COLUMN_LOCATION_ID, COLUMN_END_TIME, COLUMN_START_TIME, COLUMN_DAY};

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/nightmode";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/nightmode";

    private static final String BASE_PATH = "nightmodedata";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_NIGHT_MODE + "/" + BASE_PATH);
    private static final int NIGHTMODES = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_NIGHT_MODE, BASE_PATH, NIGHTMODES);
    }


    @Override
    public Cursor query(
            Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = sURIMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriType) {
            case NIGHTMODES:
                queryBuilder.setTables(TABLE_NIGHT_MODES);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case NIGHTMODES:
                    projection = defaultProjectionLocations;
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
            case NIGHTMODES:
                id = sqlDatabase.insert(TABLE_NIGHT_MODES, "nullhack", values);
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
            case NIGHTMODES:
                rowsDeleted = sqlDatabase.delete(TABLE_NIGHT_MODES, selection,
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
        int rowsUpdated = 0;
        switch (uriType) {
            case NIGHTMODES:
                rowsUpdated = sqlDatabase.update(TABLE_NIGHT_MODES, values,
                        selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    protected void checkColumns(String[] projection, int uriType) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = null;
            switch (uriType) {
                case NIGHTMODES:
                    availableColumns = new HashSet<String>(
                            Arrays.asList(defaultProjectionLocations));
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

}
