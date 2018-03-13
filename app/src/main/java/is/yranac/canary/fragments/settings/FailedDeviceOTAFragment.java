package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsFailedDeviceOtaBinding;
import is.yranac.canary.fragments.setup.GetHelpFragment;
import is.yranac.canary.messages.GetCachedDevice;
import is.yranac.canary.messages.GotCachedDevice;
import is.yranac.canary.messages.ModalPopped;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_FAILED_OTA_CANARY;
import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_FAILED_OTA_FLEX;
import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_FAILED_OTA_VIEW;

/**
 * Created by michaelschroeder on 7/18/17.
 */

public class FailedDeviceOTAFragment extends SettingsFragment implements View.OnClickListener {


    private Device device;

    public static FailedDeviceOTAFragment newInstance(String deviceId) {

        Bundle bundle = new Bundle();

        bundle.putString(key_device_id, deviceId);

        FailedDeviceOTAFragment fragment = new FailedDeviceOTAFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    private FragmentSettingsFailedDeviceOtaBinding binding;

    private String deviceUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsFailedDeviceOtaBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deviceUri = getArguments().getString(key_device_id);
        binding.viewDeviceBtn.setOnClickListener(this);
        binding.helpBtn.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        fragmentStack.disableBackButton();
        TinyMessageBus.register(this);
        TinyMessageBus.post(new GetCachedDevice(deviceUri));
    }

    @Override
    public void onStop() {
        TinyMessageBus.unregister(this);
        super.onStop();
    }

    @Subscribe
    public void gotCachedDevice(GotCachedDevice gotCachedDevice) {
         device = gotCachedDevice.device;

        if (device == null)
            return;


        switch (device.getDeviceType()) {

            case DeviceType.CANARY_AIO:
                binding.setupFailedDscTextView.setText(R.string.setup_failed_dsc_canary);
                binding.canaryFailedImageView.setImageResource(R.drawable.update7_aio);
                break;
            case DeviceType.FLEX:
                binding.setupFailedDscTextView.setText(R.string.setup_failed_dsc_canary_flex);
                binding.canaryFailedImageView.setImageResource(R.drawable.flex_failed);
                break;
            case DeviceType.CANARY_VIEW:
                binding.setupFailedDscTextView.setText(R.string.setup_failed_dsc_canary_view);
                binding.canaryFailedImageView.setImageResource(R.drawable.update7_view);
                break;
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
    public void onClick(View v) {

        if (device == null)
            return;

        switch (v.getId()) {
            case R.id.view_device_btn:
                fragmentStack.enableBackButton();
                fragmentStack.showHeader(true);
                fragmentStack.addFragmentAndResetStack(EditDeviceFragment.newInstance(device.id, 0), Utils.SLIDE_FROM_RIGHT);
                break;
            case R.id.help_btn:
                Fragment fragment;

                switch (device.getDeviceType()) {
                    case DeviceType.CANARY_AIO:
                        fragment = GetHelpFragment.newInstance(GET_HELP_TYPE_FAILED_OTA_CANARY);
                        break;
                    case DeviceType.FLEX:
                        fragment = GetHelpFragment.newInstance(GET_HELP_TYPE_FAILED_OTA_FLEX);
                        break;
                    case DeviceType.CANARY_VIEW:
                        fragment = GetHelpFragment.newInstance(GET_HELP_TYPE_FAILED_OTA_VIEW);
                        break;
                    default:
                        return;
                }
                addModalFragment(fragment);
                fragmentStack.enableBackButton();
                break;
        }
    }

    @Subscribe
    public void onModalPopped(ModalPopped modalPopped) {
        fragmentStack.disableBackButton();
    }
}
