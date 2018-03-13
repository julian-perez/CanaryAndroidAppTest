package is.yranac.canary.fragments.setup;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.adapter.BluetoothDeviceAdapter;
import is.yranac.canary.databinding.FragmentFindBluetoothDevicesBinding;
import is.yranac.canary.media.CanaryVideoPlayer;
import is.yranac.canary.media.PlayerVideoInitializer;
import is.yranac.canary.messages.OnBackPressed;
import is.yranac.canary.messages.SuccessDone;
import is.yranac.canary.messages.UpdateCachedDevice;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.nativelibs.models.messages.BLEError;
import is.yranac.canary.nativelibs.models.messages.bluetooth.BLTEventDeviceConnected;
import is.yranac.canary.nativelibs.models.messages.bluetooth.BLTEventDeviceFound;
import is.yranac.canary.nativelibs.models.messages.setup.BLESetupInitResponse;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.bluetooth.BluetoothHelperService;
import is.yranac.canary.services.bluetooth.BluetoothSingleton;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.LocationUtil;
import is.yranac.canary.util.PermissionUtil;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_BLUETOOTH_START;

/**
 * Created by Schroeder on 8/24/15.
 */
public class FindCanariesFragment extends BTLEBaseFragment {


    private static final String LOG_LOG = "FindCanariesFragment";
    private BluetoothDeviceAdapter adapter;
    private AlertDialog alert;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 67;

    private FragmentFindBluetoothDevicesBinding binding;
    private int deviceType;
    private CanaryVideoPlayer player;
    private DeviceType selectedDeviceType;

    public static FindCanariesFragment newInstance(Bundle arguments) {
        FindCanariesFragment fragment = new FindCanariesFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    private List<String> videos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_bluetooth_devices, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.mainListView.setEmptyView(binding.emptyView);
        deviceType = getArguments().getInt(device_type, 0);
        binding.setDeviceType(deviceType);
        binding.mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothSingleton.reset();
                showLoading(true, getString(R.string.pairing), false);
                BluetoothDevice device = (BluetoothDevice) adapter.getItem(position);
                BluetoothSingleton.getBluetoothHelperService().establishBTLEConnection(device.getAddress());
                TinyMessageBus.postDelayed(new BLEError(BLEError.ERROR.TIMEOUT), 30000);
                GoogleAnalyticsHelper.trackBluetoothEvent(isSetup(), ACTION_BLUETOOTH_START, null);
            }
        });


        int stringTitle;
        int stringDsc;
        if (isSetup()) {
            stringDsc = R.string.press_button_on_back;
        } else {
            stringDsc = R.string.press_button_on_back_wifi;
        }
        int stringDsc2 = R.string.confirm_device;
        switch (deviceType) {
            case DeviceType.CANARY_AIO:
                binding.canaryPairImageView.setImageResource(R.drawable.pair_aio);
                stringTitle = R.string.pair_canary;
                if (isSetup()) {
                    stringDsc = R.string.touch_the_top_canary;
                } else {
                    stringDsc = R.string.touch_the_top_canary_wifi;
                }
                break;
            case DeviceType.FLEX: {
                String uri = "asset:///pair_flex1.mp4";
                String uri2 = "asset:///pair_flex2.mp4";
                videos.add(uri);
                videos.add(uri2);
                stringTitle = R.string.pair_canary_flex;
                initVideoPLayer();
                break;
            }
            case DeviceType.CANARY_VIEW: {
                binding.canaryPairImageView.setImageResource(R.drawable.pair_view);
                stringTitle = R.string.pair_canary_view;
                if (isSetup()) {
                    stringDsc = R.string.touch_the_top_canary;
                } else {
                    stringDsc = R.string.touch_the_top_canary_wifi;
                }
                break;
            }
            default:
                return;
        }
        binding.pairTitle.setText(stringTitle);
        binding.pairTitleTwo.setText(stringTitle);
        binding.pairDsc.setText(stringDsc);
        binding.pairDscTwo.setText(stringDsc2);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.emptyLinearLayout.getLayoutParams();
        if (deviceType == DeviceType.CANARY_AIO || deviceType == DeviceType.CANARY_VIEW) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
    }

    private void initVideoPLayer() {
        player = new CanaryVideoPlayer(getContext(), CanaryVideoPlayer.VideoType.VideoTypeMP4);
        player.addListener(new CanaryVideoPlayer.Listener() {
            @Override
            public void onStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case CanaryVideoPlayer.STATE_READY:
                        break;
                    case CanaryVideoPlayer.STATE_ENDED:
                        if (player.getDataSource().equalsIgnoreCase(videos.get(0))) {
                            player.setDataSource(videos.get(1));
                            player.prepare();
                            player.seekTo(0);
                            player.setPlayWhenReady(true);
                        } else if (player.getDataSource().equalsIgnoreCase(videos.get(1))) {
                            player.seekTo(0);
                            player.setPlayWhenReady(true);
                        } else {
                            player.setPlayWhenReady(false);
                        }
                        break;

                }
            }
        });
        binding.textureView.setSurfaceTextureListener(new PlayerVideoInitializer(player, videos.get(0), binding.textureView));
    }

    @Subscribe
    public void bltDeviceConnected(BLTEventDeviceConnected deviceConnected) {
        if (deviceConnected.getDevice() == null) {
            showLoading(false, null);
            return;
        }

        selectedDeviceType = Utils.getDeviceTypeFromDeviceSerialNumber(deviceConnected.getDevice().getName());

        if (isSetup()) {
            createDevice(deviceConnected.getDevice().getName());
        } else {
            BluetoothSingleton.getBluetoothSetupHelper().startTheProcess(getDeviceSerial(), selectedDeviceType);
        }
    }

    @Subscribe
    public void deviceResponded(BLESetupInitResponse deviceConnected) {
        setDeviceSerial(deviceConnected.getSERIAL());
        showLoading(false, null);
        pairedComplete();
    }

    private void pairedComplete() {

        showLoading(false, null);
        alert = AlertUtils.showSuccessDialog(getActivity(), getString(R.string.paired));

        if (alert != null)
            alert.show();


        TinyMessageBus.postDelayed(new SuccessDone(), 500);
    }

    @Subscribe
    public void onSucessDone(SuccessDone sucessDone) {

        if (alert != null)
            alert.dismiss();

        if (selectedDeviceType != null && selectedDeviceType.id != DeviceType.CANARY_AIO) {
            GetCanaryWifiNetworksFragment fragment = GetCanaryWifiNetworksFragment.newInstance(getArguments(), selectedDeviceType);
            addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
        } else if (!isSetup()) {
            GetCanaryWifiNetworksFragment fragment = GetCanaryWifiNetworksFragment.newInstance(getArguments(), selectedDeviceType);
            addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
        } else {
            getArguments().putBoolean(BLUETOOTH_SETUP, true);
            SetCanaryConnectionTypeFragment fragment = getInstance(SetCanaryConnectionTypeFragment.class);
            addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
        }

    }


    private void createDevice(final String deviceSerial) {
        DeviceType type = Utils.getDeviceTypeFromDeviceSerialNumber(deviceSerial);

        DeviceAPIService.createDevice(deviceSerial, locationUri(), type.name, new Callback<Device>() {
            @Override
            public void success(Device device, Response response) {
                TinyMessageBus.post(new UpdateCachedDevice(device));
                getArguments().putString(device_uri, device.resourceUri);
                getArguments().putString(activation_token, device.activationToken);
                BluetoothSingleton.getBluetoothSetupHelper().startTheProcess(device.serialNumber, device.deviceType);
            }

            @Override
            public void failure(RetrofitError error) {
                BluetoothSingleton.reset();
                showLoading(false, null);
                if (error.getKind() == RetrofitError.Kind.NETWORK) {
                    AlertUtils.showGenericAlert(getActivity(), getString(R.string.unable_to_connect),
                            getString(R.string.unable_to_connect_check_internet), 0,
                            getString(R.string.get_help), getString(R.string.try_again), 0, 0, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    GetHelpFragment fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_BLUETOOTH);
                                    addModalFragment(fragment);
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    createDevice(deviceSerial);
                                }
                            });
                    return;
                }

                if (error.getResponse().getStatus() == 400) {
                    AlertUtils.showGenericAlert(getActivity(), getString(R.string.device_already_activated),
                            getString(R.string.please_contact_support_for_further_assistance), 0,
                            getString(R.string.get_help), getString(R.string.try_again), 0, 0, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    GetHelpFragment fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_BLUETOOTH);
                                    addModalFragment(fragment);
                                }
                            }, null);
                    return;
                }
                AlertUtils.showGenericAlert(getActivity(), getString(R.string.device_activation_failed),
                        getString(R.string.please_try_again_contact_support), 0,
                        getString(R.string.get_help), getString(R.string.try_again), 0, 0, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GetHelpFragment fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_BLUETOOTH);
                                addModalFragment(fragment);
                            }
                        }, null);
            }
        });
    }

    @Subscribe
    public void bltDeviceFound(BLTEventDeviceFound deviceFoundEvent) {
        if (!isSetup()) {
            if (getArguments() != null &&
                    deviceFoundEvent.device.getName().equalsIgnoreCase(getDeviceSerial())) {
                showLoading(true, getString(R.string.pairing));
                BluetoothDevice device = deviceFoundEvent.device;
                BluetoothSingleton.getBluetoothHelperService().establishBTLEConnection(device.getAddress());
                GoogleAnalyticsHelper.trackBluetoothEvent(!isSetup(), ACTION_BLUETOOTH_START, null);
            }
            return;
        }
        adapter.add(deviceFoundEvent.device);
    }

    @Subscribe
    @Override
    public void onError(BLEError error) {
        switch (error.getError()) {
            case DISCONNECT:
                adapter.clear();
                break;
            case TIMEOUT:
                super.onError(new BLEError(BLEError.ERROR.PAIR_FAIL));
                return;
        }

        super.onError(error);
    }

    @Override
    public void onStart() {
        super.onStart();
        BluetoothSingleton.reset();
        initializeBluetooth();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (adapter != null)
            adapter.clear();

        adapter = new BluetoothDeviceAdapter(getActivity());
        binding.mainListView.setAdapter(adapter);


        showLoading(false, null);
        switch (deviceType) {
            case DeviceType.CANARY_AIO:
                fragmentStack.setHeaderTitle(R.string.canary);
                break;
            case DeviceType.FLEX:
                fragmentStack.setHeaderTitle(R.string.canary_flex);
                break;
            case DeviceType.CANARY_VIEW:
                fragmentStack.setHeaderTitle(R.string.canary_flex);
                break;
        }

        fragmentStack.enableRightButton(this, true);
        fragmentStack.showHelpButton();
    }

    @Override
    public void onPause() {
        super.onPause();
        BluetoothSingleton.getBluetoothHelperService().stopScanningForDevices();
    }

    @Override
    public void onStop() {
        super.onStop();
        BluetoothSingleton.getBluetoothHelperService().stopScanningForDevices();
    }

    @Override
    public void onRightButtonClick() {
        GetHelpFragment fragment;
        if (deviceType == DeviceType.CANARY_AIO && isSetup()) {
            fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_CANARY_ONE_SETUP);
        } else if (isSetup()) {
            fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_BLUETOOTH);
        } else {
            fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_CHANGE_WIFI);

        }

        fragment.setLocationUri(locationUri());
        addModalFragment(fragment);
        BluetoothSingleton.getBluetoothHelperService().stopScanningForDevices();
        getActivity().getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                BluetoothSingleton.getBluetoothHelperService().startScanningForDevices();
                getActivity().getSupportFragmentManager().removeOnBackStackChangedListener(this);
            }
        });
    }


    @Subscribe
    public void onBackPressed(OnBackPressed onBackPressed) {
        BluetoothSingleton.getBluetoothHelperService().disconnect();
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null)
            player.release();
    }

    public static FindCanariesFragment newInstance(boolean changingWifi, Device device) {
        FindCanariesFragment fragment = new FindCanariesFragment();
        Bundle args = new Bundle();

        args.putBoolean(CHANGING_WIFI, changingWifi);
        args.putBoolean(key_isSetup, !changingWifi);
        args.putString(DEVICE_SERIAL, device.serialNumber);
        args.putInt(device_type, device.deviceType.id);
        args.putString(device_uri, device.resourceUri);
        fragment.setArguments(args);
        fragment.setLocationUri(device.location);
        return fragment;
    }


    private void initializeBluetooth() {
        if (LocationUtil.doesNotHaveLocationPermission(getActivity())) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }

        if (!LocationUtil.canGetLocation(getContext())) {
            String title = getString(R.string.location_services);
            String dsc = getString(R.string.location_services_btle);
            String settings = getString(R.string.go_to_settings);
            String cancel = getString(R.string.cancel);
            alert = AlertUtils.showGenericAlert(getContext(), title, dsc, 0, settings, cancel, 0, 0, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
            return;
        }
        if (!BluetoothSingleton.getBluetoothHelperService().isAdapterEnabled()) {
            Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intentBtEnabled, BluetoothHelperService.BLUETOOTH_START_REQUESTED);
        } else {
            BluetoothSingleton.getBluetoothHelperService().startScanningForDevices();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                boolean hasPermission = PermissionUtil.hasPermission(permissions, grantResults, getActivity());

                if (hasPermission) {
                    initializeBluetooth();
                }
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothHelperService.BLUETOOTH_START_REQUESTED) {
            if (resultCode == Activity.RESULT_OK) {
                initializeBluetooth();
            } else {
                getActivity().onBackPressed();
            }
        }
    }
}
