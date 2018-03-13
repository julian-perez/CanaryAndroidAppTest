package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsEditAddressBinding;
import is.yranac.canary.fragments.CountryCodeSelectFragment;
import is.yranac.canary.model.CountryCode;
import is.yranac.canary.model.location.AddressPatch;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.api.LocationAPIService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.ui.views.CanaryTextWatcher;
import is.yranac.canary.ui.views.EditTextWithLabel;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.cache.location.UpdateLocation;
import is.yranac.canary.util.ga.AnalyticsConstants;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_ADDRESS_SETTINGS;

public class EditLocationFragment extends SettingsFragment {

    private int locationId;

    private Location location;


    private List<EditTextWithLabel> editTextList = new ArrayList<>();

    private FragmentSettingsEditAddressBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsEditAddressBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationId = getArguments().getInt(location_id);
        initView();

        binding.setIsLocationSetup(false);
    }

    private void initView() {


        Location location = getLocationDetails();
        binding.locationName.setText(location.name);
        binding.locationAddress.setText(location.address);
        binding.locationAddressTwo.setText(location.address2);
        binding.locationCity.setText(location.city);
        binding.locationState.setText(location.state);
        binding.locationPostalCode.setText(location.zip);
        binding.locationCountry.setText(location.country);

        if (binding.locationCountry.getText().isEmpty())
            binding.locationCountry.setText("United States");

        binding.locationCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CountryCodeSelectFragment countryCodeSelectFragment = CountryCodeSelectFragment.newInstance(binding.locationCountry.getText(), CountryCodeSelectFragment.LIST_TYPE.COUNTRY);
                countryCodeSelectFragment.setCountryCodeComplete(new CountryCodeSelectFragment.CountryCodeComplete() {
                    @Override
                    public void countryCodeSelected(CountryCode countryCode) {
                        binding.locationCountry.setText(countryCode.name);
                    }
                });

                addModalFragment(countryCodeSelectFragment);
            }
        });

        setupValidator();
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

        setupViewWatcher(binding.locationName, CanaryTextWatcher.ANY_TEXT, editTextList, stackFragmentCallback);
        setupViewWatcher(binding.locationAddress, CanaryTextWatcher.ANY_TEXT, editTextList, stackFragmentCallback);
        setupViewWatcher(binding.locationAddressTwo, CanaryTextWatcher.ANY_TEXT, editTextList, stackFragmentCallback);
        setupViewWatcher(binding.locationCity, CanaryTextWatcher.ANY_TEXT, editTextList, stackFragmentCallback);
        setupViewWatcher(binding.locationState, CanaryTextWatcher.ANY_TEXT, editTextList, stackFragmentCallback);
        setupViewWatcher(binding.locationPostalCode, CanaryTextWatcher.ANY_TEXT, editTextList, stackFragmentCallback);
        binding.locationState.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (Location.isUnitedStates(binding.locationCountry.getText())) {
                    binding.locationState.setCursorVisible(false);
                    if (hasFocus) {

                        CountryCodeSelectFragment countryCodeSelectFragment = CountryCodeSelectFragment.newInstance(binding.locationState.getText(), CountryCodeSelectFragment.LIST_TYPE.STATE);
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


    @Override
    public void onResume() {
        super.onResume();

        fragmentStack.setHeaderTitle(R.string.address);
        fragmentStack.showRightButton(0);

    }

    @Override
    public void onPause() {
        super.onPause();
        patchLocation();
    }

    private void patchLocation() {
        AddressPatch locationPatch = new AddressPatch();

        location.name = locationPatch.setName(binding.locationName.getText());

        location.address = locationPatch.setAddress(binding.locationAddress.getText());
        location.address2 = locationPatch.setAddress2(binding.locationAddressTwo.getText());
        location.city = locationPatch.setCity(binding.locationCity.getText());
        location.state = locationPatch.setState(binding.locationState.getText());
        location.country = locationPatch.setCountry(binding.locationCountry.getText());
        location.zip = locationPatch.setZip(binding.locationPostalCode.getText());

        LocationAPIService.patchAddress(
                locationId, locationPatch, new Callback<Void>() {
                    @Override
                    public void success(Void aVoid, Response response) {

                        TinyMessageBus.post(new UpdateLocation(location));

                        GoogleAnalyticsHelper.trackSettingsEvent(AnalyticsConstants.SETTINGS_ADDRESS_UPDATE, AnalyticsConstants.SETTINGS_TYPE_LOCATION,
                                0, null, location.id, null, null);

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
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

    }


    private Location getLocationDetails() {
        if (location == null) {
            location = LocationDatabaseService.getLocationFromId(locationId);
        }
        return location;
    }


    @Override
    protected String getAnalyticsTag() {
        return SCREEN_ADDRESS_SETTINGS;

    }

    private StackFragmentCallback stackFragmentCallback = new StackFragmentCallback() {
        @Override
        public void onAllEditTextHasChanged(boolean allValid) {
            if (allValid) {
                fragmentStack.enableBackButton();
                removeErrorStates(editTextList);
            } else {
                fragmentStack.disableBackButton();
                showErrorStates(editTextList);
            }
        }
    };

}
