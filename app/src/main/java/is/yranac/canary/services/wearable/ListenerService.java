package is.yranac.canary.services.wearable;

import android.content.Intent;
import android.graphics.Bitmap;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import is.yranac.canary.CanaryApplication;
import is.yranac.canary.Constants;
import is.yranac.canary.messages.LocationAndEntry;
import is.yranac.canary.messages.LocationCurrentModeChanging;
import is.yranac.canary.model.WearData;
import is.yranac.canary.model.avatar.Avatar;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.mode.Mode;
import is.yranac.canary.model.mode.ModeCache;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.services.api.LocationAPIService;
import is.yranac.canary.services.database.AvatarDatabaseService;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.database.ReadingDatabaseService;
import is.yranac.canary.services.database.ThumbnailDatabaseService;
import is.yranac.canary.ui.EntryDetailActivity;
import is.yranac.canary.ui.LaunchActivity;
import is.yranac.canary.util.JSONUtil;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.MyLifecycleHandler;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.WearableUtil;
import is.yranac.canary.util.cache.location.SyncLocationMode;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by narendramanoharan on 6/18/16.
 */
public class ListenerService extends WearableListenerService {

    private static final String LOG_TAG = "ListenerService";

    private static final String LOCATION_DATA = "location_data";
    private static final String THUMBNAIL = "thumbnail";
    private static final String CUSTOMER = "customer";

    public GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        switch (messageEvent.getPath()) {
            case Constants.PATH_CHANGE_MODE:
                String mode_location_URI = new String(messageEvent.getData());
                modeSet(mode_location_URI);
                break;
            case Constants.PATH_LOCATION_DATA:
                getSplashData();
                break;
            case Constants.PATH_OPEN_ENTRY_ACTIVITY:
                String location = new String(messageEvent.getData());
                Log.i(LOG_TAG, location);
                startEntryActivity(Long.parseLong(location));
                break;
            case Constants.PATH_ERROR:
                getAnalyticsError(messageEvent);
                break;
            case Constants.PATH_ANALYTICS:
                getAnalytics(messageEvent);
                break;
            default:
                break;
        }
    }

    private void getAnalytics(MessageEvent messageEvent) {
        Customer customer = CurrentCustomer.getCurrentCustomer();
        if (customer != null) {
            GoogleAnalyticsHelper.setUserId(customer.id);
        }
        DataMap map = DataMap.fromByteArray(messageEvent.getData());
        String event = map.getString("category");
        String label = map.getString("event");
        GoogleAnalyticsHelper.trackEvent("wear", event, label);
    }

    private void getAnalyticsError(MessageEvent messageEvent) {
        DataMap map = DataMap.fromByteArray(messageEvent.getData());
        ByteArrayInputStream bis = new ByteArrayInputStream(map.getByteArray("exception"));
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            Throwable ex = (Throwable) ois.readObject();
            if (!Fabric.isInitialized()) {
                Fabric.with(CanaryApplication.getContext(), new Crashlytics());
            }
            Crashlytics.setBool("wearException", true);
            Crashlytics.setString("fingerprint", map.getString("fingerprint"));
            Crashlytics.logException(ex);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getSplashData() {
        List<LocationAndEntry> entries = EntryDatabaseService.getLatestEntry();


        PutDataMapRequest getLocationList = PutDataMapRequest.create(Constants.PATH_LOCATION_DATA);
        ImageLoader imageLoader = ImageLoader.getInstance();

        for (LocationAndEntry locationWithEntry : entries) {
            if (!locationWithEntry.location.devices.isEmpty()) {
                Device device = locationWithEntry.location.devices.get(0);

                if (device.deviceType.id == DeviceType.FLEX) {
                    locationWithEntry.battery = ReadingDatabaseService.getLatestReadingForDevice(device.resourceUri, 7);
                    locationWithEntry.wifi = ReadingDatabaseService.getLatestReadingForDevice(device.resourceUri, 8);
                } else {
                    locationWithEntry.temp = ReadingDatabaseService.getLatestReadingForDevice(device.resourceUri, 2);
                    locationWithEntry.humidity = ReadingDatabaseService.getLatestReadingForDevice(device.resourceUri, 1);
                    locationWithEntry.air = ReadingDatabaseService.getLatestReadingForDevice(device.resourceUri, 3);
                }

            }
            if (locationWithEntry.entry != null) {
                List<Thumbnail> thumbnails = ThumbnailDatabaseService.getThumbnailsForEntry(locationWithEntry.entry.id);
                if (!thumbnails.isEmpty()) {
                    Bitmap bit = imageLoader.loadImageSync(thumbnails.get(0).imageUrl());
                    if (bit != null) {
                        Bitmap resizedBitmap = WearableUtil.getResizedBitmap(bit, 200);
                        Asset entryAsset = WearableUtil.toAsset(resizedBitmap);
                        getLocationList.getDataMap().putAsset(THUMBNAIL + "_" + locationWithEntry.entry.id, entryAsset);
                    }

                }
            }

            for (Customer customer : locationWithEntry.location.customers) {
                Avatar avatar = AvatarDatabaseService.getAvatarFromCustomerId(customer.id);
                if (avatar != null) {
                    Bitmap bit = imageLoader.loadImageSync(avatar.thumbnailUrl());
                    if (bit != null) {
                        Bitmap resizedBitmap = WearableUtil.getResizedBitmap(bit, 200);
                        Asset entryAsset = WearableUtil.toAsset(resizedBitmap);
                        getLocationList.getDataMap().putAsset(CUSTOMER + "_" + customer.id, entryAsset);
                    }
                }
            }
        }


        WearData wearData = new WearData();
        wearData.currentCustomer = CurrentCustomer.getCurrentCustomer();
        wearData.locationAndEntries = entries;
        getLocationList.getDataMap().putString(LOCATION_DATA, JSONUtil.getJSONString(wearData));

        DataApi.DataItemResult result = Wearable.DataApi.putDataItem(mGoogleApiClient, getLocationList.asPutDataRequest().setUrgent()).await();

        if (result.getStatus().isSuccess()) {
            Log.v(LOG_TAG, "DataMap: " + getLocationList.getDataMap() + " sent successfully to data layer ");
        } else {
            // Log an error
            Log.v(LOG_TAG, "ERROR_URI: failed to send DataMap to data layer");
        }

    }

    private void changeMode(final String mode, final String modeChangeLocation) {
        final Location modeLocation = LocationDatabaseService.getLocationFromResourceUri(modeChangeLocation);
        final Mode currentMode = modeLocation != null ? modeLocation.currentMode : null;
        if ((currentMode != null && currentMode.name.equals(mode)) && !modeLocation.isPrivate) {
            return;
        } else if ((modeLocation != null && modeLocation.isPrivate) && ModeCache.privacy.equalsIgnoreCase(mode)) {
            return;
        }

        if (!mode.equalsIgnoreCase(ModeCache.privacy)) {
            TinyMessageBus.post(new LocationCurrentModeChanging(true, mode));
            LocationAPIService.changeLocationCurrentMode(modeLocation.resourceUri, ModeCache.getMode(mode).resourceUri, new Callback<Void>() {
                @Override
                public void success(Void aVoid, Response response) {
//                    TODO: Cache Clean Up
                    TinyMessageBus.post(new SyncLocationMode(modeLocation.id, ModeCache.getMode(ModeCache.privacy).name, false));
                    TinyMessageBus.post(new SyncLocationMode(modeLocation.id, ModeCache.getMode(ModeCache.privacy).name, false));
                }

                @Override
                public void failure(RetrofitError error) {
                    TinyMessageBus.post(new LocationCurrentModeChanging(false, currentMode.resourceUri));
                    sendFailMessage(currentMode.resourceUri);
                }
            });
        } else {
            TinyMessageBus.post(new LocationCurrentModeChanging(true, mode));
            LocationAPIService.changePrivacyMode(modeLocation.id, true, new Callback<Void>() {
                @Override
                public void success(Void aVoid, Response response) {
                    TinyMessageBus.post(new SyncLocationMode(modeLocation.id, ModeCache.privacy, true));
                }

                @Override
                public void failure(RetrofitError error) {
                    TinyMessageBus.post(new LocationCurrentModeChanging(false, currentMode.resourceUri));
                    sendFailMessage(currentMode.name);
                }
            });
        }
    }

    public void sendFailMessage(String modeURI) {
        PutDataMapRequest modeChangeFail = PutDataMapRequest.create(Constants.PATH_MODE_FAIL).setUrgent();
        modeChangeFail.getDataMap().putString("modeURI", modeURI);
        modeChangeFail.getDataMap().putLong("time", new Date().getTime());
        Wearable.DataApi.putDataItem(mGoogleApiClient, modeChangeFail.asPutDataRequest().setUrgent()).await();
    }

    public void modeSet(String mode_locationUri) {
        String[] split = mode_locationUri.split("\\|");
        String modeChangeUri = split[0];
        String modeChangeLocation = split[1];
        changeMode(modeChangeUri, modeChangeLocation);
    }


    public void startEntryActivity(long entryId) {

        if (MyLifecycleHandler.applicationInBackground()) {
            Intent i = new Intent(this, LaunchActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setAction(LaunchActivity.ENTRY);
            i.putExtra(LaunchActivity.ENTRY_ID, entryId);
            startActivity(i);
        } else {
            Intent i = new Intent(this, EntryDetailActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(EntryDetailActivity.ENTRY_ID, entryId);
            startActivity(i);
        }

    }
}