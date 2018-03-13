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
 * Created by Schroeder on 7/30/14.
 */
public class CanaryModeContentProvider extends CanaryBaseContentProvider {

    private static final String LOG_TAG = "CanaryLocationContentProvider";

    public static final String TABLE_MODES = "mode_table";

    public static final String MODE_ID = "_id"; // Primary Key of local
    // SQLite
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";

    public static final String DATABASE_CREATE_MODES = " CREATE TABLE " + TABLE_MODES + "("
            + MODE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID + " BIGINT UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_NAME + " TEXT NOT NULL " +
            ");";

    private static final String BASE_PATH = "modedata";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_MODE + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/modes";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/mode";

    private static final String[] defaultProjectionLocations = new String[]{COLUMN_ID, COLUMN_NAME};

    private static final int MODES = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_MODE, BASE_PATH, MODES);
    }

    @Override
    public Cursor query(
            Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = sURIMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriType) {
            case MODES:
                queryBuilder.setTables(TABLE_MODES);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case MODES:
                    projection = defaultProjectionLocations;
                    break;

                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);

            }
        } else {
            // check if the caller has requested a column which does not exists
            checkColumns(projection, uriType);
        }

        Cursor cursor = queryBuilder.query(
                sqlDatabase, projection, selection, selectionArgs, null, null, sortOrder);
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
            case MODES:
                id = sqlDatabase.insert(TABLE_MODES, "nullhack", values);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver()
                .notifyChange(uri, null);
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);

        int rowsDeleted = 0;
        switch (uriType) {
            case MODES:
                rowsDeleted = sqlDatabase.delete(
                        TABLE_MODES, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver()
                .notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        int rowsUpdated = 0;
        switch (uriType) {
            case MODES:
                rowsUpdated = sqlDatabase.update(
                        TABLE_MODES, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver()
                .notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    protected void checkColumns(String[] projection, int uriType) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = null;
            switch (uriType) {
                case MODES:
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
