package is.yranac.canary.contentproviders;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;

import is.yranac.canary.Constants;

public class CanaryDeviceContentProvider extends CanaryContentProvider {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = "CanaryDeviceContentProvider";

    public static final String TABLE_DEVICES = "device_table";

    public static final String DEVICE_ID = "_id"; // Primary Key of local
    // SQLite

    public static final String COLUMN_DEVICE_ID = "serial_number";
    public static final String COLUMN_APPLICATION_VERSION = "application_version";
    public static final String COLUMN_ID = "device_id";
    public static final String COLUMN_LOCATION_ID = "location_id";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_MODE = "mode";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ONLINE = "online";
    public static final String COLUMN_UUID = "device_uuid";
    public static final String COLUMN_DEVICE_TYPE = "device_type";
    public static final String COLUMN_DEVICE_TYPE_NAME = "device_type_name";
    public static final String COLUMN_SIREN_ACTIVE = "siren_active";
    public static final String COLUMN_UPLOADER_ACTIVE = "uploader_active";
    public static final String COLUMN_DEVICE_ACTIVATED = "device_activated";
    public static final String COLUMN_ACTIVATION_STATUS = "activation_status";
    public static final String COLUMN_OTA_STATUS = "ota_status";

    public static final String DATABASE_CREATE_DEVICES = " CREATE TABLE " + TABLE_DEVICES + "("
            + DEVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DEVICE_ID + " TEXT NOT NULL , "
            + COLUMN_LOCATION_ID + " BIGINT "
            + " REFERENCES " + CanaryLocationContentProvider.TABLE_LOCATIONS
            + "(" + CanaryLocationContentProvider.COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_APPLICATION_VERSION + " TEXT, "
            + COLUMN_DEVICE_ACTIVATED + " BOOLEAN, "
            + COLUMN_ACTIVATION_STATUS + " TEXT, "
            + COLUMN_DEVICE_TYPE + " INTEGER, "
            + COLUMN_DEVICE_TYPE_NAME + " TEXT, "
            + COLUMN_ID + " BIGINT UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_IMAGE_URL + " TEXT, "
            + COLUMN_MODE + " INTEGER, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_ONLINE + " BOOLEAN, "
            + COLUMN_UUID + " TEXT NOT NULL UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_SIREN_ACTIVE + " BOOLEAN, "
            + COLUMN_UPLOADER_ACTIVE + " BOOLEAN, "
            + COLUMN_OTA_STATUS + " TEXT "
            + ");";

    private static final String BASE_PATH = "devicedata";
    public static final Uri CONTENT_URI = Uri.parse(
            "content://" + Constants.AUTHORITY_DEVICE + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/devices";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/device";

    public static final String[] defaultProjectionDevices = new String[]{DEVICE_ID, COLUMN_DEVICE_ID, COLUMN_APPLICATION_VERSION,
            COLUMN_ID, COLUMN_LOCATION_ID, COLUMN_IMAGE_URL, COLUMN_MODE, COLUMN_NAME, COLUMN_ACTIVATION_STATUS,
            COLUMN_ONLINE, COLUMN_UUID, COLUMN_SIREN_ACTIVE, COLUMN_DEVICE_ACTIVATED, COLUMN_UPLOADER_ACTIVE,
            COLUMN_OTA_STATUS, COLUMN_DEVICE_TYPE, COLUMN_DEVICE_TYPE_NAME};

    private static final int DEVICES = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_DEVICE, BASE_PATH, DEVICES);
    }

    @Override
    public String getTableName() {
        return TABLE_DEVICES;
    }

    @Override
    public String[] getProjectionColumns() {
        return defaultProjectionDevices;
    }

    @Override
    public String getAuthority() {
        return Constants.AUTHORITY_DEVICE;
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
