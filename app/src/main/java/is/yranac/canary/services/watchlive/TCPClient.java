package is.yranac.canary.services.watchlive;

import android.content.Context;
import android.net.SSLCertificateSocketFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import WL.Wl2;
import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.messages.watchlive.ByteMessage;
import is.yranac.canary.messages.watchlive.TCPDisconnect;
import is.yranac.canary.messages.watchlive.TCPRetry;
import is.yranac.canary.model.WatchLiveSession;
import is.yranac.canary.model.WatchLiveSessionResponse;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.TinyMessageBus;

import static WL.Wl2.Channel.AUDIO;
import static WL.Wl2.Channel.CMD;
import static WL.Wl2.Channel.VIDEO;
import static WL.Wl2.CommandType.SHUTDOWN_REQUEST;
import static WL.Wl2.Shutdown.Reason.CLIENT_CLOSE;

/**
 * Created by michaelschroeder on 3/16/17.
 */

public class TCPClient {

    public static final int SERVER_PORT = 443;
    private static final byte META_TALKING = 0x02;

    public static final int HEADER_SIZE = 4;

    private static final String LOG_TAG = "TCPClient";

    // sends message received notifications
    private final OnMessageReceived mMessageListener;
    // while this is true, the server will continue running
    private volatile boolean mRun = false;
    private volatile boolean gotVideo = false;

    private final Context context;

    private OutputStream outputStream;
    private boolean muteVolume = false;
    private volatile boolean sendShutdown = true;
    private Socket socket;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(OnMessageReceived listener, Context context) {
        mMessageListener = listener;
        this.context = context;
    }

    /**
     * Sends the message entered by client to the server
     */
    public void sendMessage(final byte[] commandMessageBytes, final Wl2.Channel cmd) {
        sendMessage(new ByteMessage(commandMessageBytes, cmd, false));
    }

    @Subscribe(mode = Subscribe.Mode.Background)
    public void sendMessage(ByteMessage message) {

        try {
            byte[] nextMessage = message.getFullMessage();
            try {
                if (socket != null && socket.isClosed()) {
                    return;
                }
                if (outputStream != null) {
                    outputStream.write(nextMessage);
                }

                if (message.close) {

                    if (socket == null) {
                        mRun = false;
                        return;
                    }
                    try {
                        if (socket.isConnected() && !socket.isClosed()) {
                            socket.shutdownOutput();
                            socket.shutdownInput();
                        }
                        sendShutdown = false;
                        mRun = false;
                    } catch (Exception ignored) {

                    }
                    try {
                        socket.close();

                    } catch (IOException ignore) {
                        ignore.printStackTrace();
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        if (!isRuning())
            return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                sendShutdown = true;
                Wl2.Shutdown.Builder builder = Wl2.Shutdown.newBuilder().setReason(CLIENT_CLOSE);
                final Wl2.CommandMessage.Builder builder1 = Wl2.CommandMessage.newBuilder().setShutdown(builder).setCommandType(SHUTDOWN_REQUEST);
                sendMessage(new ByteMessage(builder1.build().toByteArray(), CMD, true));
            }
        }).start();


    }

    public void cancel() {

        if (!isRuning())
            return;

        mRun = false;
        sendShutdown = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ignored) {

                    }
                }
            }
        }).start();
    }

    public boolean isRuning() {
        return mRun && outputStream != null;
    }

    public void run(Device currentDevice) {

        mRun = true;

        WatchLiveSessionResponse watchLiveSessionResponse;
        socket = null;
        try {

            watchLiveSessionResponse = DeviceAPIService.getWatchLiveSession(currentDevice.uuid);

            Log.i(LOG_TAG, "got session");

            if (watchLiveSessionResponse.sessions.isEmpty())
                throw new RuntimeException("No session");


            TinyMessageBus.postDelayed(new TCPRetry(), 10000);

            WatchLiveSession watchLiveSession = watchLiveSessionResponse.sessions.get(0);

            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(watchLiveSession.host);

            //create a socket to make the connection with the server

            SSLSocketFactory factory = getSSLSocketFactory(!PreferencesUtils.trustsConnection());

            Log.i(LOG_TAG, "creating socket");

            socket = factory.createSocket(serverAddr, SERVER_PORT);

            Log.i(LOG_TAG, "created socket");

            //sends the message to the server
            outputStream = socket.getOutputStream();
            Customer customer = CurrentCustomer.getCurrentCustomer();
            if (customer == null)
                throw new RuntimeException("No user");

            Wl2.AuthorizationRequest authorizationRequest = Wl2.AuthorizationRequest.
                    newBuilder().
                    setSessionKey(watchLiveSession.token).
                    setUserId(customer.id).build();

            Wl2.CommandMessage commandMessage = Wl2.CommandMessage.newBuilder().
                    setCommandType(Wl2.CommandType.AUTHORIZATION).
                    setAuthorizationRequest(authorizationRequest).build();

            sendMessage(commandMessage.toByteArray(), CMD);


            while (mRun) {

                BufferMessage bufferMessage = getMessage(socket.getInputStream());

                if (bufferMessage.getChannel() == VIDEO) {

                    TinyMessageBus.cancel(TCPRetry.class);
                    handleMessage(bufferMessage, customer);
                    gotVideo = true;

                    // Continue to next block, so that we don't keep checking after the condition is
                    // satisfied
                    break;
                }
                handleMessage(bufferMessage, customer);

            }

            while (mRun) {
                BufferMessage bufferMessage = getMessage(socket.getInputStream());
                handleMessage(bufferMessage, customer);
            }

            if (sendShutdown) {
                Wl2.Shutdown.Builder builder = Wl2.Shutdown.newBuilder().setReason(CLIENT_CLOSE);
                final Wl2.CommandMessage.Builder builder1 = Wl2.CommandMessage.newBuilder().setShutdown(builder).setCommandType(SHUTDOWN_REQUEST);
                sendMessage(new ByteMessage(builder1.build().toByteArray(), CMD, true));
            }
        } catch (Exception ignored) {
        } finally {
            //the socket must be closed. It is not possible to reconnect to this socket
            // after it is closed, which means a new socket instance has to be created.


            if (socket != null) {

                try {
                    if (socket.isConnected() && !socket.isClosed()) {
                        socket.shutdownOutput();
                        socket.shutdownInput();
                    }
                } catch (Exception ignored) {

                }
                try {
                    if (socket.isConnected()) {
                        socket.close();
                    }
                } catch (IOException ignored) {

                }
            }

        }

        if (mRun) {
            TinyMessageBus.postDelayed(new TCPDisconnect(), 2000);

        }
        mRun = false;

    }

    private void handleMessage(BufferMessage bufferMessage, Customer customer) {
        if (bufferMessage != null) {

            if (bufferMessage.getChannel() == CMD) {
                Wl2.CommandMessage receivedCommandMessage = bufferMessage.getCommandMessage();
                if (receivedCommandMessage != null) {
                    TinyMessageBus.post(receivedCommandMessage);
                }
            } else {


                if (bufferMessage.getChannel() == AUDIO &&
                        (bufferMessage.meta & META_TALKING) == META_TALKING &&
                        bufferMessage.customer == customer.id && muteVolume) {

                } else if (mMessageListener != null) {
                    mMessageListener.messageMediaReceived(bufferMessage);
                }
            }
        }
    }


    public BufferMessage getMessage(InputStream inputStream) throws IOException {

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 4;
        byte[] header = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer

        int status = fillBufferCompletely(inputStream, header);

        if (status == -1) {
            throw new IOException("server disconnect");
        }

        byte[] packetSizeBytes = new byte[HEADER_SIZE];


        System.arraycopy(header, 0, packetSizeBytes, 2, 2);


        int packetSize = byteArrayToInt(packetSizeBytes);
        byte channel = header[2];
        byte meta = header[3];


        int customer = 0;
        if (channel == AUDIO.getNumber() && (meta & META_TALKING) == META_TALKING) {
            byte[] customerbytes = new byte[HEADER_SIZE];
            status = fillBufferCompletely(inputStream, customerbytes);
            if (status == -1) {
                throw new IOException("server disconnect");
            }
            customer = byteArrayToInt(customerbytes);

            packetSize -= HEADER_SIZE;

        }
        byte[] packets = new byte[packetSize];

        status = fillBufferCompletely(inputStream, packets);

        if (status == -1) {
            throw new IOException("server disconnect");
        }


        return new BufferMessage(channel, packets, meta, customer);


    }

    private static int fillBufferCompletely(InputStream is, byte[] bytes) throws IOException {
        int size = bytes.length;
        int offset = 0;
        while (offset < size) {
            int read = is.read(bytes, offset, size - offset);
            if (read == -1) {
                if (offset == 0) {
                    return -1;
                } else {
                    return offset;
                }
            } else {
                offset += read;
            }
        }

        return size;
    }

    public static int byteArrayToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static byte[] toBytes(int i) {
        byte[] result = new byte[2];

        result[0] = (byte) (i >> 8);
        result[1] = (byte) (i /*>> 0*/);

        return result;
    }

    private static int socketTimeout = (int) TimeUnit.SECONDS.toMillis(10);

    private synchronized SSLSocketFactory getSSLSocketFactory(boolean insecure) {
        if (insecure) {
            return SSLCertificateSocketFactory.getInsecure(socketTimeout, null);
        } else {
            SSLCertificateSocketFactory sSecureFactory = (SSLCertificateSocketFactory) SSLCertificateSocketFactory.getDefault(socketTimeout, null);


            try {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                Certificate ca1;
                Certificate ca2;

                try (InputStream in = context.getResources().openRawResource(R.raw.canary_cert)) {
                    ca1 = cf.generateCertificate(in);
                }

                try (InputStream in = context.getResources().openRawResource(R.raw.canary_cert_2)) {
                    ca2 = cf.generateCertificate(in);

                }
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca1", ca1);
                keyStore.setCertificateEntry("ca2", ca2);

                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);
                sSecureFactory.setTrustManagers(tmf.getTrustManagers());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return sSecureFactory;
        }
    }

    public void muteVolume() {
        muteVolume = true;
    }

    public void unMuteVolume() {
        muteVolume = false;

    }

    //Declare the interface. The method messageMediaReceived(String message) will must be implemented in the MyActivity
//class at on asynckTask doInBackground
    public interface OnMessageReceived {
        void messageMediaReceived(BufferMessage message);
    }

}