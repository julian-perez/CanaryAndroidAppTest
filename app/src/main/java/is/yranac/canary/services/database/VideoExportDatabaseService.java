package is.yranac.canary.services.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.contentproviders.CanaryVideoExportContentProvider;
import is.yranac.canary.model.videoexport.VideoExport;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 4/29/15.
 */
public class VideoExportDatabaseService extends BaseDatabaseService {

    private static final String LOG_TAG = "VideoExportDatabaseService";

    public static VideoExport getVideoExportFromCursor(Cursor cursor) {
        VideoExport videoExport = new VideoExport();
        videoExport.deviceUUID = cursor.getString(cursor.getColumnIndex(CanaryVideoExportContentProvider.COLUMN_DEVICE_UUID));
        videoExport.entry = Utils.buildResourceUri(Constants.ENTRIES_URI, cursor.getInt(cursor.getColumnIndex(CanaryVideoExportContentProvider.COLUMN_ENTRY_ID)));
        videoExport.processing = cursor.getInt(cursor.getColumnIndex(CanaryVideoExportContentProvider.COLUMN_PROCESSING)) > 0;
        videoExport.duration = cursor.getInt(cursor.getColumnIndex(CanaryVideoExportContentProvider.COLUMN_VIDEO_LENGTH));
        videoExport.size = cursor.getInt(cursor.getColumnIndex(CanaryVideoExportContentProvider.COLUMN_VIDEO_SIZE));
        videoExport.requestedAt = new Date(cursor.getLong(cursor.getColumnIndex(CanaryVideoExportContentProvider.COLUMN_REQUESTED_AT)));
        return videoExport;
    }


    public static void insertVideoExports(List<VideoExport> videoExports) {
        if (videoExports == null)
            return;

        for (VideoExport videoExport : videoExports) {
            insertVideoExport(videoExport);
        }
    }


    public static void insertVideoExport(VideoExport videoExport) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(CanaryVideoExportContentProvider.COLUMN_DEVICE_UUID, videoExport.deviceUUID);
        contentValues.put(CanaryVideoExportContentProvider.COLUMN_ENTRY_ID, Utils.getLongFromResourceUri(videoExport.entry));
        contentValues.put(CanaryVideoExportContentProvider.COLUMN_PROCESSING, videoExport.processing);
        contentValues.put(CanaryVideoExportContentProvider.COLUMN_VIDEO_LENGTH, videoExport.duration);
        contentValues.put(CanaryVideoExportContentProvider.COLUMN_VIDEO_SIZE, videoExport.size);
        long requestedAt = videoExport.requestedAt == null ? 0 : videoExport.requestedAt.getTime();
        contentValues.put(CanaryVideoExportContentProvider.COLUMN_REQUESTED_AT, requestedAt);


        String where = CanaryVideoExportContentProvider.COLUMN_ENTRY_ID + " == ?";
        where += " AND ";
        where += CanaryVideoExportContentProvider.COLUMN_DEVICE_UUID + " == ?";
        String[] whereArgs = {String.valueOf(Utils.getLongFromResourceUri(videoExport.entry)), videoExport.deviceUUID};

        Cursor cursor = contentResolver.query(CanaryVideoExportContentProvider.CONTENT_URI, null, where, whereArgs, null);

        if (cursor.moveToFirst()) {
            contentResolver.update(CanaryVideoExportContentProvider.CONTENT_URI, contentValues, where, whereArgs);
        } else {

            contentResolver.insert(CanaryVideoExportContentProvider.CONTENT_URI, contentValues);
        }
        cursor.close();

    }




    public static List<VideoExport> getVideoExportsByEntry(long entryId) {
        List<VideoExport> videoExports = new ArrayList<>();

        String where = CanaryVideoExportContentProvider.COLUMN_ENTRY_ID + " == ?";
        String[] whereArgs = {String.valueOf(entryId)};
        Cursor cursor = contentResolver.query(CanaryVideoExportContentProvider.CONTENT_URI, null, where, whereArgs, null);

        if (cursor != null){
            while (cursor.moveToNext()) {
                videoExports.add(getVideoExportFromCursor(cursor));
            }
            cursor.close();
        }


        return videoExports;

    }
}
