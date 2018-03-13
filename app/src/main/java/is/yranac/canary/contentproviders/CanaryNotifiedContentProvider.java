package is.yranac.canary.contentproviders;

import android.content.UriMatcher;
import android.net.Uri;

import is.yranac.canary.Constants;

/**
 * Created by Schroeder on 10/6/15.
 */
public class CanaryNotifiedContentProvider extends CanaryContentProvider {
    public static final String TABLE_NOTIFIED = "notified_table";

    public static final String NOTIFIED_ID = "_id";
    public static final String COLUMN_ENTRY_ID = "entry_id";
    public static final String COLUMN_DETECTION_THRESHOLD = "detection_threshold";
    public static final String COLUMN_NON_BACKGROUND_SCORE = "non_background_score";
    public static final String COLUMN_EVENT_CREATED = "event_created";
    public static final String COLUMN_EVENT_ID = "event_id";
    public static final String COLUMN_DEVICE = "device";

    public static final String DATABASE_CREATE_NOTIFIED = " CREATE TABLE " + TABLE_NOTIFIED + " ("
            + NOTIFIED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ENTRY_ID + " BIGINT UNIQUE"
            + " REFERENCES " + CanaryEntryContentProvider.TABLE_ENTRIES
            + " (" + CanaryEntryContentProvider.COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_DETECTION_THRESHOLD + " DOUBLE, "
            + COLUMN_NON_BACKGROUND_SCORE + " DOUBLE, "
            + COLUMN_EVENT_CREATED + " LONG,"
            + COLUMN_DEVICE + " TEXT,"
            + COLUMN_EVENT_ID + " BIGINT UNIQUE ON CONFLICT REPLACE"
            + ");";

    private static final String BASE_PATH = "notifieddata";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + Constants.AUTHORITY_NOTIFIED + "/" + BASE_PATH);


    private static final String[] defaultProjectionAvatars = new String[]{
            COLUMN_DETECTION_THRESHOLD,
            COLUMN_NON_BACKGROUND_SCORE,
            COLUMN_EVENT_CREATED,
            COLUMN_EVENT_ID,
            COLUMN_DEVICE
    };

    private static final int NOTIFIED = 10;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Constants.AUTHORITY_NOTIFIED, BASE_PATH, NOTIFIED);
    }

    @Override
    public String getTableName() {
        return TABLE_NOTIFIED;
    }

    @Override
    public String[] getProjectionColumns() {
        return defaultProjectionAvatars;
    }

    @Override
    public String getAuthority() {
        return Constants.AUTHORITY_NOTIFIED;
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
