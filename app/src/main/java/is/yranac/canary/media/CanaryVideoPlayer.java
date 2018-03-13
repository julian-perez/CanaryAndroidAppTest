/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package is.yranac.canary.media;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.Surface;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayer.EventListener;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer.VideoListener;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.retrofit.DefaultRetrofitClient;
import is.yranac.canary.util.Utils;

import static com.google.android.exoplayer2.upstream.cache.CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR;

public class CanaryVideoPlayer implements EventListener {

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    private static final String LOG_TAG = "CanaryVideoPlayer";
    private final EventLogger eventLogger;
    private final VideoType videoType;
    private final Context context;
    private Handler mainHandler;
    private List<Listener> listeners = new ArrayList<>();

    @Override
    public void onTimelineChanged(Timeline timeline, Object o) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {

    }

    @Override
    public void onLoadingChanged(boolean b) {

    }

    @Override
    public void onPlayerStateChanged(boolean b, int i) {

        for (Listener l : listeners) {
            l.onStateChanged(b, i);
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    public int getPlaybackState() {
        return player.getPlaybackState();
    }

    public long getCurrentPosition() {
        return player.getCurrentPosition();
    }

    public int getBufferedPercentage() {
        return player.getBufferedPercentage();
    }

    public boolean getPlayWhenReady() {
        return player.getPlayWhenReady();
    }

    public void release() {
        player.release();
        if (surface != null) {
            surface.release();
            surface = null;
        }
    }

    public void setVideoDebugListener(VideoRendererEventListener listener) {
        player.setVideoDebugListener(listener);
    }

    public void setVideoListener(VideoListener listener) {
        player.setVideoListener(listener);
    }

    public long getDuration() {
        return player.getDuration();
    }



    public enum VideoType {
        VideoTypeMP4,
        VideoTypeHLSLive,
        VideoTypeHLSVod
    }

    public CanaryVideoPlayer(Context context, VideoType videoType) {
        if (Utils.isDemo()) {
            videoType = VideoType.VideoTypeMP4;
        }
        this.videoType = videoType;
        mainHandler = new Handler();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(BANDWIDTH_METER);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

// 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

// 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
        player.addListener(this);
        eventLogger = new EventLogger(trackSelector);
        player.addListener(eventLogger);
        this.context = context;


    }

    public DataSource.Factory buildDataSourceFactory(Context context, DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(context, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(DefaultRetrofitClient.getUserAgent(), bandwidthMeter);
    }

    public boolean isPlaying() {
        return player.getPlaybackState() == ExoPlayer.STATE_READY;
    }


    public boolean isBuffering() {
        return player.getPlaybackState() == ExoPlayer.STATE_BUFFERING;
    }


    // Constants pulled into this class for convenience.
    public static final int STATE_IDLE = ExoPlayer.STATE_IDLE;
    public static final int STATE_BUFFERING = ExoPlayer.STATE_BUFFERING;
    public static final int STATE_READY = ExoPlayer.STATE_READY;
    public static final int STATE_ENDED = ExoPlayer.STATE_ENDED;

    private static final int RENDERER_BUILDING_STATE_BUILDING = 2;
    private static final int RENDERER_BUILDING_STATE_BUILT = 3;

    private final SimpleExoPlayer player;

    private int rendererBuildingState;

    private Surface surface;
    private String dataSource = "";


    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void setSurface(Surface surface) {
        if (this.surface != null) {
            this.surface.release();
        }
        this.surface = surface;
        pushSurface();
    }

    private void pushSurface() {
        player.setVideoSurface(this.surface);
    }


    public void stop() {
        player.stop();
    }

    public void prepare() {
        if (rendererBuildingState == RENDERER_BUILDING_STATE_BUILT) {
            player.stop();
        }
        rendererBuildingState = RENDERER_BUILDING_STATE_BUILDING;
    }


    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
        MediaSource mediaSource;


        switch (videoType) {
            case VideoTypeHLSLive:
                mediaSource = new HlsMediaSource(Uri.parse(dataSource),
                        buildDataSourceFactory(context, BANDWIDTH_METER),
                        mainHandler, eventLogger);

                break;
            case VideoTypeMP4:
                mediaSource = new ExtractorMediaSource(Uri.parse(dataSource),
                        buildDataSourceFactory(context, BANDWIDTH_METER),
                        new DefaultExtractorsFactory(), mainHandler, eventLogger);
                break;
            case VideoTypeHLSVod:
                mediaSource = new HlsMediaSource(Uri.parse(dataSource),
                        buildCacheDataSourceFactory(context, BANDWIDTH_METER),
                        mainHandler, eventLogger);
                break;
            default:
                return;
        }

        player.prepare(mediaSource, false, false);

    }

    private static final int MB = 1024 * 1024;
    private static final int NUMBER_OF_MB = 100;

    private DataSource.Factory buildCacheDataSourceFactory(Context context, DefaultBandwidthMeter bandwidthMeter) {

        HttpDataSource.Factory factory = buildHttpDataSourceFactory(bandwidthMeter);
        Cache cache = new SimpleCache(context.getCacheDir(), new LeastRecentlyUsedCacheEvictor(MB * NUMBER_OF_MB));
        return new CacheDataSourceFactory(cache, factory, FLAG_IGNORE_CACHE_ON_ERROR);

    }


    public String getDataSource() {
        return dataSource;
    }

    public void setPlayWhenReady(boolean ready) {
        player.setPlayWhenReady(ready);
    }


    public void seekTo(long seek) {
        player.seekTo(seek);
    }

    public interface Listener {
        void onStateChanged(boolean playWhenReady, int playbackState);
    }
}
