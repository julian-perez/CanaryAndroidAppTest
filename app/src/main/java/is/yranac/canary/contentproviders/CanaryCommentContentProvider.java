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

public class CanaryCommentContentProvider extends CanaryBaseContentProvider {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = "CanaryCommentContentProvider";

    public static final String TABLE_COMMENTS = "comment_table";

    public static final String COMMENT_ID = "_id"; // Primary Key of local
    // SQLite
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_CREATED = "created";
    public static final String COLUMN_CUSTOMER_URI = "customerUri";
    public static final String COLUMN_ENTRY_ID = "entry_resource_uri";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MODIFIED = "modified";

    public static final String DATABASE_CREATE_COMMENTS = " CREATE TABLE " + TABLE_COMMENTS + "("
            + COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_BODY + " TEXT NOT NULL, "
            + COLUMN_CREATED + " LONG, "
            + COLUMN_CUSTOMER_URI + " TEXT NOT NULL, "
            + COLUMN_ID + " BIGINT UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_MODIFIED + " LONG, "
            + COLUMN_ENTRY_ID + " BIGINT "
            + " REFERENCES " + CanaryEntryContentProvider.TABLE_ENTRIES
            + " (" + CanaryEntryContentProvider.COLUMN_ID + ") ON DELETE CASCADE "
            + ");";

    private static final String BASE_PATH = "commentdata";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_COMMENT + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/comments";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/comment";

    private static final String[] defaultProjectionComments = new String[]{COMMENT_ID, COLUMN_BODY,
            COLUMN_CREATED, COLUMN_CUSTOMER_URI, COLUMN_ENTRY_ID, COLUMN_ID, COLUMN_MODIFIED};

    private static final int COMMENTS = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_COMMENT, BASE_PATH, COMMENTS);
    }

    @Override
    protected void checkColumns(String[] projection, int uriType) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = null;
            switch (uriType) {
                case COMMENTS:
                    availableColumns = new HashSet<>(
                            Arrays.asList(defaultProjectionComments));
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
            case COMMENTS:
                queryBuilder.setTables(TABLE_COMMENTS);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case COMMENTS:
                    projection = defaultProjectionComments;
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
            case COMMENTS:
                id = sqlDatabase.insert(TABLE_COMMENTS, "nullhack", values);
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
            case COMMENTS:
                rowsDeleted = sqlDatabase.delete(TABLE_COMMENTS, selection,
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
            case COMMENTS:
                rowsUpdated = sqlDatabase.update(TABLE_COMMENTS, values,
                        selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

}
