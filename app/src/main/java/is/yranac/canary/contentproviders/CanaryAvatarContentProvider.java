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

public class CanaryAvatarContentProvider extends CanaryBaseContentProvider {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = "CanaryAvatarContentProvider";

    public static final String TABLE_AVATARS = "avatar_table";

    public static final String AVATAR_ID            = "_id";
    public static final String COLUMN_ID            = "id";
    public static final String COLUMN_IMAGE         = "image";
    public static final String COLUMN_CUSTOMER_ID   = "customer_id";
    public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";

    public static final String DATABASE_CREATE_AVATARS = " CREATE TABLE " + TABLE_AVATARS + "("
			+ AVATAR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_ID + " BIGINT UNIQUE ON CONFLICT REPLACE, "
			+ COLUMN_IMAGE + " TEXT NOT NULL, "
			+ COLUMN_CUSTOMER_ID + " BIGINT UNIQUE ON CONFLICT REPLACE"
			+ " REFERENCES " + CanaryCustomerContentProvider.TABLE_CUSTOMERS
			+ "(" + CanaryCustomerContentProvider.COLUMN_CUSTOMER_ID + ") ON DELETE CASCADE, "
			+ COLUMN_THUMBNAIL_URL + " TEXT NOT NULL" + ");";

    private static final String BASE_PATH   = "avatardata";
    public static final  Uri    CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_AVATAR + "/" + BASE_PATH);

    public static final String CONTENT_TYPE      = ContentResolver.CURSOR_DIR_BASE_TYPE + "/avatars";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/avatar";

    private static final String[] defaultProjectionAvatars = new String[]{AVATAR_ID, COLUMN_ID, COLUMN_IMAGE, COLUMN_THUMBNAIL_URL};

    private static final int AVATARS = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_AVATAR, BASE_PATH, AVATARS);
    }

    @Override
    protected void checkColumns(String[] projection, int uriType) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = null;
            switch (uriType) {
                case AVATARS:
                    availableColumns = new HashSet<String>(
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
		case AVATARS:
			queryBuilder.setTables(TABLE_AVATARS);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);

		}

		if (projection == null) {
			switch (uriType) {
			case AVATARS:
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
		case AVATARS:
			id = sqlDatabase.insert(TABLE_AVATARS, "nullhack", values);
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
		case AVATARS:
			rowsDeleted = sqlDatabase.delete(TABLE_AVATARS, selection,
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
		case AVATARS:
			rowsUpdated = sqlDatabase.update(TABLE_AVATARS, values, selection,
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}
