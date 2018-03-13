package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentMotionNotificaionsSettingsBinding;
import is.yranac.canary.databinding.ListviewDeviceMotionSettingsBinding;
import is.yranac.canary.fragments.setup.GetHelpFragment;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceSettings;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.ui.views.TickSeekBar;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.MathUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_MOTION_NOTIFICATION_SETTINGS;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_SENSITIVITY;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_TYPE_DEVICE;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_TYPE_LOCATION;

/**
 * Created by Schroeder on 9/1/15.
 */
public class MotionNotificationSettingsFragment extends SettingsFragment {

    private static final String LOG_TAG = "MotionNotificationSettingsFragment";
    private int locationId;


    private List<Device> devices;
    private List<DeviceSettings> deviceSettingses = new ArrayList<>();
    private List<DeviceSettings> originalDeviceSettingses = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private FragmentMotionNotificaionsSettingsBinding binding;

    public static MotionNotificationSettingsFragment newInstance(int locationId) {
        MotionNotificationSettingsFragment fragment = new MotionNotificationSettingsFragment();
        Bundle args = new Bundle();
        args.putInt("location", locationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMotionNotificaionsSettingsBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.segmentedLayout.segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
            }
        });

        locationId = getArguments().getInt(location_id);
        layoutInflater = LayoutInflater.from(getContext());

        getDevicesAndDeviceSettings();

        if (devices.size() == 1) {
            setupSetEachSettings();
            binding.segmentedLayout.segmentedGroup.setVisibility(View.GONE);
        } else if (allTheSame()) {
            binding.segmentedLayout.segmentedGroup.check(R.id.set_all_btn);
        } else {
            binding.segmentedLayout.segmentedGroup.check(R.id.set_each_btn);
        }

    }

    private void setupSetEachSettings() {
        binding.notificationSettingsLayout.removeAllViews();
        for (Device device : devices) {


            DeviceSettings deviceSettings = null;

            for (DeviceSettings deviceSettings1 : deviceSettingses) {
                if (device.id == Utils.getIntFromResourceUri(deviceSettings1.resourceUri)) {
                    deviceSettings = deviceSettings1;
                    break;
                }
            }

            if (deviceSettings != null) {
                setUpThresholdView(deviceSettings, device);
            }

        }
    }


    private void setupSetAllSettings() {
        binding.notificationSettingsLayout.removeAllViews();
        if (deviceSettingses.isEmpty())
            return;

        DeviceSettings firstDeviceSettings = deviceSettingses.get(0);

        for (DeviceSettings deviceSettings : deviceSettingses) {
            deviceSettings.detectionThreshold = firstDeviceSettings.detectionThreshold;
        }

        setUpThresholdView(firstDeviceSettings, null);

    }

    private void setUpThresholdView(final DeviceSettings deviceSettings, final Device device) {
        final ListviewDeviceMotionSettingsBinding binding = ListviewDeviceMotionSettingsBinding.inflate(layoutInflater);
        if (device == null) {
            binding.disarmTextView.setVisibility(View.GONE);
        } else {
            binding.disarmTextView.setText(device.name);
        }

        final TickSeekBar seekBar = binding.notificationProgressBar;
        final int value = seekBar.getMax() - (int) (deviceSettings.detectionThreshold * seekBar.getMax());


        for (DeviceSettings settings : originalDeviceSettingses) {

            if (settings.getCorrespondingDeviceId() == deviceSettings.getCorrespondingDeviceId()) {
                seekBar.setInitialValue((int) (settings.detectionThreshold * 10));
            }
        }
        final int offset = DensityUtil.dip2px(getContext(), 2);
        binding.notificationProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                float value = (((seekBar.getMax() - progress) / (float) seekBar.getMax()));
                float rounded = MathUtils.round(value, 0.1f);
                if (fromUser) {

                    if (device == null) {

                        for (DeviceSettings deviceSettings1 : deviceSettingses) {
                            deviceSettings1.detectionThreshold = rounded;
                        }

                    } else {
                        deviceSettings.detectionThreshold = rounded;
                    }

                    if (progress < 4)
                        progress = 4;

                    if (progress > 96)
                        progress = 96;
                    seekBar.setProgress(progress);

                }
                binding.sliderValue.setText(String.format(Locale.ENGLISH, "%.0f", (10 - (rounded * 10))));
                float centerX = seekBar.getThumb().getBounds().exactCenterX() - binding.sliderValue.getWidth() / 2;
                centerX -= offset;
                binding.sliderValue.setX(centerX);

                TickSeekBar tickSeekBar = (TickSeekBar) seekBar;

                float initialValue = tickSeekBar.getInitialValue() / 10.0f;

                TextView textView = binding.motionNotificationDscTextView;
                if (rounded == initialValue) {
                    textView.setText(R.string.your_current_setting);
                } else if (rounded > initialValue) {
                    textView.setText(R.string.you_will_receive_fewer_notifications);
                } else if (rounded < initialValue) {
                    textView.setText(R.string.you_will_receive_more_notifications);
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress(getLocationOnSeekBar(seekBar.getProgress()));
                saveValues();

            }
        });
        binding.sliderValue.setText(String.valueOf(value));


        binding.notificationProgressBar.setProgress(getLocationOnSeekBar(value));
        final ViewTreeObserver viewTreeObserver =
                binding.notificationProgressBar.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                seekBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float centerX = seekBar.getThumb().getBounds().exactCenterX() - binding.sliderValue.getWidth() / 2;
                centerX -= offset;
                binding.sliderValue.setX(centerX);
            }
        });

        this.binding.notificationSettingsLayout.addView(binding.getRoot());
    }


    private int getLocationOnSeekBar(int value) {
        int rounded = (int) MathUtils.round(value, 10.0f);
        switch (rounded) {
            case 0:
                return 4;
            case 10:
                return 13;
            case 20:
                return 22;
            case 30:
                return 30;
            case 40:
                return 41;
            case 60:
                return 59;
            case 70:
                return 68;
            case 80:
                return 76;
            case 90:
                return 87;
            case 100:
                return 96;

        }
        return rounded;
    }

    private void getDevicesAndDeviceSettings() {

        devices = DeviceDatabaseService.getActivatedDevicesAtLocation(locationId);
        for (Device device : devices) {

            DeviceSettings deviceSettings = DeviceDatabaseService.getDeviceSettingsForDevice(device.id);
            if (deviceSettings == null) {
                continue;
            }

            deviceSettingses.add(deviceSettings);
            originalDeviceSettingses.add(new DeviceSettings(deviceSettings));

        }
        Log.i(LOG_TAG, String.valueOf(deviceSettingses.size()));
    }

    private boolean allTheSame() {
        if (deviceSettingses.isEmpty()) {
            return true;
        }

        DeviceSettings firstDeviceSettings = deviceSettingses.get(0);

        for (DeviceSettings deviceSettings : deviceSettingses) {

            if (!deviceSettings.equals(firstDeviceSettings)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.setHeaderTitle(R.string.motion_notifications_header);
        fragmentStack.showRightButton(R.string.question_mark);
        fragmentStack.rightButtonBackground(R.drawable.white_circle_gray_ring);
        fragmentStack.setButtonsWidth(DensityUtil.dip2px(getContext(), 25));
        fragmentStack.setButtonsHeight(DensityUtil.dip2px(getContext(), 25));
        fragmentStack.rightButtonTextColor(R.color.dark_gray);
        fragmentStack.enableRightButton(this, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentStack.resetButtonStyle();
    }

    @Override
    public void onRightButtonClick() {
        GetHelpFragment fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_MOTION);
        addModalFragment(fragment);
    }

    private void saveValues() {
        String settingsType;

        int customerId = 0;

        Customer customer = CurrentCustomer.getCurrentCustomer();

        if (customer != null) {
            customerId = customer.id;
        }

        if (allTheSame()) {
            settingsType = SETTINGS_TYPE_LOCATION;
        } else {
            settingsType = SETTINGS_TYPE_DEVICE;
        }
        for (DeviceSettings deviceSettings : deviceSettingses) {
            DeviceAPIService.changeDeviceSettings(deviceSettings);
            if (!allTheSame()) {
                String uuid = "";

                for (Device device : devices) {
                    int id = Utils.getIntFromResourceUri(deviceSettings.resourceUri);
                    if (device.id == id) {
                        uuid = device.uuid;
                        break;
                    }
                }

                String oldValue = "0";
                for (DeviceSettings settings : originalDeviceSettingses) {
                    if (settings.resourceUri.equalsIgnoreCase(deviceSettings.resourceUri)) {
                        oldValue = String.valueOf(settings.detectionThreshold);
                        break;
                    }
                }
                String newValue = String.valueOf(deviceSettings.detectionThreshold);
                GoogleAnalyticsHelper.trackSettingsEvent(SETTINGS_SENSITIVITY, settingsType, customerId,
                        uuid, 0, oldValue, newValue);
            }
        }


        if (allTheSame()) {
            String oldValue = String.valueOf(originalDeviceSettingses.get(0).detectionThreshold);
            String newValue = String.valueOf(deviceSettingses.get(0).detectionThreshold);
            GoogleAnalyticsHelper.trackSettingsEvent(SETTINGS_SENSITIVITY, settingsType, customerId,
                    null, locationId, oldValue, newValue);
        }
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_MOTION_NOTIFICATION_SETTINGS;
    }

}
