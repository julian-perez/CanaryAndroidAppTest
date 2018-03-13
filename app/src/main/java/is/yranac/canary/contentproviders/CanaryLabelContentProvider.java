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
 * Created by Schroeder on 9/22/14.
 */
public class CanaryLabelContentProvider extends CanaryBaseContentProvider {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = "CanaryLabelContentProvider";

    public static final String TABLE_LABELS = "label_table";

    public static final String LABEL_ID = "_id"; // Primary Key of local SQLite

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ENTRY_ID = "entry_id";

    public static final String DATABASE_CREATE_LABELS = " CREATE TABLE " + TABLE_LABELS + "("
            + LABEL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID + " BIGINT, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_ENTRY_ID + " BIGINT "
            + ");";

    private static final String BASE_PATH = "labeldata";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_LABEL + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/labels";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/label";

    private static final String[] defaultProjectionLabels = new String[]{LABEL_ID, COLUMN_ID, COLUMN_NAME, COLUMN_ENTRY_ID};
    private static final int LABELS = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_LABEL, BASE_PATH, LABELS);
    }

    @Override
    protected void checkColumns(String[] projection, int uriType) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = null;
            switch (uriType) {
                case LABELS:
                    availableColumns = new HashSet<String>(
                            Arrays.asList(defaultProjectionLabels));
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
            case LABELS:
                queryBuilder.setTables(TABLE_LABELS);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case LABELS:
                    projection = defaultProjectionLabels;
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
            case LABELS:
                id = sqlDatabase.insert(TABLE_LABELS, "nullhack", values);
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
            case LABELS:
                rowsDeleted = sqlDatabase.delete(TABLE_LABELS, selection,
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
            case LABELS:
                rowsUpdated = sqlDatabase.update(TABLE_LABELS, values,
                        selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

}
