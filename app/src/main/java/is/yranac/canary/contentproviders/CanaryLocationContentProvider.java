package is.yranac.canary.contentproviders;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;

import is.yranac.canary.Constants;

public class CanaryLocationContentProvider extends CanaryContentProvider {

    private static final String LOG_TAG = "CanaryLocationContentProvider";

    public static final String TABLE_LOCATIONS = "location_table";

    public static final String LOCATION_ID = "_id"; // Primary Key
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_ADDRESS_TWO = "address_two";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_CREATED = "created";
    public static final String COLUMN_CURRENT_MODE = "current_mode";
    public static final String COLUMN_GEOFENCE_RADIUS = "geofence_radius";
    public static final String COLUMN_ID = "location_id";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_OWNER = "owner";
    public static final String COLUMN_IS_PRIVATE = "privacy";
    public static final String COLUMN_NIGHT_MODE_ENABLED = "night_mode_enabled";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_ENTRY_TOTAL = "has_entry_flagged_total";
    public static final String COLUMN_ENTRY_FLAGGED_TOTAL = "has_entry_total";
    public static final String COLUMN_ENTRY_ARMED_TOTAL = "has_armed_total";
    public static final String COLUMN_AUTO_MODE_ENABLED = "auto_mode_enabled";
    public static final String COLUMN_ZIP = "zip";
    public static final String COLUMN_LAST_MODIFIED = "last_modified";
    public static final String COLUMN_SHARING_DATA = "is_sharing_data";
    public static final String COLUMN_TRIAL_EXPIRED = "trial_expired";
    public static final String COLUMN_SHOW_MASK_TUTORIAL = "seen_mask_tutorial";

    public static final String DATABASE_CREATE_LOCATIONS = " CREATE TABLE " + TABLE_LOCATIONS + "("
            + LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ADDRESS + " TEXT, "
            + COLUMN_ADDRESS_TWO + " TEXT, "
            + COLUMN_CITY + " TEXT, "
            + COLUMN_COUNTRY + " TEXT, "
            + COLUMN_CREATED + " LONG, "
            + COLUMN_CURRENT_MODE + " INTEGER, "
            + COLUMN_GEOFENCE_RADIUS + " INTEGER, "
            + COLUMN_ID + " BIGINT UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_LAT + " DOUBLE, "
            + COLUMN_LNG + " DOUBLE, "
            + COLUMN_LAST_MODIFIED + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_OWNER + " TEXT, "
            + COLUMN_IS_PRIVATE + " BOOLEAN, "
            + COLUMN_NIGHT_MODE_ENABLED + " BOOLEAN, "
            + COLUMN_STATE + " TEXT, "
            + COLUMN_ZIP + " TEXT, "
            + COLUMN_ENTRY_TOTAL + " BOOLEAN,"
            + COLUMN_SHOW_MASK_TUTORIAL + " BOOLEAN, "
            + COLUMN_ENTRY_FLAGGED_TOTAL + " BOOLEAN, "
            + COLUMN_ENTRY_ARMED_TOTAL + " BOOLEAN, "
            + COLUMN_AUTO_MODE_ENABLED + " BOOLEAN, "
            + COLUMN_SHARING_DATA + " BOOLEAN, "
            + COLUMN_TRIAL_EXPIRED + " BOOLEAN "
            + ");";

    public static final String UPDATE_TIME_TRIGGER =
            "CREATE TRIGGER update_time_trigger" +
                    "  AFTER UPDATE ON " + TABLE_LOCATIONS + " FOR EACH ROW" +
                    "  BEGIN " +
                    "UPDATE " + TABLE_LOCATIONS +
                    "  SET " + COLUMN_LAST_MODIFIED + " = datetime()" +
                    "  WHERE " + LOCATION_ID + " = old." + LOCATION_ID + ";" +
                    "  END";

    private static final String[] defaultProjectionLocations = new String[]{LOCATION_ID, COLUMN_ADDRESS,
            COLUMN_CITY, COLUMN_COUNTRY, COLUMN_CURRENT_MODE, COLUMN_GEOFENCE_RADIUS, COLUMN_ID, COLUMN_LAT,
            COLUMN_LNG, COLUMN_NAME, COLUMN_OWNER, COLUMN_STATE, COLUMN_ENTRY_TOTAL, COLUMN_ENTRY_ARMED_TOTAL,
            COLUMN_ENTRY_FLAGGED_TOTAL, COLUMN_ZIP, COLUMN_ADDRESS_TWO, COLUMN_AUTO_MODE_ENABLED,
            COLUMN_LAST_MODIFIED, COLUMN_SHARING_DATA, COLUMN_IS_PRIVATE, COLUMN_NIGHT_MODE_ENABLED, COLUMN_CREATED,
            COLUMN_TRIAL_EXPIRED, COLUMN_SHOW_MASK_TUTORIAL};

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/locations";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/location";

    private static final String BASE_PATH = "locationdata";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY_LOCATION + "/" + BASE_PATH);
    private static final int LOCATIONS = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_LOCATION, BASE_PATH, LOCATIONS);
    }

    @Override
    public String getTableName() {
        return TABLE_LOCATIONS;
    }

    @Override
    public String[] getProjectionColumns() {
        return defaultProjectionLocations;
    }

    @Override
    public String getAuthority() {
        return Constants.AUTHORITY_LOCATION;
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
