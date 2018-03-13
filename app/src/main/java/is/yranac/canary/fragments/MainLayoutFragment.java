package is.yranac.canary.fragments;

import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.adapter.DeviceFragmentPagerAdapter;
import is.yranac.canary.databinding.FragmentMainLayoutBinding;
import is.yranac.canary.databinding.FragmentTimelineOverlayBinding;
import is.yranac.canary.messages.AvatarUpdated;
import is.yranac.canary.messages.BlockViewPagerDrag;
import is.yranac.canary.messages.CurrentLocationUpdated;
import is.yranac.canary.messages.CustomerFragmentFinished;
import is.yranac.canary.messages.DeviceUpdateRequest;
import is.yranac.canary.messages.LocationCurrentModeChanging;
import is.yranac.canary.messages.ResetSecondDeviceTutorial;
import is.yranac.canary.messages.ShowModeSettings;
import is.yranac.canary.messages.ShowModeTray;
import is.yranac.canary.messages.TutorialRequest;
import is.yranac.canary.messages.tutorial.SetUpDeviceViewsForTutorialRequest;
import is.yranac.canary.messages.tutorial.StartSecondDeviceTutorial;
import is.yranac.canary.messages.tutorial.StartTimelineTutorial;
import is.yranac.canary.model.avatar.Avatar;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.mode.ModeCache;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.ui.MainActivity;
import is.yranac.canary.ui.SettingsFragmentStackActivity;
import is.yranac.canary.ui.SetupFragmentStackActivity;
import is.yranac.canary.ui.views.CanaryHorizontalScrollView;
import is.yranac.canary.ui.views.CircleView;
import is.yranac.canary.ui.views.CustomViewPager;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.AnimationHelper;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.ImageUtils;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.TouchTimeUtil;
import is.yranac.canary.util.TutorialUtil;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.cache.location.GetLocation;
import is.yranac.canary.util.cache.location.GotLocationData;

import static is.yranac.canary.ui.SettingsFragmentStackActivity.home_mode_settings;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.night_mode_settings;
import static is.yranac.canary.util.TutorialUtil.TutorialType.SECOND_DEVICE;
import static is.yranac.canary.util.TutorialUtil.TutorialType.TIMELINE;

public class MainLayoutFragment extends Fragment implements ViewPager.OnPageChangeListener {
    private static final String LOG_TAG = "MainLayoutFragment";

    public DeviceFragmentPagerAdapter devicePagerAdapter;

    private ModeCustomerFragment customerFragment;

    private int deviceWidth;
    private Location location;
    private boolean modeIsChanging = false;

    private List<Device> deviceList;

    private FragmentTimelineOverlayBinding tutorialBinding;
    private FragmentMainLayoutBinding binding;

    public void setAlphas(float positionOffset) {
        if (binding == null || location == null)
            return;

        binding.rightHeaderInfo.setAlpha(Math.max(1f - positionOffset, 0f));
        binding.avatarScrollView.setAlpha(1.0f - positionOffset);
        if (location.recentlyUpdated()) {
            binding.avatarScrollView.setVisibility(View.VISIBLE);
        } else {
            binding.avatarScrollView.setVisibility(View.GONE);
        }

        binding.showTimelineBtn.setAlpha(
                Math.max(1.0f - ((positionOffset - 0.4f) / 0.6f), 0f));

        if (positionOffset == 1.0f) {
            binding.showTimelineBtn.setClickable(false);
        } else {
            binding.showTimelineBtn.setClickable(true);
        }
        int offset = binding.getRoot().getHeight() - getPanelHeight();


        binding.headerLayout.setY(offset * positionOffset);
        binding.devicePager.setY((offset * positionOffset) * .5f);

        DeviceFragment fragment = getCurrentDeviceFragment();
        if (fragment != null)
            fragment.updateOverlayViewAlphas(1.0f - positionOffset);

        if (tutorialBinding != null) {
            tutorialBinding.getRoot().setAlpha(1.0f - positionOffset);
        }
    }

    public CustomViewPager getDeviceView() {
        if (binding == null)
            return null;
        return binding.devicePager;
    }

    public DeviceFragment getCurrentDeviceFragment() {
        if (devicePagerAdapter == null)
            return null;
        return devicePagerAdapter.getItem(binding.devicePager.getCurrentItem());
    }

    public void removeTutorial() {
        if (tutorialBinding != null) {
            binding.tutorialView.removeAllViews();
            binding.tutorialView.setVisibility(View.GONE);
            tutorialBinding = null;

            TutorialUtil.finishTutorial(TIMELINE);
        }
    }

    public interface CallbackMethods {
        void onMainLayoutDisplayed(boolean isMainLayoutDisplayed);

        int getCurrentPage();

        void showTimeline();
    }

    protected CallbackMethods callbackMethods;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CallbackMethods) {
            callbackMethods = (CallbackMethods) activity;
        } else {
            throw new IllegalArgumentException("Activity must implement CallbackMethods");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_layout, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recordingTextView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        View recordingTextView = binding.recordingTextView;

                        recordingTextView.setPivotX((recordingTextView.getWidth() / 2.0f));
                        recordingTextView.setPivotY((recordingTextView.getHeight() / 2.0f));
                    }
                });

        Display display = getActivity().getWindowManager()
                .getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        deviceWidth = metrics.widthPixels;
        location = UserUtils.getLastViewedLocation();


        binding.mainContainer.setEnabled(false);
        binding.showTimelineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TouchTimeUtil.dontAllowTouch()) {
                    return;
                }

                if (customerFragment == null) {
                    resetViews();
                    callbackMethods.showTimeline();
                }

            }
        });
        binding.devicePager.addOnPageChangeListener(this);

        binding.locationNameWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callbackMethods.getCurrentPage() == 1)
                    return;

                // mis-clicking prevention, using threshold of 1000 ms
                if (TouchTimeUtil.dontAllowTouch()) {
                    return;
                }

                if (binding.slidingLayout.isPanelExpanded()) {
                    binding.slidingLayout.collapsePanel();
                } else {
                    binding.slidingLayout.expandPanel();
                }
            }
        });

        binding.avatarLocationMode.getRoot().setTag(-1);
        binding.avatarLocationMode.getRoot().setOnClickListener(avatarOnClickListener);

        binding.locationNameWrapper.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(getContext());

                return inflater.inflate(R.layout.location_name_text_view, null);
            }
        });

        LayoutTransition layoutTransition = new LayoutTransition();


        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(binding.bottomRowAnimations, View.SCALE_X, 0.75f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(binding.bottomRowAnimations, View.SCALE_Y, 0.75f, 1f);

        ObjectAnimator scaleOutXAnimator = ObjectAnimator.ofFloat(binding.bottomRowAnimations, View.SCALE_X, 1f, 0.75f);
        ObjectAnimator scaleOutYAnimator = ObjectAnimator.ofFloat(binding.bottomRowAnimations, View.SCALE_Y, 1f, 0.75f);

        ObjectAnimator alphaIn = ObjectAnimator.ofFloat(binding.bottomRowAnimations, View.ALPHA, 0.0f, 1.0f);
        ObjectAnimator alphaOut = ObjectAnimator.ofFloat(binding.bottomRowAnimations, View.ALPHA, 1.0f, 0.0f);
        alphaOut.setupStartValues();
        alphaOut.setupEndValues();


        AnimatorSet setIn = new AnimatorSet();
        setIn.playTogether(scaleXAnimator, scaleYAnimator, alphaIn);

        AnimatorSet setOut = new AnimatorSet();
        setOut.setupStartValues();
        setOut.setupEndValues();
        setOut.playTogether(scaleOutXAnimator, scaleOutYAnimator, alphaOut);

        layoutTransition.setAnimator(LayoutTransition.APPEARING, setIn);
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, setOut);

        layoutTransition.enableTransitionType(LayoutTransition.CHANGE_APPEARING);
        layoutTransition.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);

        binding.bottomRowAnimations.setLayoutTransition(layoutTransition);

        Animation in = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);

        binding.locationNameWrapper.setInAnimation(in);
        binding.locationNameWrapper.setOutAnimation(out);

        binding.avatarScrollView.setScrollListener(avatarScrollListener);

        binding.menuButton.setOnTouchListener(menuButtonOnTouchListener);

        binding.slidingLayout.setPanelSlideListener(
                new SlidingUpPanelLayout.SimplePanelSlideListener() {
                    @Override
                    public void onPanelExpanded(View panel) {
                        super.onPanelExpanded(panel);
                        /** settings screen is showing */
                        callbackMethods.onMainLayoutDisplayed(true);
                        /** release the main screen touch block if not in the tutorial */
                        binding.devicePager.setPagingEnabled(true);

                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    }

                    @Override
                    public void onPanelCollapsed(View panel) {
                        super.onPanelCollapsed(panel);
                        /** device pager screen is showing */
                        callbackMethods.onMainLayoutDisplayed(false);
                        /** release the main screen touch block if not in the tutorial */
                        binding.devicePager.setPagingEnabled(false);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                    }

                    @Override
                    public void onPanelSlide(View panel, float slideOffset) {
                        super.onPanelSlide(panel, slideOffset);
                        /** device pager screen is partially showing */
                        binding.rightHeaderInfo.setAlpha(slideOffset);
                        callbackMethods.onMainLayoutDisplayed(false);

                    }
                });

    }

    private void resetViews() {
        AnimationHelper.fadeViewIn(binding.headerLayout, 400);
        AnimationHelper.fadeViewIn(binding.avatarScrollView, 400);
        AnimationHelper.fadeViewIn(binding.showTimelineBtn, 400);
    }

    @Override
    public void onResume() {
        super.onResume();
        TinyMessageBus.post(new TutorialRequest());
        TinyMessageBus.post(new GetLocation(UserUtils.getLastViewedLocationId()));
    }

    @Subscribe
    public void gotLocationData(GotLocationData gotLocationData) {
        if (gotLocationData.location.id == UserUtils.getLastViewedLocationId()) {
            refreshData(gotLocationData);
        }
    }

    @Subscribe
    public void startTimelineTutorial(StartTimelineTutorial timelineTutorial) {
        binding.tutorialView.removeAllViews();
        tutorialBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_timeline_overlay, binding.tutorialView, true);
        tutorialBinding.setTutorialType(TIMELINE);

        Device device = deviceList.get(binding.devicePager.getCurrentItem());
        ImageLoader imageLoader = ImageLoader.getInstance();

        if (device != null)
            imageLoader.displayImage(device.imageUrl(), tutorialBinding.tutorialImage);

        binding.tutorialView.setVisibility(View.VISIBLE);
        AnimationHelper.fadeViewInAfterDelay(binding.tutorialView, 500, 750);
        int dp5 = DensityUtil.dip2px(getActivity(), 5);
        AnimationHelper.startPulsing(tutorialBinding.timelineTutorialArrow1, false, dp5, 400);
    }

    @Subscribe
    public void startSecondDeviceTutorial(StartSecondDeviceTutorial deviceTutorial) {
        fadeViewsOut();
        AnimationHelper.fadeViewOut(binding.showTimelineBtn, 400);
    }


    @Subscribe
    public void resetSecondDeviceTutorial(ResetSecondDeviceTutorial event) {
        resetViews();
        TutorialUtil.finishTutorial(SECOND_DEVICE);
    }

    private void fadeViewsOut() {
        AnimationHelper.fadeViewOut(binding.headerLayout, 400);
        AnimationHelper.fadeViewOut(binding.avatarScrollView, 400);
    }


    @Override
    public void onPause() {
        super.onPause();
        modeIsChanging = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        TinyMessageBus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        TinyMessageBus.unregister(this);
    }


    public void refreshData(GotLocationData locationData) {

        modeIsChanging = false;


        location = locationData.location;

        Subscription subscription = locationData.subscription;

        setUpAvatarScrollView(locationData.customers);
        setupDeviceViewPager(locationData.deviceList);


        if (location != null) {
            binding.locationNameWrapper.setText(location.name);
        }


        binding.menuButton.setInPreview(subscription.onTrial);
        if (subscription.onTrial) {
            binding.previewDurationTextView.setVisibility(View.VISIBLE);

            String daysLeftText;
            if (subscription.remainingDays() <= 0) {
                daysLeftText = getString(R.string.last_day);
            } else {
                daysLeftText = getString(R.string.twelve_days_left, subscription.remainingDays());
            }
            binding.previewDurationTextView.setText(daysLeftText);
        } else {
            binding.previewDurationTextView.setVisibility(View.GONE);
        }


        onCurrentLocationUpdated(null);
    }

    @Subscribe
    public void onCurrentLocationUpdated(CurrentLocationUpdated currentLocationUpdated) {
        location = UserUtils.getLastViewedLocation();
        if (location == null)
            return;
        if (MainActivity.currentVerticalViewPage == MainActivity.DEVICE_STATUS_PAGE) {
            if (binding.avatarScrollView.getVisibility() != View.VISIBLE && location.recentlyUpdated()
                    && !TutorialUtil.isTutorialInProgress()) {
                AnimationHelper.fadeViewIn(binding.avatarScrollView, 400);
                binding.avatarScrollView.setScrollEnabled(true);
            }
        }
    }

    private void setupDeviceViewPager(List<Device> devices) {
        int locationId = UserUtils.getLastViewedLocationId();
        deviceList = devices;

        if (deviceList.size() == 0) {
            Intent i = new Intent(getActivity(), SetupFragmentStackActivity.class);
            i.putExtra(SetupFragmentStackActivity.LOCATION_ID, locationId);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return;
        }

        // Compare the new device list to the old one to see if we need to change it
        boolean listIsUnchanged = true;
        if (devicePagerAdapter == null || devicePagerAdapter.getCount() != deviceList.size()) {
            listIsUnchanged = false;
        } else {
            for (Device device : deviceList) {
                if (!devicePagerAdapter.containsDevice(device.serialNumber)) {
                    listIsUnchanged = false;
                }
            }
        }

        if (listIsUnchanged) {
            TinyMessageBus.post(new DeviceUpdateRequest());
        } else {
            devicePagerAdapter = new DeviceFragmentPagerAdapter(getChildFragmentManager(), deviceList);
            binding.devicePager.setAdapter(devicePagerAdapter);
        }

        onPageSelected(binding.devicePager.getCurrentItem());

    }

    // method so that MainActivity can turn off the device viewpager when the device page is minimized

    private boolean doNotOverride = false;

    public void setDeviceViewPagerEnabled(boolean isEnabled) {
        if (binding != null && !doNotOverride) {
            binding.devicePager.setPagingEnabled(isEnabled);
        }
    }


    private void setUpAvatarScrollView(final List<Customer> customers) {
        // First create the Location Mode Change Avatar and add it to the CanaryHorizontalScrollView
        if (binding == null)
            return;


        // Compare the new customer list to the old one to see if we need to change it
        boolean listIsUnchanged = true;

        if (customers.size() != binding.avatarListContainer.getChildCount() - (!PreferencesUtils.hasShowAddMember() ? 1 : 0)) {
            listIsUnchanged = false;
        } else {
            for (Customer customer : customers) {
                if (binding.avatarListContainer.findViewWithTag(customer.id) == null) {
                    listIsUnchanged = false;
                }
            }
        }

        if (listIsUnchanged) {
            // no users have been added or removed....but we need to update the status of the current location and
            // potentially the avatar images
            for (Customer customer : customers) {
                View customerAvatarView = binding.avatarListContainer.findViewWithTag(customer.id);
                updateAvatarView(customerAvatarView, customer);
            }

            updateModeIcon();
            return;
        }
        binding.avatarListContainer.removeAllViews();
        binding.avatarListContainer.invalidate();

        // Add the mode select avatar
        updateModeIcon();


        if (customers.size() == 0) {
            return;
        }

        for (Customer customer : customers) {

            if (customer != null) {
                View customerAvatarView = createAvatarView(customer);
                customerAvatarView.setTag(customer.id);

                customerAvatarView.setOnClickListener(avatarOnClickListener);
                binding.avatarListContainer.addView(customerAvatarView);
            }
        }

        if (!PreferencesUtils.hasShowAddMember()) {
            final ImageView imageView = new ImageView(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(getActivity(), 60), DensityUtil.dip2px(getActivity(), 60));
            layoutParams.setMargins(DensityUtil.dip2px(getActivity(), 9), DensityUtil.dip2px(getActivity(), 19), DensityUtil.dip2px(getActivity(), 9), 0);
            imageView.setLayoutParams(layoutParams);

            /**
             * Fix for CA-1538: Can slide into global options while avatar popups are open
             */
            imageView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            MainActivity baseActivity = (MainActivity) getActivity();

                            AlertUtils.showMemberDialog(baseActivity, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!Utils.isDemo()) {
                                        binding.avatarListContainer.removeView(imageView);
                                        PreferencesUtils.setShownMember();
                                    }
                                }
                            });

                        }
                    });

            imageView.setTag(-2);

            imageView.setImageResource(R.drawable.user_add_icon_float);

            binding.avatarListContainer.addView(imageView);
        }

        if (customerFragment != null) {
            setAlphasOnAvatars(customerFragment.getCustomerId(), false);
        }

    }


    private void updateModeIcon() {
        setLocationModeImage(binding.avatarLocationMode.locationMode, binding.avatarLocationMode.modeBackgroundColor, location);
    }

    private void setLocationModeImage(ImageView locationModeAvatar, CircleView background, Location location) {

        if (location == null || location.currentMode == null)
            return;

        String currentModeName = location.currentMode.name;
        int color;
        int drawable;
        int paddingBottom;

        if (location.isPrivate) {

            drawable = R.drawable.mode_privacy_icon;
            color = R.color.black_fifty;
            paddingBottom = DensityUtil.dip2px(getContext(), 3);
        } else {
            switch (currentModeName) {
                case ModeCache.away:
                    color = R.color.gray;
                    drawable = R.drawable.mode_away_icon;
                    paddingBottom = 0;
                    break;

                case ModeCache.home:
                    color = R.color.gray;
                    drawable = R.drawable.mode_home_icon;
                    paddingBottom = DensityUtil.dip2px(getContext(), 3);
                    break;

                case ModeCache.night:
                    color = R.color.gray;
                    drawable = R.drawable.mode_night_icon;
                    paddingBottom = DensityUtil.dip2px(getContext(), 3);
                    break;

                default:
                    return;
            }

        }

        background.setBackgroundColor(getResources().getColor(color));
        locationModeAvatar.setImageResource(drawable);
        locationModeAvatar.setPadding(0, 0, 0, paddingBottom);

    }

    private View createAvatarView(Customer customer) {
        View customerAvatar = LayoutInflater.from(getContext()).inflate(R.layout.avatar_customer, null, false);
        return updateAvatarView(customerAvatar, customer);
    }


    private View updateAvatarView(View customerAvatar, Customer customer) {
        Avatar avatar = customer.avatar;

        View circle = customerAvatar.findViewById(R.id.gray_circle);
        ImageView avatarImageView = (ImageView) customerAvatar.findViewById(R.id.avatar_image_layout);
        ((TextView) customerAvatar.findViewById(R.id.customer_initials)).setText(customer.getInitials());
        if (avatar != null) {
            ImageUtils.loadAvatar(avatarImageView, avatar.thumbnailUrl());
            avatarImageView.setVisibility(View.VISIBLE);
        } else {
            // initialize the badge to the customer initials if there is no image
            avatarImageView.setVisibility(View.GONE);
            circle.setVisibility(View.VISIBLE);
        }

        customerAvatar.findViewById(R.id.home_flag_small)
                .setVisibility(location.resourceUri.equals(customer.currentLocation) ? View.VISIBLE : View.GONE);

        return customerAvatar;
    }

    public void refreshDeviceWatchLiveHomehealthStatus() {
        if (devicePagerAdapter == null)
            return;
        int numDevices = devicePagerAdapter.getCount();

        for (int i = 0; i < numDevices; i++) {
            devicePagerAdapter.getItem(i).setOverlayViewAlphas();
        }
    }

    /**
     * Fix for CA-1538: Can slide into global options while avatar popups are open
     */
    View.OnClickListener avatarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            // mis-clicking prevention, using threshold of 250 ms
            if (TouchTimeUtil.dontAllowTouch()) {
                return;
            }

            handleAvatarSelection(v);
        }
    };

    private void handleAvatarSelection(final View v) {

        // don't accept clicks if waiting for mode to change
        if (modeIsChanging) {
            return;
        }
        /**
         * get the x position of the new triangle: we will either position a new
         * triangle over it or translate the existing triangle to it
         */
        int[] vlocation = new int[2];
        v.getLocationOnScreen(vlocation);
        final float endX = vlocation[0] +
                v.findViewById(R.id.triangle)
                        .getX() +
                (DensityUtil.dip2px(getActivity(), 6f));

        // currentAvatar is selected again: close the customer fragment
        if (customerFragment != null) {


            if (Integer.parseInt(v.getTag().toString()) == customerFragment.getCustomerId()) {
                setAlphasOnAvatars(0, true);
                AnimationHelper.fadeViewOut(binding.blackOverlayView, 250);
                getChildFragmentManager().beginTransaction()
                        .setCustomAnimations(0, R.anim.fade_out)
                        .remove(customerFragment)
                        .commit();
                onCustomerFragmentFinished(new CustomerFragmentFinished());
                return;
            }
        }

        showCustomerFragment((int) v.getTag(), endX);
    }

    private void setAlphasOnAvatars(int currentId, boolean animated) {
        AnimationHelper.animateAvatars(binding.avatarListContainer, currentId, animated);
    }

    public void fadeBlackOverlayOut() {
        AnimationHelper.fadeViewOut(binding.blackOverlayView, 250);
        setAlphasOnAvatars(0, true);
    }

    private void showCustomerFragment(int customerId, float endX) {
        TinyMessageBus
                .post(new BlockViewPagerDrag(true));

        FragmentManager fragmentManager = getChildFragmentManager();
        customerFragment = (ModeCustomerFragment) fragmentManager.findFragmentById(R.id.avatar_container);
        setAlphasOnAvatars(customerId, true);

        if (customerFragment != null) {
            customerFragment.showTriangleView();
            customerFragment.translateTriangleViewX(endX);
            customerFragment.setCustomer(customerId);
        } else {
            AnimationHelper.fadeViewIn(binding.blackOverlayView, 250);

            binding.avatarContainer
                    .setVisibility(View.VISIBLE);
            customerFragment = ModeCustomerFragment.newInstance(customerId, endX);
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.avatar_container, customerFragment, "CustomerFragment")
                    .commit();
        }
    }

    CanaryHorizontalScrollView.ScrollListener avatarScrollListener = new CanaryHorizontalScrollView.ScrollListener() {

        private View currentAvatarView = null;

        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
            if (customerFragment != null && currentAvatarView != null) {
                int[] location = new int[2];
                currentAvatarView.getLocationOnScreen(location);

                if (location[0] < 0 || location[0] > deviceWidth - 120) {
                    if (customerFragment != null) {
                        currentAvatarView.findViewById(R.id.triangle)
                                .setVisibility(View.INVISIBLE);
                        customerFragment.animateUpAndBail();
                    }
                }
            }
        }

        @Override
        public void onScrollStart() {
            if (customerFragment != null) {
                customerFragment.hideTriangleView();

                int tag = customerFragment.getCustomerId();

                if (tag == -1) {
                    binding.avatarLocationMode.triangle.setVisibility(View.VISIBLE);
                    currentAvatarView = binding.avatarLocationMode.getRoot();
                } else {
                    currentAvatarView = binding.avatarListContainer.
                            findViewWithTag(tag);
                    currentAvatarView.findViewById(R.id.triangle)
                            .setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onScrollEnd() {
            if (customerFragment != null) {
                int tag = customerFragment.getCustomerId();

                if (tag == -1) {
                    binding.avatarLocationMode.triangle.setVisibility(View.VISIBLE);
                } else {
                    currentAvatarView = binding.avatarListContainer.
                            findViewWithTag(tag);
                    currentAvatarView.findViewById(R.id.triangle)
                            .setVisibility(View.VISIBLE);
                }
                currentAvatarView.findViewById(R.id.triangle)
                        .setVisibility(View.INVISIBLE);
                currentAvatarView.performClick();
                currentAvatarView = null;
            }
        }
    };


    @Subscribe
    public void onLocationCurrentStateChanging(LocationCurrentModeChanging message) {
        if (binding == null)
            return;


        ImageView currentModeImageView = binding.avatarLocationMode.locationMode;
        currentModeImageView.clearAnimation();
        if (message.isChanging) {
            modeIsChanging = true;
            binding.avatarScrollView.setScrollEnabled(false);
            startPulsing(currentModeImageView, message.mode);
        } else {
            modeIsChanging = false;
            if (location != null) {
                location.currentMode = ModeCache.getMode(message.mode);
            }
            updateModeIcon();
            binding.avatarScrollView.setScrollEnabled(true);
            TinyMessageBus.post(new GetLocation(UserUtils.getLastViewedLocationId()));
        }
    }

    private void startPulsing(final ImageView firstImage, String mode) {
        AnimationHelper.pulseModeView(getActivity(), firstImage, mode, new AnimationHelper.ModeChanging() {
            @Override
            public boolean modeChanging() {
                return modeIsChanging;
            }
        });
    }


    private View.OnTouchListener menuButtonOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // mis-clicking prevention, using threshold of 1000 ms
                if (!binding.slidingLayout.isPanelExpanded()) {
                    binding.slidingLayout.expandPanel();
                } else {
                    binding.slidingLayout.collapsePanel();
                }
                return true;
            }
            return false;
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Device currentDevice = deviceList.get(position);

        PreferencesUtils.setUserSwipedBetweenDevices();

        if (currentDevice.hasRecordingDisabled()) {
            if (binding.recordingTextView.getVisibility() != View.VISIBLE) {
                binding.recordingTextView.setAlpha(0.0f);
                binding.recordingTextView.setVisibility(View.VISIBLE);

            }
        } else {
            binding.recordingTextView.setVisibility(View.GONE);

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (TutorialUtil.getTutorialInProgress() == SECOND_DEVICE) {
                TinyMessageBus.post(new ResetSecondDeviceTutorial());
                PreferencesUtils.setUserSwipedBetweenDevices();
            }
            TinyMessageBus.post(new TutorialRequest());
        }

    }

    @Subscribe
    public void onCustomerFragmentFinished(CustomerFragmentFinished message) {

        if (customerFragment != null) {
            customerFragment = null;
        }
        TinyMessageBus
                .post(new BlockViewPagerDrag(false));
    }

    @Subscribe
    public void onAvatarUpdated(AvatarUpdated message) {
        LinearLayout avatarContainer = binding.avatarListContainer;
        View customerAvatarView = avatarContainer.findViewWithTag(message.customer.id);
        updateAvatarView(customerAvatarView, message.customer);
    }

    @Subscribe
    public void showDeviceAtIndex(SetUpDeviceViewsForTutorialRequest request) {
        int position = request.getPositionToSelect();
        if (position < 0 || binding.devicePager == null || position > binding.devicePager.getChildCount() - 1)
            return;

        binding.devicePager.setCurrentItem(position);
    }

    public boolean isPanelExpanded() {
        return binding == null || binding.slidingLayout.isPanelExpanded();
    }


    @Subscribe
    public void onShowModeSettings(ShowModeSettings showModeSettings) {

        if (location == null || location.currentMode == null)
            return;

        Intent i = new Intent(getActivity(), SettingsFragmentStackActivity.class);
        if (ModeCache.night.equalsIgnoreCase(location.currentMode.name)) {
            i.setAction(night_mode_settings);
        } else {
            i.setAction(home_mode_settings);
        }
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.scale_alpha_push);
    }

    @Subscribe
    public void onShowModeTray(ShowModeTray showModeTray) {
        binding.avatarLocationMode.getRoot().performClick();
    }

    public void expandPanel() {
        binding.slidingLayout.expandPanel();
    }

    public void collapsePanel() {
        binding.slidingLayout.collapsePanel();
    }

    public void setSlidingEnabled(boolean enabled) {
        if (binding == null) {
            return;
        }
        if (!doNotOverride) {
            binding.slidingLayout.setSlidingEnabled(enabled);
        }
    }

    public void setSlidingEnabledWithOverride(boolean enabled) {
        if (binding == null) {
            return;
        }
        binding.slidingLayout.setSlidingEnabled(enabled);
        doNotOverride = true;
    }

    public int getPanelHeight() {
        return binding.slidingLayout.getPanelHeight();
    }

}