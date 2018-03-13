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

public class CanaryCustomerContentProvider extends CanaryBaseContentProvider {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = "CanaryCustomerContentProvider";

    public static final String TABLE_CUSTOMERS = "customer_table";

    public static final String CUSTOMER_ID = "_id"; // Primary Key of local SQLite

    public static final String COLUMN_CREATED = "created";
    public static final String COLUMN_CURRENT_LOCATION = "curent_location";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_CUSTOMER_ID = "customer_id";
    public static final String COLUMN_LAST_LOCATION_CHANGE = "last_location_change";
    public static final String COLUMN_LANGUAGE_PREFERENCE = "language_preference";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_NOTIFICATION_SOUND = "notifications_sound";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_RESOURCE_URI = "resource_uri";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_TEMPERATE_SETTING = "celsius";
    public static final String COLUMN_DIAL_CODE = "dial_code";
    public static final String COLUMN_PENDING_DELETE = "pending_delete";
    public static final String COLUMN_HAS_SEEN_DATA_SHARE_PROMPT = "has_seen_data_share_prompt";

    public static final String DATABASE_CREATE_CUSTOMERS = " CREATE TABLE " + TABLE_CUSTOMERS + "("
            + CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CREATED + " TEXT , "
            + COLUMN_CURRENT_LOCATION + " TEXT NOT NULL, "
            + COLUMN_EMAIL + " TEXT NOT NULL, "
            + COLUMN_FIRST_NAME + " TEXT NOT NULL, "
            + COLUMN_CUSTOMER_ID + " BIGINT UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_LAST_LOCATION_CHANGE + " TEXT , "
            + COLUMN_LANGUAGE_PREFERENCE + " TEXT, "
            + COLUMN_LAST_NAME + " TEXT NOT NULL, "
            + COLUMN_NOTIFICATION_SOUND + " TEXT, "
            + COLUMN_PHONE + " TEXT NOT NULL, "
            + COLUMN_RESOURCE_URI + " TEXT , "
            + COLUMN_USERNAME + " TEXT NOT NULL, "
            + COLUMN_TEMPERATE_SETTING + " BOOLEAN, "
            + COLUMN_PENDING_DELETE + " BOOLEAN, "
            + COLUMN_HAS_SEEN_DATA_SHARE_PROMPT + " BOOLEAN, "
            + COLUMN_DIAL_CODE + " TEXT"
            + ");";


    private static final String BASE_PATH = "customerdata";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_CUSTOMER + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/customers";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/customer";

    private static final String[] defaultProjectionCustomers = new String[]{CUSTOMER_ID, COLUMN_CREATED,
            COLUMN_CURRENT_LOCATION, COLUMN_EMAIL, COLUMN_FIRST_NAME, COLUMN_CUSTOMER_ID, COLUMN_LANGUAGE_PREFERENCE,
            COLUMN_LAST_LOCATION_CHANGE, COLUMN_LAST_NAME, COLUMN_NOTIFICATION_SOUND, COLUMN_PHONE, COLUMN_RESOURCE_URI,
            COLUMN_USERNAME, COLUMN_TEMPERATE_SETTING, COLUMN_PENDING_DELETE, COLUMN_DIAL_CODE, COLUMN_HAS_SEEN_DATA_SHARE_PROMPT};

    private static final int CUSTOMERS = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_CUSTOMER, BASE_PATH, CUSTOMERS);
    }

    @Override
    protected void checkColumns(String[] projection, int uriType) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns;
            switch (uriType) {
                case CUSTOMERS:
                    availableColumns = new HashSet<>(
                            Arrays.asList(defaultProjectionCustomers));
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
            case CUSTOMERS:
                queryBuilder.setTables(TABLE_CUSTOMERS);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case CUSTOMERS:
                    projection = defaultProjectionCustomers;
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
            case CUSTOMERS:
                id = sqlDatabase.insertOrThrow(TABLE_CUSTOMERS, "nullhack", values);
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
            case CUSTOMERS:
                rowsDeleted = sqlDatabase.delete(TABLE_CUSTOMERS, selection,
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
            case CUSTOMERS:
                rowsUpdated = sqlDatabase.update(TABLE_CUSTOMERS, values,
                        selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

}
