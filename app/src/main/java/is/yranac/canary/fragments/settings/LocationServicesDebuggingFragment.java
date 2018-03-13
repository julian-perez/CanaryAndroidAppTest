package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentLocationServicesDebuggingBinding;
import is.yranac.canary.util.PreferencesUtils;

/**
 * Created by Schroeder on 10/22/15.
 */
public class LocationServicesDebuggingFragment extends SettingsFragment implements View.OnClickListener {

    public static LocationServicesDebuggingFragment newInstance() {
        return new LocationServicesDebuggingFragment();
    }

    private FragmentLocationServicesDebuggingBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLocationServicesDebuggingBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.settingCheckbox.setChecked(PreferencesUtils.locationServicesDebuggingEnabled(getContext()));
        binding.locationNotificationCheckbox.setChecked(PreferencesUtils.locationNotificationEnabled(getContext()));
        binding.locationDebugLayout.setOnClickListener(this);
        binding.locationNotificationLayout.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.showRightButton(0);
        fragmentStack.setHeaderTitle(R.string.location_debug);
    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.location_debug_layout:
                binding.settingCheckbox.toggle();
                PreferencesUtils.setLocationServicesDebugging(getContext(), binding.settingCheckbox.isChecked());
                break;
            case R.id.location_notification_layout:
                binding.locationNotificationCheckbox.toggle();
                PreferencesUtils.setLocationNotification(getContext(), binding.locationNotificationCheckbox.isChecked());

                break;
        }
    }
}
