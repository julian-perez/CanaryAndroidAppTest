package is.yranac.canary.services.bluetooth;

import android.util.Log;

import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.nativelibs.models.messages.BLEMessageRequest;
import is.yranac.canary.nativelibs.models.messages.setup.BLESetupAuthCredsRequest;
import is.yranac.canary.nativelibs.models.messages.setup.BLESetupInitRequest;
import is.yranac.canary.nativelibs.models.messages.setup.BLESetupSSIDRequest;
import is.yranac.canary.nativelibs.models.messages.setup.BLESetupWiFiCredsRequest;
import is.yranac.canary.util.TinyMessageBus;

/**
 * Created by shreyashirday on 7/31/15.
 */
public class BluetoothSetupProcessHelper {

    private final String LOG_TAG = "BluetoothCommunicator";
    private static String deviceSerial;
    private static DeviceType deviceType;
    private static boolean resetProcess;


    public void startTheProcess(String deviceToken, DeviceType type) {
        deviceSerial = deviceToken;
        deviceType = type;
        BLEMessageRequest request = new BLEMessageRequest(new BLESetupInitRequest());
        TinyMessageBus.post(request);
    }
    
    public void sendRequestForWifiList() {
        BLEMessageRequest request = new BLEMessageRequest(new BLESetupSSIDRequest());
        TinyMessageBus.post(request);
    }

    public void sendWifiCredentials(String chosenSsid, String password, boolean deactivate) {
        Log.d(LOG_TAG, "sendWifiCredentials;");

        BLEMessageRequest request = new BLEMessageRequest(new BLESetupWiFiCredsRequest(chosenSsid, password, deactivate));
        TinyMessageBus.post(request);
    }

    public void sendAuthToken(String token, boolean deactivate) {
        Log.d(LOG_TAG, "sendAuthToken;");
        BLEMessageRequest request = new BLEMessageRequest(new BLESetupAuthCredsRequest(token, deactivate));
        TinyMessageBus.post(request);
    }

    public static String getBTLEDeviceSerial(){
        return deviceSerial;
    }

    public static DeviceType getDeviceType(){
        return deviceType;
    }
}

