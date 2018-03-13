package is.yranac.canary.util;

import android.content.Context;
import android.content.SharedPreferences;

import is.yranac.canary.BuildConfig;
import is.yranac.canary.CanaryApplication;
import is.yranac.canary.Constants;
import is.yranac.canary.model.masking.DeviceMasks;
import is.yranac.canary.model.tutorial.TutorialsState;
import is.yranac.canary.ui.views.CanaryTextWatcher;
import is.yranac.canary.util.keystore.KeyStoreHelper;

public class PreferencesUtils {

    private static final String languagePrompt = "language_prompt";
    private static final String nightModePrompt = "night_mode_prompt";
    private static final String key_tutorialState = "tutorial_state";
    private static final String key_userSwipedBetweenScreens = "user_swyped_between_screens";
    private static final String key_geofencing_enabled = "geofencing_enabled";
    private static final String key_device_masks = "device_masks";
    private static final String hasSeenCVLaunch = "has_seen_cv_launch";
    private static final String hasSeenMasking = "has_seen_masking";
    private static final String key_newAccountCreatedForMaskingLaunch = "new_account_created_masking_launch";

    private static final String accessToken = "accessToken";
    private static final String refreshToken = "refreshToken";

    public static String getAccessToken() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        return preferences.getString(accessToken);
    }

    public static void setToken(String token) {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        preferences.commit(accessToken, token);
    }


    public static void setRefreshToken(String token) {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        preferences.commit(refreshToken, token);
    }

    public static String getRefreshToken() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        return preferences.getString(refreshToken);
    }


    public static String getUserName() {
        if (Utils.isDemo())
            return "regan@canary.is";

        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        return preferences.getString("user_email");
    }

    public static void setUserName(String userEmail) {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        if (CanaryTextWatcher.validateString(userEmail, CanaryTextWatcher.VALID_EMAIL)) {
            preferences.put("user_email", userEmail);
        }

    }

    //TODO: use normal caching when we finalize mask obj.
    public static void setDeviceMasks(DeviceMasks masks, int deviceId) {
        if (masks == null)
            return;

        SecurePreferences preferences = new SecurePreferences(

                CanaryApplication.getContext(), true);

        preferences.put(key_device_masks + deviceId, JSONUtil.getJSONString(masks));
    }

    public static String getDeviceMasks(int deviceId) {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);
        if (!preferences.containsKey(key_device_masks + deviceId))
            return null;

        return preferences.getString(key_device_masks + deviceId);
    }

    public static void removeUser() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);
        removePassCode();
        preferences.removeValue("user_email");

        KeyStoreHelper.saveRefreshToken("");
        KeyStoreHelper.saveToken("");
    }

    public static void saveTutorialsState(TutorialsState state) {
        String json = JSONUtil.getJSONString(state);
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);
        preferences.put(key_tutorialState, json);
    }

    public static TutorialsState getTutorialsState() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        if (!preferences.containsKey(key_tutorialState)) {
            TutorialsState newState = new TutorialsState();
            if (appUpgraded()) {
                newState.didCompleteSecondDeviceTutorial = true;
                newState.didCompleteHomeTutorial = true;
                newState.didCompleteTimelineTutorial = true;
                newState.didCompleteTimelineFilterTutorial = true;
                newState.didCompleteSingleEntryMoreOptionsTutorial = true;
                newState.didCompleteSecondDeviceTutorial = true;
                newState.didCompleteHomeForSecondDeviceTutorial = true;
            } else {
                newState.didCompleteSecondDeviceTutorial = true;
                newState.didCompleteHomeForSecondDeviceTutorial = true;
            }
            newState.currentHomeStep = 0;
            saveTutorialsState(newState);
            return newState;
        }
        return JSONUtil.getObject(preferences.getString(key_tutorialState), TutorialsState.class);
    }

    public static void setShownMember() {
        SharedPreferences preferences = CanaryApplication.getContext().getSharedPreferences("preferencesKey", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("shownAddMember", true).apply();
    }

    public static boolean hasShowAddMember() {
        SharedPreferences preferences = CanaryApplication.getContext().getSharedPreferences("preferencesKey", Context.MODE_PRIVATE);
        if (!preferences.contains("shownAddMember")) {
            preferences.edit().putBoolean("shownAddMember", false).apply();
            return false;
        }

        return preferences.getBoolean("shownAddMember", false);
    }

    public static void setPassCode(String passCode) {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);
        preferences.put("passCode", passCode);
    }

    public static boolean hasPassCode() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        return preferences.containsKey("passCode");

    }

    public static String getPassCode() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        if (preferences.containsKey("passCode")) {
            return preferences.getString("passCode");
        }

        return null;

    }

    public static void removePassCode() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);
        preferences.removeValue("passCode");
    }

    public static void setShowingPassCode(boolean showing) {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);
        preferences.put("pass_code", String.valueOf(showing));
    }

    public static boolean showingPasscode() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        if (!preferences.containsKey("pass_code")) {
            preferences.put("pass_code", String.valueOf(false));
            return false;
        }

        return Boolean.parseBoolean(preferences.getString("pass_code"));
    }

    public static void setHasGoneToMembers(boolean showing) {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);
        preferences.put("gone_to_members", String.valueOf(showing));
    }

    private static final String trustsConnection = "trusts_connection";

    public static boolean trustsConnection() {

        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        if (!preferences.containsKey(trustsConnection)) {
            preferences.put(trustsConnection, String.valueOf(true));
            return true;
        }

        return Boolean.parseBoolean(preferences.getString(trustsConnection));

    }


    public static void setTrustsConnection(boolean trusts) {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        preferences.put(trustsConnection, String.valueOf(trusts));
    }

    public static void toggleCerts() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        preferences.put(trustsConnection, String.valueOf(!trustsConnection()));
    }

    private static final String appUpgraded = "app_upgraded";
    private static final String appNumber = "app_number";

    public static void setAppNumber() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        if (preferences.containsKey(appNumber)) {
            int buildNumber = Integer.parseInt(preferences.getString(appNumber));
            if (buildNumber != BuildConfig.VERSION_CODE) {
                setAppUpgraded(true);
            }
        } else {
            setAppUpgraded(false);
        }
        preferences.put(appNumber, String.valueOf(BuildConfig.VERSION_CODE));

    }

    public static void setAppUpgraded(boolean upgraded) {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        preferences.put(appUpgraded, String.valueOf(upgraded));
    }


    public static boolean appUpgraded() {

        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        if (!preferences.containsKey(appUpgraded)) {
            preferences.put(appUpgraded, String.valueOf(false));
            return false;
        }

        return Boolean.parseBoolean(preferences.getString(appUpgraded));

    }

    private final static String locationServicesDebugging = "location_Services_debugging";

    public static void setLocationServicesDebugging(Context context, boolean checked) {

        SecurePreferences preferences = new SecurePreferences(
                context, true);

        preferences.put(locationServicesDebugging, String.valueOf(checked));
    }

    public static boolean locationServicesDebuggingEnabled(Context context) {

        SecurePreferences preferences = new SecurePreferences(
                context, true);

        if (!preferences.containsKey(locationServicesDebugging)) {
            preferences.put(locationServicesDebugging, String.valueOf(false));
            return false;
        }

        return Boolean.parseBoolean(preferences.getString(locationServicesDebugging));
    }

    public static boolean hasBeenPromptedForLanguage() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        if (!preferences.containsKey(languagePrompt)) {
            preferences.put(languagePrompt, String.valueOf(false));
            return false;
        }

        return Boolean.parseBoolean(preferences.getString(languagePrompt));
    }

    public static void setHasSeenLanguagePrompt() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);
        preferences.put(languagePrompt, String.valueOf(true));
    }

    public static boolean hasSeenNightModeOnboarding() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);
        if (!preferences.containsKey(nightModePrompt)) {
            preferences.put(nightModePrompt, String.valueOf(true));
            return false;
        }
        return Boolean.parseBoolean(preferences.getString(nightModePrompt));
    }

    public static void setUserSwipedBetweenDevices() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);
        preferences.put(key_userSwipedBetweenScreens, String.valueOf(true));
    }

    public static boolean getUserSwipedBetweenDevices() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        if (!preferences.containsKey(key_userSwipedBetweenScreens)) {
            preferences.put(key_userSwipedBetweenScreens, String.valueOf(false));
            return false;
        }
        return Boolean.parseBoolean(preferences.getString(key_userSwipedBetweenScreens));
    }

    public static void toggleGeofencing() {

        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        preferences.put(key_geofencing_enabled, String.valueOf(!getGeofencingEnabled()));
    }


    public static boolean getGeofencingEnabled() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);


        if (!preferences.containsKey(key_geofencing_enabled)) {
            preferences.put(key_geofencing_enabled, String.valueOf(!ScreenUtil.isTablet()));
        }
        return Boolean.parseBoolean(preferences.getString(key_geofencing_enabled));
    }


    public static boolean hasSeenMaskingLaunch() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        if (!preferences.containsKey(hasSeenCVLaunch)) {
            preferences.put(hasSeenCVLaunch, String.valueOf(false));
            return false;
        }
        return Boolean.parseBoolean(preferences.getString(hasSeenCVLaunch));
    }


    public static void setSeenMaskingLaunch(boolean launch) {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);
        preferences.put(hasSeenCVLaunch, String.valueOf(launch));

    }

    public static void setHasSeenCVMaskingTutorial() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);
        preferences.put(hasSeenMasking, String.valueOf(true));
    }

    public static boolean hasSeenCVMaskingTutorial() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);
        if (!preferences.containsKey(hasSeenMasking)) {
            preferences.put(hasSeenMasking, String.valueOf(false));
            return false;
        }
        return Boolean.parseBoolean(preferences.getString(hasSeenMasking));
    }

    public static Integer getFirstCreatedLocationIDForMaskingLaunch() {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);
        String locationIdString = preferences.getString(key_newAccountCreatedForMaskingLaunch);
        if (StringUtils.isNullOrEmpty(locationIdString))
            return null;
        return Integer.parseInt(locationIdString);
    }

    public static void setFirstCreatedLocationIDForMaskingLaunch(int locationID) {
        SecurePreferences preferences = new SecurePreferences(
                CanaryApplication.getContext(), true);

        preferences.put(key_newAccountCreatedForMaskingLaunch, Integer.toString(locationID));
    }


    private static final String key_location_notification = "key_location_notification";

    public static void setLocationNotification(Context context, boolean locationNotification) {
        SharedPreferences preferences = context.getSharedPreferences(
                Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        preferences.edit().putBoolean(key_location_notification, locationNotification).apply();
    }

    public static boolean locationNotificationEnabled(Context context) {
        if (!Utils.isForInternalTestingAndDevelopment())
            return false;
        SharedPreferences preferences = context.getSharedPreferences(
                Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        return preferences.getBoolean(key_location_notification, false);
    }

    private static final String key_amazon_header = "key_amazon_header";


    public static boolean hasShownAmazonHeader(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        return preferences.getBoolean(key_amazon_header, false);
    }

    public static void setHasShownAmazonHeader(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        preferences.edit().putBoolean(key_amazon_header, true).apply();
    }

    private static final String key_show_amazon_end_state = "key_show_amazon_end_state";


    public static boolean hasShownAmazonEnd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        return preferences.getBoolean(key_show_amazon_end_state, false);
    }

    public static void setHasShownAmazonEnd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        preferences.edit().putBoolean(key_show_amazon_end_state, true).apply();
    }
}
