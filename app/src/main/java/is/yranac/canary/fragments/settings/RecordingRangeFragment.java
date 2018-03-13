package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import is.yranac.canary.R;
import is.yranac.canary.adapter.SettingsSingleSelectionAdapter;
import is.yranac.canary.databinding.FragmentRecordingRangeBinding;
import is.yranac.canary.databinding.FragmentSettingsSimpleListBinding;
import is.yranac.canary.fragments.setup.GetHelpFragment;
import is.yranac.canary.model.device.DeviceSettings;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.database.DeviceDatabaseService;

/**
 * Created by sergeymorozov on 5/20/16.
 */
public class RecordingRangeFragment extends SettingsFragment {

    public static final String key_deviceSettingsJson = "device_settings_json";

    private DeviceSettings deviceSettings;
    private DeviceSettings originalDeviceSetting;


    public static RecordingRangeFragment newInstance(int deviceId) {
        Bundle args = new Bundle();
        args.putInt(key_deviceSettingsJson, deviceId);

        RecordingRangeFragment b = new RecordingRangeFragment();
        b.setArguments(args);

        return b;

    }

    private FragmentSettingsSimpleListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsSimpleListBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        loadData();

        FragmentRecordingRangeBinding headerBinding = FragmentRecordingRangeBinding.inflate(getLayoutInflater(savedInstanceState));

        fragmentStack.enableRightButton(this, false);
        fragmentStack.setHeaderTitle(R.string.battery_settings_option);

        String[] choices = getResources().getStringArray(R.array.range_states);
        String[] additionalComment = new String[]{getString(R.string.range_comment_more), null, getString(R.string.range_comment_less)};

        binding.listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        SettingsSingleSelectionAdapter adapter = new SettingsSingleSelectionAdapter(getActivity(), choices, additionalComment, R.layout.setting_row_radio);
        binding.listView.setAdapter(adapter);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.listView.setItemChecked(position, true);
                setRecordingRange(position);
            }
        });
        binding.listView.addHeaderView(headerBinding.getRoot());

        switch (deviceSettings.pirRecordingRange) {
            case DeviceSettings.pirRecordingRangeLow:
                binding.listView.setItemChecked(1, true);
                break;
            case DeviceSettings.pirRecordingRangeMedium:
                binding.listView.setItemChecked(2, true);
                break;
            case DeviceSettings.pirRecordingRangeHigh:
                binding.listView.setItemChecked(3, true);
                break;
            default:
                binding.listView.setItemChecked(2, true);
                break;
        }

    }

    private void loadData() {
        if (this.getArguments() == null)
            return;

        int deviceId = getArguments().getInt(key_deviceSettingsJson);


        deviceSettings = DeviceDatabaseService.getDeviceSettingsForDevice(deviceId);
        originalDeviceSetting = new DeviceSettings(deviceSettings);
    }

    private void setRecordingRange(int position) {
        if (position == 1) {
            deviceSettings.pirRecordingRange = DeviceSettings.pirRecordingRangeLow;
        } else if (position == 2)
            deviceSettings.pirRecordingRange = DeviceSettings.pirRecordingRangeMedium;
        else if (position == 3)
            deviceSettings.pirRecordingRange = DeviceSettings.pirRecordingRangeHigh;

        fragmentStack.enableRightButton(this, enableSave());
    }

    private boolean enableSave() {
        if (deviceSettings == null || originalDeviceSetting == null)
            return false;
        return !deviceSettings.equals(originalDeviceSetting);
    }


    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.enableRightButton(this, true);
        fragmentStack.showHelpButton();
    }

    @Override
    public void onPause() {
        super.onPause();
        DeviceAPIService.changeDeviceSettings(deviceSettings);

    }

    /**
     * Function that is called when the right button is click in the header
     */
    @Override
    public void onRightButtonClick() {
        is.yranac.canary.fragments.setup.GetHelpFragment fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_BATTERY);
        addModalFragment(fragment);
    }

    /**
     * Return the Screen tag for google Analytics
     *
     * @return Null if no tagging otherwise the corresponding tag
     */
    @Override
    protected String getAnalyticsTag() {
        return null;
    }

}
