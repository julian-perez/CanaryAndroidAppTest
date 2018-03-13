package is.yranac.canary.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.contentproviders.CanaryDeviceContentProvider;
import is.yranac.canary.databinding.FragmentDeviceBinding;
import is.yranac.canary.databinding.FragmentTimelineOverlayBinding;
import is.yranac.canary.databinding.LayoutSensorFlexBinding;
import is.yranac.canary.messages.BlockViewPagerDrag;
import is.yranac.canary.messages.CurrentLocationUpdated;
import is.yranac.canary.messages.DeviceUpdateRequest;
import is.yranac.canary.messages.GetDeviceStatistics;
import is.yranac.canary.messages.GotCachedDevice;
import is.yranac.canary.messages.GotDeviceStatistics;
import is.yranac.canary.messages.LocationCurrentModeChanging;
import is.yranac.canary.messages.PastReadingsUpdated;
import is.yranac.canary.messages.RefreshedDevice;
import is.yranac.canary.messages.ResetSecondDeviceTutorial;
import is.yranac.canary.messages.ShowModalFragment;
import is.yranac.canary.messages.ShowModeSettings;
import is.yranac.canary.messages.ShowModeTray;
import is.yranac.canary.messages.tutorial.StartSecondDeviceTutorial;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.mode.Mode;
import is.yranac.canary.model.mode.ModeCache;
import is.yranac.canary.model.reading.Reading;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.database.ReadingDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.ui.MainActivity;
import is.yranac.canary.ui.SettingsFragmentStackActivity;
import is.yranac.canary.ui.WatchLiveActivity;
import is.yranac.canary.ui.views.spotlightview.Spotlight;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.AnimationHelper;
import is.yranac.canary.util.DateUtil;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.JSONUtil;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.TouchTimeUtil;
import is.yranac.canary.util.TutorialUtil;
import is.yranac.canary.util.TutorialUtil.TutorialType;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ZendeskUtil;

import static is.yranac.canary.ui.SettingsFragmentStackActivity.DEVICE;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.flex_connectivity;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.modal;
import static is.yranac.canary.util.TutorialUtil.TutorialType.ENTRY_MORE_OPTIONS;
import static is.yranac.canary.util.TutorialUtil.TutorialType.NONE;
import static is.yranac.canary.util.TutorialUtil.TutorialType.SECOND_DEVICE;
import static is.yranac.canary.util.TutorialUtil.TutorialType.TIMELINE_FILTER;

public class DeviceFragment extends Fragment {


    public static final String deviceJsonKey = "deviceJson";
    public static final String indexInViewPagerKey = "deviceIndex";

    private Spotlight spotlight;

    private Device device;

    private char degreeSymbol = '\u00B0';
    private char percentSymbol = '\u0025';
    private float overlayAlpha = 1f;

    private boolean staleDeviceInfo;

    private FragmentDeviceBinding binding;

    private FragmentTimelineOverlayBinding tutorialBinding;

    public static DeviceFragment newInstance(String deviceJson, int indexInViewPager) {
        DeviceFragment f = new DeviceFragment();

        Bundle args = new Bundle();
        args.putString(deviceJsonKey, deviceJson);
        args.putInt(indexInViewPagerKey, indexInViewPager);

        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            device = new Gson().fromJson(savedInstanceState.getString(deviceJsonKey), Device.class);
        } else {
            device = new Gson().fromJson(getArguments().getString(deviceJsonKey), Device.class);
        }
        binding.deviceInfoLayout.watchLiveButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLiveActivity();
                    }
                });

        Location location = UserUtils.getLastViewedLocation();
        if (location == null || location.recentlyUpdated()) {
            binding.deviceInfoLayout.getRoot().setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.deviceInfoLayout.getRoot().setVisibility(View.GONE);
        }
        binding.deviceInfoLayout.deviceStatusContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getDevice().isOnline)
                    return;

                String url = getString(R.string.connectivity_url);
                ZendeskUtil.loadHelpCenter(getContext(), url);
            }
        });

        if (Utils.isDemo()) {
            binding.deviceInfoLayout.deviceName.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertUtils.showGenericAlert(getActivity(), getString(R.string.multiple_perspectives),
                            getString(R.string.multiple_perspectives_dsc));
                }
            });
        }
        binding.deviceInfoLayout.deviceName.setText(getDevice().name);

        if (device.deviceType.id == DeviceType.FLEX) {
            binding.deviceInfoLayout.sensorDataLayout.getRoot().setVisibility(View.GONE);
            binding.deviceInfoLayout.statisticsFlexContainer.getRoot().setVisibility(View.VISIBLE);
            binding.deviceInfoLayout.spotlightMessage.setVisibility(View.GONE);
        } else {
            binding.deviceInfoLayout.sensorDataLayout.getRoot().setVisibility(View.VISIBLE);
            binding.deviceInfoLayout.statisticsFlexContainer.getRoot().setVisibility(View.GONE);
        }

        spotlight = new Spotlight();

        spotlight.start(binding.deviceInfoLayout.spotlightMessage);
        binding.deviceInfoLayout.sensorDataLayout.getRoot().setOnClickListener(homeHealthMetricListener);
        binding.deviceInfoLayout.statisticsFlexContainer.getRoot().setOnClickListener(flexStatsOnClickListener);
        loadDeviceImage();

        setOverlayViewAlphas();
        updateDisplayMode(device);
    }

    @Subscribe(mode = Subscribe.Mode.Background)
    public void refreshDeviceInfoFromCache(GotCachedDevice oldDevice) {
        if (oldDevice.device.id != getDevice().id)
            return;


        Device newDevice = DeviceDatabaseService.getDeviceFromId(oldDevice.device.id);
        if (newDevice == null)
            return;

        TinyMessageBus.post(new RefreshedDevice(newDevice));
    }

    @Subscribe(mode = Subscribe.Mode.Main)
    public void updateDeviceName(RefreshedDevice newDevice) {
        if (newDevice.newDevice.id != getDevice().id)
            return;

        if (device == null) {
            getDeviceSensorReadings(newDevice.newDevice);
        }
        device = newDevice.newDevice;
        binding.deviceInfoLayout.deviceName.setText(device.name);
        staleDeviceInfo = false;
        updateDisplayMode(newDevice.newDevice);

    }

    @Override
    public void onResume() {
        super.onResume();

        updateDisplayMode(getDevice());

        // seed the device status with the current database values
        onPastReadingsUpdated(null);

        setOverlayViewAlphas();

        if (staleDeviceInfo)
            TinyMessageBus.post(new GotCachedDevice(getDevice()));

        if (getDevice() != null) {
            getDeviceSensorReadings(getDevice());

        }
    }

    @Override
    public void onPause() {
        super.onPause();

        spotlight.cancel();
        staleDeviceInfo = true;
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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(deviceJsonKey, new Gson().toJson(device));
    }

    private OnClickListener homeHealthMetricListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // mis-clicking prevention, using threshold of 1000 ms
            if (TouchTimeUtil.dontAllowTouch()) {
                return;
            }

            TinyMessageBus.post(new BlockViewPagerDrag(true));

            Reading firstReading = ReadingDatabaseService.getFirstReadingByDevice(getDevice().resourceUri);
            Calendar cal = DateUtil.getCalanderInstance();
            cal.add(Calendar.HOUR, -23);

            if (firstReading == null || cal.getTime()
                    .before(firstReading.created)) {

                showHomeHealthOverlay();
                return;
            }
            showGraphs();
        }
    };
    private OnClickListener flexStatsOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // mis-clicking prevention, using threshold of 1000 ms
            Intent intent = new Intent(getActivity(), SettingsFragmentStackActivity.class);
            intent.setAction(flex_connectivity);
            intent.putExtra(DEVICE, JSONUtil.getJSONString(device));
            intent.putExtra(modal, true);
            startActivity(intent);
        }
    };


    @Subscribe
    public void gotDeviceStatistics(GotDeviceStatistics gotDeviceStatistics) {

        if (getDevice() == null || gotDeviceStatistics.device == null)
            return;

        if (gotDeviceStatistics.device.id != getDevice().id)
            return;

        setUpDeviceStatisticsViews(gotDeviceStatistics);
    }


    private void setUpDeviceStatisticsViews(GotDeviceStatistics deviceStatistics) {
        int deviceType = getDevice().getDeviceType();

        if (!getDevice().isOnline) {
            toggleStatisticsLoadingState(false);
        }
        binding.deviceInfoLayout.sensorDataContainer.setVisibility(View.VISIBLE);

        switch (deviceType) {
            case DeviceType.FLEX:
                updateWifiAndBattery(deviceStatistics.wifiReading, deviceStatistics.batteryReading);
                break;
            case DeviceType.CANARY_AIO:
                updateHomeHealthValues(deviceStatistics.readingTemp, deviceStatistics.readingAirQuality, deviceStatistics.readingHumidity);
                break;
            default:
                return;
        }
    }


    private void updateHomeHealthValues(Reading temperatureReading,
                                        Reading airReading, Reading humidityReading) {
        if (!getDevice().isOnline) {
            showHomeHealthOfflineView();
            return;
        }
        binding.deviceInfoLayout.sensorDataContainer.setVisibility(View.VISIBLE);


        if (getDevice().deviceType.id == DeviceType.FLEX)
            return;

        if (!hasHealthData(temperatureReading, airReading, humidityReading)) {
            toggleStatisticsLoadingState(true);
            return;
        } else
            toggleStatisticsLoadingState(false);

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


        binding.deviceInfoLayout.sensorDataLayout.deviceTempTextview.setText(temperature);
        binding.deviceInfoLayout.sensorDataLayout.deviceHumidityTextview.setText(humidity);
        binding.deviceInfoLayout.sensorDataLayout.deviceAirQualityOfflineIcon.setVisibility(View.GONE);
        binding.deviceInfoLayout.sensorDataLayout.deviceAirQualityIcon.setVisibility(View.VISIBLE);

        if (airReading.value > 0.6f) {
            binding.deviceInfoLayout.sensorDataLayout.deviceAirQualityIcon.setImageResource(R.drawable.aq_float_icon_normal);
        } else if (airReading.value > 0.4f) {
            binding.deviceInfoLayout.sensorDataLayout.deviceAirQualityIcon.setImageResource(R.drawable.aq_float_icon_abnormal);
        } else {
            binding.deviceInfoLayout.sensorDataLayout.deviceAirQualityIcon.setImageResource(R.drawable.aq_float_icon_very_abnormal);

        }
    }

    private void toggleStatisticsLoadingState(boolean show) {
        if (show) {
            if (binding.deviceInfoLayout.spotlightMessage.getVisibility() != View.VISIBLE) {
                Utils.crossFadeViews(binding.deviceInfoLayout.spotlightMessage, binding.deviceInfoLayout.sensorDataContainer);
            } else {
                binding.deviceInfoLayout.spotlightMessage.setVisibility(View.VISIBLE);
                binding.deviceInfoLayout.sensorDataContainer.setVisibility(View.GONE);
            }

            if (spotlight != null && !spotlight.isAnimating())
                spotlight.start(binding.deviceInfoLayout.spotlightMessage);

        } else {
            if (binding.deviceInfoLayout.spotlightMessage.getVisibility() == View.VISIBLE) {
                Utils.crossFadeViews(binding.deviceInfoLayout.sensorDataContainer, binding.deviceInfoLayout.spotlightMessage);
                if (spotlight != null)
                    spotlight.cancel();
            } else {
                binding.deviceInfoLayout.spotlightMessage.setVisibility(View.GONE);
                binding.deviceInfoLayout.sensorDataContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showHomeHealthOfflineView() {
        binding.deviceInfoLayout.sensorDataLayout.deviceAirQualityOfflineIcon.setVisibility(View.VISIBLE);
        binding.deviceInfoLayout.sensorDataLayout.deviceAirQualityIcon.setVisibility(View.GONE);
        binding.deviceInfoLayout.sensorDataLayout.deviceTempTextview.setText(R.string.em_dash);
        binding.deviceInfoLayout.sensorDataLayout.deviceHumidityTextview.setText(R.string.em_dash);
    }

    public Device getDevice() {
        if (device == null)
            device = new Gson().fromJson(getArguments().getString(deviceJsonKey), Device.class);
        return device;

    }

    public void setOverlayViewAlphas() {

        if (device == null)
            return;

        if (isVisible()) {
            overlayAlpha = MainActivity.currentVerticalViewPage == MainActivity.DEVICE_STATUS_PAGE ? 1f : 0f;
        } else {
            overlayAlpha = 1f;
        }
        updateOverlayViewAlphas(overlayAlpha);


    }

    public void updateOverlayViewAlphas(float overlayAlpha) {

        if (binding == null)
            return;

        this.overlayAlpha = overlayAlpha;
        binding.deviceInfoLayout.getRoot().setAlpha(overlayAlpha);
    }

    private boolean hasHealthData(Reading temperatureReading,
                                  Reading airReading,
                                  Reading humidityReading) {
        return (temperatureReading != null
                && airReading != null
                && humidityReading != null);
    }

    private void showWatchLive() {
        binding.deviceInfoLayout.deviceStatusContainer.setVisibility(View.VISIBLE);

        binding.deviceInfoLayout.deviceOffineTextView.setVisibility(View.GONE);
        binding.deviceInfoLayout.devicePrivacyTextView.setVisibility(View.GONE);
        binding.deviceInfoLayout.watchLiveButton.setVisibility(View.VISIBLE);

        if (MainActivity.currentVerticalViewPage == MainActivity.TIMELINE_PAGE) {
            updateOverlayViewAlphas(0f);
        }
    }

    private void showPrivacyMode() {
        binding.deviceInfoLayout.deviceStatusContainer.setVisibility(View.VISIBLE);

        binding.deviceInfoLayout.watchLiveButton.setVisibility(View.GONE);

        binding.deviceInfoLayout.deviceOffineTextView.setVisibility(View.GONE);
        binding.deviceInfoLayout.devicePrivacyTextView.setVisibility(View.VISIBLE);
        binding.deviceInfoLayout.devicePrivacyTextView.setText(R.string.watch_live_off);

        binding.deviceInfoLayout.devicePrivacyTextView.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TinyMessageBus.post(new ShowModeSettings());
                    }
                });

        if (MainActivity.currentVerticalViewPage == MainActivity.TIMELINE_PAGE) {
            updateOverlayViewAlphas(0f);
        }
    }

    private void showLocationPrivacyMode() {
        showPrivacyMode();


        binding.deviceInfoLayout.devicePrivacyTextView.setText(R.string.location_in_privacy);
        binding.deviceInfoLayout.devicePrivacyTextView.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TinyMessageBus.post(new ShowModeTray());
                    }
                });


    }

    private void showOfflineMode() {

        binding.deviceInfoLayout.deviceStatusContainer.setVisibility(View.VISIBLE);
        binding.deviceInfoLayout.deviceOffineTextView.setVisibility(View.VISIBLE);
        binding.deviceInfoLayout.devicePrivacyTextView.setVisibility(View.GONE);
        binding.deviceInfoLayout.watchLiveButton.setVisibility(View.GONE);
        if (MainActivity.currentVerticalViewPage == MainActivity.TIMELINE_PAGE) {
            updateOverlayViewAlphas(0f);
        }
    }

    private void updateDisplayMode(Device newDevice) {
        if (device != null && device.id != newDevice.id)
            return;

        Location location = LocationDatabaseService.getLocationFromResourceUri(newDevice.location);
        if (location == null) {
            return;
        }


        TutorialType tutorialType = TutorialUtil.getTutorialInProgress();
        boolean inNonHomeScreenTutoirial = tutorialType == NONE || tutorialType == TIMELINE_FILTER || tutorialType == ENTRY_MORE_OPTIONS;
        if (location.recentlyUpdated() && binding.deviceInfoLayout.getRoot().getVisibility() != View.VISIBLE &&
                inNonHomeScreenTutoirial) {
            AnimationHelper.fadeViewIn(binding.deviceInfoLayout.getRoot(), 400);
            AnimationHelper.fadeViewOut(binding.progressBar, 400);
        }

        Mode modePrivacy = ModeCache.getMode(ModeCache.privacy);
        if (!newDevice.isOnline) {
            showOfflineMode();
        } else if (location.isPrivate) {
            showLocationPrivacyMode();
        } else if (device.mode.equalsIgnoreCase(modePrivacy.resourceUri)) {
            showPrivacyMode();
        } else {
            showWatchLive();
        }
    }

    private void showHomeHealthOverlay() {

        AlertDialog alertDialog = AlertUtils.showGenericAlert(getActivity(), getString(R.string.time_travel_error),
                getString(R.string.home_health_explaination), R.drawable.graph_icon,
                getString(R.string.continue_text), null, 0, 0, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TouchTimeUtil.dontAllowTouch()) {
                            return;
                        }
                        showGraphs();
                    }
                }, null);

        alertDialog.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        TinyMessageBus
                                .post(new BlockViewPagerDrag(false));
                    }
                });
    }


    private void showGraphs() {
        HomeHealthDataFragment fragmentModeInfo = HomeHealthDataFragment.newInstance(getDevice().resourceUri, getDevice().name);
        TinyMessageBus
                .post(new ShowModalFragment(fragmentModeInfo));
    }

    private void showLiveActivity() {
        BaseActivity activity = (BaseActivity) getActivity();
        if (!activity.hasInternetConnection()) {
            return;
        }

        Intent i = new Intent(getActivity(), WatchLiveActivity.class);
        i.putExtra(CanaryDeviceContentProvider.COLUMN_UUID, getDevice().uuid);
        i.putExtra(CanaryDeviceContentProvider.COLUMN_LOCATION_ID, getDevice().location);
        getActivity().startActivity(i);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.none);
    }

    @Subscribe
    public void onDeviceUpdateRequest(DeviceUpdateRequest message) {
        Device newDevice = DeviceDatabaseService.getDeviceFromId(device.id);

        if (newDevice == null) {
            return;
        }

        device = newDevice;

        updateDisplayMode(newDevice);

        if (Utils.deviceImageUrlEquals(getDevice().imageUrl(), newDevice.imageUrl())) {
            loadDeviceImage();
        }


    }

    @Subscribe
    public void currentLocationModeUpdated(LocationCurrentModeChanging locationCurrentModeChanging) {
        if (locationCurrentModeChanging.isChanging)
            return;

        onDeviceUpdateRequest(null);
    }

    @Subscribe
    public void onPastReadingsUpdated(PastReadingsUpdated message) {
        getDeviceSensorReadings(getDevice());
    }

    private void updateWifiAndBattery(Reading wifiReading, Reading batteryReading) {

        LayoutSensorFlexBinding flexBinding = binding.deviceInfoLayout.statisticsFlexContainer;
        flexBinding.getRoot().setVisibility(View.VISIBLE);

        if (!device.isOnline)
            batteryReading = Reading.getReadingForOfflineDevice(Reading.READING_BATTERY);
        else {
            if (batteryReading == null)
                batteryReading = new Reading();


        }
        flexBinding.statisticsBatteryStatus.setText(batteryReading.getBatteryStatus());
        flexBinding.statisticsBatteryView.updateBatteryState(batteryReading, device);


        if (!device.isOnline) {
            wifiReading = Reading.getReadingForOfflineDevice(Reading.READING_WIFI);
        } else {
            if (wifiReading == null)
                wifiReading = new Reading();
        }

        flexBinding.statisticsWifiStatus.setVisibility(View.VISIBLE);
        flexBinding.statisticsWifiView.setVisibility(View.VISIBLE);

        flexBinding.statisticsWifiStatus.setText(wifiReading.getWifiLevelLabel(getContext(), true));
        flexBinding.statisticsWifiView.setWifiConnectionLevel(wifiReading);

    }


    @Subscribe
    public void onCurrentLocationUpdated(CurrentLocationUpdated currentLocationUpdated) {
        updateDisplayMode(getDevice());
    }

    private void loadDeviceImage() {
        final ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(getDevice().imageUrl(), binding.imageView);
    }

    @Subscribe
    public void startSecondDeviceTutorial(StartSecondDeviceTutorial startSecondDeviceTutorial) {
        if (startSecondDeviceTutorial.getTutorialDetails().getPageToStart() != getViewPagerIndex())
            return;

        if (MainActivity.currentVerticalViewPage == MainActivity.DEVICE_STATUS_PAGE) {
            binding.tutorialView.removeAllViews();
            tutorialBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_timeline_overlay, binding.tutorialView, true);
            tutorialBinding.setTutorialType(SECOND_DEVICE);
            binding.tutorialView.setVisibility(View.VISIBLE);
            AnimationHelper.fadeViewIn(tutorialBinding.getRoot(), 400);
            int dp5 = DensityUtil.dip2px(getContext(), 5);
            AnimationHelper.startShaking(tutorialBinding.secondDeviceArrow, dp5, 400);

            PreferencesUtils.setUserSwipedBetweenDevices();
        }
    }

    @Subscribe
    public void endSecondDeviceTutorial(ResetSecondDeviceTutorial resetSecondDeviceTutorial) {
        if (binding != null && binding.tutorialView != null)
            binding.tutorialView.setVisibility(View.GONE);
    }

    private int getViewPagerIndex() {
        return getArguments() == null ? 0 : getArguments().getInt(indexInViewPagerKey, 0);
    }

    private void getDeviceSensorReadings(Device device) {
        if (getDevice().hasSensors()) {
            binding.deviceInfoLayout.sensorContainer.setVisibility(View.VISIBLE);
            TinyMessageBus.post(new GetDeviceStatistics(device));
        } else {
            binding.deviceInfoLayout.sensorContainer.setVisibility(View.GONE);
        }
    }
}
