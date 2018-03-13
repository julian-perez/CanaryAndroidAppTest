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

public class CanaryCustomerLocationContentProvider extends
        CanaryBaseContentProvider {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = "CanaryCustomerLocationContentProvider";

    public static final String TABLE_CUSTOMER_LOCATION_LINKS = "customer_location_link_table";
    public static final String CUSTOMER_LOCATION_ID          = "_id";
    public static final String COLUMN_LOCATION_ID            = "location_id";
    public static final String COLUMN_CUSTOMER_ID            = "customer_id";

    public static final String DATABASE_CREATE_CUSTOMERS_LOCATIONS_LINKS = " CREATE TABLE " +
            TABLE_CUSTOMER_LOCATION_LINKS + "(" +
            CUSTOMER_LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_LOCATION_ID + " BIGINT "
			+ " REFERENCES " + CanaryLocationContentProvider.TABLE_LOCATIONS
			+ "(" + CanaryLocationContentProvider.COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_CUSTOMER_ID + " BIGINT ,"
			+ " UNIQUE (" + COLUMN_CUSTOMER_ID + "," + COLUMN_LOCATION_ID + ") ON CONFLICT REPLACE "
			+ ");";

    private static final String BASE_PATH   = "customerlocationdata";
    public static final  Uri    CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_CUSTOMERLOCATION + "/" + BASE_PATH);

    public static final String CONTENT_TYPE      = ContentResolver.CURSOR_DIR_BASE_TYPE + "/customerslocations";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/customerlocation";

    private static final String[] defaultProjectionCustomersLocations = new String[]{CUSTOMER_LOCATION_ID, COLUMN_LOCATION_ID, COLUMN_CUSTOMER_ID};

    private static final int CUSTOMERSLOCATIONS = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_CUSTOMERLOCATION, BASE_PATH, CUSTOMERSLOCATIONS);
    }

    @Override
    protected void checkColumns(String[] projection, int uriType) {

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = null;
            switch (uriType) {
                case CUSTOMERSLOCATIONS:
                    availableColumns = new HashSet<String>(
                            Arrays.asList(defaultProjectionCustomersLocations));
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
		case CUSTOMERSLOCATIONS:
			queryBuilder.setTables(TABLE_CUSTOMER_LOCATION_LINKS);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);

		}

		if (projection == null) {
			switch (uriType) {
			case CUSTOMERSLOCATIONS:
				projection = defaultProjectionCustomersLocations;
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
		case CUSTOMERSLOCATIONS:
			id = sqlDatabase.insert(TABLE_CUSTOMER_LOCATION_LINKS, "nullhack",
					values);
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
		case CUSTOMERSLOCATIONS:
			rowsDeleted = sqlDatabase.delete(TABLE_CUSTOMER_LOCATION_LINKS,
					selection, selectionArgs);
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
		case CUSTOMERSLOCATIONS:
			rowsUpdated = sqlDatabase.update(TABLE_CUSTOMER_LOCATION_LINKS,
					values, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}
