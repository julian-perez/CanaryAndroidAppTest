package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsFlexConnectivityBinding;
import is.yranac.canary.messages.GetDeviceStatistics;
import is.yranac.canary.messages.GotDeviceStatistics;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.reading.Reading;
import is.yranac.canary.util.JSONUtil;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ZendeskUtil;

import static is.yranac.canary.model.reading.Reading.BATTERY_LEVEL_CRITICAL;
import static is.yranac.canary.model.reading.Reading.BATTERY_LEVEL_HALF;

/**
 * Created by michaelschroeder on 10/23/17.
 */

public class FlexConnectivityInfoFragment extends SettingsFragment implements View.OnClickListener {


    private static final String battery_value = "battery_value";
    private static final String wifi_value = "wifi_value";
    private FragmentSettingsFlexConnectivityBinding binding;

    public static FlexConnectivityInfoFragment newIntance(String deviceId) {

        Bundle bundle = new Bundle();

        bundle.putString(key_deviceJSON, deviceId);

        FlexConnectivityInfoFragment fragment = new FlexConnectivityInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private Device device;
    private Reading wifiReading;
    private Reading batteryReading;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsFlexConnectivityBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        device = JSONUtil.getObject(getArguments().getString(key_deviceJSON), Device.class);

        binding.flexConnectivityBtn.setOnClickListener(this);
        binding.flexBatteryBtn.setOnClickListener(this);
        binding.wifiNotificationSettingsBtn.setOnClickListener(this);
        binding.batteryNotificationSettingsBtn.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        TinyMessageBus.register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        TinyMessageBus.post(new GetDeviceStatistics(device));
        fragmentStack.setHeaderTitle(getString(R.string.device_status, device.name));
        fragmentStack.showRightButton(0);
    }

    @Override
    public void onStop() {
        TinyMessageBus.unregister(this);
        super.onStop();
    }

    @Subscribe
    public void gotDeviceStatistics(GotDeviceStatistics gotDeviceStatistics) {
        if (gotDeviceStatistics.device.id == this.device.id) {
            this.wifiReading = gotDeviceStatistics.wifiReading;
            this.batteryReading = gotDeviceStatistics.batteryReading;

            if (this.batteryReading == null) {
                this.batteryReading = Reading.getReadingForOfflineDevice(Reading.READING_BATTERY);
            }


            if (this.wifiReading == null) {
                this.wifiReading = Reading.getReadingForOfflineDevice(Reading.READING_WIFI);
            }

            setupWifiReading(device, wifiReading);
            setupBatteryReading(device, batteryReading);
        }
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onRightButtonClick() {

    }

    public void setupWifiReading(Device device, Reading wifiReading) {
        Reading.WifiConnectionLevel level = wifiReading.getWifiConnectionLevel();
        if (!device.isOnline) {
            level = Reading.WifiConnectionLevel.MIN;
        }
        switch (level) {
            case MIN:
                binding.wifiIconImageView.setImageResource(R.drawable.ic_wifi_offline_large);
                binding.wifiLevelTextView.setText(R.string.offline);
                binding.wifiLevelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.light_red));
                binding.wifiDescriptionTextView.setText(R.string.wifi_offline_dsc);
                binding.wifiIconBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_red));
                break;
            case ONE_BAR:
                binding.wifiIconImageView.setImageResource(R.drawable.ic_wifi_low_large);
                binding.wifiDescriptionTextView.setText(R.string.wifi_low_dsc);
                binding.wifiLevelTextView.setText(R.string.low);
                binding.wifiLevelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.light_red));
                binding.wifiIconBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_red));
                break;
            case TWO_BARS:
                binding.wifiIconImageView.setImageResource(R.drawable.ic_wifi_med_large);
                binding.wifiLevelTextView.setText(R.string.medium);
                binding.wifiDescriptionTextView.setText(R.string.wifi_med_dsc);
                binding.wifiLevelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_moderate_cyan));
                binding.wifiIconBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_moderate_cyan));
                break;
            case FULL:
                binding.wifiIconImageView.setImageResource(R.drawable.ic_wifi_high_large);
                binding.wifiLevelTextView.setText(R.string.high);
                binding.wifiDescriptionTextView.setText(R.string.wifi_high_dsc);
                binding.wifiLevelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_moderate_cyan));
                binding.wifiIconBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_moderate_cyan));
                break;
        }

    }

    private void setupBatteryReading(Device device, Reading batteryReading) {
 binding.statisticsBatteryView.updateBatteryState(batteryReading, device);
        binding.batteryLevelTextView.setText(batteryReading.getBatteryStatus());
        binding.batteryLevelTextView.setVisibility(View.VISIBLE);

        Reading.BatteryState state = batteryReading.getBatteryState();
        if (!device.isOnline) {
            state = Reading.BatteryState.OFFLINE;
        }
        switch (state) {
            case CHARGHING:
                binding.batteryLevelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_moderate_cyan));
                binding.batteryIconBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_moderate_cyan));
                if (batteryReading.value >= 100) {
                    binding.batteryDescriptionTextView.setText(R.string.battery_full_dsc);
                } else {
                    binding.batteryDescriptionTextView.setText(R.string.battery_charging_dsc);
                }
                break;
            case DISCHARGING:
                if (batteryReading.value <= BATTERY_LEVEL_CRITICAL) {
                    binding.batteryLevelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.light_red));
                    binding.batteryIconBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_red));
                    binding.batteryDescriptionTextView.setText(R.string.battery_level_critical_dsc);
                } else if (batteryReading.value <= BATTERY_LEVEL_HALF) {
                    binding.batteryLevelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_moderate_cyan));
                    binding.batteryIconBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_moderate_cyan));
                    binding.batteryDescriptionTextView.setText(R.string.battery_level_low_dsc);
                } else {
                    binding.batteryLevelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_moderate_cyan));
                    binding.batteryIconBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_moderate_cyan));
                    binding.batteryDescriptionTextView.setText(R.string.battery_level_good_dsc);
                }
                break;
            case ISSUE:
                binding.batteryLevelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.light_red));
                binding.batteryIconBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_red));
                binding.batteryLevelTextView.setVisibility(View.GONE);
                binding.batteryLevelLabelTextView.setText(R.string.battery_not_charging);
                binding.batteryDescriptionTextView.setText(R.string.battery_not_charging_dsc);

                break;
            case OFFLINE:
                binding.batteryLevelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.light_red));
                binding.batteryIconBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_red));
                binding.batteryLevelTextView.setVisibility(View.GONE);
                binding.batteryLevelLabelTextView.setText(R.string.device_is_offline);
                binding.batteryDescriptionTextView.setText(R.string.battery_offline_dsc);

                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flex_connectivity_btn:
                String url = getString(R.string.connectivity_url);
                ZendeskUtil.loadHelpCenter(getContext(), url);
                break;
            case R.id.flex_battery_btn:

                String url2 = getString(R.string.impove_battery_life_url);
                ZendeskUtil.loadHelpCenter(getContext(), url2);
                break;
            case R.id.battery_notification_settings_btn:
                RecordingRangeFragment fragment = RecordingRangeFragment.newInstance(device.id);
                addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
                break;
            case R.id.wifi_notification_settings_btn:
                ConnectivityNotificationSettingsFragment fragment2 = ConnectivityNotificationSettingsFragment.newInstance(device.getLocationId());
                addFragmentToStack(fragment2, Utils.SLIDE_FROM_RIGHT);
                break;
        }
    }
}
