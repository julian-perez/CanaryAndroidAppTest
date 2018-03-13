package is.yranac.canary.util.keystore;

import android.text.TextUtils;

import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 8/17/16.
 */
public class KeyStoreHelper {


    public static boolean hasGoodOauthToken() {
        if (Utils.isDemo())
            return true;


        return !TextUtils.isEmpty(getToken());
    }


    public static boolean hasGoodRefreshToken() {
        if (Utils.isDemo())
            return true;


        return !TextUtils.isEmpty(getRefreshToken());
    }

    public static void saveToken(String token) {
        PreferencesUtils.setToken(token);
    }


    public static String getToken() {
        return PreferencesUtils.getAccessToken();
    }


    public static void saveRefreshToken(String token) {
        PreferencesUtils.setRefreshToken(token);
    }


    public static String getRefreshToken() {
        return PreferencesUtils.getRefreshToken();
    }


    public static boolean hasRefreshToken() {
        if (Utils.isDemo())
            return true;
        return !TextUtils.isEmpty(getRefreshToken());
    }

}
