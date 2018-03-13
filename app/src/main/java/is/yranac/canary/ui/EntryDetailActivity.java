package is.yranac.canary.ui;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.adapter.VideoPlaybackFragmentAdapter;
import is.yranac.canary.contentproviders.CanaryDeviceContentProvider;
import is.yranac.canary.databinding.ActivityEntryDetailBinding;
import is.yranac.canary.databinding.FragmentTimelineOverlayBinding;
import is.yranac.canary.fragments.EntryDetailTagFragment;
import is.yranac.canary.fragments.VideoPlaybackFragment;
import is.yranac.canary.media.CanaryVideoPlayer;
import is.yranac.canary.messages.EntryLabelStringUpdated;
import is.yranac.canary.messages.PushReceived;
import is.yranac.canary.messages.ShareCompletedProcessing;
import is.yranac.canary.messages.ShareStartedProcessing;
import is.yranac.canary.messages.TutorialRequest;
import is.yranac.canary.messages.tutorial.StartEntryOptionsTutorial;
import is.yranac.canary.model.avatar.Avatar;
import is.yranac.canary.model.clip.Clip;
import is.yranac.canary.model.clip.ClipsResponse;
import is.yranac.canary.model.comment.Comment;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.entry.EntryDeleted;
import is.yranac.canary.model.entry.Notified;
import is.yranac.canary.model.mode.ModeCache;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.model.videoexport.VideoExport;
import is.yranac.canary.services.api.ClipAPIService;
import is.yranac.canary.services.api.CommentAPIServices;
import is.yranac.canary.services.api.EntryAPIService;
import is.yranac.canary.services.api.SubscriptionAPIService;
import is.yranac.canary.services.database.AvatarDatabaseService;
import is.yranac.canary.services.database.CommentDatabaseService;
import is.yranac.canary.services.database.CustomerDatabaseService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.EntryDatabaseService;
import is.yranac.canary.services.database.LabelDatabaseService;
import is.yranac.canary.services.database.ModeDatabaseService;
import is.yranac.canary.services.database.NotifiedDatabaseService;
import is.yranac.canary.services.database.ThumbnailDatabaseService;
import is.yranac.canary.services.database.VideoExportDatabaseService;
import is.yranac.canary.ui.views.ZoomTextureView;
import is.yranac.canary.ui.widget.SeekbarWithIntervals;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.AnimationHelper;
import is.yranac.canary.util.DateUtil;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.EntryUtil;
import is.yranac.canary.util.ExportUtil;
import is.yranac.canary.util.GlobalSirenTimeout;
import is.yranac.canary.util.ImageUtils;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.MyLifecycleHandler;
import is.yranac.canary.util.PermissionUtil;
import is.yranac.canary.util.PushUtils;
import is.yranac.canary.util.ShareUtil;
import is.yranac.canary.util.SirenUtil;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.TouchTimeUtil;
import is.yranac.canary.util.TutorialUtil;
import is.yranac.canary.util.UrlUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ZendeskUtil;
import is.yranac.canary.util.cache.location.GetLocation;
import is.yranac.canary.util.cache.location.GotLocationData;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

import static is.yranac.canary.media.CanaryVideoPlayer.VideoType.VideoTypeHLSVod;
import static is.yranac.canary.model.mode.ModeCache.privacy;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.extra_locationId;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.modal;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.single_entry_upsell;
import static is.yranac.canary.util.EntryUtil.getDownloadProgress;
import static is.yranac.canary.util.TutorialUtil.TutorialType.ENTRY_MORE_OPTIONS;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ADD_MEMBERSHIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_APP_ENTRY_NOTIFICATION;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ENTRY_COMMENT;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ENTRY_DELETE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ENTRY_MENU_CLOSE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ENTRY_MENU_OPEN;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ENTRY_PAUSE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ENTRY_PLAY;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ENTRY_REPLAY;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ENTRY_REWIND;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_SAVE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_UNSAVE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_WATCH_LIVE_ENTRY_COMPLETE;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_APP_ENTRY;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_ENTRY;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MEMBERSHIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_APP_ENTRY_BACKGROUNDED;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_APP_ENTRY_TERMINATED;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_ENTRY;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_SINGLE_ENTRY;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_SINGLE_ENTRY;

public class EntryDetailActivity extends BaseActivity implements TextureView.SurfaceTextureListener,
        SeekbarWithIntervals.EntrySeekBarCallBack, CanaryVideoPlayer.Listener, OnClickListener {
    private static final String LOG_TAG = "EntryDetailActivity";

    public static final String ADD_COMMENT = "add_comment";
    private static final int MY_PERMISSIONS_REQUEST_FILE_SYSTEM = 112;
    public static final String ENTRY_ID = "entryId";
    public static final String PLAYING_THUMBNAIL_ID = "thumbnail_id";
    private static final int SUBSCRIPTION_UPDATE = 8390;

    private VideoPlaybackFragmentAdapter adapter;

    private static int SEEK_BAR_UPDATE_INTERVAL = 50;

    private GestureDetector gestureDetector;

    private int customerId;

    private CanaryVideoPlayer canaryVideoPlayer;

    private boolean showThumbnailsInViewpager = true;
    private int nowPlayingIndex = 0;
    private boolean autoPlay = true;

    private long entryId;
    private Entry entry;

    private List<Comment> comments;
    private List<PageVideoData> pageVideoDataMap = new ArrayList<>();

    private SirenUtil sirenUtil;
    private AlertDialog openAlert;
    private boolean viewPagerMoving = false;
    private float lastZoom;

    private ActivityEntryDetailBinding binding;
    private int deviceWidth;

    private FragmentTimelineOverlayBinding tutorialBinding;

    private Subscription subscription;

    @Override
    protected void onNewIntent(Intent intent) {

        if (entryId == intent.getLongExtra("entryId", 0)) {
            entry = EntryDatabaseService.getEntryFromEntryId(entryId);
            updateShareIcon();
            if (entry == null)
                finish();
        } else {
            setIntent(intent);
            recreate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getIntent().getBooleanExtra("from_notification", false)) {
            GoogleAnalyticsHelper.trackEvent(CATEGORY_APP_ENTRY, ACTION_APP_ENTRY_NOTIFICATION, MyLifecycleHandler.appTerminated() ? PROPERTY_APP_ENTRY_BACKGROUNDED : PROPERTY_APP_ENTRY_TERMINATED);

        }

        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_entry_detail, null, false);
        setContentView(binding.getRoot());
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            deviceWidth = displayMetrics.heightPixels;
        } else {
            deviceWidth = displayMetrics.widthPixels;
        }

        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onConfigurationChanged(getResources().getConfiguration());
                binding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        entryId = getIntent().getLongExtra(ENTRY_ID, 0);
        entry = EntryDatabaseService.getEntryFromEntryId(entryId);
        if (entry == null) {
            finish();
            return;
        }


        if (GlobalSirenTimeout.getInstance().isSirenCountingDown()) {
            sirenUtil = new SirenUtil(
                    EntryDetailActivity.this, binding.bottomBtnsLayout.getRoot(), GlobalSirenTimeout.getInstance().getLocationId());
            sirenUtil.showSirenView();
        } else {
            sirenUtil = new SirenUtil(EntryDetailActivity.this, binding.bottomBtnsLayout.getRoot(), entry.getLocationId());
        }

        customerId = CurrentCustomer.getCurrentCustomer().id;

        gestureDetector = new GestureDetector(this, new GestureListener());

        // *********************************************************
        // *** Create a map of Clip and Thumbnail data for each
        // *** Page.  When done this method will also initialize
        // *** the VideoViewPager and start playing the first clip
        // *********************************************************
        getThumbnailAndClipData();

        // *********************************************************
        // *** Set the fixed date header for the view
        // *********************************************************
        Date startTimeAsDate = entry.startTime;
        String formattedDateString = DateUtil.dateToDay(this, startTimeAsDate);
        binding.headerTitleTextView.setText(formattedDateString);

        showFlaggedStatus();

        binding.entryDurationTextView.setText(getStartDateString());

        binding.detailEntrySummary.setText(entry.description);
        updateEntrySummary();

        setupEntryActions();
        setupEmergencyOptions();


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        binding.moreOptionsBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.isDemo()) {
                    AlertUtils.showGenericAlert(EntryDetailActivity.this, getString(R.string.even_more_options), getString(R.string.even_more_options_dsc));
                    return;
                }

                if (tutorialBinding != null) {
                    tutorialBinding = null;
                    AnimationHelper.fadeViewOut(binding.tutorialView, 200);
                    AnimationHelper.viewGoneAfterDelay(binding.tutorialView, 200);
                }

                if (TutorialUtil.isTutorialInProgress()) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    TutorialUtil.finishTutorial(ENTRY_MORE_OPTIONS);
                }

                if (binding.moreButtonLayout.getVisibility() == View.VISIBLE) {
                    GoogleAnalyticsHelper.trackEntry(ACTION_ENTRY_MENU_CLOSE, PROPERTY_ENTRY, null, entry.getLocationId(), entry.id);
                    hideMoreOptionsLayout();
                } else {
                    GoogleAnalyticsHelper.trackEntry(ACTION_ENTRY_MENU_OPEN, PROPERTY_ENTRY, null, entry.getLocationId(), entry.id);

                    binding.moreButtonLayout.setVisibility(View.VISIBLE);

                    AnimationHelper.slideViewInFromTop(binding.moreButtonLayout, 200, 1);
                    AnimationHelper.viewVisibleAfterDelay(binding.moreOptionsBackground, 100);
                    AnimationHelper.fadeViewIn(binding.blackOverlayView, 200);


                    binding.blackOverlayView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            hideMoreOptionsLayout();
                            return true;
                        }
                    });

                    setButtonStates();

                }


            }
        });

        binding.deleteEntryBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasInternetConnection())
                    return;
                openAlert = AlertUtils.showDeleteEntryAlert(EntryDetailActivity.this, new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!hasInternetConnection())
                            return;

                        final ProgressDialog dialog = new ProgressDialog(EntryDetailActivity.this);
                        dialog.setMessage(getString(R.string.deleting));
                        dialog.show();
                        EntryAPIService.deleteEntry(entry.resourceUri, new Callback<Void>() {
                            @Override
                            public void success(Void aVoid, Response response) {
                                dialog.dismiss();
                                EntryDatabaseService.deleteEntry(entry.id);
                                GoogleAnalyticsHelper.trackEvent(CATEGORY_ENTRY, ACTION_ENTRY_DELETE,
                                        PROPERTY_ENTRY);
                                finish();

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                if (isFinishing())
                                    return;

                                dialog.dismiss();
                                try {
                                    if (!isFinishing() && !isPaused())
                                        openAlert = AlertUtils.showGenericAlert(EntryDetailActivity.this, Utils.getErrorMessageFromRetrofit(EntryDetailActivity.this, error));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                }, new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });

        binding.watchLiveBtn.setOnClickListener(this);
        binding.replayBtn.setOnClickListener(this);

        binding.exportVideoOption.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (subscription != null && subscription.haveFullVideo()) {
                    ExportUtil.beforeNeededExportAction(EntryDetailActivity.this, entry);
                } else {
                    showSingleEntryUpSell();
                }
            }
        });
        // *********************************************************
        // *** Setup the comments
        // *********************************************************
        comments = CommentDatabaseService.getCommentsForEntry(entry.id);

        Notified notified = NotifiedDatabaseService.getNofificationStatus(entry.id);

        if (notified != null) {
            addNotificationComment();
        }
        for (Comment comment : comments) {
            addCommentView(comment);
        }


        boolean openMenu = getIntent().getBooleanExtra("open_menu", false);

        if (openMenu)
            openMenu();


        if (getIntent().hasExtra(ADD_COMMENT)) {
            if (binding.postEditText.requestFocus()) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }

        if (Utils.isBeta()) {

            binding.reportIssue.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ZendeskUtil.showZendesk(EntryDetailActivity.this, entryId);
                }
            });
        } else {
            binding.reportIssueLayout.setVisibility(View.GONE);
        }

        if (getSupportFragmentManager().findFragmentByTag("EntryDetailTagFragment") != null) {
            getSupportFragmentManager().popBackStack();
        }

        String canaryTalkMembersip = getString(R.string.access_full_videos);
        String activateNow = getString(R.string.activate_membership);
        SpannableStringBuilder membershipCopy = StringUtils.spannableStringBuilder(this, canaryTalkMembersip,
                activateNow, getString(R.string.gibson_light), getString(R.string.gibson_regular), 15, 15);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                String path = Constants.autoLoginUrl(entry.getLocationId(), EntryDetailActivity.this);
                String title = getString(R.string.activate_membership);
                Intent intent = WebActivity.newInstance(EntryDetailActivity.this, path, title, true);
                startActivityForResult(intent, SUBSCRIPTION_UPDATE);

                if (pageVideoDataMap == null || pageVideoDataMap.isEmpty())
                    return;

                Device device = pageVideoDataMap.get(nowPlayingIndex).deviceObject;

                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_ADD_MEMBERSHIP, PROPERTY_SINGLE_ENTRY, device.uuid, device.getLocationId(), 0);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ds.linkColor);    // you can use custom color
                ds.setUnderlineText(false);    // this remove the underline
            }
        };

        membershipCopy.setSpan(clickableSpan, canaryTalkMembersip.length(), membershipCopy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.membershipCtaTextView.setMovementMethod(LinkMovementMethod.getInstance());
        binding.membershipCtaTextView.setHighlightColor(Color.TRANSPARENT);
        binding.membershipCtaTextView.setText(membershipCopy);

    }

    @Override
    public void onStart() {
        super.onStart();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        TutorialUtil.register();
    }

    @Override
    public void onStop() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        TutorialUtil.unregister();
        super.onStop();
    }

    @Subscribe
    public void startTutorial(StartEntryOptionsTutorial startTutorial) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        tutorialBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.fragment_timeline_overlay, binding.tutorialView, true);
        tutorialBinding.setTutorialType(ENTRY_MORE_OPTIONS);
        binding.tutorialView.setVisibility(View.VISIBLE);
        AnimationHelper.fadeViewInAfterDelay(binding.tutorialView, 500, 750);
        AnimationHelper.fadeViewIn(tutorialBinding.timelineTutorialArrow3, 400);
        int dp5 = DensityUtil.dip2px(this, 5);
        AnimationHelper.startPulsing(tutorialBinding.timelineTutorialArrow3, true, dp5, 400);
        nowPlayingIndex = 0;
        showThumbnailsInViewpager = true;

    }

    @Subscribe
    public void gotLocationData(GotLocationData gotLocationData) {

        subscription = gotLocationData.subscription;
        binding.setSubscription(subscription);

        onConfigurationChanged(getResources().getConfiguration());
        if (subscription.haveFullVideo()) {
            getClips();
        }
    }

    private void getClips() {
        ClipAPIService.getClips(
                entryId, new Callback<ClipsResponse>() {
                    @Override
                    public void success(ClipsResponse clipsResponse, Response response) {
                        List<Clip> clips = clipsResponse.clips;

                        for (PageVideoData pageVideoData : pageVideoDataMap) {
                            List<Clip> deviceClips = new ArrayList<>();
                            for (Clip clip : clips) {

                                if (clip.device.equals(pageVideoData.deviceObject.resourceUri)) {
                                    deviceClips.add(clip);
                                }

                            }

                            pageVideoData.clips = deviceClips;
                        }


                        setUpSeekBar();

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
    }

    private void addNotificationComment() {

        View commentView = getLayoutInflater().inflate(R.layout.entry_comment_view_left, null, false);

        ((TextView) commentView.findViewById(R.id.comment)).setText(getString(R.string.notification_sent));
        ((TextView) commentView.findViewById(R.id.comment_timestamp)).setText(DateUtil.utcDateToDisplayString(entry.startTime));

        FrameLayout avatarLayout = (FrameLayout) commentView.findViewById(R.id.avatar_layout);
        ImageView imageView = (ImageView) avatarLayout.findViewById(R.id.avatar_image_layout);
        imageView.setImageResource(R.drawable.event_avatar_activity);
        binding.commentContainer.addView(commentView);

    }


    @Override
    public void showDialog(AlertDialog dialog) {
        super.showDialog(dialog);
    }

    @Override
    public void hideMoreOptionsLayout() {
        AnimationHelper.slideViewOutToTop(binding.moreButtonLayout, 200, 1.0f);
        AnimationHelper.viewGoneAfterDelay(binding.moreOptionsBackground, 200);
        AnimationHelper.fadeFromAlphaToAlpha(binding.blackOverlayView, 1.0f, 0.0f, 200);
        binding.blackOverlayView.setOnTouchListener(null);

    }

    @Override
    public void handlePermissionIssue() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_FILE_SYSTEM);

    }

    private void setupEmergencyOptions() {

        binding.bottomBtnsLayout.soundSirenBtn.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!hasInternetConnection()) {
                            return;
                        }

                        if (!sirenUtil.dismissSirenFromScreen(SCREEN_SINGLE_ENTRY)) {

                            if (pageVideoDataMap == null || pageVideoDataMap.isEmpty())
                                return;

                            final Device device = pageVideoDataMap.get(nowPlayingIndex).deviceObject;

                            sirenUtil.showDeviceCountdown(SCREEN_SINGLE_ENTRY, device);
                        }

                    }
                });

        binding.bottomBtnsLayout.emergencyCallBtn.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TouchTimeUtil.dontAllowTouch())
                            return;

                        if (Utils.isDemo()) {
                            if (sirenUtil != null) {
                                sirenUtil.dismissSiren();
                            }

                        }

                        openAlert = AlertUtils.showPhoneNumberAlertDialog(EntryDetailActivity.this, PROPERTY_ENTRY);
                    }
                });

    }

    private void initKeyVideoViews() {
        binding.videoLayout.videoViewPager.setOnTouchListener(videoViewTouchListener);
        binding.videoLayout.textureView.setSurfaceTextureListener(this);
        binding.videoLayout.textureView.setAllZoomWithBounds(false);
        binding.videoLayout.textureView.setAllowVerticalPaning(true);
        binding.videoLayout.textureView.setZoomTextureGestures(new ZoomTextureView.ZoomTextureGestures() {
            @Override
            public void onSingleTap(MotionEvent event) {

                if (!showThumbnailsInViewpager) {
                    if (binding.videoScrubControls.getVisibility() != View.VISIBLE) {
                        showScrubControls();
                    } else {
                        showViewPagerIndicator();
                    }
                } else {
                    if (hasInternetConnection() && !pageVideoDataMap.isEmpty()) {

                        PageVideoData data = pageVideoDataMap.get(nowPlayingIndex);
                        if (data.videoUrl == null) {
                            openAlert = AlertUtils.showGenericAlert(EntryDetailActivity.this, getString(R.string.error_playing_video));
                            return;
                        }
                        showViewPagerIndicator();

                        showThumbnailsInViewpager = false;
                        VideoPlaybackFragment videoPlaybackFragment =
                                adapter.getFragment(nowPlayingIndex);

                        if (videoPlaybackFragment != null) {
                            videoPlaybackFragment.showEventDeviceThumbnail(false);
                            videoPlaybackFragment.showVideoLoadingOverlay(true);
                            autoPlay = true;
                        }
                        GoogleAnalyticsHelper.trackEntry(ACTION_ENTRY_PLAY, PROPERTY_ENTRY,
                                data.deviceObject.uuid, entry.getLocationId(), entry.id);

                        prepareVideoPlayback();
                        binding.videoLayout.videoViewPager.setOnTouchListener(null);
                    }
                }
            }

            @Override
            public void onDoubleTap(MotionEvent event) {

            }
        });


        binding.videoLayout.textureView.setTextureViewCallbacks(new ZoomTextureView.TextureViewCallbacks() {
            @Override
            public void setScrollingEnabled(boolean enabled) {
                binding.entryScrollView.setScrollingEnabled(enabled);
            }

            @Override
            public void pastGesture(MotionEvent event) {
                try {
                    binding.videoLayout.videoViewPager.onTouchEvent(event);
                } catch (IllegalArgumentException ignore) {
                }
            }

            @Override
            public boolean pagerIsMoving() {
                return viewPagerMoving;
            }

            @Override
            public void zoomLevelCallback(float zoom) {

                NumberFormat formatter = NumberFormat.getNumberInstance();
                formatter.setMaximumFractionDigits(0);
                formatter.setRoundingMode(RoundingMode.HALF_UP);
                binding.zoomTextView.setText(formatter.format(zoom) + "x");


                if (zoom < 1.5 && lastZoom != zoom) {
                    lastZoom = zoom;
                    AnimationHelper.fadeViewOut(binding.zoomTextView, 250);
                } else if (zoom >= 1.5 && lastZoom != zoom) {
                    lastZoom = zoom;
                    AnimationHelper.fadeViewIn(binding.zoomTextView, 250);
                    AnimationHelper.fadeViewOut(binding.circlePageIndicator, 250);
                }

            }

            @Override
            public void zoomStopped() {
            }

            @Override
            public void zoomStarted() {
            }

        });

        binding.videoScrubControls.setVisibility(View.GONE);

        binding.videoSeekBar.setEntrySeekBarCallBack(this);


        binding.rewindBtnPortrait.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PageVideoData data = pageVideoDataMap.get(nowPlayingIndex);
                        GoogleAnalyticsHelper.trackEntry(ACTION_ENTRY_REWIND, PROPERTY_ENTRY, data.deviceObject.uuid, entry.getLocationId(), entry.id);
                        changeVideoPosition(Math.max(canaryVideoPlayer.getCurrentPosition() - 10000, 0));
                    }
                });

        binding.rewindBtnLandscape.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PageVideoData data = pageVideoDataMap.get(nowPlayingIndex);
                        GoogleAnalyticsHelper.trackEntry(ACTION_ENTRY_REWIND, PROPERTY_ENTRY, data.deviceObject.uuid, entry.getLocationId(), entry.id);
                        changeVideoPosition(Math.max(canaryVideoPlayer.getCurrentPosition() - 10000, 0));
                    }
                });

        binding.playPauseBtn.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PageVideoData data = pageVideoDataMap.get(nowPlayingIndex);

                        if (!hasInternetConnection()) {
                            return;
                        }
                        if (canaryVideoPlayer.getPlayWhenReady()) {
                            GoogleAnalyticsHelper.trackEntry(ACTION_ENTRY_PLAY, PROPERTY_ENTRY, data.deviceObject.uuid, entry.getLocationId(), entry.id);
                            binding.playPauseBtn.setImageResource(R.drawable.ic_play);
                            canaryVideoPlayer.setPlayWhenReady(false);
                        } else {
                            GoogleAnalyticsHelper.trackEntry(ACTION_ENTRY_PAUSE, PROPERTY_ENTRY, data.deviceObject.uuid, entry.getLocationId(), entry.id);
                            binding.playPauseBtn.setImageResource(R.drawable.ic_pause);
                            canaryVideoPlayer.setPlayWhenReady(true);
                            seekBarHandler.post(seekBarUpdater);

                        }
                    }
                });
    }

    public Entry getEntry() {
        return entry;
    }

    public void openMenu() {
        if (binding.moreButtonLayout.getVisibility() != View.VISIBLE) {
            binding.moreOptionsBtn.performClick();
        } else {
            setButtonStates();
        }

    }

    private void setButtonStates() {

        EntryUtil.DOWNLOADSTATUS downloadstatus = EntryUtil.getDownloadProgress(entry);
        List<VideoExport> videoExports = VideoExportDatabaseService.getVideoExportsByEntry(entry.id);

        String text;
        int imageRsc;

        binding.exportVideoOptionIcon.clearAnimation();
        switch (downloadstatus) {
            case IN_PROGRESS:
                if (videoExports.size() == 1) {
                    text = getString(R.string.processing);
                } else {
                    text = getString(R.string.processings, videoExports.size());
                }
                imageRsc = R.drawable.icon_processing;
                Animation rotation = AnimationUtils.loadAnimation(EntryDetailActivity.this, R.anim.clockwise_rotation);
                binding.exportVideoOptionIcon.startAnimation(rotation);
                break;
            case DOWNLOAD_READY:
                if (videoExports.size() == 1) {
                    text = getString(R.string.download_video);
                } else {
                    text = getString(R.string.download_videos, videoExports.size());
                }
                imageRsc = R.drawable.row_downoload_icon;
                break;
            case NOT_STARTED:
            default:
                imageRsc = R.drawable.row_downoload_icon;

                if (videoExports.size() == 1) {
                    text = getString(R.string.request_video_download);
                } else {
                    text = getString(R.string.request_video_downloads);
                }

                break;

        }

        binding.exportVideoOptionIcon.setImageResource(imageRsc);
        binding.exportVideoOption.setText(text);
    }

    @Override
    public void onClick(View v) {

        Device device;

        switch (v.getId()) {
            case R.id.watch_live_btn:
                startWatchLive();

                if (pageVideoDataMap == null || pageVideoDataMap.isEmpty())
                    return;

                device = pageVideoDataMap.get(nowPlayingIndex).deviceObject;
                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_WATCH_LIVE_ENTRY_COMPLETE, null, device.uuid, device.getLocationId(), 0);

                break;

            case R.id.replay_btn:
                changeVideoPosition(0);
                binding.playPauseBtn.setImageResource(R.drawable.ic_pause);
                canaryVideoPlayer.setPlayWhenReady(true);
                seekBarHandler.post(seekBarUpdater);
                binding.freeVideoOver.setVisibility(View.GONE);


                if (pageVideoDataMap == null || pageVideoDataMap.isEmpty())
                    return;

                device = pageVideoDataMap.get(nowPlayingIndex).deviceObject;

                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_ENTRY_REPLAY, null, device.uuid, device.getLocationId(), 0);
                break;
        }
    }


    private static class PageVideoData extends Thumbnail {
        public Device deviceObject;
        public List<Clip> clips;
        public String videoUrl = null;
    }

    private void getThumbnailAndClipData() {
        initKeyVideoViews();
        final List<Thumbnail> thumbnails = ThumbnailDatabaseService.getThumbnailsForEntry(entryId);

        setUpDeviceThumbnails(thumbnails);

    }

    private void setUpDeviceThumbnails(List<Thumbnail> thumbnails) {

        pageVideoDataMap.clear();

        long startingThumbnailId = getIntent().getLongExtra(PLAYING_THUMBNAIL_ID, -1);

        List<Thumbnail> sortedThumbnails = new ArrayList<>();
        for (Thumbnail thumbnail : thumbnails) {
            PageVideoData pageVideoData = new PageVideoData();
            pageVideoData.imageUrl = thumbnail.imageUrl;
            Device device = DeviceDatabaseService.getDeviceFromResourceUri(thumbnail.device);


            String deviceUUID = null;
            if (entry.displayMeta != null && entry.displayMeta.notified != null) {
                deviceUUID = entry.displayMeta.notified.deviceUUID;
            }
            if (device != null) {
                pageVideoData.deviceObject = device;
                pageVideoData.videoUrl = UrlUtils.buildEntryDeviceClipUrl(entry, String.valueOf(pageVideoData.deviceObject.id), thumbnail);

                pageVideoData.clips = new ArrayList<>();


                if (deviceUUID != null && device.uuid.equalsIgnoreCase(deviceUUID)) {
                    sortedThumbnails.add(0, thumbnail);
                    pageVideoDataMap.add(0, pageVideoData);
                } else {
                    pageVideoDataMap.add(pageVideoData);
                    sortedThumbnails.add(thumbnail);
                }

                if (thumbnail.id == startingThumbnailId) {
                    nowPlayingIndex = sortedThumbnails.indexOf(thumbnail);
                    showThumbnailsInViewpager = false;

                }
            }
        }

        initDevicePager(sortedThumbnails);

        binding.videoLayout.videoViewPager.setCurrentItem(nowPlayingIndex);

        showViewPagerIndicator();

        if (!pageVideoDataMap.isEmpty() && autoPlay)
            prepareVideoPlayback();
    }

    private void initDevicePager(List<Thumbnail> thumbnails) {
        adapter = new VideoPlaybackFragmentAdapter(getSupportFragmentManager(), thumbnails, showThumbnailsInViewpager);

        autoPlay = !showThumbnailsInViewpager;

        binding.videoLayout.videoViewPager.setOffscreenPageLimit(1);
        binding.videoLayout.videoViewPager.setAdapter(adapter);
        binding.videoLayout.videoViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (nowPlayingIndex == position) {
                    binding.videoLayout.textureViewContainer.setX(-positionOffsetPixels);
                    binding.topFrame.setX(-positionOffsetPixels);
                    binding.freeVideoOver.setX(-positionOffsetPixels);
                    binding.bottomFrame.setX(-positionOffsetPixels);
                } else {
                    int width;

                    Display display = getWindowManager().getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    width = point.x;

                    binding.videoLayout.textureViewContainer.setX(width - positionOffsetPixels);
                    binding.freeVideoOver.setX(width - positionOffsetPixels);
                    binding.topFrame.setX(width - positionOffsetPixels);
                    binding.bottomFrame.setX(width - positionOffsetPixels);

                }

                viewPagerMoving = positionOffset != 0;
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    viewPagerMoving = false;
                    int currentPage = binding.videoLayout.videoViewPager.getCurrentItem();

                    binding.videoLayout.textureViewContainer.setX(0);
                    binding.topFrame.setX(0);
                    binding.bottomFrame.setX(0);
                    binding.freeVideoOver.setX(0);
                    binding.freeVideoOver.setVisibility(View.GONE);

                    if (showThumbnailsInViewpager) {
                        nowPlayingIndex = currentPage;
                        adapter.getFragment(nowPlayingIndex)
                                .showEventDeviceThumbnail(true);
                    } else if (nowPlayingIndex != currentPage) {

                        nowPlayingIndex = currentPage;
                        nowPlayingIndex = currentPage;
                        adapter.getFragment(nowPlayingIndex)
                                .showEventDeviceThumbnail(false);
                        adapter.getFragment(nowPlayingIndex)
                                .showVideoLoadingOverlay(true);

                        binding.videoLayout.textureViewContainer.setVisibility(View.GONE);
                        binding.videoLayout.textureViewContainer.invalidate();
                        binding.topFrame.setVisibility(View.GONE);
                        binding.bottomFrame.setVisibility(View.GONE);
                        binding.videoLayout.textureView.setMatrix(new Matrix());
                        lastZoom = 1.0f;
                        binding.zoomTextView.setText("1x");
                        binding.zoomTextView.setAlpha(0.0f);

                        autoPlay = true;
                        canaryVideoPlayer.stop();
                        canaryVideoPlayer.seekTo(0);
                        prepareVideoPlayback();

                    }
                }
            }
        };

        binding.videoLayout.videoViewPager.addOnPageChangeListener(onPageChangeListener);

        if (pageVideoDataMap.size() == 1) {
            binding.circlePageIndicator.setVisibility(View.GONE);
        } else {
            binding.circlePageIndicator.setViewPager(binding.videoLayout.videoViewPager);
        }
    }

    private void prepareVideoPlayback() {

        if (pageVideoDataMap.isEmpty())
            return;

        PageVideoData pData = pageVideoDataMap.get(nowPlayingIndex);
        if (pData == null) {
            return;
        }
        String videoUrl = pData.videoUrl;
        if (videoUrl == null)
            return;

        if (canaryVideoPlayer != null) {
            canaryVideoPlayer.stop();
            canaryVideoPlayer.release();
            canaryVideoPlayer = null;
        }

        canaryVideoPlayer = new CanaryVideoPlayer(this, VideoTypeHLSVod);
        canaryVideoPlayer.addListener(this);
        canaryVideoPlayer.setDataSource(videoUrl);
        SurfaceTexture surfaceTexture = binding.videoLayout.textureView.getSurfaceTexture();

        if (surfaceTexture != null) {
            Surface surface = new Surface(surfaceTexture);
            canaryVideoPlayer.setSurface(surface);
        }

        canaryVideoPlayer.setPlayWhenReady(true);
        canaryVideoPlayer.prepare();

        setUpSeekBar();

    }

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        Log.i(LOG_TAG, "new state " + playbackState);
        switch (playbackState) {
            case CanaryVideoPlayer.STATE_READY:
                if (playWhenReady) {
                    playVideo();
                } else if (autoPlay) {
                    canaryVideoPlayer.setPlayWhenReady(true);
                }
                autoPlay = false;
                binding.videoSeekBar.setDuration(canaryVideoPlayer.getDuration());
                break;
            case CanaryVideoPlayer.STATE_ENDED:
                canaryVideoPlayer.setPlayWhenReady(false);
                canaryVideoPlayer.seekTo(0);
                binding.playPauseBtn.setImageResource(R.drawable.ic_play);
                if (subscription != null && !subscription.haveFullVideo()) {
                    AnimationHelper.fadeViewIn(binding.freeVideoOver, 750);
                    showViewPagerIndicator();
                }
                break;

        }
    }

    private void playVideo() {
        seekBarHandler.removeCallbacks(seekBarUpdater);
        binding.playPauseBtn.setImageResource(R.drawable.ic_pause);
        if (!hasInternetConnection()) {
            adapter.getFragment(nowPlayingIndex)
                    .showVideoLoadingOverlay(true);
            return;
        }
        seekBarHandler.postDelayed(seekBarUpdater, SEEK_BAR_UPDATE_INTERVAL);
    }

    private void setUpSeekBar() {

        if (pageVideoDataMap.isEmpty())
            return;

        List<Clip> clips = pageVideoDataMap.get(nowPlayingIndex).clips;
        binding.videoSeekBar.setClipsAndActivityNotification(clips);

        binding.videoSeekBar.setPosition(0);
        binding.playPauseBtn.setImageResource(R.drawable.ic_play);
    }


    @Override
    public void changeVideoPosition(long seekToPoint) {
        autoPlay = true;
        canaryVideoPlayer.seekTo(seekToPoint);
    }

    @Override
    public void stopPlayBack() {
        seekBarHandler.removeCallbacks(seekBarUpdater);
        canaryVideoPlayer.setPlayWhenReady(false);
    }


    @Override
    public void setTimeStampLabels(long position) {
        Date newDate = new Date(position);
        String dateString = DateUtil.utcDateToDisplayStringSeconds(newDate);
        binding.dateTimestampPortrait.setText(dateString);
        binding.dateTimestampLandscape.setText(dateString);
    }

    private Handler seekBarHandler = new Handler();

    private Runnable seekBarUpdater = new Runnable() {
        public void run() {

            int buffer = canaryVideoPlayer.getBufferedPercentage();

            binding.videoSeekBar.setBuffer(buffer);
            if (updateSeekBar()) {
                binding.videoLayout.watermark.setVisibility(View.VISIBLE);
                onUserInteraction();
                seekBarHandler.postDelayed(seekBarUpdater, SEEK_BAR_UPDATE_INTERVAL);
                return;
            } else {
                binding.videoLayout.watermark.setVisibility(View.GONE);
            }

            if (buffer != 100) {
                seekBarHandler.postDelayed(seekBarUpdater, SEEK_BAR_UPDATE_INTERVAL);
            }

        }
    };

    private boolean updateSeekBar() {
        long actualMediaPlayerPosition = 0;
        try {

            if (!Utils.hasInternetConnection(getBaseContext())) {
                binding.playPauseBtn.setImageResource(R.drawable.ic_play);
                canaryVideoPlayer.setPlayWhenReady(false);
                return false;
            }

            if (!canaryVideoPlayer.isPlaying())
                return false;
            actualMediaPlayerPosition = canaryVideoPlayer.getCurrentPosition();

        } catch (IllegalStateException ignore) {
            Crashlytics.logException(ignore);
        }

        if (actualMediaPlayerPosition > 0) {
            VideoPlaybackFragment videoPlaybackFragment = adapter.getFragment(nowPlayingIndex);
            if (videoPlaybackFragment != null)
                videoPlaybackFragment.showVideoLoadingOverlay(false);

            binding.videoLayout.textureViewContainer.setVisibility(View.VISIBLE);

            binding.bottomFrame.setVisibility(View.VISIBLE);
            binding.topFrame.setVisibility(View.VISIBLE);


            if (getSupportFragmentManager().findFragmentByTag(EntryDetailTagFragment.class.getSimpleName()) != null) {
                binding.playPauseBtn.setImageResource(R.drawable.ic_play);
                canaryVideoPlayer.setPlayWhenReady(false);
                return false;
            }
        }

        long seekBarPosition = binding.videoSeekBar.setPosition(actualMediaPlayerPosition);

        if (canaryVideoPlayer.getPlaybackState() == CanaryVideoPlayer.STATE_ENDED)
            return false;

        return actualMediaPlayerPosition <= seekBarPosition;

    }


    private void toggleFlag() {
        if (!hasInternetConnection()) {
            return;
        }
        entry.starred = !entry.starred;
        showFlaggedStatus();
        onRequestEntryFlagged(entry.id, entry.starred);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Entry entry = EntryDatabaseService.getEntryFromEntryId(entryId);
        if (entry == null) {
            onEntryDeleted(new EntryDeleted(entryId));
            return;
        }
        boolean shareOverlay = getIntent().getBooleanExtra("show_share_overlay", false);
        if (shareOverlay) {
            ShareUtil.beforeNeededExportAction(this, entry);
            getIntent().putExtra("show_share_overlay", false);
        }
        if (GlobalSirenTimeout.getInstance().isSirenCountingDown()) {
            sirenUtil.showSirenView();
        } else {
            sirenUtil.dismissSiren();
        }

        TinyMessageBus.post(new TutorialRequest());
        TinyMessageBus.post(new GetLocation(entry.getLocationId()));

    }

    @Override
    public boolean hasSirenFooter() {
        return DateUtil.getCurrentTime().getTime() - entry.startTime.getTime() < TimeUnit.HOURS.toMillis(12);
    }

    @Override
    protected void onPause() {
        super.onPause();
        seekBarHandler.removeCallbacks(seekBarUpdater);

        try {
            canaryVideoPlayer.setPlayWhenReady(false);
            binding.playPauseBtn.setImageResource(R.drawable.ic_play);
        } catch (Exception ignore) {
            Crashlytics.logException(ignore);
        }

        if (sirenUtil != null) {
            sirenUtil.removeSirenListener();
        }
    }

    OnClickListener postButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            postComment(
                    binding.postEditText.getText()
                            .toString());
            hideSoftKeyboard();
        }
    };

    private void showFlaggedStatus() {
        if (entry.starred) {
            binding.saveEntryIcon.setImageResource(R.drawable.bookmarked);
        } else {
            binding.saveEntryIcon.setImageResource(R.drawable.bookmark);
        }
    }

    public void onRequestEntryFlagged(final long id, final boolean isFlagged) {
        if (!hasInternetConnection()) {
            return;
        }

        EntryAPIService.setEntryRecordFlag(
                id, isFlagged, new Callback<Void>() {
                    @Override
                    public void success(Void aVoid, Response response) {
                        // flagged patch was accepted on the server - now update the database asynchronously
                        EntryDatabaseService.setEntryAsFlagged(id, isFlagged);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                    }
                });
    }

    public void postComment(final String commentText) {

        if (!hasInternetConnection()) {
            return;
        }

        final Comment comment = new Comment();

        comment.body = commentText;
        comment.created = DateUtil.getCurrentTime();
        comment.customerUri = Utils.buildResourceUri(Constants.CUSTOMER_URI, customerId);
        comment.entryUri = entry.resourceUri;
        comment.modified = comment.created;

        CommentAPIServices.createComment(
                commentText, entry.resourceUri, new Callback<Void>() {
                    @Override
                    public void success(Void aVoid, Response response) {
                        binding.postEditText.setText(null);

                        GoogleAnalyticsHelper.trackEntry(ACTION_ENTRY_COMMENT,
                                PROPERTY_ENTRY, null, entry.getLocationId(), entry.id);
                        if (response.getStatus() == 201) {

                            String commentUri = "";
                            for (Header header : response.getHeaders()) {
                                if ("location".equalsIgnoreCase(header.getName())) {
                                    commentUri = header.getValue()
                                            .substring(Constants.BASE_URL.length());
                                    break;
                                }
                            }

                            comments.add(comment);
                            comment.resourceUri = commentUri;
                            if (commentUri.length() != 0) {
                                comment.id = Utils.getIntFromResourceUri(commentUri);
                            }

                            CommentDatabaseService.updateOrInsertComment(comment);

                            if (!isFinishing()) {
                                addCommentView(comment);
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (!isFinishing() && !isPaused()) {
                            try {
                                openAlert = AlertUtils.showGenericAlert(EntryDetailActivity.this, Utils.getErrorMessageFromRetrofit(EntryDetailActivity.this, error));
                            } catch (JSONException ignore) {
                            }
                        }
                    }
                });
    }

    private void updateEntrySummary() {
        String labels = LabelDatabaseService.getLabelDisplayTextForEntryId(entry.id);
        binding.entryLabels.setText(labels);

        if (labels == null || labels.length() == 0) {
            binding.tagContainer.setVisibility(View.GONE);
            binding.tagButtonLabel.setText(R.string.tag);
            binding.tagIcon.setImageResource(R.drawable.tag);
        } else {
            binding.tagContainer.setVisibility(View.VISIBLE);
            binding.tagButtonLabel.setText(R.string.edit_tag);
            binding.tagIcon.setImageResource(R.drawable.edit_tag);
        }
    }

    private void setupEntryActions() {
        binding.watchLiveIcn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startWatchLive();
            }
        });

        binding.saveLayout.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Utils.isDemo()) {
                            if (TouchTimeUtil.dontAllowTouch()) {
                                return;
                            }

                            AlertUtils.showGenericAlert(EntryDetailActivity.this, getString(R.string.save_video), getString(R.string.save_video_dsc));
                            return;
                        }

                        // if this would uncheck the flag then put up an alert first to make sure
                        if (entry.starred) {

                            openAlert = AlertUtils.showUnSaveWarningAlert(EntryDetailActivity.this, entry.startTime, new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    GoogleAnalyticsHelper.trackEntry(ACTION_UNSAVE,
                                            PROPERTY_ENTRY, null, entry.getLocationId(), entry.id);
                                    toggleFlag();
                                }
                            });


                        } else {
                            GoogleAnalyticsHelper.trackEntry(ACTION_SAVE,
                                    PROPERTY_ENTRY, null, entry.getLocationId(), entry.id);
                            toggleFlag();
                        }

                    }
                });


        binding.cvLayout.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Utils.isDemo()) {
                            if (TouchTimeUtil.dontAllowTouch()) {
                                return;
                            }
                            AlertUtils.showGenericAlert(EntryDetailActivity.this, getString(R.string.help_canary_learn), getString(R.string.help_canary_learn_dsc));
                            return;
                        }
                        if (!hasInternetConnection()) {
                            return;
                        }

                        if (binding.moreButtonLayout.getVisibility() == View.VISIBLE) {
                            hideMoreOptionsLayout();
                        }

                        EntryDetailTagFragment entryDetailTagFragment = EntryDetailTagFragment.newInstance(entry.id);

                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                                .add(R.id.fragment_container, entryDetailTagFragment, entryDetailTagFragment.getClass().getSimpleName())
                                .addToBackStack(null)
                                .commit();
                    }
                });

        updateShareIcon();
        binding.shareLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isDemo()) {
                    return;
                }

                if (!hasInternetConnection()) {
                    return;
                }

                if (TouchTimeUtil.dontAllowTouch())
                    return;

                if (subscription != null && !subscription.haveFullVideo()) {
                    showSingleEntryUpSell();
                    return;
                }

                ShareUtil.beforeNeededExportAction(EntryDetailActivity.this, entry);


            }


        });

        if (Utils.isDemo()) {
            binding.postEditText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        AlertUtils.showGenericAlert(EntryDetailActivity.this, getString(R.string.leave_a_comment), getString(R.string.leave_a_comment_dsc));

                    return true;
                }
            });
            return;
        }
        binding.postBtn.setOnClickListener(postButtonListener);
        binding.postEditText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        binding.postBtn.setEnabled(s.length() != 0);
                    }
                });
    }

    private void showSingleEntryUpSell() {
        Intent i = new Intent(EntryDetailActivity.this, SettingsFragmentStackActivity.class);
        i.setAction(single_entry_upsell);
        i.putExtra(extra_locationId, entry.getLocationId());
        i.putExtra(modal, true);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.scale_alpha_push);

    }

    public void updateShareIcon() {
        EntryUtil.DOWNLOADSTATUS downloadstatus = getDownloadProgress(entry);
        binding.shareIcon.clearAnimation();
        switch (downloadstatus) {
            case NOT_STARTED:
                binding.shareIcon.setImageResource(R.drawable.share);
                binding.shareButtonLabel.setText(R.string.share);

                break;
            case IN_PROGRESS:
                binding.shareIcon.setImageResource(R.drawable.preparing);
                Animation rotation = AnimationUtils.loadAnimation(EntryDetailActivity.this, R.anim.clockwise_rotation);
                binding.shareIcon.startAnimation(rotation);
                binding.shareButtonLabel.setText(R.string.preparing);
                break;
            case DOWNLOAD_READY:
                binding.shareIcon.setImageResource(R.drawable.share_ready);
                binding.shareButtonLabel.setText(R.string.share);
                break;

        }

    }

    private void startWatchLive() {
        if (!hasInternetConnection()) {
            return;
        }

        if (pageVideoDataMap == null || pageVideoDataMap.isEmpty())
            return;

        Device device = pageVideoDataMap.get(nowPlayingIndex).deviceObject;

        if (device == null || !device.isOnline) {
            AlertUtils.showGenericAlert(this, getString(R.string.device_is_offline));
            return;
        }

        if (ModeCache.getMode(privacy).resourceUri.equals(ModeDatabaseService.getModeForLocation(
                entry.getLocationId()).resourceUri)) {
            AlertUtils.showGenericAlert(this, getString(R.string.location_is_in_privacy_mode));
            return;
        }

        Intent i = new Intent(EntryDetailActivity.this, WatchLiveActivity.class);
        i.putExtra(CanaryDeviceContentProvider.COLUMN_UUID, device.uuid);
        i.putExtra(CanaryDeviceContentProvider.COLUMN_LOCATION_ID, device.location);
        startActivity(i);
    }

    private String getStartDateString() {
        return DateUtil.utcDateToDisplayString(entry.startTime);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        View current = getCurrentFocus();
        if (current != null) {
            current.clearFocus();
        }

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            binding.eventHeaderLayout.setVisibility(View.GONE);
            binding.landscapeHidden.setVisibility(View.GONE);
            sirenUtil.setVisible(View.GONE);

            if (subscription != null && subscription.haveFullVideo()) {
                binding.dateTimestampLandscape.setVisibility(View.VISIBLE);
                binding.dateTimestampPortrait.setVisibility(View.GONE);
            }

            binding.rewindBtnLandscape.setVisibility(View.VISIBLE);
            binding.rewindBtnPortrait.setVisibility(View.GONE);
            LinearLayout.LayoutParams playbackContainerParams = (LinearLayout.LayoutParams) binding.playbackContainer.getLayoutParams();
            playbackContainerParams.height = deviceWidth;
            binding.playbackContainer.setLayoutParams(playbackContainerParams);

            RelativeLayout.LayoutParams videoFrameParams = (RelativeLayout.LayoutParams) binding.videoLayout.textureView.getLayoutParams();
            videoFrameParams.height = deviceWidth;
            videoFrameParams.topMargin = 0;
            binding.videoLayout.textureView.setLayoutParams(videoFrameParams);
            binding.topFrame.setBackgroundColor(getResources().getColor(R.color.transparent));
            binding.bottomFrame.setBackgroundColor(getResources().getColor(R.color.transparent));
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            binding.eventHeaderLayout.setVisibility(View.VISIBLE);
            binding.landscapeHidden.setVisibility(View.VISIBLE);

            if (hasSirenFooter()) {
                sirenUtil.setVisible(View.VISIBLE);
            }

            if (subscription != null && subscription.haveFullVideo()) {
                binding.dateTimestampLandscape.setVisibility(View.GONE);
                binding.dateTimestampPortrait.setVisibility(View.VISIBLE);
            }
            binding.rewindBtnLandscape.setVisibility(View.GONE);
            binding.rewindBtnPortrait.setVisibility(View.VISIBLE);
            int videoHeight = (int) ((float) deviceWidth * (9.0f / 16.0f));

            LinearLayout.LayoutParams playbackContainerParams = (LinearLayout.LayoutParams) binding.playbackContainer.getLayoutParams();
            playbackContainerParams.height = videoHeight + DensityUtil.dip2px(this, 110);
            binding.playbackContainer.setLayoutParams(playbackContainerParams);

            RelativeLayout.LayoutParams videoFrameParams = (RelativeLayout.LayoutParams) binding.videoLayout.textureView.getLayoutParams();
            videoFrameParams.height = videoHeight;
            videoFrameParams.topMargin = (int) getResources().getDimension(R.dimen.video_border_mask_top);
            binding.videoLayout.textureView.setLayoutParams(videoFrameParams);
            binding.topFrame.setBackgroundColor(getResources().getColor(R.color.black));
            binding.bottomFrame.setBackgroundColor(getResources().getColor(R.color.black));
        }

        binding.videoLayout.textureView.setMatrix(new Matrix());
        binding.zoomTextView.setText("1x");

        binding.zoomTextView.setAlpha(0.0f);
        lastZoom = 1.0f;

        binding.videoLayout.textureView.invalidate();
        binding.playbackContainer.invalidate();

        if (openAlert != null && openAlert.isShowing())
            openAlert.dismiss();

        binding.moreButtonLayout.setVisibility(View.GONE);
        binding.blackOverlayView.setVisibility(View.GONE);
        binding.moreOptionsBackground.setVisibility(View.GONE);

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    getCurrentFocus()
                            .getWindowToken(), 0);

        } catch (Exception ignore) {

        }
        binding.entryScrollView.scrollTo(0, 0);

    }

    private void addCommentView(Comment comment) {
        View commentView;

        String whoAmI = Utils.buildResourceUri(Constants.CUSTOMER_URI, customerId);
        if (whoAmI.equalsIgnoreCase(comment.customerUri)) {
            commentView = getLayoutInflater().inflate(R.layout.entry_comment_view_right, null, false);
        } else {
            commentView = getLayoutInflater().inflate(R.layout.entry_comment_view_left, null, false);
        }

        ((TextView) commentView.findViewById(R.id.comment)).setText(comment.body);
        ((TextView) commentView.findViewById(R.id.comment_timestamp)).setText(DateUtil.utcDateToDisplayString(comment.created));

        Avatar avatar = AvatarDatabaseService.getAvatarFromCustomerId(Utils.getIntFromResourceUri(comment.customerUri));
        FrameLayout avatarLayout = (FrameLayout) commentView.findViewById(R.id.avatar_layout);

        if (avatar != null) {
            ImageUtils.loadAvatar(((ImageView) avatarLayout.findViewById(R.id.avatar_image_layout)), avatar.thumbnailUrl());
        } else {
            // initialize the badge to the customer initials if there is no image
            Customer customer = CustomerDatabaseService.getCustomerFromId(Utils.getIntFromResourceUri(comment.customerUri));
            if (customer != null)
                ((TextView) avatarLayout.findViewById(R.id.customer_initials)).setText(customer.getInitials());
            else
                ((ImageView) avatarLayout.findViewById(R.id.avatar_image_layout)).setImageResource(R.drawable.ic_blankprofile);

        }


        binding.commentContainer.addView(commentView);


    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_SINGLE_ENTRY;
    }

    @Subscribe
    public void onEntryLabelStringUpdated(EntryLabelStringUpdated message) {
        updateEntrySummary();
    }

    @Override
    public void onDestroy() {
        try {
            if (canaryVideoPlayer != null)
                canaryVideoPlayer.release();
        } finally {
            super.onDestroy();
        }
    }

    View.OnTouchListener videoViewTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            gestureDetector.onTouchEvent(event);
            binding.videoLayout.videoViewPager.onTouchEvent(event);

            return true;
        }
    };

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (!showThumbnailsInViewpager) {
                if (binding.videoScrubControls.getVisibility() != View.VISIBLE) {
                    try {
                        showScrubControls();
                    } catch (Exception ignore) {
                    }
                } else {
                    showViewPagerIndicator();
                }
            } else {
                if (hasInternetConnection() && !pageVideoDataMap.isEmpty()) {

                    if (pageVideoDataMap.get(nowPlayingIndex).videoUrl == null) {
                        openAlert = AlertUtils.showGenericAlert(EntryDetailActivity.this, getString(R.string.error_playing_video));
                        return super.onSingleTapConfirmed(e);
                    }
                    showViewPagerIndicator();

                    showThumbnailsInViewpager = false;
                    VideoPlaybackFragment videoPlaybackFragment =
                            adapter.getFragment(nowPlayingIndex);

                    if (videoPlaybackFragment != null) {
                        videoPlaybackFragment.showEventDeviceThumbnail(false);
                        videoPlaybackFragment.showVideoLoadingOverlay(true);
                        autoPlay = true;
                    }
                    String device = null;
                    if (!pageVideoDataMap.isEmpty()) {
                        device = pageVideoDataMap.get(nowPlayingIndex).deviceObject.uuid;
                    }
                    GoogleAnalyticsHelper.trackEntry(ACTION_ENTRY_PLAY, PROPERTY_ENTRY, device,
                            entry.getLocationId(), entry.id);


                    binding.videoLayout.videoViewPager.setOnTouchListener(null);
                    prepareVideoPlayback();
                }
            }
            return super.onSingleTapConfirmed(e);
        }

    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {

        if (showThumbnailsInViewpager) {
            binding.videoLayout.textureViewContainer.setVisibility(View.GONE);
            binding.bottomFrame.setVisibility(View.GONE);
            binding.topFrame.setVisibility(View.GONE);
        }

        if (autoPlay || !showThumbnailsInViewpager)
            prepareVideoPlayback();
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

    private void showScrubControls() {
        if (binding.videoScrubControls.getVisibility() != View.VISIBLE) {
            if (pageVideoDataMap.size() > 1) {
                Utils.crossFadeViews(binding.videoScrubControls, binding.circlePageIndicator);
            } else {
                Utils.fadeIn(binding.videoScrubControls);
            }
        }
    }

    private void showViewPagerIndicator() {
        if (binding.circlePageIndicator.getVisibility() != View.VISIBLE) {
            if (pageVideoDataMap.size() > 1) {
                Utils.crossFadeViews(binding.circlePageIndicator, binding.videoScrubControls);
            } else {
                Utils.fadeOut(binding.videoScrubControls);
            }
        }
    }

    @Subscribe
    public void onPushReceived(PushReceived pushReceived) {
        PushUtils.showPush(this, pushReceived);
    }

    private AlertDialog deleteAlert;

    @Subscribe
    public void onEntryDeleted(EntryDeleted entryDeleted) {
        if (entryDeleted.entryId == entry.id) {
            if (deleteAlert == null) {
                deleteAlert = AlertUtils.showGenericAlert(this, getString(R.string.entry_deleted), getString(R.string.entry_deleted_no_longer),
                        0, getString(R.string.okay), null, 0, 0, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }, null);
                deleteAlert.setCancelable(false);
                deleteAlert.setCanceledOnTouchOutside(false);
            }
        }
    }

    @Subscribe
    public void onShareCompletedProcessing(ShareCompletedProcessing updateEntry) {
        if (entryId == updateEntry.entryId) {
            entry.exported = true;
            updateShareIcon();
        }
    }

    @Subscribe
    public void onShareStartedProcessing(ShareStartedProcessing updateEntry) {
        if (entryId == updateEntry.entryId) {
            updateShareIcon();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.scale_alpha_pop, R.anim.slide_out_right);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FILE_SYSTEM: {
                // If request is cancelled, the result arrays are empty.
                boolean hasPermission = PermissionUtil.hasPermission(permissions, grantResults, this);

                if (hasPermission) {
                    ExportUtil.fetchDownloadUrl(this, entry);
                }

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SUBSCRIPTION_UPDATE) {
            if (entry == null)
                return;
            SubscriptionAPIService.getSubscription(entry.getLocationId(), new Callback<Subscription>() {
                @Override
                public void success(Subscription subscription, Response response) {
                    binding.setSubscription(subscription);

                    onConfigurationChanged(getResources().getConfiguration());
                    if (subscription.haveFullVideo()) {
                        getClips();
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }
}
