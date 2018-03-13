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

public class CanaryEntryContentProvider extends CanaryBaseContentProvider {

    public static final String TABLE_ENTRIES = "entry_table";
    public static final String ENTRY_ID = "_id"; // Primary Key of local
    // SQLite
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_END_TIME = "end_time";
    public static final String COLUMN_ENTRY_DESCRIPTION = "entry_description";
    public static final String COLUMN_ENTRY_TYPE = "entry_type";
    public static final String COLUMN_ID = "entry_id";
    public static final String COLUMN_LAST_MODIFIED = "last_modified";
    public static final String COLUMN_LABEL_STRING = "label_string";
    public static final String COLUMN_LOCATION_ID = "location_id";
    public static final String COLUMN_LOCATION_MODE = "location_mode";
    public static final String COLUMN_LOCATION_IS_PRIVATE = "is_private";
    public static final String COLUMN_STARRED = "starred";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_THUMBNAIL_COUNT = "thumbnail_count";
    public static final String COLUMN_DEVICE_MODE = "device_mode";
    public static final String COLUMN_ENTRY_CALL_TYPE = "call_type";
    public static final String COLUMN_EXPORTED = "exported";
    public static final String COLUMN_DEVICES = "devices";

    public static final String DATABASE_CREATE_ENTRIES = " CREATE TABLE " + TABLE_ENTRIES + "("
            + ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DURATION + " TEXT NOT NULL, "
            + COLUMN_END_TIME + " LONG, "
            + COLUMN_ENTRY_DESCRIPTION + " TEXT NOT NULL, "
            + COLUMN_ENTRY_TYPE + " TEXT NOT NULL, "
            + COLUMN_ID + " BIGINT UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_LABEL_STRING + " TEXT, "
            + COLUMN_ENTRY_CALL_TYPE + " INTEGER, "
            + COLUMN_LAST_MODIFIED + " LONG, "
            + COLUMN_LOCATION_ID + " BIGINT "
            + " REFERENCES " + CanaryLocationContentProvider.TABLE_LOCATIONS
            + "(" + CanaryLocationContentProvider.COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_LOCATION_MODE + " TEXT, "
            + COLUMN_LOCATION_IS_PRIVATE + " BOOLEAN, "
            + COLUMN_STARRED + " BOOLEAN, "
            + COLUMN_START_TIME + " LONG, "
            + COLUMN_THUMBNAIL_COUNT + " INTEGER, "
            + COLUMN_DEVICE_MODE + " TEXT ,"
            + COLUMN_DEVICES + " TEXT ,"
            + COLUMN_EXPORTED + " BOOLEAN "
            + ");";

    private static final String[] defaultProjectionEntries = new String[]{ENTRY_ID, COLUMN_DURATION,
            COLUMN_LABEL_STRING, COLUMN_END_TIME, COLUMN_ENTRY_DESCRIPTION, COLUMN_ENTRY_TYPE, COLUMN_ID,
            COLUMN_LAST_MODIFIED, COLUMN_LOCATION_ID, COLUMN_ENTRY_CALL_TYPE, COLUMN_STARRED, COLUMN_START_TIME,
            COLUMN_THUMBNAIL_COUNT, COLUMN_DEVICE_MODE,COLUMN_LOCATION_IS_PRIVATE, COLUMN_EXPORTED, COLUMN_LOCATION_MODE,
            COLUMN_DEVICES};

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/entries";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/entry";

    private static final String BASE_PATH = "entrydata";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_ENTRY + "/" + BASE_PATH);
    private static final int ENTRIES = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_ENTRY, BASE_PATH, ENTRIES);
    }

    @Override
    protected void checkColumns(String[] projection, int uriType) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = null;
            switch (uriType) {
                case ENTRIES:
                    availableColumns = new HashSet<String>(
                            Arrays.asList(defaultProjectionEntries));
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
            case ENTRIES:
                queryBuilder.setTables(TABLE_ENTRIES);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case ENTRIES:
                    projection = defaultProjectionEntries;
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
            case ENTRIES:
                id = sqlDatabase.insert(TABLE_ENTRIES, "nullhack", values);
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
            case ENTRIES:
                rowsDeleted = sqlDatabase.delete(TABLE_ENTRIES, selection,
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
            case ENTRIES:
                rowsUpdated = sqlDatabase.update(TABLE_ENTRIES, values, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

}
