package is.yranac.canary.services.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import is.yranac.canary.contentproviders.CanaryNotifiedContentProvider;
import is.yranac.canary.model.entry.Notified;

/**
 * Created by Schroeder on 10/6/15.
 */
public class NotifiedDatabaseService extends BaseDatabaseService {

    public static void insertNotified(Notified notified, long entryId) {
        contentResolver.insert(CanaryNotifiedContentProvider.CONTENT_URI, contentValuesFromNotified(notified, entryId));
    }

    public static ContentValues contentValuesFromNotified(Notified notified, long entryId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CanaryNotifiedContentProvider.COLUMN_DETECTION_THRESHOLD, notified.detectionThreshold);
        contentValues.put(CanaryNotifiedContentProvider.COLUMN_ENTRY_ID, entryId);
        contentValues.put(CanaryNotifiedContentProvider.COLUMN_EVENT_ID, notified.eventId);
        contentValues.put(CanaryNotifiedContentProvider.COLUMN_DEVICE, notified.deviceUUID);
        contentValues.put(CanaryNotifiedContentProvider.COLUMN_NON_BACKGROUND_SCORE, notified.nonBackgroundScore);
        if (notified.eventCreated != null)
            contentValues.put(CanaryNotifiedContentProvider.COLUMN_EVENT_CREATED, notified.eventCreated.getTime());
        return contentValues;
    }


    public static Notified notifiedFromCursor(Cursor cursor) {
        Notified notified = new Notified();
        notified.detectionThreshold = cursor.getDouble(cursor.getColumnIndex(CanaryNotifiedContentProvider.COLUMN_DETECTION_THRESHOLD));
        notified.eventCreated = new Date(cursor.getLong(cursor.getColumnIndex(CanaryNotifiedContentProvider.COLUMN_EVENT_CREATED)));
        notified.eventId = cursor.getLong(cursor.getColumnIndex(CanaryNotifiedContentProvider.COLUMN_EVENT_ID));
        notified.nonBackgroundScore = cursor.getDouble(cursor.getColumnIndex(CanaryNotifiedContentProvider.COLUMN_NON_BACKGROUND_SCORE));
        notified.deviceUUID = cursor.getString(cursor.getColumnIndex(CanaryNotifiedContentProvider.COLUMN_DEVICE));
        return notified;
    }

    public static Notified getNofificationStatus(long id) {

        String where = CanaryNotifiedContentProvider.COLUMN_ENTRY_ID + " == ?";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = contentResolver.query(CanaryNotifiedContentProvider.CONTENT_URI, null, where, whereArgs, null);
        if (cursor == null)
            return null;
        try {

            if (cursor.moveToFirst()) {
                return notifiedFromCursor(cursor);
            }

        } finally {
            cursor.close();
        }
        return null;
    }
}
