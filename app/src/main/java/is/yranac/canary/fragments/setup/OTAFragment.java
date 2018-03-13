package is.yranac.canary.fragments.setup;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupOtaBinding;
import is.yranac.canary.fragments.ModeTutorialFragment;
import is.yranac.canary.fragments.settings.ManageDevicesFragment;
import is.yranac.canary.messages.CheckDeviceStatus;
import is.yranac.canary.messages.ModalPopped;
import is.yranac.canary.messages.OTAComplete;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.SubscriptionPlanDatabaseService;
import is.yranac.canary.services.jobs.APILocationJobService;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.anim.ResizeWidthAnimation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OTAFragment extends SetUpBaseFragment implements View.OnClickListener {

    private static final String LOG_TAG = "OTAFragment";
    private AlertDialog failedAlert = null;
    private Device device;

    private FragmentSetupOtaBinding binding;
    private int otherDevices;

    public static OTAFragment newInstance(String deviceUri) {

        OTAFragment fragment = new OTAFragment();
        Bundle args = new Bundle();
        args.putString(device_uri, deviceUri);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSetupOtaBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ModeTutorialFragment modeTutorialFragment = (ModeTutorialFragment) getChildFragmentManager().findFragmentById(R.id.fragment_slide_show);
        modeTutorialFragment.setDeviceUri(getArguments().getString(device_uri));
        modeTutorialFragment.setTutorialType(ModeTutorialFragment.TutorialType.OTA);
        device = DeviceDatabaseService.getDeviceFromResourceUri(getArguments().getString(device_uri));
        if (device == null)
            return;

        List<Device> devices = DeviceDatabaseService.getActivatedDevicesAtLocation(device.getLocationId());

        otherDevices = 0;
        for (Device device1 : devices) {
            if (!device1.serialNumber.equalsIgnoreCase(device.serialNumber)) {
                otherDevices++;
            }
        }

        Subscription subscription = SubscriptionPlanDatabaseService.getServicePlanForLocation(device.getLocationId());

        fragmentStack.disableBackButton();

        if (otherDevices > 0) {
            getChildFragmentManager().beginTransaction().hide(modeTutorialFragment).commitAllowingStateLoss();
        }

        switch (device.getDeviceType()) {
            case DeviceType.CANARY_AIO:
                binding.otaAsset.setImageResource(R.drawable.update7_aio);
                break;
            case DeviceType.FLEX:
                binding.otaAsset.setImageResource(R.drawable.update7_flex);
                break;
            case DeviceType.CANARY_VIEW:
                binding.otaAsset.setImageResource(R.drawable.update7_view);
                break;
        }

        if (subscription != null) {
            if (subscription.hasMembership && otherDevices > 0) {
                switch (device.getDeviceType()) {
                    case DeviceType.CANARY_AIO:
                        binding.otaMessageTextView.setText(R.string.canary_will_update_second);
                        break;
                    case DeviceType.FLEX:
                        binding.otaMessageTextView.setText(R.string.canary_flex_will_update_second);
                        break;
                    case DeviceType.CANARY_VIEW:
                        binding.otaMessageTextView.setText(R.string.canary_view_will_update_second);
                        break;
                }

            } else {
                switch (device.getDeviceType()) {
                    case DeviceType.CANARY_AIO:
                        binding.otaMessageTextView.setText(R.string.canary_will_update);
                        break;
                    case DeviceType.FLEX:
                        binding.otaMessageTextView.setText(R.string.canary_flex_needs_update);
                        break;
                    case DeviceType.CANARY_VIEW:
                        binding.otaMessageTextView.setText(R.string.canary_view_needs_update);
                        break;
                }
            }
        }

        if (otherDevices > 0 && (subscription != null
                && subscription.hasMembership)) {
            binding.nextBtn.setEnabled(true);
        } else {
            binding.nextBtn.setEnabled(false);
        }

        binding.nextBtn.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        TinyMessageBus.register(this);
        fragmentStack.resetButtonStyle();
        fragmentStack.showHelpButton();
        fragmentStack.enableRightButton(this, true);
        fragmentStack.setHeaderTitle(R.string.update);
        TinyMessageBus.post(new CheckDeviceStatus());
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentStack.resetButtonStyle();
        TinyMessageBus.unregister(this);
        if (failedAlert != null) {
            failedAlert.dismiss();
            failedAlert = null;

        }
    }

    @Subscribe
    public void checkDeviceStatus(CheckDeviceStatus checkDeviceStatus) {

        final String deviceUri = getArguments().getString(device_uri, "/v1/devices/fake/");
        DeviceAPIService.getDeviceByUri(deviceUri, new Callback<Device>() {
            @Override
            public void success(final Device device, Response response) {

                changeOTAStatus(device);
                if (device.deviceActivated) {

                    if (otherDevices > 0) {
                        onOTAComplete();
                    } else {
                        TinyMessageBus.post(new OTAComplete());
                    }

                    return;
                } else if (device.failedOTA()) {

                    if (failedAlert == null) {
                        failedAlert = AlertUtils.showDeviceUpdateFailedDialog(getActivity(), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fragmentStack.resetSetup(device.location);

                            }
                        });
                    }
                    return;
                }
                TinyMessageBus.postDelayed(new CheckDeviceStatus(), 5000);
            }

            @Override
            public void failure(RetrofitError error) {
                TinyMessageBus.postDelayed(new CheckDeviceStatus(), 5000);
            }

        });


    }


    private void changeOTAStatus(Device newDevice) {
        switch (newDevice.ota_status.toLowerCase()) {
            case "init":
                setProgressYellowImagesWithString(1);
                break;
            case "downloading":
                setProgressYellowImagesWithString(2);
                break;
            case "verified":
                setProgressYellowImagesWithString(3);
                break;
            case "complete":
                setProgressYellowImagesWithString(4);
                break;

        }
    }

    private void setProgressYellowImagesWithString(final int yellow) {

        Activity activity = getActivity();
        if (activity == null)
            return;

        Display display = activity.getWindowManager().getDefaultDisplay();

        if (display == null)
            return;

        int constant = 4;

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        final int barWidth = width / constant * yellow;


        if (barWidth == binding.progressBar.getWidth())
            return;

        ResizeWidthAnimation anim = new ResizeWidthAnimation(binding.progressBar, barWidth);
        int duration = 500;

        anim.setDuration(duration);
        binding.progressBar.startAnimation(anim);
    }

    private void onOTAComplete() {

        APILocationJobService.fetchLocations(getContext());
        binding.otaTitleText.setText(R.string.update_succesful);

        switch (device.deviceType.id) {
            case DeviceType.CANARY_AIO:
                binding.otaMessageTextView.setText(R.string.your_canary_is_set_up);
                break;
            case DeviceType.FLEX:
                binding.otaMessageTextView.setText(R.string.your_flex_is_set_up);
                break;
        }
        binding.nextBtn.setEnabled(true);
    }

    @Override
    public void onRightButtonClick() {
        final GetHelpFragment fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_OTA);
        addModalFragment(fragment);
        fragmentStack.enableBackButton();
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Subscribe
    public void onModalPopped(ModalPopped modalPopped) {
        fragmentStack.disableBackButton();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_btn:
                Subscription subscription = SubscriptionPlanDatabaseService.getServicePlanForLocation(device.getLocationId());

                if (subscription.hasMembership) {
                    fragmentStack.enableBackButton();
                    fragmentStack.addFragmentAndResetStack(ManageDevicesFragment.newInstance(device.getLocationId()), Utils.SLIDE_FROM_RIGHT);
                } else {
                    fragmentStack.addFragmentAndResetStack(PurchaseMembershipFragment.newInstance(device), Utils.SLIDE_FROM_RIGHT);
                }
                break;
        }
    }
}

