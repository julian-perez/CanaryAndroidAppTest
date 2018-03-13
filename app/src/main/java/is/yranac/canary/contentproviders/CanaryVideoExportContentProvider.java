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
 * Created by Schroeder on 4/29/15.
 */
public class CanaryVideoExportContentProvider extends CanaryBaseContentProvider {
    @SuppressWarnings("unused")
    private static final String LOG_TAG = "CanaryVideoExportContentProvider";

    public static final String TABLE_VIDEO_EXPORT = "video_export_table";

    public static final String VIDEO_EXPORT_ID = "_id";
    public static final String COLUMN_DEVICE_UUID = "device_uuid";
    public static final String COLUMN_ENTRY_ID = "entryId";
    public static final String COLUMN_PROCESSING = "processing";
    public static final String COLUMN_VIDEO_LENGTH = "video_size";
    public static final String COLUMN_VIDEO_SIZE = "video_length";
    public static final String COLUMN_REQUESTED_AT = "request_at";
    public static final String COLUMN_DOWNLOAD_ID = "download_id";

    public static final String DATABASE_CREATE_VIDEO_EXPORT = " CREATE TABLE " + TABLE_VIDEO_EXPORT + "("
            + VIDEO_EXPORT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_PROCESSING + " BOOLEAN, "
            + COLUMN_DEVICE_UUID + " TEXT NOT NULL, "
            + COLUMN_VIDEO_LENGTH + " INTEGER, "
            + COLUMN_VIDEO_SIZE + " INTEGER, "
            + COLUMN_DOWNLOAD_ID + " BIGINT, "
            + COLUMN_REQUESTED_AT + " LONG, "
            + COLUMN_ENTRY_ID + " BIGINT "
            + " REFERENCES " + CanaryEntryContentProvider.TABLE_ENTRIES
            + " (" + CanaryEntryContentProvider.COLUMN_ID + ") ON DELETE CASCADE"
            + ");";

    private static final String BASE_PATH = "videoexportdata";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_VIDEO_EXPORT + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/videoexports";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/videoexport";

    private static final String[] defaultProjectionVideoExport = new String[]{VIDEO_EXPORT_ID,
            COLUMN_PROCESSING, COLUMN_DEVICE_UUID, COLUMN_ENTRY_ID, COLUMN_DOWNLOAD_ID, COLUMN_VIDEO_SIZE,
            COLUMN_VIDEO_LENGTH, COLUMN_REQUESTED_AT};

    private static final int VIDEOEXPORT = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_VIDEO_EXPORT, BASE_PATH, VIDEOEXPORT);
    }

    @Override
    protected void checkColumns(String[] projection, int uriType) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = null;
            switch (uriType) {
                case VIDEOEXPORT:
                    availableColumns = new HashSet<>(
                            Arrays.asList(defaultProjectionVideoExport));
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
            case VIDEOEXPORT:
                queryBuilder.setTables(TABLE_VIDEO_EXPORT);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case VIDEOEXPORT:
                    projection = defaultProjectionVideoExport;
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
            case VIDEOEXPORT:
                id = sqlDatabase.insert(TABLE_VIDEO_EXPORT, "nullhack", values);
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

        int rowsDeleted;
        switch (uriType) {
            case VIDEOEXPORT:
                rowsDeleted = sqlDatabase.delete(TABLE_VIDEO_EXPORT, selection,
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
        int rowsUpdated;
        switch (uriType) {
            case VIDEOEXPORT:
                rowsUpdated = sqlDatabase.update(TABLE_VIDEO_EXPORT, values, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
