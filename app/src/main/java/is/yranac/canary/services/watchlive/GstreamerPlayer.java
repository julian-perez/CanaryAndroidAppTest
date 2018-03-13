package is.yranac.canary.services.watchlive;

import android.content.Context;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.os.AsyncTask;
import android.view.Surface;

import java.util.Locale;

import WL.Wl2;
import is.yranac.canary.messages.ZoomOut;
import is.yranac.canary.messages.watchlive.ByteMessage;
import is.yranac.canary.messages.watchlive.PostVideoLoad;
import is.yranac.canary.messages.watchlive.VideoLoaded;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_LIVE_LOADING;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_LIVE_PLAYING;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_WATCH_LIVE_2;


/**
 * Created by michaelschroeder on 5/23/17.
 */

public class GstreamerPlayer implements TCPClient.OnMessageReceived {

    private static final String LOG_TAG = "GstreamerPlayer";

    static {
        System.loadLibrary("tutorial-5");
        nativeClassInit();
    }

    private final Context context;

    private Surface surface;
    private TCPClient tcpClient;
    private boolean isPlaying = false;
    private boolean talking = false;
    private Device device;
    private boolean released = false;
    private boolean talkInitiated = false;
    private boolean hasNotStarted = true;


    public GstreamerPlayer(Context context) {
        this.context = context;
        nativeInit();
    }

    private native void nativeInit();     // Initialize native code, build pipeline, etc

    private native void nativeFinalize(); // Destroy pipeline and shutdown native code

    private native void nativePlay();     // Set pipeline to PLAYING

    private static native boolean nativeClassInit(); // Initialize native class: cache Method IDs for callbacks

    private native void nativeSurfaceInit(Object surface); // A new surface is available

    private native void nativeSurfaceFinalize(); // Surface about to be destroyed

    private native void playerRXMedia(int type, byte[] bytes); // Surface about to be destroyed

    private native void playerStartTalk();

    private native void playerEndTalk();

    private native void nativePause();

    private long native_custom_data;      // Native code will use this to keep private data

    public void setSurface(Surface surface) {
        if (released)
            return;

        if (this.surface != null) {
            nativeSurfaceFinalize();
            this.surface = null;
        }
        this.surface = surface;

        nativeSurfaceInit(surface);
    }

    private void onGStreamerInitialized() {
        if (released) {
            release();
        }
    }


    public void playerTXMedia(final int type, final byte[] bytes) {
        if (tcpClient != null) {
            tcpClient.sendMessage(bytes, Wl2.Channel.values()[type]);
        }

    }

    public void playbackStateChanged(boolean isPlaying) {
        this.isPlaying = isPlaying;
        if (isPlaying) {
            if (hasNotStarted) {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_WATCH_LIVE_2, ACTION_LIVE_PLAYING, null, device.uuid, device.getLocationId(), 0);
                TinyMessageBus.postDelayed(new ZoomOut(), 1500);
                TinyMessageBus.postDelayed(new PostVideoLoad(), 500);
                TinyMessageBus.postDelayed(new VideoLoaded(), 500);
                hasNotStarted = false;
            }
        }
    }

    // Called from native code when the size of the media changes or is first detected.
    // Inform the video surface about the new size and recalculate the layout.
    private void onMediaSizeChanged(int width, int height) {
    }

    // Gstreamer prefix for video decoders
    private static final String AMC_VID_DEC = "amcviddec-";
    private static final int MEDIA_WIDTH = 1280;
    private static final int MEDIA_HEIGHT = 720;

    private static String vidDec;

    public String viddecElementName() {
        if (vidDec != null)
            return vidDec;
        String vidDec = getMediaFormat();
        if (vidDec == null)
            return null;
        String formatted = vidDec.replaceAll("[^A-Za-z0-9 ]", "").toLowerCase(Locale.ENGLISH);
        vidDec = AMC_VID_DEC + formatted;
        return vidDec;
    }

    private String getMediaFormat() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            MediaCodecList list = new MediaCodecList(MediaCodecList.ALL_CODECS);
            return list.findDecoderForFormat(MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, MEDIA_WIDTH, MEDIA_HEIGHT));
        }

        int numCodecs = MediaCodecList.getCodecCount();

        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);

            if (codecInfo.isEncoder()) {
                continue;
            }

            String[] types = codecInfo.getSupportedTypes();
            for (String type : types) {
                if (type.equalsIgnoreCase(MediaFormat.MIMETYPE_VIDEO_AVC)) {
                    return codecInfo.getName();
                }
            }
        }
        return null;


    }

    public void startTalk() {
        if (talking)
            return;

        talking = true;

        if (tcpClient != null) {
            tcpClient.muteVolume();
        }
        playerStartTalk();
    }

    public boolean isTalking() {
        return talking;
    }

    public void endTalking() {
        if (!talking)
            return;

        if (tcpClient != null) {
            tcpClient.unMuteVolume();
        }
        playerEndTalk();
        talking = false;
    }

    public void stopPlayback() {
        if (tcpClient != null) {
            tcpClient.stopClient();
        }

        nativePause();
    }

    public void release() {
        stopPlayback();
        released = true;

        if (surface != null) {
            nativeSurfaceFinalize();
            surface = null;
        }
        endTalking();
        nativeFinalize();

    }

    @Override
    public void messageMediaReceived(BufferMessage message) {
        if (released)
            return;


        if (!tcpClient.isRuning())
            return;
        switch (message.getChannel()) {

            case AUDIO:
                playerRXMedia(0, message.packet);
                break;
            case VIDEO:

                if (!isPlaying) {
                    nativePlay();
                }

                playerRXMedia(1, message.packet);

                break;
        }
    }


    public void sendMessage(byte[] bytes, Wl2.Channel cmd) {
        TinyMessageBus.post(new ByteMessage(bytes, cmd, false));
    }

    public void playDevice(Device currentDevice) {

        isPlaying = false;
        hasNotStarted = true;
        this.device = currentDevice;

        nativePause();
        GoogleAnalyticsHelper.trackEvent(CATEGORY_WATCH_LIVE_2, ACTION_LIVE_LOADING, null, device.uuid, device.getLocationId(), 0);

        ConnectTask  task = new ConnectTask();

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, device);
    }

    public void setTalkInitiated(boolean initiated) {

        talkInitiated = initiated;
    }

    public boolean isTalkInitiated() {
        return talkInitiated;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean hasNotStarted() {
        return hasNotStarted;
    }

    public void cancel() {
        if (tcpClient != null) {
            tcpClient.cancel();
        }
    }

    private class ConnectTask extends AsyncTask<Device, Void, Void> {

        @Override
        protected Void doInBackground(Device... params) {

            if (params.length == 0)
                return null;

            Device currentDevice = params[0];
            tcpClient.run(currentDevice);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tcpClient = new TCPClient(GstreamerPlayer.this, context);
            TinyMessageBus.register(tcpClient);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            TinyMessageBus.unregister(tcpClient);

        }
    }


}
