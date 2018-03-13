package is.yranac.canary.contentproviders;

import android.content.UriMatcher;
import android.net.Uri;

import is.yranac.canary.Constants;

/**
 * Created by sergeymorozov on 6/1/16.
 */
public class CanaryInsuranceProviderContentProvider extends CanaryContentProvider {
    private static final String LOG_TAG = "CanaryDeviceContentProvider";
    private static final String BASE_PATH = "insuranceproviderdata";
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_INSURANCE_PROVIDER, BASE_PATH, defaultUriType);
    }

    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_INSURANCE_PROVIDER + "/" + BASE_PATH);
    public static final String TABLE_LOCATION_INSURANCE_PROVIDER = "location_insurance_provider";
    public static final String INSURANCE_POLICY_ID = "_id"; // Primary Key

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PROVIDER_NAME = "policy_name";

    public static String DATABASE_CREATE_INSURANCE_PROVIDER = " CREATE TABLE " + TABLE_LOCATION_INSURANCE_PROVIDER + "("
            + INSURANCE_POLICY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID + " BIGINT UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_PROVIDER_NAME + " TEXT "
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
        return TABLE_LOCATION_INSURANCE_PROVIDER;
    }

    @Override
    public String[] getProjectionColumns() {
        return new String[]{
                INSURANCE_POLICY_ID, COLUMN_ID, COLUMN_PROVIDER_NAME};
    }
}
