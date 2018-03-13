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
 * Created by Schroeder on 10/19/15.
 */

public class CanaryLocationNetworkContentProvider extends CanaryBaseContentProvider {

    public static final String TABLE_LOCATION_NETWORK = "location_network_table";

    public static final String COLUMN_LOCATION_ID = "location_id";
    public static final String COLUMN_NETWORK_NAME = "network_name";
    public static final String COLUMN_MAC_ADDRESS = "mac_address";
    public static final String COLUMN_DISTANCE_FROM_LOCATION = "distance_from_location";

    public static final String DATABASE_CREATE_LOCATION_NETWORKS = " CREATE TABLE " + TABLE_LOCATION_NETWORK +
            "(" +
            COLUMN_NETWORK_NAME + " TEXT, " +
            COLUMN_LOCATION_ID + " BIGINT " +
            " REFERENCES " + CanaryLocationContentProvider.TABLE_LOCATIONS +
            " (" + CanaryLocationContentProvider.COLUMN_ID + ") ON DELETE CASCADE , " +
            COLUMN_MAC_ADDRESS + " TEXT UNIQUE ON CONFLICT REPLACE, " +
            COLUMN_DISTANCE_FROM_LOCATION + " DOUBLE " +
            ");";


    private static final String BASE_PATH = "locationnetwork";
    public static final Uri CONTENT_URI = Uri.parse(
            "content://" + Constants.AUTHORITY_LOCATION_NETWORK + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/serviceplans";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/serviceplan";

    private static final String[] defaultProjectionLocationNetwork = new String[]{COLUMN_NETWORK_NAME,
            COLUMN_LOCATION_ID, COLUMN_MAC_ADDRESS};
    private static final int LOCATION_NETWORK = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_LOCATION_NETWORK, BASE_PATH, LOCATION_NETWORK);
    }

    @Override
    public Cursor query(
            Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = sURIMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriType) {
            case LOCATION_NETWORK:
                queryBuilder.setTables(TABLE_LOCATION_NETWORK);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case LOCATION_NETWORK:
                    projection = defaultProjectionLocationNetwork;
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
            case LOCATION_NETWORK:
                id = sqlDatabase.insert(TABLE_LOCATION_NETWORK, "nullhack", values);
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
            case LOCATION_NETWORK:
                rowsDeleted = sqlDatabase.delete(TABLE_LOCATION_NETWORK, selection,
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
            case LOCATION_NETWORK:
                rowsUpdated = sqlDatabase.update(TABLE_LOCATION_NETWORK, values,
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
            HashSet<String> requestedColumns = new HashSet<>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = null;
            switch (uriType) {
                case LOCATION_NETWORK:
                    availableColumns = new HashSet<String>(
                            Arrays.asList(defaultProjectionLocationNetwork));
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
