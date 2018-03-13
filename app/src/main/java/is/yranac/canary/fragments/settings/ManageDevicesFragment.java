package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.adapter.SettingsAdapter;
import is.yranac.canary.databinding.FooterManageDevicesBinding;
import is.yranac.canary.databinding.FragmentManageDevicesBinding;
import is.yranac.canary.databinding.HeaderManageDevicesBinding;
import is.yranac.canary.fragments.setup.SelectNewDeviceLocationFragment;
import is.yranac.canary.model.SettingsObject;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.SubscriptionPlanDatabaseService;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ADD_DEVICE_INTENT;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_SETTINGS;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_LOCATION_DEVICE_SETTINGS;

public class ManageDevicesFragment extends SettingsFragment implements View.OnClickListener {
    private static final String LOG_TAG = "EditUserDetailsFragment";

    private int locationId;
    private List<Device> devices;

    private Subscription subscription;
    private FragmentManageDevicesBinding binding;

    public static ManageDevicesFragment newInstance(int locationId) {
        ManageDevicesFragment fragment = new ManageDevicesFragment();

        Bundle args = new Bundle();
        args.putInt(location_id, locationId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManageDevicesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationId = getArguments().getInt(location_id);


        subscription = SubscriptionPlanDatabaseService.getServicePlanForLocation(locationId);

        SettingsAdapter adapter = new SettingsAdapter(
                getActivity(), getSettings(), null);

        binding.listView.setAdapter(adapter);

        binding.listView.setOnItemClickListener(onItemClickListener);

        HeaderManageDevicesBinding headerBinding = HeaderManageDevicesBinding.inflate(getLayoutInflater(savedInstanceState));

        binding.listView.addHeaderView(headerBinding.getRoot(), null, false);

        if (!devices.isEmpty()) {
            FooterManageDevicesBinding footerBinding = FooterManageDevicesBinding.inflate(getLayoutInflater(savedInstanceState));
            footerBinding.modes.setOnClickListener(this);
            footerBinding.notifications.setOnClickListener(this);
            binding.listView.addFooterView(footerBinding.getRoot(), null, false);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        fragmentStack.showLogoutButton(false);
        fragmentStack.showHeader(true);
        fragmentStack.setHeaderTitle(R.string.devices);
        fragmentStack.showRightButton(0);
    }

    private List<SettingsObject> getSettings() {
        List<SettingsObject> settingsArray;

        devices = DeviceDatabaseService.getAllDevicesAtLocation(locationId);


        settingsArray = getUnlimitedDevices();


        return settingsArray;
    }

    private List<SettingsObject> getUnlimitedDevices() {
        List<SettingsObject> settingsArray = new ArrayList<>();

        for (Device device : devices) {
            String prompt = device.name;
            int icon = device.getIcon();
            settingsArray.add(SettingsObject.promptWithImage(
                    prompt,
                    icon, device.isOtaing(), device.failedOTA()));
        }

        if ((subscription != null && subscription.hasMembership) || devices.size() < 4) {
            int icon = R.drawable.ic_add_icon;
            String prompt = getString(R.string.add_canary_device);
            settingsArray.add(SettingsObject.promptWithImage(
                    prompt,
                    icon, false, false));
        }


        return settingsArray;
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            int actualPostion = position - 1;
            if (actualPostion < devices.size()) {
                addFragmentToStack(EditDeviceFragment.newInstance(devices.get(actualPostion).id, devices.size()), Utils.SLIDE_FROM_RIGHT);
            } else if (actualPostion == devices.size()) {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_SETTINGS, ACTION_ADD_DEVICE_INTENT, null, null, 0, 0);
                addFragmentToStack(SelectNewDeviceLocationFragment.newInstance(locationId), Utils.SLIDE_FROM_RIGHT);
            }
        }
    };

    @Override
    public void onRightButtonClick() {

    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_LOCATION_DEVICE_SETTINGS;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modes:
                addFragmentToStack(getInstance(ModeSettingsFragment.class), Utils.SLIDE_FROM_RIGHT);
                break;
            case R.id.notifications:
                addFragmentToStack(getInstance(NotificationSettingsFragment.class), Utils.SLIDE_FROM_RIGHT);
                break;
        }
    }
}

