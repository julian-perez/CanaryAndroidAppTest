package is.yranac.canary.contentproviders;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.Arrays;
import java.util.HashSet;

import is.yranac.canary.Constants;

public class CanaryInvitationContentProvider extends CanaryBaseContentProvider {
    @SuppressWarnings("unused")
    private static final String LOG_TAG = "CanaryInvitationContentProvider";

    public static final String TABLE_INVITATIONS = "invitation_table";

    public static final String INVITATION_ID = "invitation_id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_INVITER = "inviter";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_LOCATION_ID = "location_id";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_USER_TYPE = "user_type";
    public static final String COLUMN_PENDING_DELETE = "pending_delete";

    public static final String DATABASE_CREATE_INVITATIONS = " CREATE TABLE " + TABLE_INVITATIONS + "("
            + INVITATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_EMAIL + " TEXT NOT NULL, "
            + COLUMN_FIRST_NAME + " TEXT NOT NULL, "
            + COLUMN_ID + " BIGINT, "
            + COLUMN_INVITER + " TEXT NOT NULL, "
            + COLUMN_LAST_NAME + " TEXT NOT NULL, "
            + COLUMN_LOCATION_ID + " BIGINT "
            + " REFERENCES " + CanaryLocationContentProvider.TABLE_LOCATIONS
            + "(" + CanaryLocationContentProvider.COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_PHONE + " TEXT NOT NULL, "
            + COLUMN_STATUS + " TEXT NOT NULL, "
            + COLUMN_PENDING_DELETE + " BOOLEAN, "
            + COLUMN_USER_TYPE + " TEXT NOT NULL"
            + ");";

    private static final String BASE_PATH = "invitationdata";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_INVITATION + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/invitations";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/invitation";

    private static final String[] defaultProjectionLocations = new String[]{INVITATION_ID, COLUMN_EMAIL,
            COLUMN_FIRST_NAME, COLUMN_ID, COLUMN_INVITER, COLUMN_LAST_NAME, COLUMN_LOCATION_ID, COLUMN_PHONE,
            COLUMN_STATUS, COLUMN_USER_TYPE, COLUMN_PENDING_DELETE};


    private static final int INVITATIONS = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_INVITATION, BASE_PATH, INVITATIONS);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Cursor query(
            Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = sURIMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriType) {
            case INVITATIONS:
                queryBuilder.setTables(TABLE_INVITATIONS);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        if (projection == null) {
            switch (uriType) {
                case INVITATIONS:
                    projection = defaultProjectionLocations;
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
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        long id = 0;
        switch (uriType) {
            case INVITATIONS:
                id = sqlDatabase.insert(TABLE_INVITATIONS, "nullhack", values);
                if (id <= 0) {
                    throw new SQLException("Failed to insert row into " + uri);
                }
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
            case INVITATIONS:
                rowsDeleted = sqlDatabase.delete(TABLE_INVITATIONS, selection,
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
            case INVITATIONS:
                rowsUpdated = sqlDatabase.update(TABLE_INVITATIONS, values,
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
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = null;
            switch (uriType) {
                case INVITATIONS:
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
