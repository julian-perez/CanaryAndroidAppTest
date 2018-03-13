package is.yranac.canary.services.api;

import is.yranac.canary.Constants;
import is.yranac.canary.messages.NewDeviceSettings;
import is.yranac.canary.messages.UpdateCachedDevice;
import is.yranac.canary.messages.UpdateCachedDeviceName;
import is.yranac.canary.model.DeviceModeSettings;
import is.yranac.canary.model.WatchLiveSessionResponse;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceActivation;
import is.yranac.canary.model.device.DeviceCreate;
import is.yranac.canary.model.device.DeviceHomeModeSettings;
import is.yranac.canary.model.device.DeviceMotionSettings;
import is.yranac.canary.model.device.DeviceNamePatch;
import is.yranac.canary.model.device.DeviceSettings;
import is.yranac.canary.model.device.DeviceSiren;
import is.yranac.canary.model.device.DeviceStreaming;
import is.yranac.canary.model.device.PatchDeviceSettings;
import is.yranac.canary.model.masking.CVMask;
import is.yranac.canary.model.masking.DeviceMasks;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by cumisnic on 8/14/14.
 */
public class DeviceAPIService {


    public static void createDevice(String serial, String locationUri, String name, final Callback<Device> createCallback) {
        DeviceCreate deviceCreate = new DeviceCreate(serial, locationUri, name);
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        DeviceService customerService = restAdapter.create(DeviceService.class);
        customerService.createDevice(deviceCreate, new Callback<Device>() {
            @Override
            public void success(Device device, Response response) {
                TinyMessageBus.post(new UpdateCachedDevice(device));
                createCallback.success(device, response);
            }

            @Override
            public void failure(RetrofitError error) {
                createCallback.failure(error);
            }
        });
    }

    public static void activateDevice(final String resourseUri, String status, String password, final Callback<Device> callback) {
        DeviceActivation deviceActivation = new DeviceActivation(status, password);
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        DeviceService customerService = restAdapter.create(DeviceService.class);
        customerService.activateDevice(resourseUri, deviceActivation, new Callback<Device>() {
            @Override
            public void success(Device device, Response response) {
                TinyMessageBus.post(new UpdateCachedDevice(device));
                callback.success(device, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    public static void changeSirenState(String deviceUri, boolean onOff) {
        String state = onOff ? "on" : "off";
        DeviceSiren deviceSiren = new DeviceSiren(state, deviceUri);

        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        DeviceService deviceService = restAdapter.create(DeviceService.class);
        deviceService.soundDeviceSiren(deviceSiren, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static void changeDeviceName(final String resourceURI, final int deviceId, final String name, final Callback<Void> callback) {
        DeviceNamePatch deviceNamePatch = new DeviceNamePatch(name);
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        DeviceService deviceService = restAdapter.create(DeviceService.class);
        deviceService.changeDeviceName(resourceURI, deviceNamePatch, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                //caching if response was successful
                TinyMessageBus.post(new UpdateCachedDeviceName(deviceId, name));
                callback.success(aVoid, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    public static void changeStreamingState(String deviceUri, boolean isStreaming) {
        DeviceStreaming deviceStreaming = new DeviceStreaming(isStreaming, deviceUri);

        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        DeviceService deviceService = restAdapter.create(DeviceService.class);
        deviceService.changeStreamingState(deviceStreaming, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {

            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    public static void getDeviceByUri(String deviceUri, final Callback<Device> callback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        DeviceService deviceService = restAdapter.create(DeviceService.class);
        deviceService.getDeviceByUri(deviceUri.substring(1), new Callback<Device>() {
            @Override
            public void success(Device device, Response response) {
                TinyMessageBus.post(new UpdateCachedDevice(device));
                callback.success(device, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    public static DeviceSettings getDeviceSettings(int id) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        DeviceService deviceService = restAdapter.create(DeviceService.class);
        DeviceSettings deviceSettings = deviceService.getDeviceSettings(id);
        DeviceDatabaseService.insertDeviceSettings(deviceSettings);
        return deviceSettings;
    }

    public static void changeDeviceSettings(final DeviceSettings deviceSettings) {

        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true, true, true);
        DeviceService deviceService = restAdapter.create(DeviceService.class);
        PatchDeviceSettings patchDeviceSettings = new PatchDeviceSettings(deviceSettings);
        deviceService.changeDeviceSettings(deviceSettings.resourceUri.substring(1), patchDeviceSettings, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                TinyMessageBus.post(new NewDeviceSettings(deviceSettings));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    public static void changeDeviceModeSettings(final DeviceSettings deviceSettings) {
        DeviceDatabaseService.insertDeviceSettings(deviceSettings);
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        DeviceModeSettings modeSettings = new DeviceModeSettings(deviceSettings.homeMode, deviceSettings.nightMode);
        DeviceService deviceService = restAdapter.create(DeviceService.class);
        deviceService.changeDeviceModeSettings(Utils.getIntFromResourceUri(deviceSettings.resourceUri), modeSettings, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                TinyMessageBus.post(new NewDeviceSettings(deviceSettings));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static void changeDeviceMotionSettings(int deviceId, float motionSettings) {
        RestAdapter restAdapter = new RetroFitAdapterFactory().getNewDefaultAdapter(true);
        DeviceMotionSettings modeSettings = new DeviceMotionSettings(motionSettings);
        DeviceService deviceService = restAdapter.create(DeviceService.class);
        deviceService.changeDeviceMotionSettings(deviceId, modeSettings, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static void changeDeviceHomeMode(int deviceId, int mode) {
        RestAdapter restAdapter = new RetroFitAdapterFactory().getNewDefaultAdapter(true);
        DeviceHomeModeSettings modeSettings = new DeviceHomeModeSettings(mode);
        DeviceService deviceService = restAdapter.create(DeviceService.class);
        deviceService.changeDeviceHomeMode(deviceId, modeSettings, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static void saveDeviceMasks(final int deviceId, final DeviceMasks masks, final Callback<Void> callback) {

        //if there's any masks without verteces, it means we're deleting all existing masks
        boolean deleteAll = false;
        for (CVMask mask : masks.deviceMasks)
            if (mask.vertices == null || mask.vertices.size() == 0) {
                deleteAll = true;
                break;
            }
        final boolean _deleteAll = deleteAll;
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        DeviceService deviceService = restAdapter.create(DeviceService.class);
        deviceService.saveDeviceMasks(masks, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                //caching masks
                PreferencesUtils.setDeviceMasks(_deleteAll ? new DeviceMasks() : masks, deviceId);
                callback.success(aVoid, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    public static DeviceMasks getDeviceMasks(final int deviceId) {
        RestAdapter restAdapter = new RetroFitAdapterFactory().getNewDefaultAdapter(true);
        DeviceService deviceService = restAdapter.create(DeviceService.class);
        DeviceMasks masks = deviceService.getDeviceMasks(deviceId + "");

        if (masks != null) {
            if (masks.deviceMasks != null && masks.deviceMasks.size() >= 1) {
                CVMask mask = masks.deviceMasks.get(0);
                if (mask.vertices != null && mask.vertices.size() == 0)
                    return new DeviceMasks();
            }
        }
        return masks;
    }

    public static WatchLiveSessionResponse getWatchLiveSession(String deviceUUID) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        DeviceService deviceService = restAdapter.create(DeviceService.class);
        return deviceService.getWatchLiveSession(deviceUUID);
    }

    public static void deleteDevice(final Device device, final Callback<Void> callback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        DeviceService deviceService = restAdapter.create(DeviceService.class);
        deviceService.deleteDevice(device.resourceUri.substring(1), new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                DeviceDatabaseService.deleteDevice(device.id);
                callback.success(aVoid, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    public interface DeviceService {

        @GET("/{resource_uri}")
        void getDeviceByUri(
                @Path(value = "resource_uri", encode = false)
                        String resourceUri,
                Callback<Device> callback
        );

        @POST(Constants.DEVICE_URI)
        void createDevice(
                @Body DeviceCreate deviceCreate,
                Callback<Device> createCallback
        );

        @DELETE("/{resource_uri}")
        void deleteDevice(
                @Path(value = "resource_uri", encode = false)
                        String resourceUri,
                Callback<Void> callback
        );

        @PATCH("/{resource_uri}")
        void changeDeviceName(
                @Path("resource_uri") String resourceUri,
                @Body DeviceNamePatch deviceNamePatch,
                Callback<Void> callback
        );

        @PATCH("/{resource_uri}")
        void activateDevice(
                @Path("resource_uri") String resourceUri,
                @Body DeviceActivation deviceActivation,
                Callback<Device> callback
        );

        @POST(Constants.COMMANDS_URI)
        void soundDeviceSiren(
                @Body DeviceSiren deviceSiren,
                Callback<Void> callback
        );

        @POST(Constants.COMMANDS_URI)
        void changeStreamingState(
                @Body DeviceStreaming deviceStreaming,
                Callback<Void> createCallback

        );

        @GET(Constants.DEVICE_SETTINGS_URI + "{id}/")
        DeviceSettings getDeviceSettings(
                @Path("id") int id
        );

        @PATCH("/{resource_uri}")
        void changeDeviceSettings(
                @Path(value = "resource_uri", encode = false)
                        String resourceUri,
                @Body PatchDeviceSettings deviceSettings,
                Callback<Void> callback

        );

        @PATCH(Constants.DEVICE_SETTINGS_URI + "{id}/")
        void changeDeviceModeSettings(
                @Path("id") int deviceId,
                @Body DeviceModeSettings deviceSettings,
                Callback<Void> callback

        );

        @PATCH(Constants.DEVICE_SETTINGS_URI + "{id}/")
        void changeDeviceMotionSettings(
                @Path("id") int deviceId,
                @Body DeviceMotionSettings modeSettings,
                Callback<Void> callback);

        @PATCH(Constants.DEVICE_SETTINGS_URI + "{id}/")
        void changeDeviceHomeMode(
                @Path("id") int deviceId,
                @Body DeviceHomeModeSettings modeSettings,
                Callback<Void> callback);

        @GET(Constants.DEVICE_MASKS)
        DeviceMasks getDeviceMasks(
                @Query("device") String deviceId
        );

        @PATCH(Constants.DEVICE_MASKS)
        void saveDeviceMasks(
                @Body DeviceMasks deviceMasks,
                Callback<Void> callback
        );

        @GET(Constants.WATCH_LIVE_SESSION)
        WatchLiveSessionResponse getWatchLiveSession(
                @Query("device_uuid") String uuid);
    }
}
