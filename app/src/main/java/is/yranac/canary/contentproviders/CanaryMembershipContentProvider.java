package is.yranac.canary.contentproviders;

import android.content.UriMatcher;
import android.net.Uri;

import is.yranac.canary.Constants;

/**
 * Created by michaelschroeder on 1/10/18.
 */

public class CanaryMembershipContentProvider extends CanaryContentProvider {
    public static final String TABLE_MEMBERSHIP = "membership_table";

    public static final String MEMBERSHIP_ID = "_id";
    public static final String COLUMN_CUSTOMER_ID = "customer_id";
    public static final String COLUMN_LOCATION_ID = "location_id";
    public static final String COLUMN_PRESENCE_NOTIFICATIONS = "presence_notifications";

    public static final String DATABASE_CREATE_MEMBERSHIP = " CREATE TABLE " + TABLE_MEMBERSHIP + " ("
            + MEMBERSHIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_LOCATION_ID + " BIGINT "
            + " REFERENCES " + CanaryLocationContentProvider.TABLE_LOCATIONS
            + " (" + CanaryLocationContentProvider.COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_CUSTOMER_ID + " BIGINT, "
            + COLUMN_PRESENCE_NOTIFICATIONS + " BOOLEAN, "
            + " UNIQUE(" + COLUMN_LOCATION_ID + "," + COLUMN_CUSTOMER_ID + ") ON CONFLICT REPLACE "
            + ");";

    private static final String BASE_PATH = "membershipdata";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + Constants.AUTHORITY_MEMBERSHIP + "/" + BASE_PATH);


    private static final String[] defaultProjectionAvatars = new String[]{
            COLUMN_LOCATION_ID,
            COLUMN_PRESENCE_NOTIFICATIONS,
            COLUMN_CUSTOMER_ID
    };

    private static final int MEMBERSHIP = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_MEMBERSHIP, BASE_PATH, MEMBERSHIP);
    }

    @Override
    public String getTableName() {
        return TABLE_MEMBERSHIP;
    }

    @Override
    public String[] getProjectionColumns() {
        return defaultProjectionAvatars;
    }

    @Override
    public String getAuthority() {
        return Constants.AUTHORITY_MEMBERSHIP;
    }

    @Override
    public String getBasePath() {
        return BASE_PATH;
    }

    @Override
    public Uri getContentUrl() {
        return CONTENT_URI;
    }

    @Override
    public UriMatcher getURIMatcher() {
        return sURIMatcher;
    }
}

