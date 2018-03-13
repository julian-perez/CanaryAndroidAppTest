package is.yranac.canary.util.cache;

import android.text.TextUtils;

import com.urbanairship.UAirship;

import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.BuildConfig;
import is.yranac.canary.Constants;
import is.yranac.canary.messages.DeleteTag;
import is.yranac.canary.messages.DeviceDeactivated;
import is.yranac.canary.messages.GeneralError;
import is.yranac.canary.messages.GetCachedDevice;
import is.yranac.canary.messages.GetCachedDeviceAndLocationOwner;
import is.yranac.canary.messages.GetDeviceMasks;
import is.yranac.canary.messages.GetTags;
import is.yranac.canary.messages.GotCachedDevice;
import is.yranac.canary.messages.GotCachedDeviceAndLocationOwner;
import is.yranac.canary.messages.GotDeviceMasks;
import is.yranac.canary.messages.InsertTag;
import is.yranac.canary.messages.InsertVideos;
import is.yranac.canary.messages.NewDeviceSettings;
import is.yranac.canary.messages.PurgeDatabase;
import is.yranac.canary.messages.PushToken;
import is.yranac.canary.messages.ShareStartedProcessing;
import is.yranac.canary.messages.Tags;
import is.yranac.canary.messages.UpdateCachedDevice;
import is.yranac.canary.messages.UpdateCachedDeviceName;
import is.yranac.canary.messages.UpdateDeviceSettings;
import is.yranac.canary.messages.UpdateToken;
import is.yranac.canary.model.authentication.OauthResponse;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceSettings;
import is.yranac.canary.model.devicetoken.DeviceToken;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.locationchange.ClientLocation;
import is.yranac.canary.model.masking.DeviceMasks;
import is.yranac.canary.model.membership.MembershipPresence;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.api.DeviceTokenAPIServices;
import is.yranac.canary.services.api.OathAuthenticationAPIService;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.database.CacheGeofenceDatabaseService;
import is.yranac.canary.services.database.CustomerDatabaseService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.DeviceTokenDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.database.MembershipDatabaseService;
import is.yranac.canary.services.database.ThumbnailDatabaseService;
import is.yranac.canary.services.database.UserTagsDatabaseService;
import is.yranac.canary.services.database.VideoExportDatabaseService;
import is.yranac.canary.util.JSONUtil;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.cache.device.DeviceCacheProvider;
import is.yranac.canary.util.cache.location.LocationCacheProvider;
import is.yranac.canary.util.cache.location.UpdateMembershipPresenceCache;
import is.yranac.canary.util.keystore.KeyStoreHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sergeymorozov on 6/9/15.
 */
public class CacheProvider {
    private static final String CACHE_TAG = "CacheProvider";
    private static CacheProvider cacheProvider;

    public static void register() {
        TinyMessageBus.register(getCacheProvider());
        LocationCacheProvider.register();
        DeviceCacheProvider.register();
    }

    public static void unregister() {
        TinyMessageBus.unregister(getCacheProvider());
        LocationCacheProvider.unregister();
        DeviceCacheProvider.unregister();
    }

    private static CacheProvider getCacheProvider() {
        if (cacheProvider == null)
            cacheProvider = new CacheProvider();
        return cacheProvider;
    }

    /*
    * Device Cache
    * */
    @Subscribe(mode = Subscribe.Mode.Background, queue = CACHE_TAG)
    public void updateCachedDeviceName(UpdateCachedDeviceName updateCachedDeviceName) {
        DeviceDatabaseService.updateDeviceName(updateCachedDeviceName.deviceId + "", updateCachedDeviceName.newDeviceName);
    }

    @Subscribe(mode = Subscribe.Mode.Background, queue = CACHE_TAG)
    public void addClientLocationCache(ClientLocation clientLocation) {
        CacheGeofenceDatabaseService.insertClientLocation(clientLocation);
    }


    @Subscribe(mode = Subscribe.Mode.Background, queue = CACHE_TAG)
    public void purgeDatabse(PurgeDatabase purgeDatabase) {

        Log.i(CACHE_TAG, "Purge Database message received");
        BaseDatabaseService.purgeDatabase();
    }

    @Subscribe(mode = Subscribe.Mode.Background, queue = CACHE_TAG)
    public void updateCachedDevice(UpdateCachedDevice updateCachedDevice) {

        if (updateCachedDevice == null || updateCachedDevice.device == null)
            return;

        Device deviceToModify = updateCachedDevice.device;

        DeviceDatabaseService.insertOrUpdateDevice(deviceToModify);
        UserUtils.checkCurrentLocationDevices();
        if (deviceToModify.activationStatus.equalsIgnoreCase("deactivated")) {
            TinyMessageBus.post(new DeviceDeactivated(deviceToModify));
        }
    }

    @Subscribe(mode = Subscribe.Mode.Background, queue = CACHE_TAG)
    public void updateCachedDevice(UpdateDeviceSettings updateCachedDevice) {
        Log.i(CACHE_TAG, "Get device settings");
        DeviceSettings settings = DeviceDatabaseService.getDeviceSettingsForDevice(updateCachedDevice.device.id);
        if (settings != null && settings.recentlyUpdate())
            return;
        try {
            DeviceAPIService.getDeviceSettings(updateCachedDevice.device.id);
        } catch (RetrofitError ignored) {

        }
    }

    @Subscribe(mode = Subscribe.Mode.Background, queue = CACHE_TAG)
    public void getCachedDeviceBySerial(GetCachedDevice info) {
        Device device = DeviceDatabaseService.getDeviceFromResourceUri(info.deviceUri);
        TinyMessageBus.post(new GotCachedDevice(device));
    }


    @Subscribe(mode = Subscribe.Mode.Background, queue = CACHE_TAG)
    public void updateDeviceSettings(NewDeviceSettings newDeviceSettings) {
        DeviceDatabaseService.insertDeviceSettings(newDeviceSettings.deviceSettings);
    }

    @Subscribe(mode = Subscribe.Mode.Background, queue = CACHE_TAG)
    public void getDeviceMasks(GetDeviceMasks getDeviceMasks) {

        //checking cache first
        String existingMasksJSON = PreferencesUtils.getDeviceMasks(getDeviceMasks.deviceId);
        DeviceMasks deviceMasks = JSONUtil.getObject(existingMasksJSON, DeviceMasks.class);

        //if cache is empty or there aren't any actual masks lets check the cloud,
        if (deviceMasks == null
                || (deviceMasks.deviceMasks != null && deviceMasks.deviceMasks.size() == 0) || getDeviceMasks.refreshCachedData) {
            try {
                deviceMasks = DeviceAPIService.getDeviceMasks(getDeviceMasks.deviceId);
                PreferencesUtils.setDeviceMasks(deviceMasks, getDeviceMasks.deviceId);
            } catch (RetrofitError e) {
                Log.e("getDeviceMasks", e.getLocalizedMessage());
            }
        }

        if (deviceMasks != null) {
            TinyMessageBus.post(new GotDeviceMasks(deviceMasks));
        }
    }

    /*
    * Customer Cache
    * */
    @Subscribe(mode = Subscribe.Mode.Background, queue = CACHE_TAG)
    public void getCachedDeviceAndLocationOwner(GetCachedDeviceAndLocationOwner request) {

        Customer currentCustomer = CurrentCustomer.getCurrentCustomer();
        if (currentCustomer == null) {
            dispatchError(GeneralError.ErrorLevel.Major, "Cannot find current customer");
            return;
        }

        Device device = DeviceDatabaseService.getDeviceFromId(request.getDeviceId());
        if (device == null || device.location == null) {
            dispatchError(GeneralError.ErrorLevel.Major, "can't find the device or locationResourceURI");
            return;
        }
        Thumbnail thumbnail = ThumbnailDatabaseService.getThumbnailsForDevice(device);

        Location deviceLocation = LocationDatabaseService.getLocationFromResourceUri(device.location);
        if (deviceLocation == null) {
            dispatchError(GeneralError.ErrorLevel.Major, "can't find the location");
            return;
        }

        Customer locationOwner = CustomerDatabaseService.getCustomerFromUri(deviceLocation.locationOwner);
        if (locationOwner == null) {
            dispatchError(GeneralError.ErrorLevel.Major, "can't location owner customer with this resouceURI: " + deviceLocation.locationOwner);
            return;
        }

        int totalNumberOfDevices = DeviceDatabaseService.getAllActivatedDevices().size();
        TinyMessageBus.post(new GotCachedDeviceAndLocationOwner(device, locationOwner, currentCustomer.equals(locationOwner),
                thumbnail, deviceLocation.id, totalNumberOfDevices));
    }

    private void dispatchError(GeneralError.ErrorLevel errorLevel, String errorMessage) {
        TinyMessageBus.post(new GeneralError(errorLevel, errorMessage));
    }

    @Subscribe(mode = Subscribe.Mode.Background, queue = CACHE_TAG)
    public void onCheckPushToken(final PushToken pushToken) {
        final String channel = UAirship.shared().getPushManager().getChannelId();
        if (TextUtils.isEmpty(channel))
            return;

        DeviceToken deviceToken = DeviceTokenDatabaseService.getCurrentToken();
        if (deviceToken != null && deviceToken.token.equalsIgnoreCase(channel) && deviceToken.buildNumber == BuildConfig.VERSION_CODE) {
            return;
        }
        final String token = UAirship.shared().getPushManager().getRegistrationToken();

        DeviceTokenAPIServices.changeDeviceToken(token, channel, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                DeviceTokenDatabaseService.saveToken(channel);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }


    //================================================================================
    // User Tag Caching
    //================================================================================

    @Subscribe(mode = Subscribe.Mode.Background, queue = CACHE_TAG)
    public void onDeleteTag(DeleteTag tag) {
        UserTagsDatabaseService.deleteTag(tag.tag);
    }

    @Subscribe(mode = Subscribe.Mode.Background, queue = CACHE_TAG)
    public void getUserTags(GetTags tag) {

        List<String> userTags = UserTagsDatabaseService.getUserTags();
        List<String> defaultTags = UserTagsDatabaseService.getDefaultTags();
        TinyMessageBus.post(new Tags(defaultTags, userTags));
    }

    @Subscribe(mode = Subscribe.Mode.Background, queue = CACHE_TAG)
    public void insertUser(InsertTag tag) {
        Log.i(CACHE_TAG, "Insert " + tag.override);
        UserTagsDatabaseService.insertTag(tag.tag, tag.override);
    }


    @Subscribe(mode = Subscribe.Mode.Background)
    public void insertVideos(InsertVideos insertVideos) {
        VideoExportDatabaseService.insertVideoExports(insertVideos.videoExports);

        TinyMessageBus.post(new ShareStartedProcessing(insertVideos.entryId));
    }

    @Subscribe(mode = Subscribe.Mode.Background)
    public void updateToken(UpdateToken updateToken) {
        String oldToken = KeyStoreHelper.getToken();

        if (!Utils.isNotProd())
            return;

        if (StringUtils.isNullOrEmpty(oldToken))
            return;

        String refreshToken = KeyStoreHelper.getRefreshToken();
        if (StringUtils.isNullOrEmpty(refreshToken)) {
            try {
                OauthResponse response = OathAuthenticationAPIService.oauthAuthenticationUpdate(oldToken);
                KeyStoreHelper.saveToken(response.accessToken);
                KeyStoreHelper.saveRefreshToken(response.refreshToken);
            } catch (RetrofitError error) {

                Response response = error.getResponse();
                if (response != null) {
                    if (response.getStatus() == 400) {
                        KeyStoreHelper.saveToken(null);
                    }
                }

            }
        }

    }

    @Subscribe(mode = Subscribe.Mode.Background)
    public void changeMembershipPresence(UpdateMembershipPresenceCache presenceCache) {
        MembershipPresence membershipPresence = new MembershipPresence();
        membershipPresence.customerUri = Utils.buildResourceUri(Constants.CUSTOMER_URI, presenceCache.customerId);
        membershipPresence.locationUri = Utils.buildResourceUri(Constants.CUSTOMER_URI, presenceCache.locationId);
        membershipPresence.sendPresenceNotifications = presenceCache.presence;
        MembershipDatabaseService.insertMembershipPresence(presenceCache.context, membershipPresence);
    }
}
