package is.yranac.canary.services.jobs;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Date;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.contentproviders.CanaryDeviceContentProvider;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.services.api.EntryAPIService;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.ui.LaunchActivity;
import is.yranac.canary.ui.MainActivity;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.RandomString;
import is.yranac.canary.util.keystore.KeyStoreHelper;

/**
 * Created by michaelschroeder on 1/29/18.
 */

public class APINotificationJobService extends JobService {


    private static final String ENTRY_ID = "entry_id";
    private static final String TITLE = "title";
    private static final String DEVICE_NAME = "device_name";
    private static final String DEVICE_UUID = "device_uuid";
    private static final String LOG_TAG = "APINotificationJobService";
    private GetEntry getLocationsAsync;

    public static void scheduleJob(Context context,
                                   long entryID,
                                   final String title,
                                   final String deviceName,
                                   final String deviceUUID) {

        Log.i(LOG_TAG, "scheduleJob");

        Bundle bundle = new Bundle();
        bundle.putLong(ENTRY_ID, entryID);
        bundle.putString(TITLE, title);
        bundle.putString(DEVICE_NAME, deviceName);
        bundle.putString(DEVICE_UUID, deviceUUID);


        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job.Builder myJob = dispatcher.newJobBuilder()
                .setService(APINotificationJobService.class)
                .setRecurring(false)
                .setExtras(bundle)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setTrigger(Trigger.NOW)
                .setTag(new RandomString(4).nextString())
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setReplaceCurrent(true);

        dispatcher.mustSchedule(myJob.build());
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters params) {
        Bundle bundle = params.getExtras();
        if (bundle == null)
            return false;

        Log.i(LOG_TAG, "onStartJob");

        long entryId = bundle.getLong(ENTRY_ID);
        String title = bundle.getString(TITLE);
        String deviceName = bundle.getString(DEVICE_NAME);
        String deviceUUID = bundle.getString(DEVICE_UUID);
        getLocationsAsync = new GetEntry(getBaseContext(), entryId, title, deviceName, deviceUUID) {
            @Override

            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                jobFinished(params, false);
            }
        };
        getLocationsAsync.execute();
        return true;
    }

    @Override
    public boolean onStopJob(final JobParameters params) {
        if (getLocationsAsync != null) {
            getLocationsAsync.cancel(true);
        }
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private class GetEntry extends AsyncTask<Void, Void, Void> {


        private final Context context;
        private final Long entryId;
        private final String title;
        private final String deviceName;
        private final String deviceUUID;

        GetEntry(Context context, long entryId, String title, String deviceName, String deviceUUID) {

            this.context = context;
            this.entryId = entryId;
            this.title = title;
            this.deviceName = deviceName;
            this.deviceUUID = deviceUUID;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(LOG_TAG, "doInBackground");


            if (!KeyStoreHelper.hasGoodOauthToken())
                return null;


            Entry entry;
            try {
                entry = EntryAPIService.getEntryById(entryId);
            } catch (Exception ignored) {
                return null;
            }

            entry.lastModified = new Date(0);
            EntryDatabaseService.insertEntryAndLinkedObjects(entry, Entry.SHOWING_ALL);

            if (entry.thumbnails.isEmpty())
                return null;


            Thumbnail thumbnail = entry.thumbnails.get(0);
            ImageLoader imageLoader = ImageLoader.getInstance();

            Bitmap bitmap = imageLoader.loadImageSync(thumbnail.imageUrl());
            imageLoader.stop();

            Intent resultIntent = new Intent(context, LaunchActivity.class);
            resultIntent.putExtra("entryId", entryId);
            resultIntent.putExtra("position", -1);
            resultIntent.putExtra("from_notification", true);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            final RemoteViews remoteViews = createRemoteViews(context, R.layout.notifcation_layout,
                    title, deviceName, bitmap);


            Intent intent = new Intent(context, LaunchActivity.class);
            intent.setAction("watchlive");
            intent.putExtra(CanaryDeviceContentProvider.COLUMN_UUID, deviceUUID);
            intent.putExtra(CanaryDeviceContentProvider.COLUMN_LOCATION_ID, entry.locationUri);
            PendingIntent watchLiveIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.CANARY_NOTIFICATION_CHANNEL)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.notification_logo);

            builder.addAction(
                    new NotificationCompat.Action(0,
                            context.getString(R.string.view_entry),
                            resultPendingIntent));

            builder.addAction(
                    new NotificationCompat.Action(0,
                            context.getString(R.string.watch_live),
                            watchLiveIntent));

            final RemoteViews bigRemoteView = createRemoteViews(context, R.layout.notification_layout_large,
                    title, deviceName, bitmap);
            builder.setCustomContentView(remoteViews)
                    .setOnlyAlertOnce(true)
                    .setCustomBigContentView(bigRemoteView)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle());

            final NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (mNotificationManager != null) {
                mNotificationManager.notify(entryId.intValue(), builder.build());
            }

            return null;
        }

        private RemoteViews createRemoteViews(Context context, int layout,
                                              String title, String message, Bitmap bitmap) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), layout);
            remoteViews.setTextViewText(R.id.text_title, title);
            remoteViews.setTextViewText(R.id.text_message, message);
            remoteViews.setImageViewBitmap(
                    R.id.image_icon, bitmap);
            return remoteViews;
        }
    }


}
