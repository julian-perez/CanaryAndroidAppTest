package is.yranac.canary.services.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.contentproviders.CanaryEntryContentProvider;
import is.yranac.canary.contentproviders.CanaryLocationContentProvider;
import is.yranac.canary.messages.LocationAndEntry;
import is.yranac.canary.model.entry.DisplayMeta;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.entry.EntryDeleted;
import is.yranac.canary.model.entry.Notified;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.mode.Mode;
import is.yranac.canary.model.mode.ModeCache;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.util.JSONUtil;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.model.mode.ModeCache.armed;
import static is.yranac.canary.model.mode.ModeCache.getMode;

/**
 * The type Entry database service.
 */
public class EntryDatabaseService extends BaseDatabaseService {

    /**
     * Insert or update entries.
     *
     * @param entries the entries.  New entries will be inserted, existing entries will be updated.
     * @return the number of records inserted (excludes updates)
     */
    public static int insertOrUpdateEntries(List<Entry> entries, int type) {
        int numInserted = 0;
        for (Entry entry : entries) {
            numInserted += insertEntryAndLinkedObjects(entry, type);
        }
        return numInserted;
    }

    public static void checkAndUpdateEntries(List<Entry> entries, int dailyTimeline) {
        for (Entry entry : entries) {
            ContentValues values = createContentValuesFromEntry(entry, dailyTimeline);
            values.remove(CanaryEntryContentProvider.COLUMN_ENTRY_CALL_TYPE);
            values.remove(CanaryEntryContentProvider.COLUMN_LAST_MODIFIED);
            String where = CanaryEntryContentProvider.COLUMN_ID + " == ?";
            String[] whereArgs = {String.valueOf(entry.id)};
            int updated = contentResolver.update(CanaryEntryContentProvider.CONTENT_URI, values, where, whereArgs);
            if (updated < 1) {
                insertEntryAndLinkedObjects(entry, dailyTimeline);
            } else {
                updateEntryLinkedObjects(entry);
            }
        }
    }

    public static int insertEntryAndLinkedObjects(Entry entry, int type) {
        if (insertOrUpdateEntry(entry, type)) {
            updateEntryLinkedObjects(entry);

            return 1;
        }

        return 0;
    }

    public static boolean insertOrUpdateEntry(Entry entry, int type) {
        if (entry.deleted) {
            TinyMessageBus.post(new EntryDeleted(entry.id));
            deleteEntry(entry.id);
            return false;
        }
        ContentValues values = createContentValuesFromEntry(entry, type);


        return contentResolver.insert(CanaryEntryContentProvider.CONTENT_URI, values) != null;

    }

    public static void setEntryAsFlagged(long entryId, boolean flagged) {

        ContentValues values = new ContentValues();
        values.put(CanaryEntryContentProvider.COLUMN_STARRED, flagged);

        String where = CanaryEntryContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(entryId)};

        contentResolver.update(CanaryEntryContentProvider.CONTENT_URI, values, where, whereArgs);
    }

    public static void updateEntryLinkedObjects(Entry entry) {

        CommentDatabaseService.insertComments(entry.comments);

        LabelDatabaseService.setLabelsForEntryId(entry.id, entry.labels);

        ThumbnailDatabaseService.insertThumbnails(entry.thumbnails);

        EntryCustomersDatabaseService.deleteCustomersForEntry(entry.id);

        EntryCustomersDatabaseService.insertCustomersForEntry(entry.customers, entry.id);

        VideoExportDatabaseService.insertVideoExports(entry.videoExports);

        if (entry.displayMeta != null) {
            Notified notified = entry.displayMeta.notified;

            if (notified != null) {
                NotifiedDatabaseService.insertNotified(notified, entry.id);
            }
        }

    }

    public static Entry getEntryFromEntryId(long entryId) {
        String where = CanaryEntryContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(entryId)};

        Cursor cursor = contentResolver.query(CanaryEntryContentProvider.CONTENT_URI, null, where, whereArgs, null);

        if (cursor == null)
            return null;

        Entry entry = null;
        if (cursor.moveToFirst()) {
            entry = getEntryFromCursor(cursor);
        }
        cursor.close();

        return entry;
    }

    /**
     * Create content values from entry.
     *
     * @param entry the entry
     * @return the content values
     */
    public static ContentValues createContentValuesFromEntry(Entry entry, int type) {

        ContentValues values = new ContentValues();

        values.put(CanaryEntryContentProvider.COLUMN_DURATION, String.valueOf(entry.duration));
        values.put(CanaryEntryContentProvider.COLUMN_END_TIME, entry.endTime == null ? 0 : entry.endTime.getTime());
        values.put(CanaryEntryContentProvider.COLUMN_ENTRY_DESCRIPTION, entry.description == null ? "" : entry.description);
        values.put(CanaryEntryContentProvider.COLUMN_ENTRY_TYPE, entry.entryType == null ? "" : entry.entryType);
        values.put(CanaryEntryContentProvider.COLUMN_ID, entry.id);
        values.put(CanaryEntryContentProvider.COLUMN_LAST_MODIFIED, entry.lastModified == null ? 0 : entry.lastModified.getTime());
        values.put(CanaryEntryContentProvider.COLUMN_LOCATION_ID, Utils.getIntFromResourceUri(entry.locationUri));
        values.put(CanaryEntryContentProvider.COLUMN_STARRED, entry.starred);
        values.put(CanaryEntryContentProvider.COLUMN_START_TIME, entry.startTime == null ? 0 : entry.startTime.getTime());
        values.put(CanaryEntryContentProvider.COLUMN_THUMBNAIL_COUNT, entry.thumbnails == null ? 0 : entry.thumbnails.size());
        values.put(CanaryEntryContentProvider.COLUMN_ENTRY_CALL_TYPE, type);
        values.put(CanaryEntryContentProvider.COLUMN_EXPORTED, entry.exported);
        values.put(CanaryEntryContentProvider.COLUMN_DEVICES, JSONUtil.getJSONString(entry.deviceUuids));

        DisplayMeta displayMeta = entry.displayMeta;
        if (displayMeta != null) {
            values.put(CanaryEntryContentProvider.COLUMN_LOCATION_MODE, displayMeta.locationMode);
            values.put(CanaryEntryContentProvider.COLUMN_LOCATION_IS_PRIVATE, displayMeta.locationIsPrivate);
        }

        Mode deviceMode = entry.deviceMode;

        if (deviceMode != null) {
            values.put(CanaryEntryContentProvider.COLUMN_DEVICE_MODE, deviceMode.resourceUri);
        }

        return values;
    }

    /**
     * Gets entry from cursor.
     *
     * @param cursor the cursor
     * @return the entry from cursor
     */
    public static Entry getEntryFromCursor(Cursor cursor) {
        Entry entry = new Entry();

        entry.id = cursor.getLong(cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_ID));
        entry.duration = cursor.getInt(cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_DURATION));

        long endTimeStamp = cursor.getLong(cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_END_TIME));
        long lastModifiedTimeStamp = cursor.getLong(cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_LAST_MODIFIED));
        long startTimeStamp = cursor.getLong(cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_START_TIME));

        entry.endTime = new Date(endTimeStamp);
        entry.lastModified = new Date(lastModifiedTimeStamp);
        entry.startTime = new Date(startTimeStamp);
        entry.description = cursor.getString(cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_ENTRY_DESCRIPTION));
        entry.entryType = cursor.getString(cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_ENTRY_TYPE));
        int location = cursor.getInt(cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_LOCATION_ID));
        entry.locationUri = Utils.buildResourceUri(Constants.LOCATIONS_URI, location);
        entry.resourceUri = Utils.buildResourceUri(Constants.ENTRIES_URI, entry.id);
        entry.starred = cursor.getInt(cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_STARRED)) > 0;
        entry.thumbnailCount = cursor.getInt(cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_THUMBNAIL_COUNT));
        entry.exported = cursor.getInt(cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_EXPORTED)) > 0;

        String modeUri = cursor.getString(cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_DEVICE_MODE));
        if (modeUri != null)
            entry.deviceMode = ModeDatabaseService.getModeFromResourceUri(modeUri);

        entry.customers = EntryCustomersDatabaseService.getCustomersForEntry(entry.id);

        DisplayMeta displayMeta = new DisplayMeta();
        displayMeta.locationIsPrivate = cursor.getInt(cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_LOCATION_IS_PRIVATE)) > 0;
        displayMeta.locationMode = cursor.getString(cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_LOCATION_MODE));

        entry.displayMeta = displayMeta;
        Type aClass = new TypeToken<List<String>>() {
        }.getType();

        String deviceUuid = cursor.getString(cursor.getColumnIndex(CanaryEntryContentProvider.COLUMN_DEVICES));
        entry.deviceUuids = JSONUtil.getObject(deviceUuid, aClass);

        entry.displayMeta.notified = NotifiedDatabaseService.getNofificationStatus(entry.id);

        return entry;
    }

    public static Date getLastModifiedDate(int locationId) {
        Date lastModified = null;


        String where = CanaryEntryContentProvider.COLUMN_LOCATION_ID
                + " == ? AND "
                + CanaryEntryContentProvider.COLUMN_ENTRY_TYPE + " != ? ";
        String[] whereArgs;
        whereArgs = new String[]{String.valueOf(locationId), String.valueOf(Entry.DAILY_TIMELINE)};


        Cursor cursor = contentResolver.query(CanaryEntryContentProvider.CONTENT_URI, null, where, whereArgs, CanaryEntryContentProvider.COLUMN_LAST_MODIFIED + " DESC");

        if (cursor.moveToFirst()) {
            Entry entry = getEntryFromCursor(cursor);
            lastModified = entry.lastModified;

            long earliestAllowedRecord = SubscriptionPlanDatabaseService.oldestAllowableNonStarredSubscriptionDate(locationId).getTime();

            if (earliestAllowedRecord > lastModified.getTime()) {
                lastModified = null;
            }
        }
        cursor.close();

        return lastModified;
    }

    public static int deleteOldEntries(Date date, int locationId) {
        String where = CanaryEntryContentProvider.COLUMN_START_TIME + " < ?";
        where += " AND " + CanaryEntryContentProvider.COLUMN_STARRED + " == ?";
        where += " AND " + CanaryEntryContentProvider.COLUMN_LOCATION_ID + " == ?";

        String[] whereArgs = {String.valueOf(date.getTime()), "0", String.valueOf(locationId)};
        int deleted = contentResolver.delete(CanaryEntryContentProvider.CONTENT_URI, where, whereArgs);

        return deleted;
    }

    public static int getFlaggedEntryCount() {

        return getFlaggedEntryCount(UserUtils.getLastViewedLocationId());
    }

    public static int getFlaggedEntryCount(int locationId) {
        String where = CanaryEntryContentProvider.COLUMN_LOCATION_ID + " == ?";
        where += "AND ";
        where += CanaryEntryContentProvider.COLUMN_STARRED + " == ?";
        String[] whereArgs = {String.valueOf(locationId), "1"};

        Cursor cursor = contentResolver.query(CanaryEntryContentProvider.CONTENT_URI, null, where, whereArgs, CanaryEntryContentProvider.COLUMN_LAST_MODIFIED + " DESC");
        try {
            return cursor.getCount();
        } finally {
            cursor.close();
        }
    }

    public static int getArmedEntries(int locationId) {
        String where = CanaryEntryContentProvider.COLUMN_LOCATION_ID + " == ?";
        where += "AND ";
        where += CanaryEntryContentProvider.COLUMN_DEVICE_MODE + " == ?";
        where += "AND ";
        where += CanaryEntryContentProvider.COLUMN_ENTRY_CALL_TYPE + " != ?";
        where += "AND ";
        where += CanaryEntryContentProvider.COLUMN_ENTRY_CALL_TYPE + " != ?";
        String mode = ModeCache.getMode(armed).resourceUri;

        String[] whereArgs = {String.valueOf(locationId),
                mode, String.valueOf(Entry.SHOWING_FLAGGED), String.valueOf(Entry.DAILY_TIMELINE)};

        Cursor cursor = contentResolver.query(CanaryEntryContentProvider.CONTENT_URI, null, where, whereArgs, CanaryEntryContentProvider.COLUMN_LAST_MODIFIED + " DESC");
        try {
            return cursor.getCount();
        } finally {
            cursor.close();
        }
    }

    public static boolean allValidRecordsAreInDatabase(int location, int type) {
        int totalCount = 0;

        String where = CanaryLocationContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(location)};

        Cursor cursor = contentResolver.query(
                CanaryLocationContentProvider.CONTENT_URI,
                null,
                where,
                whereArgs,
                null);

        if (cursor.moveToFirst()) {
            switch (type) {
                case Entry.SHOWING_ALL:
                    totalCount = cursor.getInt(
                            cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_ENTRY_TOTAL));
                    break;
                case Entry.SHOWING_AWAY_MODE:
                    totalCount = cursor.getInt(
                            cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_ENTRY_ARMED_TOTAL));
                    break;
                case Entry.SHOWING_FLAGGED:
                    totalCount = cursor.getInt(
                            cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_ENTRY_FLAGGED_TOTAL));
                    break;
                default:
                    return true;

            }
        }
        cursor.close();

        return totalCount > 0;
    }

    public static void allValidEntriesAreInDatabase(int locationId, int type, boolean upToDate) {
        ContentValues values = new ContentValues();

        switch (type) {
            case Entry.SHOWING_ALL:
                values.put(CanaryLocationContentProvider.COLUMN_ENTRY_TOTAL, upToDate);
                break;
            case Entry.SHOWING_AWAY_MODE:
                values.put(CanaryLocationContentProvider.COLUMN_ENTRY_ARMED_TOTAL, upToDate);
                break;
            case Entry.SHOWING_FLAGGED:
                values.put(CanaryLocationContentProvider.COLUMN_ENTRY_FLAGGED_TOTAL, upToDate);
                break;
            default:
                return;

        }

        String where = CanaryLocationContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(locationId)};
        contentResolver.update(
                CanaryLocationContentProvider.CONTENT_URI,
                values,
                where,
                whereArgs);

    }

    public static Thumbnail getMostRecentThumbnailForLocation(String locationUri) {

        if (locationUri == null) {
            return null;
        }

        String where = CanaryEntryContentProvider.COLUMN_LOCATION_ID + " == ? AND " +
                CanaryEntryContentProvider.COLUMN_ENTRY_TYPE + " == ? ";
        String[] whereArgs = new String[]{Utils.getStringFromResourceUri(locationUri), "motion"};

        Cursor cursor = contentResolver.query(CanaryEntryContentProvider.CONTENT_URI, null, where, whereArgs, CanaryEntryContentProvider.COLUMN_LAST_MODIFIED + " DESC");

        try {
            while (cursor.moveToNext()) {
                Entry entry = getEntryFromCursor(cursor);

                if (entry.thumbnailCount > 0) {
                    List<Thumbnail> thumbnails = ThumbnailDatabaseService.getThumbnailsForEntry(entry.id);
                    if (thumbnails != null && !thumbnails.isEmpty()) {

                        return thumbnails.get(0);
                    }
                }
            }

            return null;

        } finally {
            cursor.close();

        }

    }

    public static int getOffsetForType(int type, int location) {


        String where = CanaryEntryContentProvider.COLUMN_LOCATION_ID + " == ?";

        String[] whereArgs;
        switch (type) {
            case Entry.SHOWING_ALL:
                where += " AND ";
                where += CanaryEntryContentProvider.COLUMN_ENTRY_CALL_TYPE + " == ?";
                whereArgs = new String[]{String.valueOf(location), String.valueOf(type)};
                break;
            case Entry.SHOWING_FLAGGED:

                where += " AND ";
                where += CanaryEntryContentProvider.COLUMN_STARRED + " == ?";
                where += " AND ";
                where += CanaryEntryContentProvider.COLUMN_ENTRY_CALL_TYPE + " != ?";
                whereArgs = new String[]{String.valueOf(location), "1", String.valueOf(Entry.DAILY_TIMELINE)};
                break;
            case Entry.SHOWING_AWAY_MODE:
                where += " AND ";
                where += CanaryEntryContentProvider.COLUMN_DEVICE_MODE + " == ?";
                where += " AND ";
                where += CanaryEntryContentProvider.COLUMN_ENTRY_CALL_TYPE + " != ?";
                where += " AND ";
                where += CanaryEntryContentProvider.COLUMN_ENTRY_CALL_TYPE + " != ?";
                String armedMode = getMode(armed).resourceUri;

                whereArgs = new String[]{String.valueOf(location), armedMode, String.valueOf(Entry.SHOWING_FLAGGED), String.valueOf(Entry.DAILY_TIMELINE)};
                break;
            default:
                return 0;

        }
        Cursor cursor = contentResolver.query(CanaryEntryContentProvider.CONTENT_URI, null, where, whereArgs, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    public static void deleteEntry(long id) {
        String where = CanaryEntryContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        contentResolver.delete(CanaryEntryContentProvider.CONTENT_URI, where, whereArgs);

    }

    public static List<Entry> getEntriesBetweenDates(Date startDate, Date endDate, int locationId) {

        List<Entry> entries = new ArrayList<>();


        String where = CanaryEntryContentProvider.COLUMN_LOCATION_ID + " == ?";
        where += " AND ";
        where += CanaryEntryContentProvider.COLUMN_START_TIME + " <= ?";
        where += " AND ";
        where += CanaryEntryContentProvider.COLUMN_START_TIME + " >= ?";
        where += " AND ";
        where += CanaryEntryContentProvider.COLUMN_ENTRY_TYPE + " == ?";

        String startDateString = String.valueOf(startDate.getTime());
        String endDateString = String.valueOf(endDate.getTime());

        String[] whereArgs = new String[]{String.valueOf(locationId), endDateString, startDateString, "motion"};

        String sortOrder = CanaryEntryContentProvider.COLUMN_START_TIME + " DESC";


        Cursor cursor = contentResolver.query(CanaryEntryContentProvider.CONTENT_URI, null, where, whereArgs, sortOrder);

        if (cursor.moveToFirst()) {

            do {
                entries.add(getEntryFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return entries;
    }


    public static List<Entry> getFlaggedEntriesBeforeDate(Date startDate, int locationId) {

        String where = CanaryEntryContentProvider.COLUMN_LOCATION_ID + " == ?";
        where += " AND ";
        where += CanaryEntryContentProvider.COLUMN_START_TIME + " <= ?";
        where += " AND ";
        where += CanaryEntryContentProvider.COLUMN_ENTRY_TYPE + " == ?";
        where += " AND ";
        where += CanaryEntryContentProvider.COLUMN_STARRED + " == ?";

        String startDateString = String.valueOf(startDate.getTime());
        String sortOrder = CanaryEntryContentProvider.COLUMN_START_TIME + " DESC";
        String[] whereArgs = new String[]{String.valueOf(locationId), startDateString, "motion", String.valueOf(1)};
        Cursor cursor = contentResolver.query(CanaryEntryContentProvider.CONTENT_URI, null, where, whereArgs, sortOrder);
        List<Entry> entries = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {

            do {
                entries.add(getEntryFromCursor(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return entries;
    }


    public static List<LocationAndEntry> getLatestEntry() {
        List<LocationAndEntry> entries = new ArrayList<>();

        List<Location> locations = LocationDatabaseService.getLocationList();

        for (Location location : locations) {

            location.customers = CustomerDatabaseService.getCustomersAtLocation(location.id);
            location.devices = DeviceDatabaseService.getActivatedDevicesAtLocation(location.id);
            String where = CanaryEntryContentProvider.COLUMN_LOCATION_ID + " == ?";
            where += " AND ";
            where += CanaryEntryContentProvider.COLUMN_ENTRY_TYPE + " == ?";
            String sortOrder = CanaryEntryContentProvider.COLUMN_START_TIME + " DESC";

            String[] whereArgs = new String[]{String.valueOf(location.id), "motion"};
            Cursor cursor = contentResolver.query(CanaryEntryContentProvider.CONTENT_URI, null, where, whereArgs, sortOrder);

            LocationAndEntry locationWithEntry = new LocationAndEntry();
            locationWithEntry.location = location;

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    Entry entry = getEntryFromCursor(cursor);
                    entry.thumbnails = ThumbnailDatabaseService.getThumbnailsForEntry(entry.id);
                    entry.comments = CommentDatabaseService.getCommentsForEntry(entry.id);
                    entry.videoExports = VideoExportDatabaseService.getVideoExportsByEntry(entry.id);
                    entry.labels = LabelDatabaseService.getLabelsForEntryId(entry.id);
                    locationWithEntry.entry = entry;
                }
                cursor.close();
            }

            locationWithEntry.subscription = SubscriptionPlanDatabaseService.getServicePlanForLocation(location.id);
            entries.add(locationWithEntry);


        }

        return entries;
    }
}
