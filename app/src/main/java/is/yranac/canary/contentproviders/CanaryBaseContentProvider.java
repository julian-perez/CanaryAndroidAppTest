package is.yranac.canary.contentproviders;

import android.content.ContentProvider;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

import is.yranac.canary.contentproviders.geofence.CacheGeofenceContentProvider;


public abstract class CanaryBaseContentProvider extends ContentProvider {

    private static final String LOG_TAG = "BaseContentProvider";
    // database

    private static final int DATABASE_VERSION = 85;

    private static final String DATABASE_NAME = "canary_base.sqlite";

    /**
     * Database that all cached content goes into
     */

    protected SQLiteDatabase sqlDatabase;

    private SQLiteDatabase.CursorFactory cursorFactory = new CursorFactory() {
        @Override
        public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
            return new SQLiteCursor(masterQuery, editTable, query);
        }
    };

    @Override
    public boolean onCreate() {
        Context context = getContext();
        CanaryDatabaseHelper dbHelper = CanaryDatabaseHelper.getHelper(context, DATABASE_NAME, cursorFactory, DATABASE_VERSION);

        try {
            sqlDatabase = dbHelper.getWritableDatabase();
            if (!sqlDatabase.isReadOnly()) {
                sqlDatabase.setForeignKeyConstraintsEnabled(true);
            }


        } catch (SQLiteException e) {
            sqlDatabase = null;
            Log.d(LOG_TAG, "Database Opening exception");
            e.printStackTrace();
        }
        return sqlDatabase != null;
    }

    abstract protected void checkColumns(String[] projection, int uriType);


    private static class CanaryDatabaseHelper extends SQLiteOpenHelper {


        private static CanaryDatabaseHelper instance;

        private static synchronized CanaryDatabaseHelper getHelper(Context context, String name,
                                                                   CursorFactory factory, int version) {
            if (instance == null)
                instance = new CanaryDatabaseHelper(context, name, factory, version);
            return instance;
        }

        public CanaryDatabaseHelper(Context context, String name,
                                    CursorFactory factory, int version) {
            super(context, name, factory, version);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CanaryLocationContentProvider.DATABASE_CREATE_LOCATIONS);
            db.execSQL(CanaryDeviceContentProvider.DATABASE_CREATE_DEVICES);
            db.execSQL(CanaryCustomerContentProvider.DATABASE_CREATE_CUSTOMERS);
            db.execSQL(CanaryCustomerLocationContentProvider.DATABASE_CREATE_CUSTOMERS_LOCATIONS_LINKS);
            db.execSQL(CanaryAvatarContentProvider.DATABASE_CREATE_AVATARS);
            db.execSQL(CanaryEntryContentProvider.DATABASE_CREATE_ENTRIES);
            db.execSQL(CanaryCommentContentProvider.DATABASE_CREATE_COMMENTS);
            db.execSQL(CanaryInvitationContentProvider.DATABASE_CREATE_INVITATIONS);
            db.execSQL(CanaryModeContentProvider.DATABASE_CREATE_MODES);
            db.execSQL(CanaryThumbnailContentProvider.DATABASE_CREATE_THUMBNAIL);
            db.execSQL(CanaryLabelContentProvider.DATABASE_CREATE_LABELS);
            db.execSQL(CanaryReadingContentProvider.DATABASE_CREATE_READINGS);
            db.execSQL(CanaryEntryCustomerContentProvider.DATABASE_CREATE_ENTRY_CUSTOMERS);
            db.execSQL(CanaryVideoExportContentProvider.DATABASE_CREATE_VIDEO_EXPORT);
            db.execSQL(CanarySubscriptionContentProvider.DATABASE_SUBSCRIPTION);
            db.execSQL(CacheGeofenceContentProvider.DATABASE_CREATE_CAHCE_GEOFENCE);
            db.execSQL(CanaryDeviceSettingsContentProvider.DATABASE_CREATE_DEVICE_SETTINGS);
            db.execSQL(CanaryEmergencyContactsContentProvider.DATABASE_CREATE_EMERGENCY_CONTACTS);
            db.execSQL(CanaryNotifiedContentProvider.DATABASE_CREATE_NOTIFIED);
            db.execSQL(CanaryLocationNetworkContentProvider.DATABASE_CREATE_LOCATION_NETWORKS);
            db.execSQL(CanaryDeviceTokenContentProvider.DATABASE_CREATE_DEVICE_TOKENS);
            db.execSQL(CanaryUserTagsContentProvider.DATABASE_CREATE_USER_TAGS);
            db.execSQL(CanaryFeatureFlagsContentProvider.DATABASE_CREATE_FEATURE_FLAGS);
            db.execSQL(CanaryLocationContentProvider.UPDATE_TIME_TRIGGER);
            db.execSQL(CanaryDeviceSettingsContentProvider.UPDATE_TIME_TRIGGER);
            db.execSQL(CanaryNightModeScheduleContentProvider.DATABASE_CREATE_NIGHT_MODE_SCHEDULE);
            db.execSQL(CanaryInsurancePolicyContentProvider.DATABASE_CREATE_INSURANCE_POLICY);
            db.execSQL(CanaryInsuranceProviderContentProvider.DATABASE_CREATE_INSURANCE_PROVIDER);
            db.execSQL(CanarySubscriptionPricesContentProvider.DATABASE_CREATE_SUBSCRIPTION_PRICES);
            db.execSQL(CanaryMembershipContentProvider.DATABASE_CREATE_MEMBERSHIP);
        }


        private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE + CanaryLocationContentProvider.TABLE_LOCATIONS);
            db.execSQL(DROP_TABLE + CanaryDeviceContentProvider.TABLE_DEVICES);
            db.execSQL(DROP_TABLE + CanaryCustomerContentProvider.TABLE_CUSTOMERS);
            db.execSQL(DROP_TABLE + CanaryCustomerLocationContentProvider.TABLE_CUSTOMER_LOCATION_LINKS);
            db.execSQL(DROP_TABLE + CanaryAvatarContentProvider.TABLE_AVATARS);
            db.execSQL(DROP_TABLE + CanaryEntryContentProvider.TABLE_ENTRIES);
            db.execSQL(DROP_TABLE + CanaryCommentContentProvider.TABLE_COMMENTS);
            db.execSQL(DROP_TABLE + CanaryInvitationContentProvider.TABLE_INVITATIONS);
            db.execSQL(DROP_TABLE + CanaryModeContentProvider.TABLE_MODES);
            db.execSQL(DROP_TABLE + CanaryThumbnailContentProvider.TABLE_THUMBNAIL);
            db.execSQL(DROP_TABLE + CanaryLabelContentProvider.TABLE_LABELS);
            db.execSQL(DROP_TABLE + CanaryReadingContentProvider.TABLE_READINGS);
            db.execSQL(DROP_TABLE + CanaryEntryCustomerContentProvider.TABLE_ENTRY_CUSTOMERS);
            db.execSQL(DROP_TABLE + CanaryVideoExportContentProvider.TABLE_VIDEO_EXPORT);
            db.execSQL(DROP_TABLE + CanaryPlanTypeContentProvider.TABLE_PLAN_TYPES);
            db.execSQL(DROP_TABLE + CanaryServicePlanContentProvider.TABLE_SERVICE_PLAN);
            db.execSQL(DROP_TABLE + CacheGeofenceContentProvider.TABLE_CACHE_GEOFENCE);
            db.execSQL(DROP_TABLE + CanaryDeviceSettingsContentProvider.TABLE_DEVICE_SETTINGS);
            db.execSQL(DROP_TABLE + CanaryEmergencyContactsContentProvider.TABLE_EMERGENCY_CONTACTS);
            db.execSQL(DROP_TABLE + CanaryNotifiedContentProvider.TABLE_NOTIFIED);
            db.execSQL(DROP_TABLE + CanaryLocationNetworkContentProvider.TABLE_LOCATION_NETWORK);
            db.execSQL(DROP_TABLE + CanaryDeviceTokenContentProvider.TABLE_DEVICE_TOKEN_TABLE);
            db.execSQL(DROP_TABLE + CanaryUserTagsContentProvider.TABLE_USER_TAGS);
            db.execSQL(DROP_TABLE + CanaryFeatureFlagsContentProvider.TABLE_FEATURE_FLAGS);
            db.execSQL(DROP_TABLE + CanaryNightModeScheduleContentProvider.TABLE_NIGHT_MODES);
            db.execSQL(DROP_TABLE + CanaryInsurancePolicyContentProvider.TABLE_LOCATION_INSURANCE_POLICY);
            db.execSQL(DROP_TABLE + CanaryInsuranceProviderContentProvider.TABLE_LOCATION_INSURANCE_PROVIDER);
            db.execSQL(DROP_TABLE + CanarySubscriptionContentProvider.TABLE_SUBSCRIPTION);
            db.execSQL(DROP_TABLE + CanarySubscriptionPricesContentProvider.TABLE_SUBSCRIPTION_PRICES);
            db.execSQL(DROP_TABLE + CanaryVZWInfoContentProvider.TABLE_VZW_INFO);
            db.execSQL(DROP_TABLE + CanaryMembershipContentProvider.TABLE_MEMBERSHIP);

            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion,
                                int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

    }

}
