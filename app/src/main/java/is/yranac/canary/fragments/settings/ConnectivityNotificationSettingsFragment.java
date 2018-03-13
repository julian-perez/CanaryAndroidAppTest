package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.messages.ResetDeviceSettings;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceSettings;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.ui.views.CustomCheckedView;
import is.yranac.canary.util.AnimationHelper;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_CONNECTIVITY_NOTIFICATION_SETTINGS;

/**
 * Created by Schroeder on 9/1/15.
 */

public class ConnectivityNotificationSettingsFragment extends SettingsFragment {

    private int locationId;
    private List<Device> devices;
    private List<DeviceSettings> deviceSettingses = new ArrayList<>();
    private List<DeviceSettings> originalDeviceSettingses = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private LinearLayout linearLayout;
    private LinearLayout notificationBodyLayout;
    private SwitchCompat toggle;


    public static ConnectivityNotificationSettingsFragment newInstance(int locationId){
        Bundle bundle = new Bundle();
        bundle.putInt(location_id, locationId);

        ConnectivityNotificationSettingsFragment fragment = new ConnectivityNotificationSettingsFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connectivity_notificaions_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationId = getArguments().getInt(location_id);

        linearLayout = (LinearLayout) view.findViewById(R.id.mode_settings_options);
        notificationBodyLayout = (LinearLayout) view.findViewById(R.id.device_list);
        layoutInflater = LayoutInflater.from(getActivity());

        getDevicesAndDeviceSettings();

        resetDeviceSettings();

        final View topLayout = view.findViewById(R.id.connectivity_settings_toggle_layout);
        toggle = (SwitchCompat) view.findViewById(R.id.mode_setting_checkbox);
        toggle.setChecked(!allOff());
        topLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle.toggle();

                for (DeviceSettings deviceSettings : deviceSettingses) {
                    deviceSettings.sendConnectivityNotifications = toggle.isChecked();
                }

                if (devices.size() > 1) {

                    if (toggle.isChecked()) {
                        resetDeviceSettings();
                        AnimationHelper.expandHeight(notificationBodyLayout, getActivity(), 250);
                    } else {
                        AnimationHelper.collapseHeight(notificationBodyLayout, getActivity(), 250);
                        TinyMessageBus.postDelayed(new ResetDeviceSettings(), 250);

                    }

                }

                enableRightButton();
            }
        });

        if (!toggle.isChecked()) {
            notificationBodyLayout.setVisibility(View.GONE);
            linearLayout.removeAllViews();
        }

        if (deviceSettingses.size() < 2){
            notificationBodyLayout.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onResetDeviceSettings(ResetDeviceSettings resetDeviceSettings) {
        resetDeviceSettings();
    }

    private boolean allOff() {
        for (DeviceSettings deviceSettings : deviceSettingses) {
            if (deviceSettings.sendConnectivityNotifications)
                return false;
        }

        return true;
    }

    private void resetDeviceSettings() {

        if (devices.size() > 1) {
            linearLayout.removeAllViews();

            for (Device device : devices) {
                DeviceSettings deviceSettings = null;

                for (DeviceSettings deviceSettings1 : deviceSettingses) {
                    if (device.id == Utils.getIntFromResourceUri(deviceSettings1.resourceUri)) {
                        deviceSettings = deviceSettings1;
                        break;
                    }
                }

                if (deviceSettings != null) {
                    setConnectionView(device, deviceSettings);
                }
            }
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }


    private void setConnectionView(Device device, final DeviceSettings deviceSettings) {


        final CustomCheckedView connectivityDeviceView = (CustomCheckedView) layoutInflater.inflate(R.layout.setting_row_checkmark, (ViewGroup) getView(), false);
        connectivityDeviceView.getTitle().setText(device.name);

        linearLayout.addView(connectivityDeviceView);

        connectivityDeviceView.setChecked(deviceSettings.sendConnectivityNotifications);

        connectivityDeviceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceSettings.sendConnectivityNotifications = !deviceSettings.sendConnectivityNotifications;
                connectivityDeviceView.setChecked(deviceSettings.sendConnectivityNotifications);
                enableRightButton();
            }
        });

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
    }


    private void enableRightButton() {
        for (int i = 0; i < originalDeviceSettingses.size(); i++) {
            DeviceSettings deviceSettings = deviceSettingses.get(i);
            DeviceSettings oringinalDeviceSettings = originalDeviceSettingses.get(i);

            if (oringinalDeviceSettings.sendConnectivityNotifications != deviceSettings.sendConnectivityNotifications) {
                fragmentStack.enableRightButton(this, true);
                return;
            }
        }
        fragmentStack.enableRightButton(this, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.setHeaderTitle(R.string.connectivity_header);
        fragmentStack.resetButtonStyle();
        fragmentStack.showRightButton(R.string.save);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        TinyMessageBus
                .register(this);
    }

    @Override
    public void onStop() {
        TinyMessageBus
                .unregister(this);
        super.onStop();
    }

    @Override
    public void onRightButtonClick() {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (!baseActivity.hasInternetConnection())
            return;
        for (DeviceSettings deviceSettings : deviceSettingses) {

            if (!toggle.isChecked()) {
                deviceSettings.sendConnectivityNotifications = false;
            }
            DeviceAPIService.changeDeviceSettings(deviceSettings);

        }
        getActivity().onBackPressed();
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_CONNECTIVITY_NOTIFICATION_SETTINGS;
    }
}
