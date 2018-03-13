package is.yranac.canary.services.jobs;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.JobParameters;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import is.yranac.canary.messages.PastReadingsUpdated;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.model.reading.Reading;
import is.yranac.canary.model.reading.ReadingResponse;
import is.yranac.canary.services.api.ReadingAPIService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.ReadingDatabaseService;
import is.yranac.canary.util.DateUtil;
import is.yranac.canary.util.MyLifecycleHandler;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.keystore.KeyStoreHelper;
import retrofit.RetrofitError;

/**
 * An {@link android.app.IntentService} subclass for handling asynchronous entry requests with the canary server
 * <p/>
 */
public class APIReadingJobService extends BaseJobService {
    private static final String TAG = "APIReadingJobService";

    private GetReadings getReadingsAsync;

    @Override
    public boolean onStartJob(final JobParameters params) {
        getReadingsAsync = new GetReadings() {
            @Override

            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                jobFinished(params, !MyLifecycleHandler.applicationInBackground() && KeyStoreHelper.hasGoodOauthToken());
            }
        };
        getReadingsAsync.execute();
        return true;
    }

    @Override
    public boolean onStopJob(final JobParameters params) {
        if (getReadingsAsync != null) {
            getReadingsAsync.cancel(true);
        }
        return true;
    }


    private static class GetReadings extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            getPastReadings();
            return null;
        }

    }

    private static void getPastReadings() {
        Calendar cal = DateUtil.getCalanderInstance();
        cal.add(Calendar.DATE, -1);

        int deleted = ReadingDatabaseService.deleteOldReadings(
                cal.getTime().getTime());
        if (deleted > 0) {
            TinyMessageBus.post(new PastReadingsUpdated(null));
        }

        HashMap<String, Set<Integer>> updatedReadingTypes = new HashMap<>();

        getPastReadingByType(updatedReadingTypes);


        if (!updatedReadingTypes.isEmpty()) {
            TinyMessageBus.post(new PastReadingsUpdated(updatedReadingTypes));
        }
    }


    private static void getPastReadingByType(HashMap<String, Set<Integer>> updatedReadingTypes) {

        if (updatedReadingTypes == null)
            return;

        List<Device> devices = DeviceDatabaseService.getAllActivatedDevices();

        for (Device device : devices) {

            if (!shouldMakeCalls(device)) {
                continue;
            }
            ReadingResponse readingResponse;

            Calendar cal = DateUtil.getCalanderInstance();
            cal.add(Calendar.DATE, -1);

            Date checkDate = cal.getTime();


            Reading latestReading = ReadingDatabaseService.getLatestReadingForDevice(device.resourceUri);

            if (latestReading != null)
                checkDate = latestReading.created;

            try {
                readingResponse = ReadingAPIService.getReadingsSinceDate(checkDate, device.id);
            } catch (RetrofitError e) {
                continue;
            }

            ReadingDatabaseService.insertReadings(readingResponse.readings);

            Set<Integer> existingTypes = new HashSet<>();

            for (Reading reading : readingResponse.readings) {
                existingTypes.add(reading.sensorType.id);
            }

            updatedReadingTypes.put(device.resourceUri, existingTypes);

            while (readingResponse.meta.next != null) {

                try {
                    readingResponse = ReadingAPIService.getResourceByUri(readingResponse.meta.next);

                } catch (RetrofitError e) {
                    return;
                }
                ReadingDatabaseService.insertReadings(readingResponse.readings);
            }
        }
    }

    private static boolean shouldMakeCalls(Device device) {
        if (device.deviceType.id != DeviceType.CANARY_AIO)
            return true;

        Reading latestTempReading = ReadingDatabaseService.getLatestReadingForDevice(device.resourceUri, 1);
        Reading latestHumidityReading = ReadingDatabaseService.getLatestReadingForDevice(device.resourceUri, 2);
        Reading latestAirQualityReading = ReadingDatabaseService.getLatestReadingForDevice(device.resourceUri, 3);
        if (latestTempReading == null || latestHumidityReading == null || latestAirQualityReading == null)
            return true;

        boolean oldTemp = latestTempReading.isOlderThan(TimeUnit.MINUTES.toMillis(10));
        boolean oldAir = latestAirQualityReading.isOlderThan(TimeUnit.MINUTES.toMillis(10));
        boolean oldHumidity = latestHumidityReading.isOlderThan(TimeUnit.MINUTES.toMillis(10));

        return oldTemp || oldAir || oldHumidity;
    }

    public static void rescheduleIntent(FirebaseJobDispatcher dispatcher) {
        GetReadings getLocation = new GetReadings();
        getLocation.execute();
        scheduleJob(APIReadingJobService.class, TAG, dispatcher, null);
    }

}
