package is.yranac.canary.utils;

import java.math.BigDecimal;

import is.yranac.canary.model.CurrentCustomer;
import is.yranac.canary.model.Customer;

/**
 * Created by michaelschroeder on 10/7/16.
 */

public class Utils {


    public enum TemperatureScale {
        CELSIUS,
        FAHRENHEIT
    }

    public static float getTemperatureInPreferredUnits(float celsius) {
        Customer customer = CurrentCustomer.getCurrentCustomer();
        if (customer == null) {
            return celsius;
        }

        return getTemperatureInPreferredUnits(celsius,
                TemperatureScale.CELSIUS,
                customer.celsius ? TemperatureScale.CELSIUS : TemperatureScale.FAHRENHEIT,
                null);
    }

    private static float getTemperatureInPreferredUnits(float temp, TemperatureScale currentScale, TemperatureScale neededScale, Integer decimalPlacesToFormat) {

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


    public static float formatFloatDecimalPlaces(float num, int decimalPlaces) {
        return BigDecimal.valueOf(num).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP).floatValue();
    }

}
