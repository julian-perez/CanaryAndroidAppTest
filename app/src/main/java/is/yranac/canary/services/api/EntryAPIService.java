package is.yranac.canary.services.api;

import java.util.Date;

import is.yranac.canary.Constants;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.entry.EntryDeletePatch;
import is.yranac.canary.model.entry.EntryFlaggedPatch;
import is.yranac.canary.model.entry.EntryResponse;
import is.yranac.canary.model.label.LabelsPatch;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.model.videoexport.VideoExportResponse;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import is.yranac.canary.util.DateUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.Path;
import retrofit.http.Query;

public class EntryAPIService {

    // ******************************************
    // *** Entry Records
    // ******************************************
    // Synchronous call for Entry Records
    public static EntryResponse getEntryRecords(int locationId, int offset, int limit) throws RetrofitError {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        EntryService entryService = restAdapter.create(EntryService.class);

        return entryService.getEntries(locationId, offset, limit);
    }

    public static EntryResponse getFlaggedEntryRecords(int locationId, int offset, int limit) throws RetrofitError {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        EntryService entryService = restAdapter.create(EntryService.class);

        return entryService.getFlaggedEntries(locationId, offset, true, limit);
    }

    public static EntryResponse getFlaggedEntryRecordsBeforeDate(int locationId, Date beforeDate) throws RetrofitError {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        EntryService entryService = restAdapter.create(EntryService.class);

        return entryService.getFlaggedEntriesBeforeDate(locationId, DateUtil.convertDateToApiString(beforeDate), true, 0);
    }

    // Asynchronous call to get a entry by id
    public static void getEntryById(long entryId, Callback<Entry> callback) {
        if (entryId == 0) {
            callback.failure(null);
        } else {
            String entryUri = Constants.ENTRIES_URI + String.valueOf(entryId) + "/";
            RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
            EntryService entryService = restAdapter.create(EntryService.class);
            entryService.getEntryByUri(entryUri.substring(1), callback);
        }
    }


    public static Entry getEntryById(long entryId) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true, false, true);
        EntryService entryService = restAdapter.create(EntryService.class);
        return entryService.getEntryById(entryId);
    }


    public static EntryResponse getEntryRecordsByMode(int locationId, int offset, int limit, int mode) throws RetrofitError {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        EntryService entryService = restAdapter.create(EntryService.class);

        return entryService.getEntriesByMode(locationId, offset, limit, mode);
    }

    public static void patchEntryLabels(String labels, String entryId, Callback<Void> callback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        EntryService entryService = restAdapter.create(EntryService.class);
        LabelsPatch labelsPatch = new LabelsPatch(labels);

        entryService.setLabels(entryId, labelsPatch, callback);
    }

    // Asynchronous call to set/unset the flag on a specified Inbox Record
    public static void setEntryRecordFlag(long id, boolean isFlagged, Callback<Void> callback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        EntryService entryService = restAdapter.create(EntryService.class);

        entryService.setEntryFlaggedAsync(String.valueOf(id), new EntryFlaggedPatch(isFlagged), callback);
    }

    public static EntryResponse getEntriesSinceModifiedDate(int locationId, int offset, int limit, Date date) throws RetrofitError {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        EntryService entryService = restAdapter.create(EntryService.class);
        return entryService.getEntriesSinceModifiedDate(locationId, offset, "True", limit, DateUtil.convertDateToApiString(date));
    }

    public static void deleteEntry(String resourceUri, Callback<Void> callback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        EntryService entryService = restAdapter.create(EntryService.class);
        entryService.deleteEntry(resourceUri.substring(1), new EntryDeletePatch(), callback);
    }

    public static void getVideoExports(long entryId, boolean share, Callback<VideoExportResponse> callback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        EntryService entryService = restAdapter.create(EntryService.class);
        entryService.getExportVideos(entryId, share ? "share" : "download", callback);
    }

    public static EntryResponse getEntriesBetweenDates(Date startDate, Date endDate, int lastViewedLocation) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        EntryService entryService = restAdapter.create(EntryService.class);
        return entryService.getEntriesBetweenDates(DateUtil.convertDateToApiString(startDate), DateUtil.convertDateToApiString(endDate), lastViewedLocation, 0);
    }

    public static void getNewThumbnail(long thumbnailId, Callback<Thumbnail> callback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        EntryService entryService = restAdapter.create(EntryService.class);
        entryService.getThumbnail(thumbnailId, callback);
    }

    public static EntryResponse getResourceByUri(String resourceUri) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        EntryService entryService = restAdapter.create(EntryService.class);
        return entryService.getEntries(resourceUri.substring(1));

    }

    public interface EntryService {
        // Get limit entries from offset since the specified time

        @GET("/{resource_uri}")
        EntryResponse getEntries(
                @Path(value = "resource_uri", encode = false)
                        String resourceUri);


        @GET(Constants.ENTRIES_URI)
        EntryResponse getEntries(
                @Query("location") int locationId,
                @Query("offset") int offset,
                @Query("limit") int limit);

        @GET(Constants.ENTRIES_URI)
        EntryResponse getEntriesByMode(
                @Query("location") int locationId,
                @Query("offset") int offset,
                @Query("limit") int limit,
                @Query("device_mode") int mode);

        // Get all flagged entries (there is no subscription time constraint for flagged)
        @GET(Constants.ENTRIES_URI)
        EntryResponse getFlaggedEntries(
                @Query("location") int locationId,
                @Query("offset") int offset,
                @Query("starred") boolean starred,
                @Query("limit") int limit);

        // Get all entries modified since a specific date
        @GET(Constants.ENTRIES_URI)
        EntryResponse getEntriesSinceModifiedDate(
                @Query("location") int locationId,
                @Query("offset") int offset,
                @Query("include_deleted") String include,
                @Query("limit") int limit,
                @Query("last_modified__gte") String date);

        @GET("/{entry_uri}")
        void getEntryByUri(
                @Path("entry_uri") String entry_uri,
                Callback<Entry> callback);

        @GET(Constants.ENTRIES_URI + "{id}/")
        Entry getEntryById(
                @Path("id") long entryId);

        @PATCH(Constants.ENTRIES_URI + "{id}/")
        void setEntryFlaggedAsync(
                @Path("id") String id,
                @Body EntryFlaggedPatch entryFlaggedPatch,
                Callback<Void> callback);

        @PATCH(Constants.ENTRIES_URI + "{id}/")
        void setLabels(
                @Path("id") String id,
                @Body LabelsPatch labelsPatch,
                Callback<Void> callback);

        @PATCH("/{entry_uri}")
        void deleteEntry(
                @Path("entry_uri") String entry_uri,
                @Body EntryDeletePatch entryDeletePatch,
                Callback<Void> callback);

        @GET(Constants.EXPORT)
        void getExportVideos(
                @Query("entry") long entryId,
                @Query("export_type") String type,
                Callback<VideoExportResponse> callback
        );

        @GET(Constants.ENTRIES_URI)
        EntryResponse getEntriesBetweenDates(
                @Query("start_time__gte") String startDate,
                @Query("start_time__lte") String endDate,
                @Query("location") int locationId,
                @Query("limit") int limit);

        @GET(Constants.ENTRIES_URI)
        EntryResponse getFlaggedEntriesBeforeDate(
                @Query("location") int locationId,
                @Query("start_time__lte") String startDate,
                @Query("starred") boolean starred,
                @Query("limit") int limit);

        @GET(Constants.THUMBNAIL_URI + "{id}/")
        void getThumbnail(
                @Path("id") long id,
                Callback<Thumbnail> callback);
    }

}
