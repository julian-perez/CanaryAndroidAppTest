package is.yranac.canary.fragments.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.adapter.SettingsSingleSelectionAdapter;
import is.yranac.canary.databinding.FragmentPowerBatteryNotifBinding;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceSettings;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.database.DeviceDatabaseService;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_POWER_AND_BATTERY_NOTIFICATIONS;

/**
 * Created by sergeymorozov on 5/17/16.
 */
public class PowerAndBatteryNotificationFragment extends SettingsFragment {

    private List<DeviceSettings> deviceSettings = new ArrayList<>();
    private final List<DeviceSettings> originalDeviceSettings = new ArrayList<>();
    private String[] deviceNames;
    private boolean powerSourceNotificationsOn;
    private boolean fullBatteryNotificationsOn;


    private FragmentPowerBatteryNotifBinding binding;
    private List<Device> devices;
    private int locationId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_power_battery_notif, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        loadData();

        binding.listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deviceSettings.get(position).sendConnectivityNotifications = true;
                updateSaveButton();
            }
        });

        deviceNames = getDeviceNames();
        SettingsSingleSelectionAdapter adapter = new SettingsSingleSelectionAdapter(getActivity(), deviceNames, R.layout.setting_row_checkmark);
        binding.listView.setAdapter(adapter);

        binding.powerSourceCheckBox.setChecked(powerSourceNotificationsOn);

        if (powerSourceNotificationsOn) {
            binding.powerSourceBody.setVisibility(View.VISIBLE);
            for (int i = 0; i < deviceSettings.size(); i++) {
                DeviceSettings settings = deviceSettings.get(i);
                binding.listView.setItemChecked(i, settings.sendPowerSourceNotifications);
            }
        } else
            binding.powerSourceBody.setVisibility(View.GONE);


        binding.powerSourceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adjustPowerSourceNotificationPrefs(isChecked);

            }
        });
        binding.powerSourceHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.powerSourceCheckBox.toggle();
            }
        });

        binding.fullBatteryCheckBox.setChecked(fullBatteryNotificationsOn);
        binding.batteryFullHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.fullBatteryCheckBox.setChecked(!binding.fullBatteryCheckBox.isChecked());
                for (DeviceSettings deviceSettings : PowerAndBatteryNotificationFragment.this.deviceSettings) {
                    deviceSettings.sendBatteryFullNotifications = binding.fullBatteryCheckBox.isChecked();
                }
                updateSaveButton();
            }
        });

        if (powerSourceNotificationsOn && deviceSettings.size() > 1) {
            binding.powerSourceBody.setVisibility(View.VISIBLE);
        } else
            binding.powerSourceBody.setVisibility(View.GONE);
    }

    /**
     * Return the Screen tag for google Analytics
     *
     * @return Null if no tagging otherwise the corresponding tag
     */
    @Override
    public String getAnalyticsTag() {
        return SCREEN_POWER_AND_BATTERY_NOTIFICATIONS;
    }

    /**
     * Function that is called when the right button is click in the header
     */
    @Override
    public void onRightButtonClick() {

        getActivity().onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();

        fragmentStack.setHeaderTitle(R.string.power_source_fragment_header);
        fragmentStack.resetButtonStyle();
        updateSaveButton();
        fragmentStack.enableRightButton(this, false);

    }

    @Override
    public void onPause() {
        super.onPause();

        for (DeviceSettings deviceSettings : deviceSettings){
            DeviceAPIService.changeDeviceSettings(deviceSettings);
        }
    }

    private void loadData() {


        locationId = getArguments().getInt(location_id);

        getDevicesAndDeviceSettings();

        powerSourceNotificationsOn = false;
        fullBatteryNotificationsOn = false;

        for (DeviceSettings deviceSettings : this.deviceSettings) {

            originalDeviceSettings.add(new DeviceSettings(deviceSettings));

            if (!powerSourceNotificationsOn)
                powerSourceNotificationsOn = deviceSettings.sendPowerSourceNotifications;
            if (!fullBatteryNotificationsOn)
                fullBatteryNotificationsOn = deviceSettings.sendBatteryFullNotifications;
        }
    }

    private void getDevicesAndDeviceSettings() {


        devices = DeviceDatabaseService.getActivatedDevicesAtLocation(locationId);
        for (Device device : devices) {

            DeviceSettings deviceSettings = DeviceDatabaseService.getDeviceSettingsForDevice(device.id);
            if (deviceSettings == null) {
                continue;
            }

            if (device.deviceType.id == DeviceType.FLEX) {
                this.deviceSettings.add(deviceSettings);
                originalDeviceSettings.add(new DeviceSettings(deviceSettings));
            }
        }
    }


    private boolean hasDataChanged() {
        for (int i = 0; i < deviceSettings.size(); i++) {
            DeviceSettings deviceWithSetting = deviceSettings.get(i);
            DeviceSettings oringinalDeviceSettings = originalDeviceSettings.get(i);

            if (!deviceWithSetting.equals(oringinalDeviceSettings)) {
                return true;
            }
        }
        return false;
    }

    private void updateSaveButton() {
        fragmentStack.enableRightButton(this, hasDataChanged());
    }


    private void adjustPowerSourceNotificationPrefs(boolean sendPowerSourceNotifications) {

        if (sendPowerSourceNotifications && deviceSettings.size() > 1) {
            binding.powerSourceBody.setVisibility(View.VISIBLE);
        } else
            binding.powerSourceBody.setVisibility(View.GONE);

        int i = 0;
        for (DeviceSettings settings : deviceSettings) {
            settings.sendPowerSourceNotifications = sendPowerSourceNotifications;
            binding.listView.setItemChecked(i, settings.sendPowerSourceNotifications);
            i++;
        }
    }

    private String[] getDeviceNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Device device : devices) {
            if (device.hasBattery())
                names.add(device.name);
        }
        return Arrays.copyOf(names.toArray(), names.size(), String[].class);
    }
}
