package is.yranac.canary.fragments.setup;

import is.yranac.canary.fragments.StackFragment;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.util.Utils;

abstract public class SetUpBaseFragment extends StackFragment {


    protected static final String BSSID = "bssid";
    protected static final String SSID = "ssid";
    protected static final String WIFI_PASSWORD = "wifi_password";
    protected static final String WIFI_HIDDEN = "hidden_wifi";

    protected static final String CHANGING_WIFI = "changing_wifi";
    protected static final String BLUETOOTH_SETUP = "bluetooth";
    protected static final String BLUETOOTH_CHANGE_WIFI = "bluetooth_change_wifi";

    protected static final String DEVICE_SERIAL = "device_serial";


    protected static final String device_uri = "device_uri";
    protected static final String activation_token = "activation_token";

    protected static final String device_type = "device_type";


    public final static String key_isSetup = "key_isSetup";
    public final static String key_currentDeviceName = "key_currentDeviceName";
    public final static String key_deviceID = "key_deviceID";
    public final static String key_device = "key_device";
    public final static String key_isOutdoor = "key_isOutdoor";
    protected static final String key_location_uri = "location_uri";

    private static boolean showSignIn = false;


    protected boolean showSignIn() {
        return showSignIn;
    }

    protected void setShowIn(boolean showSignIn) {
        SetUpBaseFragment.showSignIn = showSignIn;
    }

    protected void setLocationUri(String locationUri) {
        getArguments().putString(key_location_uri, locationUri);
        getArguments().putInt(location_id, Utils.getIntFromResourceUri(locationUri));
    }

    protected String locationUri() {
        return getArguments().getString(key_location_uri, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        hideSoftKeyboard();
    }

    // We should us isSetup instead of this value
    @Deprecated
    public boolean changingWifi() {
        return getArguments() != null && getArguments().getBoolean(CHANGING_WIFI, false);
    }

    // We should us isSetup instead of this value
    @Deprecated
    public void setChangingWifi(boolean b) {
        getArguments().putBoolean(CHANGING_WIFI, b);
    }

    public boolean isSetup() {
        return getArguments() != null && getArguments().getBoolean(key_isSetup, false);
    }

    public void setIsSetup(boolean b) {
        getArguments().putBoolean(key_isSetup, b);
    }

    protected void setDeviceSerial(String deviceSerial) {
        getArguments().putString(DEVICE_SERIAL, deviceSerial);
    }

    protected String getDeviceSerial() {
        return getArguments().getString(DEVICE_SERIAL);
    }


    protected void setDeviceUri(String deviceUri) {
        getArguments().putString(device_uri, deviceUri);
    }

    protected String getDeviceUri() {
        return getArguments().getString(device_uri);
    }

    protected int getDeviceType() {
        return getArguments().getInt(device_type, DeviceType.CANARY_AIO);

    }
}
