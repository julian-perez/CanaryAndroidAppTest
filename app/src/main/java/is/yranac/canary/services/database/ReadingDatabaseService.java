package is.yranac.canary.services.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.contentproviders.CanaryEntryContentProvider;
import is.yranac.canary.contentproviders.CanaryReadingContentProvider;
import is.yranac.canary.model.SensorType;
import is.yranac.canary.model.reading.Reading;
import is.yranac.canary.util.Utils;

public class ReadingDatabaseService extends BaseDatabaseService {

    private static final String LOG_TAG = "ReadingDatabaseService";

    public static int insertReadings(List<Reading> readingList) {

        List<ContentValues> contentValuesList = new ArrayList<>();
        for (Reading reading : readingList) {
            ContentValues contentValues = createContentValuesFromReading(reading);
            contentValuesList.add(contentValues);
        }
        return contentResolver.bulkInsert(CanaryReadingContentProvider.CONTENT_URI, contentValuesList.toArray(new ContentValues[contentValuesList.size()]));

    }

    public static ContentValues createContentValuesFromReading(Reading reading) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CanaryReadingContentProvider.COLUMN_SENSOR_ID, reading.sensorType.id);
        contentValues.put(CanaryReadingContentProvider.COLUMN_DEVICE_UUID, Utils.getStringFromResourceUri(reading.deviceUri));
        contentValues.put(CanaryReadingContentProvider.COLUMN_VALUE, reading.value);
        contentValues.put(CanaryReadingContentProvider.COLUMN_CREATED, reading.created.getTime());
        contentValues.put(CanaryReadingContentProvider.COLUMN_STATUS, reading.status);
        return contentValues;
    }

    public static Reading getFirstReadingByDevice(String deviceResourceUri) {
        String where = CanaryReadingContentProvider.COLUMN_DEVICE_UUID + " == ?";
        String[] whereArgs = {Utils.getStringFromResourceUri(deviceResourceUri)};
        Cursor cursor = contentResolver.query(CanaryReadingContentProvider.CONTENT_URI, null, where, whereArgs, CanaryReadingContentProvider.COLUMN_CREATED + " ASC");
        try {
            if (cursor.moveToFirst()) {
                return cursorToReading(cursor);
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    public static Reading cursorToReading(Cursor cursor) {
        Reading reading = new Reading();
        reading.sensorType = new SensorType();
        reading.sensorType.id = cursor.getInt(cursor.getColumnIndex(CanaryReadingContentProvider.COLUMN_SENSOR_ID));
        String deviceUUID = cursor.getString(cursor.getColumnIndex(CanaryReadingContentProvider.COLUMN_DEVICE_UUID));
        reading.deviceUri = Utils.buildResourceUri(Constants.READING_URI, deviceUUID);

        long createdTimestamp = cursor.getLong(cursor.getColumnIndex(CanaryReadingContentProvider.COLUMN_CREATED));

        reading.created = new Date(createdTimestamp);
        reading.value = cursor.getFloat(cursor.getColumnIndex(CanaryReadingContentProvider.COLUMN_VALUE));
        reading.status = cursor.getString(cursor.getColumnIndex(CanaryReadingContentProvider.COLUMN_STATUS));
        return reading;
    }

    public static int deleteOldReadings(long timestamp) {
        String where = CanaryReadingContentProvider.COLUMN_CREATED + " < ?";

        String[] whereArgs = {String.valueOf(timestamp)};
        return contentResolver.delete(CanaryReadingContentProvider.CONTENT_URI, where, whereArgs);
    }

    public static Reading getLatestReadingForDevice(String resourceUri, int type) {

        String where = CanaryReadingContentProvider.COLUMN_DEVICE_UUID + " == ?";
        where += " AND ";
        where += CanaryReadingContentProvider.COLUMN_SENSOR_ID + " == ?";
        String[] whereArgs = {Utils.getStringFromResourceUri(resourceUri), String.valueOf(type)};
        Cursor cursor = contentResolver.query(CanaryReadingContentProvider.CONTENT_URI, null, where, whereArgs, CanaryReadingContentProvider.COLUMN_CREATED + " DESC");

        try {

            if (cursor.moveToFirst()) {
                return cursorToReading(cursor);
            }
            return null;


        } finally {
            cursor.close();

        }
    }

    public static Reading getLatestReadingForDevice(String resourceUri) {

        String where = CanaryReadingContentProvider.COLUMN_DEVICE_UUID + " == ?";
        where += " AND ";
        where += CanaryReadingContentProvider.COLUMN_SENSOR_ID + " != ? ";
        where += " AND ";
        where += CanaryReadingContentProvider.COLUMN_SENSOR_ID + " != ? ";
        String[] whereArgs = {Utils.getStringFromResourceUri(resourceUri), "7", "8"};

        Cursor cursor = contentResolver.query(CanaryReadingContentProvider.CONTENT_URI, null, where, whereArgs, CanaryReadingContentProvider.COLUMN_CREATED + " DESC");

        try {

            if (cursor.moveToFirst()) {
                return cursorToReading(cursor);
            }
            return null;


        } finally {
            cursor.close();

        }
    }


    public static void deleteReadings() {
        contentResolver.delete(CanaryReadingContentProvider.CONTENT_URI, null, null);
    }
}
