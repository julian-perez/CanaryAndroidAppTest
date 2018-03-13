package is.yranac.canary.util.ga;

/**
 * Created by Schroeder on 3/29/16.
 */

public class AnalyticsConstants {

    /**
     * Screen View Constants
     */

    public static final String SCREEN_SINGLE_ENTRY = "single_entry";
    public static final String SCREEN_WATCH_LIVE = "watch_live";
    public static final String SCREEN_MAIN = "main_view";
    public static final String SCREEN_TIMELINE = "timeline";
    public static final String SCREEN_SETTINGS = "settings_menu";

    /**
     * Settings
     */

    public static final String SCREEN_ABOUT_CANARY = "about_canary";
    public static final String SCREEN_ADD_A_MEMBER = "add_location_member";
    public static final String SCREEN_CHANGE_PASSWORD = "change_password";
    public static final String SCREEN_DEVICE_NAME = "device_name";
    public static final String SCREEN_DEVICE_SETTINGS = "device_settings";
    public static final String SCREEN_HOME_HEALTH_NOTIFICATION_SETTINGS = "home_health_notification_settings";
    public static final String SCREEN_LOCATION_EMERGENCY_CONTACT = "location_emergency_numbers_settings";
    public static final String SCREEN_LOCATION_DEVICE_SETTINGS = "locationDevice_settings";
    public static final String SCREEN_LOCATION_MEMBER_SETTINGS = "locationMembers_settings";
    public static final String SCREEN_LOCATION_MODE_SETTINGS = "locationMode_settings";
    public static final String SCREEN_MOTION_NOTIFICATION_SETTINGS = "motion_notification_settings";
    public static final String SCREEN_NOTIFICATION_SETTINGS = "notification_settings";
    public static final String SCREEN_CONNECTIVITY_NOTIFICATION_SETTINGS = "connectivity_notification_settings";
    public static final String SCREEN_PASSCODE = "passcode";
    public static final String SCREEN_SINGLE_LOCATION_MEMBER = "single_location_member";
    public static final String SCREEN_PENDING_INVITATION = "pending_invitation";
    public static final String SCREEN_HOME_MODE_SETTINGS = "home_mode_settings";
    public static final String SCREEN_NIGHT_MODE_SETTINGS = "night_mode_settings";
    public static final String SCREEN_MEMBERSHIP_TRIAL_COMPLETE = "membership_trial_complete";
    public static final String SCREEN_MEMBERSHIP_SETTINGS = "membership_settings";
    public static final String SCREEN_CREATE_ACCOUNT = "create_account_details";
    public static final String SCREEN_EDIT_ACCOUNT = "edit_account";
    public static final String SCREEN_POWER_AND_BATTERY_NOTIFICATIONS = "power_and_battery_notification";
    public static final String SCREEN_ACCOUNT_SETTINGS = "account_settings";
    public static final String ACCOUNT_SETTINGS_PREFERENCES = "account_settings_preferences";
    public static final String SCREEN_ADDRESS_SETTINGS = "address_settings";
    public static final String SCREEN_GEOFENCE_SETTINGS = "geofence_settings";
    public static final String SCREEN_GEOFENCE_SETTINGS_SIZE = "geofence_settings_size";
    public static final String SCREEN_GEOFENCE_SETTINGS_POSITION = "geofence_settings_position";
    public static final String SCREEN_ADD_MEMBERSHIP_DOWNLOADS = "add_membership_downloads";

    /**
     * Setup
     */
    public static final String SCREEN_GET_STARTED = "get_started";
    public static final String SCREEN_CREATE_LOCATION = "create_location";
    public static final String SCREEN_CHOOSE_CONNECTION_TYPE = "choose_connection_type";
    public static final String SCREEN_POWER_CANARY = "power_canary";
    public static final String SCREEN_PLACEMENT_SUGGESTIONS = "placement_suggestions";
    public static final String SCREEN_ADD_DEVICE = "add_device_start";
    public static final String SCREEN_DEVICE_PLACEMENT = "device_placement";
    public static final String SCREEN_SETUP_COMPLETE_ADD_MEMBERSHIP = "setup_complete_add_membership";
    public static final String SCREEN_LOCATION_WIFI_NETWORKS = "local_wifi_networks";
    public static final String SCREEN_WIFI_CONNECTION_SETUP = "wifi_connection_setup";
    public static final String SCREEN_RESET_PASSWORD = "reset_password";
    public static final String SCREEN_AUDIO_SETUP = "audio_setup";
    public static final String SCREEN_CREATE_ACCOUNT_INITIAL = "create_account_initial";
    public static final String SCREEN_SECURE_SETUP_CABLE = "secure_setup_cable";
    public static final String SCREEN_ETHERNET_SETUP = "ethernet_Setup";

    /**
     * Watch Live Events
     */
    public static final String CATEGORY_LIVE = "live";
    public static final String ACTION_LIMIT = "limit";
    public static final String ACTION_LIMIT_PAGE_CHANGE = "page_change";
    public static final String ACTION_ZOOM = "zoom";
    public static final String ACTION_LIMIT_CONTINUE = "limit_continue";

    /**
     * Entry Events
     */

    public static final String CATEGORY_ENTRY = "entry";

    public static final String ACTION_EXPORT_REQUESTED = "export_requested";
    public static final String ACTION_EXPORT_DOWNLOAD = "download_video";
    public static final String ACTION_ENTRY_MENU_OPEN = "menu_open";
    public static final String ACTION_ENTRY_MENU_CLOSE = "menu_close";
    public static final String ACTION_VIEW_HOME_HEALTH = "home_health";
    public static final String ACTION_VIEW_OFFLINE = "offline";

    public static final String ACTION_SHARE_VIDEO = "share_video";
    public static final String ACTION_SHARE_DOWNLOAD = "share_video_download";
    public static final String ACTION_SHARE_CANCEL = "share_video_cancel";
    public static final String ACTION_SHARE_COMPLETE = "share_video_complete";

    public static final String ACTION_ENTRY_PLAY = "video_played";
    public static final String ACTION_ENTRY_PAUSE = "video_paused";
    public static final String ACTION_ENTRY_REWIND = "video_rewinded";
    public static final String ACTION_ENTRY_DELETE = "delete";
    public static final String ACTION_ENTRY_COMMENT = "comment";
    public static final String ACTION_SAVE = "save";
    public static final String ACTION_UNSAVE = "unsave";


    /**
     * Notification tracking
     */
    public static final String NOTIFICATION_CATEGORY = "push_notification";
    public static final String NOTIFICATION_ACTION = "received";
    public static final String NOTIFICATION_IN_APP_PROPERTY = "in_app";
    public static final String NOTIFICATION_BACKGROUND_PROPERTY = "background";


    /**
     * Emergency call tracking
     */
    public static final String CATEGORY_EMERGENCY_OPTIONS = "emergency_options";

    public static final String ACTION_SIREN_PRESS = "siren_press";
    public static final String ACTION_SIREN_CANCEL = "siren_cancel";
    public static final String ACTION_SIREN_CONFIRM_ONE = "siren_confirm_one";
    public static final String ACTION_SIREN_CONFIRM_ALL = "siren_confirm_all";
    public static final String ACTION_SIREN_STOP = "siren_stop";

    public static final String CALL_PRESS = "call_press";
    public static final String ACTION_CALL_POLICE = "call_police";
    public static final String ACTION_CALL_EMS = "call_ems";
    public static final String ACTION_CALL_FIRE = "call_fire";
    public static final String ACTION_CALL_CANCEL = "call_cancel";


    /**
     * Membership tracking
     */
    public static final String CATEGORY_MEMBERSHIP = "membership";

    public static final String ACTION_CONTINUE_PREVIEW = "continue_preview";
    public static final String ACTION_ADD_MEMBERSHIP = "add_membership";
    public static final String ACTION_MANAGE_MEMBERSHIP = "manage_membership";
    public static final String ACTION_CONTACT_INCIDENT_SUPPORT = "contact_incident_support";
    public static final String READ_MEMBERSHIP_DETAILS = "read_membership_details";

    public static final String ACTION_ACTIVATE_MEMBERSHIP_SKIP = "activate_membership_skip";
    public static final String ACTION_ACTIVATE_MEMBERSHIP_ADD = "activate_membership_add";
    public static final String ACTION_ACTIVATE_TRIAL_ADD = "activate_trial_add";
    public static final String ACTION_ACTIVATE_TRIAL_ADD_RETRY = "activate_trial_add_retry";
    public static final String ACTION_ACTIVATE_TRIAL_SKIP = "activate_trial_skip";
    public static final String ACTION_SETUP_COMPLETE_SKIP = "setup_complete_skip";
    public static final String ACTION_SETUP_COMPLETE_ADD = "setup_complete_add";

    public static final String ACTION_NIGHT_MODE_CTA_OPEN = "night_mode_cta_open";
    public static final String ACTION_NIGHT_MODE_CTA_CANCEL = "night_mode_cta_cancel";
    public static final String ACTION_NIGHT_MODE_SETTINGS_CTA_OPEN = "night_mode_settings_cta_open";
    public static final String ACTION_NIGHT_MODE_SETTINGS_CTA_CANCEL = "night_mode_settings_cta_cancel";
    public static final String ACTION_HOME_MODE_SETTINGS_CTA_OPEN = "home_mode_settings_cta_open";
    public static final String ACTION_HOME_MODE_SETTINGS_CTA_CANCEL = "home_mode_settings_cta_cancel";

    public static final String ACTION_VIEW_TIMELINE_FOOTER_CTA = "view_timeline_footer_cta";
    public static final String ACTION_WATCH_LIVE_ENTRY_COMPLETE = "watch_live_entry_complete";
    public static final String ACTION_ENTRY_REPLAY = "entry_replay";
    public static final String ACTION_SINGLE_ENTRY_DOWNLOAD_CTA_CANCEL = "single_entry_download_cta_cancel";

    public static final String LEARN_MORE = "learn_more";

    public static final String PROPERTY_MEMBERSHIP_TRIAL_COMPLETE = "membership_trial_complete";
    public static final String PROPERTY_MEMBERSHIP_SETTINGS = "membership_settings";
    public static final String PROPERTY_LIVE = "live";
    public static final String PROPERTY_ENTRY = "entry";
    public static final String PROPERTY_SINGLE_ENTRY = "single_entry";
    public static final String PROPERTY_SINGLE_ENTRY_DOWNLOAD_CTA = "single_entry_download_cta";
    public static final String PROPERTY_TIMELINE = "timeline";
    public static final String PROPERTY_NIGHT_MODE_CTA = "night_mode_cta";
    public static final String PROPERTY_NIGHT_MODE_SETTINGS_CTA = "night_mode_settings_cta";
    public static final String PROPERTY_HOME_MODE_SETTINGS_CTA = "home_mode_settings_cta";


    /**
     * Bluetooth
     */

    public static final String CATEGORY_BLUETOOTH_SETUP = "bluetooth";
    public static final String CATEGORY_BLUETOOTH_CHANGE_WIFI = "bluetooth";

    public static final String ACTION_BLUETOOTH_START = "start";
    public static final String ACTION_BLUETOOTH_FAILURE = "failure";
    public static final String ACTION_BLUETOOTH_SUCCESS = "success";

    /**
     * Audio
     */

    public static final String CATEGORY_AUDIO_SETUP = "audio_setup";
    public static final String CATEGORY_AUDIO_CHANGE_WIFI = "audio_change_wifi";

    public static final String ACTION_AUDIO_START = "start";
    public static final String ACTION_AUDIO_FAILURE = "failure";
    public static final String ACTION_AUDIO_SUCCESS = "success";


    /**
     * Mode
     */
    public static final String CATEGORY_MODE = "audio_setup";
    public static final String ACTION_MODE_CHANGE = "audio_setup";
    public static final String PROPERTY_MODE_MANUAL = "manual";


    /**
     * Help
     */
    public static final String CATEGORY_HELP = "help";
    public static final String ACTION_CONTRACT_SUPPORT = "contact_support";
    public static final String ACTION_HELP_CENTER = "help_center";

    /**
     * Enter app
     */

    public static final String CATEGORY_APP_ENTRY = "app_entry_point";
    public static final String ACTION_APP_ENTRY_NOTIFICATION = "push";
    public static final String ACTION_APP_ENTRY_HOMSCREEN = "homescreen";
    public static final String PROPERTY_APP_ENTRY_BACKGROUNDED = "background";
    public static final String PROPERTY_APP_ENTRY_TERMINATED = "terminated";


    /**
     * Insurance
     */

    public static final String CATEGORY_INSURANCE = "insurance";

    public static final String ACTION_INSURANCE_SKIP = "skip";
    public static final String ACTION_INSURANCE_LINK_POLICY = "link_policy";
    public static final String ACTION_INSURANCE_SAVE_SETTINGS = "save_settings";

    public static final String PROPERTY_INSURANCE_OTHER_PROVIDERS = "other_providers";
    public static final String PROPERTY_INSURANCE_LEARN_MORE = "learn_more";
    public static final String PROPERTY_INSURANCE_SHARE_DATA = "data_share";
    public static final String PROPERTY_INSURANCE_WILL_SHARE = "will_share";
    public static final String PROPERTY_INSURANCE_WILL_NOT_SHARE = "will_not_share";


    /**
     * calendar
     */

    public static final String CATEGORY_CALENDAR = "calendar";
    public static final String ACTION_CALENDAR_OPEN = "calendar_open";
    public static final String ACTION_CALENDAR_CLOSE = "calendar_close";
    public static final String PROPERTY_CALENDAR_TIMELINE = "timeline";
    public static final String PROPERTY_CALENDAR_FILTER = "filter";

    /**
     * timeline
     */

    public static final String CATEGORY_TIMELINE = "timeline";
    public static final String ACTION_TIMELINE_OPEN = "open";
    public static final String ACTION_TIMELINE_CLOSE = "close";
    public static final String ACTION_TIMELINE_TOGGLE = "toggle";
    public static final String ACTION_TIMELINE_SHOW_DETAILS = "show_details";
    public static final String ACTION_TIMELINE_ENTRY_SCROLLED = "entry_scrolled";


    /**
     * Settings
     */

    public static final String SETTINGS_UPDATE_NAME = "update_name";
    public static final String SETTINGS_SENSITIVITY = "sensitivity";
    public static final String SETTINGS_ADDRESS_UPDATE = "address_update";
    public static final String SETTINGS_AUTO_MODE_ENABLED = "auto_mode_enabled";
    public static final String SETTINGS_NIGHT_MODE_TIME_START = "night_mode_start_time";
    public static final String SETTINGS_NIGHT_MODE_TIME_END = "night_mode_end_time";
    public static final String SETTINGS_NIGHT_MODE_ENABLED = "night_mode_enabled";
    public static final String SETTINGS_PASSCODE_UPDATE = "passcode_enabled";
    public static final String SETTINGS_LANGUAGE_UPDATE = "language_update";
    public static final String SETTINGS_AVATAR_UPDATE = "avatar_update";
    public static final String SETTINGS_GEOFENCE_SIZE = "geofence_size";
    public static final String SETTINGS_GEOFENCE_POSITION = "geofence_position";

    public static final String SETTINGS_TYPE_APP = "app";
    public static final String SETTINGS_TYPE_CUSTOMER = "customer";
    public static final String SETTINGS_TYPE_DEVICE = "device";
    public static final String SETTINGS_TYPE_LOCATION = "location";


    /**
     * Ouath
     */

    public static final String OUATH_CATEGORY = "api_client";
    public static final String OUATH_ACTION = "error_";


    /**
     * cv masking
     */
    public static final String CATEGORY_MASKING = "masking";
    public static final String ACTION_MASKING_ADD = "add";
    public static final String ACTION_MASKING_EDIT = "edit";
    public static final String ACTION_MASKING_DELETE = "delete";
    public static final String ACTION_MASKING_SAVE = "save";
    public static final String ACTION_MASKING_HELP = "help";
    public static final String PROPERTY_MASKING_MANAGE_DEVICE = "manage_device";
    public static final String PROPERTY_MASKING_MANAGE_MASK = "manage_mask";

    /**
     * cv masking onboarding
     */
    public static final String CATEGORY_MASKING_ONBOARDING = "masking_onboarding";
    public static final String ACTION_MASKING_ONBOARDING_CANCELLED = "cancelled";
    public static final String ACTION_MASKING_ONBOARDING_START = "start";
    public static final String ACTION_MASKING_ONBOARDING_COMPLETED = "completed";
    public static final String ACTION_MASKING_ONBOARDING_HELP = "help";
    public static final String PROPERTY_MASKING_ONBOARDING_MOTION = "motion";
    public static final String PROPERTY_MASKING_ONBOARDING_MASKING = "masking";

    /**
     * settings 2.0
     */
    public static final String CATEGORY_SETTINGS = "settings";

    public static final String ACTION_HELP = "help";
    public static final String ACTION_ADD_DEVICE_INTENT = "add_device_intent";
    public static final String ACTION_ADD_DEVICE = "add_device";
    public static final String PROPERTY_GEOFECE = "geofece";
    public static final String PROPERTY_GEOFECE_SIZE = "geofece_size";
    public static final String PROPERTY_GEOFENCE_POSITION = "geofence_position";
    public static final String PROPERTY_MEMBERSHIP = "membership";
    public static final String PROPERTY_EXISTING_LOCATION = "existing_location";
    public static final String PROPERTY_NEW_LOCATION = "new_location";


    /**
     * Watch Live 2
     */

    public static final String CATEGORY_WATCH_LIVE_2 = "watch_live_2";

    public static final String ACTION_TALK_LOADING = "talk_loading";
    public static final String ACTION_TALK_BEGIN = "talk_begin";
    public static final String ACTION_TALK_END = "talk_end";
    public static final String ACTION_TALK_CANCELLED = "talk_cancelled";
    public static final String ACTION_LIVE_LOADING = "live_loading";
    public static final String ACTION_LIVE_FAILURE = "live_failure";
    public static final String ACTION_LIVE_PLAYING = "live_playing";
    public static final String ACTION_SERVER_CLOSE = "server_close";

    public static final String PROPERTY_VIDEO_LOADED = "video_loaded";
    public static final String PROPERTY_NO_VIDEO_LOADED = "no_video_loaded";


    /**
     * Geofence
     */


    public static final String CATEGORY_GEOFENCE = "geofence";

    public static final String ACTION_GEOFENCE_ENTER = "enter";
    public static final String ACTION_GEOFENCE_EXIT = "exit";

    public static final String PROPERTY_FOREGROUND = "foreground";
    public static final String PROPERTY_BACKGROUND = "background";
}
