package is.yranac.canary.nativelibs;

import android.util.Log;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import is.yranac.canary.exceptions.ChirpException;
import is.yranac.canary.messages.scheduler.SchedulerCallback;
import is.yranac.canary.model.EncryptionToken;
import is.yranac.canary.model.EncryptionTokenResponse;
import is.yranac.canary.nativelibs.models.messages.BLEError;
import is.yranac.canary.nativelibs.models.messages.BLEMessageResponse;
import is.yranac.canary.nativelibs.models.messages.BLEResentRequest;
import is.yranac.canary.nativelibs.nativeclasses.Chirp;
import is.yranac.canary.services.api.EncryptionAPIService;
import is.yranac.canary.services.bluetooth.BluetoothHelperService;
import is.yranac.canary.services.bluetooth.BluetoothSetupProcessHelper;
import is.yranac.canary.services.bluetooth.BluetoothSingleton;
import is.yranac.canary.util.SchedulerUtil;
import is.yranac.canary.util.TinyMessageBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sergeymorozov on 10/6/15.
 */
public class ChirpManager {
    private static final String LOG_TAG = "ChirpManager";
    private static SecretKeySpec secretKeySpec;
    private static IvParameterSpec ivEncrypt;

    /**
     * Sending a message using Chirp. This passed the full JSON message into Chirp and starts the
     * process by calling {@link #manageTimer()}, which calls {@link Chirp#chirpNextTOms()} that
     * kickes it off.
     *
     * @param jsonString json strign to send. In UTF-8 format.
     * @throws ChirpException
     */
    public static synchronized void sendMessage(String jsonString) throws ChirpException {
        Log.i("sendMessage", jsonString);
        try {
            byte[] msg = encrypt(jsonString.getBytes("UTF-8"));
            int chirpCode = Chirp.instance().chirpTx(msg, false);
            if (chirpCode < 0)
                throw new ChirpException(chirpCode, null);

        } catch (ChirpException ce) {
            throw ce;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ChirpException(-6, e.getMessage());
        }
        manageTimer();
    }

    public static synchronized void chunkReceived(byte[] chunk) {

        int code = Chirp.instance().chirpRx(chunk);
        Log.i("chunkReceived", code + "");
        manageTimer();
    }

    /**
     * Chirp calls this when it assembles a full JSON message that was send by the device. It's in
     * UTF-8 format.
     *
     * @param message Full message recieved from the device, fully assembled by Chirp. Should
     *                contain a valid JSON object.
     */
    public static synchronized void chirpMessageReceived(byte[] message) {
        if (ivEncrypt == null) {
            TinyMessageBus.post(
                    new BLEError("Received a message from the device but cant decrypt it!",
                            null));
            reset();
            return;
        }
        try {
            String jsonString = getUTFStringFromBytes(decrypt(message));
            BLEMessageResponse messageResponse = new BLEMessageResponse(jsonString);
            TinyMessageBus.post(messageResponse);
        } catch (Exception e) {
            TinyMessageBus.post(new BLEError(e.getMessage(), e));
        }
    }

    /**
     * Chirp library uses this method to send a part of JSON message it received through
     * {@link Chirp#chirpTx(byte[])}.
     *
     * @param chunk Part of the JSON message to send to the device
     */
    public static synchronized void sendChunk(byte[] chunk) {
        getBluetoothHelerService().sendChunk(chunk);
    }

    /**
     * Callback to indicate that Canary device recieved the last message that was sent. It lets the
     * message queue know to send a next message.
     */
    public static synchronized void messageGoneThrough() {
        Log.i("messageGoneThrough", "message ok");
    }

    /**
     * Chirp uses this to get system time to manage it's callback schedule (or something)
     *
     * @return current time in milliseconds.
     */
    public static synchronized long getTimeMs() {
        return System.currentTimeMillis();
    }

    /**
     * This is called to let Chirp know when the time it requested has passed.
     *
     * @param callback type of callback requested
     */
    public static synchronized void handeChirpSchedulerCallback(SchedulerCallback callback) {
        manageTimer();
    }

    /**
     * Chirp calls this when setting up encryption
     *
     * @param deviceIv this is what sould be used to decrypt messages
     */
    public static synchronized void chirpEncryptionIVRecieved(byte[] deviceIv) {
        TinyMessageBus.post(new BLEResentRequest());
    }

    /**
     * Step 1 in setting up values for encryption: getting encryption key from the Cloud
     */
    public static void setupEncryptionValues() {
        Log.i("haveEncryptionKey", "Grabbing encryption key");

        EncryptionAPIService.getEncryptionToken(BluetoothSetupProcessHelper.getBTLEDeviceSerial(), new Callback<EncryptionTokenResponse>() {
            @Override
            public void success(EncryptionTokenResponse encryptionTokenResponse, Response response) {
                try {
                    if (encryptionTokenResponse.memberships.isEmpty())
                        throw new Exception();

                    EncryptionToken r = encryptionTokenResponse.memberships.get(0);
                    byte[] encryptionkey = Hex.decodeHex(r.value.toCharArray());
                    byte[] seed = new byte[16];
                    new Random().nextBytes(seed);
                    setUpIvEncrypt(encryptionkey, seed, seed);
                } catch (Exception e) {
                    TinyMessageBus.post(
                            new BLEError(BLEError.ERROR.BLEK_KEY, e.getMessage(), e));
                    BluetoothSingleton.reset();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                TinyMessageBus.post(
                        new BLEError(BLEError.ERROR.BLEK_KEY, error.getMessage(), error));
                BluetoothSingleton.reset();
            }
        });
    }

    public static void setupNewEncryptionValues(String sig, final String nonce) {
        EncryptionAPIService.getEncryptionToken(BluetoothSetupProcessHelper.getBTLEDeviceSerial(), sig, nonce, new Callback<EncryptionTokenResponse>() {
            @Override
            public void success(EncryptionTokenResponse encryptionTokenResponse, Response response) {
                try {
                    if (encryptionTokenResponse.memberships.isEmpty())
                        throw new Exception();
                    EncryptionToken r = encryptionTokenResponse.memberships.get(0);
                    byte[] encryptionkeyHex = Hex.decodeHex(r.symKey.toCharArray());
                    byte[] tempPubHex = Hex.decodeHex(r.tempPub.toCharArray());
                    byte[] nonceHex = Hex.decodeHex(nonce.toCharArray());
                    setUpIvEncrypt(encryptionkeyHex, tempPubHex, nonceHex);
                } catch (Exception e) {
                    TinyMessageBus.post(
                            new BLEError(BLEError.ERROR.BLEK_KEY, e.getMessage(), e));
                    BluetoothSingleton.reset();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                TinyMessageBus.post(
                        new BLEError(BLEError.ERROR.BLEK_KEY, error.getMessage(), error));
                BluetoothSingleton.reset();
            }
        });
    }

    /**
     * Step 2 in setting up values for encryption: setting up the ivEncrypt and passing it to the device
     * so if can decrypt messages we encrypt. For plus and flex, this allows the device to be able to generate
     * the key that we have based upon the temporary public key we are able to pass to it
     */

    private static void setUpIvEncrypt(byte[] encryptionkey, byte[] tempPub, byte[] nonce) throws NoSuchAlgorithmException {
        secretKeySpec = new SecretKeySpec(encryptionkey, "AES");
        ivEncrypt = new IvParameterSpec(nonce);
        Chirp.instance().chirpTx(tempPub, true);
    }

    public static void reset() {
        Chirp.instance().reset();
        ivEncrypt = null;
    }

    public static boolean isEncryptionSetup() {
        return ivEncrypt != null;
    }

    private static byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher();
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivEncrypt);
        return cipher.doFinal(data);
    }

    private static byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher();
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivEncrypt);
        return cipher.doFinal(data);
    }

    private static void manageTimer() {
        Log.i(LOG_TAG, "manageTimer");
        long chirpNextTO = Chirp.instance().chirpNextTOms();
        if (chirpNextTO > 1) {
            SchedulerUtil.scheduleExclusiveCallback(SchedulerCallback.SchedulerType.ChirpTimeout, chirpNextTO);
        } else if (chirpNextTO == 1) {
            Chirp.instance().chirpHandleTO();
            SchedulerUtil.scheduleExclusiveCallback(SchedulerCallback.SchedulerType.ChirpTimeout, 4);
        }
    }

    private static String getUTFStringFromBytes(byte[] bytes) {
        String d;
        try {
            d = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            d = e.getMessage();
            TinyMessageBus.post(new BLEError("can't convert String to bytes", e));
        }
        return d;
    }

    private static BluetoothHelperService getBluetoothHelerService() {
        return BluetoothSingleton.getBluetoothHelperService();
    }

    private static Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance("AES/CBC/PKCS7Padding");
    }
}
