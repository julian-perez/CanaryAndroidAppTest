package is.yranac.canary.contentproviders;

import android.content.UriMatcher;
import android.net.Uri;

import is.yranac.canary.Constants;

/**
 * Created by Schroeder on 5/5/15.
 */
public class CanarySubscriptionContentProvider extends CanaryContentProvider {

    public static final String TABLE_SUBSCRIPTION = "subscription_table";

    public static final String COLUMN_LOCATION_ID = "location_id";
    public static final String COLUMN_TRIAL = "trial";
    public static final String COLUMN_MEMBERSHIP = "membership";
    public static final String COLUMN_TIMELINE_LENGTH = "timeline_length";
    public static final String COLUMN_EXPIRES_ON = "expires_on";
    public static final String COLUMN_C_STAT_OVERRIDE = "cstat_override";
    public static final String COLUMN_PAYMENT_PERIOD = "payment_period";
    public static final String COLUMN_CURRENCY = "currency";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_EMPLOYEE = "employee";
    public static final String COLUMN_LEGACY_FREE = "legacy_free";

    public static final String DATABASE_SUBSCRIPTION = " CREATE TABLE " + TABLE_SUBSCRIPTION + "("
            + COLUMN_LOCATION_ID + " BIGINT UNIQUE ON CONFLICT REPLACE "
            + " REFERENCES " + CanaryLocationContentProvider.TABLE_LOCATIONS
            + " (" + CanaryLocationContentProvider.COLUMN_ID + ") ON DELETE CASCADE , "
            + COLUMN_TRIAL + " BOOLEAN,"
            + COLUMN_MEMBERSHIP + " BOOLEAN, "
            + COLUMN_C_STAT_OVERRIDE + " BOOLEAN, "
            + COLUMN_EMPLOYEE + " BOOLEAN, "
            + COLUMN_LEGACY_FREE + " BOOLEAN, "
            + COLUMN_EXPIRES_ON + " DATETIME, "
            + COLUMN_TIMELINE_LENGTH + " INTEGER, "
            + COLUMN_PAYMENT_PERIOD + " TEXT, "
            + COLUMN_CURRENCY + " TEXT, "
            + COLUMN_PRICE + " INTEGER "
            + ");";

    private static final String BASE_PATH = "serviceplans";
    public static final Uri CONTENT_URI = Uri.parse(
            "content://" + Constants.AUTHORITY_SUBSCRIPTION + "/" + BASE_PATH);

    private static final int SUBSCRIPTION = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_SUBSCRIPTION, BASE_PATH, SUBSCRIPTION);
    }

    @Override
    public String getAuthority() {
        return Constants.AUTHORITY_SUBSCRIPTION;
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

    @Override
    public String getTableName() {
        return TABLE_SUBSCRIPTION;
    }

    @Override
    public String[] getProjectionColumns() {
        return new String[]{COLUMN_LOCATION_ID,
                COLUMN_TRIAL, COLUMN_MEMBERSHIP, COLUMN_C_STAT_OVERRIDE, COLUMN_EXPIRES_ON,
                COLUMN_TIMELINE_LENGTH, COLUMN_CURRENCY, COLUMN_PAYMENT_PERIOD, COLUMN_PRICE,
                COLUMN_EMPLOYEE, COLUMN_LEGACY_FREE};
    }
}



