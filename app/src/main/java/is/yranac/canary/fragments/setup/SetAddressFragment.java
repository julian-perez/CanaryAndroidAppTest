package is.yranac.canary.fragments.setup;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsEditAddressBinding;
import is.yranac.canary.fragments.CountryCodeSelectFragment;
import is.yranac.canary.fragments.StackFragment;
import is.yranac.canary.model.CountryCode;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.location.LocationCreate;
import is.yranac.canary.model.location.LocationPatch;
import is.yranac.canary.services.api.LocationAPIService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.intent.GooglePlacesAPIIntentServiceEmergencyNumbers;
import is.yranac.canary.ui.views.CanaryTextWatcher;
import is.yranac.canary.ui.views.EditTextWithLabel;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.MapUtils;
import is.yranac.canary.util.MapUtils.GEOCODE_TYPE;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

import static is.yranac.canary.fragments.CountryCodeSelectFragment.LIST_TYPE.STATE;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_CREATE_LOCATION;


public class SetAddressFragment extends SetUpBaseFragment implements StackFragment.StackFragmentCallback,
        OnSuccessListener<android.location.Location>,
        MapUtils.AddressCallback, View.OnClickListener {

    private List<EditTextWithLabel> editTextList = new ArrayList<>();

    private static String firstLocation = "firstLocation";
    private FragmentSettingsEditAddressBinding binding;
    private Address currentAddress;

    public static SetAddressFragment newInstance(Bundle arguments, boolean first) {
        SetAddressFragment locationFragment = new SetAddressFragment();

        Bundle args = new Bundle();
        if (arguments != null) {
            args.putAll(arguments);
        }

        args.putBoolean(firstLocation, first);
        locationFragment.setArguments(args);
        return locationFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsEditAddressBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

        binding.setIsLocationSetup(true);
        binding.nextBtn.setOnClickListener(this);

    }


    private void initView() {
        binding.locationCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CountryCodeSelectFragment countryCodeSelectFragment = CountryCodeSelectFragment.newInstance(binding.locationCountry.getText(), CountryCodeSelectFragment.LIST_TYPE.COUNTRY);
                countryCodeSelectFragment.setCountryCodeComplete(new CountryCodeSelectFragment.CountryCodeComplete() {
                    @Override
                    public void countryCodeSelected(CountryCode countryCode) {
                        binding.locationCountry.setText(countryCode.name);
                        setupValidator();
                    }
                });

                addModalFragment(countryCodeSelectFragment);
            }
        });
        boolean first = getArguments().getBoolean(firstLocation, false);

        if (first) {
            binding.locationName.setText(getString(R.string.home));
        }
        setupValidator();
        locateUser();

    }

    private void setupValidator() {

        editTextList.clear();
        editTextList.add(binding.locationName);
        editTextList.add(binding.locationAddress);

        if (Location.isUnitedStates(binding.locationCountry.getText())) {
            editTextList.add(binding.locationCity);
            editTextList.add(binding.locationState);
            editTextList.add(binding.locationPostalCode);
        }

        setupViewWatcher(binding.locationName, CanaryTextWatcher.ANY_TEXT, editTextList, this);
        setupViewWatcher(binding.locationAddress, CanaryTextWatcher.ANY_TEXT, editTextList, this);
        setupViewWatcher(binding.locationAddressTwo, CanaryTextWatcher.ANY_TEXT, editTextList, this);
        setupViewWatcher(binding.locationCity, CanaryTextWatcher.ANY_TEXT, editTextList, this);
        setupViewWatcher(binding.locationState, CanaryTextWatcher.ANY_TEXT, editTextList, this);
        setupViewWatcher(binding.locationPostalCode, CanaryTextWatcher.ANY_TEXT, editTextList, this);
        binding.locationState.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (Location.isUnitedStates(binding.locationCountry.getText())) {
                    binding.locationState.setCursorVisible(false);
                    if (hasFocus) {

                        CountryCodeSelectFragment countryCodeSelectFragment = CountryCodeSelectFragment.newInstance(binding.locationState.getText(), STATE);
                        countryCodeSelectFragment.setCountryCodeComplete(new CountryCodeSelectFragment.CountryCodeComplete() {
                            @Override
                            public void countryCodeSelected(CountryCode countryCode) {
                                binding.locationState.setText(countryCode.code);
                            }
                        });
                        hideSoftKeyboard();
                        v.clearFocus();
                        addModalFragment(countryCodeSelectFragment);
                    }

                } else {
                    binding.locationState.setCursorVisible(true);
                }

            }
        });


    }

    private void locateUser() {

        showLoading(true, getString(R.string.locating));
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this);

    }

    @Override
    public void onSuccess(android.location.Location location) {
        if (location == null) {
            showLoading(false, "");
            return;
        }

        MapUtils.reverseGeocodeLatLng(location, getContext(), this);
    }

    @Override
    public void onResume() {
        super.onResume();

        fragmentStack.showHeader(true);

        fragmentStack.setHeaderTitle(R.string.address);

        fragmentStack.showRightButton(0);

        fragmentStack.enableRightButton(this, false);

    }

    @Override
    public void onRightButtonClick() {

    }


    private void patchLocation(final Location location) {
        LocationCallback locationCallback = new LocationCallback();
        locationCallback.location = location;
        showLoading(true, null);
        LocationPatch locationPatch = new LocationPatch(location);
        LocationAPIService.patchLocation(locationUri(), locationPatch, locationCallback);
    }

    @Override
    public void onAllEditTextHasChanged(boolean allValid) {
        if (currentAddress == null) {
            MapUtils.geocodeLocationName(getAddressFromTextViews(), getContext(), this);
        }

        binding.nextBtn.setEnabled(allValid);
    }

    private String getAddressFromTextViews() {
        return binding.locationAddress.getText() + ", " + binding.locationCity.getText()
                + ", " + binding.locationState.getText();
    }

    @Override
    public void onNewAddress(Address address, GEOCODE_TYPE geocode_type) {
        this.currentAddress = address;

        showLoading(false, getString(R.string.locating));
        if (geocode_type == GEOCODE_TYPE.GEOCODE_TYPE_LAT_LNG) {
            updateSettingsLocationInfo(address);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_btn:
                hideSoftKeyboard();
                final Location location = new Location();


                location.name = binding.locationName.getText();

                location.address = binding.locationAddress.getText();
                location.address2 = binding.locationAddressTwo.getText();
                location.city = binding.locationCity.getText();
                location.state = binding.locationState.getText();
                location.country = binding.locationCountry.getText();
                location.zip = binding.locationPostalCode.getText();
                if (currentAddress != null) {
                    location.lat = currentAddress.getLatitude();
                    location.lng = currentAddress.getLongitude();
                }
                location.geofenceRadius = 150;

                if (locationUri() != null) {
                    patchLocation(location);
                    return;
                }
                showLoading(true, null);

                LocationCreate locationCreate = new LocationCreate(location);

                LocationCallback locationCallback = new LocationCallback();
                locationCallback.location = location;
                LocationAPIService.createLocation(locationCreate, locationCallback);
                break;
        }
    }


    private class LocationCallback implements Callback<Void> {
        public Location location;


        @Override
        public void success(Void aVoid, Response response) {
            showLoading(false, null);
            for (Header header : response.getHeaders()) {
                if ("location".equalsIgnoreCase(header.getName())) {
                    String uri = header.getValue()
                            .substring(Constants.BASE_URL.length());
                    setLocationUri(uri);
                    break;
                }
            }
            int locationId = Utils.getIntFromResourceUri(locationUri());
            GooglePlacesAPIIntentServiceEmergencyNumbers.
                    getNewEmergencyNumbersForLocations(locationId, true);
            location.resourceUri = locationUri();
            location.id = locationId;
            Customer customer = CurrentCustomer.getCurrentCustomer();
            if (customer != null) {
                location.locationOwner = customer.resourceUri;
            }
            LocationDatabaseService.insertLocation(location);
            if (isVisible()) {
                CheckGeofenceFragment fragment = getInstance(CheckGeofenceFragment.class);
                addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
            }

            if (PreferencesUtils.hasSeenMaskingLaunch() || PreferencesUtils.getFirstCreatedLocationIDForMaskingLaunch() != null)
                return;

            PreferencesUtils.setFirstCreatedLocationIDForMaskingLaunch(location.id);
        }

        @Override
        public void failure(RetrofitError error) {
            showLoading(false, null);

            try {
                AlertUtils.showGenericAlert(getActivity(), Utils.getErrorMessageFromRetrofit(getActivity(), error));
            } catch (JSONException ignore) {
            }
        }
    }


    @Override
    protected String getAnalyticsTag() {
        return SCREEN_CREATE_LOCATION;
    }


    private void updateSettingsLocationInfo(Address currentAddress) {

        this.currentAddress = currentAddress;


        String streetNumber = currentAddress.getSubThoroughfare();
        String street = currentAddress.getThoroughfare();

        binding.locationAddress.setText(String.format(Locale.getDefault(), "%1$s %2$s ", streetNumber, street));
        binding.locationCity.setText(currentAddress.getLocality());
        binding.locationState.setText(MapUtils.getStateAbbreviation(currentAddress.getAdminArea(), currentAddress.getCountryName()));
        binding.locationPostalCode.setText(currentAddress.getPostalCode());
        binding.locationCountry.setText(currentAddress.getCountryName());
        setupValidator();

    }


}
