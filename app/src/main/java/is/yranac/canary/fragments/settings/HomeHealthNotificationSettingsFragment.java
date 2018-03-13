package is.yranac.canary.fragments.settings;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.android.segmented.SegmentedGroup;
import is.yranac.canary.R;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceSettings;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.ui.views.RangeSeekBar;
import is.yranac.canary.util.AnimationHelper;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_HOME_HEALTH_NOTIFICATION_SETTINGS;

/**
 * Created by Schroeder on 9/24/15.
 */
public class HomeHealthNotificationSettingsFragment extends SettingsFragment {

    private static final int SLIDER_MIN = 0;
    private static final int SLIDER_MID = 50;
    private static final int SLIDER_MAX = 100;

    private static final float AIR_QUALITY_MIN = 0;
    private static final float AIR_QUALITY_ABNORMAL = 0.6f;
    private static final float AIR_QUALITY_VERY_ABNORMAL = 0.4f;

    private static final String LOG_TAG = "HomeHealthNotificationSettingsFragment";
    private ViewGroup notificationSettingsBody;
    private SegmentedGroup segmentedGroup;

    private List<Device> devices;
    private List<DeviceSettings> deviceSettingses = new ArrayList<>();
    private List<DeviceSettings> originalDeviceSettingses = new ArrayList<>();
    private int locationId;
    private LayoutInflater layoutInflater;
    private LinearLayout linearLayout;
    private SwitchCompat toggle;
    private boolean needCelsus;

    private enum SensorType {
        TEMPERATURE,
        HUMIDITY
    }

    public static HomeHealthNotificationSettingsFragment newInstance(int locationId) {
        HomeHealthNotificationSettingsFragment fragment = new HomeHealthNotificationSettingsFragment();
        Bundle args = new Bundle();
        args.putInt(location_id, locationId);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_health_notificaions_settings, container, false);

    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toggle = (SwitchCompat) view.findViewById(R.id.mode_setting_checkbox);
        notificationSettingsBody = (ViewGroup) view.findViewById(R.id.notification_settings_body);

        Customer customer = CurrentCustomer.getCurrentCustomer();
        if (customer != null)
            needCelsus = customer.celsius;

        View topLayout = view.findViewById(R.id.home_health_settings_toggle_layout);
        topLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle.toggle();

                for (DeviceSettings deviceSettings : deviceSettingses) {
                    deviceSettings.sendHomehealthNotifications = toggle.isChecked();
                }

                if (toggle.isChecked()) {
                    adjustNotificationHeader();
                    if (allTheSame()) {
                        segmentedGroup.check(R.id.set_all_btn);
                        setupSetAllSettings();
                    } else {
                        segmentedGroup.check(R.id.set_each_btn);
                        setupSetEachSettings();
                    }
                    AnimationHelper.expandHeight(notificationSettingsBody, getActivity(), 250);
                } else {
                    AnimationHelper.collapseHeight(notificationSettingsBody, getActivity(), 250);
                }

                enableRightButton();
            }
        });

        segmentedGroup = (SegmentedGroup) view.findViewById(R.id.segmented_group);
        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.set_all_btn:
                        setupSetAllSettings();
                        break;
                    case R.id.set_each_btn:
                        setupSetEachSettings();
                        break;
                }

                enableRightButton();
            }
        });

        locationId = getArguments().getInt(location_id);

        linearLayout = (LinearLayout) view.findViewById(R.id.notification_settings_layout);
        layoutInflater = LayoutInflater.from(getActivity());

        getDevicesAndDeviceSettings();

        if (allOff()) {
            toggle.setChecked(false);
            notificationSettingsBody.setVisibility(View.GONE);

        } else {
            toggle.setChecked(true);
            notificationSettingsBody.setVisibility(View.VISIBLE);
        }

        if (devices.size() == 1) {
            setupSetAllSettings();
            segmentedGroup.setVisibility(View.GONE);
        } else {
            adjustNotificationHeader();
            if (allTheSame()) {
                segmentedGroup.check(R.id.set_all_btn);
            } else {
                segmentedGroup.check(R.id.set_each_btn);
            }
        }

        enableRightButton();
    }

    private boolean allOff() {
        for (DeviceSettings deviceSettings : deviceSettingses) {
            if (deviceSettings.sendHomehealthNotifications)
                return false;
        }
        return true;
    }

    private void setupSetEachSettings() {
        linearLayout.removeAllViews();

        //resetting to original settings
        for (int i = 0; i < originalDeviceSettingses.size(); i++) {
            DeviceSettings originalSetting = originalDeviceSettingses.get(i);
            DeviceSettings currentSetting = deviceSettingses.get(i);
            currentSetting.humidityThresholdMax = originalSetting.humidityThresholdMax;
            currentSetting.humidityThresholdMin = originalSetting.humidityThresholdMin;
            currentSetting.airQualityThreshold = originalSetting.airQualityThreshold;
            currentSetting.tempThresholdMin = originalSetting.tempThresholdMin;
            currentSetting.tempThresholdMax = originalSetting.tempThresholdMax;
        }

        for (Device device : devices) {
            DeviceSettings deviceSettings = null;
            for (DeviceSettings deviceSettings1 : deviceSettingses) {
                if (device.id == Utils.getIntFromResourceUri(deviceSettings1.resourceUri)) {
                    deviceSettings = deviceSettings1;
                    break;
                }
            }
            if (deviceSettings != null) {
                setUpNotificationView(deviceSettings, device);
            }
        }
    }


    private void setupSetAllSettings() {
        linearLayout.removeAllViews();

        if (deviceSettingses.isEmpty())
            return;

        DeviceSettings firstDeviceSettings = deviceSettingses.get(0);
        firstDeviceSettings.sendHomehealthNotifications = true;

        for (DeviceSettings deviceSettings : deviceSettingses) {
            deviceSettings.humidityThresholdMax = firstDeviceSettings.humidityThresholdMax;
            deviceSettings.sendHumidityMaxNotifications = firstDeviceSettings.sendHumidityMaxNotifications;
            deviceSettings.humidityThresholdMin = firstDeviceSettings.humidityThresholdMin;
            deviceSettings.sendHumidityMinNotifications = firstDeviceSettings.sendHumidityMinNotifications;

            deviceSettings.airQualityThreshold = firstDeviceSettings.airQualityThreshold;
            deviceSettings.sendAirQualityNotifications = firstDeviceSettings.sendAirQualityNotifications;

            deviceSettings.tempThresholdMin = firstDeviceSettings.tempThresholdMin;
            deviceSettings.sendTempMinNotifications = firstDeviceSettings.sendTempMinNotifications;
            deviceSettings.tempThresholdMax = firstDeviceSettings.tempThresholdMax;
            deviceSettings.sendTempMaxNotifications = firstDeviceSettings.sendTempMaxNotifications;

            deviceSettings.sendHomehealthNotifications = firstDeviceSettings.sendHomehealthNotifications;
        }
        setUpNotificationView(firstDeviceSettings, null);

    }

    private void setUpNotificationView(final DeviceSettings deviceSettings, Device device) {
        View notificationView = layoutInflater.inflate(R.layout.listview_device_home_health_settings, null, false);
        final TextView textView = (TextView) notificationView.findViewById(R.id.disarm_text_view);
        final ViewGroup notificationHeader = (ViewGroup) notificationView.findViewById(R.id.notification_header);
        if (device == null) {
            notificationHeader.setVisibility(View.GONE);
        } else {
            textView.setText(device.name);
            notificationHeader.setVisibility(View.VISIBLE);
        }

        final SwitchCompat enabledCheckbox = (SwitchCompat) notificationView.findViewById(R.id.notification_enable_checkbox);
        final LinearLayout notificationLayout = (LinearLayout) notificationView.findViewById(R.id.notification_layout);
        notificationHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enabledCheckbox.toggle();
                deviceSettings.sendHomehealthNotifications = enabledCheckbox.isChecked();

                if (enabledCheckbox.isChecked()) {
                    AnimationHelper.expandHeight(notificationLayout, getActivity(), 250);
                } else {
                    AnimationHelper.collapseHeight(notificationLayout, getActivity(), 250);
                }
                enableRightButton();
            }
        });

        if (deviceSettings != null && deviceSettings.sendHomehealthNotifications) {
            enabledCheckbox.setChecked(true);
        } else {
            enabledCheckbox.setChecked(false);
            notificationLayout.setVisibility(View.GONE);
        }


        setupTempRange(notificationView, deviceSettings);
        setupHumidityRange(notificationView, deviceSettings);
        setupAirQualityRange(notificationView, deviceSettings);
        linearLayout.addView(notificationView);
    }

    private Drawable getSliderDrawable() {

        Resources res = getResources();
        Drawable thumb = res.getDrawable(R.drawable.slider_knob);
        int h = DensityUtil.dip2px(getActivity(), 35);
        int w = DensityUtil.dip2px(getActivity(), 30);
        Bitmap bmpOrg = ((BitmapDrawable) thumb).getBitmap();
        Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, w, h, true);
        Drawable newThumb = new BitmapDrawable(res, bmpScaled);
        newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());

        return newThumb;
    }


    private void setupTempRange(View notificationView, final DeviceSettings deviceSettings) {
        int gray = getResources().getColor(R.color.gray);
        int cyan = getResources().getColor(R.color.dark_moderate_cyan);
        FrameLayout frameLayout = (FrameLayout) notificationView.findViewById(R.id.temp_range_frame);

        final float graphMin = DeviceSettings.getMinAllowedTempVal(needCelsus) - 1;
        final float graphMax = DeviceSettings.getMaxAllowedTempVal(needCelsus) + 1;
        final float graphRange = DeviceSettings.getAllowedTempRange(needCelsus);

        final RangeSeekBar<Float> rangeSeekBar = new RangeSeekBar<>(graphMin, graphMax, graphRange, getActivity(), gray, cyan, gray, getSliderDrawable());

        final TextView tempMinTextView = (TextView) notificationView.findViewById(R.id.temp_min_max_text_view);
        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Float>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Float minValue, Float maxValue) {

                //rounding right away to minimize conversion rounding issues
                minValue = UserUtils.formatFloatDecimalPlaces(minValue, 0);
                maxValue = UserUtils.formatFloatDecimalPlaces(maxValue, 0);

                //taking care of the "no alerts" graph values
                if (minValue > (graphMax - 1) - graphRange)
                    minValue = (graphMax - 1) - graphRange;

                if (maxValue < (graphMin + 1) + graphRange)
                    maxValue = (graphMin + 1) + graphRange;

                tempMinTextView.setText(getLabel(SensorType.TEMPERATURE, minValue, maxValue, needCelsus));

                if (segmentedGroup.getCheckedRadioButtonId() == R.id.set_each_btn) {
                    deviceSettings.setTempNotificationPreferences(minValue, maxValue, needCelsus);
                } else {
                    for (DeviceSettings deviceSettings1 : deviceSettingses) {
                        deviceSettings1.setTempNotificationPreferences(minValue, maxValue, needCelsus);
                    }
                }
                enableRightButton();
            }
        });

        float setSelectedMin, setSelectedMax;

        if (deviceSettings.sendTempMaxNotifications)
            setSelectedMax = deviceSettings.getTempThresholdMax(needCelsus);
        else
            setSelectedMax = graphMax;

        if (deviceSettings.sendTempMinNotifications)
            setSelectedMin = deviceSettings.getTempThresholdMin(needCelsus);
        else
            setSelectedMin = graphMin;

        rangeSeekBar.setSelectedMinValue(setSelectedMin);
        rangeSeekBar.setSelectedMaxValue(setSelectedMax);

        tempMinTextView.setText(getLabel(SensorType.TEMPERATURE, setSelectedMin, setSelectedMax, needCelsus));
        frameLayout.addView(rangeSeekBar);
    }

    private String getTempLabel(float val, boolean isCelsius) {
        float allowedMax = DeviceSettings.getMaxAllowedTempVal(isCelsius);
        float allowedMin = DeviceSettings.getMinAllowedTempVal(isCelsius);

        if (val <= allowedMax && val >= allowedMin) {
            return String.format(Locale.getDefault(), "%.0f°%s", val, isCelsius ? "C" : "F");
        }

        return getString(R.string.no_alert);
    }


    private void setupHumidityRange(View notificationView, final DeviceSettings deviceSettings) {
        int gray = getResources().getColor(R.color.gray);
        int cyan = getResources().getColor(R.color.dark_moderate_cyan);
        FrameLayout frameLayout = (FrameLayout) notificationView.findViewById(R.id.humidity_range_frame);

        float graphMin = 0.00f, graphMax = 1.00f, graphRange = 0.1f;
        RangeSeekBar<Float> rangeSeekBar = new RangeSeekBar<>(graphMin, graphMax, graphRange, getActivity(), gray, cyan, gray, getSliderDrawable());

        final TextView tempMinTextView = (TextView) notificationView.findViewById(R.id.humidity_min_text_view);
        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Float>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Float minValue, Float maxValue) {

                tempMinTextView.setText(getLabel(SensorType.HUMIDITY, minValue, maxValue, false));
                if (segmentedGroup.getCheckedRadioButtonId() == R.id.set_each_btn) {
                    deviceSettings.setHumidityNotificationPreferences(minValue, maxValue);
                } else {
                    for (DeviceSettings deviceSettings1 : deviceSettingses) {
                        deviceSettings1.setHumidityNotificationPreferences(minValue, maxValue);
                    }
                }

                enableRightButton();
            }
        });

        float setMin, setMax;
        if (deviceSettings.sendHumidityMaxNotifications)
            setMax = deviceSettings.humidityThresholdMax;
        else
            setMax = graphMax;

        if (deviceSettings.sendHumidityMinNotifications)
            setMin = deviceSettings.humidityThresholdMin;
        else
            setMin = graphMin;

        rangeSeekBar.setSelectedMinValue(setMin);
        rangeSeekBar.setSelectedMaxValue(setMax);

        tempMinTextView.setText(getLabel(SensorType.HUMIDITY, setMin, setMax, false));

        frameLayout.addView(rangeSeekBar);
    }

    private String getLabel(SensorType sensorType, float min, float max, boolean isCelsius) {
        String minLabel, maxLabel;
        String defaultLabel = getString(R.string.no_alert);

        switch (sensorType) {
            case TEMPERATURE:
                minLabel = getTempLabel(min, isCelsius);
                maxLabel = getTempLabel(max, isCelsius);
                if (minLabel.equals(defaultLabel) && maxLabel.equals(defaultLabel))
                    return getString(R.string.temperature_range_single, defaultLabel);
                return getString(R.string.temperature_range, minLabel, maxLabel);
            case HUMIDITY:
                minLabel = getHumidityLabel(min);
                maxLabel = getHumidityLabel(max);
                if (minLabel.equals(defaultLabel) && maxLabel.equals(defaultLabel))
                    return getString(R.string.humidity_range_single, defaultLabel);
                return getString(R.string.humidity_range, minLabel, maxLabel);
            default:
                return null;
        }
    }

    private String getHumidityLabel(float val) {

        if (val >= 0.01f && val <= 0.99f)
            return ((int) (UserUtils.formatFloatDecimalPlaces(val * 100, 0))) + "%";

        return getString(R.string.no_alert);
    }


    private void setupAirQualityRange(View notificationView, final DeviceSettings deviceSettings) {

        final TextView textView = (TextView) notificationView.findViewById(R.id.air_quality_value);
        final SeekBar airQualitySeekBar = (SeekBar) notificationView.findViewById(R.id.air_quality_seek_bar);
        airQualitySeekBar.setProgress((int) getAirQualityGraphVal(deviceSettings.airQualityThreshold));
        textView.setText(getAirString((int) getAirQualityGraphVal(deviceSettings.airQualityThreshold)));
        airQualitySeekBar.setThumb(getSliderDrawable());
        airQualitySeekBar.setThumbOffset(0);
        airQualitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
                textView.setText(getAirString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int currentProgress = seekBar.getProgress();
                int validSliderValue = getNearestValidSliderValue(currentProgress);
                seekBar.setProgress(validSliderValue);
                textView.setText(getAirString(currentProgress));

                if (segmentedGroup.getCheckedRadioButtonId() == R.id.set_each_btn) {
                    deviceSettings.setAirQualityNotificationPreferences(
                            convertProgressToAirQualityVal(currentProgress));
                } else {
                    for (DeviceSettings deviceSettings1 : deviceSettingses) {
                        deviceSettings1.setAirQualityNotificationPreferences(
                                convertProgressToAirQualityVal(currentProgress));
                    }
                }
                enableRightButton();
            }
        });
    }

    //re-positioning the slider after movement to one of 3 closest positions
    private int getNearestValidSliderValue(int progress) {
        if (progress < 33)
            return SLIDER_MIN;
        if (progress < 66)
            return SLIDER_MID;
        else
            return SLIDER_MAX;
    }

    //getting one of position of the slider based on air quality value
    private float getAirQualityGraphVal(float airQuality) {
        if (airQuality == 0)
            return SLIDER_MIN;
        if (airQuality <= AIR_QUALITY_VERY_ABNORMAL)
            return SLIDER_MAX;
        else
            return SLIDER_MID;
    }

    private float convertProgressToAirQualityVal(float progress) {
        if (progress < 33f)
            return AIR_QUALITY_MIN;
        if (progress < 66f)
            return AIR_QUALITY_ABNORMAL;
        return AIR_QUALITY_VERY_ABNORMAL;
    }

    private String getAirString(int progress) {
        String airSetting;
        if (progress < 33) {
            airSetting = getString(R.string.no_max);
        } else if (progress < 66) {
            airSetting = getString(R.string.abnormal);
        } else {
            airSetting = getString(R.string.very_abnormal);
        }
        return getString(R.string.air_quality_range, airSetting);
    }

    private void getDevicesAndDeviceSettings() {

        Customer customer = CurrentCustomer.getCurrentCustomer();
        if (customer != null)
            needCelsus = customer.celsius;

        devices = DeviceDatabaseService.getActivatedDevicesAtLocation(locationId);
        for (Device device : devices) {

            DeviceSettings deviceSettings = DeviceDatabaseService.getDeviceSettingsForDevice(device.id);
            if (deviceSettings == null) {
                continue;
            }

            if (device.deviceType.id == DeviceType.CANARY_AIO) {
                deviceSettingses.add(deviceSettings);
                originalDeviceSettingses.add(new DeviceSettings(deviceSettings));
            }
        }
    }


    private void enableRightButton() {
        for (int i = 0; i < originalDeviceSettingses.size(); i++) {
            DeviceSettings deviceSettings = deviceSettingses.get(i);
            DeviceSettings oringinalDeviceSettings = originalDeviceSettingses.get(i);

            if (!deviceSettings.areHomeHealthSettingsSame(oringinalDeviceSettings)) {
                fragmentStack.enableRightButton(this, true);
                return;
            }
        }
        fragmentStack.enableRightButton(this, false);

    }

    private boolean allTheSame() {
        if (deviceSettingses.isEmpty()) {
            return true;
        }

        DeviceSettings firstDeviceSettings = deviceSettingses.get(0);
        for (DeviceSettings deviceSettings : deviceSettingses) {
            if (!deviceSettings.areHomeHealthSettingsSame(firstDeviceSettings))
                return false;
        }

        return true;
    }

    private void adjustNotificationHeader() {
        if (allOff()) {
            segmentedGroup.setVisibility(View.GONE);
            return;
        }

        if (deviceSettingses != null && deviceSettingses.size() < 2) {
            segmentedGroup.setVisibility(View.GONE);
        } else
            segmentedGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.setHeaderTitle(R.string.home_health);
        fragmentStack.showRightButton(R.string.save);
        enableRightButton();
    }


    @Override
    public void onRightButtonClick() {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (!baseActivity.hasInternetConnection())
            return;

        for (DeviceSettings deviceSettings : deviceSettingses) {
            DeviceAPIService.changeDeviceSettings(deviceSettings);
        }

        getActivity().onBackPressed();
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_HOME_HEALTH_NOTIFICATION_SETTINGS;
    }

}
