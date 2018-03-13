package is.yranac.canary;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiscCache;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;
import com.urbanairship.push.notifications.NotificationFactory;

import io.fabric.sdk.android.Fabric;
import is.yranac.canary.messages.PushToken;
import is.yranac.canary.messages.UpdateToken;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.featureflag.CurrentLocationFeatureFlags;
import is.yranac.canary.receiver.CustomNotificationFactory;
import is.yranac.canary.util.LocaleHelper;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.MyLifecycleHandler;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.cache.CacheProvider;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

/**
 * This the base Application class that all different versions of the application must have a subclass of.
 */
public class CanaryApplication extends MultiDexApplication {
    protected static final String LOG_TAG = "CanaryApplication";

    private static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        context = base;
        context = LocaleHelper.setLocale(base);
        super.attachBaseContext(context);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(LOG_TAG, "config changed");
        LocaleHelper.setLocale(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GoogleAnalyticsHelper.initSnowplow(context);

        PreferencesUtils.setAppNumber();

        if (Utils.isDev()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String url = prefs.getString("url", Constants.BASE_URL_ENDPOINT);
            prefs.edit().putString("url", url).apply();
            Constants.updateEndPoint(url);
        }


        if (!Utils.isDemo()) {
            Fabric.with(this, new Crashlytics());

        } else {

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
//                    .penaltyDeath()
                    .build());
        }


        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        LruDiscCache lruDiscCache = new LruDiscCache(getCacheDir(),
                DefaultConfigurationFactory.createFileNameGenerator(), 1024 * 1024 * 100);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .diskCacheExtraOptions(480, 320, null)
                .diskCache(lruDiscCache)
                .build();

        ImageLoader.getInstance().init(config);


        AirshipConfigOptions options = new AirshipConfigOptions.Builder()
                .setDevelopmentAppKey(Constants.URBAN_AIRSHIP_DEV_KEY)
                .setDevelopmentAppSecret(Constants.URBAN_AIRSHIP_DEV_SECRET)
                .setProductionAppKey(Constants.URBAN_AIRSHIP_PRODUCTION_KEY)
                .setProductionAppSecret(Constants.URBAN_AIRSHIP_PRODUCTION_SECRET)
                .setInProduction(true)
                .setGcmSender(Constants.GCM_SENDER_ID)
                .build();

        UAirship.takeOff(this, options, new UAirship.OnReadyCallback() {
            @Override
            public void onAirshipReady(UAirship uAirship) {

                NotificationFactory notificationFactory
                        = new CustomNotificationFactory(getApplicationContext());
                // Set the factory on the PushManager
                uAirship.getPushManager().setNotificationFactory(notificationFactory);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence name = getString(R.string.app_name);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannelGroup(new NotificationChannelGroup(Constants.CANARY_NOTIFICATION_CHANNEL, name));
            }
        }

        registerActivityLifecycleCallbacks(new MyLifecycleHandler());

        //this will be wired up as long as the application is alive so that services can user this
        //even when the app is in the background
        CacheProvider.register();
        CurrentLocationFeatureFlags.getInstance();

        CurrentCustomer.initCache();
        PushManager pushManager = UAirship.shared().getPushManager();

        if (!Utils.isDemo()) {
            // Take off initializes the services

            pushManager.setSoundEnabled(true); // enable/disable sound when a push is received
            pushManager.setVibrateEnabled(true); // enable/disable vibrate on receive
            pushManager.setPushEnabled(true); // enable/disable all push messages.
            pushManager.setUserNotificationsEnabled(true);
            TinyMessageBus.post(new PushToken());
            TinyMessageBus.post(new UpdateToken());


        } else {
            pushManager.setPushEnabled(false);
        }

        Customer customer = CurrentCustomer.getCurrentCustomer();

        if (customer != null) {
            GoogleAnalyticsHelper.setUserId(customer.id);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = mNotificationManager.getNotificationChannel(Constants.CANARY_NOTIFICATION_CHANNEL);
            if (channel == null) {
                channel = new NotificationChannel(Constants.CANARY_NOTIFICATION_CHANNEL,
                        context.getString(R.string.app_name),
                        NotificationManager.IMPORTANCE_DEFAULT);
                mNotificationManager.createNotificationChannel(channel);
            }
        }
    }

    @Deprecated
    /**
     * We should not be using this anymore, it's better memory management and app performance to pass
     * contexts along rather keeping static references
     */
    public static Context getContext() {
        return context;
    }

}
