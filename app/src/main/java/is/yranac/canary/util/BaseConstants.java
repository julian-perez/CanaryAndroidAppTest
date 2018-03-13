package is.yranac.canary.util;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Locale;
import java.util.Set;

import is.yranac.canary.BuildConfig;
import is.yranac.canary.CanaryApplication;
import is.yranac.canary.R;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.location.Location;

public class BaseConstants {

    public static final String BASE_URL_HTTP = "https://";

    public static final String API_VERSION = "/v1";
    public static final String OAUTH_VERSION = "/o";

    public static final String OAUTH_GRANT_TYPE = "password";
    public static final String OAUTH_GRANT_TYPE_UPDATE = "update_token";
    public static final String OAUTH_GRANT_TYPE_REFRESH = "refresh_token";
    public static final String OAUTH_SCOPE = "write";
    public static final String OAUTH_INVALID_TOKEN = "";

    public static final String AUTHORITY_AVATAR = BuildConfig.APPLICATION_ID + ".avatarprovider";
    public static final String AUTHORITY_CACHE_GEOFENCE = BuildConfig.APPLICATION_ID + ".cachegeofenceprovider";
    public static final String AUTHORITY_COMMENT = BuildConfig.APPLICATION_ID + ".commentprovider";
    public static final String AUTHORITY_CUSTOMER = BuildConfig.APPLICATION_ID + ".customerprovider";
    public static final String AUTHORITY_CUSTOMERLOCATION = BuildConfig.APPLICATION_ID + ".customerlocationprovider";
    public static final String AUTHORITY_DEVICE = BuildConfig.APPLICATION_ID + ".deviceprovider";
    public static final String AUTHORITY_ENTRY = BuildConfig.APPLICATION_ID + ".entryprovider";
    public static final String AUTHORITY_ENTRY_CUSTOMERS = BuildConfig.APPLICATION_ID + ".entrycustomersprovider";
    public static final String AUTHORITY_LABEL = BuildConfig.APPLICATION_ID + ".labelprovider";
    public static final String AUTHORITY_INVITATION = BuildConfig.APPLICATION_ID + ".invitationprovider";
    public static final String AUTHORITY_LOCATION = BuildConfig.APPLICATION_ID + ".locationprovider";
    public static final String AUTHORITY_MODE = BuildConfig.APPLICATION_ID + ".modeprovider";
    public static final String AUTHORITY_READING = BuildConfig.APPLICATION_ID + ".readingprovider";
    public static final String AUTHORITY_SUBSCRIPTION = BuildConfig.APPLICATION_ID + ".subscriptionprovider";
    public static final String AUTHORITY_SUBSCRIPTION_PRICES = BuildConfig.APPLICATION_ID + ".subscriptionpricesprovider";
    public static final String AUTHORITY_THUMBNAIL = BuildConfig.APPLICATION_ID + ".thumbnailprovider";
    public static final String AUTHORITY_VIDEO_EXPORT = BuildConfig.APPLICATION_ID + ".videoexportprovider";
    public static final String AUTHORITY_DEVICE_SETTINGS = BuildConfig.APPLICATION_ID + ".devicesettingsprovider";
    public static final String AUTHORITY_DEVICE_STATISTICS = BuildConfig.APPLICATION_ID + ".devicestatisticsprovider";
    public static final String AUTHORITY_EMERGENCY_CONTACTS = BuildConfig.APPLICATION_ID + ".emergencycontactsprovider";
    public static final String AUTHORITY_DEVICE_TOKEN = BuildConfig.APPLICATION_ID + ".devicetokenprovider";
    public static final String AUTHORITY_NOTIFIED = BuildConfig.APPLICATION_ID + ".notifiedprovider";
    public static final String AUTHORITY_LOCATION_NETWORK = BuildConfig.APPLICATION_ID + ".locationnetwork";
    public static final String AUTHORITY_INSURANCE_POLICY = BuildConfig.APPLICATION_ID + ".insurancepolicyprovider";
    public static final String AUTHORITY_INSURANCE_PROVIDER = BuildConfig.APPLICATION_ID + ".insuranceproviderprovider";
    public static final String AUTHORITY_USER_TAGS = BuildConfig.APPLICATION_ID + ".usertagsprovider";
    public static final String AUTHORITY_FEATURE_FLAG = BuildConfig.APPLICATION_ID + ".featureflagsprovider";
    public static final String AUTHORITY_FILES = BuildConfig.APPLICATION_ID + ".fileprovider";
    public static final String AUTHORITY_NIGHT_MODE = BuildConfig.APPLICATION_ID + ".nightmodeprovider";
    public static final String AUTHORITY_OAUTH = BuildConfig.APPLICATION_ID + ".oauthprovider";
    public static final String AUTHORITY_MEMBERSHIP = BuildConfig.APPLICATION_ID + ".membershipprovider";


    public static final String LOCATIONS_URI = API_VERSION + "/locations/";
    public static final String ENTRIES_URI = API_VERSION + "/entries/";
    public static final String DEVICE_TOKEN_URI = API_VERSION + "/devicetokens/";
    public static final String COMMENT_URI = API_VERSION + "/comments/";
    public static final String CLIPS_URI = API_VERSION + "/clips/";
    public static final String CUSTOMER_URI = API_VERSION + "/customers/";
    public static final String AVATAR_URI = API_VERSION + "/avatars/";
    public static final String THUMBNAIL_URI = API_VERSION + "/thumbnails/";
    public static final String DEVICE_URI = API_VERSION + "/devices/";
    public static final String INVITATION_URI = API_VERSION + "/invitations/";
    public static final String MEMBERSHIP_URI = API_VERSION + "/memberships/";
    public static final String MODES_URI = API_VERSION + "/modes/";
    public static final String LOCATION_CHANGES_URI = API_VERSION + "/locationchanges/";
    public static final String READING_URI = API_VERSION + "/readings/";
    public static final String DEVICE_SETTINGS_URI = API_VERSION + "/devicesettings/";
    public static final String EMERGENCY_CONTACT_URI = API_VERSION + "/emergencycontact/";
    public static final String COMMANDS_URI = API_VERSION + "/commands/";
    public static final String FEATUREFLAG_URI = API_VERSION + "/features/";
    public static final String NIGHT_MODE_URI = API_VERSION + "/nightmodeconfigurations/";
    public static final String INSURANCE_URI = API_VERSION + "/insurance_providers/";
    public static final String SUBSCRIPTIONS_URI = API_VERSION + "/subscription/";
    public static final String PRICES_URI = API_VERSION + "/subscription/pricing/";

    public static final String DEVICE_MASKS = API_VERSION + "/masks/";
    public static final String WATCH_LIVE_SESSION = API_VERSION + "/watchlivesession/";

    public static final String EXPORT = API_VERSION + "/export/";
    public static final String PATH_MANIFEST = API_VERSION + "/manifests/?device=";
    public static final String PATH_ENTRY_MANIFEST = API_VERSION + "/manifests/?entry=";

    public static final String ENCRYPTION_TOKEN = API_VERSION + "/blek/";

    public static final String SHARED_PREFERENCES = "CANARY_SHARED_PREFERENCES";
    public static final String LAST_LOCATION_ID = "LAST_LOCATION_ID";

    public static final String GOOGLE_PLACES_URL = "https://maps.googleapis.com";
    public static final String GOOGLE_PLACES_NEARBY_URI = "/maps/api/place/nearbysearch/json";
    public static final String GOOGLE_PLACES_DETAIL_URI = "/maps/api/place/details/json";

    // Wearable
    public static final String PATH_CHANGE_MODE = "/mode_change_data_path";
    public static final String PATH_LOCATION_DATA = "/location_data_path";
    public static final String PATH_OPEN_ENTRY_ACTIVITY = "/entry_open_path";
    public static final String PATH_MODE_FAIL = "/mode_fail";
    public static final String PATH_ERROR = "/error";
    public static final String PATH_ANALYTICS = "/analytics";

    public static final String CLIENT_LOCATION = API_VERSION + "/clientlocation/";

    public static final String DEMO_FLAVOR = "demo";


    public static String SNOWPLOW_ID = "canary20151119";
    public static String SNOWPLOW_URL = "snowplow.canary.is";

    public static final float Kibi = 1024;

    public static final String CANARY_NOTIFICATION_CHANNEL = "canary_notification_channel";


    public static String CANARY_TOS() {
        Context context = CanaryApplication.getContext();
        return context.getString(R.string.website) + context.getString(R.string.terms_link);

    }

    public static String CANARY_PP() {
        Context context = CanaryApplication.getContext();
        return context.getString(R.string.website) + context.getString(R.string.privacy_link);

    }

    public static String CANARY_EULA() {
        Context context = CanaryApplication.getContext();
        return context.getString(R.string.website) + context.getString(R.string.eula_link);

    }

    public static String CANARY_COMPLIANCE() {
        Context context = CanaryApplication.getContext();
        return context.getString(R.string.website) + context.getString(R.string.compliance_link);
    }

    public static String CANARY_WEBSITE() {
        Context context = CanaryApplication.getContext();
        return context.getString(R.string.website);
    }

    public static String CANARY_MODE_PRIVACY() {
        Context context = CanaryApplication.getContext();
        return context.getString(R.string.help_center_privacy_mode);
    }

    public static String CANARY_HELP() {
        Context context = CanaryApplication.getContext();
        return context.getString(R.string.help_center_site);
    }

    public static String CANARY_MEMBERSHIP_HELP() {
        return "http:website-assets.canary.is/resources/Canary_HDR_Terms_and_Conditions.pdf";
    }

    public static String CANARY_RETURNS_AND_WARRANTY() {
        return "https://canary.is/legal/returns-and-warranty/";
    }


    public static String CANARY_INSURANCE() {
        Context context = CanaryApplication.getContext();
        return context.getString(R.string.canary_insurance_url);
    }

    public static String CANARY_DATA_SHARE() {

        Context context = CanaryApplication.getContext();
        return context.getString(R.string.canary_data_share_url);
    }

    public static String CANARY_OTHER_INSURANCE() {

        Context context = CanaryApplication.getContext();
        return context.getString(R.string.other_insurance_url);
    }

    private static Uri.Builder getBaseAutoLogin(@NonNull Context context) {
        Customer customer = CurrentCustomer.getCurrentCustomer();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(context.getString(R.string.scheme))
                .authority(context.getString(R.string.webapp));

        if (customer != null) {
            if (!Locale.ENGLISH.getLanguage().equalsIgnoreCase(customer.languagePreference)) {
                builder.appendEncodedPath(customer.languagePreference);
            }

            builder.appendQueryParameter("email", customer.email);
        }


        builder.appendEncodedPath("api/auth/m2w");

        builder.appendQueryParameter("webview", "true");

        return builder;
    }

    public static String autoLoginAccountUrl(@NonNull Location location, @NonNull Context context) {
        Uri.Builder builder = getBaseAutoLogin(context);

        builder.appendQueryParameter("locationId", String.valueOf(location.id));

        builder.appendQueryParameter("path", "/account/profile");

        return builder.toString();
    }


    public static String autoLoginUrl(int locationId, @NonNull Context context) {
        Uri.Builder builder = getBaseAutoLogin(context);


        builder.appendQueryParameter("locationId", String.valueOf(locationId));

        return builder.toString();
    }

    public static String autoLoginUrl(@NonNull Uri uri, @NonNull Context context) {
        Uri.Builder builder = getBaseAutoLogin(context);


        Set<String> names = uri.getQueryParameterNames();

        for (String name : names) {
            builder.appendQueryParameter(name, uri.getQueryParameter(name));
        }

        builder.appendQueryParameter("path", uri.getPath());


        return builder.toString();
    }

    public static final String FREE_TRAIL = "freetrial";
    public static final String THREE_MONTHS_FREE = "threemonthsfree";

    public static String autoLoginUrlWithPromoCodes(Location location, Context context, boolean promoCode) {

        return autoLoginUrlWithPromoCodes(location, context, false, FREE_TRAIL);

    }


    public static String autoLoginUrlWithPromoCodes(Location location, Context context, boolean promoCode, String code) {
        if (location == null)
            return null;

        boolean isOwner = location.isOwner(CurrentCustomer.getCurrentCustomer());
        if (!isOwner) {
            return autoLoginAccountUrl(location, context);

        }

        Uri.Builder builder = getBaseAutoLogin(context);


        builder.appendQueryParameter("path", "/subscription/checkout/");

        builder.appendQueryParameter("recurrence", "monthly");

        builder.appendQueryParameter("locationId", String.valueOf(location.id));

        if (promoCode && !TextUtils.isEmpty(code)) {
            builder.appendQueryParameter("promoCode", code);
        }

        return builder.toString();

    }

    public static String subscriptionSelect(Location location, Context context, String trial) {
        if (location == null)
            return null;

        boolean isOwner = location.isOwner(CurrentCustomer.getCurrentCustomer());
        if (!isOwner) {
            return autoLoginAccountUrl(location, context);

        }

        Uri.Builder builder = getBaseAutoLogin(context);


        builder.appendQueryParameter("path", "/subscription/select");

        builder.appendQueryParameter("recurrence", "annual");

        builder.appendQueryParameter("locationId", String.valueOf(location.id));

        if (!TextUtils.isEmpty(trial)) {
            builder.appendQueryParameter("campaign", trial);
        }

        return builder.toString();
    }

    public static void updateEndPoint(String url) {

    }


}
