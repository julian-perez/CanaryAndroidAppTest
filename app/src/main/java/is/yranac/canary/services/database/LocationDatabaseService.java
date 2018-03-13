package is.yranac.canary.services.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.Constants;
import is.yranac.canary.contentproviders.CanaryLocationContentProvider;
import is.yranac.canary.contentproviders.CanaryNightModeScheduleContentProvider;
import is.yranac.canary.messages.CurrentLocationUpdated;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.model.emergencycontacts.EmergencyContact;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.location.NightModeSchedule;
import is.yranac.canary.model.mode.ModeCache;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.util.DateUtil;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 8/8/14.
 */
public class LocationDatabaseService extends BaseDatabaseService {
    private static final String TAG = "LocationDatabaseService";


    public static void rebuildLocations(Context context, List<Location> locations) {

        checkIfLocationsExists(context, locations);

        if (locations == null || locations.size() == 0)
            return;

        int lastViewedLocationId = UserUtils.getLastViewedLocationId();
        boolean lastViewedLocationIdIsGood = false;


        for (Location location : locations) {
            LocationDatabaseService.insertLocation(location);


            LocationDatabaseService.replaceLocationRelatedData(context, location);

            List<Device> newDevices = DeviceDatabaseService.getActivatedDevicesAtLocation(location.id);

            if (location.id == lastViewedLocationId && newDevices.size() != 0) {
                lastViewedLocationIdIsGood = true;
                TinyMessageBus.post(new CurrentLocationUpdated());
            }
        }


        if (!lastViewedLocationIdIsGood) {
            lastViewedLocationId = locations.get(0).id;
            UserUtils.setLastViewedLocationId(lastViewedLocationId);


            for (Location location : locations) {
                List<Device> devices = DeviceDatabaseService.getActivatedDevicesAtLocation(location.id);

                if (devices.size() != 0) {
                    lastViewedLocationId = location.id;
                    break;
                }
            }
        }

        UserUtils.setLastViewedLocationId(lastViewedLocationId);
    }

    public static void insertLocation(Location location) {

        ContentValues values = createContentValuesFromLocation(location);
        String where = CanaryLocationContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(location.id)};
        if (contentResolver.update(CanaryLocationContentProvider.CONTENT_URI, values, where, whereArgs) == 0) {
            contentResolver.insert(CanaryLocationContentProvider.CONTENT_URI, values);
        }
    }

    /**
     * Check locations in the database have been deleted on the API
     *
     * @param context
     * @param locations the location returned from the API
     */
    private static void checkIfLocationsExists(Context context, List<Location> locations) {
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = {CanaryLocationContentProvider.COLUMN_ID};
        Cursor cursor = contentResolver.query(CanaryLocationContentProvider.CONTENT_URI, projection, null, null, null);
        if (cursor == null)
            return;
        if (cursor.moveToFirst()) {
            do {
                int locationId = cursor.getInt(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_ID));

                boolean found = false;
                for (Location location : locations) {
                    if (location.id == locationId) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    deleteLocation(locationId);
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    /**
     * Delete a location and all associated data
     *
     * @param locationId the
     */
    public static void deleteLocation(int locationId) {

        String where = CanaryLocationContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(locationId)};
        contentResolver.delete(CanaryLocationContentProvider.CONTENT_URI, where, whereArgs);
    }

    /**
     * Replace all the information that is associated with a given location
     *
     * @param context
     * @param location Location object whose related data needs to be updated
     */
    public static void replaceLocationRelatedData(Context context, Location location) {

        DeviceDatabaseService.updateDevicesAtLocation(location);

        CustomerDatabaseService.deleteCustomerLocationLinksAtLocation(context, location.id);
        List<Customer> customers = location.customers;
        for (Customer customer : customers) {
            CustomerDatabaseService.addCustomerLocationLink(context,
                    Utils.getIntFromResourceUri(location.resourceUri), Utils.getIntFromResourceUri(customer.resourceUri));
        }
        CustomerDatabaseService.insertCustomersAtLocation(location.customers);


        InsuranceDatabaseService.insertInsurancePolicy(location.insurancePolicy, location.id);

    }

    /**
     * Check if the database has emergency contact information for a particular location
     * <p/>
     * This is in place so that we limit the number of times that we call the
     * Google Places API to limit the probability that we will hit out limit
     **/

    public static Location checkIfLocationHasEmergencyContacts() {
        if (Utils.isDemo())
            return null;

        for (Location location : LocationDatabaseService.getLocationList()) {
            if (DeviceDatabaseService.getActivatedDevicesAtLocation(location.id).size() > 0) {
                Map<EmergencyContact.ContactType, EmergencyContact> emergencyContacts = EmergencyContactDatabaseService.getEmergencyContacts(location.id);
                if (emergencyContacts.size() < 3)
                    return location;

                for (EmergencyContact contact : emergencyContacts.values()) {
                    if (StringUtils.isNullOrEmpty(contact.phoneNumber))
                        return location;
                }
            }
        }

        return null;
    }

    public static void deleteLocations() {
        contentResolver.delete(CanaryLocationContentProvider.CONTENT_URI, null, null);
    }


    public static List<Location> getLocationList() {
        List<Location> locationList = new ArrayList<>();


        Cursor cursor = contentResolver.query(CanaryLocationContentProvider.CONTENT_URI, null, null, null, null);

        if (cursor == null)
            return locationList;

        while (cursor.moveToNext()) {
            locationList.add(getLocationFromCursor(cursor));
        }
        cursor.close();

        return locationList;
    }

    public static boolean patchLocationMode(int location, String modeId) {
        String where = CanaryLocationContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(location)};

        ContentValues values = new ContentValues();

        values.put(CanaryLocationContentProvider.COLUMN_CURRENT_MODE, ModeCache.getMode(modeId).id);

        ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();
        int numUpdated = contentResolver.update(CanaryLocationContentProvider.CONTENT_URI, values, where, whereArgs);

        return numUpdated > 0;
    }


    public static boolean patchLocationMode(int location, boolean isPrivacy) {
        String where = CanaryLocationContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(location)};

        ContentValues values = new ContentValues();

        values.put(CanaryLocationContentProvider.COLUMN_IS_PRIVATE, isPrivacy);

        ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();
        int numUpdated = contentResolver.update(CanaryLocationContentProvider.CONTENT_URI, values, where, whereArgs);

        return numUpdated > 0;
    }

    public static Location getLocationFromId(int id) {
        Location location = null;

        String where = CanaryLocationContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(id)};

        ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();

        Cursor cursor = contentResolver.query(CanaryLocationContentProvider.CONTENT_URI, null, where, whereArgs, null);
        if (cursor == null)
            return null;

        if (cursor.moveToFirst()) {
            location = getLocationFromCursor(cursor);
        }
        cursor.close();

        return location;
    }

    public static Location getLocationFromResourceUri(String resourceUri) {
        Location location = null;

        String where = CanaryLocationContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {Utils.getStringFromResourceUri(resourceUri)};

        ContentResolver contentResolver = CanaryApplication.getContext().getContentResolver();

        Cursor cursor = contentResolver.query(CanaryLocationContentProvider.CONTENT_URI, null, where, whereArgs, null);
        if (cursor == null)
            return null;

        if (cursor.moveToFirst()) {
            location = getLocationFromCursor(cursor);
        }
        cursor.close();

        return location;
    }

    /**
     * Create a Location object from a cursor
     *
     * @param cursor Cursor object; the location with be generated from the values at the current position
     * @return Location object
     */
    public static Location getLocationFromCursor(Cursor cursor) {
        Location location = new Location();

        location.id = cursor.getInt(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_ID));
        location.address = cursor.getString(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_ADDRESS));
        location.address2 = cursor.getString(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_ADDRESS_TWO));
        location.city = cursor.getString(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_CITY));
        location.country = cursor.getString(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_COUNTRY));
        location.geofenceRadius = cursor.getInt(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_GEOFENCE_RADIUS));
        location.lat = cursor.getDouble(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_LAT));
        location.lng = cursor.getDouble(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_LNG));
        location.name = cursor.getString(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_NAME));
        location.resourceUri = Utils.buildResourceUri(Constants.LOCATIONS_URI, location.id);
        location.state = cursor.getString(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_STATE));
        location.zip = cursor.getString(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_ZIP));
        location.autoModeEnabled = cursor.getInt(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_AUTO_MODE_ENABLED)) > 0;
        location.isSharingData = cursor.getInt(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_SHARING_DATA)) > 0;
        location.isPrivate = cursor.getInt(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_IS_PRIVATE)) > 0;
        location.trailJustExpired = cursor.getInt(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_TRIAL_EXPIRED)) > 0;
        location.nightModeEnabled = cursor.getInt(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_NIGHT_MODE_ENABLED)) > 0;
        location.locationOwner = cursor.getString(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_OWNER));

        String lastModifiedString = cursor.getString(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_LAST_MODIFIED));
        location.lastModified = DateUtil.convertSqlStringToDate(lastModifiedString);

        location.showCVMaskTutorial = cursor.getInt(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_SHOW_MASK_TUTORIAL)) > 0;

        long createdTimestamp = cursor.getLong(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_CREATED));
        location.created = new Date(createdTimestamp);

        // now get the rest of the location info
        int currentMode = cursor.getInt(cursor.getColumnIndex(CanaryLocationContentProvider.COLUMN_CURRENT_MODE));
        location.currentMode = ModeDatabaseService.getModeFromId(currentMode);

        return location;
    }

    public static void setTrailExpired(int locationId, boolean expired) {
        ContentValues values = new ContentValues();
        values.put(CanaryLocationContentProvider.COLUMN_TRIAL_EXPIRED, expired);

        String where = CanaryLocationContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(locationId)};

        contentResolver.update(CanaryLocationContentProvider.CONTENT_URI, values, where, whereArgs);


    }

    /**
     * Create a ContentValues object from a location object
     *
     * @param location Location to convert
     * @return ContentValues object with the location information
     */
    public static ContentValues createContentValuesFromLocation(Location location) {
        ContentValues values = new ContentValues();

        int currentMode = location.currentMode != null ? location.currentMode.id : 0;

        values.put(CanaryLocationContentProvider.COLUMN_ADDRESS, location.address);
        values.put(CanaryLocationContentProvider.COLUMN_ADDRESS_TWO, location.address2);
        values.put(CanaryLocationContentProvider.COLUMN_CITY, location.city);
        values.put(CanaryLocationContentProvider.COLUMN_COUNTRY, location.country);
        values.put(CanaryLocationContentProvider.COLUMN_CURRENT_MODE, currentMode);
        values.put(CanaryLocationContentProvider.COLUMN_GEOFENCE_RADIUS, location.geofenceRadius);
        values.put(CanaryLocationContentProvider.COLUMN_ID, location.id);
        values.put(CanaryLocationContentProvider.COLUMN_LAT, location.lat);
        values.put(CanaryLocationContentProvider.COLUMN_LNG, location.lng);
        values.put(CanaryLocationContentProvider.COLUMN_NAME, location.name);
        values.put(CanaryLocationContentProvider.COLUMN_STATE, location.state);
        values.put(CanaryLocationContentProvider.COLUMN_ZIP, location.zip);
        values.put(CanaryLocationContentProvider.COLUMN_SHARING_DATA, location.isSharingData);
        values.put(CanaryLocationContentProvider.COLUMN_IS_PRIVATE, location.isPrivate);
        values.put(CanaryLocationContentProvider.COLUMN_NIGHT_MODE_ENABLED, location.nightModeEnabled);
        values.put(CanaryLocationContentProvider.COLUMN_OWNER, location.locationOwner);
        if (location.created != null)
            values.put(CanaryLocationContentProvider.COLUMN_CREATED, location.created.getTime());

        values.put(CanaryLocationContentProvider.COLUMN_AUTO_MODE_ENABLED, location.autoModeEnabled);

        return values;
    }

    public static void updateLocationModeSettings(int locationId, boolean checked) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CanaryLocationContentProvider.COLUMN_AUTO_MODE_ENABLED, checked);

        String where = CanaryLocationContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(locationId)};
        contentResolver.update(CanaryLocationContentProvider.CONTENT_URI, contentValues, where, whereArgs);
    }

    public static List<Location> getLocationListWithoutLocation(int locationId) {
        String where = CanaryLocationContentProvider.COLUMN_ID + " != ?";
        String[] whereArgs = {String.valueOf(locationId)};
        Cursor cursor = contentResolver.query(CanaryLocationContentProvider.CONTENT_URI, null, where, whereArgs, null);
        List<Location> locationList = new ArrayList<>();


        if (cursor == null)
            return locationList;
        while (cursor.moveToNext()) {
            locationList.add(getLocationFromCursor(cursor));
        }
        cursor.close();

        return locationList;
    }

    public static boolean checkIfGeofencingEnabled() {
        if (Utils.isDemo())
            return true;
        String where = CanaryLocationContentProvider.COLUMN_AUTO_MODE_ENABLED + " == ?";
        String[] whereArgs = {String.valueOf(1)};
        Cursor cursor = contentResolver.query(CanaryLocationContentProvider.CONTENT_URI, null, where, whereArgs, null);
        if (cursor == null)
            return true;
        try {
            return cursor.getCount() > 0;
        } finally {
            cursor.close();
        }

    }

    public static void updateDataOptin(int location, boolean dataOptIn) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CanaryLocationContentProvider.COLUMN_SHARING_DATA, dataOptIn);


        String where = CanaryLocationContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(location)};
        contentResolver.update(CanaryLocationContentProvider.CONTENT_URI, contentValues, where, whereArgs);
    }

    public static void insertNightModes(List<NightModeSchedule> nightmodes) {
        for (NightModeSchedule nightmode : nightmodes) {
            insertNightMode(nightmode);
        }
    }

    public static void insertNightMode(NightModeSchedule nightmode) {
        ContentValues contentValues = createContentValuesFromNightMode(nightmode);
        String where = CanaryNightModeScheduleContentProvider.COLUMN_LOCATION_ID + " == ?";

        String[] whereArgs = {String.valueOf(Utils.getIntFromResourceUri(nightmode.location))};


        if (contentResolver.update(CanaryNightModeScheduleContentProvider.CONTENT_URI,
                contentValues, where, whereArgs) == 0) {
            contentResolver.insert(CanaryNightModeScheduleContentProvider.CONTENT_URI, contentValues);

        }


    }

    public static NightModeSchedule getNightModeForLocation(int location) {
        String where = CanaryNightModeScheduleContentProvider.COLUMN_LOCATION_ID + " == ?";

        String[] whereArgs = {String.valueOf(location)};

        Cursor cursor = contentResolver.query(CanaryNightModeScheduleContentProvider.CONTENT_URI, null, where, whereArgs, null);
        if (cursor == null)
            return null;
        try {

            if (cursor.moveToFirst()) {
                return getNightModeFromCursor(cursor);
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    private static NightModeSchedule getNightModeFromCursor(Cursor cursor) {
        NightModeSchedule nightmode = new NightModeSchedule();

        nightmode.startTime = cursor.getString(cursor.getColumnIndex(CanaryNightModeScheduleContentProvider.COLUMN_START_TIME));
        nightmode.endTime = cursor.getString(cursor.getColumnIndex(CanaryNightModeScheduleContentProvider.COLUMN_END_TIME));
        nightmode.id = cursor.getInt(cursor.getColumnIndex(CanaryNightModeScheduleContentProvider.COLUMN_ID));
        return nightmode;
    }

    public static ContentValues createContentValuesFromNightMode(NightModeSchedule nightMode) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CanaryNightModeScheduleContentProvider.COLUMN_ID, nightMode.id);
        contentValues.put(CanaryNightModeScheduleContentProvider.COLUMN_END_TIME, nightMode.endTime);
        contentValues.put(CanaryNightModeScheduleContentProvider.COLUMN_START_TIME, nightMode.startTime);
        contentValues.put(CanaryNightModeScheduleContentProvider.COLUMN_LOCATION_ID, Utils.getIntFromResourceUri(nightMode.location));

        return contentValues;
    }

    public static void checkForThumbnails(int id) {


        List<Device> devices = DeviceDatabaseService.getActivatedDevicesAtLocation(id);

        if (devices.isEmpty())
            return;


        boolean launchMasking = false;

        for (Device device : devices) {
            Thumbnail thumbnail = ThumbnailDatabaseService.getThumbnailsForDevice(device);


            if (device.getDeviceType() != DeviceType.CANARY_AIO) {
                continue;
            }

            if (device.getDeviceType() == DeviceType.CANARY_AIO && !device.isMaskCompatible()) {
                launchMasking = false;
                break;
            }

            if (thumbnail != null) {
                launchMasking = true;
            }
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(CanaryLocationContentProvider.COLUMN_SHOW_MASK_TUTORIAL, launchMasking);

        String where = CanaryLocationContentProvider.COLUMN_ID + " == ?";
        String[] whereArgs = {String.valueOf(id)};

        contentResolver.update(CanaryLocationContentProvider.CONTENT_URI, contentValues, where, whereArgs);

    }
}
