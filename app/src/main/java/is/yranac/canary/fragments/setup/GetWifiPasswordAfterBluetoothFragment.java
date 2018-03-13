package is.yranac.canary.fragments.setup;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupConfirmNetworkBinding;
import is.yranac.canary.fragments.settings.DeviceNamingFragment;
import is.yranac.canary.fragments.settings.EditDeviceFragment;
import is.yranac.canary.messages.SuccessDone;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.nativelibs.models.messages.BLEError;
import is.yranac.canary.nativelibs.models.messages.setup.BLESetupAuthCredsResponse;
import is.yranac.canary.nativelibs.models.messages.setup.BLESetupWifiCredsResponse;
import is.yranac.canary.services.bluetooth.BluetoothSingleton;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_BLUETOOTH_SUCCESS;

/**
 * A simple {@link Fragment} subclass.
 */
public class GetWifiPasswordAfterBluetoothFragment extends BTLEBaseFragment implements View.OnClickListener {


    private String wifiSSID;
    private AlertDialog sucessAlert;
    private BLESetupWifiCredsResponse successfulResponse;

    private FragmentSetupConfirmNetworkBinding binding;

    public static GetWifiPasswordAfterBluetoothFragment newInstance(String ssid, Bundle args) {

        args.putString(SSID, ssid);
        GetWifiPasswordAfterBluetoothFragment fragment = new GetWifiPasswordAfterBluetoothFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSetupConfirmNetworkBinding.inflate(inflater);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);

        Bundle args = getArguments();
        wifiSSID = args.getString(SSID);
        String enterWifiPassword;

        if (TextUtils.isEmpty(wifiSSID)) {
            binding.wifiNameContainer.setVisibility(View.VISIBLE);
            enterWifiPassword = getString(R.string.enter_wifi_name_and_password);
            binding.wifiNameContainer.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    wifiSSID = s.toString();
                    binding.nextBtn.setEnabled(!TextUtils.isEmpty(wifiSSID));
                }
            });

        } else {
            binding.wifiNameContainer.setVisibility(View.GONE);
            enterWifiPassword = String.format(getString(R.string.enter_password), wifiSSID);
        }
        binding.nextBtn.setEnabled(!TextUtils.isEmpty(wifiSSID));
        binding.wifiNameTextView.setText(enterWifiPassword);
        onClickFocusListener(binding.wifiPasswordContainer, binding.wifiPasswordContainer.getEditText());
        PasswordOnClickListener passwordOnClickListener = new PasswordOnClickListener(binding.showWifiPasswordCheckBox, binding.wifiPasswordContainer.getEditText());
        binding.showPasswordContainer.setOnClickListener(passwordOnClickListener);

        binding.nextBtn.setOnClickListener(this);

        if (isSetup()) {
            binding.nextBtn.setText(R.string.next);
        } else {
            binding.nextBtn.setText(R.string.okay);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        fragmentStack.showHelpButton();
        fragmentStack.enableRightButton(this, true);

    }

    @Override
    public void onRightButtonClick() {
        GetHelpFragment fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_INTERNET);
        addModalFragment(fragment);
    }

    private void startMessage(String wifiPassword) {
        showLoading(true, getString(R.string.connecting_nodots), false);
        successfulResponse = null;
        BluetoothSingleton.getBluetoothSetupHelper().sendWifiCredentials(wifiSSID, wifiPassword, isSetup());
    }


    @Subscribe
    public void onWifiResponse(BLESetupWifiCredsResponse response) {

        if (response.isSuccess()) {
            successfulResponse = response;
            if (!isSetup()) {
                showLoading(false, null);
                sucessAlert = AlertUtils.showSuccessDialog(getActivity(), getString(R.string.connected));
                sucessAlert.show();
                TinyMessageBus.postDelayed(new SuccessDone(), 500);
            } else {
                BluetoothSingleton.getBluetoothSetupHelper().sendAuthToken(getArguments().getString(activation_token), false);
            }
        } else {
            showLoading(false, null);
            AlertUtils.showGenericAlert(getActivity(),
                    getActivity().getString(R.string.btle_wifi_error_header),
                    response.getMeaningfulErrorMessage());
        }
    }

    private void setupIsComplete() {
        showLoading(false, null);
        sucessAlert = AlertUtils.showSuccessDialog(getActivity(), getString(R.string.connected));

        if (sucessAlert != null)
            sucessAlert.show();

        TinyMessageBus.postDelayed(new SuccessDone(), 500);
    }

    @Subscribe
    public void onSucessDone(SuccessDone sucessDone) {
        GoogleAnalyticsHelper.trackBluetoothEvent(!isSetup(), ACTION_BLUETOOTH_SUCCESS, null);

        if (sucessAlert != null)
            sucessAlert.dismiss();

        if (!isSetup()) {
            fragmentStack.popBackStack(EditDeviceFragment.class);
            BluetoothSingleton.reset();
            return;
        }

        int deviceType = getArguments().getInt(device_type);

        Fragment fragment;
        final String deviceUri = getArguments().getString(device_uri);
        if (deviceType == DeviceType.FLEX) {
            fragment = PlacementSuggestionsFragment.newInstance(getArguments(), deviceType, 0, deviceUri, true, "", false);
        } else {
            fragment = DeviceNamingFragment.newInstance(getArguments(), false);
        }

        addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);

    }

    @Subscribe
    public void onTokenAcknowledged(BLESetupAuthCredsResponse response) {
        BluetoothSingleton.reset();
        setupIsComplete();
    }

    @Override
    @Subscribe
    public void onError(BLEError error) {
        if (successfulResponse != null && successfulResponse.isSuccess())
            return;
        super.onError(error);
    }

    private void showWarning() {

        AlertUtils.showNewGreenGenericAlert(getContext(), getString(R.string.continue_without_password),
                getString(R.string.recommend_secure_network),
                getString(R.string.continue_text),
                getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startMessage("");
                    }
                }, null);
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_btn:

                String wifiPassword = binding.wifiPasswordContainer.getText();

                if (wifiPassword.length() == 0) {
                    showWarning();
                    return;
                }

                startMessage(wifiPassword);
                break;
        }
    }
}
