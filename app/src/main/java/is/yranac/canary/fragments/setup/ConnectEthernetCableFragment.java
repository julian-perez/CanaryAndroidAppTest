package is.yranac.canary.fragments.setup;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupConnectEthernetBinding;
import is.yranac.canary.fragments.settings.DeviceNamingFragment;
import is.yranac.canary.fragments.settings.EditDeviceFragment;
import is.yranac.canary.media.CanaryVideoPlayer;
import is.yranac.canary.media.PlayerVideoInitializer;
import is.yranac.canary.messages.CheckDeviceStatus;
import is.yranac.canary.messages.SuccessDone;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.nativelibs.models.messages.BLEError;
import is.yranac.canary.nativelibs.models.messages.setup.BLESetupAuthCredsResponse;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.bluetooth.BluetoothSingleton;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.AnalyticsConstants;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Schroeder on 9/23/14.
 */

public class ConnectEthernetCableFragment extends BTLEBaseFragment {

    private static final String LOG_TAG = "PlaceCanaryFragment";
    private CanaryVideoPlayer player;

    private AlertDialog sucessAlert;
    private BLESetupAuthCredsResponse response;

    private FragmentSetupConnectEthernetBinding binding;

    @Override
    protected String getAnalyticsTag() {
        return AnalyticsConstants.SCREEN_ETHERNET_SETUP;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSetupConnectEthernetBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int deviceType = getArguments().getInt(device_type);

        binding.setDeviceType(deviceType);
        switch (deviceType) {
            case DeviceType.CANARY_AIO:
                binding.contentImage2.setImageResource(R.drawable.ethernet_aio);
                binding.contentText.setText(R.string.plug_an_ethernet_cable);
                break;
        }

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getArguments().getBoolean(CHANGING_WIFI, false)) {
                    fragmentStack.popBackStack(EditDeviceFragment.class);
                } else {
                    if (getArguments().getBoolean(BLUETOOTH_SETUP, false)) {
                        showLoading(true, getString(R.string.activating));
                        response = null;
                        getActivity().getWindow().addFlags(
                                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        BluetoothSingleton.getBluetoothSetupHelper().sendAuthToken(getArguments().getString(activation_token), !changingWifi());
                        TinyMessageBus.postDelayed(new CheckDeviceStatus(), 5000);
                    } else {
                        ConnectCableSetUpFragment fragment = getInstance(ConnectCableSetUpFragment.class);
                        addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
                    }
                }
            }
        });


        if (getArguments().getBoolean(CHANGING_WIFI, false))
            binding.nextBtn.setText(R.string.okay);
        else
            binding.nextBtn.setText(R.string.next);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.showLogoutButton(false);
        fragmentStack.setHeaderTitle(R.string.connect);

    }

    @Override
    public void onRightButtonClick() {
    }

    @Subscribe
    public void checkDeviceStatus(CheckDeviceStatus checkDeviceStatus) {

        final String deviceUri = getArguments().getString(device_uri);
        DeviceAPIService.getDeviceByUri(deviceUri, new Callback<Device>() {
            @Override
            public void success(final Device device, Response response) {

                if (device.failedOTA()) {

                    AlertDialog alertDialog = AlertUtils.showGenericAlert(getActivity(), getString(R.string.error_occurred_ota), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fragmentStack.popBackToWifi();
                        }
                    });


                    if (alertDialog != null) {
                        alertDialog.setCancelable(false);
                        alertDialog.setCanceledOnTouchOutside(false);
                    }
                    return;
                } else if (!device.ota_status.equalsIgnoreCase("inactive")) {
                    showLoading(false, null);
                    setupIsComplete();
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


    @Subscribe
    public void onTokenAcknowledged(BLESetupAuthCredsResponse response) {
        this.response = response;
        BluetoothSingleton.reset();
        setupIsComplete();
    }


    private void setupIsComplete() {
        showLoading(false, null);
        sucessAlert = AlertUtils.showSuccessDialog(getActivity(), getString(R.string.activated));

        if (sucessAlert != null)
            sucessAlert.show();

        TinyMessageBus.postDelayed(new SuccessDone(), 500);
    }

    @Override
    @Subscribe
    public void onError(BLEError connectionReset) {
        if (response != null)
            return;
        super.onError(connectionReset);
    }

    @Subscribe
    public void onSucessDone(SuccessDone sucessDone) {

        getActivity().getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        showLoading(false, null);
        getArguments().putBoolean(key_isSetup, true);
        DeviceNamingFragment fragment = DeviceNamingFragment.newInstance(getArguments(), false);
        addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
        if (sucessAlert != null)
            sucessAlert.dismiss();
    }

    private void initVideoPLayer() {
        player = new CanaryVideoPlayer(getContext(), CanaryVideoPlayer.VideoType.VideoTypeMP4);
        String ethernetVideo = "asset:///ethernet_plus.mp4";
        binding.textureView.setSurfaceTextureListener(new PlayerVideoInitializer(player, ethernetVideo, binding.textureView));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null)
            player.release();
    }
}
