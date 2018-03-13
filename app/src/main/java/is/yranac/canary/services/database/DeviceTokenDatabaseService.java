package is.yranac.canary.services.database;

import android.content.ContentValues;
import android.database.Cursor;

import is.yranac.canary.BuildConfig;
import is.yranac.canary.contentproviders.CanaryDeviceTokenContentProvider;
import is.yranac.canary.model.devicetoken.DeviceToken;

/**
 * Created by Schroeder on 1/12/16.
 */
public class DeviceTokenDatabaseService  extends BaseDatabaseService{


    public static void deleteTokens() {
        contentResolver.delete(CanaryDeviceTokenContentProvider.CONTENT_URI, null, null);
    }

    public static DeviceToken getCurrentToken() {
        DeviceToken token = null;
        Cursor cursor = contentResolver.query(CanaryDeviceTokenContentProvider.CONTENT_URI,null, null, null, null );
        if (cursor == null)
            return null;

        if (cursor.moveToFirst())
            token = getDeviceTokenFromCursor(cursor);

        cursor.close();

        return token;
    }

    private static DeviceToken getDeviceTokenFromCursor(Cursor cursor) {
        DeviceToken deviceToken = new DeviceToken();
        deviceToken.buildNumber = cursor.getInt(cursor.getColumnIndex(CanaryDeviceTokenContentProvider.COLUMN_BUILD_NUMBER));
        deviceToken.token = cursor.getString(cursor.getColumnIndex(CanaryDeviceTokenContentProvider.COLUMN_TOKEN));
        return deviceToken;
    }

    public static void saveToken(String pushToken) {
        deleteTokens();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CanaryDeviceTokenContentProvider.COLUMN_BUILD_NUMBER, BuildConfig.VERSION_CODE);
        contentValues.put(CanaryDeviceTokenContentProvider.COLUMN_TOKEN, pushToken);
        contentResolver.insert(CanaryDeviceTokenContentProvider.CONTENT_URI, contentValues);
    }
}
