package is.yranac.canary.fragments.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsHomeModeBinding;
import is.yranac.canary.databinding.ListviewDeviceModeSettingsBinding;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceSettings;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.location.NightModeSchedule;
import is.yranac.canary.model.mode.Mode;
import is.yranac.canary.model.mode.ModeCache;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.api.LocationAPIService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.ui.SettingsFragmentStackActivity;
import is.yranac.canary.util.NightModeUtil;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.cache.location.GetLocation;
import is.yranac.canary.util.cache.location.GotLocationData;
import is.yranac.canary.util.ga.AnalyticsConstants;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_HOME_MODES;
import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_NIGHT_MODES;
import static is.yranac.canary.model.mode.ModeCache.armed;
import static is.yranac.canary.model.mode.ModeCache.disarmed;
import static is.yranac.canary.model.mode.ModeCache.privacy;
import static is.yranac.canary.model.mode.ModeCache.standby;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.extra_locationId;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.home_mode_settings_upsell;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.modal;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_HOME_MODE_SETTINGS_CTA_OPEN;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MEMBERSHIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_NIGHT_MODE_ENABLED;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_NIGHT_MODE_TIME_END;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_NIGHT_MODE_TIME_START;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_TYPE_LOCATION;

/**
 * Created by Schroeder on 4/13/16.
 */
public class ConfigureModeSettingsFragment extends SettingsFragment implements View.OnClickListener {


    private static final String LOG_TAG = "ConfigureModeSettingsFragment";

    private int locationId;
    private LayoutInflater layoutInflater;
    private List<Device> devices;
    private List<DeviceSettings> deviceSettingses = new ArrayList<>();
    private List<DeviceSettings> originalDeviceSettingses = new ArrayList<>();

    private boolean isHomeMode;
    private Location location;

    private Subscription subscription;

    private static final String is_home_mode = "isHomeMode";

    public static ConfigureModeSettingsFragment newInstance(int locationId, boolean isHomeMode) {
        ConfigureModeSettingsFragment fragment = new ConfigureModeSettingsFragment();
        Bundle args = new Bundle();
        args.putBoolean(is_home_mode, isHomeMode);
        args.putInt(location_id, locationId);
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentSettingsHomeModeBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsHomeModeBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationId = getArguments().getInt(location_id);
        location = LocationDatabaseService.getLocationFromId(locationId);
        isHomeMode = getArguments().getBoolean(is_home_mode);
        originalNightMode = LocationDatabaseService.getNightModeForLocation(locationId);


        if (isHomeMode) {
            binding.nightModeScheduleLayout.setVisibility(View.GONE);
            binding.modeDscTextView.setText(R.string.home_mode_dsc);
        } else {
            binding.nightModeScheduleLayout.setVisibility(View.VISIBLE);
            setUpNightModeSchdule();
            binding.modeDscTextView.setText(R.string.night_mode_dsc);

        }

        binding.setAllLayout.setOnClickListener(this);
        binding.setEachLayout.setOnClickListener(this);

        layoutInflater = LayoutInflater.from(getContext());

        getDevicesAndDeviceSettings();

        if (devices.size() == 1) {
            setupSetAllSettings();
            binding.segmentedGroup.setVisibility(View.GONE);
        } else {
            binding.segmentedGroup.setVisibility(View.VISIBLE);
            if (allTheSame()) {
                setupSetAllSettings();
            } else {
                setupSetEachSettings();
            }

        }

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                View v;

                if (allTheSame()) {
                    v = binding.setAllLayout;
                } else {
                    v = binding.setEachLayout;
                }


                float setX = v.getX() + (v.getWidth() / 2) - (binding.selectedLevel.getWidth() / 2);

                binding.selectedLevel.animate().translationX(setX).start();
            }
        });

    }

    private NightModeSchedule originalNightMode;
    private NightModeSchedule nightMode;

    private void setUpNightModeSchdule() {

        // Set a night mode if one does not exist
        nightMode = new NightModeSchedule();
        if (originalNightMode == null) {
            nightMode = NightModeSchedule.createNew();
        } else {
            nightMode.id = originalNightMode.id;
            nightMode.startTime = originalNightMode.startTime;
            nightMode.endTime = originalNightMode.endTime;
        }


        binding.nightModeStartTime.setText(nightMode.localStartTime());
        binding.nightModeEndTime.setText(nightMode.localEndTime());


        binding.nightModeToggleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.nightModeScheduleToggle.toggle();
                if (binding.nightModeScheduleToggle.isChecked()) {
                    binding.nightModeTimeLayout.setVisibility(View.VISIBLE);

                } else {
                    binding.nightModeTimeLayout.setVisibility(View.GONE);

                }
            }
        });


        binding.startNightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NightModeUtil.setUpNightModeDialog(nightMode.startTime, getContext(), new NightModeUtil.NightModeCallback() {
                    @Override
                    public void onComplete(String date) {
                        nightMode.startTime = date;
                        binding.nightModeStartTime.setText(nightMode.localStartTime());
                    }
                });


            }
        });
        binding.endNightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NightModeUtil.setUpNightModeDialog(nightMode.endTime, getContext(), getString(R.string.select_a_end_time), new NightModeUtil.NightModeCallback() {
                    @Override
                    public void onComplete(String date) {
                        nightMode.endTime = date;
                        binding.nightModeEndTime.setText(nightMode.localEndTime());
                    }
                });

            }
        });

        binding.nightModeScheduleToggle.setChecked(location.nightModeEnabled);

        if (location.nightModeEnabled) {
            binding.nightModeTimeLayout.setVisibility(View.VISIBLE);
        } else {
            binding.nightModeTimeLayout.setVisibility(View.GONE);
        }
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

    private void setupSetEachSettings() {
        binding.notificationSettingsLayout.removeAllViews();
        deviceSettingses.clear();

        for (DeviceSettings deviceSettings : originalDeviceSettingses) {
            deviceSettingses.add(new DeviceSettings(deviceSettings));

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
        binding.notificationSettingsLayout.removeAllViews();

        if (deviceSettingses.isEmpty())
            return;

        DeviceSettings firstDeviceSettings = deviceSettingses.get(0);

        for (DeviceSettings deviceSettings : deviceSettingses) {
            deviceSettings.homeMode = firstDeviceSettings.homeMode;
            deviceSettings.nightMode = firstDeviceSettings.nightMode;
        }
        setUpNotificationView(firstDeviceSettings, null);

    }

    private void setUpNotificationView(final DeviceSettings deviceSettings, final Device device) {
        final ListviewDeviceModeSettingsBinding modeBinding = ListviewDeviceModeSettingsBinding.inflate(layoutInflater,
                binding.notificationSettingsLayout, false);


        if (device == null) {
            modeBinding.disarmTextView.setText(R.string.all_devices);
        } else {
            modeBinding.disarmTextView.setText(device.name);
        }


        if (isHomeMode) {
            modeBinding.inModeTextView.setText(R.string.in_home_mode);
        } else {
            modeBinding.inModeTextView.setText(R.string.in_night_mode);

        }


        modeBinding.recordModeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showUpsell()) {
                    return;
                }
                modeBinding.radioRecord.setChecked(true);
                modeBinding.radioPrivate.setChecked(false);
                modeBinding.notificationModeLayout.setVisibility(View.VISIBLE);
                modeBinding.watchLiveModeLayout.setVisibility(View.GONE);

                Mode newMode = modeBinding.notificationToggle.isChecked() ? ModeCache.getMode(armed) : ModeCache.getMode(disarmed);

                setDeviceMode(device, deviceSettings, newMode);

            }
        });

        modeBinding.notificationToggle.setChecked(true);
        modeBinding.notificationModeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeBinding.notificationToggle.toggle();

                Mode newMode = modeBinding.notificationToggle.isChecked() ? ModeCache.getMode(armed) : ModeCache.getMode(disarmed);

                setDeviceMode(device, deviceSettings, newMode);
            }
        });


        modeBinding.privateModeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeBinding.radioRecord.setChecked(false);
                modeBinding.radioPrivate.setChecked(true);
                modeBinding.notificationModeLayout.setVisibility(View.GONE);

                if (device != null && device.hasStandbyMode()) {
                    modeBinding.watchLiveModeLayout.setVisibility(View.VISIBLE);
                } else if (allDevicesHaveStandbyMode()) {
                    modeBinding.watchLiveModeLayout.setVisibility(View.VISIBLE);
                } else {
                    modeBinding.watchLiveModeLayout.setVisibility(View.GONE);
                }

                Mode newMode = modeBinding.watchLiveToggle.isChecked() ? ModeCache.getMode(standby) : ModeCache.getMode(privacy);
                setDeviceMode(device, deviceSettings, newMode);
            }
        });

        modeBinding.watchLiveModeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeBinding.watchLiveToggle.toggle();
                Mode newMode = modeBinding.watchLiveToggle.isChecked() ? ModeCache.getMode(standby) : ModeCache.getMode(privacy);
                setDeviceMode(device, deviceSettings, newMode);
            }
        });

        Mode mode;
        if (isHomeMode) {
            mode = deviceSettings.homeMode;
        } else {
            mode = deviceSettings.nightMode;
        }

        if (mode != null) {
            if (mode.id == ModeCache.getMode(privacy).id || mode.id == ModeCache.getMode(standby).id) {
                modeBinding.radioRecord.setChecked(false);
                modeBinding.radioPrivate.setChecked(true);
                modeBinding.notificationModeLayout.setVisibility(View.GONE);
                if (device != null && device.hasStandbyMode()) {
                    modeBinding.watchLiveModeLayout.setVisibility(View.VISIBLE);
                } else if (allDevicesHaveStandbyMode()) {
                    modeBinding.watchLiveModeLayout.setVisibility(View.VISIBLE);
                } else {
                    modeBinding.watchLiveModeLayout.setVisibility(View.GONE);
                }
                modeBinding.notificationToggle.setChecked(false);
                modeBinding.watchLiveToggle.setChecked(mode.id == ModeCache.getMode(standby).id);
            } else {
                modeBinding.radioRecord.setChecked(true);
                modeBinding.radioPrivate.setChecked(false);
                modeBinding.notificationModeLayout.setVisibility(View.VISIBLE);
                modeBinding.watchLiveModeLayout.setVisibility(View.GONE);
                modeBinding.notificationToggle.setChecked(mode.id == ModeCache.getMode(armed).id);
                modeBinding.watchLiveToggle.setChecked(false);
            }
        }


        binding.notificationSettingsLayout.addView(modeBinding.getRoot());
    }

    private boolean allDevicesHaveStandbyMode() {
        for (Device device : devices) {
            if (!device.hasStandbyMode())
                return false;
        }
        return true;
    }

    private boolean settingsChanged() {
        for (int i = 0; i < originalDeviceSettingses.size(); i++) {
            DeviceSettings deviceSettings = deviceSettingses.get(i);
            DeviceSettings oringinalDeviceSettings = originalDeviceSettingses.get(i);

            if (!deviceSettings.modeSettingsSame(oringinalDeviceSettings)) {
                return true;
            }
        }

        if (!isHomeMode) {
            if (location.nightModeEnabled != binding.nightModeScheduleToggle.isChecked()) {
                return true;
            }

            if (originalNightMode != null) {
                if (!originalNightMode.equals(nightMode)) {
                    return true;
                }
            } else {
                if (binding.nightModeScheduleToggle.isChecked()) {
                    return true;

                }
            }
        }
        return false;
    }

    private boolean allTheSame() {

        if (originalDeviceSettingses.isEmpty())
            return true;

        DeviceSettings first = originalDeviceSettingses.get(0);
        for (DeviceSettings deviceSettings : originalDeviceSettingses) {
            if (isHomeMode) {
                if (deviceSettings.homeMode.id != first.homeMode.id) {
                    return false;
                }
            } else {
                if (deviceSettings.nightMode.id != first.nightMode.id) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setDeviceMode(Device device, DeviceSettings deviceSettings, Mode newMode) {
        if (isHomeMode) {
            if (device == null) {
                for (DeviceSettings deviceSettings1 : deviceSettingses) {
                    deviceSettings1.homeMode = newMode;
                }
            } else {
                deviceSettings.homeMode = newMode;
            }
        } else {
            if (device == null) {
                for (DeviceSettings deviceSettings1 : deviceSettingses) {
                    deviceSettings1.nightMode = newMode;
                }
            } else {
                deviceSettings.nightMode = newMode;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        TinyMessageBus.register(this);

    }

    @Override
    public void onStop() {
        TinyMessageBus.unregister(this);
        saveSettings();
        super.onStop();
    }

    private void saveSettings() {

        if (!settingsChanged())
            return;
        for (DeviceSettings deviceSetting : deviceSettingses) {
            DeviceAPIService.changeDeviceModeSettings(deviceSetting);
        }

        if (!isHomeMode) {
            if (location.nightModeEnabled != binding.nightModeScheduleToggle.isChecked()) {
                LocationAPIService.setNightModeEnabled(location, location.id,
                        binding.nightModeScheduleToggle.isChecked());

                GoogleAnalyticsHelper.trackSettingsEvent(SETTINGS_NIGHT_MODE_ENABLED, SETTINGS_TYPE_LOCATION, 0,
                        null, location.id, String.valueOf(!binding.nightModeScheduleToggle.isChecked()),
                        String.valueOf(binding.nightModeScheduleToggle.isChecked()));
            }
            if (binding.nightModeScheduleToggle.isChecked()) {
                if (originalNightMode == null || !originalNightMode.equals(nightMode)) {

                    String originalStart = null;
                    String originalEnd = null;

                    if (originalNightMode != null) {
                        originalStart = originalNightMode.startTime;
                        originalEnd = originalNightMode.endTime;
                    }

                    String newStart = nightMode.startTime;
                    String newEnd = nightMode.endTime;

                    GoogleAnalyticsHelper.trackSettingsEvent(SETTINGS_NIGHT_MODE_TIME_START, SETTINGS_TYPE_LOCATION, 0,
                            null, location.id, originalStart, newStart);

                    GoogleAnalyticsHelper.trackSettingsEvent(SETTINGS_NIGHT_MODE_TIME_END, SETTINGS_TYPE_LOCATION, 0,
                            null, location.id, originalEnd, newEnd);

                    LocationAPIService.createNightMode(locationId, nightMode);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isHomeMode) {
            fragmentStack.setHeaderTitle(R.string.home_mode);
        } else {
            fragmentStack.setHeaderTitle(R.string.night_mode);
        }
        fragmentStack.showHelpButton();
        fragmentStack.enableRightButton(this, true);

        TinyMessageBus.post(new GetLocation(locationId));
    }


    @Subscribe
    public void gotLocationData(GotLocationData gotLocationData) {
        if (gotLocationData.location.id == locationId) {
            subscription = gotLocationData.subscription;
        }
    }

    @Override
    public void onRightButtonClick() {
        is.yranac.canary.fragments.setup.GetHelpFragment fragment = is.yranac.canary.fragments.setup.GetHelpFragment.newInstance(
                isHomeMode ? GET_HELP_TYPE_HOME_MODES : GET_HELP_TYPE_NIGHT_MODES);

        addModalFragment(fragment);

    }

    @Override
    protected String getAnalyticsTag() {
        if (isHomeMode) {
            return AnalyticsConstants.SCREEN_HOME_MODE_SETTINGS;
        }
        return AnalyticsConstants.SCREEN_NIGHT_MODE_SETTINGS;
    }

    private boolean showUpsell() {
        if (!isHomeMode) {
            return false;
        }
        if (subscription == null)
            return true;

        if (subscription.doesNotHaveModeConfigs()) {
            Intent i = new Intent(getActivity(), SettingsFragmentStackActivity.class);
            GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_HOME_MODE_SETTINGS_CTA_OPEN, null, null, location.id, 0);
            i.setAction(home_mode_settings_upsell);
            i.putExtra(extra_locationId, locationId);
            i.putExtra(modal, true);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.scale_alpha_push);
            return true;
        }

        return false;

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.set_each_layout:
                setupSetEachSettings();
                break;

            case R.id.set_all_layout:
                setupSetAllSettings();
                break;
            default:
                return;
        }

        float setX = v.getX() + (v.getWidth() / 2) - (binding.selectedLevel.getWidth() / 2);
        binding.selectedLevel.animate().translationX(setX).start();

    }
}
