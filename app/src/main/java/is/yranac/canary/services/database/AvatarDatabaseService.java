package is.yranac.canary.services.database;

import android.content.ContentValues;
import android.database.Cursor;

import is.yranac.canary.Constants;
import is.yranac.canary.contentproviders.CanaryAvatarContentProvider;
import is.yranac.canary.model.avatar.Avatar;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 8/8/14.
 */
public class AvatarDatabaseService extends BaseDatabaseService{
    private final static String LOG_TAG = "AvatarDatabaseService";

    public static void insertAvatar(Avatar avatar) {

        ContentValues values = createContentValuesFromAvatar(avatar);
        contentResolver.insert(CanaryAvatarContentProvider.CONTENT_URI,
                values);

    }

    public static ContentValues createContentValuesFromAvatar(Avatar avatar) {
        int id = avatar.id;
        int customerId = Utils.getIntFromResourceUri(avatar.customer);
        String image = avatar.image;
        String thumbnailUrl = avatar.thumbnailUrl;
        ContentValues values = new ContentValues();
        values.put(CanaryAvatarContentProvider.COLUMN_ID, id);
        values.put(CanaryAvatarContentProvider.COLUMN_IMAGE, image);
        values.put(CanaryAvatarContentProvider.COLUMN_THUMBNAIL_URL,
                thumbnailUrl);
        values.put(CanaryAvatarContentProvider.COLUMN_CUSTOMER_ID,
                customerId);
        return values;
    }

    // *****************************************************
    // *** Get Avatar from CustomerId
    // *****************************************************
    public static Avatar getAvatarFromCustomerId(int customerId) {
        Avatar avatar = null;

        String where = CanaryAvatarContentProvider.COLUMN_CUSTOMER_ID + " == ?";
        String whereArgs[] = {String.valueOf(customerId)};

        Cursor cursor = contentResolver.query(CanaryAvatarContentProvider.CONTENT_URI,
                null, where, whereArgs, null);

        if (cursor.moveToFirst()) {
            avatar = getAvatarFromCursor(cursor);
        }

        cursor.close();
        return avatar;
    }

    // *****************************************************
    // *** Get Avatar from Cursor
    // *****************************************************
    public static Avatar getAvatarFromCursor(Cursor cursor) {
        Avatar avatar = new Avatar();
        avatar.id = cursor.getInt(cursor.getColumnIndex(CanaryAvatarContentProvider.COLUMN_ID));
        avatar.image = cursor.getString(cursor.getColumnIndex(CanaryAvatarContentProvider.COLUMN_IMAGE));
        avatar.resourceUri = Utils.buildResourceUri(Constants.AVATAR_URI, avatar.id);
        avatar.thumbnailUrl = cursor.getString(cursor.getColumnIndex(CanaryAvatarContentProvider.COLUMN_THUMBNAIL_URL));

        return avatar;
    }

    public static void deleteAvatar(int customerId) {

        String where = CanaryAvatarContentProvider.COLUMN_CUSTOMER_ID + " == ?";
        String whereArgs[] = {String.valueOf(customerId)};
        contentResolver.delete(CanaryAvatarContentProvider.CONTENT_URI, where, whereArgs);
    }
}
