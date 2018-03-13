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
 * Created by Schroeder on 3/1/16.
 */
public class CanaryFeatureFlagsContentProvider extends CanaryBaseContentProvider {

    public static final String TABLE_FEATURE_FLAGS = "feature_flag_table";

    public static final String FEATURE_FLAG_ID = "_id"; // Primary Key of local
    // SQLite

    public static final String COLUMN_LOCATION_ID = "location_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_META_DATA = "meta_data";
    public static final String COLUMN_ENABLED = "uploader_active";


    public static final String DATABASE_CREATE_FEATURE_FLAGS = " CREATE TABLE " + TABLE_FEATURE_FLAGS + "("
            + FEATURE_FLAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_LOCATION_ID + " BIGINT "
            + " REFERENCES " + CanaryLocationContentProvider.TABLE_LOCATIONS
            + "(" + CanaryLocationContentProvider.COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_META_DATA + " TEXT, "
            + COLUMN_ENABLED + " BOOLEAN, "
            + COLUMN_NAME + " TEXT "
            + ");";


    private static final String BASE_PATH = "featureflagdata";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_FEATURE_FLAG + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/avatars";

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/avatar";

    private static final String[] defaultProjectionAvatars = new String[]{FEATURE_FLAG_ID,
            COLUMN_ENABLED, COLUMN_LOCATION_ID, COLUMN_META_DATA, COLUMN_NAME};

    private static final int FEATUREFLAGS = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_FEATURE_FLAG, BASE_PATH, FEATUREFLAGS);
    }


    @Override
    protected void checkColumns(String[] projection, int uriType) {
        if (projection != null) {


            HashSet<String> requestedColumns = new HashSet<>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns;
            switch (uriType) {
                case FEATUREFLAGS:
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
            case FEATUREFLAGS:
                queryBuilder.setTables(TABLE_FEATURE_FLAGS);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case FEATUREFLAGS:
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
            case FEATUREFLAGS:
                id = sqlDatabase.insert(TABLE_FEATURE_FLAGS, "nullhack", values);
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
            case FEATUREFLAGS:
                rowsDeleted = sqlDatabase.delete(TABLE_FEATURE_FLAGS, selection,
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
            case FEATUREFLAGS:
                rowsUpdated = sqlDatabase.update(TABLE_FEATURE_FLAGS, values, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
