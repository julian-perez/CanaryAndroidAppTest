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
 * Created by Schroeder on 8/15/14.
 */
public class CanaryThumbnailContentProvider extends CanaryBaseContentProvider {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = "CanaryThumbnailContentProvider";

    public static final String TABLE_THUMBNAIL = "thumbnail_table";

    public static final String THUMBNAIL_ID = "_id";
    public static final String COLUMN_THUMBNAIL_ID = "thumbnail_id";
    public static final String COLUMN_DEVICE_UUID = "device_uuid";
    public static final String COLUMN_DEVICE_ID = "device_id";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_ENTRY_ID = "entry_id";

    public static final String DATABASE_CREATE_THUMBNAIL = " CREATE TABLE " + TABLE_THUMBNAIL
            + "(" + THUMBNAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_THUMBNAIL_ID + " BIGINT UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_DEVICE_UUID + " TEXT NOT NULL, "
            + COLUMN_DEVICE_ID + " BIGINT, "
            + COLUMN_IMAGE_URL + " TEXT, "
            + COLUMN_ENTRY_ID + " BIGINT" + " REFERENCES " + CanaryEntryContentProvider.TABLE_ENTRIES
            + " (" + CanaryEntryContentProvider.COLUMN_ID + ") ON DELETE CASCADE"
            + ");";

    private static final String BASE_PATH = "thumbnaildata";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_THUMBNAIL + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/thumbnails";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/thumbnail";

    private static final String[] defaultProjectionThumbnails = new String[]{THUMBNAIL_ID, COLUMN_DEVICE_UUID,
            COLUMN_IMAGE_URL, COLUMN_ENTRY_ID, COLUMN_DEVICE_ID, COLUMN_THUMBNAIL_ID};

    private static final int THUMBNAIL = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_THUMBNAIL, BASE_PATH, THUMBNAIL);
    }

    @Override
    protected void checkColumns(String[] projection, int uriType) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = null;
            switch (uriType) {
                case THUMBNAIL:
                    availableColumns = new HashSet<String>(
                            Arrays.asList(defaultProjectionThumbnails));
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
            case THUMBNAIL:
                queryBuilder.setTables(TABLE_THUMBNAIL);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case THUMBNAIL:
                    projection = defaultProjectionThumbnails;
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
            case THUMBNAIL:
                id = sqlDatabase.insert(TABLE_THUMBNAIL, "nullhack", values);
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
            case THUMBNAIL:
                rowsDeleted = sqlDatabase.delete(TABLE_THUMBNAIL, selection,
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
            case THUMBNAIL:
                rowsUpdated = sqlDatabase.update(TABLE_THUMBNAIL, values, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
