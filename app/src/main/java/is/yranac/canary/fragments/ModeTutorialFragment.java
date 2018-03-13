package is.yranac.canary.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.adapter.DeviceModeTutorialAdapter;
import is.yranac.canary.databinding.FragmentModeSlideShowBinding;
import is.yranac.canary.fragments.settings.ManageDevicesFragment;
import is.yranac.canary.fragments.setup.PurchaseMembershipFragment;
import is.yranac.canary.fragments.setup.SetUpBaseFragment;
import is.yranac.canary.messages.OTAComplete;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.model.location.NightModeSchedule;
import is.yranac.canary.model.mode.ModeCache;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.api.LocationAPIService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.database.SubscriptionPlanDatabaseService;
import is.yranac.canary.services.jobs.APILocationJobService;
import is.yranac.canary.util.AnimationHelper;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.NightModeUtil;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import jp.wasabeef.blurry.Blurry;

import static is.yranac.canary.model.mode.ModeCache.disarmed;
import static is.yranac.canary.model.mode.ModeCache.privacy;

/**
 * Created by Schroeder on 4/26/16.
 */
public class ModeTutorialFragment extends SetUpBaseFragment {


    private Device device;
    private NightModeSchedule schudele;
    private boolean resetAfterTransition = false;
    private boolean startedClicked = false;

    public enum TutorialType {
        MODE_TUTORIAL,
        OTA
    }

    public static ModeTutorialFragment newInstance(TutorialType showHeader) {
        ModeTutorialFragment fragment = new ModeTutorialFragment();
        Bundle args = new Bundle();
        args.putSerializable("type", showHeader);
        fragment.setArguments(args);
        return fragment;
    }

    private int firstSelection = -1;
    private int secondSelection = -1;
    private int thirdSelection = -1;


    private static final String LOG_TAG = "DeviceSetupViewPagerFragment";

    private int width = 0;

    private int horizontalPadding;
    private int marginSmall;
    private int marginLarge;
    private TutorialType tutorialType;

    private DeviceModeTutorialAdapter adapter;
    private DeviceType deviceType;

    private FragmentModeSlideShowBinding binding;

    private boolean otaComplete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mode_slide_show, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            tutorialType = (TutorialType) bundle.getSerializable("type");
        } else if (tutorialType == null) {
            tutorialType = TutorialType.OTA;

        }

        if (tutorialType == TutorialType.MODE_TUTORIAL) {
            startedClicked = true;
        }
        adapter = new DeviceModeTutorialAdapter(getActivity(), tutorialType);
        binding.pager.setAdapter(adapter);
        binding.setTutorialType(tutorialType.ordinal());

        binding.blurImageView.setColorFilter(getResources().getColor(R.color.black_ten));

        horizontalPadding = (int) getResources().getDimension(R.dimen.padding_mode_onboardin);
        marginSmall = (int) getResources().getDimension(R.dimen.margin_top_mode_small);
        marginLarge = (int) getResources().getDimension(R.dimen.margin_top_mode_large);


        ((RelativeLayout.LayoutParams) binding.canaryThreeModes.getLayoutParams()).setMargins(0, marginSmall, 0, 0);

        view.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        ImageView imageView = binding.deviceArmedImageView;
                        if (tutorialType == TutorialType.OTA) {
                            imageView.setPivotY((imageView.getHeight() / 2.0f) - DensityUtil.dip2px(getContext(), -50));
                            imageView.setPivotX((imageView.getWidth() / 2.0f));
                        }
                        binding.customizeImageView.setPivotX(binding.customizeImageView.getWidth() / 2.0f);
                        binding.customizeImageView.setPivotY(binding.customizeImageView.getHeight() / 2.0f);
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        width = view.getWidth();
                        int height = view.getHeight();
                        setupImages(width, height);
                        setupLayouts();
                    }
                });


        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subscription subscription = SubscriptionPlanDatabaseService.getServicePlanForLocation(device.getLocationId());

                if (subscription.hasMembership) {
                    fragmentStack.enableBackButton();
                    fragmentStack.addFragmentAndResetStack(ManageDevicesFragment.newInstance(device.getLocationId()), Utils.SLIDE_FROM_RIGHT);
                } else {
                    fragmentStack.addFragmentAndResetStack(PurchaseMembershipFragment.newInstance(device), Utils.SLIDE_FROM_RIGHT);
                }
            }
        });


        if (tutorialType == TutorialType.MODE_TUTORIAL) {
            binding.indicator.setViewPager(binding.pager);
            binding.indicator.setOnPageChangeListener(onPageChangeListener);
            binding.setupSteps.setVisibility(View.GONE);
            binding.nextBtn.setVisibility(View.GONE);
        } else {
            binding.indicator.setVisibility(View.GONE);
            binding.pager.addOnPageChangeListener(onPageChangeListener);
        }


        binding.startTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.pager.setScrollDurationFactor(3.0);
                binding.startTutorial.setEnabled(false);
                binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1, 100);
            }
        });

        if (device != null) {
            setDeviceUri(device.resourceUri);
        }

        binding.modeActionLayout.
                modeActionOne
                .setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            binding.pager.setScrollDurationFactor(3.0);
                                            binding.modeActionLayout.modeActionOne.setEnabled(true);
                                            binding.modeActionLayout.modeActionTwo.setEnabled(false);
                                            binding.modeActionLayout.modeActionThree.setEnabled(false);
                                            switch (binding.pager.getCurrentItem()) {
                                                case 2:
                                                    firstSelection = 1;
                                                    DeviceAPIService.changeDeviceMotionSettings(device.id, 0.3f);
                                                    adapter.setPageCount(5);
                                                    break;
                                                case 4:
                                                    secondSelection = 1;
                                                    DeviceAPIService.changeDeviceHomeMode(device.id, ModeCache.getMode(disarmed).id);
                                                    adapter.setPageCount(7);
                                                    break;
                                                case 6:
                                                    thirdSelection = 1;
                                                    adapter.setPageCountOverride(8);
                                                    break;

                                            }
                                            binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1, 100);

                                        }
                                    }
                );

        binding.modeActionLayout.
                modeActionTwo
                .setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            binding.pager.setScrollDurationFactor(3.0);
                                            binding.modeActionLayout.modeActionOne.setEnabled(false);
                                            binding.modeActionLayout.modeActionTwo.setEnabled(true);
                                            binding.modeActionLayout.modeActionThree.setEnabled(false);
                                            binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1, 100);
                                            switch (binding.pager.getCurrentItem()) {
                                                case 2:
                                                    firstSelection = 2;
                                                    DeviceAPIService.changeDeviceMotionSettings(device.id, 0.8f);
                                                    adapter.setPageCount(5);
                                                    break;
                                                case 4:
                                                    secondSelection = 2;
                                                    DeviceAPIService.changeDeviceHomeMode(device.id, ModeCache.getMode(privacy).id);
                                                    adapter.setPageCount(7);
                                                    break;
                                                case 6:
                                                    thirdSelection = 2;
                                                    int locationId = Utils.getIntFromResourceUri(device.location);
                                                    LocationAPIService.setNightModeEnabled(null, locationId, false);
                                                    adapter.setPageCountOverride(11);
                                                    break;

                                            }
                                        }
                                    }
                );

        binding.modeActionLayout.
                modeActionThree
                .setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            binding.pager.setScrollDurationFactor(3.0);
                                            binding.modeActionLayout.modeActionOne.setEnabled(false);
                                            binding.modeActionLayout.modeActionTwo.setEnabled(false);
                                            binding.modeActionLayout.modeActionThree.setEnabled(true);
                                            binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1, 100);
                                            switch (binding.pager.getCurrentItem()) {
                                                case 2:
                                                    firstSelection = 3;
                                                    DeviceAPIService.changeDeviceMotionSettings(device.id, 0.5f);
                                                    adapter.setPageCount(5);
                                                    break;
                                                case 4:
                                                    secondSelection = 3;
                                                    adapter.setPageCount(7);
                                                    break;
                                                case 6:
                                                    thirdSelection = 3;
                                                    adapter.setPageCountOverride(11);
                                                    break;

                                            }
                                        }
                                    }
                );


        binding.modeActionLayout.modeActionNightStart.setEnabled(false);
        binding.modeActionLayout.modeActionNightStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                NightModeUtil.setUpNightModeDialog(schudele.startTime, getContext(), getString(R.string.i_go_to_bed_at), new NightModeUtil.NightModeCallback() {
                    @Override
                    public void onComplete(String date) {
                        schudele.startTime = date;
                        binding.modeActionLayout.nightModeStartTime.setText(schudele.localStartTime());
                    }
                });
            }
        });
        binding.modeActionLayout.modeActionNightEnd.setEnabled(false);
        binding.modeActionLayout.modeActionNightEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                NightModeUtil.setUpNightModeDialog(schudele.endTime, getContext(), getString(R.string.i_wake_up_at), new NightModeUtil.NightModeCallback() {
                    @Override
                    public void onComplete(String date) {
                        schudele.endTime = date;
                        binding.modeActionLayout.nightModeStartTime.setText(schudele.localEndTime());
                    }
                });

            }
        });
        binding.modeActionLayout.modeActionNightSet.setEnabled(false);
        binding.modeActionLayout.modeActionNightSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                binding.pager.setScrollDurationFactor(3.0);
                adapter.setPageCount(12);
                LocationAPIService.createNightMode(Utils.getIntFromResourceUri(device.location), schudele);
                int locationId = Utils.getIntFromResourceUri(device.location);
                LocationAPIService.setNightModeEnabled(null, locationId, true);
                binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1, 100);
            }
        });

        if (tutorialType == TutorialType.OTA) {
            binding.pager.setOnTouchListener(new View.OnTouchListener() {

                private View inside;

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    float x = event.getRawX();
                    float y = event.getRawY();

                    if (tutorialType == TutorialType.OTA && binding.pager.getCurrentItem() == 0 && !startedClicked) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                            case MotionEvent.ACTION_MOVE: {
                                if (insideButton(binding.startTutorial, x, y)) {
                                    binding.startTutorial.setEnabled(true);
                                }
                                break;
                            }
                            case MotionEvent.ACTION_UP: {
                                if (insideButton(binding.startTutorial, x, y)) {
                                    binding.startTutorial.performClick();
                                    binding.startTutorial.setEnabled(false);
                                    resetAfterTransition = true;
                                }
                            }
                        }
                        return true;
                    }


                    if (inside != null) {
                        if (!insideButton(inside, x, y)) {
                            inside.setEnabled(false);
                            inside = null;
                        } else {
                            inside.setEnabled(true);
                            return true;
                        }
                    }

                    return false;
                }
            });
        }

        if (device != null) {
            setDeviceUri(device.resourceUri);
        }


    }


    private void setupText() {

        if (binding == null || deviceType == null || tutorialType == null)
            return;

        binding.setTutorialType(tutorialType.ordinal());

        int firstSlideHeader;
        int firstSlideDsc;
        int firstSlideTag;
        int deviceLights;
        int otaMessage;
        switch (tutorialType) {

            case MODE_TUTORIAL:
                firstSlideHeader = R.string.protect_your_home;
                firstSlideDsc = R.string.canary_modes_help_you_personalize;
                firstSlideTag = R.string.swipe_to_start_tutorial;
                deviceLights = R.string.if_you_have_more_than_one;
                otaMessage = R.string.blank;
                break;

            case OTA:
                firstSlideHeader = R.string.canary_is_online;
                switch (deviceType.id) {
                    case DeviceType.CANARY_AIO:
                        firstSlideDsc = R.string.canary_will_update;
                        otaMessage = R.string.your_canary_is_still_updating;
                        break;
                    case DeviceType.FLEX:
                        firstSlideDsc = R.string.canary_flex_will_update;
                        otaMessage = R.string.your_canary_flex_is_still_updating;
                        break;
                    case DeviceType.CANARY_VIEW:
                        firstSlideDsc = R.string.canary_view_will_update;
                        otaMessage = R.string.your_canary_view_is_still_updating;
                        break;
                    default:
                        return;
                }
                deviceLights = R.string.when_monitoring_white_light;
                firstSlideTag = R.string.while_updates_swipe;
                break;
            default:
                return;
        }


        binding.establishingConnectionTextView.setText(firstSlideHeader);
        binding.downloadingLatestTextView.setText(firstSlideDsc);
        binding.canaryModeTextView.setText(firstSlideTag);
        binding.deviceLightsDsc.setText(deviceLights);
        binding.otaMessageTextView.setText(otaMessage);

    }

    private void setupImages(final float width, final float height) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<ImageView> imageViews = new ArrayList<>();

                imageViews.add(binding.deviceArmedImageView);
                imageViews.add(binding.awayImageView);
                imageViews.add(binding.homeImageView);
                imageViews.add(binding.nightImageView);
                imageViews.add(binding.autoArmImageView);
                imageViews.add(binding.privacyImageView);
                imageViews.add(binding.customizeImageView);

                final int[] drawables = new int[7];

                if (deviceType != null) {
                    switch (deviceType.id) {
                        case DeviceType.CANARY_AIO:
                            drawables[0] = R.drawable.update1_aio;
                            break;
                        case DeviceType.FLEX:
                            drawables[0] = R.drawable.update1_flex;
                            break;
                        case DeviceType.CANARY_VIEW:
                            drawables[0] = R.drawable.update1_view;
                            break;
                    }
                } else {
                    drawables[0] = R.drawable.update1_aio;

                }

                drawables[1] = R.drawable.update2;
                drawables[2] = R.drawable.update3;
                drawables[3] = R.drawable.update4;
                drawables[4] = R.drawable.update5;
                drawables[5] = R.drawable.update6_android;
                if (tutorialType == TutorialType.MODE_TUTORIAL ||
                        (deviceType != null && deviceType.id == DeviceType.FLEX)) {
                    drawables[6] = R.drawable.update7_flex;
                } else {
                    if (deviceType != null) {
                        switch (deviceType.id) {
                            case DeviceType.CANARY_AIO:
                                drawables[6] = R.drawable.update7_aio;
                                break;
                            case DeviceType.FLEX:
                                drawables[6] = R.drawable.update7_flex;
                                break;
                            case DeviceType.CANARY_VIEW:
                                drawables[6] = R.drawable.update7_view;
                                break;
                        }
                    } else {
                        drawables[6] = R.drawable.update7_aio;
                    }

                }

                for (int i = 0; i < drawables.length; i++) {
                    final ImageView imageView = imageViews.get(i);
                    int drawable = drawables[i];
                    Activity activity = getActivity();
                    if (activity == null)
                        return;
                    if (isDetached())
                        return;
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), drawable);
                    float ratio = width / height;
                    float imageWidth = ratio * bmp.getHeight();

                    float startingX = Math.abs(imageWidth - bmp.getWidth()) / 2.0f;
                    try {
                        bmp = Bitmap.createBitmap(bmp, (int) startingX, 0, (int) imageWidth, bmp.getHeight());
                    } catch (IllegalArgumentException ignore) {

                    }
                    final Bitmap finalBmp = bmp;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(finalBmp);
                        }
                    });
                }

            }
        }).start();


    }


    private void setupLayouts() {
        int newWidth = (int) (width - (getResources().getDimension(R.dimen.mode_text_padding) * 2));
        binding.awayDscTextView.getLayoutParams().width = newWidth;
        binding.homeDscTextView.getLayoutParams().width = newWidth;
        binding.nightDscTextView.getLayoutParams().width = newWidth;
    }

    public void setTutorialType(TutorialType tutorialType) {
        this.tutorialType = tutorialType;

        if (binding != null && binding.deviceArmedImageView.getHeight() != 0) {
            if (tutorialType == TutorialType.OTA) {
                ImageView imageView = binding.deviceArmedImageView;
                imageView.setPivotY((imageView.getHeight() / 2.0f) - DensityUtil.dip2px(getContext(), -50));
                imageView.setPivotX((imageView.getWidth() / 2.0f));
            }
        }
        if (tutorialType == TutorialType.OTA) {
            binding.startTutorial.setVisibility(View.VISIBLE);
        } else {
            binding.startTutorial.setVisibility(View.GONE);
        }
        setupText();

        if (adapter != null) {
            adapter.setTutorialType(tutorialType);
        }

    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
        setupText();
    }

    private boolean insideButton(View view, float xPoint, float yPoint) {

        if (view.getVisibility() == View.VISIBLE) {
            int[] l = new int[2];
            view.getLocationOnScreen(l);
            int x = l[0];
            int y = l[1];
            int w = view.getWidth();
            int h = view.getHeight();

            if (xPoint < x || xPoint > x + w || yPoint < y || yPoint > y + h) {
                return false;
            }
        } else {
            return false;
        }
        return true;

    }


    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        TinyMessageBus.register(this);
    }

    @Override
    public void onStop() {
        TinyMessageBus.unregister(this);
        super.onStop();
    }

    private int previous = 0;
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset != 1.0f) {
                onPageScrolled(position - 1, 1.0f, 0);
            }
            int tempPosition = position;
            try {
                if (tutorialType == TutorialType.OTA) {

                    if (resetAfterTransition && position == 1) {
                        startedClicked = true;
                        resetAfterTransition = false;
                        adapter.setPageCount(8);
                        binding.pager.setCurrentItem(0, false);
                        return;
                    }


                    if (startedClicked) {
                        tempPosition++;
                    }
                }

                switch (tempPosition) {
                    case 0:
                        animationBetweenOneAndTwo(positionOffset);
                        break;
                    case 1:
                        animationBetweenTwoAndThree(positionOffset);
                        break;
                    case 2:
                        setThreeSlide();
                        animationBetweenThreeAndFour(positionOffset);
                        break;
                    case 3:
                        animationBetweenFourAndFive(positionOffset);
                        break;
                    case 4:
                        animationBetweenFiveAndSix(positionOffset);
                        break;
                    case 5:
                        animationBetweenSixAndSeven(positionOffset);
                        break;
                    case 6:
                        animationBetweenSevenAndEight(positionOffset);
                        break;
                    case 7:
                        animationBetweenEightAndNine(positionOffset);
                        break;
                    case 8:
                        setOTASlide();
                        break;


                }

                if (startedClicked) {
                    if (positionOffset != 1.0f) {
                        if (previous < position && position == adapter.getCount() - 1) {
                            AnimationHelper.slideDownAndOut(binding.setupSteps, 250);
                        } else if (previous == adapter.getCount() - 1 && previous != position) {
                            AnimationHelper.slideUpAndIn(binding.setupSteps, 250);
                        }
                    }
                }


            } finally {
                if (!startedClicked) {
                    if (position == 0 && positionOffset > 0.0f) {
                        AnimationHelper.slideUpAndIn(binding.setupSteps, 250);
                    } else if (!otaComplete) {
                        AnimationHelper.slideDownAndOut(binding.setupSteps, 250);
                    }
                    return;
                }

                if (position == adapter.getCount() - 2) {
                    binding.rightArrow.setAlpha(1.0f - (positionOffset / 2.0f));
                } else if (position == adapter.getCount() - 1) {
                    binding.rightArrow.setAlpha(0.5f);
                } else {
                    binding.rightArrow.setAlpha(1.0f);
                }

                if (position == 0) {
                    binding.leftArrow.setAlpha(0.5f + (positionOffset / 2.0f));
                } else {
                    binding.leftArrow.setAlpha(1.0f);
                }
                if (positionOffset != 1.0f) {
                    previous = position;
                }
            }
        }


        @Override
        public void onPageSelected(int position) {
            binding.pager.setScrollDurationFactor(1);
            if (tutorialType == TutorialType.OTA) {
                setBlurImage(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void setupNightModeSelectionTransition(float positionOffset) {
        binding.modeActionLayout.modeActionNightStart.setVisibility(View.VISIBLE);
        binding.modeActionLayout.modeActionNightEnd.setVisibility(View.VISIBLE);
        binding.modeActionLayout.modeActionNightSet.setVisibility(View.VISIBLE);
        downAnimation(binding.modeActionLayout.modeActionOne,
                binding.modeActionLayout.modeActionTwo,
                binding.modeActionLayout.modeActionThree,
                positionOffset);
        stagerAnimation(binding.modeActionLayout.modeActionNightStart,
                binding.modeActionLayout.modeActionNightEnd,
                binding.modeActionLayout.modeActionNightSet,
                (positionOffset - 1) * 3.0f);

    }

    private void setUpButton(int tempPosition) {

        ImageView background;
        TextView layout;
        String newText;
        String buttonOneText;
        String buttonTwoText;
        String buttonThreeText = getString(R.string.i_will_decide_later);
        int selection;
        switch (tempPosition) {
            case 1:
            case 2:
                layout = binding.awayDscTextView;
                background = binding.awayImageView;
                newText = getString(R.string.what_do_you_want_notifications);
                buttonOneText = getString(R.string.i_want_all_notifications);
                buttonTwoText = getString(R.string.i_dont_want_subtle_motion);
                selection = firstSelection;
                break;
            case 3:
            case 4:
                layout = binding.homeDscTextView;
                background = binding.homeImageView;
                newText = getString(R.string.when_you_are_home_you_want);
                buttonOneText = getString(R.string.when_home_record);
                buttonTwoText = getString(R.string.when_home_dont_record);
                selection = secondSelection;
                break;
            case 5:
            case 6:
                layout = binding.nightDscTextView;
                background = binding.nightImageView;
                newText = getString(R.string.do_you_want_night_mode);
                buttonOneText = getString(R.string.i_want_night_mode);
                buttonTwoText = getString(R.string.i_dont_want_night_mode);
                selection = thirdSelection;
                break;
            default:
                return;
        }

        binding.modeActionLayout.locationSettingTitle.setText(newText);
        binding.modeActionLayout.modeActionOne.setText(buttonOneText);
        binding.modeActionLayout.modeActionTwo.setText(buttonTwoText);
        binding.modeActionLayout.modeActionThree.setText(buttonThreeText);
        binding.modeActionLayout.modeActionOne.setEnabled(selection == 1);
        binding.modeActionLayout.modeActionTwo.setEnabled(selection == 2);
        binding.modeActionLayout.modeActionThree.setEnabled(selection == 3);
        binding.modeActionLayout.locationSettingTitle.setTextColor(layout.getCurrentTextColor());
        background.setAlpha(1.0f);
    }


    private Drawable awayBlurredImage;
    private Drawable homeBlurredImage;
    private Drawable nightBlurredImage;

    private boolean busy = false;

    private void setBlurImage(int position) {

        if (position == 2 || position == 3) {
            if (awayBlurredImage != null) {
                binding.blurImageView.setImageDrawable(awayBlurredImage);
                return;
            }
            if (!busy) {
                busy = true;
                Blurry.with(getContext())
                        .radius(10)
                        .sampling(8)
                        .async(new Blurry.ImageComposer.ImageComposerListener() {
                            @Override
                            public void onImageReady(BitmapDrawable drawable) {
                                awayBlurredImage = drawable.getConstantState().newDrawable();
                                binding.blurImageView.setImageDrawable(nightBlurredImage);

                                busy = false;
                            }
                        })
                        .capture(binding.awayImageView)
                        .into(null);
            }
        }
        if (position == 4 || position == 5) {
            if (homeBlurredImage != null) {
                binding.blurImageView.setImageDrawable(homeBlurredImage);
                return;
            }

            if (!busy) {
                busy = true;
                Blurry.with(getContext())
                        .radius(10)
                        .sampling(8)
                        .async(new Blurry.ImageComposer.ImageComposerListener() {
                            @Override
                            public void onImageReady(BitmapDrawable drawable) {
                                homeBlurredImage = drawable.getConstantState().newDrawable();
                                binding.blurImageView.setImageDrawable(nightBlurredImage);
                                busy = false;
                            }
                        })
                        .capture(binding.homeImageView)
                        .into(null);
            }
        }

        if (position == 6 || position == 7) {
            if (nightBlurredImage != null) {
                binding.blurImageView.setImageDrawable(nightBlurredImage);
                return;
            }
            if (!busy) {
                busy = true;
                Blurry.with(getContext())
                        .radius(10)
                        .sampling(8)
                        .async(new Blurry.ImageComposer.ImageComposerListener() {
                            @Override
                            public void onImageReady(BitmapDrawable drawable) {
                                nightBlurredImage = drawable.getConstantState().newDrawable();
                                binding.blurImageView.setImageDrawable(nightBlurredImage);
                                busy = false;
                            }
                        })
                        .capture(binding.nightImageView)
                        .into(null);
            }
        }
    }

    private void animationBetweenOneAndTwo(float position) {


        binding.firstSlideLayout.setAlpha(1.0f);
        if (tutorialType == TutorialType.OTA) {

            int width = ((ViewGroup) binding.startTutorial.getParent()).getWidth();
            int center = width / 2;

            float offset = (position) * width;
            binding.startTutorial.setX(center - offset - binding.startTutorial.getWidth() / 2);
        }
        binding.deviceArmedImageView.setScaleX(2.0f - position);
        binding.deviceArmedImageView.setScaleY(2.0f - position);

        binding.homeModeLayout.setVisibility(View.VISIBLE);
        binding.awayModeLayout.setVisibility(View.VISIBLE);
        binding.nightModeLayout.setVisibility(View.VISIBLE);
        float alphaArmed = position < 0.67f ? position * 3.0f / 2.0f : 1.0f;

        int armedVerticalPadding = (int) (marginLarge * (1.0f - alphaArmed));

        float alphaDisarmed;

        if (position < 0.17f)
            alphaDisarmed = 0.0f;
        else if (position < 0.83f)
            alphaDisarmed = (position - 0.17f) * 3.0f / 2.0f;
        else
            alphaDisarmed = 1.0f;

        int disarmedVerticalPadding = (int) (marginLarge * (1.0f - alphaDisarmed));

        float alphaPrivacy = position > 0.33f ? (position - 0.33f) * 3.0f / 2.0f : 0.0f;
        int privacyVerticalPadding = (int) (marginLarge * (1.0f - alphaPrivacy));

        float awayTextViewAlpha = position > 0.9 ? Math.min(1.0f, (position - 0.9f) * 10f) : 0.0f;
        float homeTextViewAlpha = position > 0.9 ? Math.min(1.0f, (position - 0.9f) * 10f) : 0.0f;
        float nightTextViewAlpha = position > 0.9 ? Math.min(1.0f, (position - 0.9f) * 10f) : 0.0f;


        binding.awayModeLayout.setAlpha(alphaArmed);
        binding.awayModeLayout.setPadding(0, armedVerticalPadding, horizontalPadding, 0);
        binding.awayTextView.setAlpha(awayTextViewAlpha);

        binding.homeModeLayout.setAlpha(alphaDisarmed);
        binding.homeModeLayout.setPadding(0, disarmedVerticalPadding, 0, 0);
        binding.homeTextView.setAlpha(homeTextViewAlpha);

        binding.nightModeLayout.setAlpha(alphaPrivacy);
        binding.nightModeLayout.setPadding(horizontalPadding, privacyVerticalPadding, 0, 0);
        binding.nightTextView.setAlpha(nightTextViewAlpha);

        binding.canaryModeTextView.setAlpha(1.0f - position);
        binding.canaryThreeModes.setAlpha(position);
        binding.establishingConnectionTextView.setAlpha(1.0f - position);
        binding.downloadingLatestTextView.setAlpha(1.0f - position);
        transitionNumbers(1, position);

    }

    private void animationBetweenTwoAndThree(float position) {
        binding.firstSlideLayout.setAlpha(0.0f);
        binding.establishingConnectionTextView.setAlpha(0.0f);
        binding.downloadingLatestTextView.setAlpha(0.0f);

        binding.deviceArmedImageView.setScaleX(1.0f);
        binding.deviceArmedImageView.setScaleY(1.0f);
        binding.homeImageView.setAlpha(0.0f);
        binding.awayTextView.setAlpha(1.0f);
        binding.homeTextView.setAlpha(1.0f);
        binding.nightTextView.setAlpha(1.0f);
        binding.canaryModeTextView.setAlpha(0.0f);

        binding.canaryThreeModes.setAlpha(1.0f - position);

        binding.homeModeLayout.setAlpha(1.0f - position);
        binding.nightModeLayout.setAlpha(1.0f - position);

        int center = width / 2;

        binding.awayModeLayout.setX(center - binding.awayModeLayout.getWidth() / 2);
        binding.homeModeLayout.setX(center - binding.homeModeLayout.getWidth() / 2);
        binding.nightModeLayout.setX(center - binding.nightModeLayout.getWidth() / 2);


        int marginAdjustment = (int) ((1.0f - position) * (marginLarge - marginSmall));

        RelativeLayout.LayoutParams iconDisarmedImageViewLayoutParams = (RelativeLayout.LayoutParams) binding.homeModeLayout.getLayoutParams();
        iconDisarmedImageViewLayoutParams.setMargins(0, marginLarge, 0, 0);
        binding.homeModeLayout.setLayoutParams(iconDisarmedImageViewLayoutParams);


        RelativeLayout.LayoutParams iconPrivacyImageViewLayoutParams = (RelativeLayout.LayoutParams) binding.nightModeLayout.getLayoutParams();
        iconPrivacyImageViewLayoutParams.setMargins(0, marginLarge, 0, 0);
        binding.nightModeLayout.setLayoutParams(iconPrivacyImageViewLayoutParams);

        RelativeLayout.LayoutParams iconArmedImageViewLayoutParams = (RelativeLayout.LayoutParams) binding.awayModeLayout.getLayoutParams();
        iconArmedImageViewLayoutParams.setMargins(0, marginAdjustment + marginSmall, 0, 0);
        int padding = (int) (horizontalPadding * (1.0f - position));


        binding.awayModeLayout.setPadding(0, 0, padding, 0);
        binding.nightModeLayout.setPadding(horizontalPadding, 0, 0, 0);
        binding.homeModeLayout.setPadding(0, 0, 0, 0);
        binding.homeDscTextView.setAlpha(0.0f);
        binding.nightDscTextView.setAlpha(0.0f);

        float alpha = position < 0.9f ? 0.0f : (position - 0.9f) * 10;
        binding.awayDscTextView.setAlpha(alpha);
        binding.deviceArmedImageView.setAlpha(1.0f - position);
        binding.awayImageView.setAlpha(position);
        transitionNumbers(1, position);
    }

    private void setThreeSlide() {
        binding.canaryThreeModes.setAlpha(0.0f);
        binding.nightTextView.setTextColor(getResources().getColor(R.color.black));

        binding.deviceArmedImageView.setAlpha(0.0f);
        binding.awayImageView.setAlpha(1.0f);
        binding.homeImageView.setAlpha(0.0f);
        binding.nightModeLayout.setAlpha(0.0f);
        binding.awayModeLayout.setPadding(0, 0, 0, 0);
        binding.homeImageView.setPadding(0, 0, 0, 0);
        binding.nightModeLayout.setPadding(0, 0, 0, 0);


        RelativeLayout.LayoutParams iconArmedImageViewLayoutParams = (RelativeLayout.LayoutParams) binding.awayModeLayout.getLayoutParams();
        iconArmedImageViewLayoutParams.setMargins(0, marginSmall, 0, 0);
        binding.awayModeLayout.setLayoutParams(iconArmedImageViewLayoutParams);

        RelativeLayout.LayoutParams iconDisarmedImageViewLayoutParams = (RelativeLayout.LayoutParams) binding.homeModeLayout.getLayoutParams();
        iconDisarmedImageViewLayoutParams.setMargins(0, marginSmall, 0, 0);
        binding.homeModeLayout.setLayoutParams(iconDisarmedImageViewLayoutParams);

        RelativeLayout.LayoutParams iconPrivacyImageViewLayoutParams = (RelativeLayout.LayoutParams) binding.nightModeLayout.getLayoutParams();
        iconPrivacyImageViewLayoutParams.setMargins(0, marginSmall, 0, 0);
        binding.nightModeLayout.setLayoutParams(iconPrivacyImageViewLayoutParams);


        binding.awayDscTextView.setAlpha(1.0f);
        binding.homeDscTextView.setAlpha(1.0f);
        binding.nightDscTextView.setAlpha(1.0f);
    }

    private void animationBetweenThreeAndFour(float position) {
        binding.awayDscTextView.setAlpha(1.0f);
        binding.homeDscTextView.setAlpha(1.0f);
        binding.nightDscTextView.setAlpha(1.0f);
        crossFade(binding.homeImageView, binding.awayImageView, position);
        slideInAndOut(binding.awayModeLayout, binding.homeModeLayout, position);
    }


    private void animationBetweenFourAndFive(float position) {
        binding.awayImageView.setAlpha(0.0f);
        binding.nightTextView.setTextColor(getResources().getColor(R.color.white));
        crossFade(binding.nightImageView, binding.homeImageView, position);
        slideInAndOut(binding.homeModeLayout, binding.nightModeLayout, position);
        transitionNumbers(3, position);
    }

    private void animationBetweenFiveAndSix(float position) {
        crossFade(binding.autoArmImageView, binding.nightImageView, position);
        slideInAndOut(binding.nightModeLayout, binding.autoArmLayout, position);
        transitionNumbers(4, position);
    }

    private void animationBetweenSixAndSeven(float position) {
        crossFade(binding.privacyImageView, binding.autoArmImageView, position);
        slideInAndOut(binding.autoArmLayout, binding.privacyModeLayout, position);

        transitionNumbers(5, position);
    }

    private void animationBetweenSevenAndEight(float position) {
        binding.autoArmImageView.setAlpha(0.0f);
        crossFade(binding.customizeImageView, binding.privacyImageView, position);
        slideInAndOut(binding.privacyModeLayout, binding.personalizeModes, position);

        transitionNumbers(6, position);
    }

    private void animationBetweenEightAndNine(float position) {
        binding.otaFinishLayout.setAlpha(1.0f);
        slideInAndOut(binding.personalizeModes, binding.otaFinishLayout, position);
        binding.customizeImageView.setScaleX(1.0f + position);
        binding.customizeImageView.setScaleY(1.0f + position);

    }

    private void setNineSlide() {

        if (tutorialType == TutorialType.OTA) {

            int center = width / 2;
            binding.customizeImageView.setAlpha(1.0f);
            binding.privacyImageView.setAlpha(0.0f);
            binding.customizeImageView.setScaleX(1.0f);
            binding.customizeImageView.setScaleY(1.0f);
            binding.privacyModeLayout.setX(center - width - binding.privacyModeLayout.getWidth() / 2);
            binding.personalizeModes.setX(center - binding.personalizeModes.getWidth() / 2);
        } else {
            int center = width / 2;
            binding.customizeImageView.setScaleX(2.0f);
            binding.customizeImageView.setScaleY(2.0f);
            binding.personalizeModes.setX(center - width - binding.personalizeModes.getWidth() / 2);
            binding.customizeModes.setX(center - binding.customizeModes.getWidth() / 2);
        }
    }

    private void animationBetweenNineAndTen(float position) {
        binding.customizeImageView.setScaleX(1.0f + position);
        binding.customizeImageView.setScaleY(1.0f + position);
        binding.otaFinishLayout.setAlpha(1.0f);
        slideInAndOut(binding.personalizeModes, binding.otaFinishLayout, position);
        transitionNumbers(7, position);

    }

    private void setOTASlide() {
        binding.customizeImageView.setScaleX(2.0f);
        binding.customizeImageView.setScaleY(2.0f);
        slideInAndOut(binding.personalizeModes, binding.otaFinishLayout, 1.0f);
        binding.otaFinishLayout.setAlpha(1.0f);
        if (otaComplete) {
            binding.pager.setVisibility(View.GONE);
            AnimationHelper.fadeViewOut(binding.setupSteps, 250);
        }
    }

    private void crossFade(@NonNull ImageView viewIn, @NonNull ImageView viewOut, @FloatRange(from = 0.0, to = 1.0) float position) {
        viewIn.setVisibility(View.VISIBLE);
        viewOut.setVisibility(View.VISIBLE);
        viewIn.setAlpha(position);
        viewOut.setAlpha(1.0f - position);
    }

    private void slideInAndOut(@NonNull View viewOut, @NonNull View viewIn, @FloatRange(from = 0.0, to = 1.0) float position) {
        viewOut.setVisibility(View.VISIBLE);
        viewIn.setVisibility(View.VISIBLE);
        viewIn.setAlpha(1.0f);
        viewOut.setAlpha(1.0f);
        int width = ((ViewGroup) viewIn.getParent()).getWidth();
        int center = width / 2;
        float offset = position * width;
        float offset2 = width * (1.0f - position);
        viewOut.setX(center - offset - viewOut.getWidth() / 2);
        viewIn.setX(center + offset2 - viewIn.getWidth() / 2);

    }


    private void stagerAnimation(View view1, View view2, View view3, float position) {
        int width = ((ViewGroup) view1.getParent()).getWidth();
        int center = width / 2;
        float offset = (float) ((position * 0.7) * width);
        float offset2 = (float) ((position * 0.6) * width);
        float offset3 = (float) ((position * 0.5) * width);
        view1.setX(center - offset - view1.getWidth() / 2);
        view2.setX(center - offset2 - view2.getWidth() / 2);
        view3.setX(center - offset3 - view3.getWidth() / 2);

    }

    private void downAnimation(View view1, View view2, View view3, float position) {
        int height = ((ViewGroup) view1.getParent()).getHeight();
        float offset = (float) ((position * 2.1) * height);
        float offset2 = (float) ((position * 1.8) * height);
        float offset3 = (float) ((position * 1.5) * height);
        view1.setTranslationY(offset3);
        view2.setTranslationY(offset2);
        view3.setTranslationY(offset);

    }

    @Subscribe
    public void onOTAComplete(OTAComplete complete) {

        otaComplete = true;

        binding.otaTitleText.setText(R.string.update_succesful);

        switch (deviceType.id) {
            case DeviceType.CANARY_AIO:
                binding.otaMessageTextView.setText(R.string.your_canary_is_set_up);
                break;
            case DeviceType.FLEX:
                binding.otaMessageTextView.setText(R.string.your_flex_is_set_up);
                break;
            case DeviceType.CANARY_VIEW:
                binding.otaMessageTextView.setText(R.string.your_canary_view_is_set_up);
                break;
        }

        binding.nextBtn.setEnabled(true);


        int current = binding.pager.getCurrentItem();
        if (current == adapter.getCount() - 1) {
            binding.pager.setVisibility(View.GONE);
        }

        if (!startedClicked) {
            AnimationHelper.fadeViewOut(binding.startTutorial, 250);
            AnimationHelper.fadeViewOut(binding.setupSteps, 250);
            AnimationHelper.fadeViewOut(binding.establishingConnectionTextView, 250);
            AnimationHelper.fadeViewOut(binding.downloadingLatestTextView, 250);
            AnimationHelper.fadeViewOut(binding.canaryModeTextView, 250);
            AnimationHelper.fadeViewOut(binding.deviceArmedImageView, 250);
            binding.customizeImageView.setScaleX(2.0f);
            binding.customizeImageView.setScaleY(2.0f);
            AnimationHelper.fadeViewIn(binding.customizeImageView, 250);
            slideInAndOut(binding.personalizeModes, binding.otaFinishLayout, 1.0f);
            AnimationHelper.fadeViewIn(binding.otaFinishLayout, 250);
            binding.pager.setVisibility(View.GONE);
        }
        APILocationJobService.fetchLocations(getContext());
        startedClicked = true;

    }


    public void setDeviceUri(String deviceUri) {
        device = DeviceDatabaseService.getDeviceFromResourceUri(deviceUri);


        deviceType = device.deviceType;
        schudele = LocationDatabaseService.getNightModeForLocation(Utils.getIntFromResourceUri(device.location));
        if (schudele == null) {
            schudele = NightModeSchedule.createNew();
        }

        if (binding != null) {
            binding.modeActionLayout.nightModeStartTime.setText(schudele.localStartTime());
            binding.modeActionLayout.nightModeEndTime.setText(schudele.localEndTime());
        }

        setupText();
    }


    private void transitionNumbers(int current, float positionOffset) {

        if (positionOffset == 1.0f) {
            return;
        }

        if (positionOffset > .50) {
            current++;
        }

        Animation animation = binding.nextStep.getAnimation();
        if (binding.nextStep.getText().toString().equalsIgnoreCase(String.valueOf(current)) || (animation != null && !animation.hasEnded()))
            return;

        boolean older;
        try {
            int old = Integer.parseInt(binding.currentStep.getText().toString());
            older = old > current;
        } catch (Exception e) {
            return;
        }


        int translation = older ? binding.currentStep.getLineHeight() : -binding.currentStep.getLineHeight();
        final String newPage = String.valueOf(current);

        binding.nextStep.setText(newPage);
        binding.nextStep.setTranslationY(-translation);
        binding.nextStep.setAlpha(0.0f);
        binding.nextStep.setVisibility(View.VISIBLE);
        binding.nextStep.animate()
                .setDuration(250)
                .translationY(0)
                .alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        binding.nextStep.setAlpha(0.0f);
                    }
                });


        binding.currentStep.setAlpha(1.0f);
        binding.currentStep.animate()
                .setDuration(250)
                .alpha(0.0f)
                .translationY(translation)
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        binding.currentStep.setTranslationY(0);
                        binding.currentStep.setText(newPage);
                        binding.currentStep.setAlpha(1.0f);

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tutorialType == TutorialType.MODE_TUTORIAL) {
            fragmentStack.enableRightButton(this, false);
            fragmentStack.showRightButton(0);
        }
    }

    @Override
    public void onRightButtonClick() {

    }

}
