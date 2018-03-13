package is.yranac.canary.fragments.settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentEditDeviceBinding;
import is.yranac.canary.databinding.LayoutSensorFlexBinding;
import is.yranac.canary.fragments.setup.FindCanariesFragment;
import is.yranac.canary.fragments.setup.GetHelpFragment;
import is.yranac.canary.fragments.setup.SetCanaryConnectionTypeFragment;

import is.yranac.canary.messages.GeneralError;
import is.yranac.canary.messages.GetCachedDeviceAndLocationOwner;
import is.yranac.canary.messages.GetDeviceMasks;
import is.yranac.canary.messages.GetDeviceStatistics;
import is.yranac.canary.messages.GotCachedDeviceAndLocationOwner;
import is.yranac.canary.messages.GotDeviceMasks;
import is.yranac.canary.messages.GotDeviceStatistics;
import is.yranac.canary.messages.UpdateCachedDevice;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.model.masking.DeviceMasks;
import is.yranac.canary.model.reading.Reading;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.bluetooth.BluetoothSingleton;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.ui.MaskingActivity;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.JSONUtil;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_CHANGE_WIFI;
import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_FAILED_OTA_CANARY;
import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_FAILED_OTA_FLEX;
import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_FAILED_OTA_VIEW;
import static is.yranac.canary.ui.BaseActivity.key_device;
import static is.yranac.canary.ui.BaseActivity.key_masks;
import static is.yranac.canary.ui.BaseActivity.key_thumbnail;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_MASKING_ADD;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_MASKING_EDIT;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MASKING;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_MASKING_MANAGE_DEVICE;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_DEVICE_SETTINGS;

public class EditDeviceFragment extends SettingsFragment implements View.OnClickListener {
    private static final String LOG_TAG = "EditDeviceFragment";

    private Device device;
    private Customer owner;
    private boolean isCurrentUserLocationOwner;

    private AlertDialog removeDeviceDialog;

    private FragmentEditDeviceBinding binding;

    private String thumbnailUrl;
    private DeviceMasks deviceMasks;


    public static EditDeviceFragment newInstance(int deviceId, int totalNumDevicesAtLocation) {
        EditDeviceFragment fragment = new EditDeviceFragment();

        Bundle args = new Bundle();
        args.putInt(key_device_id, deviceId);
        args.putInt(key_num_devices, totalNumDevicesAtLocation);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditDeviceBinding.inflate(inflater);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fragmentStack.enableRightButton(this, true);

        binding.editMasksLayout.setOnClickListener(this);
        binding.aboutDevice.setOnClickListener(this);
        binding.batterySettings.setOnClickListener(this);
        binding.changeConnection.setOnClickListener(this);
        binding.retrySetupBtn.setOnClickListener(this);
        binding.removeDeviceBtn.setOnClickListener(this);


    }

    @Override
    public void onStart() {
        super.onStart();

        if (device != null) {
            binding.setDevice(device);
        }

        TinyMessageBus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        TinyMessageBus.unregister(this);
        if (binding != null && binding.maskingDisplay != null)
            TinyMessageBus.unregister(binding.maskingDisplay);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getArguments() == null || !getArguments().containsKey(key_device_id)) {
            return;
        }

        int deviceId = getArguments().getInt(key_device_id);
        TinyMessageBus.post(new GetCachedDeviceAndLocationOwner(deviceId));

        setupRightButton(true);
    }

    @Subscribe
    public void gotDeviceStatistics(GotDeviceStatistics latestStats) {
        if (device.getDeviceType() == DeviceType.FLEX) {
            binding.sensorDataFlexLayout.getRoot().setVisibility(View.VISIBLE);
            binding.sensorDataLayout.getRoot().setVisibility(View.GONE);
            setUpBatteryView(latestStats.batteryReading);
            setUpWiFiView(latestStats.wifiReading);
        } else {
            binding.sensorDataFlexLayout.getRoot().setVisibility(View.GONE);
            binding.sensorDataLayout.getRoot().setVisibility(View.VISIBLE);
            setSensorDataView(latestStats.readingAirQuality, latestStats.readingHumidity, latestStats.readingTemp);
        }
    }

    private void setSensorDataView(Reading readingAirQuality, Reading readingHumidity, Reading readingTemp) {


        if (readingAirQuality == null || readingHumidity == null || readingTemp == null || !device.isOnline) {
            binding.sensorDataLayout.deviceAirQualityOfflineIcon.setVisibility(View.VISIBLE);
            binding.sensorDataLayout.deviceAirQualityIcon.setVisibility(View.GONE);
            binding.sensorDataLayout.deviceTempTextview.setText(R.string.em_dash);
            binding.sensorDataLayout.deviceHumidityTextview.setText(R.string.em_dash);
            return;
        }
        DecimalFormat format = new DecimalFormat("#,##0");
        format.setRoundingMode(RoundingMode.HALF_UP);
        String temperature = format.format(UserUtils.getTemperatureInPreferredUnits(readingTemp.value));
        char degreeSymbol = '\u00B0';
        temperature += degreeSymbol;
        Customer customer = CurrentCustomer.getCurrentCustomer();
        if (customer != null && customer.celsius) {
            temperature += "C";
        } else {
            temperature += "F";

        }

        String humidity = format.format(readingHumidity.value);
        char percentSymbol = '\u0025';
        humidity += percentSymbol;


        binding.sensorDataLayout.deviceTempTextview.setText(temperature);
        binding.sensorDataLayout.deviceHumidityTextview.setText(humidity);
        binding.sensorDataLayout.deviceAirQualityOfflineIcon.setVisibility(View.GONE);
        binding.sensorDataLayout.deviceAirQualityIcon.setVisibility(View.VISIBLE);

        if (readingAirQuality.value > 0.6f) {
            binding.sensorDataLayout.deviceAirQualityIcon.setImageResource(R.drawable.aq_float_icon_normal);
        } else if (readingAirQuality.value > 0.4f) {
            binding.sensorDataLayout.deviceAirQualityIcon.setImageResource(R.drawable.aq_float_icon_abnormal);
        } else {
            binding.sensorDataLayout.deviceAirQualityIcon.setImageResource(R.drawable.aq_float_icon_very_abnormal);

        }
    }


    private void setUpBatteryView(Reading batteryReading) {
        LayoutSensorFlexBinding binding = this.binding.sensorDataFlexLayout;

        if (batteryReading == null || !device.isOnline) {
            batteryReading = Reading.getReadingForOfflineDevice(Reading.READING_BATTERY);
        }

        binding.statisticsBatteryView.updateBatteryState(batteryReading, device);

        if (batteryReading.getBatteryState() != Reading.BatteryState.ISSUE
                || batteryReading.getBatteryState() != Reading.BatteryState.OFFLINE) {
            binding.statisticsBatteryView.updateBatteryState(batteryReading, device);
            binding.statisticsBatteryStatus.setText(batteryReading.getBatteryStatus());
        } else {
            binding.statisticsBatteryStatus.setVisibility(View.GONE);
        }
    }

    private void setUpWiFiView(Reading wifiReading) {
        LayoutSensorFlexBinding flexBinding = binding.sensorDataFlexLayout;

        if (wifiReading == null || !device.isOnline) {
            wifiReading = Reading.getReadingForOfflineDevice(Reading.READING_WIFI);
        }
        flexBinding.statisticsWifiStatus.setVisibility(View.VISIBLE);
        flexBinding.statisticsWifiView.setVisibility(View.VISIBLE);
        flexBinding.statisticsWifiView.setWifiConnectionLevel(wifiReading);
        flexBinding.statisticsWifiStatus.setText(wifiReading.getWifiLevelLabel(getContext(), true));

    }

    @Subscribe
    public void gotCachedDeviceAndOwner(GotCachedDeviceAndLocationOwner data) {

        if (isDetached() || isRemoving())
            return;

        int deivceId = getArguments().getInt(key_device_id);
        if (deivceId != data.getDevice().id) {
            return;
        }
        device = data.getDevice();


        if (device == null) {
            return;
        }

        if (device.isMaskCompatible()) {
            binding.editMasksLayout.setVisibility(View.VISIBLE);
        }

        if (device.isOtaing()) {
            mHandler.postDelayed(mStatusChecker, 0);
        }

        this.device = data.getDevice();
        this.owner = data.getLocationOwner();
        this.isCurrentUserLocationOwner = data.isCurrentUserLocationOwner();

        binding.setDevice(device);


        fragmentStack.showLogoutButton(false);
        fragmentStack.showHeader(true);
        fragmentStack.setHeaderTitle(device.name);

        setupRightButton(false);

        setDeviceThumbnail(data.getThumbnail());
        TinyMessageBus.post(new GetDeviceMasks(device.id, true));

        TinyMessageBus.post(new GetDeviceStatistics(device));


    }

    private void setDeviceThumbnail(Thumbnail thumbnail) {
        if (thumbnail == null) {
            binding.deviceThumbnailImageView.setVisibility(View.GONE);
            binding.imagePreviewUnavailable.setVisibility(View.VISIBLE);
        } else {
            thumbnailUrl = thumbnail.imageUrl();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(thumbnail.imageUrl(), binding.deviceThumbnailImageView, new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    binding.deviceThumbnailImageView.setVisibility(View.GONE);
                    binding.imagePreviewUnavailable.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                }
            });
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        hideSoftKeyboard();

        mHandler.removeCallbacks(mStatusChecker);

        setupRightButton(true);

        if (removeDeviceDialog != null && removeDeviceDialog.isShowing()) {
            removeDeviceDialog.dismiss();
        }

    }

    @Override
    public void onRightButtonClick() {
        if (device.failedOTA()) {
            showHelpCenter();
        }
    }

    private void showHelpCenter() {
        Fragment fragment;

        switch (device.getDeviceType()) {
            case DeviceType.CANARY_AIO:
                fragment = GetHelpFragment.newInstance(GET_HELP_TYPE_FAILED_OTA_CANARY);
                break;
            case DeviceType.FLEX:
                fragment = GetHelpFragment.newInstance(GET_HELP_TYPE_FAILED_OTA_FLEX);
                break;
            case DeviceType.CANARY_VIEW:
                fragment = GetHelpFragment.newInstance(GET_HELP_TYPE_FAILED_OTA_VIEW);
                break;
            default:
                return;
        }

        addModalFragment(fragment);
    }


    private String getBodyNotOwnerDialogMessage() {
        return getString(R.string.remove_device_not_owner_body, getOwnerName());
    }

    private String getOwnerName() {
        if (owner == null) {
            return "";
        }
        return owner.firstName;
    }

    private boolean isOwner() {
        return isCurrentUserLocationOwner;
    }

    private Handler mHandler = new Handler();
    private int mInterval = 5000;

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_DEVICE_SETTINGS;
    }

    private Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            DeviceAPIService.getDeviceByUri(device.resourceUri, new Callback<Device>() {
                @Override
                public void success(final Device device, Response response) {


                    EditDeviceFragment.this.device = device;

                    TinyMessageBus.post(new UpdateCachedDevice(device));

                    BaseActivity baseActivity = (BaseActivity) getActivity();

                    if (baseActivity == null || baseActivity.isPaused())
                        return;

                    if (isDetached() && !isVisible())
                        return;
                    binding.setDevice(device);

                    if (device.failedOTA()) {
                        return;
                    }

                    if (device.ota_status.equalsIgnoreCase("complete")) {
                        binding.otaStatusBackGround.setVisibility(View.GONE);
                        return;
                    }

                    mHandler.postDelayed(mStatusChecker, mInterval);
                }

                @Override
                public void failure(RetrofitError error) {
                    mHandler.postDelayed(mStatusChecker, mInterval);
                }

            });


        }
    };

    @Subscribe
    public void handleError(GeneralError error) {
        if (removeDeviceDialog != null && removeDeviceDialog.isShowing())
            removeDeviceDialog.cancel();

        showLoading(false, null);
        Activity activity = getActivity();
        if (activity != null)
            activity.onBackPressed();
    }


    @Subscribe
    public void gotDeviceMasks(GotDeviceMasks masks) {
        if (device == null)
            return;

        this.deviceMasks = masks.deviceMasks;
        if (device.isMaskCompatible()) {
            binding.maskingDisplay.setDeviceMasks(this.deviceMasks, device);
            binding.setNumberOfMasks(this.deviceMasks.deviceMasks.size());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_device:
                openAboutCanary();
                break;

            case R.id.battery_settings:
                openBatterySettings();
                break;
            case R.id.change_connection:
                changeDeviceConnection();
                break;
            case R.id.edit_masks_layout:
                openEditMask();
                break;
            case R.id.retry_setup_btn:
                restartDeviceSetup();
                break;
            case R.id.remove_device_btn:
                removeDevice();
                break;

        }
    }

    private void openBatterySettings() {
        RecordingRangeFragment fragment = RecordingRangeFragment.newInstance(device.id);
        addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
    }

    private void openAboutCanary() {
        addFragmentToStack(AboutDeviceFragment.newInstance(getArguments(), device), Utils.SLIDE_FROM_RIGHT);
    }

    private void changeDeviceConnection() {
        hideSoftKeyboard();
        if (device.deviceType.id == DeviceType.CANARY_AIO) {
            SetCanaryConnectionTypeFragment fragment = SetCanaryConnectionTypeFragment.newInstance(true, device);
            addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
        } else if (BluetoothSingleton.checkBleHardwareAvailable()) {
            Fragment fragment = FindCanariesFragment.newInstance(true, device);
            addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
        } else {
            String dsc = getString(R.string.canary_uses_bluetooth);
            AlertUtils.showBleAlert(getActivity(), dsc, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addModalFragment(is.yranac.canary.fragments.setup.GetHelpFragment.newInstance(GET_HELP_TYPE_CHANGE_WIFI));
                }
            });
        }
    }

    private void openEditMask() {

        if (deviceMasks != null) {
            String action = deviceMasks.deviceMasks.size() > 0 ? ACTION_MASKING_EDIT : ACTION_MASKING_ADD;

            GoogleAnalyticsHelper.trackEvent(CATEGORY_MASKING, action, PROPERTY_MASKING_MANAGE_DEVICE, device.uuid, device.getLocationId(), 0);
        }

        Intent maskingActivity = new Intent(getContext(), MaskingActivity.class);
        Bundle extras = new Bundle();
        extras.putString(key_device, JSONUtil.getJSONString(device));
        extras.putString(key_thumbnail, thumbnailUrl);
        extras.putString(key_masks, JSONUtil.getJSONString(deviceMasks));

        maskingActivity.putExtras(extras);

        getContext().startActivity(maskingActivity);

    }

    private void restartDeviceSetup() {
        hideSoftKeyboard();
        if (BluetoothSingleton.checkBleHardwareAvailable()) {
            FindCanariesFragment fragment = FindCanariesFragment.newInstance(false, device);
            addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
        } else if (device.deviceType.id == DeviceType.CANARY_AIO) {
            SetCanaryConnectionTypeFragment fragment = getInstance(SetCanaryConnectionTypeFragment.class);
            addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
        } else {
            String dsc = getString(R.string.canary_uses_bluetooth);
            AlertUtils.showBleAlert(getContext(), dsc, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addModalFragment(is.yranac.canary.fragments.setup.GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_BLUETOOTH));
                }
            });
        }
    }

    private void removeDevice() {
        if (!isOwner()) {
            AlertUtils.showGenericAlert(
                    getActivity(),
                    getString(R.string.remove_device_not_owner_header),
                    getBodyNotOwnerDialogMessage());
            return;
        }
        removeDeviceDialog = AlertUtils.showRemoveAlert(getContext(), getString(R.string.are_you_sure_remove, device.name), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(true, getString(R.string.removing));
                DeviceAPIService.deleteDevice(device, new Callback<Void>() {
                    @Override
                    public void success(Void aVoid, Response response) {
                        showLoading(false, getString(R.string.removing));

                        Activity activity = getActivity();
                        if (activity == null)
                            return;

                        activity.onBackPressed();
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        showLoading(false, getString(R.string.removing));
                        try {
                            removeDeviceDialog = AlertUtils.showGenericAlert(getContext(), Utils.getErrorMessageFromRetrofit(getContext(), retrofitError));
                        } catch (JSONException ignored) {
                        }
                    }
                });
            }
        });
    }

    private void setupRightButton(boolean revert) {
        if (revert) {
            fragmentStack.resetButtonStyle();
        } else {
            if (device != null) {
                if (device.deviceActivated || device.isOtaing()) {
                    fragmentStack.resetButtonStyle();
                    fragmentStack.enableRightButton(this, false);
                    return;
                } else if (device.failedOTA()) {
                    fragmentStack.showHelpButton();
                }
            }
        }

        fragmentStack.enableRightButton(this, !revert);
    }
}