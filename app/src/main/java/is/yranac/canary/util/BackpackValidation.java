package is.yranac.canary.util;

import android.support.annotation.NonNull;

/**
 * Created by michaelschroeder on 3/16/17.
 */

public class BackpackValidation {

    public static boolean isValidIdentifier(@NonNull String identifier) {

        String[] parts = identifier.split("-");

        if (parts.length != 2)
            return false;

        String prefix = parts[0];
        String suffix = parts[1];

        return prefix.equalsIgnoreCase("CanaryLTE") && suffix.length() == 4 &&
                suffix.matches("[A-Za-z0-9]+");

    }


    public static boolean isValidPairingKey(@NonNull String pairingKey) {

        return pairingKey.length() == 17 &&
                pairingKey.matches("[A-Za-z0-9]+");

    }


    public static boolean isValidIMEI(@NonNull String imei) {

        return imei.length() == 15 &&
                imei.matches("[0-9]+");

    }


    public static boolean isValidICCID(@NonNull String iccid) {
        return iccid.length() == 20 &&
                iccid.matches("[0-9]+");

    }
}
