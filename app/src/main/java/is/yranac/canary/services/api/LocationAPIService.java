package is.yranac.canary.services.api;

import android.content.Context;

import is.yranac.canary.Constants;
import is.yranac.canary.messages.UpdateInsurancePolicyCache;
import is.yranac.canary.model.LocationDataOptin;
import is.yranac.canary.model.location.AddressPatch;
import is.yranac.canary.model.location.ChangeLocationNightModeSchedule;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.location.LocationCreate;
import is.yranac.canary.model.location.LocationGeofencePosition;
import is.yranac.canary.model.location.LocationGeofenceRadius;
import is.yranac.canary.model.location.LocationInsurancePolicy;
import is.yranac.canary.model.location.LocationInsurancePolicy.InsurancePolicyPatch;
import is.yranac.canary.model.location.LocationModePrivatePatch;
import is.yranac.canary.model.location.LocationModeSettingsPatch;
import is.yranac.canary.model.location.LocationNightModeEnabled;
import is.yranac.canary.model.location.LocationNightModeSchedule;
import is.yranac.canary.model.location.LocationPatch;
import is.yranac.canary.model.location.LocationPatchMode;
import is.yranac.canary.model.location.LocationResponse;
import is.yranac.canary.model.location.NightModeResponse;
import is.yranac.canary.model.location.NightModeSchedule;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.geofence.SetUpGeofence;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.cache.location.UpdateLocation;
import is.yranac.canary.util.cache.location.UpdateNightMode;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;


/**
 * Created by Schroeder on 8/8/14.
 */
public class LocationAPIService {

    // ******************************************
    // *** Location Records
    // ******************************************
    // Synchronous call for Location Records
    public static LocationResponse getLocationRecords() throws RetrofitError {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        LocationService entryService = restAdapter.create(LocationService.class);
        return entryService.getLocations();
    }

    // Asynchronous call for Location Record Update
    public static void patchLocation(String locationUri, LocationPatch locationPatch, Callback<Void> locationPatchCallback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        LocationService locationService = restAdapter.create(LocationService.class);
        locationService.updateLocationAsync(Utils.getIntFromResourceUri(locationUri), locationPatch, locationPatchCallback);
    }

    // Asynchronous call for Location Record Update
    public static void patchAddress(int locationUri, AddressPatch locationPatch, Callback<Void> locationPatchCallback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        LocationService locationService = restAdapter.create(LocationService.class);
        locationService.updateAddressAsync(locationUri, locationPatch, locationPatchCallback);
    }

    // Asynchronous call for Location Record Create
    public static void createLocation(LocationCreate locationCreate, Callback<Void> locationPostCallback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        LocationService locationService = restAdapter.create(LocationService.class);
        locationService.createLocationAsync(locationCreate, locationPostCallback);
    }

    // Asynchronous call for Location Record Update Mode
    public static void changeLocationCurrentMode(String locationUri, String modeUri, Callback<Void> locationPatchCallback) {
        LocationPatchMode locationPatchMode = new LocationPatchMode(modeUri);
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        LocationService locationService = restAdapter.create(LocationService.class);
        locationService.changeLocationCurrentStateAsync(Utils.getIntFromResourceUri(locationUri), locationPatchMode, locationPatchCallback);
    }

    public static void updateLocationModeSettingsAsync(int locationId, boolean onOff, Callback<Void> locationPatchCallback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);

        LocationModeSettingsPatch modeSettingsPatch = new LocationModeSettingsPatch(onOff);

        LocationService locationService = restAdapter.create(LocationService.class);
        locationService.updateLocationModeSettingsAsync(locationId, modeSettingsPatch, locationPatchCallback);
    }

    public static void changeDataOptIn(int locationId, boolean optIn, Callback<Void> locationPatchCallback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        LocationDataOptin modeSettingsPatch = new LocationDataOptin(optIn);
        LocationService locationService = restAdapter.create(LocationService.class);
        locationService.updateDataOptIn(locationId, modeSettingsPatch, locationPatchCallback);

    }

    public static void changePrivacyMode(int locationId, boolean isPrivate, Callback<Void> locationPatchCallback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        LocationModePrivatePatch modeSettingsPatch = new LocationModePrivatePatch(isPrivate);
        LocationService locationService = restAdapter.create(LocationService.class);
        locationService.privacyModePatch(locationId, modeSettingsPatch, locationPatchCallback);

    }

    public static NightModeResponse getNightModeForLocation() {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        LocationService locationService = restAdapter.create(LocationService.class);
        return locationService.getNightMode();
    }

    public static void createNightMode(int locationId, final NightModeSchedule nightMode) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        LocationService locationService = restAdapter.create(LocationService.class);
        final String locationUri = Utils.buildResourceUri(Constants.LOCATIONS_URI, locationId);
        nightMode.location = locationUri;
        TinyMessageBus.post(new UpdateNightMode(nightMode));

        if (nightMode.id == 0) {
            locationService.createNightSchedule(new LocationNightModeSchedule(locationUri, nightMode.startTime, nightMode.endTime)
                    , new Callback<Void>() {
                        @Override
                        public void success(Void aVoid, Response response) {
                            String uri = "";
                            for (Header header : response.getHeaders()) {
                                if ("location".equalsIgnoreCase(header.getName())) {
                                    uri = header.getValue()
                                            .substring(Constants.BASE_URL.length());
                                    break;
                                }
                            }
                            nightMode.id = Utils.getIntFromResourceUri(uri);
                            TinyMessageBus.post(new UpdateNightMode(nightMode));
                        }

                        @Override
                        public void failure(RetrofitError error) {
                        }
                    });
        } else {
            locationService.patchNightSchedule(nightMode.id, new ChangeLocationNightModeSchedule(nightMode.startTime, nightMode.endTime)
                    , new Callback<Void>() {
                        @Override
                        public void success(Void aVoid, Response response) {
                        }

                        @Override
                        public void failure(RetrofitError error) {
                        }
                    });
        }
    }

    public static void setNightModeEnabled(final Location location, int locationId, final boolean checked) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        LocationService locationService = restAdapter.create(LocationService.class);
        if (location != null) {
            location.nightModeEnabled = checked;
            TinyMessageBus.post(new UpdateLocation(location));
        }
        locationService.changeNightModeEnabled(locationId, new LocationNightModeEnabled(checked), new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static void updateInsurancePolicy(final int id, final InsurancePolicyPatch insurancePolicy) throws RetrofitError {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        LocationService locationService = restAdapter.create(LocationService.class);
        LocationInsurancePolicy locationInsurancePolicy = new LocationInsurancePolicy(insurancePolicy);
        locationService.patchInsurancePolicy(id, locationInsurancePolicy, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                TinyMessageBus.post(new UpdateInsurancePolicyCache(id, insurancePolicy));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    public static void deleteLocation(final int id, final Callback<Void> callback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        LocationService locationService = restAdapter.create(LocationService.class);
        locationService.deleteLocation(id, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {

                LocationDatabaseService.deleteLocation(id);
                callback.success(aVoid, response);
            }

            @Override
            public void failure(RetrofitError error) {

                callback.failure(error);
            }
        });

    }

    public static void changeGeofencePosition(final Context context, final Location location, final double lat, final double lng) {

        LocationGeofencePosition locationChange = new LocationGeofencePosition(lat, lng);
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        LocationService locationChangeService = restAdapter.create(LocationService.class);

        location.lat = lat;
        location.lng = lng;

        locationChangeService.changeGeofencePosition(location.id, locationChange, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                TinyMessageBus.post(new UpdateLocation(location));
                SetUpGeofence.startService(context.getApplicationContext());
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });

    }

    public static void changeGeofenceRadius(final Context context, final Location location, int geofenceRadius) {

        location.geofenceRadius = geofenceRadius;
        LocationGeofenceRadius locationChange = new LocationGeofenceRadius(geofenceRadius);
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        LocationService locationChangeService = restAdapter.create(LocationService.class);
        locationChangeService.changeGeofenceRadius(location.id, locationChange, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                TinyMessageBus.post(new UpdateLocation(location));
                SetUpGeofence.startService(context.getApplicationContext());

            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });

    }


    public interface LocationService {

        @GET(Constants.LOCATIONS_URI + "{id}/")
        Location getLocation(
                @Path("id") int id
        );


        @DELETE(Constants.LOCATIONS_URI + "{id}/")
        void deleteLocation(
                @Path("id") int id,
                Callback<Void> locationPostCallback
        );

        @GET(Constants.LOCATIONS_URI)
        LocationResponse getLocations();

        @POST(Constants.LOCATIONS_URI)
        void createLocationAsync(
                @Body LocationCreate locationPost,
                Callback<Void> locationPostCallback
        );

        @PATCH(Constants.LOCATIONS_URI + "{id}/")
        void updateAddressAsync(
                @Path("id") int id,
                @Body AddressPatch addressPatch,
                Callback<Void> locationPatchCallback
        );

        @PATCH(Constants.LOCATIONS_URI + "{id}/")
        void updateLocationAsync(
                @Path("id") int id,
                @Body LocationPatch locationPatch,
                Callback<Void> locationPatchCallback
        );

        @PATCH(Constants.LOCATIONS_URI + "{id}/")
        void changeLocationCurrentStateAsync(
                @Path("id") int id,
                @Body LocationPatchMode locationPatchMode,
                Callback<Void> locationPatchCallback
        );

        @PATCH(Constants.LOCATIONS_URI + "{id}/")
        void updateLocationModeSettingsAsync(
                @Path("id") int id,
                @Body LocationModeSettingsPatch locationPatch,
                Callback<Void> locationPatchCallback
        );

        @PATCH(Constants.LOCATIONS_URI + "{id}/")
        void updateDataOptIn(
                @Path("id") int id,
                @Body LocationDataOptin locationPatch,
                Callback<Void> locationPatchCallback
        );


        @PATCH(Constants.LOCATIONS_URI + "{id}/")
        void privacyModePatch(
                @Path("id") int id,
                @Body LocationModePrivatePatch locationPatch,
                Callback<Void> locationPatchCallback
        );

        @POST(Constants.NIGHT_MODE_URI)
        void createNightSchedule(
                @Body LocationNightModeSchedule nightModeSchedule,
                Callback<Void> locationPatchCallback
        );

        @PATCH(Constants.LOCATIONS_URI + "{id}/")
        void changeNightModeEnabled(
                @Path("id") int id,
                @Body LocationNightModeEnabled nightModeEnabled,
                Callback<Void> locationPatchCallback
        );

        @PATCH(Constants.NIGHT_MODE_URI + "{id}/")
        void patchNightSchedule(
                @Path("id") int id,
                @Body ChangeLocationNightModeSchedule nightModeSchedule,
                Callback<Void> locationPatchCallback
        );

        @GET(Constants.NIGHT_MODE_URI)
        NightModeResponse getNightMode();


        @PATCH(Constants.LOCATIONS_URI + "{id}/")
        void patchInsurancePolicy(
                @Path("id") int id,
                @Body LocationInsurancePolicy locationPatch,
                Callback<Void> locationPatchCallback
        );

        @PATCH(Constants.LOCATIONS_URI + "{id}/")
        void changeGeofencePosition(
                @Path("id") int id,
                @Body LocationGeofencePosition locationGeofencePosition,
                Callback<Void> locationPatchCallback
        );

        @PATCH(Constants.LOCATIONS_URI + "{id}/")
        void changeGeofenceRadius(
                @Path("id") int id,
                @Body LocationGeofenceRadius locationGeofenceRadius,
                Callback<Void> locationPatchCallback
        );
    }
}
