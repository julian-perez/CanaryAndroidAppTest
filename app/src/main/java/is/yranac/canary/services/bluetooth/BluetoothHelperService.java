package is.yranac.canary.services.bluetooth;

/**
 * Created by shreyashirday on 7/30/15.
 */

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.ParcelUuid;

import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.nativelibs.ChirpManager;
import is.yranac.canary.nativelibs.models.messages.BLEError;
import is.yranac.canary.nativelibs.models.messages.bluetooth.BLTEventDeviceConnected;
import is.yranac.canary.nativelibs.models.messages.bluetooth.BLTEventDeviceFound;
import is.yranac.canary.services.bluetooth.constants.BluetoothUUIDConstants;
import is.yranac.canary.services.bluetooth.constants.BluetoothUUIDConstants.Characteristic;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.TinyMessageBus;

import static is.yranac.canary.services.bluetooth.constants.BluetoothUUIDConstants.Descriptor.CHAR_CLIENT_CONFIG;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothHelperService extends BluetoothGattCallback {

    public static final int BLUETOOTH_START_REQUESTED = 5;

    private static final String LOG_TAG = "BluetoothHelperService";
    private static final int RSSI_UPDATE_TIME_INTERVAL = 1500; // 1.5 seconds
    private static final int BTLE_SCAN_TIMEOUT = 60000; //60 secs

    private BluetoothDevice mBluetoothDevice;
    private static final String canaryBluetoothServiceUUID = "00005500-d102-11e1-9b23-00025b00a5a5";
    private BluetoothGattCharacteristic characteristic;

    private ScanCallback scanCallback;

    private BluetoothGatt mBluetoothGatt;
    private BTLEWriter btleWriter;

    public BluetoothDevice getCurrentDevice() {
        return mBluetoothDevice;
    }

    public void resetCurrentDevice() {
        mBluetoothDevice = null;
    }

    private String getCanaryBluetoothServiceUUID() {
        return canaryBluetoothServiceUUID;
    }


    private final BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            if (device.getName() == null)
                return;
            if (device.getAddress().startsWith("7C:70:BC")
                    || device.getAddress().startsWith("D8:42:E2")) {
                TinyMessageBus.post(new BLTEventDeviceFound(device, rssi));
            }

        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    void startScanning() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startLeScan(leScanCallback);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void startFilteredScanning() {

        List<ScanFilter> scanFilters = new ArrayList<>();

        ScanFilter.Builder filterBuilder = new ScanFilter.Builder();
        filterBuilder.setServiceUuid(new ParcelUuid(UUID.fromString(getCanaryBluetoothServiceUUID())));
        ScanFilter filter = filterBuilder.build();
        scanFilters.add(filter);


        ScanSettings.Builder settingsBuilder = new ScanSettings.Builder();
        settingsBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        ScanSettings settings = settingsBuilder.build();
        scanCallback = createScanCallback();
        BluetoothLeScanner bluetoothAdapter = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        if (bluetoothAdapter == null) {
            return;
        }
        bluetoothAdapter.startScan(scanFilters, settings, scanCallback);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void stopFilteredScanning() {
        if (scanCallback != null) {
            BluetoothLeScanner bluetoothAdapter = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
            if (bluetoothAdapter == null) {
                return;
            }
            bluetoothAdapter.stopScan(scanCallback);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScanCallback createScanCallback() {

        return new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                BluetoothDevice device = result.getDevice();

                if (device.getName() == null)
                    return;

                TinyMessageBus.post(new BLTEventDeviceFound(device, result.getRssi()));

            }
        };
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void stopScanning() {
        BluetoothAdapter.getDefaultAdapter().stopLeScan(leScanCallback);
    }

    public boolean isAdapterEnabled() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    public void establishBTLEConnection(String deviceAddress) {
        Log.e("BTLEHelperService", "establishBTLEConnection");
        stopScanningForDevices();
        disconnect();
        //closing the old connection since we're establishing a new one
        BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);
        mBluetoothGatt = bluetoothDevice.connectGatt(CanaryApplication.getContext(), false, this);
    }


    /* disconnect the device. It is still possible to reconnect to it later with this Gatt client */
    public void disconnect() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
        }

        ChirpManager.reset();
    }

    public void startServicesDiscovery() {
        if (mBluetoothGatt != null)
            mBluetoothGatt.discoverServices();
    }

    /* gets services and calls UI callback to handle them
       before calling getServices() make sure service discovery is finished! */
    public void getSupportedServices() {
        if (mBluetoothGatt != null) {
            for (BluetoothGattService service : mBluetoothGatt.getServices()) {
                if (service.getUuid().equals(BluetoothUUIDConstants.Service.SERIAL_SERVICE)) {
                    getCharacteristicsForService(service);
                    break;
                }
            }
        }
    }

    public void getCharacteristic(UUID characteristic) {
        if (mBluetoothGatt != null) {
            for (BluetoothGattService service : mBluetoothGatt.getServices()) {
                if (service.getUuid().equals(BluetoothUUIDConstants.Service.SERIAL_SERVICE)) {
                    List<BluetoothGattCharacteristic> chars = service.getCharacteristics();
                    if (chars == null)
                        continue;

                    for (BluetoothGattCharacteristic c : chars) {
                        if (c.getUuid().equals(characteristic)) {
                            if (mBluetoothGatt.setCharacteristicNotification(c, true)) {
                                if (mBluetoothGatt.readCharacteristic(c)) {
                                    return;
                                }
                            }
                        }
                    }
                    break;
                }
            }
            Log.i(LOG_TAG, "not found");
        }

        TinyMessageBus.post(new BLEError(BLEError.ERROR.PAIR_FAIL, null, null));
    }

    /* get all characteristic for particular service and pass them to the UI callback */

    public void getCharacteristicsForService(final BluetoothGattService service) {
        if (service == null)
            return;

        List<BluetoothGattCharacteristic> chars = service.getCharacteristics();
        for (BluetoothGattCharacteristic c : chars) {
            if (c.getUuid().equals(Characteristic.SERIAL_DATA_TRANSFER)) {
                characteristic = c;
                setNotificationForCharacteristic(characteristic, true);
                break;
            }
        }
    }

    /* set new value for particular characteristic */
    public void sendChunk(byte[] dataToWrite) {
        if (mBluetoothGatt == null || characteristic == null || dataToWrite == null) {
            ChirpManager.reset();
            return;
        }

        if (btleWriter != null && btleWriter.isSendingInProgress()) {
            Log.e("sendChunk", "sending is still in progress");
            return;
        }

        Log.e("sendChunk", dataToWrite + "" + " with length " + dataToWrite.length);

        btleWriter = new BTLEWriter(dataToWrite, characteristic, mBluetoothGatt);
        btleWriter.sendNextChunk();
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (btleWriter != null && btleWriter.isSendingInProgress())
                btleWriter.sendNextChunk();
        }
    }

    /* enables/disables notification for characteristic */
    public void setNotificationForCharacteristic(BluetoothGattCharacteristic ch, boolean enabled) {
        if (mBluetoothGatt == null) {
            Log.i(LOG_TAG, "Gatt null");
            return;
        }

        boolean success = mBluetoothGatt.setCharacteristicNotification(ch, enabled);
        if (!success) {
            Log.e(LOG_TAG, "Setting notification status failed!");
            return;
        }

        // This is also sometimes required (e.g. for heart rate monitors) to enable notifications/indications
        // see: https://developer.bluetooth.org/gatt/descriptors/Pages/DescriptorViewer.aspx?u=org.bluetooth.descriptor.gatt.client_characteristic_configuration.xml
        BluetoothGattDescriptor descriptor = ch.getDescriptor(CHAR_CLIENT_CONFIG);
        if (descriptor != null) {
            Log.i(LOG_TAG, "Writing descriptor " + enabled);
            descriptor.setValue(enabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE :
                    BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        } else {
            Log.i(LOG_TAG, "descriptor null");
        }
    }


    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        Log.i(LOG_TAG, "char changed");
        ChirpManager.chunkReceived(characteristic.getValue());
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        Log.i(LOG_TAG, "onDescriptorWrite " + (status == BluetoothGatt.GATT_SUCCESS) + "");
        if (status == BluetoothGatt.GATT_SUCCESS) {
            TinyMessageBus.post(new BLTEventDeviceConnected(mBluetoothDevice));
        }
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        Log.i(LOG_TAG, "onConnectionStateChange " + String.valueOf(newState));

        if (newState == BluetoothProfile.STATE_CONNECTED) {
            mBluetoothDevice = gatt.getDevice();
            startServicesDiscovery();
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            gatt.close();
            if (mBluetoothDevice != null) {
                TinyMessageBus.post(new BLEError(BLEError.ERROR.DISCONNECT, null, null));
            }
            mBluetoothDevice = null;
        } else {
            TinyMessageBus.post(new BLEError(BLEError.ERROR.PAIR_FAIL, null, null));
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            getSupportedServices();
        }
    }

    public void startScanningForDevices() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startFilteredScanning();
        } else {
            startScanning();
        }
    }

    public void stopScanningForDevices() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            stopFilteredScanning();
        } else {
            stopScanning();
        }
    }

    private String sig;
    private String nonce;

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic,
                                     int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (characteristic.getUuid().equals(Characteristic.SERIAL_SIG)) {
                sig = String.copyValueOf(Hex.encodeHex(characteristic.getValue()));
                getCharacteristic(Characteristic.SERIAL_NONCE);
            } else if (characteristic.getUuid().equals(Characteristic.SERIAL_NONCE)) {
                nonce = String.copyValueOf(Hex.encodeHex(characteristic.getValue()));
                if (sig != null && nonce != null) {
                    ChirpManager.setupNewEncryptionValues(sig, nonce);
                    sig = null;
                    nonce = null;
                }
            }
        }


    }
}