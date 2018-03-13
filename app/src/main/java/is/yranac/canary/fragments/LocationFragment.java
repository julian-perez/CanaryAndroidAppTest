package is.yranac.canary.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentLocationBinding;
import is.yranac.canary.messages.ClosePanelRequest;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.intent.APIIntentServiceSettingsInfo;
import is.yranac.canary.ui.SettingsFragmentStackActivity;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.cache.location.GetLocation;
import is.yranac.canary.util.cache.location.GotLocationData;

import static is.yranac.canary.ui.SettingsFragmentStackActivity.extra_locationId;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.location_details;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.manage_devices;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.manage_members;

/**
 * Created by michaelschroeder on 5/5/17.
 */

public class LocationFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {


    private FragmentLocationBinding binding;
    private final static String BUNDLE_KEY_MAP_STATE = "mapData";

    private Location location;
    private int locationId;
    private List<Device> devices;

    public static LocationFragment newInstance(int location) {
        LocationFragment locationFragment = new LocationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("location", location);
        locationFragment.setArguments(bundle);
        return locationFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocationBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle mapState = null;
        if (savedInstanceState != null) {
            // Load the map state bundle from the main savedInstanceState
            mapState = savedInstanceState.getBundle(BUNDLE_KEY_MAP_STATE);
        }

        binding.mapView.onCreate(mapState);
        locationId = getArguments().getInt("location");


        binding.homeDetails.setOnClickListener(this);
        binding.locationTrailLayout.setOnClickListener(this);
        binding.manageDevices.setOnClickListener(this);
        binding.manageMembers.setOnClickListener(this);
        binding.locationButton.setOnClickListener(this);
        binding.mapView.getMapAsync(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        TinyMessageBus.register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
        TinyMessageBus.post(new GetLocation(locationId));
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Subscribe
    public void onGotLocationInfo(GotLocationData gotLocationData) {
        if (gotLocationData.location == null ||
                gotLocationData.location.id != locationId) {
            return;
        }

        if (!isAdded())
            return;

        location = gotLocationData.location;


        binding.locationButton.setText(location.name);

        devices = gotLocationData.deviceList;
        List<Customer> customers = gotLocationData.customers;
        Subscription subscription = gotLocationData.subscription;
        binding.homeDetailsTrail.setText(getString(R.string.home_settings, location.name));
        binding.homeDetails.setText(getString(R.string.home_settings, location.name));

        if (subscription != null) {
            if (subscription.hasMembership) {
                binding.locationTrailLayout.setVisibility(View.GONE);
                binding.locationDetails.setVisibility(View.VISIBLE);

                binding.membershipStatus.setText(R.string.membership_active);
                binding.membershipStatus.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.bright_sky_blue_two));

            } else if (subscription.onTrial) {
                binding.locationTrailLayout.setVisibility(View.VISIBLE);
                binding.locationDetails.setVisibility(View.GONE);

                String daysRemaining = getString(R.string.days_left_membership_preview,
                        subscription.remainingDays());
                binding.daysRemainingText.setText(daysRemaining);

            } else {
                binding.locationTrailLayout.setVisibility(View.GONE);
                binding.locationDetails.setVisibility(View.VISIBLE);
                binding.membershipStatus.setText(R.string.membership_inactive);
                binding.membershipStatus.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.dark_gray));
            }
        }

        int deviceString;

        if (devices.size() == 1) {
            deviceString = R.string.one_device;
        } else {
            deviceString = R.string.more_than_one_device;

        }

        int customerString;

        if (customers.size() == 1) {
            customerString = R.string.one_customer;
        } else {
            customerString = R.string.more_than_one_customer;
        }

        binding.manageDevices.setText(getString(deviceString, devices.size()));
        binding.manageMembers.setText(getString(customerString, customers.size()));
        binding.mapView.getMapAsync(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        TinyMessageBus.unregister(this);
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapState = new Bundle();
        binding.mapView.onSaveInstanceState(mapState);
        // Put the map bundle in the main outState
        outState.putBundle(BUNDLE_KEY_MAP_STATE, mapState);
        binding.mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        binding.mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (getContext() == null) {
            return;
        }

        if (location != null) {
            LatLng latLng = new LatLng(location.lat, location.lng);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }

        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);

        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        getContext(), R.raw.map_config));


    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), SettingsFragmentStackActivity.class);

        switch (v.getId()) {
            case R.id.location_trail_layout:
            case R.id.home_details:
                APIIntentServiceSettingsInfo.updateSettingsForLocation(getContext(), locationId);
                i.setAction(location_details);
                break;
            case R.id.manage_devices:
                i.setAction(manage_devices);
                break;

            case R.id.manage_members:
                i.setAction(manage_members);
                break;
            case R.id.location_button:
                if (devices != null && !devices.isEmpty()) {
                    TinyMessageBus.post(new ClosePanelRequest());
                    return;
                } else {
                    i.setAction(manage_devices);
                }
        }
        i.putExtra(extra_locationId, locationId);

        startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.scale_alpha_push);

    }
}
