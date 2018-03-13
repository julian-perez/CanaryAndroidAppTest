package is.yranac.canary.services.jobs;

import android.app.IntentService;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.JobParameters;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import is.yranac.canary.messages.EntryTableUpdated;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.entry.EntryResponse;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.mode.Mode;
import is.yranac.canary.model.mode.ModeCache;
import is.yranac.canary.model.mode.ModeResponse;
import is.yranac.canary.services.api.EntryAPIService;
import is.yranac.canary.services.api.ModeAPIService;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.database.ModeDatabaseService;
import is.yranac.canary.services.database.SubscriptionPlanDatabaseService;
import is.yranac.canary.util.MyLifecycleHandler;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.keystore.KeyStoreHelper;
import retrofit.RetrofitError;

import static is.yranac.canary.model.mode.ModeCache.armed;
import static is.yranac.canary.services.jobs.APIEntryJobService.Status.INSTANCE;

/**
 * An {@link IntentService} subclass for handling asynchronous entry requests with the canary server
 * <p/>
 */
public class APIEntryJobService extends BaseJobService {
    private static final String TAG = "APIEntryJobService";

    public static final int GETALLENTRIES = 0;
    public static final int GETLATEST = 1;

    public static final int BATCHSIZE = 20;

    private static final String EXTRA_LIMIT = "limit";
    private static final String EXTRA_MODE = "mode";
    private static final String EXTRA_TYPE = "type";
    private static final String EXTRA_LOCATION = "location";

    private GetEntries getEntriesAsync;

    public static void fetchAllEntries(Context context, int location, int type) {
        INSTANCE.setIsUpdating(true);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_MODE, GETALLENTRIES);
        bundle.putInt(EXTRA_LIMIT, BATCHSIZE);
        bundle.putInt(EXTRA_TYPE, type);
        bundle.putInt(EXTRA_LOCATION, location);
        GetEntries entries = new GetEntries();
        entries.execute(bundle);
    }


    @Override
    public boolean onStartJob(final JobParameters params) {
        getEntriesAsync = new GetEntries() {
            @Override

            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                jobFinished(params, !MyLifecycleHandler.applicationInBackground() && KeyStoreHelper.hasGoodOauthToken());
            }
        };
        getEntriesAsync.execute(params.getExtras());
        return true;
    }

    @Override
    public boolean onStopJob(final JobParameters params) {
        if (getEntriesAsync != null) {
            getEntriesAsync.cancel(true);
        }
        return true;
    }

    public static void rescheduleIntent(@NonNull Context context) {
        rescheduleIntent(new FirebaseJobDispatcher(new GooglePlayDriver(context)));
    }


    private static class GetEntries extends AsyncTask<Bundle, Void, Void> {


        @Override
        protected Void doInBackground(Bundle... params) {


            if (params.length != 0) {
                Bundle bundle = params[0];
                getEntries(bundle);
            }
            return null;
        }
    }

    private static void getEntries(Bundle bundle) {

        if (!KeyStoreHelper.hasGoodOauthToken()) {
            return;
        }

        INSTANCE.setIsUpdating(true);

        final int limit = bundle.getInt(EXTRA_LIMIT, 0);
        final int mode = bundle.getInt(EXTRA_MODE, GETLATEST);
        final int type = bundle.getInt(EXTRA_TYPE, Entry.SHOWING_ALL);

        Location location = UserUtils.getLastViewedLocation();
        if (location != null) {
            boolean someReturned = fetchEntries(location.id, limit, mode, type);
            TinyMessageBus.post(new EntryTableUpdated(someReturned, location.id));

            if (someReturned) {
                LocationDatabaseService.checkForThumbnails(location.id);
            }


        }

        INSTANCE.setIsUpdating(false);
    }

    /**
     * Fetch Entries in the background thread
     */
    private static boolean fetchEntries(int locationId, int limit, int mode, int type) {

        boolean someReturned = false;
        INSTANCE.setMode(mode);

        Date oldestDate = SubscriptionPlanDatabaseService.oldestAllowableNonStarredSubscriptionDate(locationId);
        int numDeleted = EntryDatabaseService.deleteOldEntries(oldestDate, locationId);

        if (numDeleted > 0)
            someReturned = true;

        int numRecordsReturned;
        EntryResponse entryResponse;

        int offset = 0;
        Date date = EntryDatabaseService.getLastModifiedDate(locationId);

        try {
            switch (mode) {
                case GETALLENTRIES:
                    offset = EntryDatabaseService.getOffsetForType(type, locationId);
                    switch (type) {
                        case Entry.SHOWING_ALL:
                            entryResponse = EntryAPIService.getEntryRecords(locationId, offset, limit);
                            break;
                        case Entry.SHOWING_AWAY_MODE:
                            List<Mode> modes = ModeDatabaseService.getAllModes();
                            if (modes.isEmpty()) {

                                ModeResponse modeResponse = ModeAPIService.getModes();
                                ModeDatabaseService.insertModes(modeResponse.modes);
                            }
                            Mode armedMode = ModeCache.getMode(armed);

                            entryResponse = EntryAPIService.getEntryRecordsByMode(locationId, offset, limit, armedMode.id);
                            break;
                        case Entry.SHOWING_FLAGGED:

                            entryResponse = EntryAPIService.getFlaggedEntryRecords(locationId, offset, limit);
                            break;
                        default:
                            return someReturned;
                    }
                    break;
                case GETLATEST:
                    if (date == null) {
                        return fetchEntries(locationId, BATCHSIZE, GETALLENTRIES, Entry.SHOWING_ALL) ||
                                fetchEntries(locationId, BATCHSIZE, GETALLENTRIES, Entry.SHOWING_FLAGGED) ||
                                fetchEntries(locationId, BATCHSIZE, GETALLENTRIES, Entry.SHOWING_AWAY_MODE);
                    }

                    entryResponse = EntryAPIService.getEntriesSinceModifiedDate(locationId, offset, BATCHSIZE, date);
                    break;
                default:
                    return someReturned;
            }
        } catch (RetrofitError e) {
            return someReturned;
        }


        List<Entry> entries = entryResponse.entries;
        while (entryResponse.entries.size() >= BATCHSIZE && mode == GETLATEST) {

            try {
                offset += BATCHSIZE;
                entryResponse = EntryAPIService.getEntriesSinceModifiedDate(locationId, offset, BATCHSIZE, date);
                entries.addAll(entryResponse.entries);
            } catch (RetrofitError ignored) {
            }
        }
        // Insert new or update existing Entry records for this locationId
        numRecordsReturned = entryResponse.entries == null ? 0 : entryResponse.entries.size();
        if (numRecordsReturned > 0)
            someReturned = true;

        if (numRecordsReturned > 0) {
            if (mode == GETLATEST) {
                Collections.sort(entries, new Comparator<Entry>() {
                    @Override
                    public int compare(Entry lhs, Entry rhs) {
                        if (lhs.lastModified.after(rhs.lastModified)) {
                            return 1;
                        } else if (lhs.lastModified.before(rhs.lastModified)) {
                            return -1;
                        }
                        return 0;

                    }
                });
            }
            EntryDatabaseService.insertOrUpdateEntries(entries, type);

        }

        if (mode == GETALLENTRIES) {
            if (numRecordsReturned < limit) {
                EntryDatabaseService.allValidEntriesAreInDatabase(locationId, type, true);
            } else {
                EntryDatabaseService.allValidEntriesAreInDatabase(locationId, type, false);
            }
        }

        if (EntryDatabaseService.getFlaggedEntryCount(locationId) == 0 &&
                !EntryDatabaseService.allValidRecordsAreInDatabase(locationId, Entry.SHOWING_FLAGGED)) {
            fetchEntries(locationId, BATCHSIZE, GETALLENTRIES, Entry.SHOWING_FLAGGED);
        }

        if (EntryDatabaseService.getArmedEntries(locationId) == 0 &&
                !EntryDatabaseService.allValidRecordsAreInDatabase(locationId, Entry.SHOWING_AWAY_MODE)) {
            fetchEntries(locationId, BATCHSIZE, GETALLENTRIES, Entry.SHOWING_AWAY_MODE);
        }

        return someReturned;
    }


    public static void rescheduleIntent(FirebaseJobDispatcher dispatcher) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_MODE, GETLATEST);

        GetEntries entries = new GetEntries();
        entries.execute(new Bundle(bundle));

        scheduleJob(APIEntryJobService.class, TAG, dispatcher, bundle);

    }


    public enum Status {
        INSTANCE;
        private boolean isUpdating = false;
        private int mode = GETLATEST;

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public boolean getIsUpdating() {
            return isUpdating;
        }

        public void setIsUpdating(boolean isUpdating) {
            this.isUpdating = isUpdating;
        }
    }
}
