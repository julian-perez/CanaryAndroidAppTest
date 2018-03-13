package is.yranac.canary.contentproviders;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by sergeymorozov on 6/1/16.
 */
public abstract class CanaryContentProvider extends CanaryBaseContentProvider {

    protected static final int defaultUriType = 10;

    public abstract String getTableName();

    public abstract String[] getProjectionColumns();

    public abstract String getAuthority();

    public abstract String getBasePath();

    public abstract Uri getContentUrl();

    public abstract UriMatcher getURIMatcher();

    //TODO: add bulk insert

    @Override
    protected void checkColumns(String[] projection, int uriType) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns;
            switch (uriType) {
                case defaultUriType:
                    availableColumns = new HashSet<>(
                            Arrays.asList(getProjectionColumns()));
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
        int uriType = getURIMatcher().match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriType) {
            case defaultUriType:
                queryBuilder.setTables(getTableName());
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case defaultUriType:
                    projection = getProjectionColumns();
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
        int uriType = getURIMatcher().match(uri);
        long id = 0;
        switch (uriType) {
            case defaultUriType:
                id = sqlDatabase.insert(getTableName(), "nullhack", values);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(getContentUrl(), id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = getURIMatcher().match(uri);

        int rowsDeleted = 0;
        switch (uriType) {
            case defaultUriType:
                rowsDeleted = sqlDatabase.delete(getTableName(), selection,
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
        int uriType = getURIMatcher().match(uri);
        int rowsUpdated = 0;
        switch (uriType) {
            case defaultUriType:
                rowsUpdated = sqlDatabase.update(getTableName(), values, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
