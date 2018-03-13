package is.yranac.canary.fragments.setup;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupAdjustVolumeBinding;
import is.yranac.canary.util.PermissionUtil;
import is.yranac.canary.util.Utils;

public class AdjustVolumeFragment extends SetUpBaseFragment {


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                int micState = intent.getIntExtra("microphone", -1);
                cableIn = state == 1 && micState == 1;
                checkConditions();
            }
        }
    };

    private SettingsContentObserver mSettingsContentObserver;
    private boolean volumeRight;
    private boolean cableIn;
    private static final int MY_PERMISSIONS_REQUEST_AUDIO = 121;
    private AudioManager audioManager;

    private FragmentSetupAdjustVolumeBinding binding;

    private void checkConditions() {
        boolean granted = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;

        if (cableIn && volumeRight && granted) {
            if (getArguments().getBoolean(CHANGING_WIFI, false)) {
                PowerCycleCanaryFragment fragment = getInstance(PowerCycleCanaryFragment.class);
                addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
            } else {
                DeviceAudioSetupFragment fragment = getInstance(DeviceAudioSetupFragment.class);
                addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSetupAdjustVolumeBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        audioManager = (AudioManager) getActivity()
                .getSystemService(Context.AUDIO_SERVICE);

        final int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

                volumeRight = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) >= max;
                checkConditions();
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                seekBar.setProgress(currentVolume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.seekBar.setMax(max);

        mSettingsContentObserver = new SettingsContentObserver(new Handler());

        checkPermission();

        Animation slideAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.infinite_slide_left);
        binding.audioPulseImage.startAnimation(slideAnimation);

    }

    private void checkPermission() {
        final int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        final int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            binding.allowAccessBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestPermissions(
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            MY_PERMISSIONS_REQUEST_AUDIO);
                }
            });
        } else if (currentVolume >= max) {
            checkConditions();
        } else {
            binding.allowAccessBtn.setVisibility(View.GONE);
            binding.adjustVolumeLayout.setVisibility(View.VISIBLE);
            binding.allowAccessTextView.setText(R.string.adjust_volume);
            binding.allowAccessDscTextView.setText(R.string.your_phones_volume_to_low);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().getContentResolver().unregisterContentObserver(
                mSettingsContentObserver);

        getContext().unregisterReceiver(mReceiver);

    }

    @Override
    public void onResume() {
        super.onResume();

        getContext().getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI, true,
                mSettingsContentObserver);
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        binding.seekBar.setProgress(volume);
        fragmentStack.setHeaderTitle(R.string.activate);

        fragmentStack.showHelpButton();

        fragmentStack.enableRightButton(this, true);

        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);

        getContext().registerReceiver(mReceiver, filter);


    }


    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onRightButtonClick() {
        addModalFragment(GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_AUDIO));
    }

    private class SettingsContentObserver extends ContentObserver {

        SettingsContentObserver(Handler handler) {
            super(handler);
        }


        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            binding.seekBar.setProgress(currentVolume);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_AUDIO: {

                boolean hasPermission = PermissionUtil.hasPermission(permissions, grantResults, getActivity());

                if (hasPermission) {
                    checkPermission();
                    checkConditions();
                }
                // If request is cancelled, the result arrays are empty.
            }

        }
    }
}
