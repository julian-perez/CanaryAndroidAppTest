package is.yranac.canary.media;

import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.view.Surface;
import android.view.TextureView;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

/**
 * Created by Schroeder on 6/29/16.
 */
public class PlayerVideoInitializer implements TextureView.SurfaceTextureListener {

    private CanaryVideoPlayer player;
    private String startVideo;

    public PlayerVideoInitializer(@NonNull CanaryVideoPlayer player, @NonNull String startVideo, @NonNull final TextureView textureView) {

        this.player = player;
        this.startVideo = startVideo;
        this.player.setVideoDebugListener(new VideoRendererEventListener() {
            @Override
            public void onVideoEnabled(DecoderCounters counters) {

            }

            @Override
            public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

            }

            @Override
            public void onVideoInputFormatChanged(Format format) {

            }

            @Override
            public void onDroppedFrames(int count, long elapsedMs) {

            }

            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) textureView.getLayoutParams();
                layout.height = (int) (textureView.getWidth() * ((float) height / (float) width));
                layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
                textureView.setLayoutParams(layout);
            }

            @Override
            public void onRenderedFirstFrame(Surface surface) {

            }

            @Override
            public void onVideoDisabled(DecoderCounters counters) {

            }
        });

        SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();

        if (surfaceTexture != null) {
            Surface surface = new Surface(surfaceTexture);
            player.setSurface(surface);
        }
        player.setDataSource(startVideo);

        player.prepare();
        player.setPlayWhenReady(true);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        player.setSurface(new Surface(surface));
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
