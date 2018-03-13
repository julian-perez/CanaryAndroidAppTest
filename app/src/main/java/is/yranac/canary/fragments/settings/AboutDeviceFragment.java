package is.yranac.canary.fragments.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentAboutDeviceBinding;
import is.yranac.canary.fragments.setup.ForgotPasswordFragment;
import is.yranac.canary.fragments.setup.IndoorOutdoorFlexFragment;
import is.yranac.canary.interfaces.DeviceActivationDialogListener;
import is.yranac.canary.messages.GetCachedDeviceAndLocationOwner;
import is.yranac.canary.messages.GotCachedDeviceAndLocationOwner;
import is.yranac.canary.messages.SuccessDone;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceActivation;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.ui.SetupFragmentStackActivity;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.JSONUtil;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ZendeskUtil;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.ui.SetupFragmentStackActivity.LOCATION_ID;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

/**
 * Created by sergeymorozov on 5/20/16.
 */
public class AboutDeviceFragment extends SettingsFragment implements View.OnClickListener {

    private static final String key_deviceJSON = "device_json";
    private static final String LOG_TAG = "AboutDeviceFragment";

    private Device device;

    private FragmentAboutDeviceBinding binding;
    private AlertDialog dialog;
    private Customer owner;
    private boolean isCurrentUserLocationOwner;
    private int totalNumDevices;
    private static final int SUCCESS_WAIT_TIME = 500;

    public static AboutDeviceFragment newInstance(Bundle bundle, Device device) {
        AboutDeviceFragment a = new AboutDeviceFragment();

        String deviceJSON = JSONUtil.getJSONString(device);

        Bundle args = new Bundle();
        if (bundle != null) {
            args.putAll(bundle);
        }
        args.putString(key_deviceJSON, deviceJSON);

        a.setArguments(args);
        return a;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAboutDeviceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        loadData();
        binding.headerLayout.deviceIcon.setImageResource(device.getLargeIcon());
        binding.setDevice(device);

        binding.headerLayout.setDevice(device);
        setUpNameView();
        binding.headerLayout.removeDeviceBtn.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        TinyMessageBus.register(this);
        fragmentStack.enableRightButton(this, false);
        fragmentStack.showRightButton(0);
        TinyMessageBus.post(new GetCachedDeviceAndLocationOwner(device.id));
    }

    @Override
    public void onPause() {
        super.onPause();

        TinyMessageBus.unregister(this);
    }

    private void setUpNameView() {

        binding.deviceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                if (device.deviceType.id == DeviceType.FLEX) {
                    fragment = IndoorOutdoorFlexFragment.newInstance(getArguments(), device.deviceType.id, device.id, device.resourceUri, false, device.name);
                } else {
                    fragment = DeviceNamingFragment.newInstance(getArguments(), device.deviceType.id, device.id, device.resourceUri, false, false, device.name);
                }
                addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
            }
        });
    }


    private void loadData() {
        if (getArguments() == null)
            return;

        String deviceJson = getArguments().getString(key_deviceJSON);
        if (StringUtils.isNullOrEmpty(deviceJson))
            return;

        device = JSONUtil.getObject(deviceJson, Device.class);
    }


    @Subscribe
    public void gotCachedDeviceAndOwner(GotCachedDeviceAndLocationOwner data) {

        if (isDetached() || isRemoving())
            return;

        if (data.getDevice() == null || data.getDevice().id != device.id) {
            return;
        }

        this.device = data.getDevice();
        String deviceJSON = JSONUtil.getJSONString(device);
        getArguments().putString(key_deviceJSON, deviceJSON);
        binding.setDevice(device);
        binding.headerLayout.setDevice(device);
        fragmentStack.setHeaderTitle(getString(R.string.about_device_header, device.deviceType.name));
        setUpNameView();

        this.owner = data.getLocationOwner();
        this.isCurrentUserLocationOwner = data.isCurrentUserLocationOwner();
        this.totalNumDevices = data.getTotalNumberDevices();

    }

    /**
     * Function that is called when the right button is click in the header
     */
    @Override
    public void onRightButtonClick() {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remove_device_btn:
                resetDevice();

                break;
        }
    }


    private void resetDevice() {

        if (device == null)
            return;

        if (device.deviceActivated) {
            showLoading(false, null);
            if (isOwner()) {
                if (!device.isBTLECompatible())
                    showCannotDeactivateDeviceDialog();
                else
                    showDeactivateDeviceDialog(false);
            } else {
                AlertUtils.showGenericAlert(
                        getActivity(),
                        getString(R.string.remove_device_not_owner_header),
                        getBodyNotOwnerDialogMessage()).show();
            }
        }
    }

    private void showCannotDeactivateDeviceDialog() {
        final AlertDialog dialogCannotDeactivate = AlertUtils.showCannotDeactivateDeviceDialog(getActivity(), new View.OnClickListener() {

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //show the customer support thing
                ZendeskUtil.showZendesk(getActivity(), 0);
            }
        });
        dialogCannotDeactivate.show();

    }

    private void showDeactivateDeviceDialog(boolean incorrectPassword) {
        dialog = AlertUtils.showRemoveDevicePrompt(
                getActivity(),
                getDialogHeader(incorrectPassword),
                getDialogBody(incorrectPassword, isLastDeviceInLocation()),
                incorrectPassword,
                new DeviceActivationDialogListener() {
                    @Override
                    public void onSubmit(String password) {
                        dialog.cancel();
                        showLoading(
                                true,
                                getString(
                                        R.string.remove_device_loading)
                        );
                        DeviceAPIService.activateDevice(device.resourceUri,
                                DeviceActivation.activation_status_deactivated, password, new Callback<Device>() {
                                    @Override
                                    public void success(Device device, Response response) {
                                        deviceDeactivated();
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        errorDeactivatingDevice(error);
                                    }
                                });
                    }

                    @Override
                    public void resetPassword() {
                        hideSoftKeyboard();
                        dialog.cancel();
                        String email = PreferencesUtils.getUserName();
                        ForgotPasswordFragment forgotPasswordFragment = ForgotPasswordFragment.newInstance(email);
                        addFragmentToStack(forgotPasswordFragment, Utils.SLIDE_FROM_RIGHT);
                    }
                });
    }

    private void deviceDeactivated() {

        showLoading(false, null);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = AlertUtils.showSuccessDialog(getActivity());
        dialog.show();
        device.deviceActivated = false;
        TinyMessageBus.postDelayed(new SuccessDone(), SUCCESS_WAIT_TIME);
    }

    @Subscribe
    public void onSuccessDone(SuccessDone successDone) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (isOnlyDevice()) {
            Intent i = new Intent(getActivity(), SetupFragmentStackActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(LOCATION_ID, device.getLocationId());
            getActivity().startActivity(i);
        } else {
            fragmentStack.popBackStack(ManageDevicesFragment.class);
        }
    }

    private boolean isOnlyDevice() {
        return this.totalNumDevices == 1;
    }


    private void errorDeactivatingDevice(RetrofitError error) {
        showLoading(false, null);
        Response response = error.getResponse();

        if (response != null && response.getStatus() == HTTP_BAD_REQUEST) {
            showDeactivateDeviceDialog(true);
        } else {
            try {
                AlertUtils.showGenericAlert(getActivity(), Utils.getErrorMessageFromRetrofit(getActivity(), error)).show();
            } catch (JSONException ignored) {
            }
        }
    }

    private String getBodyNotOwnerDialogMessage() {
        return getString(R.string.remove_device_not_owner_body, getOwnerName());
    }

    private String getDialogHeader(boolean incorrectPassword) {
        return !incorrectPassword ? getString(R.string.remove_device_header) : getString(R.string.remove_device_header_incorrect);
    }

    private String getOwnerName() {
        if (owner == null) {
            return "";
        }
        return owner.firstName;
    }

    private String getDialogBody(boolean incorrectPassword, boolean isLastInLocation) {
        if (incorrectPassword)
            return getString(R.string.remove_device_body_incorrect);

        return isLastInLocation ?
                getString(R.string.remove_device_body_last) :
                getString(R.string.remove_device_body_notlast);
    }

    private boolean isLastDeviceInLocation() {
        int totalNumDevices = 1;
        if (getArguments() != null && getArguments().getInt(key_num_devices) > 0)
            totalNumDevices = getArguments().getInt(key_num_devices);
        return totalNumDevices == 1;
    }

    private boolean isOwner() {
        return isCurrentUserLocationOwner;
    }

}
