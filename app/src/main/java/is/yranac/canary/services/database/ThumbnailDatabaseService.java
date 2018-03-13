package is.yranac.canary.services.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.Constants;
import is.yranac.canary.contentproviders.CanaryThumbnailContentProvider;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 8/15/14.
 */
public class ThumbnailDatabaseService extends BaseDatabaseService {

    public static void insertThumbnails(List<Thumbnail> thumbnails) {
        if (thumbnails == null)
            return;

        for (Thumbnail thumbnail : thumbnails) {
            insertThumbnail(thumbnail);
        }
    }

    public static void insertThumbnail(Thumbnail thumbnail) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CanaryThumbnailContentProvider.COLUMN_THUMBNAIL_ID, Utils.getLongFromResourceUri(thumbnail.resourceUri));
        contentValues.put(CanaryThumbnailContentProvider.COLUMN_DEVICE_UUID, Utils.getStringFromResourceUri(thumbnail.device));
        contentValues.put(CanaryThumbnailContentProvider.COLUMN_ENTRY_ID, Utils.getLongFromResourceUri(thumbnail.entry));
        contentValues.put(CanaryThumbnailContentProvider.COLUMN_IMAGE_URL, thumbnail.imageUrl);
        Device device = DeviceDatabaseService.getDeviceFromResourceUri(thumbnail.device);
        if (device != null) {
            contentValues.put(CanaryThumbnailContentProvider.COLUMN_DEVICE_ID, device.id);
        } else {
            contentValues.put(CanaryThumbnailContentProvider.COLUMN_DEVICE_ID, Long.MAX_VALUE);

        }
        contentResolver.insert(CanaryThumbnailContentProvider.CONTENT_URI, contentValues);
    }

    public static void deleteThumbnail(long entryId) {
        String where = CanaryThumbnailContentProvider.COLUMN_ENTRY_ID + " == ?";
        String[] whereArgs = {String.valueOf(entryId)};
        ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();

        contentResolver.delete(CanaryThumbnailContentProvider.CONTENT_URI, where, whereArgs);
    }

    public static List<Thumbnail> getThumbnailsForEntry(long entryId) {
        List<Thumbnail> thumbnails = new ArrayList<>();

        String where = CanaryThumbnailContentProvider.COLUMN_ENTRY_ID + " == ?";
        String whereArgs[] = {String.valueOf(entryId)};

        ContentResolver contentResolver = CanaryApplication.getContext()
                .getContentResolver();

        String sortOrder = CanaryThumbnailContentProvider.COLUMN_DEVICE_ID + " ASC";

        Cursor cursor = contentResolver.query(CanaryThumbnailContentProvider.CONTENT_URI, null, where, whereArgs, sortOrder);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                thumbnails.add(getThumbnailFromCursor(cursor));
            }
            cursor.close();
        }

        return thumbnails;
    }

    public static Thumbnail getThumbnailFromCursor(Cursor cursor) {
        Thumbnail thumbnail = new Thumbnail();

        String deviceUUID = cursor.getString(cursor.getColumnIndex(CanaryThumbnailContentProvider.COLUMN_DEVICE_UUID));
        thumbnail.device = Utils.buildResourceUri(Constants.DEVICE_URI, deviceUUID);
        thumbnail.imageUrl = cursor.getString(cursor.getColumnIndex(CanaryThumbnailContentProvider.COLUMN_IMAGE_URL));
        thumbnail.deviceId = cursor.getInt(cursor.getColumnIndex(CanaryThumbnailContentProvider.COLUMN_DEVICE_ID));
        thumbnail.id = cursor.getLong(cursor.getColumnIndex(CanaryThumbnailContentProvider.COLUMN_THUMBNAIL_ID));

        long entryId = cursor.getLong(cursor.getColumnIndex(CanaryThumbnailContentProvider.COLUMN_ENTRY_ID));
        thumbnail.entry = Utils.buildResourceUri(Constants.ENTRIES_URI, entryId);

        return thumbnail;
    }

    public static Thumbnail getThumbnailsForDevice(Device device) {

        ContentResolver contentResolver = CanaryApplication.getContext()
                .getContentResolver();

        String where = CanaryThumbnailContentProvider.COLUMN_DEVICE_UUID + " == ?";
        String whereArgs[] = {String.valueOf(device.uuid)};
        String sortBy = CanaryThumbnailContentProvider.COLUMN_ENTRY_ID + " DESC";
        Cursor cursor = contentResolver.query(CanaryThumbnailContentProvider.CONTENT_URI, null, where, whereArgs, sortBy);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Thumbnail thumbnail = getThumbnailFromCursor(cursor);
                    Entry entry = EntryDatabaseService.getEntryFromEntryId(thumbnail.getEntryId());
                    if (entry != null) {
                        long elapseStartTime = new Date().getTime() - entry.startTime.getTime();

                        if (TimeUnit.MILLISECONDS.toDays(elapseStartTime) < 30) {
                            return thumbnail;
                        }
                    }
                } while (cursor.moveToNext());

            }
            return null;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void updateThumbnailUrl(Thumbnail thumbnail) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CanaryThumbnailContentProvider.COLUMN_IMAGE_URL, thumbnail.imageUrl);
        String where = CanaryThumbnailContentProvider.COLUMN_THUMBNAIL_ID + " == ?";
        String whereArgs[] = {String.valueOf(thumbnail.id)};
        contentResolver.update(CanaryThumbnailContentProvider.CONTENT_URI, contentValues, where, whereArgs);

    }
}
