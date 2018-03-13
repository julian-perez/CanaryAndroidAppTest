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
 * Created by Schroeder on 3/30/15.
 */

public class CanaryEntryCustomerContentProvider extends CanaryBaseContentProvider {

    private static final String LOG_TAG = "CanaryCustomerContentProvider";

    public static final String TABLE_ENTRY_CUSTOMERS = "customer_entry_table";

    public static final String COLUMN_ENTRY_ID = "entry_id";
    public static final String COLUMN_CUSTOMER_RESOURCE_URI = "customer_resource_uri";

    public static final String DATABASE_CREATE_ENTRY_CUSTOMERS = " CREATE TABLE " + TABLE_ENTRY_CUSTOMERS +"("
            + COLUMN_ENTRY_ID + " BIGINT"
            + " REFERENCES " + CanaryEntryContentProvider.TABLE_ENTRIES
            + " (" + CanaryEntryContentProvider.COLUMN_ID + ") ON DELETE CASCADE" + ", "
            + COLUMN_CUSTOMER_RESOURCE_URI + " TEXT NOT NULL"
            + ")";


    private static final String[] defaultProjectionEntries = new String[]{COLUMN_ENTRY_ID,
            COLUMN_CUSTOMER_RESOURCE_URI};

    public static final  String   CONTENT_TYPE             = ContentResolver.CURSOR_DIR_BASE_TYPE + "/entry_customers";
    public static final  String   CONTENT_ITEM_TYPE        = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/entry_customer";

    private static final String BASE_PATH   = "entrycustomersprovider";
    public static final  Uri    CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_ENTRY_CUSTOMERS + "/" + BASE_PATH);
    private static final int ENTRY_CUSTOMERS = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_ENTRY_CUSTOMERS, BASE_PATH, ENTRY_CUSTOMERS);
    }

    @Override
    protected void checkColumns(String[] projection, int uriType) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns;
            switch (uriType) {
                case ENTRY_CUSTOMERS:
                    availableColumns = new HashSet<>(
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
            case ENTRY_CUSTOMERS:
                queryBuilder.setTables(TABLE_ENTRY_CUSTOMERS);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case ENTRY_CUSTOMERS:
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
            case ENTRY_CUSTOMERS:
                id = sqlDatabase.insert(TABLE_ENTRY_CUSTOMERS, "nullhack", values);
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
            case ENTRY_CUSTOMERS:
                rowsDeleted = sqlDatabase.delete(TABLE_ENTRY_CUSTOMERS, selection,
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
            case ENTRY_CUSTOMERS:
                rowsUpdated = sqlDatabase.update(TABLE_ENTRY_CUSTOMERS, values, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}