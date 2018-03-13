package is.yranac.canary.contentproviders;

import android.content.UriMatcher;
import android.net.Uri;

import is.yranac.canary.Constants;

/**
 * Created by sergeymorozov on 6/1/16.
 */
public class CanaryInsurancePolicyContentProvider extends CanaryContentProvider {
    private static final String LOG_TAG = "CanaryDeviceContentProvider";
    private static final String BASE_PATH = "insurancepolicydata";
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_INSURANCE_POLICY, BASE_PATH, defaultUriType);
    }

    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_INSURANCE_POLICY + "/" + BASE_PATH);
    public static final String TABLE_LOCATION_INSURANCE_POLICY = "location_insurance_policy";
    public static final String INSURANCE_POLICY_ID = "_id"; // Primary Key

    public static final String COLUMN_LOCATION_ID = "location_id";
    public static final String COLUMN_POLICY_NUMBER = "policy_number";
    public static final String COLUMN_PPROVIDER_NAME = "policy_name";
    public static final String COLUMN_WILL_SHARE_DATA = "will_share_data";
    public static final String COLUMN_PROVIDER_ID = "policy_id";

    public static String DATABASE_CREATE_INSURANCE_POLICY = " CREATE TABLE " + TABLE_LOCATION_INSURANCE_POLICY + "("
            + INSURANCE_POLICY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_LOCATION_ID + " BIGINT "
            + " REFERENCES " + CanaryLocationContentProvider.TABLE_LOCATIONS
            + "(" + CanaryLocationContentProvider.COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_PPROVIDER_NAME + " TEXT, "
            + COLUMN_POLICY_NUMBER + " TEXT, "
            + COLUMN_WILL_SHARE_DATA + " BOOLEAN, "
            + COLUMN_PROVIDER_ID + " INTEGER "
            + ");";


    @Override
    public String getAuthority() {
        return Constants.AUTHORITY_DEVICE_STATISTICS;
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
        return TABLE_LOCATION_INSURANCE_POLICY;
    }

    @Override
    public String[] getProjectionColumns() {
        return new String[]{
                INSURANCE_POLICY_ID, COLUMN_LOCATION_ID, COLUMN_POLICY_NUMBER,
                COLUMN_PPROVIDER_NAME, COLUMN_WILL_SHARE_DATA, COLUMN_PROVIDER_ID};
    }
}
