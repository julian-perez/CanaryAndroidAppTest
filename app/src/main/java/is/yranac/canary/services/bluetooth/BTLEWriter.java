package is.yranac.canary.services.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import java.util.Arrays;

/**
 * Created by sergeymorozov on 1/5/16.
 * <p/>
 * This class breaks submessage CHIRP is sending into even smaller pieces
 * and sends them to Canary.
 */
public class BTLEWriter {

    static final int DEFAULT_CHUNK_SIZE = 20;

    volatile byte[] dataToWrite;
    volatile int offset;
    BluetoothGattCharacteristic characteristic;
    BluetoothGatt mBluetoothGatt;

    public BTLEWriter(byte[] dataToWrite, BluetoothGattCharacteristic characteristic, BluetoothGatt mBluetoothGatt) {
        this.dataToWrite = dataToWrite;
        this.characteristic = characteristic;
        this.mBluetoothGatt = mBluetoothGatt;
        this.offset = 0;
    }

    /**
     * sends the next piece of CHIRP message accross BTLE
     */
    public synchronized void sendNextChunk() {

        if (dataToWrite == null || characteristic == null || mBluetoothGatt == null)
            return;

        int chunkEnd = Math.min(DEFAULT_CHUNK_SIZE, dataToWrite.length - offset);
        byte[] submessage = Arrays.copyOfRange(dataToWrite, offset, offset + chunkEnd);
        characteristic.setValue(submessage);

        boolean initiatedSuccessfully = mBluetoothGatt.writeCharacteristic(characteristic);
        if (!initiatedSuccessfully) {
            return;
        }
        offset += DEFAULT_CHUNK_SIZE;
    }

    public boolean isSendingInProgress() {
        return dataToWrite != null && offset < dataToWrite.length;
    }
}
