package is.yranac.canary.fragments.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupBuyOrSetupBinding;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.services.bluetooth.BluetoothSingleton;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_ADD_DEVICE;

public class AddADeviceFragment extends SetUpBaseFragment implements OnClickListener {


    private static final String LOG_TAG = "AddADeviceFragment";

    public static AddADeviceFragment newInstance(String location) {
        AddADeviceFragment f = new AddADeviceFragment();
        Bundle args = new Bundle();
        args.putBoolean(key_isSetup, true);
        args.putString(key_location_uri, location);
        f.setArguments(args);
        return f;
    }

    private FragmentSetupBuyOrSetupBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetupBuyOrSetupBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity activity = (BaseActivity) getActivity();
        activity.registerForOTBFailedReceive();
        activity.enableInAppNotifications();


        binding.flexSelection.setOnClickListener(this);

        binding.canarySelection.setOnClickListener(this);

        binding.canaryViewSelection.setOnClickListener(this);

    }

    private void goToNextStep(int deviceType) {
        getArguments().putInt(device_type, deviceType);
        PlaceCanaryFragment fragment = getInstance(PlaceCanaryFragment.class);
        addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);

    }

    @Override
    public void onResume() {
        super.onResume();

        fragmentStack.showRightButton(0);
        fragmentStack.showHeader(true);
        fragmentStack.enableRightButton(this, false);
        fragmentStack.setHeaderTitle(R.string.select_a_device);

    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_ADD_DEVICE;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.canary_view_selection:
                if (BluetoothSingleton.checkBleHardwareAvailable()) {
                    goToNextStep(DeviceType.CANARY_VIEW);
                } else {
                    String dsc = getString(R.string.canary_uses_bluetooth);
                    AlertUtils.showBleAlert(getContext(), dsc, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addModalFragment(GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_BLUETOOTH));
                        }
                    });
                }
                break;
            case R.id.canary_selection:
                goToNextStep(DeviceType.CANARY_AIO);
                break;

            case R.id.flex_selection:
                if (BluetoothSingleton.checkBleHardwareAvailable()) {
                    goToNextStep(DeviceType.FLEX);
                } else {
                    String dsc = getString(R.string.canary_uses_bluetooth);
                    AlertUtils.showBleAlert(getContext(), dsc, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addModalFragment(GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_BLUETOOTH));
                        }
                    });
                }
                break;
        }
    }
}
