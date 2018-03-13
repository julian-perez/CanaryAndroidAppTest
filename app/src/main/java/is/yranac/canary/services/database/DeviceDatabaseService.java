package is.yranac.canary.services.database;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.Constants;
import is.yranac.canary.contentproviders.CanaryDeviceContentProvider;
import is.yranac.canary.contentproviders.CanaryDeviceSettingsContentProvider;
import is.yranac.canary.messages.UpdateDeviceSettings;
import is.yranac.canary.messages.watchlive.DeviceOnOffline;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceSettings;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.mode.Mode;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.TutorialUtil;
import is.yranac.canary.util.Utils;

public class DeviceDatabaseService extends BaseDatabaseService {

    public static final String LOG_TAG = "DeviceDatabaseService";
    public static final String DEVICE_UPDATED_FAILED = "DEVICE_UPDATED_FAILED";


    public static void insertOrUpdateDevice(Device device) {

        String where = CanaryDeviceContentProvider.COLUMN_ID + " == ?";
        String whereArgs[] = {String.valueOf(device.id)};

        Cursor cursor = contentResolver.query(CanaryDeviceContentProvider.CONTENT_URI, null, where, whereArgs, null);
        ContentValues values = createContentValuesFromDevice(device);

        if (cursor == null)
            return;
        if (cursor.moveToFirst()) {
            Device oldDevice = getDeviceFromCursor(cursor);
            checkIfDeviceFailedOTA(device, oldDevice);
            checkIfDeviceOffline(device, oldDevice);
            contentResolver.update(CanaryDeviceContentProvider.CONTENT_URI, values, where, whereArgs);
        } else {
            contentResolver.insert(CanaryDeviceContentProvider.CONTENT_URI, values);

        }

        cursor.close();
    }

    private static void checkIfDeviceOffline(Device device, Device deviceFromCursor) {
        if (device.isOnline != deviceFromCursor.isOnline) {
            TinyMessageBus.post(new DeviceOnOffline(device.serialNumber, device.isOnline));
        }
    }

    private static void checkIfDeviceFailedOTA(Device device, Device deviceFromCursor) {
        if (!deviceFromCursor.failedOTA() && device.failedOTA() && getAllActivatedDevices().size() > 0) {
            Intent newIntent = new Intent();
            newIntent.setAction(DEVICE_UPDATED_FAILED);
            newIntent.putExtra("device_uri", device.resourceUri);
            CanaryApplication.getContext().sendBroadcast(newIntent);
        }
    }

    public static void updateDevicesAtLocation(Location location) {
        updateDevicesAtLocation(location.devices, location.id);
    }

    public static void updateDevicesAtLocation(List<Device> updatedDeviceList, int locationId) {
        List<Device> currentDeviceList = getAllCachedDevicesAtLocation(locationId);
        HashMap<String, Device> currentPendingDevices = new HashMap();

        // process the deletions, if any
        for (Device device : currentDeviceList) {
            boolean found = false;
            for (Device device2 : updatedDeviceList) {
                if (device2.id == device.id) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                deleteDevice(device.id);
            } else if ("pending".equalsIgnoreCase(device.activationStatus)) {
                currentPendingDevices.put(device.id + "", device);
            }
        }

        Device activatedDevice = null;
        int activatedDevices = 0;
        for (Device device : updatedDeviceList) {

            insertOrUpdateDevice(device);


            if (device.deviceActivated) {
                activatedDevices++;
                TinyMessageBus.post(new UpdateDeviceSettings(device));

                if (currentPendingDevices.containsKey(device.id + "")) {
                    if (activatedDevice == null)
                        activatedDevice = device;
                    else {
                        //this is a WTF moment, user has more than ONE pending device become
                        // activated in the same call for the same location. Better not do anything
                        activatedDevice = null;
                    }
                }
            }
        }

        if (activatedDevice != null && activatedDevices >= 2) {
            TutorialUtil.checkNewDeviceTutorials(activatedDevice);
        }

    }

    public static void deleteDevice(int deviceId) {
        String where = CanaryDeviceContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(deviceId)};

        contentResolver.delete(CanaryDeviceContentProvider.CONTENT_URI, where, whereArgs);
    }


    public static List<Device> getActivatedDevicesAtLocation(int locationId) {
        List<Device> devices = new ArrayList<>();

        String where = CanaryDeviceContentProvider.COLUMN_LOCATION_ID + " == ?" +
                " AND " + CanaryDeviceContentProvider.COLUMN_DEVICE_ACTIVATED + " == ?";

        String[] whereArgs = new String[]{String.valueOf(locationId), "1"};

        Cursor cursor = contentResolver.query(CanaryDeviceContentProvider.CONTENT_URI, null, where, whereArgs, CanaryDeviceContentProvider.COLUMN_ID + " ASC");

        if (cursor == null)
            return devices;
        while (cursor.moveToNext()) {
            devices.add(getDeviceFromCursor(cursor));
        }

        cursor.close();
        return devices;
    }


    public static List<Device> getAllDevicesAtLocation(int locationId) {
        List<Device> devices = new ArrayList<>();


        String where = CanaryDeviceContentProvider.COLUMN_LOCATION_ID + " == ?";
        where += " AND ";
        where += CanaryDeviceContentProvider.COLUMN_ACTIVATION_STATUS + " != ?";

        String[] whereArgs = new String[]{String.valueOf(locationId), "deactivated"};

        Cursor cursor = contentResolver.query(CanaryDeviceContentProvider.CONTENT_URI, null, where, whereArgs, CanaryDeviceContentProvider.COLUMN_ID + " ASC");

        if (cursor == null)
            return devices;

        while (cursor.moveToNext()) {
            devices.add(getDeviceFromCursor(cursor));
        }

        cursor.close();

        return devices;
    }

    public static List<Device> getAllCachedDevicesAtLocation(int location) {
        List<Device> devices = new ArrayList<>();


        String where = CanaryDeviceContentProvider.COLUMN_LOCATION_ID + " == ?";

        String[] whereArgs = new String[]{String.valueOf(location)};

        Cursor cursor = contentResolver.query(CanaryDeviceContentProvider.CONTENT_URI, null, where, whereArgs, CanaryDeviceContentProvider.COLUMN_ID + " ASC");

        if (cursor == null)
            return devices;

        while (cursor.moveToNext()) {
            devices.add(getDeviceFromCursor(cursor));
        }

        cursor.close();

        return devices;
    }


    // *****************************************************
    // *** Get Device from Id
    // *****************************************************
    public static Device getDeviceFromId(long id) {
        Device device = null;

        String where = CanaryDeviceContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(id)};

        Cursor cursor = contentResolver.query(CanaryDeviceContentProvider.CONTENT_URI, null, where, whereArgs, null);

        if (cursor == null)
            return null;
        if (cursor.moveToFirst()) {
            device = getDeviceFromCursor(cursor);
        }
        cursor.close();

        return device;
    }

    // *****************************************************
    // *** Get Device from Uri
    // *****************************************************
    public static Device getDeviceFromResourceUri(String resourceUri) {

        if (resourceUri == null) {
            return null;
        }

        return getDeviceFromResourceUUID(Utils.getStringFromResourceUri(resourceUri));

    }

    public static Device getDeviceFromResourceUUID(String uuid) {

        String where = CanaryDeviceContentProvider.COLUMN_UUID + " == ?";
        String[] whereArgs = {uuid};

        Cursor cursor = contentResolver.query(CanaryDeviceContentProvider.CONTENT_URI, null, where, whereArgs, null);

        Device device = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                device = getDeviceFromCursor(cursor);

            }
            cursor.close();
        }

        return device;
    }


    public static ContentValues createContentValuesFromDevice(Device device) {
        ContentValues values = new ContentValues();

        values.put(CanaryDeviceContentProvider.COLUMN_DEVICE_ID, device.serialNumber);
        values.put(CanaryDeviceContentProvider.COLUMN_APPLICATION_VERSION, device.applicationVersion == null ? "" : device.applicationVersion);
        values.put(CanaryDeviceContentProvider.COLUMN_ID, device.id);
        values.put(CanaryDeviceContentProvider.COLUMN_IMAGE_URL, device.imageUrl == null ? "" : device.imageUrl);
        values.put(CanaryDeviceContentProvider.COLUMN_DEVICE_ACTIVATED, device.deviceActivated);
        values.put(CanaryDeviceContentProvider.COLUMN_LOCATION_ID, Utils.getIntFromResourceUri(device.location));
        values.put(CanaryDeviceContentProvider.COLUMN_MODE, Utils.getIntFromResourceUri(device.mode));
        values.put(CanaryDeviceContentProvider.COLUMN_NAME, device.name);
        values.put(CanaryDeviceContentProvider.COLUMN_ONLINE, device.isOnline);
        values.put(CanaryDeviceContentProvider.COLUMN_UUID, device.uuid);
        values.put(CanaryDeviceContentProvider.COLUMN_SIREN_ACTIVE, device.sirenActive);
        values.put(CanaryDeviceContentProvider.COLUMN_ACTIVATION_STATUS, device.activationStatus);
        values.put(CanaryDeviceContentProvider.COLUMN_UPLOADER_ACTIVE, device.uploaderActive);
        values.put(CanaryDeviceContentProvider.COLUMN_DEVICE_ACTIVATED, device.deviceActivated);
        values.put(CanaryDeviceContentProvider.COLUMN_OTA_STATUS, device.ota_status);

        int deviceTypeId;
        String name;
        if (device.deviceType != null) {
            deviceTypeId = device.deviceType.id;
            switch (deviceTypeId) {
                case DeviceType.CANARY_AIO:
                    name = "Canary All-in-One";
                    break;
                case DeviceType.FLEX:
                    name = "Canary Flex";
                    break;
                case DeviceType.CANARY_VIEW:
                    name = "Canary View";
                    break;
                default:
                    name = device.deviceType.name;
                    break;
            }
        } else {
            deviceTypeId = 1;
            name = "Canary";
        }
        values.put(CanaryDeviceContentProvider.COLUMN_DEVICE_TYPE, deviceTypeId);
        values.put(CanaryDeviceContentProvider.COLUMN_DEVICE_TYPE_NAME, name);

        return values;
    }


    // *****************************************************
    // *** Get Device from Cursor
    // *****************************************************
    public static Device getDeviceFromCursor(Cursor cursor) {
        Device device = new Device();
        device.serialNumber = cursor.getString(cursor.getColumnIndex(CanaryDeviceContentProvider.COLUMN_DEVICE_ID));
        device.uuid = cursor.getString(cursor.getColumnIndex(CanaryDeviceContentProvider.COLUMN_UUID));
        device.applicationVersion = cursor.getString(cursor.getColumnIndex(CanaryDeviceContentProvider.COLUMN_APPLICATION_VERSION));
        device.id = cursor.getInt(cursor.getColumnIndex(CanaryDeviceContentProvider.COLUMN_ID));
        device.deviceActivated = cursor.getInt(cursor.getColumnIndex(CanaryDeviceContentProvider.COLUMN_DEVICE_ACTIVATED)) > 0;
        device.imageUrl = cursor.getString(cursor.getColumnIndex(CanaryDeviceContentProvider.COLUMN_IMAGE_URL));
        int locationId = cursor.getInt(cursor.getColumnIndex(CanaryDeviceContentProvider.COLUMN_LOCATION_ID));
        device.location = Utils.buildResourceUri(Constants.LOCATIONS_URI, locationId);
        String modeId = cursor.getString(cursor.getColumnIndex(CanaryDeviceContentProvider.COLUMN_MODE));
        device.mode = Utils.buildResourceUri(Constants.MODES_URI, modeId);
        device.name = cursor.getString(cursor.getColumnIndex(CanaryDeviceContentProvider.COLUMN_NAME));
        device.isOnline = cursor.getInt(cursor.getColumnIndex(CanaryDeviceContentProvider.COLUMN_ONLINE)) > 0;
        device.resourceUri = Utils.buildResourceUri(Constants.DEVICE_URI, device.uuid);
        device.sirenActive = cursor.getInt(cursor.getColumnIndex(CanaryDeviceContentProvider.COLUMN_SIREN_ACTIVE)) > 0;
        device.uploaderActive = cursor.getInt(cursor.getColumnIndex(CanaryDeviceContentProvider.COLUMN_UPLOADER_ACTIVE)) > 0;
        device.ota_status = cursor.getString(cursor.getColumnIndex(CanaryDeviceContentProvider.COLUMN_OTA_STATUS));
        device.activationStatus = cursor.getString(cursor.getColumnIndex(CanaryDeviceContentProvider.COLUMN_ACTIVATION_STATUS));
        DeviceType deviceType = new DeviceType();
        deviceType.id = cursor.getInt(cursor.getColumnIndex(CanaryDeviceContentProvider.COLUMN_DEVICE_TYPE));
        deviceType.name = cursor.getString(cursor.getColumnIndex(CanaryDeviceContentProvider.COLUMN_DEVICE_TYPE_NAME));
        device.deviceType = deviceType;
        return device;
    }

    public static boolean updateDeviceName(String deviceID, String deviceName) {
        String where = CanaryDeviceContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {deviceID};

        ContentValues vals = new ContentValues();
        vals.put(CanaryDeviceContentProvider.COLUMN_NAME, deviceName);

        int rows = contentResolver.update(CanaryDeviceContentProvider.CONTENT_URI, vals, where, whereArgs);

        //there should be exactly 1 raw modified. If it's something else, could be concerning
        return rows == 1;
    }

    public static List<Device> getAllDevices() {
        List<Device> devices = new ArrayList<>();

        String where = CanaryDeviceContentProvider.COLUMN_ACTIVATION_STATUS + " != ?";
        where += " AND ";
        where += CanaryDeviceContentProvider.COLUMN_OTA_STATUS + " != ?";

        String[] whereArgs = {"deactivated", "inactive"};

        Cursor cursor = contentResolver.query(CanaryDeviceContentProvider.CONTENT_URI, null,
                where, whereArgs, null);
        if (cursor.moveToFirst()) {
            do {
                devices.add(getDeviceFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return devices;
    }

    public static List<Device> getAllActivatedDevices() {
        List<Device> devices = new ArrayList<>();

        String where = CanaryDeviceContentProvider.COLUMN_DEVICE_ACTIVATED + " == ?";
        String[] whereArgs = {"1"};

        Cursor cursor = contentResolver.query(CanaryDeviceContentProvider.CONTENT_URI, null, where, whereArgs, null);
        if (cursor == null)
            return devices;
        if (cursor.moveToFirst()) {
            do {
                devices.add(getDeviceFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return devices;
    }

    public static void insertDeviceSettings(DeviceSettings deviceSettings) {

        ContentValues values = createContentValuesFromDeviceSettings(deviceSettings);
        contentResolver.insert(CanaryDeviceSettingsContentProvider.CONTENT_URI, values);

    }

    public static DeviceSettings getDeviceSettingsForDevice(int deviceId) {

        String where = CanaryDeviceSettingsContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(deviceId)};
        Cursor cursor = contentResolver.query(CanaryDeviceSettingsContentProvider.CONTENT_URI, null, where, whereArgs, null);
        if (cursor == null)
            return null;

        try {
            if (cursor.moveToFirst()) {
                return getDeviceSettingsFromCursor(cursor);
            }

            return null;
        } finally {
            cursor.close();
        }
    }

    public static ContentValues createContentValuesFromDeviceSettings(DeviceSettings device) {
        ContentValues values = new ContentValues();
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_AIR_QUALITY_THRESHOLD, device.airQualityThreshold);
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_DETECTION_THRESHOLD, device.detectionThreshold);
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_HUMIDITY_THRESHOLD_MAX, device.humidityThresholdMax);
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_HUMIDITY_THRESHOLD_MIN, device.humidityThresholdMin);
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_SEND_CONNECTIVITY_NOTIFICATIONS, device.sendConnectivityNotifications);
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_SEND_HOMEHEALTH_NOTIFICATIONS, device.sendHomehealthNotifications);
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_TEMP_THRESHOLD_MAX, device.tempThresholdMax);
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_TEMP_THRESHOLD_MIN, device.tempThresholdMin);
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_SEND_HUMIDITY_MAX, device.sendHumidityMaxNotifications);
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_SEND_HUMIDITY_MIN, device.sendHumidityMinNotifications);
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_SEND_TEMP_MAX, device.sendTempMaxNotifications);
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_SEND_TEMP_MIN, device.sendTempMinNotifications);
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_SEND_AIR_QUALITY_NOTIFICATIONS, device.sendAirQualityNotifications);
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_SEND_POWER_SOURCE_NOTIFICATIONS, device.sendPowerSourceNotifications);
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_SEND_BATTERY_FULL_NOTIFICATIONS, device.sendBatteryFullNotifications);
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_BATTERY_SAVER_USE, device.useBatterySaver);
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_PIR_RECORDING_RANGE, device.pirRecordingRange);
        if (device.created != null)
            values.put(CanaryDeviceSettingsContentProvider.COLUMN_CREATED, device.created.getTime());

        if (device.updated != null)
            values.put(CanaryDeviceSettingsContentProvider.COLUMN_UPDATED, device.updated.getTime());

        Mode homeMode = device.homeMode;
        if (homeMode != null) {
            values.put(CanaryDeviceSettingsContentProvider.COLUMN_HOME_MODE, homeMode.id);
        }

        Mode nightMode = device.nightMode;
        if (nightMode != null) {
            values.put(CanaryDeviceSettingsContentProvider.COLUMN_NIGHT_MODE, nightMode.id);
        }

        int id = Utils.getIntFromResourceUri(device.resourceUri);
        values.put(CanaryDeviceSettingsContentProvider.COLUMN_ID, id);


        values.put(CanaryDeviceSettingsContentProvider.COLUMN_BACKPACK_DATA_USAGE_START_DAY, device.backpackDataUsageStartDay);
        return values;
    }


    public static DeviceSettings getDeviceSettingsFromCursor(Cursor cursor) {
        DeviceSettings deviceSettings = new DeviceSettings();

        deviceSettings.airQualityThreshold = cursor.getFloat(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_AIR_QUALITY_THRESHOLD));
        deviceSettings.detectionThreshold = cursor.getFloat(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_DETECTION_THRESHOLD));
        deviceSettings.humidityThresholdMax = cursor.getFloat(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_HUMIDITY_THRESHOLD_MAX));
        deviceSettings.humidityThresholdMin = cursor.getFloat(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_HUMIDITY_THRESHOLD_MIN));
        deviceSettings.sendConnectivityNotifications = cursor.getInt(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_SEND_CONNECTIVITY_NOTIFICATIONS)) > 0;
        deviceSettings.sendHomehealthNotifications = cursor.getInt(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_SEND_HOMEHEALTH_NOTIFICATIONS)) > 0;
        deviceSettings.tempThresholdMax = cursor.getFloat(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_TEMP_THRESHOLD_MAX));
        deviceSettings.tempThresholdMin = cursor.getFloat(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_TEMP_THRESHOLD_MIN));
        deviceSettings.sendHumidityMaxNotifications = cursor.getInt(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_SEND_HUMIDITY_MAX)) > 0;
        deviceSettings.sendHumidityMinNotifications = cursor.getInt(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_SEND_HUMIDITY_MIN)) > 0;
        deviceSettings.sendTempMaxNotifications = cursor.getInt(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_SEND_TEMP_MAX)) > 0;
        deviceSettings.sendTempMinNotifications = cursor.getInt(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_SEND_TEMP_MIN)) > 0;
        deviceSettings.sendAirQualityNotifications = cursor.getInt(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_SEND_AIR_QUALITY_NOTIFICATIONS)) > 0;
        deviceSettings.sendPowerSourceNotifications = cursor.getInt(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_SEND_POWER_SOURCE_NOTIFICATIONS)) > 0;
        deviceSettings.sendBatteryFullNotifications = cursor.getInt(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_SEND_BATTERY_FULL_NOTIFICATIONS)) > 0;
        deviceSettings.useBatterySaver = cursor.getInt(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_BATTERY_SAVER_USE)) > 0;
        deviceSettings.pirRecordingRange = cursor.getInt(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_PIR_RECORDING_RANGE));

        int homeModeId = cursor.getInt(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_HOME_MODE));
        int nightModeId = cursor.getInt(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_NIGHT_MODE));

        if (homeModeId != 0) {
            Mode mode = new Mode();
            mode.id = homeModeId;
            deviceSettings.homeMode = mode;

        }

        if (nightModeId != 0) {
            Mode mode = new Mode();
            mode.id = nightModeId;
            deviceSettings.nightMode = mode;
        }


        long createdTimeStamp = cursor.getLong(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_CREATED));
        long updatedTimeStamp = cursor.getLong(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_UPDATED));
        long modifiedTimeStamp = cursor.getLong(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_LAST_MODIFIED));

        deviceSettings.created = new Date(createdTimeStamp);
        deviceSettings.updated = new Date(updatedTimeStamp);
        deviceSettings.lastModified = new Date(modifiedTimeStamp);

        int id = cursor.getInt(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_ID));
        deviceSettings.resourceUri = Utils.buildResourceUri(Constants.DEVICE_SETTINGS_URI, id);

        deviceSettings.backpackDataUsageStartDay = cursor.getInt(cursor.getColumnIndex(CanaryDeviceSettingsContentProvider.COLUMN_BACKPACK_DATA_USAGE_START_DAY));

        return deviceSettings;
    }

    public static void updateDeviceMode(Device device, int mode) {
        String where = CanaryDeviceSettingsContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(device.id)};

        ContentValues contentValues = new ContentValues();
        contentValues.put(CanaryDeviceContentProvider.COLUMN_MODE, mode);

        contentResolver.update(CanaryDeviceContentProvider.CONTENT_URI, contentValues, where, whereArgs);
    }

}
