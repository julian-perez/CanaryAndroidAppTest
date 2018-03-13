package is.yranac.canary.fragments.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.adapter.SettingsAdapter;
import is.yranac.canary.databinding.FragmentSettingsSimpleListBinding;
import is.yranac.canary.model.SettingsObject;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.fragments.settings.SecurityFragment.SettingType.CERT;
import static is.yranac.canary.fragments.settings.SecurityFragment.SettingType.LOCATION_DEBUG;

public class SecurityFragment extends SettingsFragment implements AdapterView.OnItemClickListener, SettingsAdapter.SettingsAdapterCallback {


    private ArrayList<SettingsObject> settingsObjects = new ArrayList<>();
    private ArrayList<SettingType> settingsNeeded = new ArrayList<>();


    private FragmentSettingsSimpleListBinding binding;

    public static SecurityFragment newInstance() {
        return new SecurityFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings_simple_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.listView.setOnItemClickListener(this);
        List<SettingsObject> settingsObjects = getSettings();
        SettingsAdapter adapter = new SettingsAdapter(
                getActivity(), settingsObjects, this, null);
        binding.listView.setAdapter(adapter);

    }


    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.showRightButton(0);
        fragmentStack.showHeader(true);
        fragmentStack.setHeaderTitle(R.string.password_and_security);
    }

    enum SettingType {
        CHANGE_PASSWORD,
        ADD_PASS_CODE,
        EDIT_PASS_CODE,
        REMOVE_PASS_CODE,
        TOGGLE_GEOFENCE,
        LOCATION_DEBUG,
        CERT
    }

    private ArrayList<SettingsObject> getSettings() {

        settingsNeeded.clear();
        settingsObjects.clear();

        settingsNeeded.add(SettingType.CHANGE_PASSWORD);

        if (!PreferencesUtils.hasPassCode()) {
            settingsNeeded.add(SettingType.ADD_PASS_CODE);
        } else {
            settingsNeeded.add(SettingType.EDIT_PASS_CODE);

            settingsNeeded.add(SettingType.REMOVE_PASS_CODE);
        }

        settingsNeeded.add(SettingType.TOGGLE_GEOFENCE);


        settingsNeeded.add(LOCATION_DEBUG);
        settingsNeeded.add(CERT);

        for (SettingType settingType : settingsNeeded) {
            SettingsObject settingsObject = getSettingObjectBySettingType(settingType);
            if (settingsObject != null)
                settingsObjects.add(settingsObject);
        }
        return settingsObjects;
    }


    private SettingsObject getSettingObjectBySettingType(SettingType type) {
        switch (type) {
            case CHANGE_PASSWORD:
                return SettingsObject.prompt(
                        getString(R.string.change_password));
            case ADD_PASS_CODE:
                return SettingsObject.prompt(
                        getString(R.string.add_passcode_lock));
            case EDIT_PASS_CODE:
                return SettingsObject.prompt(
                        getString(R.string.edit_pass_code));
            case REMOVE_PASS_CODE:
                return SettingsObject.prompt(getString(R.string.remove_pass_code));
            case TOGGLE_GEOFENCE:
                return SettingsObject.checkbox(getString(R.string.toggle_geofence), PreferencesUtils.getGeofencingEnabled(), true);
            case LOCATION_DEBUG:
                return SettingsObject.prompt(getString(R.string.location_debug));
            case CERT:
                boolean acceptConnections = PreferencesUtils.trustsConnection();
                return SettingsObject.checkbox(getString(R.string.accept_untrusted_connections), !acceptConnections, true);
            default:
                return null;
        }
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        SettingType settingType = settingsNeeded.get(position);
        handleSettingType(settingType);

    }

    private void handleSettingType(SettingType selectedType) {
        switch (selectedType) {
            case CHANGE_PASSWORD:
                addFragmentToStack(
                        ChangePasswordFragment.newInstance(), Utils.SLIDE_FROM_RIGHT);
                break;
            case ADD_PASS_CODE:
                addFragmentToStack(
                        SetPassCodeFragment.newInstance(SetPassCodeFragment.CREATE), Utils.SLIDE_FROM_RIGHT);
                break;
            case EDIT_PASS_CODE:
                addFragmentToStack(
                        SetPassCodeFragment.newInstance(SetPassCodeFragment.CHANGE), Utils.SLIDE_FROM_RIGHT);
                break;
            case REMOVE_PASS_CODE:
                addFragmentToStack(
                        SetPassCodeFragment.newInstance(SetPassCodeFragment.REMOVE), Utils.SLIDE_FROM_RIGHT);
                break;
            case LOCATION_DEBUG:
                addFragmentToStack(
                        LocationServicesDebuggingFragment.newInstance(), Utils.SLIDE_FROM_RIGHT);
                break;

        }
    }

    @Override
    public void onOptionSelected(int position) {
        SettingType settingType = settingsNeeded.get(position);
        switch (settingType) {
            case CERT:
                PreferencesUtils.toggleCerts();
                break;
            case TOGGLE_GEOFENCE:
                PreferencesUtils.toggleGeofencing();
                break;
        }
    }

}
