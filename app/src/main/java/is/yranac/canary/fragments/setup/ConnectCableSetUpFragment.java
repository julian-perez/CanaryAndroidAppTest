package is.yranac.canary.fragments.setup;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupConnectCableBinding;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_SECURE_SETUP_CABLE;

public class ConnectCableSetUpFragment extends SetUpBaseFragment {

    private FragmentSetupConnectCableBinding binding;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                int micState = intent.getIntExtra("microphone", -1);
                if (state == 1 && micState != 1) {
                    binding.nextBtn.setEnabled(false);
                } else if (state == 1) {
                    binding.nextBtn.setEnabled(true);
                } else {
                    binding.nextBtn.setEnabled(false);
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSetupConnectCableBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String wifiName = getArguments().getString(SSID);
        if (wifiName == null) {
            binding.contentImage.setImageResource(R.drawable.audio_ethernet_aio);
        } else {
            binding.contentImage.setImageResource(R.drawable.audio_wifi_aio);
        }
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.showLogoutButton(false);
        fragmentStack.showHelpButton();
        fragmentStack.enableRightButton(this, true);
        fragmentStack.setHeaderTitle(R.string.canary);

        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        getActivity().getApplicationContext().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getApplicationContext().unregisterReceiver(mReceiver);

    }

    @Override
    public void onRightButtonClick() {
        addModalFragment(GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_AUDIO));
    }

    private void checkPermissions() {
        final AudioManager audioManager = (AudioManager) getActivity()
                .getSystemService(Context.AUDIO_SERVICE);

        final int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        final int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        boolean granted = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;

        if (currentVolume >= max && granted)
            gotToAudioSetup();
        else
            goToVolumeAdjust();

    }

    private void gotToAudioSetup() {
        if (getArguments().getBoolean(CHANGING_WIFI, false)) {
            PowerCycleCanaryFragment fragment = getInstance(PowerCycleCanaryFragment.class);
            addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
        } else {
            DeviceAudioSetupFragment fragment = getInstance(DeviceAudioSetupFragment.class);
            addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
        }
    }

    private void goToVolumeAdjust() {
        AdjustVolumeFragment fragment = getInstance(AdjustVolumeFragment.class);
        addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_SECURE_SETUP_CABLE;
    }

    public static ConnectCableSetUpFragment newInstance(Bundle bundle) {
        ConnectCableSetUpFragment fragment = new ConnectCableSetUpFragment();

        fragment.setArguments(bundle);
        return fragment;
    }


}
