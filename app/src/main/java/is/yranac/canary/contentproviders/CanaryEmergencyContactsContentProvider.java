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
 * Created by Schroeder on 10/2/15.
 */
public class CanaryEmergencyContactsContentProvider extends CanaryBaseContentProvider {
    private static final String LOG_TAG = "CanaryEmergencyContactsContentProvider";

    public static final String TABLE_EMERGENCY_CONTACTS = "emergency_contacts_table";

    public static final String EMERGENCY_CONTACTS_ID = "_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LOCATION_ID = "location_id";
    public static final String COLUMN_CONTACT_TYPE = "contact_type";
    public static final String COLUMN_PHONE_NUMBER = "customer_id";

    public static final String DATABASE_CREATE_EMERGENCY_CONTACTS = " CREATE TABLE " + TABLE_EMERGENCY_CONTACTS
            + "(" + EMERGENCY_CONTACTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID + " BIGINT UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_CONTACT_TYPE + " TEXT, "
            + COLUMN_PHONE_NUMBER + " TEXT, "
            + COLUMN_LOCATION_ID + " BIGINT REFERENCES " + CanaryLocationContentProvider.TABLE_LOCATIONS
            + "(" + CanaryLocationContentProvider.COLUMN_ID + ") ON DELETE CASCADE, "
            + " UNIQUE(" + COLUMN_CONTACT_TYPE + "," + COLUMN_LOCATION_ID + ") ON CONFLICT REPLACE "
            + ");";
    private static final String BASE_PATH = "emergencycontactsdata";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_EMERGENCY_CONTACTS + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/emergencycontacts";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/emergencycontact";

    private static final String[] defaultProjectionAvatars = new String[]{COLUMN_LOCATION_ID, COLUMN_ID,
            COLUMN_CONTACT_TYPE, COLUMN_PHONE_NUMBER};

    private static final int EMERGENCY_CONTACTS = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_EMERGENCY_CONTACTS, BASE_PATH, EMERGENCY_CONTACTS);
    }

    @Override
    protected void checkColumns(String[] projection, int uriType) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = null;
            switch (uriType) {
                case EMERGENCY_CONTACTS:
                    availableColumns = new HashSet<>(
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
            case EMERGENCY_CONTACTS:
                queryBuilder.setTables(TABLE_EMERGENCY_CONTACTS);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case EMERGENCY_CONTACTS:
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
            case EMERGENCY_CONTACTS:
                id = sqlDatabase.insert(TABLE_EMERGENCY_CONTACTS, "nullhack", values);
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
            case EMERGENCY_CONTACTS:
                rowsDeleted = sqlDatabase.delete(TABLE_EMERGENCY_CONTACTS, selection,
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
            case EMERGENCY_CONTACTS:
                rowsUpdated = sqlDatabase.update(TABLE_EMERGENCY_CONTACTS, values, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

}
