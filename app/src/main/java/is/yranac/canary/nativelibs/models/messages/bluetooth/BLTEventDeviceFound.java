package is.yranac.canary.nativelibs.models.messages.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Created by sergeymorozov on 10/14/15.
 */
public class BLTEventDeviceFound {
    public final BluetoothDevice device;
    public final int rssi;

    public BLTEventDeviceFound(BluetoothDevice device, int rssi) {
        this.device = device;
        this.rssi = rssi;
    }


}
