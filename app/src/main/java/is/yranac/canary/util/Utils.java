package is.yranac.canary.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLHandshakeException;

import is.yranac.canary.BuildConfig;
import is.yranac.canary.CanaryApplication;
import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.messages.InvalidCert;
import is.yranac.canary.model.device.DeviceType;
import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;

public class Utils {

    private static final String LOG_TAG = "Utils";

    public static short[] concatenateShortArrays(short[] a, short[] b) {
        short[] result = new short[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public static boolean deviceImageUrlEquals(String imageUrl1, String imageUrl2) {
        if (imageUrl1 == null)
            return false;

        if (imageUrl2 == null)
            return true;

        String baseImage1Url = imageUrl1.split("Signature=")[0];
        String baseImage2Url = imageUrl2.split("Signature=")[0];

        return baseImage1Url.equalsIgnoreCase(baseImage2Url);

    }

    public static boolean isForInternalTestingAndDevelopment() {
        return (isNotProd() && !isBeta() && !isStage()) || BuildConfig.DEBUG;
    }

    public static boolean isForMaskingTestingAndDevelopment() {
        return (isNotProd() && !isBeta()) || BuildConfig.DEBUG;
    }


    public static boolean isNotProd() {
        return !BuildConfig.FLAVOR.contains("prod");
    }

    public static boolean isDemo() {
        return BuildConfig.FLAVOR.equalsIgnoreCase(Constants.DEMO_FLAVOR);
    }

    public static boolean isDev() {
        return BuildConfig.FLAVOR.contains("dev");
    }

    public static boolean isBeta() {
        return BuildConfig.FLAVOR.contains("beta");
    }

    public static boolean isSerinus() {
        return BuildConfig.FLAVOR.contains("feature");

    }

    public static boolean isProdBeta() {
        return BuildConfig.FLAVOR.equalsIgnoreCase("beta");
    }

    public static boolean isStage() {
        return BuildConfig.FLAVOR.contains("stage");
    }


    public static long findDayForDate(Date date) {
        Calendar cal = DateUtil.getCalanderInstance();

        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

    public static void v(String msg) {
        Log.v(LOG_TAG, msg);
    }

    public static void e(String msg) {
        Log.e(LOG_TAG, msg);
    }


    public static boolean canDial(Context context) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);

        List<ResolveInfo> callAppsList =
                context.getPackageManager().queryIntentActivities(callIntent, 0);

        return !callAppsList.isEmpty();
    }

    public static boolean isWifiEnabled(Context context) {
        if (Utils.isDemo())
            return true;

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifi != null && wifi.isWifiEnabled();
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }

        return false;
    }


    public static String buildResourceUri(String resource, long id) {

        if (resource == null)
            return null;

        StringBuilder builder = new StringBuilder(resource);

        builder.append(id)
                .append("/");
        return builder.toString();
    }

    public static String buildResourceUri(String resource, String id) {
        if (id == null || resource == null)
            return null;
        StringBuilder builder = new StringBuilder(resource);

        builder.append(id)
                .append("/");
        return builder.toString();
    }

    public static int getIntFromResourceUri(String resourceUri) {
        if (TextUtils.isEmpty(resourceUri))
            return 0;
        String[] resourceUriSeparated = resourceUri.split("/");
        if (resourceUriSeparated.length < 1) {
            return 0;
        }
        String id = resourceUriSeparated[resourceUriSeparated.length - 1];
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return 0;
        }
        // only got here if we didn't return false
    }

    public static long getLongFromResourceUri(String resourceUri) {
        if (StringUtils.isNullOrEmpty(resourceUri))
            return 0;
        String[] resourceUriSeparated = resourceUri.split("/");
        if (resourceUriSeparated.length < 1) {
            return 0;
        }
        String id = resourceUriSeparated[resourceUriSeparated.length - 1];
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            return 0;
        }
        // only got here if we didn't return false
    }


    public static String getStringFromResourceUri(String resourceUri) {
        String[] resourceUriSeparated = resourceUri.split("/");
        if (resourceUriSeparated.length >= 1) {
            return resourceUriSeparated[resourceUriSeparated.length - 1];
        }
        return "";
    }

    public static int canaryAnimationDuration() {
        // Retrieve the system's default "short" animation time.
        int mShortAnimationDuration = CanaryApplication.getContext()
                .getResources()
                .getInteger(android.R.integer.config_shortAnimTime);

        return mShortAnimationDuration * 3;
    }

    public static void fadeIn(View inView) {
        crossFadeViews(inView, null);
    }

    public static void fadeOut(View outView) {
        crossFadeViews(null, outView);
    }


    public static void crossFadeViews(View inView, final View outView) {
        crossFadeViews(inView, outView, 0f);
    }

    public static void crossFadeViews(View inView, final View outView, final float fadeToAlpha) {
        // Start the fade in from the current alpha - unless it starts fully visible.
        // In this case set the content view to 0% opacity but visible, so that it starts
        // visible (but fully transparent) for the animation.
        if (inView != null) {
            if (inView.getAlpha() == 1f) {
                inView.setAlpha(0f);
            }
            inView.setVisibility(View.VISIBLE);

            // Animate the content view to 100% opacity, and clear any animation
            // listener set on the view.
            inView.animate()
                    .alpha(1f)
                    .setDuration(canaryAnimationDuration())
                    .setListener(null);
        }

        // Animate the outgoing view to the fadeToAlpha. After the animation ends,
        // if the fadeToAlpha == 0f then set its visibility to GONE as an optimization step
        // (it won't participate in layout passes, etc.)
        if (outView != null) {
            outView.animate()
                    .alpha(fadeToAlpha)
                    .setDuration(canaryAnimationDuration())
                    .setListener(
                            new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    if (fadeToAlpha == 0f) {
                                        outView.setVisibility(View.GONE);
                                    }
                                }
                            }
                    );
        }
    }

    // *************************************************************
    // *** Add Fragment Utilities
    // *************************************************************
    public static final int NO_ANIMATION = 0;
    public static final int SLIDE_FROM_RIGHT = 1;
    public static final int SLIDE_FROM_BOTTOM = 2;
    public static final int CROSSFADE = 3;
    public static final int FADE_IN_DISAPPEAR = 4;
    public static final int NO_PUSH_ANIMATION = 5;

    public static void addFragmentToStack(FragmentManager fm, Fragment fragment, String tag, int animation) {
        addFragmentToStack(fm, fragment, tag, animation, false);
    }

    public static void addModalFragmentToStack(FragmentManager fm, Fragment fragment, String tag) {
        addModalFragmentToStack(fm, fragment, tag, true);
    }


    public static void addModalFragmentToStack(FragmentManager fm, Fragment fragment, String tag, boolean animated) {
        int enter, exit, popEnter, popExit;

        exit = R.anim.none;
        popExit = R.anim.slide_out_bottom;
        popEnter = R.anim.none;

        if (animated) {
            enter = R.anim.slide_in_bottom;
        } else {
            enter = R.anim.none;
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction
                .setCustomAnimations(enter, exit, popEnter, popExit)
                .add(R.id.modal_view_frame_layout, fragment, tag)
                .addToBackStack(tag);

        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void addSecondModalFragmentToStack(FragmentManager fm, Fragment fragment, String tag) {
        int enter, exit, popEnter, popExit;
        enter = R.anim.slide_in_right;
        exit = R.anim.slide_out_left;
        popEnter = R.anim.slide_in_left;
        popExit = R.anim.slide_out_right;
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction
                .setCustomAnimations(enter, exit, popEnter, popExit)
                .replace(R.id.modal_view_frame_layout, fragment, tag)
                .addToBackStack(tag);

        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void addFragmentToStack(FragmentManager fm, Fragment fragment, String tag, int animation, boolean bottom) {


        if (bottom) {
            FragmentUtils.sDisableFragmentAnimations = true;
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentUtils.sDisableFragmentAnimations = false;
        }

        int enter, exit, popEnter, popExit;
        enter = exit = popEnter = popExit = 0;

        switch (animation) {
            case SLIDE_FROM_RIGHT:
                enter = R.anim.slide_in_right;
                exit = R.anim.slide_out_left;
                popEnter = R.anim.slide_in_left;
                popExit = R.anim.slide_out_right;
                break;
            case SLIDE_FROM_BOTTOM:
                enter = R.anim.slide_in_bottom;
                exit = R.anim.slide_out_bottom;
                popEnter = R.anim.slide_in_bottom;
                popExit = R.anim.slide_out_bottom;
                break;
            case CROSSFADE:
                enter = android.R.anim.fade_in;
                exit = android.R.anim.fade_out;
                popEnter = android.R.anim.fade_in;
                popExit = android.R.anim.fade_out;
                break;
            case FADE_IN_DISAPPEAR:
                enter = android.R.anim.fade_in;
                exit = 0;
                popEnter = 0;
                popExit = 0;
                break;
            case NO_PUSH_ANIMATION:
                enter = 0;
                exit = 0;
                popEnter = R.anim.slide_in_left;
                popExit = R.anim.slide_out_right;
                break;

        }

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction
                .setCustomAnimations(enter, exit, popEnter, popExit);

        if (!bottom) {
            fragmentTransaction.replace(R.id.graph_scroll_view, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
        } else {
            fragmentTransaction.replace(R.id.graph_scroll_view, fragment, null);

        }
        fragmentTransaction.commitAllowingStateLoss();
        fm.executePendingTransactions();

    }

    public static String getErrorMessageFromRetrofit(Context context, RetrofitError error) throws JSONException {

        if (error.getCause() != null) {
            if (error.getCause() instanceof SSLHandshakeException && context != null) {

                TinyMessageBus.post(new InvalidCert());
                return "";
            }
        }

        String json;
        try {
            if (error.getBody() == null || error.getResponse() == null)
                return "Unable to connect to server";

            json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
        } catch (Exception e) {
            return "Invalid response";
        }


        JSONObject jsonObject = new JSONObject(json);
        return getErrorMessage(jsonObject);
    }

    /**
     * Retrieve the error message from a Message Body
     *
     * @return The error message from the API
     */
    public static String getErrorMessage(JSONObject jsonObject) throws JSONException {

        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            Object object = jsonObject.get(keys.next());
            if (object instanceof String) {
                return (String) object;
            } else if (object instanceof JSONObject) {
                return getErrorMessage((JSONObject) object);
            } else if (object instanceof JSONArray) {
                return (String) ((JSONArray) object).get(0);
            }
        }
        return "";
    }

    /**
     * Method to check if there currently is Internet
     *
     * @return true if there is an internet connection, false otherwise
     */
    public static boolean hasInternetConnection(Context context) {

        if (Utils.isDemo())
            return true;
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();


        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Password Utils
     */


    public static boolean goodPassword(String email, String password, Context context) {

        if (!Utils.checkPasswordLength(context, password)) {
            return false;
        }

        if (!Utils.checkPasswordCharacters(context, password)) {
            return false;
        }

        if (Utils.passwordUsernameMatch(context, email, password)) {
            return false;
        }

        return true;
    }

    public static boolean checkPasswordLength(Context context, String password) {
        if (checkPasswordLength(password)) {
            return true;
        } else {
            AlertUtils.showGenericAlert(context, context.getString(R.string.password_strength),
                    context.getString(R.string.password_eight_characters));
            return false;
        }
    }

    public static boolean checkPasswordLength(String password) {
        return (password != null && password.length() > 7);
    }

    public static boolean checkPasswordCharacters(Context context, String password) {
        if (checkPasswordCharacters(password)) {
            return true;
        } else {
            AlertUtils.showGenericAlert(context, context.getString(R.string.password_strength),
                    context.getString(R.string.password_special_character));
            return false;
        }
    }


    public static boolean checkPasswordCharacters(String password) {
        String charSet = "[!\\#$%&'()*+-./:;<=>?[]^_`{|}~´]@01234567890]";
        Pattern p = Pattern.compile(charSet);
        Matcher matcher = p.matcher(password);

        return matcher.find();
    }

    public static boolean passwordUsernameMatch(Context context, String username, String password) {
        if (passwordUsernameMatch(username, password)) {
            AlertUtils.showGenericAlert(context, context.getString(R.string.whoops_try_again),
                    context.getString(R.string.email_password_match));
            return true;
        } else {
            return false;
        }
    }

    public static boolean passwordUsernameMatch(String username, String password) {
        return username.equalsIgnoreCase(password);
    }

    public static int roundDownNearest(int n, int mulitple) {

        int mod = n % mulitple;
        return n + (mulitple - mod);
    }

    public static int roundUpNearest(int n, int mulitple) {

        int mod = n % mulitple;
        return n - mod;
    }


    public static String loadJSONFromRawFile(int rawId) {
        InputStream is = CanaryApplication.getContext().getResources().openRawResource(rawId);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }

        return writer.toString();
    }

    public static int getRelativeLeft(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }

    public static int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }


    public static String getDeviceName() {
        final String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;
        final String sdk = Build.VERSION.RELEASE;

        Context context = CanaryApplication.getContext();
        String version = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String versionName = pInfo.versionName;
            version = "\nVersion " + versionName;
            if (Constants.ZENDESK_TESTING) {
                version += "\n Build Number " + String.valueOf(BuildConfig.VERSION_CODE);
            }
        } catch (Exception ignore) {
        }
        if (model.startsWith(manufacturer)) {
            return model.toUpperCase() + "\n" + sdk;
        }
        if (manufacturer.equalsIgnoreCase("HTC")) {
            // make sure "HTC" is fully capitalized.
            return "HTC\n" + model + "\n" + sdk;
        }
        return manufacturer.toUpperCase(Locale.getDefault()) + "\n" + model + "\n" + sdk + version;
    }

    /**
     * Attempts to determine DeviceType from a device Serial Number. ex:
     * <p/>
     * Models and Serial number examples:
     * <p/>
     * Canary first generation		C100A1412345 || C999C1489239
     * Bowery				        C200G1600125
     * Crosby				        C600H1600014
     *
     * @return if can figure it out, returns a {@link DeviceType} or null otherwise.
     */

    public static DeviceType getDeviceTypeFromDeviceSerialNumber(String serialNumber) {
        if (TextUtils.isEmpty(serialNumber))
            return new DeviceType(DeviceType.CANARY_AIO);

        String serialChars = serialNumber.substring(1, 2);
        if (TextUtils.isEmpty(serialChars))
            return new DeviceType(DeviceType.CANARY_AIO);

        Log.i(LOG_TAG, serialChars);
        try {
            int type = Integer.parseInt(serialChars);

            if (type == 1 || type == 9)
                return new DeviceType(DeviceType.CANARY_AIO);
            if (type == 2)
                return new DeviceType(DeviceType.CANARY_PLUS);
            if (type == 4)
                return new DeviceType(DeviceType.CANARY_VIEW);
            if (type == 6)
                return new DeviceType(DeviceType.FLEX);
        } catch (NumberFormatException ignored) {
        }
        return new DeviceType(DeviceType.CANARY_AIO);

    }

    public static boolean hasStandbyMode() {
        return isNotProd();
    }
}
