package is.yranac.canary.fragments.tutorials;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.contentproviders.CanaryDeviceContentProvider;
import is.yranac.canary.databinding.FragmentTutorialBinding;
import is.yranac.canary.fragments.HomeHealthDataFragment;
import is.yranac.canary.fragments.ModeCustomerFragment;
import is.yranac.canary.messages.BlockViewPagerDrag;
import is.yranac.canary.messages.CustomerFragmentFinished;
import is.yranac.canary.messages.GetDeviceStatistics;
import is.yranac.canary.messages.GotDeviceStatistics;
import is.yranac.canary.messages.OnBackPressed;
import is.yranac.canary.messages.OpenSettings;
import is.yranac.canary.messages.PulsingDelay;
import is.yranac.canary.messages.ShowModalFragment;
import is.yranac.canary.messages.TutorialNextStep;
import is.yranac.canary.messages.TutorialRequest;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceSettings;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.mode.Mode;
import is.yranac.canary.model.mode.ModeCache;
import is.yranac.canary.model.reading.Reading;
import is.yranac.canary.model.tutorial.TutorialDetails;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.ReadingDatabaseService;
import is.yranac.canary.ui.MainActivity;
import is.yranac.canary.ui.WatchLiveActivity;
import is.yranac.canary.ui.views.CircleView;
import is.yranac.canary.util.AnimationHelper;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.JSONUtil;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.TouchTimeUtil;
import is.yranac.canary.util.TutorialUtil;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.util.TutorialUtil.TutorialType.ENTRY_MORE_OPTIONS;
import static is.yranac.canary.util.TutorialUtil.TutorialType.HOME;
import static is.yranac.canary.util.TutorialUtil.TutorialType.HOME_FOR_SECOND_DEVICE;
import static is.yranac.canary.util.TutorialUtil.TutorialType.SECOND_DEVICE;
import static is.yranac.canary.util.TutorialUtil.TutorialType.TIMELINE;
import static is.yranac.canary.util.TutorialUtil.TutorialType.TIMELINE_FILTER;

/**
 * Created by Schroeder on 12/14/15.
 */
public class HomeTutorialOverlayFragment extends Fragment {

    private static final String LOG_TAG = "HomeTutorialOverlayFragment";
    private static final String flexTutorialKey = "flexTutorialKey";

    private Mode locationMode;


    private Location location;
    private Device device;

    private FragmentTutorialBinding binding;

    private char degreeSymbol = '\u00B0';
    private char percentSymbol = '\u0025';

    public static HomeTutorialOverlayFragment newInstance(TutorialDetails tutorial) {
        Bundle args = new Bundle();
        args.putString(flexTutorialKey, JSONUtil.getJSONString(tutorial));

        HomeTutorialOverlayFragment fragment = new HomeTutorialOverlayFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tutorial, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Device> deviceList = DeviceDatabaseService.getActivatedDevicesAtLocation(UserUtils.getLastViewedLocationId());
        if (deviceList.isEmpty()) {
            closeFragment();
            return;
        }
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null)
            return;


        int position = activity.getDevicePagerPosition();

        //Some inconsistency between UI and data - lets not crash
        if (position >= deviceList.size())
            closeFragment();

        device = deviceList.get(position);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        binding.skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TutorialUtil.finishTutorials(new TutorialUtil.TutorialType[]{
                        HOME,
                        TIMELINE,
                        TIMELINE_FILTER,
                        ENTRY_MORE_OPTIONS,
                        SECOND_DEVICE,
                        HOME_FOR_SECOND_DEVICE
                });

                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().beginTransaction().remove(HomeTutorialOverlayFragment.this).
                        setCustomAnimations(0, R.anim.fade_out_7).
                        commit();
                TinyMessageBus.post(new BlockViewPagerDrag(false));
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        });

        binding.deviceTutorialLayout.deviceOffineTextView.setVisibility(View.GONE);
        binding.deviceTutorialLayout.devicePrivacyTextView.setVisibility(View.GONE);
        binding.deviceTutorialLayout.spotlightMessage.setVisibility(View.GONE);

        if (device.getDeviceType() == DeviceType.FLEX) {
            binding.deviceTutorialLayout.sensorDataLayout.getRoot().setVisibility(View.GONE);
            binding.deviceTutorialLayout.statisticsFlexContainer.getRoot().setVisibility(View.INVISIBLE);
        } else if (device.getDeviceType() == DeviceType.CANARY_AIO) {
            binding.deviceTutorialLayout.statisticsFlexContainer.getRoot().setVisibility(View.GONE);
            binding.deviceTutorialLayout.sensorDataLayout.getRoot().setVisibility(View.INVISIBLE);
        } else {
            binding.deviceTutorialLayout.sensorContainer.setVisibility(View.GONE);
        }

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(device.imageUrl(), binding.tutorialImage);

        Reading batteryReading = ReadingDatabaseService.getLatestReadingForDevice(device.resourceUri, Reading.READING_BATTERY);
        if (batteryReading == null) {
            if (device.isOnline) {
                batteryReading = new Reading();
                batteryReading.status = "not_chrg";
                batteryReading.value = 100;
            } else
                batteryReading = Reading.getReadingForOfflineDevice(Reading.READING_BATTERY);
        }


        Reading wifiReading = ReadingDatabaseService.getLatestReadingForDevice(device.resourceUri, Reading.READING_WIFI);
        if (wifiReading == null) {
            if (device.isOnline) {
                wifiReading = new Reading();
                wifiReading.status = "10";
            } else
                wifiReading = Reading.getReadingForOfflineDevice(Reading.READING_WIFI);
        }

        binding.deviceTutorialLayout.statisticsFlexContainer.statisticsWifiStatus.setText(wifiReading.getWifiLevelLabel(getContext(), true));
        binding.deviceTutorialLayout.statisticsFlexContainer.statisticsWifiView.setWifiConnectionLevel(wifiReading);

        DeviceSettings deviceSettings = DeviceDatabaseService.getDeviceSettingsForDevice(device.id);
        if (deviceSettings == null) {
            DeviceSettings fake = new DeviceSettings();
            fake.useBatterySaver = false;
            fake.tempBatterySaverUse = false;

        }
        binding.deviceTutorialLayout.statisticsFlexContainer.statisticsBatteryStatus.setText(batteryReading.getBatteryStatus());
        binding.deviceTutorialLayout.statisticsFlexContainer.statisticsBatteryView.updateBatteryState(batteryReading, device);

        binding.okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.okayButton.setOnClickListener(null);
                if (getTutorialDetails().getTutorialType() == HOME_FOR_SECOND_DEVICE) {
                    TutorialUtil.finishTutorial(HOME_FOR_SECOND_DEVICE);
                    closeFragmentQuietly();
                } else if (getTutorialDetails().getTutorialType() == HOME) {
                    //checking to see if we need a second device tutorial
                    if (getTutorialDetails().totalNumOfActivatedDvices > 1
                            && !PreferencesUtils.getUserSwipedBetweenDevices()) {
                        pauseHomeTutorialForSecondDeviceTutorial();
                    } else {
                        TutorialUtil.setCompletedTutorialStep(HOME, 2);
                        TinyMessageBus.post(new OnBackPressed());
                    }
                }
            }
        });


        location = UserUtils.getLastViewedLocation();
        locationMode = location.currentMode;

        binding.continueButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TutorialUtil.setCompletedTutorialStep(HOME, 0);

                AnimationHelper.fadeViewOut(view, 400);
                TinyMessageBus.postDelayed(new TutorialNextStep(), 400);

                if (location.isPrivate || !device.isOnline || device.isPrivate()) {
                    TutorialUtil.setCompletedTutorialStep(HOME, 1);
                    if (needViewTutorial()) {
                        TutorialUtil.setCompletedTutorialStep(HOME, 2);
                    }
                }
            }
        });

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                setUpViewPositions();
            }
        });

        binding.deviceTutorialLayout.watchLiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), WatchLiveActivity.class);
                i.putExtra(CanaryDeviceContentProvider.COLUMN_UUID, device.uuid);
                i.putExtra(CanaryDeviceContentProvider.COLUMN_LOCATION_ID, device.location);
                getActivity().startActivity(i);
                TutorialUtil.setCompletedTutorialStep(HOME, 1);
                if (needViewTutorial()) {
                    TutorialUtil.setCompletedTutorialStep(HOME, 2);
                }
            }
        });

        binding.deviceTutorialLayout.watchLiveButton.setVisibility(View.INVISIBLE);

        binding.deviceTutorialLayout.sensorDataLayout.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeHealthDataFragment fragmentModeInfo = HomeHealthDataFragment.newInstance(device.resourceUri, device.name);
                TinyMessageBus
                        .post(new ShowModalFragment(fragmentModeInfo));
                TutorialUtil.setCompletedTutorialStep(HOME, 2);
                AnimationHelper.viewInvisblieAfterDelay(view, 1000);
            }
        });

        binding.menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.menuLayout.setOnClickListener(null);
                TutorialUtil.setCompletedTutorialStep(HOME, 4);
                TutorialUtil.finishTutorial(HOME);
                TutorialUtil.resetTutorialDeviceIndex();
                TinyMessageBus.post(new BlockViewPagerDrag(false));

                AnimationHelper.fadeViewOut(view, 500);
                TinyMessageBus.postDelayed(new OpenSettings(), 500);
                TinyMessageBus.postDelayed(new OnBackPressed(), 500);
            }
        });
        View.OnClickListener avatarOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // mis-clicking prevention, using threshold of 250 ms
                if (TouchTimeUtil.dontAllowTouch()) {
                    return;
                }
                TutorialUtil.setCompletedTutorialStep(HOME, 3);
                showCustomerFragment(v);
            }
        };

        ImageView modeIcon = binding.tutorialModeLayout.locationMode;
        CircleView modeBackGround = binding.tutorialModeLayout.modeBackgroundColor;
        setLocationModeImage(modeIcon, modeBackGround, UserUtils.getLastViewedLocation());
        binding.tutorialModeLayout.getRoot().setOnClickListener(avatarOnClickListener);
        if (TutorialUtil.getTutorialStep(HOME) > 0) {
            view.setVisibility(View.GONE);
            if (TutorialUtil.getTutorialStep(HOME) == 1
                    && (location.isPrivate || !device.isOnline || device.isPrivate())) {
                TutorialUtil.setCompletedTutorialStep(HOME, 1);
                if (needViewTutorial()) {
                    TutorialUtil.setCompletedTutorialStep(HOME, 2);
                }

            }
            TinyMessageBus.postDelayed(new TutorialNextStep(), 500);
        }
    }

    private void closeFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(HomeTutorialOverlayFragment.this).
                setCustomAnimations(0, 0).
                commit();
    }

    private void setUpViewPositions() {

        int dp55 = DensityUtil.dip2px(getActivity(), 55);
        int dp20 = DensityUtil.dip2px(getActivity(), 20);

        TextView watchLiveTextView = binding.watchLiveDscTextView;
        watchLiveTextView.setY(Utils.getRelativeTop(binding.deviceTutorialLayout.watchLiveButton)
                - (dp55 + watchLiveTextView.getHeight()));

        TextView sensorTextView = binding.sensorDscTextView;
        sensorTextView.setY(Utils.getRelativeTop(binding.deviceTutorialLayout.sensorDataLayout.getRoot())
                - (sensorTextView.getHeight() + dp55));

        TextView changeModeTextView = binding.modeChangeDscTextView;
        changeModeTextView.setY(Utils.getRelativeTop(binding.tutorialModeLayout.getRoot())
                - (changeModeTextView.getHeight() + dp55));
        TextView menuBtnDscTextView = binding.menuDscTextView;
        menuBtnDscTextView.setY(Utils.getRelativeTop(binding.menuButton)
                + binding.menuButton.getHeight() + dp55);

        TextView flexDscTextView = binding.flexTutorialDscTextView;
        flexDscTextView.setY(Utils.getRelativeTop(binding.deviceTutorialLayout.statisticsFlexContainer.getRoot())
                - (flexDscTextView.getHeight() + dp55));

        float arrow1Y = Utils.getRelativeTop(binding.deviceTutorialLayout.watchLiveButton) -
                (binding.arrowOne.getHeight() + dp20);
        binding.arrowOne.setY(arrow1Y);

        float arrow2Y = Utils.getRelativeTop(binding.deviceTutorialLayout.sensorDataLayout.getRoot())
                - (binding.arrowTwo.getHeight() + dp20);
        binding.arrowTwo.setY(arrow2Y);

        int modeTop = Utils.getRelativeTop(binding.tutorialModeLayout.getRoot());


        float arrow3Y = modeTop - (binding.arrowThree.getHeight() + dp20);

        binding.arrowThree.setY(arrow3Y);


    }

    @Subscribe
    public void onBackPressed(OnBackPressed onBackPressed) {

        if (!TutorialUtil.isTutorialInProgress()) {
            getActivity().getSupportFragmentManager().popBackStack();
            return;
        }
        if (TutorialUtil.getTutorialInProgress() == HOME) {
            TinyMessageBus.postDelayed(new TutorialNextStep(), 400);
            AnimationHelper.fadeViewOut(binding.getRoot(), 400);
        }
    }

    private void showCustomerFragment(View v) {
        final FragmentManager fragmentManager = getChildFragmentManager();

        if (fragmentManager.findFragmentByTag("CustomerFragment") != null) {
            fragmentManager.popBackStack();
            TutorialUtil.setCompletedTutorialStep(HOME, 3);
            fadeBlackOverlayOut();
            return;
        }
        int[] vlocation = new int[2];
        v.getLocationOnScreen(vlocation);
        final float endX = vlocation[0] +
                v.findViewById(R.id.triangle)
                        .getX() +
                (DensityUtil.dip2px(getActivity(), 6f));

        binding.avatarContainer.setVisibility(View.VISIBLE);


        ModeCustomerFragment customerFragment = ModeCustomerFragment.newInstance(-1, endX);
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out)
                .add(R.id.avatar_container, customerFragment, "CustomerFragment")
                .addToBackStack(null)
                .commit();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                fragmentManager.removeOnBackStackChangedListener(this);

                binding.menuButton.setVisibility(View.VISIBLE);
            }
        });
    }


    @Subscribe
    public void onCustomerFragmentFinished(CustomerFragmentFinished customerFragmentFinished) {
        fadeBlackOverlayOut();
    }

    private void setLocationModeImage(ImageView locationModeAvatar, CircleView background, Location location) {


        if (location.currentMode == null)
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

    private void updateHomeHealthValuesDefault() {

        DecimalFormat format = new DecimalFormat("#,##0");
        format.setRoundingMode(RoundingMode.HALF_UP);
        String temperature = format.format(UserUtils.getTemperatureInPreferredUnits(23.33f));
        temperature += '°';
        Customer customer = CurrentCustomer.getCurrentCustomer();
        if (customer != null && customer.celsius) {
            temperature += "C";
        } else {
            temperature += "F";

        }

        binding.deviceTutorialLayout.sensorDataLayout.deviceTempTextview.setText(temperature);

    }

    @Subscribe
    public void gotDeviceStatistics(GotDeviceStatistics gotDeviceStatistics) {

        if (device == null || gotDeviceStatistics.device == null)
            return;

        if (gotDeviceStatistics.device.id != device.id)
            return;

        updateHomeHealthValues(
                gotDeviceStatistics.readingTemp,
                gotDeviceStatistics.readingAirQuality,
                gotDeviceStatistics.readingHumidity);
    }

    private void updateHomeHealthValues(Reading temperatureReading,
                                        Reading airReading, Reading humidityReading) {

        if (device == null)
            return;

        if (!device.isOnline ||
                (temperatureReading == null || airReading == null || humidityReading == null)) {
            updateHomeHealthValuesDefault();
            return;
        }

        DecimalFormat format = new DecimalFormat("#,##0");
        format.setRoundingMode(RoundingMode.HALF_UP);
        String temperature = format.format(UserUtils.getTemperatureInPreferredUnits(temperatureReading.value));
        temperature += degreeSymbol;
        Customer customer = CurrentCustomer.getCurrentCustomer();
        if (customer != null && customer.celsius) {
            temperature += "C";
        } else {
            temperature += "F";

        }

        String humidity = format.format(humidityReading.value);
        humidity += percentSymbol;


        binding.deviceTutorialLayout.sensorDataLayout.deviceTempTextview.setText(temperature);
        binding.deviceTutorialLayout.sensorDataLayout.deviceHumidityTextview.setText(humidity);
        binding.deviceTutorialLayout.sensorDataLayout.deviceAirQualityOfflineIcon.setVisibility(View.GONE);
        binding.deviceTutorialLayout.sensorDataLayout.deviceAirQualityIcon.setVisibility(View.VISIBLE);

        if (airReading.value > 0.6f) {
            binding.deviceTutorialLayout.sensorDataLayout.deviceAirQualityIcon.setImageResource(R.drawable.aq_float_icon_normal);
        } else if (airReading.value > 0.4f) {
            binding.deviceTutorialLayout.sensorDataLayout.deviceAirQualityIcon.setImageResource(R.drawable.aq_float_icon_abnormal);
        } else {
            binding.deviceTutorialLayout.sensorDataLayout.deviceAirQualityIcon.setImageResource(R.drawable.aq_float_icon_very_abnormal);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        TinyMessageBus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        TinyMessageBus.register(this);
        startTutorial(TutorialUtil.getTutorialStep(HOME));
        TinyMessageBus.post(new GetDeviceStatistics(device));
    }

    protected void startTutorial(int step) {

        if (step < 0)
            return;

        binding.arrowOne.clearAnimation();
        binding.arrowTwo.clearAnimation();
        binding.arrowThree.clearAnimation();
        binding.arrowFour.clearAnimation();

        binding.setupOneLayout.setVisibility(View.GONE);
        binding.tutorialLayout.setVisibility(View.VISIBLE);

        binding.arrowOne.setVisibility(View.GONE);
        binding.arrowTwo.setVisibility(View.GONE);
        binding.arrowThree.setVisibility(View.GONE);
        binding.arrowFour.setVisibility(View.GONE);

        //if it's a home for second device tutorial or device is in privacy more, just do step 2
        if ((getTutorialDetails().getTutorialType() == HOME_FOR_SECOND_DEVICE) ||
                (locationMode != null
                        && locationMode.name.equalsIgnoreCase(ModeCache.privacy)
                        && step == 1)) {
            step = 2;
        }
        switch (step) {
            case 0:
                binding.setupOneLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                binding.deviceTutorialLayout.watchLiveButton.setVisibility(View.VISIBLE);
                binding.watchLiveDscTextView.setVisibility(View.VISIBLE);
                binding.sensorDscTextView.setVisibility(View.INVISIBLE);
                binding.tutorialModeLayout.getRoot().setVisibility(View.GONE);
                binding.arrowOne.setVisibility(View.VISIBLE);

                AnimationHelper.fadeViewInAfterDelay(binding.getRoot(), 500, 750);
                TinyMessageBus.postDelayed(new PulsingDelay(binding.arrowOne, false), 1000);
                break;
            case 2:
                binding.deviceTutorialLayout.watchLiveButton.setVisibility(View.INVISIBLE);
                if (needFlexTutorial()) {
                    binding.deviceTutorialLayout.sensorDataContainer.setVisibility(View.VISIBLE);
                    binding.deviceTutorialLayout.statisticsFlexContainer.getRoot().setVisibility(View.VISIBLE);
                    binding.deviceTutorialLayout.sensorDataLayout.getRoot().setVisibility(View.GONE);
                    binding.flexTutorialDscTextView.setVisibility(View.VISIBLE);
                    binding.okayButton.setVisibility(View.VISIBLE);
                } else {
                    binding.deviceTutorialLayout.sensorDataContainer.setVisibility(View.VISIBLE);
                    binding.deviceTutorialLayout.sensorDataLayout.getRoot().setVisibility(View.VISIBLE);
                    binding.deviceTutorialLayout.statisticsFlexContainer.getRoot().setVisibility(View.GONE);
                    binding.flexTutorialDscTextView.setVisibility(View.GONE);
                    binding.okayButton.setVisibility(View.GONE);
                }


                if (needFlexTutorial())
                    binding.arrowTwo.setVisibility(View.GONE);
                else
                    binding.arrowTwo.setVisibility(View.VISIBLE);

                binding.watchLiveDscTextView.setVisibility(View.INVISIBLE);
                if (needFlexTutorial())
                    binding.sensorDscTextView.setVisibility(View.GONE);
                else
                    binding.sensorDscTextView.setVisibility(View.VISIBLE);
                binding.tutorialModeLayout.getRoot().setVisibility(View.GONE);
                AnimationHelper.fadeViewInAfterDelay(binding.getRoot(), 500, 750);
                if (!needFlexTutorial())
                    TinyMessageBus.postDelayed(new PulsingDelay(binding.arrowTwo, false), 1000);

                break;
            case 3:
                if (getTutorialDetails().getTutorialType() == HOME
                        && getTutorialDetails().totalNumOfActivatedDvices > 1
                        && !PreferencesUtils.getUserSwipedBetweenDevices()) {

                    pauseHomeTutorialForSecondDeviceTutorial();

                    return;
                }

                binding.watchLiveDscTextView.setVisibility(View.INVISIBLE);
                binding.deviceTutorialLayout.statisticsFlexContainer.getRoot().setVisibility(View.GONE);
                binding.sensorDscTextView.setVisibility(View.INVISIBLE);
                binding.deviceTutorialLayout.watchLiveButton.setVisibility(View.INVISIBLE);
                binding.tutorialModeLayout.getRoot().setVisibility(View.VISIBLE);
                binding.modeChangeDscTextView.setVisibility(View.VISIBLE);

                binding.deviceTutorialLayout.statisticsFlexContainer.getRoot().setVisibility(View.GONE);
                binding.deviceTutorialLayout.sensorDataLayout.getRoot().setVisibility(View.GONE);
                binding.flexTutorialDscTextView.setVisibility(View.GONE);
                binding.okayButton.setVisibility(View.GONE);

                binding.arrowThree.setVisibility(View.VISIBLE);

                TinyMessageBus.postDelayed(new PulsingDelay(binding.arrowThree, false), 1400);

                AnimationHelper.fadeViewInAfterDelay(binding.getRoot(), 500, 1150);
                break;
            case 4:

                binding.deviceTutorialLayout.watchLiveButton.setVisibility(View.INVISIBLE);

                binding.arrowFour.setVisibility(View.VISIBLE);

                AnimationHelper.fadeViewInAfterDelay(binding.getRoot(), 500, 750);
                binding.tutorialModeLayout.getRoot().setVisibility(View.INVISIBLE);
                binding.modeChangeDscTextView.setVisibility(View.INVISIBLE);
                binding.menuButton.setVisibility(View.VISIBLE);
                binding.menuDscTextView.setVisibility(View.VISIBLE);
                TinyMessageBus.postDelayed(new PulsingDelay(binding.arrowFour, true), 1000);
                break;
        }
    }

    private void closeFragmentQuietly() {
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().beginTransaction().remove(HomeTutorialOverlayFragment.this).
                setCustomAnimations(0, R.anim.fade_out_7).
                commit();
        TinyMessageBus.post(new BlockViewPagerDrag(false));
        TinyMessageBus.post(new TutorialRequest());
    }

    private void pauseHomeTutorialForSecondDeviceTutorial() {
        //continue the tutorial after second device tutorial
        TutorialUtil.saveTutorialDeviceIndex(getTutorialDetails().getPageToStart());
        TutorialUtil.setCompletedTutorialStep(HOME, 2);
        //                    TutorialUtil.setHasFinishedTutorial(TutorialType.HOME, true);
        TutorialUtil.finishTutorial(HOME);
        closeFragmentQuietly();
    }

    public void fadeBlackOverlayOut() {
        AnimationHelper.fadeViewOut(binding.getRoot(), 500);
        TinyMessageBus.postDelayed(new TutorialNextStep(), 500);
    }

    @Subscribe
    public void onTutorialNextStep(TutorialNextStep tutorialNextStep) {
        startTutorial(TutorialUtil.getTutorialStep(HOME));
    }

    @Subscribe
    public void onPulsingDelay(PulsingDelay pulsingDelay) {
        int dp5 = DensityUtil.dip2px(getActivity(), 5);
        AnimationHelper.fadeViewIn(pulsingDelay.view, 400);
        AnimationHelper.startPulsing(pulsingDelay.view, pulsingDelay.up, dp5, 400);
    }

    private boolean needFlexTutorial() {
        return device != null && device.getDeviceType() == DeviceType.FLEX;
    }

    private boolean needViewTutorial() {
        return device != null && device.getDeviceType() == DeviceType.CANARY_VIEW;
    }

    private TutorialDetails getTutorialDetails() {
        return getArguments() == null ? null :
                (TutorialDetails) JSONUtil.getObject(getArguments().getString(flexTutorialKey), TutorialDetails.class);
    }
}
