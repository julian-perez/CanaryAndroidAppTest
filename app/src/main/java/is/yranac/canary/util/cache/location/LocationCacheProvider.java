package is.yranac.canary.util.cache.location;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.messages.LocationCurrentModeChanging;
import is.yranac.canary.messages.NewDeviceSettings;
import is.yranac.canary.messages.TryGeofenceAgain;
import is.yranac.canary.messages.UpdateInsurancePolicyCache;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceSettings;
import is.yranac.canary.model.insurance.InsurancePolicy;
import is.yranac.canary.model.insurance.InsuranceProvider;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.mode.Mode;
import is.yranac.canary.model.mode.ModeCache;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.api.LocationChangeAPIServices;
import is.yranac.canary.services.database.CustomerDatabaseService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.InsuranceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.database.SubscriptionPlanDatabaseService;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.model.mode.ModeCache.armed;

/**
 * Created by Schroeder on 4/18/16.
 */
public class LocationCacheProvider {

    private static final String CACHE_TAG = "LocationCacheProvider";
    private static LocationCacheProvider cacheProvider;

    public static void register() {
        TinyMessageBus.register(getCacheProvider());
    }

    public static void unregister() {
        TinyMessageBus.unregister(getCacheProvider());
    }

    private static LocationCacheProvider getCacheProvider() {
        if (cacheProvider == null)
            cacheProvider = new LocationCacheProvider();
        return cacheProvider;
    }

    @Subscribe(mode = Subscribe.Mode.Background, queue = CACHE_TAG)
    public void updateDataOptIn(UpdateDataOptIn updateDataOptIn) {
        LocationDatabaseService.updateDataOptin(updateDataOptIn.location, updateDataOptIn.dataOptIn);
    }

    @Subscribe(mode = Subscribe.Mode.Background)
    public void updateLocation(UpdateLocation location) {
        LocationDatabaseService.insertLocation(location.location);
        TinyMessageBus.post(new GetLocation(location.location.id));
    }

    @Subscribe(mode = Subscribe.Mode.Background)
    public void updateLocation(GetLocation getLocation) {
        Location location = LocationDatabaseService.getLocationFromId(getLocation.location);
        if (location == null)
            return;
        InsurancePolicy insurancePolicy = InsuranceDatabaseService.getInsurancePolicy(getLocation.location);
        Subscription subscription = SubscriptionPlanDatabaseService.getServicePlanForLocation(getLocation.location);
        List<Device> deviceList = DeviceDatabaseService.getActivatedDevicesAtLocation(getLocation.location);
        List<Customer> customers = CustomerDatabaseService.getCustomersAtLocationWithAvatar(getLocation.location);
        TinyMessageBus.post(new GotLocationData(location, subscription,
                insurancePolicy, deviceList, customers));
    }


    @Subscribe(mode = Subscribe.Mode.Background)
    public void updateNightMode(UpdateNightMode nightMode) {
        LocationDatabaseService.insertNightMode(nightMode.nightMode);
    }

    @Subscribe(mode = Subscribe.Mode.Background, queue = CACHE_TAG)
    public void syncLocation(SyncLocationMode syncLocation) {

        if (syncLocation.mode.equalsIgnoreCase(ModeCache.privacy)) {
            LocationDatabaseService.patchLocationMode(syncLocation.id, syncLocation.isPrivate);
        } else if (syncLocation.mode.equalsIgnoreCase(ModeCache.away)) {
            LocationDatabaseService.patchLocationMode(syncLocation.id, syncLocation.mode);
            for (Device devices : DeviceDatabaseService.getActivatedDevicesAtLocation(syncLocation.id)) {
                DeviceDatabaseService.updateDeviceMode(devices, ModeCache.getMode(armed).id);
            }
        } else {
            LocationDatabaseService.patchLocationMode(syncLocation.id, syncLocation.mode);

            for (Device devices : DeviceDatabaseService.getActivatedDevicesAtLocation(syncLocation.id)) {
                try {
                    DeviceSettings deviceSettings = DeviceAPIService.getDeviceSettings(devices.id);

                    switch (syncLocation.mode) {
                        case ModeCache.home:
                            Mode homeMode = deviceSettings.homeMode;
                            if (homeMode != null) {
                                DeviceDatabaseService.updateDeviceMode(devices, homeMode.id);
                            }
                            break;
                        case ModeCache.night:
                            Mode nightMode = deviceSettings.homeMode;
                            if (nightMode != null) {
                                DeviceDatabaseService.updateDeviceMode(devices, nightMode.id);
                            }
                            break;
                        default:
                            break;
                    }
                } catch (RetrofitError ignored) {
                }

            }
        }
        TinyMessageBus.post(new LocationCurrentModeChanging(false, syncLocation.mode));

    }

    @Subscribe(mode = Subscribe.Mode.Background, queue = CACHE_TAG)
    public void newDeviceMode(NewDeviceSettings newDeviceSettings) {
        int deviceId = Utils.getIntFromResourceUri(newDeviceSettings.deviceSettings.resourceUri);
        DeviceSettings deviceSettings = newDeviceSettings.deviceSettings;
        Device device = DeviceDatabaseService.getDeviceFromId(deviceId);
        Location location = LocationDatabaseService.getLocationFromResourceUri(device.location);
        if (location.currentMode == null)
            return;

        switch (location.currentMode.name) {
            case ModeCache.home:
                DeviceDatabaseService.updateDeviceMode(device, deviceSettings.homeMode.id);
                break;
            case ModeCache.night:
                DeviceDatabaseService.updateDeviceMode(device, deviceSettings.nightMode.id);
                break;
            default:
                break;
        }
        TinyMessageBus.post(new LocationCurrentModeChanging(false, location.currentMode.resourceUri));

    }

    @Subscribe(mode = Subscribe.Mode.Background, queue = CACHE_TAG)
    public void syncInsurancePolicy(UpdateInsurancePolicyCache updateInsurancePolicy) {
        InsuranceProvider insuranceProvider = InsuranceDatabaseService.insuranceProvider(updateInsurancePolicy.insurancePolicy.insuranceProvider);

        InsurancePolicy policy = new InsurancePolicy();
        policy.shareEnabled = updateInsurancePolicy.insurancePolicy.shareEnabled;
        policy.policyNumber = updateInsurancePolicy.insurancePolicy.policyNumber;
        if (policy.policyNumber == null) {
            policy.policyNumber = "";
        }
        policy.insuranceProvider = insuranceProvider;
        InsuranceDatabaseService.insertInsurancePolicy(policy, updateInsurancePolicy.id);

    }


    @Subscribe
    public void tryGeofenceAgain(final TryGeofenceAgain tryGeofenceAgain) {
        if (tryGeofenceAgain.retryCount > 10)
            return;

        final String location = tryGeofenceAgain.locationUri;
        final double lat = tryGeofenceAgain.latitude;
        final double lng = tryGeofenceAgain.longitude;
        final boolean arrival = tryGeofenceAgain.arrival;
        final int currentTry = tryGeofenceAgain.retryCount + 1;
        LocationChangeAPIServices.changeLocation(location, lat, lng, arrival, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                if (arrival) {
                    CustomerDatabaseService.updateCustomerLocation(location);
                } else {
                    CustomerDatabaseService.updateCustomerLocation(null);
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                TinyMessageBus.postDelayed(new TryGeofenceAgain(location, lat, lng, arrival, currentTry), TimeUnit.SECONDS.toMillis(10));
            }
        });
    }

    @Subscribe
    public void getLocations(GetLocations getLocations) {
        Location currentLocation = UserUtils.getLastViewedLocation();
        List<Location> locations = LocationDatabaseService.getLocationList();
        TinyMessageBus.post(new GotLocations(locations, currentLocation));
    }
}
