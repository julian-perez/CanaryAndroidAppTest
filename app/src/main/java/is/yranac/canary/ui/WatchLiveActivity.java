package is.yranac.canary.ui;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;

import org.freedesktop.gstreamer.GStreamer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import WL.Wl2;
import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.adapter.VideoPlaybackFragmentAdapter;
import is.yranac.canary.contentproviders.CanaryDeviceContentProvider;
import is.yranac.canary.databinding.ActivityWatchLiveBinding;
import is.yranac.canary.fragments.VideoPlaybackFragment;
import is.yranac.canary.media.CanaryVideoPlayer;
import is.yranac.canary.messages.LocationTableUpdated;
import is.yranac.canary.messages.PushReceived;
import is.yranac.canary.messages.WatchLiveTimeUp;
import is.yranac.canary.messages.ZoomOut;
import is.yranac.canary.messages.watchlive.DeviceOnOffline;
import is.yranac.canary.messages.watchlive.PostVideoLoad;
import is.yranac.canary.messages.watchlive.StillWatching;
import is.yranac.canary.messages.watchlive.TCPDisconnect;
import is.yranac.canary.messages.watchlive.TCPRetry;
import is.yranac.canary.messages.watchlive.VideoLoaded;
import is.yranac.canary.model.avatar.Avatar;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.database.AvatarDatabaseService;
import is.yranac.canary.services.database.CustomerDatabaseService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.database.SubscriptionPlanDatabaseService;
import is.yranac.canary.services.watchlive.GstreamerPlayer;
import is.yranac.canary.ui.views.ZoomTextureView;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.AnimationHelper;
import is.yranac.canary.util.DateUtil;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.GlobalSirenTimeout;
import is.yranac.canary.util.ImageUtils;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.PermissionUtil;
import is.yranac.canary.util.PushUtils;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.TouchTimeUtil;
import is.yranac.canary.util.UrlUtils;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.WatchLiveSirenUtil;
import is.yranac.canary.util.ZendeskUtil;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static WL.Wl2.Channel.CMD;
import static is.yranac.canary.media.CanaryVideoPlayer.STATE_READY;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_LIMIT;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_LIMIT_CONTINUE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_LIMIT_PAGE_CHANGE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_LIVE_FAILURE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_SERVER_CLOSE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_TALK_BEGIN;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_TALK_CANCELLED;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_TALK_END;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_TALK_LOADING;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ZOOM;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_LIVE;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_WATCH_LIVE_2;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_LIVE;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_NO_VIDEO_LOADED;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_VIDEO_LOADED;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_WATCH_LIVE;

public class WatchLiveActivity extends BaseActivity implements TextureView.SurfaceTextureListener,
        ZoomTextureView.TextureViewCallbacks, ViewPager.OnPageChangeListener, View.OnClickListener, ZoomTextureView.ZoomTextureGestures, CanaryVideoPlayer.Listener {
    private static final String LOG_TAG = "WatchLiveActivity";
    private static final int WATCH_LIVE_MINS_TIMEOUT = 30;
    private static final int REQUEST_CODE = 52453;


    static {
        System.loadLibrary("gstreamer_android");
    }

    private String currentDeviceUUID;

    private VideoPlaybackFragmentAdapter adapter;

    private int playingIndex;

    private WatchLiveSirenUtil sirenUtil;

    private Date latencyDate;

    private boolean viewPagerMoving;

    private List<Device> devices = new ArrayList<>();

    private boolean firstPlayBack = true;

    private CanaryVideoPlayer player;
    private GstreamerPlayer gPlayer;

    private ActivityWatchLiveBinding binding;
    private String location;

    private Device currentDevice;
    private Location watchLiveLocation;

    private Subscription subscription;

    private int black;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWatchLiveBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        currentDeviceUUID = getIntent().getStringExtra(CanaryDeviceContentProvider.COLUMN_UUID);

        location = getIntent().getStringExtra(
                CanaryDeviceContentProvider.COLUMN_LOCATION_ID);
        watchLiveLocation = LocationDatabaseService.getLocationFromResourceUri(location);

        initView();

        final GestureDetector gestureDetector = new GestureDetector(this, new GestureListener());

        binding.videoViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        if (GlobalSirenTimeout.getInstance().isSirenCountingDown()) {
            sirenUtil = new WatchLiveSirenUtil(this, binding.bottomBtnsLayout);
            sirenUtil.showSirenView();
        } else {
            sirenUtil = new WatchLiveSirenUtil(this, binding.bottomBtnsLayout, Utils.getIntFromResourceUri(location));
        }
        try {
            GStreamer.init(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initDevicePager() {

        playingIndex = 0;
        devices = DeviceDatabaseService.getActivatedDevicesAtLocation(Utils.getIntFromResourceUri(location));
        subscription = SubscriptionPlanDatabaseService.getServicePlanForLocation(Utils.getIntFromResourceUri(location));

        if (devices.size() == 0) {
            finish();
            return;
        }


        if (currentDeviceUUID == null) {
            currentDevice = devices.get(playingIndex);
        } else {
            for (Device device : devices) {
                if (device.uuid.equalsIgnoreCase(currentDeviceUUID)) {
                    playingIndex = devices.indexOf(device);
                    currentDevice = device;
                    break;
                }
            }
        }


        adapter = new VideoPlaybackFragmentAdapter(getSupportFragmentManager(), devices);

        binding.videoViewPager.setOffscreenPageLimit(1);
        binding.videoViewPager.setAdapter(adapter);
        binding.videoViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.videoViewPager.setCurrentItem(playingIndex);

        binding.deviceName.setText(devices.get(playingIndex).name);

        if (devices.size() == 1) {
            binding.watchLiveIndicator.setVisibility(View.GONE);
        }
        binding.videoViewPager.addOnPageChangeListener(this);
        binding.watchLiveIndicator.setViewPager(binding.videoViewPager);
        binding.textureViewContainer.setVisibility(View.INVISIBLE);
        zoomToMax();

    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            onSingleTap(e);
            return true;
        }

    }

    private void setZoomLevels() {


        Matrix matrix = new Matrix();
        float translateY = 0;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int deviceWidth = displayMetrics.widthPixels;
            int deviceHeight = displayMetrics.heightPixels;

            float newHeight = (int) ((float) deviceWidth * (9.0f / 16.0f));
            translateY = (deviceHeight / 2.0f) - (newHeight / 2.0f);

            if (player != null) {
                float[] m = new float[9];
                matrix.getValues(m);
                float scaleHeight = newHeight / deviceHeight;
                binding.textureView.setAllZoomWithBounds(true);
                m[Matrix.MSCALE_X] = 1.0f;
                m[Matrix.MSCALE_Y] = scaleHeight;
                m[Matrix.MTRANS_X] = 0.0f;
                m[Matrix.MTRANS_Y] = translateY;
                matrix.setValues(m);

            } else {
                binding.textureView.setAllZoomWithBounds(false);
            }

            binding.textureView.setAllowVerticalPaning(false);

        } else {
            binding.textureView.clearAnimation();

            binding.watermark.setY(DensityUtil.dip2px(this, 30));

            binding.textureView.setAllZoomWithBounds(false);

            binding.textureView.setAllowVerticalPaning(true);
        }
        binding.watermark.setY((int) translateY + DensityUtil.dip2px(this, 30));

        binding.textureView.setOriginalMatrix(new Matrix(matrix));
        binding.textureView.setMatrix(new Matrix(matrix));

    }

    private void zoomToMax() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int deviceWidth = displayMetrics.widthPixels;
            int deviceHeight = displayMetrics.heightPixels;
            binding.textureView.setMaxZoom((float) deviceWidth, (float) deviceHeight);
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        scheduleTimeout();
    }

    private void initView() {

        String canaryTalkMembersip = getString(R.string.use_two_way_talk_read_time);
        String activateNow = getString(R.string.activate_membership);
        SpannableStringBuilder membershipCopy = StringUtils.spannableStringBuilder(this, canaryTalkMembersip, activateNow, "Gibson-Light.otf", "Gibson.otf", 15, 15);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                String url = Constants.autoLoginUrlWithPromoCodes(watchLiveLocation, WatchLiveActivity.this, false);
                String title = getString(R.string.membership);
                Intent intent = WebActivity.newInstance(WatchLiveActivity.this, url, title, true);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ds.linkColor);    // you can use custom color
                ds.setUnderlineText(false);    // this remove the underline
            }
        };

        membershipCopy.setSpan(clickableSpan, canaryTalkMembersip.length(), membershipCopy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.talkMembershipCopy.setMovementMethod(LinkMovementMethod.getInstance());
        binding.talkMembershipCopy.setHighlightColor(Color.TRANSPARENT);
        binding.talkMembershipCopy.setText(membershipCopy);


        binding.textureView.setSurfaceTextureListener(this);

        binding.textureView.setZoomTextureGestures(this);

        binding.textureView.setTextureViewCallbacks(this);
        black = ContextCompat.getColor(this, R.color.very_dark_gray_mostly_black_twenty);
        binding.talkBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        if (subscription != null && !subscription.hasMembership) {
                            setUpTalkLayout();
                            return true;
                        }

                        if (!currentDevice.isOnline) {
                            AlertUtils.showGenericAlert(WatchLiveActivity.this,
                                    getString(R.string.device_offline),
                                    getString(R.string.device_offline_talk), 0,
                                    getString(R.string.get_help), getString(R.string.okay), 0, 0, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String url = getString(R.string.connectivity_url);
                                            ZendeskUtil.loadHelpCenter(WatchLiveActivity.this, url);
                                        }
                                    }, null);
                            return true;

                        }

                        if (currentDevice.isPrivate()) {
                            AlertUtils.showGenericAlert(WatchLiveActivity.this,
                                    getString(R.string.device_private),
                                    getString(R.string.device_private_talk), 0, getString(R.string.okay),
                                    null, 0, 0, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }, null);
                            return true;
                        }

                        if (gPlayer == null) {
                            return true;
                        }

                        gPlayer.setTalkInitiated(true);

                        GoogleAnalyticsHelper.trackEvent(CATEGORY_WATCH_LIVE_2, ACTION_TALK_LOADING, null, currentDevice.uuid, currentDevice.getLocationId(), 0);

                        if (TouchTimeUtil.dontAllowTouch())
                            return true;
                        // PRESSED

                        if (ContextCompat.checkSelfPermission(WatchLiveActivity.this,
                                Manifest.permission.RECORD_AUDIO)
                                != PackageManager.PERMISSION_GRANTED) {

                            AlertUtils.showGenericAlert(WatchLiveActivity.this, getString(R.string.enable_microphone_access),
                                    getString(R.string.canary_talk_uses_microphone), 0,
                                    getString(R.string.not_now), getString(R.string.allow), 0, 0,
                                    null, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ActivityCompat.requestPermissions(WatchLiveActivity.this,
                                                    new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE);
                                        }
                                    });
                            return false;
                        }

                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(50);

                        Log.i(LOG_TAG, "request speaking lock");
                        Wl2.SpeakingLockRequest.Builder request = Wl2.SpeakingLockRequest.newBuilder()
                                .setState(Wl2.SpeakingLockRequest.State.ACQUIRE);

                        Wl2.CommandMessage commandMessage = Wl2.CommandMessage.newBuilder()
                                .setCommandType(Wl2.CommandType.SPEAKING_LOCK_REQUEST)
                                .setSpeakingLockRequest(request).build();

                        gPlayer.sendMessage(commandMessage.toByteArray(), CMD);

                        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1, 2);
                        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1, 2);
                        ObjectAnimator scaleAnimation = ObjectAnimator.ofPropertyValuesHolder(binding.talkBackground, pvhX, pvhY);
                        scaleAnimation.setDuration(500);
                        scaleAnimation.setInterpolator(new OvershootInterpolator());
                        AnimatorSet setAnimation = new AnimatorSet();
                        setAnimation.play(scaleAnimation);
                        setAnimation.start();

                        binding.arcAnimation.startSpin();
                        binding.talkLabel.setAlpha(0.0f);
                        return true; // if you want to handle the touch event
                    }
                    case MotionEvent.ACTION_UP: {


                        if (gPlayer == null) {
                            return true;
                        }


                        if (!gPlayer.isTalkInitiated()) {
                            return true;
                        }
                        Wl2.SpeakingLockRequest.Builder request = Wl2.SpeakingLockRequest.newBuilder()
                                .setState(Wl2.SpeakingLockRequest.State.RELEASE);
                        Wl2.CommandMessage commandMessage = Wl2.CommandMessage.newBuilder().
                                setCommandType(Wl2.CommandType.SPEAKING_LOCK_REQUEST).
                                setSpeakingLockRequest(request).build();
                        gPlayer.sendMessage(commandMessage.toByteArray(), CMD);

                        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, 2, 1);
                        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 2, 1);
                        ObjectAnimator scaleAnimation = ObjectAnimator.ofPropertyValuesHolder(binding.talkBackground, pvhX, pvhY);
                        scaleAnimation.setDuration(500);
                        AnimatorSet setAnimation = new AnimatorSet();
                        setAnimation.play(scaleAnimation);
                        setAnimation.start();

                        binding.arcAnimation.cancelSpin();
                        binding.talkLabel.setAlpha(1.0f);
                        endTalk();

                        return true; // if you want to handle the touch event
                    }
                }
                return false;
            }
        });

        binding.emergencyCallBtn.setOnClickListener(this);
        binding.sirenBtn.setOnClickListener(this);
    }

    private void setUpTalkLayout() {

        AnimationHelper.fadeViewInAndOutAfterDelay(
                binding.membershipTopOverlay, 500, 5000);

        binding.membershipTopOverlay.setY(Utils.getRelativeTop(binding.talkBtn) - binding.membershipTopOverlay.getHeight());
        binding.membershipTopOverlay.setX(Utils.getRelativeLeft(binding.talkBtn) +
                (binding.talkBtn.getWidth() / 2) -
                (binding.membershipTopOverlay.getWidth() / 2));
    }


    @Override
    public boolean hasSirenFooter() {
        return true;
    }

    private void animateShowDeviceName(boolean show) {

        Animation fadeInAnimation;
        if (show) {
            binding.deviceName.setVisibility(View.VISIBLE);
            fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        } else {
            fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
            binding.deviceName.setVisibility(View.INVISIBLE);
        }
        binding.deviceName.startAnimation(fadeInAnimation);
    }


    @Subscribe
    public void onTCPDisconnect(TCPDisconnect tcpDisconnect) {


        binding.textureViewContainer.setVisibility(View.GONE);
        if (currentDevice != null && gPlayer != null) {

            if (gPlayer.isPlaying()) {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_WATCH_LIVE_2, ACTION_SERVER_CLOSE, PROPERTY_VIDEO_LOADED, currentDevice.uuid, currentDevice.getLocationId(), 0);
            } else {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_WATCH_LIVE_2, ACTION_SERVER_CLOSE, PROPERTY_NO_VIDEO_LOADED, currentDevice.uuid, currentDevice.getLocationId(), 0);
            }

            if (gPlayer.hasNotStarted()) {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_WATCH_LIVE_2, ACTION_LIVE_FAILURE, null, currentDevice.uuid, currentDevice.getLocationId(), 0);
            }

        }
        startPlaying();
    }


    @Subscribe
    public void onTCPRetry(TCPRetry tcpRetry) {
        binding.textureViewContainer.setVisibility(View.GONE);
        if (currentDevice != null) {
            GoogleAnalyticsHelper.trackEvent(CATEGORY_WATCH_LIVE_2, ACTION_LIVE_FAILURE, null, currentDevice.uuid, currentDevice.getLocationId(), 0);
        }

        if (gPlayer != null) {
            gPlayer.cancel();
        }
        startPlaying();


    }

    private void startPlaying() {

        if (currentDevice == null) {
            return;
        }
        loadVideo();

        stopPlayback();

        if (deviceNotPlayingVideo(currentDevice)) {
            onPostVideoLoad(null);
            if (currentDevice.hasNewWatchLive()) {
                binding.talkLayout.setVisibility(View.VISIBLE);
            } else {
                binding.talkLayout.setVisibility(View.GONE);
            }
            return;
        }

        if (currentDevice.hasNewWatchLive()) {

            TinyMessageBus.post(new StillWatching());

            gPlayer = new GstreamerPlayer(this);
            SurfaceTexture surfaceTexture = binding.textureView.getSurfaceTexture();

            if (surfaceTexture != null) {
                Surface surface = new Surface(surfaceTexture);
                gPlayer.setSurface(surface);
            }


            gPlayer.playDevice(currentDevice);

            binding.talkLayout.setVisibility(View.VISIBLE);

        } else {
            binding.talkLayout.setVisibility(View.GONE);

            String streamingUrl = UrlUtils.buildStreamingUrl(currentDevice);

            player = new CanaryVideoPlayer(this, CanaryVideoPlayer.VideoType.VideoTypeHLSLive);
            player.addListener(this);

            player.setDataSource(streamingUrl);
            SurfaceTexture surfaceTexture = binding.textureView.getSurfaceTexture();

            if (surfaceTexture != null) {
                Surface surface = new Surface(surfaceTexture);
                player.setSurface(surface);
            }

            player.prepare();
            player.setPlayWhenReady(true);
        }
        setZoomLevels();
    }

    @Subscribe
    public void timesUp(WatchLiveTimeUp timeUp) {
        GoogleAnalyticsHelper.trackEvent(CATEGORY_LIVE, ACTION_LIMIT, null, currentDeviceUUID, Utils.getIntFromResourceUri(location), 0);
        stopPlayback();
        AlertUtils.showWatchLiveLimitReachedDialog(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnimationHelper.fadeViewOut(binding.textureViewContainer, 200);
                startPlaying();
                GoogleAnalyticsHelper.trackEvent(CATEGORY_LIVE, ACTION_LIMIT_CONTINUE, null, currentDeviceUUID, Utils.getIntFromResourceUri(location), 0);
                scheduleTimeout();
            }
        });
    }

    private void scheduleTimeout() {
        TinyMessageBus.cancel(WatchLiveTimeUp.class);
        TinyMessageBus.postDelayed(new WatchLiveTimeUp(), TimeUnit.MINUTES.toMillis(WATCH_LIVE_MINS_TIMEOUT));
    }

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {

        try {
            if (latencyDate != null && playbackState == STATE_READY) {
                binding.textureViewContainer.setVisibility(View.VISIBLE);
                binding.textureView.invalidate();
                AnimationHelper.fadeFromAlphaToAlpha(binding.textureViewContainer, 0.0f, 1.0f, 200);
                if (firstPlayBack) {
                    binding.watermark.setVisibility(View.VISIBLE);
                    TinyMessageBus.postDelayed(new ZoomOut(), 1000);
                    TinyMessageBus.post(new PostVideoLoad());
                    firstPlayBack = false;
                }


                long latency = DateUtil.getCurrentTime().getTime() - latencyDate.getTime();
                GoogleAnalyticsHelper.trackWatchLiveLatency(latency);
                latencyDate = null;
            }


        } catch (Exception ignore) {

        }
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_WATCH_LIVE;
    }


    @Override
    protected void onDestroy() {
        stopPlayback();

        if (sirenUtil != null) {
            sirenUtil.removeSirenListener();
        }

        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        TinyMessageBus.register(binding.textureView);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        latencyDate = DateUtil.getCurrentTime();

        binding.bottomBtnsLayout.setVisibility(View.INVISIBLE);
        initDevicePager();
    }


    @Override
    public void onResume() {
        super.onResume();
        scheduleTimeout();
        TinyMessageBus.register(binding.textureView);

        firstPlayBack = true;
        startPlaying();

        binding.membershipTopOverlay.setAlpha(0.0f);


        if (GlobalSirenTimeout.getInstance().isSirenCountingDown()) {
            sirenUtil.showSirenView();
        } else {
            sirenUtil.dismissSiren();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPlayback();
        TinyMessageBus.unregister(binding.textureView);

        for (Device device : devices) {
            if (!device.hasNewWatchLive()) {
                DeviceAPIService.changeStreamingState(device.resourceUri, false);
            }
        }
    }


    @Override
    public void onStop() {
        stopPlayback();
        TinyMessageBus.unregister(binding.textureView);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onStop();
    }


    @Subscribe
    public void onPostVideoLoad(PostVideoLoad showBottonButtons) {

        if (binding.bottomBtnsLayout.getVisibility() != View.VISIBLE) {
            sirenUtil.slideViewInFromBottom(200, 1500);
        }
        zoomStopped();

    }

    private void stopPlayback() {

        VideoPlaybackFragment fragment = adapter.getFragment(binding.videoViewPager.getCurrentItem());


        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }

        if (gPlayer != null) {
            gPlayer.release();
            gPlayer = null;
        }

        if (fragment != null) {
            fragment.showVideoLoadingOverlay(true);
        }


    }


    private long timeCheck = 0;

    @Subscribe
    public void onStillWatching(StillWatching stillWatching) {
        if (timeCheck > DateUtil.getCurrentTime().getTime() - 10000) {
            return;
        }

        timeCheck = DateUtil.getCurrentTime().getTime();

        for (Device device : devices) {
            if (device.hasNewWatchLive())
                continue;

            boolean sendWatchLiveCommand = device.sendWatchLiveCommand();
            if (sendWatchLiveCommand ||
                    device.uuid.equalsIgnoreCase(currentDeviceUUID)) {
                DeviceAPIService.changeStreamingState(device.resourceUri, true);
            }
        }

        TinyMessageBus.postDelayed(new StillWatching(), 10000);
    }

    private void loadVideo() {
        onStillWatching(null);
    }

    @Subscribe
    public void onLocationTableUpdated(LocationTableUpdated message) {
        checkIfPrivacyMode();
    }

    private AlertDialog alertDialog = null;

    private void checkIfPrivacyMode() {

        Location location = UserUtils.getLastViewedLocation();

        if (location != null) {
            if (location.isPrivate) {
                if (alertDialog == null) {
                    alertDialog = AlertUtils.showGenericAlert(this, getString(R.string.privacy_watch_live_disabled),
                            null, 0, getString(R.string.continue_text), null, 0, 0,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (alertDialog != null)
                                        alertDialog.dismiss();
                                    finish();
                                }
                            }, null);
                }
            }
        }
    }


    @Subscribe
    public void onPushReceived(PushReceived pushReceived) {
        PushUtils.showPush(this, pushReceived);

    }

    private boolean deviceNotPlayingVideo(Device device) {

        if (device == null)
            return true;

        if (device.uuid.equalsIgnoreCase(currentDeviceUUID)) {

            VideoPlaybackFragment fragment = adapter.getFragment(binding.videoViewPager.getCurrentItem());

            if (device.isPrivate()) {
                stopPlayback();
                if (fragment != null) {
                    fragment.showOfflineView(R.string.set_to_private_period);
                }
                return true;
            } else if (!device.isOnline) {
                stopPlayback();
                if (fragment != null) {
                    fragment.showOfflineView(R.string.canary_is_offline);
                }
                return true;
            } else {
                if (fragment != null) {
                    fragment.showVideoLoadingOverlay(true);
                }
                return false;
            }
        }

        return false;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setZoomLevels();
        binding.membershipTopOverlay.setVisibility(View.INVISIBLE);
        binding.membershipTopOverlay.clearAnimation();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.topFrame.setVisibility(View.VISIBLE);
            binding.bottomBtnsLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            binding.sirenLabel.setVisibility(View.VISIBLE);
            binding.emergencyLabel.setVisibility(View.VISIBLE);
            binding.talkLabel.setVisibility(View.VISIBLE);
            if (((player != null && player.isPlaying()) || (gPlayer != null && gPlayer.isPlaying()))
                    || deviceNotPlayingVideo(currentDevice)) {
                AnimationHelper.fadeViewIn(binding.bottomBtnsLayout, 0);
                AnimationHelper.fadeViewIn(binding.topFrame, 0);
            }

        } else {

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

            binding.bottomBtnsLayout.setGravity(Gravity.START);
            binding.sirenLabel.setVisibility(View.GONE);
            binding.emergencyLabel.setVisibility(View.GONE);
            binding.talkLabel.setVisibility(View.GONE);
            AnimationHelper.fadeViewOut(binding.bottomBtnsLayout, 0);
            AnimationHelper.fadeViewOut(binding.topFrame, 0);


        }


        binding.bottomBtnsLayout.requestLayout();
    }

    @Subscribe
    public void onDeviceOnOffline(DeviceOnOffline onOffline) {
        for (Device device : devices) {
            if (device.serialNumber.equalsIgnoreCase(onOffline.device) &&
                    device.isOnline != onOffline.online) {
                device.isOnline = onOffline.online;
                deviceNotPlayingVideo(device);
                break;
            }
        }
    }


    //Surface listener
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (player != null) {
            player.setSurface(new Surface(surface));
        } else if (gPlayer != null) {
            gPlayer.setSurface(new Surface(surface));
        }

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

    @Override
    public void setScrollingEnabled(boolean enabled) {

    }

    @Override
    public void pastGesture(MotionEvent event) {
        try {
            binding.videoViewPager.onTouchEvent(event);
        } catch (IllegalStateException | IllegalArgumentException ignore) {
        }
    }

    @Override
    public boolean pagerIsMoving() {
        return viewPagerMoving;
    }


    @Override
    public void zoomLevelCallback(float zoom) {
        GoogleAnalyticsHelper.trackEvent(CATEGORY_LIVE, ACTION_ZOOM, null,
                currentDeviceUUID, Utils.getIntFromResourceUri(location), 0);

    }

    @Override
    public void zoomStopped() {
        float translateY;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (player != null) {
                translateY = binding.textureView.getMatrixTop();
            } else {
                int mediaHeight = binding.textureView.media_height;
                int mediaWidth = binding.textureView.media_width;
                int height = binding.textureView.getWidth() * mediaHeight / mediaWidth;
                float scaleY = binding.textureView.getMatrixScaleY();
                float videoHeight = height * scaleY;
                int viewHeight = binding.textureView.getHeight();
                translateY = (viewHeight - videoHeight) / 2;
            }
        } else {
            translateY = 0;
        }

        View watermark = binding.watermark;
        watermark.setY((int) translateY + DensityUtil.dip2px(WatchLiveActivity.this, 30));

        if (((player != null && player.isPlaying()) || (gPlayer != null && gPlayer.isPlaying())) || !firstPlayBack) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                AnimationHelper.fadeFromAlphaToAlpha(watermark, watermark.getAlpha(), 1.0f, 200);
            }
        }
    }

    @Override
    public void zoomStarted() {
        View watermark = binding.watermark;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            AnimationHelper.fadeFromAlphaToAlpha(watermark, watermark.getAlpha(), 0.0f, 200);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (playingIndex == position) {
            binding.textureViewContainer.setX(-positionOffsetPixels);
        } else {
            int width;
            Display display = getWindowManager().getDefaultDisplay();
            Point point = new Point();

            display.getSize(point);
            width = point.x;

            binding.textureViewContainer.setX(width - positionOffsetPixels);
        }
        viewPagerMoving = positionOffset != 0;
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        int currentPage = binding.videoViewPager.getCurrentItem();
        if (state == ViewPager.SCROLL_STATE_IDLE && playingIndex != currentPage) {
            if (player != null) {
                player.stop();
                player.seekTo(0);
            }
            binding.textureView.reset();
            binding.textureViewContainer.setVisibility(View.INVISIBLE);
            binding.textureView.invalidate();
            binding.textureViewContainer.invalidate();
            playingIndex = currentPage;
            binding.textureViewContainer.setX(0);

            Device newDevice = devices.get(playingIndex);
            if (currentDeviceUUID == null ||
                    !currentDeviceUUID.equalsIgnoreCase(newDevice.uuid)) {
                timeCheck = 0;
            }

            currentDeviceUUID = newDevice.uuid;
            currentDevice = newDevice;
            binding.deviceName.setText(currentDevice.name);

            latencyDate = DateUtil.getCurrentTime();
            startPlaying();
            GoogleAnalyticsHelper.trackEvent(CATEGORY_LIVE, ACTION_LIMIT_PAGE_CHANGE, null,
                    currentDeviceUUID, Utils.getIntFromResourceUri(location), 0);
        }

        if (state == ViewPager.SCROLL_STATE_IDLE)
            animateShowDeviceName(true);
        else if (viewPagerMoving && devices.size() > 1)
            animateShowDeviceName(false);

        viewPagerMoving = state != ViewPager.SCROLL_STATE_IDLE;
    }


    // Called from native code. Native code calls this once it has created its pipeline and
    // the main loop is running, so it is ready to accept commands.

    @Subscribe
    public void videoLoaded(VideoLoaded loaded) {

        if (binding.textureViewContainer.getVisibility() != View.VISIBLE) {
            AnimationHelper.fadeFromAlphaToAlpha(binding.textureViewContainer, 0.0f, 1.0f, 200);
        }
    }


    @Subscribe
    public void onCommandMessage(Wl2.CommandMessage commandMessage) {
        switch (commandMessage.getCommandType()) {
            case STATUS_BROADCAST:
                Wl2.StatusBroadcast statusBroadcast = commandMessage.getStatusBroadcast();
                if (statusBroadcast.hasCurrentSpeakingUserId()) {
                    int currentUser = statusBroadcast.getCurrentSpeakingUserId();
                    if (CurrentCustomer.getCurrentCustomer().id == currentUser
                            && gPlayer.isTalkInitiated()) {

                        gPlayer.startTalk();
                        GoogleAnalyticsHelper.trackEvent(CATEGORY_WATCH_LIVE_2, ACTION_TALK_BEGIN, null, currentDevice.uuid, currentDevice.getLocationId(), 0);

                        int black = ContextCompat.getColor(this, R.color.very_dark_gray_mostly_black_twenty);
                        int green = ContextCompat.getColor(this, R.color.dark_moderate_cyan);
                        AnimationHelper.animationBackgroundColor(binding.talkBackground, black, green, 1000);
                        binding.arcAnimation.stopSpin();

                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(50);
                        AnimationHelper.fadeViewIn(binding.pulsator, 100);
                        binding.pulsator.start();

                    } else {
                        endTalk();

                        Customer customer = CustomerDatabaseService.getCustomerFromId(currentUser);

                        if (customer != null) {
                            binding.someoneElseSpeakingTextView.setText(
                                    getString(R.string.person_is_speaking, customer.firstName));
                            Avatar avatar = AvatarDatabaseService.getAvatarFromCustomerId(currentUser);
                            binding.avatarLayout.customerInitials.setText(customer.getInitials());
                            binding.customerTalkingLayout.setVisibility(View.VISIBLE);

                            if (avatar != null) {
                                ImageUtils.loadAvatar(binding.avatarLayout.avatarImageLayout, avatar.thumbnailUrl());
                                binding.avatarLayout.avatarImageLayout.setVisibility(View.VISIBLE);
                            } else {
                                binding.avatarLayout.avatarImageLayout.setVisibility(View.GONE);
                                binding.avatarLayout.grayCircle.setVisibility(View.VISIBLE);
                            }

                            binding.talkBtn.setEnabled(false);
                            binding.talkLabel.setAlpha(0.0f);

                        } else {
                            binding.someoneElseSpeakingTextView.setText(R.string.someone_else_is_speaking);

                            binding.talkBtn.setEnabled(false);
                            binding.talkBtn.setAlpha(0.5f);
                            binding.talkLabel.setAlpha(0.5f);
                        }

                        binding.someoneElseSpeakingTextView.measure(0, 0);
                        binding.someoneElseSpeakingTextView.setX(Utils.getRelativeLeft(binding.talkBackground) +
                                (binding.talkBackground.getWidth() / 2) -
                                (binding.someoneElseSpeakingTextView.getWidth() / 2));
                        binding.someoneElseSpeakingTextView.setY(Utils.getRelativeTop(binding.talkBackground) - binding.someoneElseSpeakingTextView.getHeight() - DensityUtil.dip2px(this, 23));

                        AnimationHelper.fadeFromAlphaToAlpha(binding.someoneElseSpeakingTextView,
                                0.0f,
                                1.0f, 500);

                    }
                } else {
                    binding.talkBtn.setEnabled(true);
                    binding.talkBtn.setAlpha(1.0f);
                    binding.talkLabel.setAlpha(1.0f);
                    endTalk();
                    AnimationHelper.animationBackgroundColor(binding.talkBackground,
                            binding.talkBackground.getBackgroundColor(),
                            black, 1000);

                    AnimationHelper.fadeFromAlphaToAlpha(binding.someoneElseSpeakingTextView,
                            binding.someoneElseSpeakingTextView.getAlpha(),
                            0.0f, 500);
                }
                break;
        }
    }

    private void endTalk() {

        binding.talkBtn.setEnabled(true);
        binding.pulsator.clearAnimation();
        binding.pulsator.setVisibility(View.GONE);
        binding.pulsator.stop();
        binding.talkBtn.setAlpha(1.0f);
        binding.customerTalkingLayout.setVisibility(View.INVISIBLE);

        if (gPlayer != null && gPlayer.isTalkInitiated()) {
            gPlayer.setTalkInitiated(false);

            if (gPlayer.isTalking()) {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_WATCH_LIVE_2, ACTION_TALK_END, null, currentDevice.uuid, currentDevice.getLocationId(), 0);
            } else {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_WATCH_LIVE_2, ACTION_TALK_CANCELLED, null, currentDevice.uuid, currentDevice.getLocationId(), 0);
            }

            gPlayer.endTalking();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.siren_btn:
                if (!hasInternetConnection()) {
                    return;
                }
                if (!sirenUtil.dismissSirenFromScreen(SCREEN_WATCH_LIVE)) {
                    Device device = devices.get(playingIndex);
                    sirenUtil.showDeviceCountdown(SCREEN_WATCH_LIVE, device);

                }
                break;

            case R.id.emergency_call_btn:
                AlertUtils.showPhoneNumberAlertDialog(WatchLiveActivity.this, PROPERTY_LIVE);
                break;
        }
    }

    @Override
    public void onSingleTap(MotionEvent event) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (binding.topFrame.getVisibility() == View.VISIBLE) {
                AnimationHelper.fadeViewOut(binding.topFrame, 500);
                AnimationHelper.fadeViewOut(binding.bottomBtnsLayout, 500);
                if (binding.membershipTopOverlay.getAlpha() == 1.0f) {
                    AnimationHelper.fadeViewOut(binding.membershipTopOverlay, 500);

                }
            } else {
                AnimationHelper.fadeViewIn(binding.topFrame, 500);
                AnimationHelper.fadeViewIn(binding.bottomBtnsLayout, 500);
            }
        }
    }

    @Override
    public void onDoubleTap(MotionEvent event) {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                PermissionUtil.hasPermission(permissions, grantResults, this);
            }
            break;
        }
    }
}
