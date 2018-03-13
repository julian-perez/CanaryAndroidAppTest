package is.yranac.canary.nativelibs.models.messages.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Created by sergeymorozov on 10/14/15.
 */
public class BLTEventDeviceConnected {

   private BluetoothDevice device;

    public BluetoothDevice getDevice() {
        return device;
    }

    public BLTEventDeviceConnected(BluetoothDevice device){
        this.device = device;

    }
}
