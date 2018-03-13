package is.yranac.canary.util.ga;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.snowplowanalytics.snowplow.tracker.Emitter;
import com.snowplowanalytics.snowplow.tracker.Subject;
import com.snowplowanalytics.snowplow.tracker.Tracker.TrackerBuilder;
import com.snowplowanalytics.snowplow.tracker.emitter.HttpMethod;
import com.snowplowanalytics.snowplow.tracker.emitter.RequestSecurity;
import com.snowplowanalytics.snowplow.tracker.events.ScreenView;
import com.snowplowanalytics.snowplow.tracker.events.SelfDescribing;
import com.snowplowanalytics.snowplow.tracker.events.Structured;
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson;
import com.snowplowanalytics.snowplow.tracker.payload.TrackerPayload;
import com.snowplowanalytics.snowplow.tracker.utils.LogLevel;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

import io.fabric.sdk.android.Fabric;
import is.yranac.canary.BuildConfig;
import is.yranac.canary.CanaryApplication;
import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.util.DateUtil;

import static com.snowplowanalytics.snowplow.tracker.Tracker.init;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_GEOFENCE_ENTER;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_GEOFENCE_EXIT;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_ENTRY;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_GEOFENCE;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_BACKGROUND;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_FOREGROUND;

/**
 * Global Singleton for Google Analysts
 */
public class GoogleAnalyticsHelper {

    private static int customerId;

    public static final String SCHEMA_WATCH_LIVE_LATENCY = "iglu:is.canary/example_event/jsonschema/1-0-0";
    public static final String SCHEMA_CUSTOM_CONTEXT = "iglu:is.canary/application_context/jsonschema/1-0-0";
    public static final String SCHEMA_ENTRY_CONTEXT = "iglu:is.canary/mobile_event/jsonschema/1-0-0";
    public static final String SCHEMA_SETTINGS_CONTEXT = "iglu:is.canary/settings_event/jsonschema/1-0-0";

    /**
     * HashMap of All Trackers if more than one is needed
     */
    private static Tracker mTracker = null;


    /**
     * Get the proper google analytics tracker associated with the tracker id
     *
     * @return Track with the proper configuration
     */
    public synchronized static Tracker getTracker() {
        if (mTracker == null) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(CanaryApplication.getContext());
            mTracker = analytics.newTracker(R.xml.global_tracker);

        }
        return mTracker;
    }

    public synchronized static com.snowplowanalytics.snowplow.tracker.Tracker getSnowPlow() {
        return com.snowplowanalytics.snowplow.tracker.Tracker.instance();
    }


    public synchronized static com.snowplowanalytics.snowplow.tracker.Tracker initSnowplow(Context context) {
        Emitter emitter = getClassicEmitter(context);
        Subject subject = getSubject(context);  // Optional

        com.snowplowanalytics.snowplow.tracker.Tracker tracker = init(new TrackerBuilder(emitter, BuildConfig.FLAVOR, Constants.SNOWPLOW_ID, context)
                .subject(subject)
                .level(LogLevel.OFF)
                .lifecycleEvents(true)
                .sessionContext(true)
                .build()
        );

        trackCustomContext(context);


        return tracker;


    }

    private static Emitter getClassicEmitter(Context context) {
        return new Emitter
                .EmitterBuilder(Constants.SNOWPLOW_URL, context)
                .security(RequestSecurity.HTTPS)
                .method(HttpMethod.POST)
                .build();
    }

    private static Subject getSubject(Context context) {
        return new Subject
                .SubjectBuilder()
                .context(context)
                .build();
    }


    public static void setUserId(int id) {
        getTracker().set("&uid", String.valueOf(id));
        customerId = id;
        if (Fabric.isInitialized())
            Crashlytics.getInstance().core.setUserIdentifier(String.valueOf(id));

        getSnowPlow().getSubject().setUserId(String.valueOf(id));
    }

    public static void removeUserId() {
        getTracker().set("&uid", null);
        customerId = 0;
        getSnowPlow().getSubject().setUserId(String.valueOf(0));
    }

    public static void trackScreenEvent(final String event) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Tracker t = GoogleAnalyticsHelper.getTracker();

                // Set screen name.
                // Where path is a String representing puthe screen name.
                t.setScreenName(event);
                HitBuilders.AppViewBuilder hitBuilders = new HitBuilders.AppViewBuilder();
                setDefaultAttributes(hitBuilders);

                // Send a screen view.
                t.send(hitBuilders.build());

            }
        }).start();
        getSnowPlow().track(ScreenView.builder().name(event).build());
    }

    public static void trackEvent(final String category, final String event) {

        trackEvent(category, event, null, null, 0, 0);

    }


    public static void trackEvent(final String category, final String event, final String label) {
        trackEvent(category, event, label, null, 0, 0);
    }


    public static void trackBluetoothEvent(boolean changingWifi, final String action, final String property) {

        String category;

        if (changingWifi) {
            category = AnalyticsConstants.CATEGORY_BLUETOOTH_CHANGE_WIFI;
        } else {
            category = AnalyticsConstants.CATEGORY_BLUETOOTH_SETUP;
        }

        trackEvent(category, action, property, null, 0, 0);

    }

    public static void trackAudioEvent(boolean changingWifi, final String action, final String property) {

        String category;

        if (changingWifi) {
            category = AnalyticsConstants.CATEGORY_AUDIO_CHANGE_WIFI;
        } else {
            category = AnalyticsConstants.CATEGORY_AUDIO_SETUP;
        }

        trackEvent(category, action, property, null, 0, 0);

    }

    private static void setDefaultAttributes(HitBuilders.AppViewBuilder builder) {

        Date date = new Date();
        String dateString = DateUtil.convertDateToApiString(date);
        String session = sessionId();

        if (customerId != 0) {
            String userId = String.valueOf(customerId);
            builder.setCustomDimension(1, userId);
        }

        builder.setCustomDimension(2, dateString);
        builder.setCustomDimension(3, session);
    }

    public static void trackWatchLiveLatency(final long latency) {

        getSnowPlow().track(Structured.builder().category(AnalyticsConstants.CATEGORY_LIVE).action("initial_load_time").value((double) latency).build());

        Context context = CanaryApplication.getContext();

        TrackerPayload payload = new TrackerPayload();
        payload.add("initialLoadTime", latency);

        if (context != null) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                Integer linkSpeed = wifiInfo.getLinkSpeed();
                payload.add("linkSpeed", linkSpeed);
            }
        }
        SelfDescribingJson eventData = new SelfDescribingJson(SCHEMA_WATCH_LIVE_LATENCY, payload.getMap());

        getSnowPlow().track(SelfDescribing.builder()
                .eventData(eventData)
                .build());
    }


    private static void trackCustomContext(Context context) {
        TrackerPayload payload = new TrackerPayload();
        int stringId = context.getApplicationInfo().labelRes;

        payload.add("name", context.getString(stringId));
        payload.add("version", BuildConfig.VERSION_NAME);
        payload.add("build", String.valueOf(BuildConfig.VERSION_CODE));
        payload.add("build_is_release", !BuildConfig.DEBUG);

        trackCustomSnowplow(SCHEMA_CUSTOM_CONTEXT, payload);

    }

    public static void trackEntry(String action, String property, String device, int location, long entryId) {
        trackEvent(CATEGORY_ENTRY, action, property, device, location, entryId);
    }

    public static void trackEvent(String catagory, String action, String property, String device, int location, long entryId) {

        TrackerPayload payload = new TrackerPayload();
        payload.add("category", catagory);
        payload.add("action", action);
        payload.add("property", property);
        payload.add("device_uuid", device);
        if (location != 0) {
            payload.add("location_id", location);
        }

        if (entryId != 0) {
            payload.add("entry_id", entryId);
        }

        trackCustomSnowplow(SCHEMA_ENTRY_CONTEXT, payload);
    }


    public static void trackSettingsEvent(String setting, String settingType,
                                          int customerId, String device, int location,
                                          String oldValue, String newValue) {

        TrackerPayload payload = new TrackerPayload();
        payload.add("setting", setting);
        payload.add("setting_type", settingType);
        if (customerId != 0) {
            payload.add("customer_id", customerId);
        }

        payload.add("device_uuid", device);
        if (location != 0) {
            payload.add("location_id", location);
        }
        payload.add("new_value", newValue);
        payload.add("old_value", oldValue);


        trackCustomSnowplow(SCHEMA_SETTINGS_CONTEXT, payload);
    }

    private static void trackCustomSnowplow(String context, TrackerPayload payload) {
        SelfDescribingJson eventData = new SelfDescribingJson(context, payload.getMap());

        getSnowPlow().track(SelfDescribing.builder()
                .eventData(eventData)
                .build());
    }

    private static SecureRandom random = new SecureRandom();

    private static String sessiondId = null;

    public static String sessionId() {


        if (sessiondId == null) {
            sessiondId = new BigInteger(130, random).toString(32);

        }

        return sessiondId;
    }


    public static void trackGeofenceEvent(int locationId, boolean geofenceEnter, boolean applicationInBackground) {
        trackEvent(CATEGORY_GEOFENCE, geofenceEnter ? ACTION_GEOFENCE_ENTER : ACTION_GEOFENCE_EXIT,
                applicationInBackground ? PROPERTY_BACKGROUND: PROPERTY_FOREGROUND, null, locationId, 0);
    }
}
