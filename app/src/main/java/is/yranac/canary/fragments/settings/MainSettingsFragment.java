package is.yranac.canary.fragments.settings;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.adapter.MainSettingsAdapter;
import is.yranac.canary.databinding.FragmentMainSettingsBinding;
import is.yranac.canary.messages.CheckLocation;
import is.yranac.canary.messages.LocationTableUpdated;
import is.yranac.canary.messages.ResetDashboard;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.jobs.APIEntryJobService;
import is.yranac.canary.ui.SettingsFragmentStackActivity;
import is.yranac.canary.util.LocationUtil;
import is.yranac.canary.util.PermissionUtil;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.cache.location.GetLocations;
import is.yranac.canary.util.cache.location.GotLocations;

import static is.yranac.canary.fragments.settings.MainSettingsFragment.BannerType.BANNER_TYPE_DATA_SAVER;
import static is.yranac.canary.fragments.settings.MainSettingsFragment.BannerType.BANNER_TYPE_EMERGENCY_NUMBERS;
import static is.yranac.canary.fragments.settings.MainSettingsFragment.BannerType.BANNER_TYPE_LOCATION_ENABLED;
import static is.yranac.canary.fragments.settings.MainSettingsFragment.BannerType.BANNER_TYPE_LOCATION_PERMISSION;
import static is.yranac.canary.fragments.settings.MainSettingsFragment.BannerType.BANNER_TYPE_NONE;
import static is.yranac.canary.fragments.settings.MainSettingsFragment.BannerType.BANNER_TYPE_WIFI;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.about_canary;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.canary_help;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.edit_profile;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.extra_locationId;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.location_emergency_contact;

public class MainSettingsFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String LOG_TAG = "MainSettingsFragment";

    private static final int REQUEST_CODE = 234;

    private MainSettingsAdapter adapter;

    private int visibleLocation = 0;

    private final BroadcastReceiver mWifiChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setUpHeader();
        }
    };

    private FragmentMainSettingsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainSettingsBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new MainSettingsAdapter(getChildFragmentManager());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(this);
        binding.aboutButton.setOnClickListener(this);
        binding.helpButton.setOnClickListener(this);
        binding.accountButton.setOnClickListener(this);
        binding.bannerBackground.setOnClickListener(this);
        binding.indicator.setViewPager(binding.viewPager);
    }


    @Override
    public void onResume() {
        super.onResume();


        TinyMessageBus.post(new GetLocations());

        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.location.PROVIDERS_CHANGED");
        getActivity().registerReceiver(mWifiChangedReceiver, internetFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mWifiChangedReceiver);

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


    enum BannerType {
        BANNER_TYPE_LOCATION_PERMISSION,
        BANNER_TYPE_LOCATION_ENABLED,
        BANNER_TYPE_WIFI,
        BANNER_TYPE_DATA_SAVER,
        BANNER_TYPE_EMERGENCY_NUMBERS,
        BANNER_TYPE_NONE
    }

    public BannerType getBannerType() {
        boolean locationProviders = LocationUtil.canGetLocation(getActivity());
        boolean geofencingEnabled = LocationDatabaseService.checkIfGeofencingEnabled();
        ConnectivityManager connMgr = (ConnectivityManager)
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean dataSaverEnabled = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dataSaverEnabled = connMgr.getRestrictBackgroundStatus() == ConnectivityManager.RESTRICT_BACKGROUND_STATUS_ENABLED;
        }

        if (geofencingEnabled && LocationUtil.doesNotHaveLocationPermission(getActivity())) {
            return BANNER_TYPE_LOCATION_PERMISSION;
        } else if (!locationProviders && geofencingEnabled) {
            return BANNER_TYPE_LOCATION_ENABLED;
        } else if (!Utils.isWifiEnabled(getActivity()) && geofencingEnabled) {
            return BANNER_TYPE_WIFI;
        } else if (geofencingEnabled && dataSaverEnabled) {
            return BANNER_TYPE_DATA_SAVER;
        } else if (LocationDatabaseService.checkIfLocationHasEmergencyContacts() != null) {
            return BANNER_TYPE_EMERGENCY_NUMBERS;

        }

        return BANNER_TYPE_NONE;
    }


    public void setUpHeader() {
        switch (getBannerType()) {
            case BANNER_TYPE_LOCATION_PERMISSION:
                binding.bannerBackground.setVisibility(View.VISIBLE);
                binding.alertTextView.setText(R.string.please_turn_location_on);
                binding.actionTextView.setText(R.string.turn_on);
                break;
            case BANNER_TYPE_LOCATION_ENABLED:
                binding.bannerBackground.setVisibility(View.VISIBLE);
                binding.alertTextView.setText(R.string.please_turn_location_on);
                binding.actionTextView.setText(R.string.turn_on);
                break;
            case BANNER_TYPE_WIFI:
                binding.bannerBackground.setVisibility(View.VISIBLE);
                binding.alertTextView.setText(R.string.please_turn_wifi_on);
                binding.actionTextView.setText(R.string.turn_on);
                break;
            case BANNER_TYPE_DATA_SAVER:
                binding.bannerBackground.setVisibility(View.VISIBLE);
                binding.alertTextView.setText(R.string.data_saver_enabled);
                binding.actionTextView.setText(R.string.allow);
                break;
            case BANNER_TYPE_EMERGENCY_NUMBERS:
                binding.bannerBackground.setVisibility(View.VISIBLE);
                Location location = LocationDatabaseService.checkIfLocationHasEmergencyContacts();
                if (location != null) {
                    String missingString = getString(R.string.emergency_numbers_missing, location.name);
                    binding.alertTextView.setText(missingString);
                } else {
                    binding.bannerBackground.setVisibility(View.GONE);
                }
                binding.actionTextView.setText(R.string.add_now);
                break;
            case BANNER_TYPE_NONE:
                binding.bannerBackground.setVisibility(View.GONE);
                break;
        }
    }

    private void headerClick() {

        switch (getBannerType()) {
            case BANNER_TYPE_LOCATION_PERMISSION:
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                break;
            case BANNER_TYPE_LOCATION_ENABLED:
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                break;
            case BANNER_TYPE_WIFI:
                WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
                break;
            case BANNER_TYPE_DATA_SAVER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Intent intent = new Intent(Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS, Uri.parse("package:" + getContext().getPackageName()));
                    startActivity(intent);
                }
                break;
            case BANNER_TYPE_EMERGENCY_NUMBERS:
                Location location = LocationDatabaseService.checkIfLocationHasEmergencyContacts();
                if (location != null) {
                    Intent i = new Intent(getActivity(), SettingsFragmentStackActivity.class);
                    i.setAction(location_emergency_contact);
                    i.putExtra(extra_locationId, location.id);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }
                break;
            case BANNER_TYPE_NONE:
                break;
        }

    }


    @Subscribe
    public void onLocationTableUpdated(LocationTableUpdated message) {
        TinyMessageBus.post(new GetLocations());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                boolean hasPermission = PermissionUtil.hasPermission(permissions, grantResults, getActivity());
                if (hasPermission) {
                    setUpHeader();
                }

            }

        }
    }

    @Override
    public void onClick(View v) {

        Intent i = new Intent(getActivity(), SettingsFragmentStackActivity.class);
        switch (v.getId()) {
            case R.id.about_button:
                i.setAction(about_canary);
                break;
            case R.id.help_button:
                i.setAction(canary_help);
                break;

            case R.id.account_button:
                i.setAction(edit_profile);
                break;
            case R.id.banner_background:
                headerClick();
                return;
        }
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.scale_alpha_push);


    }

    @Subscribe
    public void gotLocations(GotLocations gotLocations) {
        if (!isVisible())
            return;

        setUpHeader();
        Location currentLocation = gotLocations.currentLocation;
        if (currentLocation == null)
            return;

        if (visibleLocation == 0) {
            visibleLocation = currentLocation.id;
        }

        List<Location> locations = gotLocations.locations;
        List<Location> locationsCopy = new ArrayList<>(locations);

        for (Location location : locations) {
            if (location.id == visibleLocation) {
                currentLocation = location;
                break;
            }
        }

        if (locations.size() > MainSettingsAdapter.MAX_LOCATIONS) {
            for (Location location : locationsCopy) {
                if (location.id == visibleLocation) {
                    locations.remove(location);
                    locations.add(0, currentLocation);
                    break;
                }
            }
        }

        adapter = new MainSettingsAdapter(getChildFragmentManager(), locations);
        binding.viewPager.setAdapter(adapter);

        if (locations.size() > MainSettingsAdapter.MAX_LOCATIONS) {
            binding.viewPager.setCurrentItem(1, true);
        } else {
            for (Location location : locations) {
                if (location.id == visibleLocation) {
                    binding.viewPager.setCurrentItem(locations.indexOf(location));
                    break;
                }
            }
        }
    }

    @Override

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        if (adapter.getLocation().size() > MainSettingsAdapter.MAX_LOCATIONS) {
            return;
        }

        if (position == adapter.getCount() - 1)
            return;

        Location location = adapter.getLocation(position);
        TinyMessageBus.post(new CheckLocation(location.id, false));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Subscribe(mode = Subscribe.Mode.Background)
    public void getLocationDevices(CheckLocation locationDevices) {
        List<Device> devices = DeviceDatabaseService.getActivatedDevicesAtLocation(locationDevices.location);
        visibleLocation = locationDevices.location;

        if (!devices.isEmpty()) {
            UserUtils.setLastViewedLocationId(locationDevices.location);
            TinyMessageBus.post(new ResetDashboard());
            APIEntryJobService.rescheduleIntent(getContext());
            if (adapter.getLocation().size() > MainSettingsAdapter.MAX_LOCATIONS) {
                TinyMessageBus.post(new GetLocations());
            }

        } else if (locationDevices.onClick &&
                adapter.getLocation().size() > MainSettingsAdapter.MAX_LOCATIONS) {

            TinyMessageBus.post(new GetLocations());
        }
    }
}
