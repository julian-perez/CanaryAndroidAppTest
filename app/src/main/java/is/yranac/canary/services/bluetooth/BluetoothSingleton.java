package is.yranac.canary.services.bluetooth;

import android.content.pm.PackageManager;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.nativelibs.ChirpManager;

public class BluetoothSingleton {

    private static final String LOG_TAG = "BluetoothSingleton";
    private static BluetoothHelperService bluetoothHelperService;
    private static BluetoothSetupProcessHelper bluetoothSetupProcessHelper;

    public static void reset() {
        if (checkBleHardwareAvailable()) {
            getBluetoothHelperService().disconnect();
            ChirpManager.reset();
            bluetoothHelperService = null;
        }
    }

    public static BluetoothHelperService getBluetoothHelperService() {
        if (bluetoothHelperService == null) {
            bluetoothHelperService = new BluetoothHelperService();
        }
        return bluetoothHelperService;
    }

    public static BluetoothSetupProcessHelper getBluetoothSetupHelper() {
        if (bluetoothSetupProcessHelper == null)
            bluetoothSetupProcessHelper = new BluetoothSetupProcessHelper();
        return bluetoothSetupProcessHelper;
    }

    /* run test and check if this device has BT and BLE hardware available */
    public static boolean checkBleHardwareAvailable() {
        return CanaryApplication.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);

    }
}
