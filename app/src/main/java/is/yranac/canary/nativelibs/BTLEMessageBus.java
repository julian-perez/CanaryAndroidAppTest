package is.yranac.canary.nativelibs;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.messages.scheduler.SchedulerCallback;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.nativelibs.models.messages.BLEError;
import is.yranac.canary.nativelibs.models.messages.BLEMessage;
import is.yranac.canary.services.bluetooth.BluetoothSetupProcessHelper;
import is.yranac.canary.services.bluetooth.BluetoothSingleton;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.TinyMessageBus;

import static is.yranac.canary.services.bluetooth.constants.BluetoothUUIDConstants.Characteristic.SERIAL_SIG;

/**
 * Created by sergeymorozov on 10/9/15.
 */
public class BTLEMessageBus {
    private static final String LOG_TAG = "BTLEMessageBus";
    private static BTLEMessageBus messageBus;

    static BTLEMessageBus getMessageBus() {
        if (messageBus == null)
            messageBus = new BTLEMessageBus();
        return messageBus;
    }

    /**
     * This is a function that queues up a message to be send using Chirp.
     *
     * @param message JSON string to send to the device.
     */
    @Subscribe(mode = Subscribe.Mode.Background)
    public void sendBTLEMessage(BLEMessage message) {
        Log.i(LOG_TAG, "senBTLEMessage");
        sendNextMessage(message.getJsonString());
    }

    /**
     * When Chirp schedules a callback, this is what's called.
     *
     * @param callback The type of scheduler to handle
     */
    public static void schedulerCallback(SchedulerCallback callback) {
        if (callback.getSchedulerType() == SchedulerCallback.SchedulerType.ChirpTimeout)
            ChirpManager.handeChirpSchedulerCallback(callback);
    }

    public static void register() {
        TinyMessageBus.register(getMessageBus());
    }

    public static void unregister() {
        TinyMessageBus.unregister(getMessageBus());
    }

    private void sendNextMessage(String message) {
        try {
            if (ChirpManager.isEncryptionSetup()) {
                Log.i(LOG_TAG, "send message");
                ChirpManager.sendMessage(message);
            } else {
                DeviceType type = BluetoothSetupProcessHelper.getDeviceType();

                Log.i(LOG_TAG, String.valueOf(type == null));
                if (type != null && (type.id == DeviceType.CANARY_AIO || type.id == DeviceType.CANARY_VIEW))
                    ChirpManager.setupEncryptionValues();
                else
                    BluetoothSingleton.getBluetoothHelperService().getCharacteristic(SERIAL_SIG);
            }
        } catch (Exception e) {
            TinyMessageBus.post(new BLEError(e.getMessage(), null));
        }
    }
}
