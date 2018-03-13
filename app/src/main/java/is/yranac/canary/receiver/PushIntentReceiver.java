package is.yranac.canary.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.urbanairship.push.PushMessage;

import java.util.Random;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.messages.PushReceived;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.jobs.APINotificationJobService;
import is.yranac.canary.services.jobs.APIReadingJobService;
import is.yranac.canary.ui.LaunchActivity;
import is.yranac.canary.ui.MainActivity;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.MyLifecycleHandler;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.ga.AnalyticsConstants.NOTIFICATION_ACTION;
import static is.yranac.canary.util.ga.AnalyticsConstants.NOTIFICATION_BACKGROUND_PROPERTY;
import static is.yranac.canary.util.ga.AnalyticsConstants.NOTIFICATION_CATEGORY;
import static is.yranac.canary.util.ga.AnalyticsConstants.NOTIFICATION_IN_APP_PROPERTY;

/**
 * Created by Schroeder on 7/30/14.
 */

public class PushIntentReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "PushIntentReceiver";

    public static final String EVENT_MOTION_START = "motion_start";
    public static final String EVENT_MOTION_STOP = "motion_stop";
    public static final String EVENT_HUMIDITY_HIGH = "humidity_high";
    public static final String EVENT_HUMIDITY_LOW = "humidity_low";
    public static final String EVENT_TEMPERATURE_HIGH = "temperature_high";
    public static final String EVENT_TEMPERATURE_LOW = "temperature_low";
    public static final String EVENT_ENTRY_EXPORTED = "entry_exported";
    public static final String EVENT_ENTRY_EXPORTED_SHARE = "entry_exported_share";
    public static final String EVENT_CONNECT = "connect";
    public static final String EVENT_DISCONNECT = "disconnect";
    public static final String EVENT_POWER_SOURCE_OFF = "power_source_off";
    public static final String EVENT_POWER_SOURCE_ON = "power_source_on";
    public static final String EVENT_BATTERY_FULL = "battery_full";
    public static final String EVENT_BATTERY_LOW = "battery_low";
    public static final String EVENT_BATTERY_CRITICAL_LOW = "battery_critical_low";

    public static final String CUSTOM_SOUND = "canary.wav";
    public static final String DEFAULT_SOUND = "default";

    private void showMain(Context context) {
        Intent newIntent = new Intent(context, LaunchActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }

    public static boolean shouldOpen(String eventType) {

        if (eventType == null)
            return false;

        switch (eventType) {
            case EVENT_MOTION_START:
            case EVENT_MOTION_STOP:
            case EVENT_HUMIDITY_HIGH:
            case EVENT_HUMIDITY_LOW:
            case EVENT_TEMPERATURE_HIGH:
            case EVENT_TEMPERATURE_LOW:
            case EVENT_ENTRY_EXPORTED:
            case EVENT_ENTRY_EXPORTED_SHARE:
            case EVENT_CONNECT:
            case EVENT_DISCONNECT:
            case EVENT_POWER_SOURCE_OFF:
            case EVENT_POWER_SOURCE_ON:
            case EVENT_BATTERY_FULL:
            case EVENT_BATTERY_LOW:
            case EVENT_BATTERY_CRITICAL_LOW:
                return true;
            default:
                return false;
        }
    }

    public static boolean isMotion(String eventType) {
        if (eventType == null)
            return false;

        switch (eventType) {
            case EVENT_MOTION_START:
            case EVENT_MOTION_STOP:
                return true;
            default:
                return false;
        }
    }

    public static boolean hasEntryView(String eventType) {
        if (eventType == null)
            return false;

        switch (eventType) {
            case EVENT_MOTION_START:
            case EVENT_MOTION_STOP:
            case EVENT_ENTRY_EXPORTED:
            case EVENT_ENTRY_EXPORTED_SHARE:
                return true;
            default:
                return false;
        }
    }

    public static boolean isDisconnect(String eventType) {
        if (eventType == null)
            return false;

        switch (eventType) {
            case EVENT_DISCONNECT:
                return true;
            default:
                return false;
        }
    }


    private boolean shouldRefreshDeviceStatistics(String eventType) {
        if (TextUtils.isEmpty(eventType))
            return false;

        switch (eventType) {
            case EVENT_POWER_SOURCE_OFF:
            case EVENT_POWER_SOURCE_ON:
            case EVENT_BATTERY_FULL:
            case EVENT_BATTERY_LOW:
            case EVENT_BATTERY_CRITICAL_LOW:
            case EVENT_HUMIDITY_HIGH:
            case EVENT_HUMIDITY_LOW:
            case EVENT_TEMPERATURE_HIGH:
            case EVENT_TEMPERATURE_LOW:
                return true;
            default:
                return false;
        }
    }

    private void onPushReceived(@NonNull final Context context, @NonNull PushMessage message) {
        Log.i(LOG_TAG, "Received push notification. Alert: " + message.getAlert());

        Bundle bundle = message.getPushBundle();

        String entryType = bundle.getString("event_type", "default");
        final String entryIdString = bundle.getString("entry_id", "0");
        final Long entryId = Long.parseLong(entryIdString);
        final String eventType = bundle.getString("event_type");


        int duration = 0;
        int size = 0;
        if (bundle.containsKey("video_export_duration")) {
            duration = Integer.parseInt(bundle.getString("video_export_duration", "0"));
        }
        if (bundle.containsKey("video_export_size")) {
            size = Integer.parseInt(bundle.getString("video_export_size", "0"));
        }
        final String deviceUUID = bundle.getString("event_device_uuid", "");
        String alert = message.getAlert();

        if (shouldOpen(entryType)) {
            TinyMessageBus.post(new PushReceived(entryIdString, alert, entryType, duration, size));
        }


        //lets refresh device stats if it's a power related notification
        if (shouldRefreshDeviceStatistics(entryType)) {
            APIReadingJobService.rescheduleIntent(new FirebaseJobDispatcher(new GooglePlayDriver(context.getApplicationContext())));
        }

        String property = MyLifecycleHandler.applicationInBackground() ? NOTIFICATION_BACKGROUND_PROPERTY : NOTIFICATION_IN_APP_PROPERTY;

        GoogleAnalyticsHelper.trackEvent(NOTIFICATION_CATEGORY, NOTIFICATION_ACTION, property, deviceUUID, 0, entryId);

        final String title = message.getAlert();
        String deviceName = null;

        if (!TextUtils.isEmpty(deviceUUID) && isMotion(entryType)) {
            String deviceUri = Utils.buildResourceUri(Constants.DEVICE_URI, deviceUUID);
            Device device = DeviceDatabaseService.getDeviceFromResourceUri(deviceUri);
            if (device != null) {
                deviceName = device.name;
            }
        }
        // Build the notification
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.CANARY_NOTIFICATION_CHANNEL)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentTitle(title)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.drawable.notification_logo);
        if (deviceName != null) {
            builder.setContentText(deviceName);
        }


        String notificationSound = bundle.getString("notification_sound", "default");
        switch (notificationSound) {
            case CUSTOM_SOUND:
                Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification_sound);
                builder.setSound(soundUri);
                break;
            default:
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(uri);
                break;
        }


        final PendingIntent resultPendingIntent;
        if (hasEntryView(entryType)) {

            Intent resultIntent = new Intent(context, LaunchActivity.class);
            if (eventType.equalsIgnoreCase("entry_exported")) {
                resultIntent.putExtra("open_menu", true);
            }
            if (eventType.equalsIgnoreCase("entry_exported_share")) {
                resultIntent.putExtra("show_share_overlay", true);
            }

            resultIntent.putExtra("entryId", entryId);
            resultIntent.putExtra("position", -1);
            resultIntent.putExtra("from_notification", true);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        } else if (shouldRefreshDeviceStatistics(entryType)) {
            APIReadingJobService.rescheduleIntent(new FirebaseJobDispatcher(new GooglePlayDriver(context.getApplicationContext())));
            Intent resultIntent = new Intent(context, LaunchActivity.class);
            resultIntent.setAction("homehealth");
            resultIntent.putExtra("event_device_uuid", deviceUUID);

            resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else if (isDisconnect(eventType)) {
            Intent intent = new Intent(context, LaunchActivity.class);
            intent.setAction("disconnet");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            resultPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        } else {
            Intent resultIntent = new Intent(context, LaunchActivity.class);
            resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        }

        builder.setContentIntent(resultPendingIntent);

        final NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (mNotificationManager != null) {
            if (isMotion(eventType)) {
                mNotificationManager.notify(entryId.intValue(), builder.build());
            } else {
                mNotificationManager.notify(new Random().nextInt(), builder.build());
            }
        } else {
            return;
        }

        if (isMotion(eventType)) {
            APINotificationJobService.scheduleJob(context.getApplicationContext(), entryId, title, deviceName, deviceUUID);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        PushMessage message = PushMessage.fromIntent(intent);
        if (message == null)
            return;

        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case "com.urbanairship.push.RECEIVED":
                    onPushReceived(context, message);
                    break;

            }
        }
    }
}