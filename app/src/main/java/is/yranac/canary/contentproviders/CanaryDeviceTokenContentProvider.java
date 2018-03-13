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

/**
 * Created by Schroeder on 1/12/16.
 */
public class CanaryDeviceTokenContentProvider extends CanaryBaseContentProvider {

    private static final String LOG_TAG = "CanaryDeviceTokenContentProvider";

    public static final String TABLE_DEVICE_TOKEN_TABLE = "device_token_table";

    public static final String DEVICE_TOKEN_ID = "_id";
    public static final String COLUMN_TOKEN = "device_token";
    public static final String COLUMN_BUILD_NUMBER = "build_number";

    public static final String DATABASE_CREATE_DEVICE_TOKENS = " CREATE TABLE " + TABLE_DEVICE_TOKEN_TABLE
            + "(" + DEVICE_TOKEN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_BUILD_NUMBER + " INTEGER, "
            + COLUMN_TOKEN + " TEXT UNIQUE "
            + ");";


    private static final String BASE_PATH = "devicetoken";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_DEVICE_TOKEN + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/devicetoken";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/devicetoken";

    private static final String[] defaultProjectionAvatars = new String[]{DEVICE_TOKEN_ID, COLUMN_TOKEN,
            COLUMN_BUILD_NUMBER};

    private static final int DEVICE_TOKEN = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_DEVICE_TOKEN, BASE_PATH, DEVICE_TOKEN);
    }

    @Override
    protected void checkColumns(String[] projection, int uriType) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = null;
            switch (uriType) {
                case DEVICE_TOKEN:
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
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int uriType = sURIMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriType) {
            case DEVICE_TOKEN:
                queryBuilder.setTables(TABLE_DEVICE_TOKEN_TABLE);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case DEVICE_TOKEN:
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
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        long id;
        switch (uriType) {
            case DEVICE_TOKEN:
                id = sqlDatabase.insert(TABLE_DEVICE_TOKEN_TABLE, "nullhack", values);
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

        int rowsDeleted = 0;
        switch (uriType) {
            case DEVICE_TOKEN:
                rowsDeleted = sqlDatabase.delete(TABLE_DEVICE_TOKEN_TABLE, selection,
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
            case DEVICE_TOKEN:
                rowsUpdated = sqlDatabase.update(TABLE_DEVICE_TOKEN_TABLE, values, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
