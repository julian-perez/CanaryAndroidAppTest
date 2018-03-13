package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsNotifcationsBinding;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_NOTIFICATION_SETTINGS;

/**
 * Created by Schroeder on 9/1/15.
 */
public class NotificationSettingsFragment extends SettingsFragment implements View.OnClickListener {

    private int locationId;

    private FragmentSettingsNotifcationsBinding binding;
    private List<Device> locationDevices = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsNotifcationsBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationId = getArguments().getInt(location_id);

        binding.motionNotificationsLayout.setOnClickListener(this);
        binding.homeHealthNotificationsLayout.setOnClickListener(this);
        binding.connectionNotificationsLayout.setOnClickListener(this);
        binding.powerNotificationsLayout.setOnClickListener(this);
        binding.presenceNotificationsLayout.setOnClickListener(this);

    }


    private void checkSettings() {

        if (!needToShowHomeHealth()) {
            binding.homeHealthNotificationsLayout.setVisibility(View.GONE);
        } else {
            binding.homeHealthNotificationsLayout.setVisibility(View.VISIBLE);
        }

        if (!needToShowBatterySettings()) {
            binding.powerNotificationsLayout.setVisibility(View.GONE);
        } else {
            binding.powerNotificationsLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.enableRightButton(this, false);
        fragmentStack.showRightButton(0);
        fragmentStack.showHeader(true);
        fragmentStack.setHeaderTitle(R.string.notifications);
        getDevicesAndDeviceSettings();

        checkSettings();
    }

    @Override
    public void onRightButtonClick() {

    }

    private boolean needToShowBatterySettings() {
        for (Device device : locationDevices) {
            if (device.getDeviceType() == DeviceType.FLEX) {
                return true;
            }
        }

        return false;
    }

    private boolean needToShowHomeHealth() {

        for (Device deviceWithSetting : locationDevices) {

            if (deviceWithSetting.getDeviceType() == DeviceType.CANARY_AIO) {
                return true;
            }
        }

        return false;

    }

    private void getDevicesAndDeviceSettings() {
        locationDevices = DeviceDatabaseService.getActivatedDevicesAtLocation(locationId);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.motion_notifications_layout:
                addFragmentToStack(getInstance(MotionNotificationSettingsFragment.class), Utils.SLIDE_FROM_RIGHT);
                break;

            case R.id.home_health_notifications_layout:
                addFragmentToStack(getInstance(HomeHealthNotificationSettingsFragment.class), Utils.SLIDE_FROM_RIGHT);
                break;

            case R.id.connection_notifications_layout:
                addFragmentToStack(getInstance(ConnectivityNotificationSettingsFragment.class), Utils.SLIDE_FROM_RIGHT);
                break;

            case R.id.power_notifications_layout:
                addFragmentToStack(getInstance(PowerAndBatteryNotificationFragment.class), Utils.SLIDE_FROM_RIGHT);
                break;
            case R.id.presence_notifications_layout:
                addFragmentToStack(getInstance(PresenceNotificationFragment.class), Utils.SLIDE_FROM_RIGHT);

                break;
        }
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_NOTIFICATION_SETTINGS;
    }

}
