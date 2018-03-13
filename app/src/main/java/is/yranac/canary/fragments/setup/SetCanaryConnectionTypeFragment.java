package is.yranac.canary.fragments.setup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupConnectionTypeBinding;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.services.bluetooth.BluetoothSingleton;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.LocationUtil;
import is.yranac.canary.util.PermissionUtil;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_CHOOSE_CONNECTION_TYPE;

public class SetCanaryConnectionTypeFragment extends BTLEBaseFragment {


    private static final int MY_PERMISSIONS_REQUEST_LOCATION_WIFI = 65;
    private static final String LOG_TAG = "SetCanaryConnectionTypeFragment";

    private FragmentSetupConnectionTypeBinding binding;

    public static SetCanaryConnectionTypeFragment newInstance(boolean changingWifi, Device device) {
        SetCanaryConnectionTypeFragment fragment = new SetCanaryConnectionTypeFragment();
        Bundle args = new Bundle();
        args.putBoolean(CHANGING_WIFI, changingWifi);
        args.putString(DEVICE_SERIAL, device.serialNumber);
        args.putString(device_uri, device.resourceUri);
        args.putInt(device_type, device.deviceType.id);
        fragment.setArguments(args);
        return fragment;
    }

    public static SetCanaryConnectionTypeFragment newInstance(Bundle args) {
        SetCanaryConnectionTypeFragment fragment = new SetCanaryConnectionTypeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        BaseActivity activity = (BaseActivity) getActivity();
        activity.registerForOTBFailedReceive();
        activity.enableInAppNotifications();

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setup_connection_type,
                container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.connectWifiBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments().getBoolean(BLUETOOTH_SETUP, false)) {
                    //We are doing BTLE setup
                    GetCanaryWifiNetworksFragment fragment = GetCanaryWifiNetworksFragment.newInstance(getArguments(), new DeviceType(DeviceType.CANARY_AIO));
                    addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
                    return;
                }

                if (!isSetup()) {
                    //We are doing changing WIFI over btle
                    final String deviceUri = getArguments().getString(device_uri, null);

                    if (deviceUri != null) {
                        Device device = DeviceDatabaseService.getDeviceFromResourceUri(deviceUri);
                        if (device != null) {
                            if (device.isBTLECompatible() && BluetoothSingleton.checkBleHardwareAvailable()) {
                                startProcessOverBluetooth();
                                return;
                            }
                        }
                    }
                }

                getArguments().remove(BLUETOOTH_CHANGE_WIFI);


                //We have to do this if we aren't doing a bluetooth setup OR we can't change WIFI
                //over BTLE
                WifiManager mng = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (mng.isWifiEnabled()) {
                    showWifiFragment();
                } else {
                    showWifiAlertMessage();
                }
            }
        });

        binding.connectEthernetBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectEthernetCableFragment fragment = getInstance(ConnectEthernetCableFragment.class);
                addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);

            }
        });

        if (!isSetup()) {
            binding.internetConnectionTextView.setText(R.string.change_internet);
        }

    }

    private void startProcessOverBluetooth() {
        getArguments().putBoolean(BLUETOOTH_CHANGE_WIFI, true);

        FindCanariesFragment fragment = FindCanariesFragment.newInstance(getArguments());
        addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
    }

    @Override
    public void onResume() {
        super.onResume();
        getArguments().remove(BLUETOOTH_CHANGE_WIFI);
        fragmentStack.showLogoutButton(false);
        fragmentStack.enableRightButton(this, true);
        fragmentStack.setHeaderTitle(R.string.connect);
        fragmentStack.showHelpButton();
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentStack.resetButtonStyle();
    }

    private void showWifiFragment() {
        if (LocationUtil.doesNotHaveLocationPermission(getActivity())) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION_WIFI);

            return;
        }


        LocalWifiNetworksFragment fragment = LocalWifiNetworksFragment.newInstance(getArguments());
        addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
    }

    private void showWifiAlertMessage() {
        AlertUtils.showGenericAlert(getActivity(), getString(R.string.oops_wifi), getString(R.string.turn_wifi_on_to_connect), 0, getString(R.string.cancel), getString(R.string.go_to_settings), 0, 0, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(
                        Settings.ACTION_WIFI_SETTINGS));

            }
        });
    }

    @Override
    public void onRightButtonClick() {
        GetHelpFragment fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_INTERNET);
        addModalFragment(fragment);
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_CHOOSE_CONNECTION_TYPE;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION_WIFI: {
                // If request is cancelled, the result arrays are empty.

                boolean hasPermission = PermissionUtil.hasPermission(permissions, grantResults, getActivity());
                if (hasPermission) {
                    showWifiFragment();
                }

                break;
            }

        }
    }

}
