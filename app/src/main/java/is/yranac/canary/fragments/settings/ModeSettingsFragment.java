package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsModeBinding;
import is.yranac.canary.fragments.setup.GetHelpFragment;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.api.LocationAPIService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.intent.APIIntentServiceSettingsInfo;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_MODES;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_LOCATION_MODE_SETTINGS;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_AUTO_MODE_ENABLED;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_TYPE_LOCATION;

public class ModeSettingsFragment extends SettingsFragment implements View.OnClickListener {

    private static final String LOG_TAG = "ModeSettingsFragment";
    private int locationId;

    private FragmentSettingsModeBinding binding;

    public static ModeSettingsFragment newInstance(int locationId) {
        ModeSettingsFragment fragment = new ModeSettingsFragment();

        Bundle args = new Bundle();
        args.putInt(location_id, locationId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsModeBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        locationId = getArguments().getInt(location_id);

        APIIntentServiceSettingsInfo.updateSettingsForLocation(getContext(), locationId);


        final Location location = LocationDatabaseService.getLocationFromId(locationId);


        if (location == null) {
            getActivity().onBackPressed();
            return;
        }

        binding.modeSettingCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                LocationAPIService.updateLocationModeSettingsAsync(locationId, isChecked, new Callback<Void>() {
                    @Override
                    public void success(Void aVoid, Response response) {
                        GoogleAnalyticsHelper.trackSettingsEvent(SETTINGS_AUTO_MODE_ENABLED, SETTINGS_TYPE_LOCATION, 0,
                                null, locationId, String.valueOf(!isChecked), String.valueOf(isChecked));
                        LocationDatabaseService.updateLocationModeSettings(locationId, isChecked);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                    }
                });
            }
        });

        binding.modeSettingCheckbox.setChecked(location.autoModeEnabled);

        binding.connectivitySettingsToggleLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.modeSettingCheckbox.toggle();
                    }
                }

        );

        binding.homeModeLayout.setOnClickListener(this);

        binding.nightModeLayout.setOnClickListener(this);

    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_LOCATION_MODE_SETTINGS;
    }

    @Override
    public void onRightButtonClick() {

        is.yranac.canary.fragments.setup.GetHelpFragment fragment = GetHelpFragment.newInstance(GET_HELP_TYPE_MODES);

        addModalFragment(fragment);

    }


    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.showHeader(true);
        fragmentStack.setHeaderTitle(R.string.modes);
        fragmentStack.showHelpButton();
        fragmentStack.enableRightButton(ModeSettingsFragment.this, true);

    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentStack.resetButtonStyle();
    }


    @Override
    public void onClick(View view) {

        boolean isHomeMode;
        switch (view.getId()) {
            case R.id.home_mode_layout:
                isHomeMode = true;
                break;
            case R.id.night_mode_layout:
                isHomeMode = false;
                break;
            default:
                return;
        }

        ConfigureModeSettingsFragment homeModeSettingsFragment = ConfigureModeSettingsFragment.newInstance(locationId, isHomeMode);
        addFragmentToStack(homeModeSettingsFragment, Utils.SLIDE_FROM_RIGHT);
    }
}
