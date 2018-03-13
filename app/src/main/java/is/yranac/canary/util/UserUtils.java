package is.yranac.canary.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.math.BigDecimal;
import java.util.List;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.Constants;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;

public class UserUtils {

    private static final String LOG_TAG = "UserUtils";

    public static void setLastViewedLocationId(int lastViewedLocationId) {
        SharedPreferences preferences = CanaryApplication.getContext().getSharedPreferences(
                Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        preferences.edit().putInt(Constants.LAST_LOCATION_ID, lastViewedLocationId).apply();
    }

    public static float getTemperatureInPreferredUnits(float celsius) {
        Customer customer = CurrentCustomer.getCurrentCustomer();
        if (customer == null || customer.celsius) {
            return celsius;
        }

        return getTemperatureInPreferredUnits(celsius,
                TemperatureScale.CELSIUS,
                customer.celsius ? TemperatureScale.CELSIUS : TemperatureScale.FAHRENHEIT,
                null);
    }

    public static float getTemperatureInPreferredUnits(float celsius, boolean needCelsius) {
        return getTemperatureInPreferredUnits(celsius, needCelsius, true);
    }

    /**
     * @param temp           value to convest
     * @param currentScale   scale the value is in currently
     * @param neededScale    scale needed to return
     * @param needFormatting null is no formatting is needed, otherwise number of decimal places to format to.
     * @return
     */
    public static float getTemperatureInPreferredUnits(float temp, TemperatureScale currentScale, TemperatureScale neededScale, Integer decimalPlacesToFormat) {

        float result = Float.MAX_VALUE;
        if (decimalPlacesToFormat != null)
            temp = formatFloatDecimalPlaces(temp, decimalPlacesToFormat);

        if (currentScale == neededScale)
            result = temp;
        else if (currentScale == TemperatureScale.CELSIUS && neededScale == TemperatureScale.FAHRENHEIT) {
            result = (temp * 9.0f / 5.0f) + 32;
        } else if (currentScale == TemperatureScale.FAHRENHEIT && neededScale == TemperatureScale.CELSIUS)
            result = ((temp - 32) * 5.0f / 9.0f);

        return decimalPlacesToFormat != null ? formatFloatDecimalPlaces(result, decimalPlacesToFormat) : result;
    }

    public static float getTemperatureInPreferredUnits(float celsius, boolean needCelsius, boolean formatted) {

        return getTemperatureInPreferredUnits(celsius,
                TemperatureScale.CELSIUS,
                needCelsius ? TemperatureScale.CELSIUS : TemperatureScale.FAHRENHEIT,
                formatted ? 2 : null);
    }

    public static float formatFloatDecimalPlaces(float num, int decimalPlaces) {
        return BigDecimal.valueOf(num).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static int getLastViewedLocationId() {
        SharedPreferences preferences = CanaryApplication.getContext().getSharedPreferences(
                Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getInt(Constants.LAST_LOCATION_ID, -1);
    }

    public static Location getLastViewedLocation() {
        SharedPreferences preferences = CanaryApplication.getContext().getSharedPreferences(
                Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        Location location = null;

        int lastLocationId = preferences.getInt(Constants.LAST_LOCATION_ID, 0);

        if (lastLocationId != 0) {
            location = LocationDatabaseService.getLocationFromId(lastLocationId);
        }

        return location;
    }

    public static void checkCurrentLocationDevices() {
        List<Device> devices = DeviceDatabaseService.getActivatedDevicesAtLocation(getLastViewedLocationId());

        if (devices.size() == 0) {

            int newLocation = 0;
            List<Location> locations = LocationDatabaseService.getLocationList();

            for (Location location : locations) {

                List<Device> devices1 = DeviceDatabaseService.getActivatedDevicesAtLocation(location.id);

                if (devices1.size() != 0) {
                    newLocation = location.id;
                    break;
                }
            }

            setLastViewedLocationId(newLocation);
        }
    }
}
