package is.yranac.canary.fragments.setup;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupDeviceAudioBinding;
import is.yranac.canary.fragments.settings.DeviceNamingFragment;
import is.yranac.canary.fragments.settings.EditDeviceFragment;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.nativelibs.nativeclasses.BlackBox;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.anim.ResizeWidthAnimation;
import is.yranac.canary.util.ga.AnalyticsConstants;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_AUDIO_FAILURE;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_AUDIO_SETUP;

public class DeviceAudioSetupFragment extends SetUpBaseFragment {

    private static final String LOG_TAG = "DeviceAudioSetupFragment";
    private boolean isRecording;
    private AudioRecord recorder;
    private AudioManager audioManager;
    private Thread recordingThread = null;
    private Thread playingThread = null;
    private AudioTrack audioTrack;

    private JSONObject lastJSONRequest;
    private boolean audioSetupComplete;

    private boolean cableIsPluggedIn = true;


    private Activity mActivity;
    private String deviceUri;

    private float volumeLevel;
    private String activationToken;

    private String encodedDestination;

    private FragmentSetupDeviceAudioBinding binding;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction()
                    .equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                int micState = intent.getIntExtra("microphone", -1);

                if (audioSetupComplete) {
                    if (state != 1)
                        binding.nextBtn.setEnabled(true);

                    return;
                }
                if (state == 1 && micState == 1) {
                    cableIsPluggedIn = true;
                    playingThread = null;
                    startTone(true);
                } else {
                    audioTrack = null;
                    stopRecording();
                    playingThread = null;
                    cableIsPluggedIn = false;
                    audioSetupComplete = true;

                    String unableToConnect = getString(R.string.canary_cannot_detect_cable);
                    String unableToConnectDsc = getString(R.string.confirm_audio_cable_is_in);
                    String tryAgain = getString(R.string.try_again);

                    String viewHelp = getString(R.string.view_help);

                    AlertDialog alertDialog = AlertUtils.showNewGreenGenericAlert(getContext(), unableToConnect,
                            unableToConnectDsc, tryAgain, viewHelp, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    fragmentStack.popBackStack(SetCanaryConnectionTypeFragment.class);
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    addModalFragment(GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_AUDIO));
                                    fragmentStack.popBackStack(SetCanaryConnectionTypeFragment.class);
                                }
                            });

                    if (alertDialog != null) {
                        alertDialog.setCancelable(false);
                        alertDialog.setCanceledOnTouchOutside(false);
                    }


                }
            }
        }
    };


    private void showPowerCycleAlert() {
        GoogleAnalyticsHelper.trackAudioEvent(changingWifi(), ACTION_AUDIO_FAILURE, null);
        audioSetupComplete = true;
        mActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        String unableToConnect = getString(R.string.cannot_connect_canary);
                        String unableToConnectDsc = getString(R.string.cannot_hear_canary_dsc);
                        String tryAgain = getString(R.string.try_again);
                        String viewHelp = getString(R.string.view_help);

                        AlertDialog alertDialog = AlertUtils.showNewGreenGenericAlert(getContext(), unableToConnect, unableToConnectDsc, tryAgain, viewHelp, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                fragmentStack.popBackStack(SetCanaryConnectionTypeFragment.class);
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addModalFragment(GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_AUDIO));
                                fragmentStack.popBackStack(SetCanaryConnectionTypeFragment.class);
                            }
                        });

                        if (alertDialog != null) {
                            alertDialog.setCancelable(false);
                            alertDialog.setCanceledOnTouchOutside(false);
                        }
                    }
                });

    }

    @Override
    public void onRightButtonClick() {
        GetHelpFragment fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_AUDIO);
        addModalFragment(fragment);

    }

    private void finalizeActivation() {
        createActivationJSON(activationToken);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BaseActivity activity = (BaseActivity) getActivity();
        activity.unregisterForOTAFailedReceive();
        activity.disableInAppNotifications();

        binding = FragmentSetupDeviceAudioBinding.inflate(inflater);
        return binding.getRoot();

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        encodedDestination = getActivity().getFilesDir() + "encoded_file.txt";

        audioSetupComplete = false;
        JSONObject firstJSONRequest = new JSONObject();
        try {
            firstJSONRequest.put("ID", "01");
            firstJSONRequest.put("LEN", String.valueOf(wifiJSON().toString().length()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lastJSONRequest = firstJSONRequest;

        audioManager = (AudioManager) mActivity
                .getSystemService(Context.AUDIO_SERVICE);
        setProgressYellowImagesWithString(1);


        GoogleAnalyticsHelper.trackAudioEvent(changingWifi(), AnalyticsConstants.ACTION_AUDIO_START, null);


        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (changingWifi()) {
                    fragmentStack.popBackStack(EditDeviceFragment.class);
                } else {
                    getArguments().putBoolean(key_isSetup, true);
                    DeviceNamingFragment fragment = DeviceNamingFragment.newInstance(getArguments(), false);
                    addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();


        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        mActivity.getApplicationContext().registerReceiver(mReceiver, filter);

        if (audioSetupComplete)
            return;

        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT | AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);
        mActivity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        fragmentStack.enableRightButton(this, true);
        fragmentStack.disableBackButton();
        fragmentStack.setHeaderTitle(R.string.canary);
        fragmentStack.showHelpButton();

    }

    @Override
    public void onPause() {
        super.onPause();
        releaseAudioRecording();
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseAudioRecording();
    }

    private void releaseAudioRecording() {
        mActivity.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try {
            mActivity.getApplicationContext().unregisterReceiver(mReceiver);
        } catch (IllegalArgumentException ignored) {
        }

        audioTrack = null;
        stopRecording();

        retryHandler.removeCallbacks(retryRunnable);
        retryHandler.removeCallbacks(firstRetryRunnable);
        decodeHandler.removeCallbacks(decodeRunnable);
        mHandler.removeCallbacks(mStatusChecker);
        retryHandler.removeCallbacks(mOTAStartCheck);

        if (!audioSetupComplete) {
            audioSetupComplete = true;
            fragmentStack.popBackToWifi();
        }
    }

    private final Handler decodeHandler = new Handler();
    private final int RECORDER_SAMPLE_RATE = 44100;
    private final int RECORDER_OUT_CHANNELS = AudioFormat.CHANNEL_OUT_MONO;
    private final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;


    private final int RECORDER_IN_CHANNELS = AudioFormat.CHANNEL_IN_MONO;

    private void adjustVolume() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * volumeLevel), 0);
    }

    private void startTone(final boolean listen) {
        stopRecording();
        adjustVolume();


        if (playingThread != null)
            return;

        playingThread = new Thread(new Runnable() {

            public void run() {

                final String lastStringRequest = lastJSONRequest.toString();

                BlackBox.instance().encode(encodedDestination, lastStringRequest);


                if (!cableIsPluggedIn)
                    return;

                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                int minBufferSize = AudioTrack.getMinBufferSize(RECORDER_SAMPLE_RATE,
                        RECORDER_OUT_CHANNELS,
                        RECORDER_AUDIO_ENCODING);

                int bufferSize = 512;
                audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                        RECORDER_SAMPLE_RATE,
                        RECORDER_OUT_CHANNELS,
                        RECORDER_AUDIO_ENCODING,
                        minBufferSize,
                        AudioTrack.MODE_STREAM);
                float maxVolume = AudioTrack.getMaxVolume();
                audioTrack.setStereoVolume(maxVolume, maxVolume);

                byte[] s = new byte[bufferSize];
                File pcmFile = new File(encodedDestination);
                FileInputStream fin = null;
                DataInputStream dis = null;
                try {

                    fin = new FileInputStream(pcmFile);
                    dis = new DataInputStream(fin);
                    int i;
                    audioTrack.play();
                    while ((i = dis.read(s, 0, bufferSize)) > -1) {
                        if (audioTrack == null)
                            return;
                        audioTrack.write(s, 0, i);
                    }

                    int x;
                    do {
                        if (audioTrack == null)
                            return;
                        x = audioTrack.getPlaybackHeadPosition();
                    } while (x < pcmFile.length() / 2);

                    audioTrack.stop();
                    audioTrack.release();
                    audioTrack = null;
                    if (listen) {
                        startRecording();
                    } else {
                        setupIsComplete();
                        playingThread = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    pcmFile.delete();
                    if (dis != null) {
                        try {
                            dis.close();
                        } catch (IOException ignore) {
                        }
                    }
                    if (fin != null) {
                        try {
                            fin.close();
                        } catch (IOException ignore) {
                        }
                    }
                }

            }
        });

        playingThread.start();
    }

    private int bufferSize;

    private void nextTone(JSONObject response) {
        try {
            int id = response.getInt("ID");
            switch (id) {
                case 2:
                    stopRecording();
                    setProgressYellowImagesWithString(2);
                    String serial = response.getString("SERIAL");
                    if (!response.has("VERSION") && getArguments().getBoolean(WIFI_HIDDEN, false)) {
                        showPopupHiddenNetworkPopup();
                        return;
                    }
                    if (getArguments().getBoolean(CHANGING_WIFI, false))
                        checkSerial(serial);
                    else
                        createDevice(serial);

                    break;
                case 4:
                    wifiCheck(response);
                    break;
                case 6:
                    stopRecording();
                    startOTATimeout();
                    break;
                default:
                    Log.i(LOG_TAG, "HOUSTON WE HAVE A PROBLEM!!!!!!!!");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    static int wifiCount = 0;

    private void wifiCheck(JSONObject response) {
        try {
            String wifiCode = response.getString("VALUE");
            if (wifiCode.equalsIgnoreCase("S")) {
                stopRecording();

                boolean isChangingWifi = getArguments().getBoolean(CHANGING_WIFI, false);
                if (isChangingWifi) {
                    wifiChangeIsComplete();
                } else {
                    finalizeActivation();
                }
            } else if (wifiCode.equalsIgnoreCase("R")) {
                createRetryHandler();

                System.gc();
                lastAudio = currentAudio;
                currentAudio = new short[]{};
                scheduleDecode();

            } else if (wifiCode.equalsIgnoreCase("F")) {
                stopRecording();

                String errorCode = response.getString("ERROR_CODE");
                if (errorCode.equalsIgnoreCase("timeout") || errorCode.equalsIgnoreCase("wifi-not-found")) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String unableToConnect = getString(R.string.unable_to_connect_wifi);
                            String unableToConnectDsc = getString(R.string.unable_to_connect_dsc);
                            String getHelp = getString(R.string.get_help);
                            String tryAgain = getString(R.string.try_again);

                            View.OnClickListener leftClickListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    GetHelpFragment fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_INTERNET);
                                    addModalFragment(fragment);
                                }
                            };


                            View.OnClickListener rightClickListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    audioSetupComplete = true;
                                    fragmentStack.popBackToWifi();
                                }
                            };


                            AlertDialog alertDialog = AlertUtils.showGenericAlert(getActivity(), unableToConnect, unableToConnectDsc,
                                    0, getHelp, tryAgain, 0, 0, leftClickListener, rightClickListener);

                            if (alertDialog != null) {
                                alertDialog.setCancelable(false);
                                alertDialog.setCanceledOnTouchOutside(false);
                            }

                        }
                    });
                } else {

                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            AlertDialog alertDialog = AlertUtils.showWifiPasswordAlert(getActivity(), new AlertUtils.OnClickListenerWithText() {
                                @Override
                                public void onClick(View v, String password) {
                                    wifiCount = 0;
                                    getArguments().putString(WIFI_PASSWORD, password);
                                    hideSoftKeyboard();

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            createWifiJSON();
                                        }
                                    }).start();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    audioSetupComplete = true;
                                    fragmentStack.popBackToWifi();
                                }
                            });

                            if (alertDialog != null) {
                                alertDialog.setCancelable(false);
                                alertDialog.setCanceledOnTouchOutside(false);
                            }
                        }
                    });

                }

            } else if (wifiCode.equalsIgnoreCase("N")) {

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String unableToConnect = getString(R.string.unable_to_connect);
                        String unableToConnectDsc = getString(R.string.unable_to_connect_dsc);
                        String backToWifi = getString(R.string.back_to_wifi);

                        AlertDialog alertDialog = AlertUtils.showGenericAlert(getActivity(), unableToConnect, unableToConnectDsc, 0, backToWifi, null, 0, 0, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                audioSetupComplete = true;
                                fragmentStack.popBackToWifi();
                            }
                        }, null);

                        if (alertDialog != null) {
                            alertDialog.setCancelable(false);
                            alertDialog.setCanceledOnTouchOutside(false);
                        }

                    }
                });
                stopRecording();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void wifiChangeIsComplete() {
        setupIsComplete();

    }

    private void checkSerial(String serial) {
        String serialNumber = getDeviceSerial();
        if (serialNumber.equalsIgnoreCase(serial)) {
            createWifiJSON();
        } else {
            AlertDialog alertDialog = AlertUtils.showGenericAlert(getActivity(), "This is not the device you selected to change.\nPlease select the correct device.", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentStack.popBackStack(EditDeviceFragment.class);

                }
            });
            if (alertDialog != null) {
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
            }
        }
    }

    private void createDevice(final String number) {

        String deviceName = Utils.getDeviceTypeFromDeviceSerialNumber(number).name;
        DeviceAPIService.createDevice(number, locationUri(), deviceName, new Callback<Device>() {
            @Override
            public void success(final Device device, Response response) {
                deviceUri = device.resourceUri;
                setActivationToken(device.activationToken);
                getArguments().putString(device_uri, deviceUri);
                createWifiJSON();
            }

            @Override
            public void failure(RetrofitError error) {
                audioSetupComplete = true;
                try {
                    if (isVisible()) {

                        String title = getString(R.string.device_activation_failed);
                        String dsc = Utils.getErrorMessageFromRetrofit(getActivity(), error);
                        String leftBtn = getString(R.string.get_help);
                        String rightBtn = getString(R.string.cancel);

                        View.OnClickListener leftOnClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GetHelpFragment fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_AUDIO);
                                addModalFragment(fragment);
                            }
                        };
                        View.OnClickListener rightOnClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fragmentStack.resetSetup(locationUri());

                            }
                        };
                        AlertDialog alertDialog = AlertUtils.showGenericAlert(getActivity(), title, dsc, 0, leftBtn, rightBtn, 0, 0, leftOnClickListener, rightOnClickListener);

                        if (alertDialog != null) {
                            alertDialog.setCancelable(false);
                            alertDialog.setCanceledOnTouchOutside(false);
                        }
                    }
                } catch (JSONException ignored) {
                }
            }
        });
    }

    private void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    private void showPopupHiddenNetworkPopup() {
        stopRecording();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AlertDialog alertDialog = AlertUtils.showGenericAlert(getActivity(),
                        getString(R.string.unable_to_connect_wifi),
                        getString(R.string.cannot_select_hidden_network),
                        0,
                        getString(R.string.try_again),
                        null,
                        0,
                        0, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fragmentStack.popBackToWifi();

                            }
                        }, null);
                if (alertDialog != null) {
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);
                }
            }
        });
    }


    private JSONObject wifiJSON() {

        Bundle args = getArguments();
        JSONObject wifiJsonObject = new JSONObject();
        String userName = args.getString(SSID);
        String password = args.getString(WIFI_PASSWORD);
        String bssid = args.getString(BSSID);
        try {
            wifiJsonObject.put("ID", "03")
                    .put("SSID", userName)
                    .put("PASSWORD", password);

            if (!StringUtils.isNullOrEmpty(bssid)) {
                wifiJsonObject.put("BSSID", bssid);
            }

            if (getArguments().getBoolean(CHANGING_WIFI, false)) {
                wifiJsonObject.put("D", 0);
            } else {
                wifiJsonObject.put("D", 1);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wifiJsonObject;
    }

    private void createWifiJSON() {

        String wifiName = getArguments().getString(SSID);

        if (wifiName == null && !getArguments().getBoolean(CHANGING_WIFI, false)) {
            createActivationJSON(activationToken);
            return;
        }

        lastJSONRequest = wifiJSON();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setProgressYellowImagesWithString(3);
        startTone(true);
    }


    private void createActivationJSON(String token) {

        JSONObject activationJSON = new JSONObject();

        try {
            activationJSON.put("ID", "05");
            activationJSON.put("TOKEN", token);
            String wifiName = getArguments().getString(SSID);

            if (wifiName == null && !getArguments().getBoolean(CHANGING_WIFI, false)) {
                activationJSON.put("D", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        lastJSONRequest = activationJSON;
        setProgressYellowImagesWithString(5);
        startTone(true);
        mHandler.postDelayed(mStatusChecker, 5);

    }

    private Handler mHandler = new Handler();
    private int mInterval = 5000;


    private Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            DeviceAPIService.getDeviceByUri(deviceUri, new Callback<Device>() {
                @Override
                public void success(Device device, Response response) {
                    if (device.failedOTA()) {

                        try {
                            mActivity.getApplicationContext().unregisterReceiver(mReceiver);
                        } catch (IllegalArgumentException ignore) {
                        }
                        AlertDialog alertDialog = AlertUtils.showDeviceUpdateFailedDialog(getActivity(), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                audioSetupComplete = true;
                                fragmentStack.popBackToWifi();
                            }
                        });

                        if (alertDialog != null) {
                            alertDialog.setCancelable(false);
                            alertDialog.setCanceledOnTouchOutside(false);
                        }
                        return;
                    } else if (!device.ota_status.equalsIgnoreCase("inactive")) {
                        setupIsComplete();
                        return;
                    }
                    mHandler.postDelayed(mStatusChecker, mInterval);
                }

                @Override
                public void failure(RetrofitError error) {
                    mHandler.postDelayed(mStatusChecker, mInterval);
                }

            });


        }
    };


    private Runnable mOTAStartCheck = new Runnable() {
        @Override
        public void run() {
            if (audioSetupComplete)
                return;

            audioSetupComplete = true;
            try {
                mActivity.getApplicationContext().unregisterReceiver(mReceiver);

            } catch (IllegalArgumentException ignore) {

            }

            mHandler.removeCallbacks(mStatusChecker);


            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog alertDialog = AlertUtils.showDeviceUpdateFailedDialog(getActivity(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            audioSetupComplete = true;
                            fragmentStack.popBackToWifi();
                        }
                    });

                    if (alertDialog != null) {
                        alertDialog.setCancelable(false);
                        alertDialog.setCanceledOnTouchOutside(false);
                    }
                }
            });

            stopRecording();


        }
    };

    private int otaTimeoutCheck = 120000;

    private void startOTATimeout() {
        if (!audioSetupComplete)
            retryHandler.postDelayed(mOTAStartCheck, otaTimeoutCheck);

    }

    private void setupIsComplete() {
        GoogleAnalyticsHelper.trackAudioEvent(changingWifi(), AnalyticsConstants.ACTION_AUDIO_SUCCESS, null);
        retryHandler.removeCallbacks(mOTAStartCheck);
        stopRecording();
        audioSetupComplete = true;
        binding.establishingConnectionTextView.setText(R.string.canary_connected_updating);
        int full = getArguments().getBoolean(CHANGING_WIFI, false) ? 4 : 6;
        setProgressYellowImagesWithString(full);
    }

    // Start listening for the next tone
    private void startRecording() {
        stopRecording();
        playingThread = null;
        bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE,
                RECORDER_IN_CHANNELS, RECORDER_AUDIO_ENCODING) * 2;
        isRecording = true;
        scheduleDecode();
        recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                RECORDER_SAMPLE_RATE, RECORDER_IN_CHANNELS,
                RECORDER_AUDIO_ENCODING, bufferSize);

        recorder.startRecording();
        isRecording = true;
        try {
            if (lastJSONRequest.getString("ID").equalsIgnoreCase("01")) {
                createFirstRetryHandler();
            } else {
                createRetryHandler();
            }
        } catch (JSONException e) {
            createRetryHandler();
        }


        recordingThread = new Thread(new Runnable() {
            public void run() {
                compileRecording();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }

    private final int retryTime = 120 * 1000;
    private final int firstRetryTime = 30 * 1000;

    private int firstRetry = 0;

    private int retry = 0;

    private void createFirstRetryHandler() {
        retryHandler.postDelayed(firstRetryRunnable, firstRetryTime);
    }

    final Runnable firstRetryRunnable = new Runnable() {
        public void run() {
            firstRetry++;
            stopRecording();

            if (firstRetry % 2 == 1)
                volumeLevel = 1.0f;
            else
                volumeLevel = 0.8f;

            if (firstRetry >= 4) {
                showPowerCycleAlert();
                firstRetry = 0;

            } else {
                startTone(true);
            }
            decodeHandler.removeCallbacks(decodeRunnable);
        }
    };


    private void createRetryHandler() {
        retryHandler.removeCallbacks(retryRunnable);
        retryHandler.postDelayed(retryRunnable, retryTime);
    }

    final Handler retryHandler = new Handler();

    final Runnable retryRunnable = new Runnable() {
        public void run() {
            retry++;
            stopRecording();

            if (retry >= 3) {
                showPowerCycleAlert();
                retry = 0;

            } else {
                startTone(true);
            }
            decodeHandler.removeCallbacks(decodeRunnable);
        }
    };


    private short[] lastAudio = new short[]{};
    private short[] currentAudio = new short[]{};


    private void compileRecording() {
        while (isRecording) {
            short[] readShorts = new short[bufferSize];
            if (recorder == null)
                continue;

            if (!isVisible())
                break;

            int readNumber = recorder.read(readShorts, 0, bufferSize);
            if (readNumber <= 0)
                continue;
            short[] currentRead = new short[readNumber];
            System.arraycopy(readShorts, 0, currentRead, 0, readNumber);
            currentAudio = Utils.concatenateShortArrays(currentAudio, currentRead);
            System.gc();
        }
    }

    private final int DECODE_INTERVAL = 6 * 1000;

    public void scheduleDecode() {
        decodeHandler.postDelayed(decodeRunnable, DECODE_INTERVAL);
    }

    private Runnable decodeRunnable = new Runnable() {
        public void run() {
            decodeResponse();
        }
    };

    private int invert = 0;
    private float factor = 0.1f;

    private void decodeResponse() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!isVisible())
                    return;

                short[] recordedShorts = Utils.concatenateShortArrays(lastAudio, currentAudio);
                short[] bandPassFilteredShorts = bandPassFilter(recordedShorts);
                String decodedString = decodedString(bandPassFilteredShorts);

                if (decodedString.length() == 0) {

                    short[] amplifyShorts = amplify(recordedShorts, (short) 4);
                    decodedString = decodedString(amplifyShorts);

                }


                try {
                    if (decodedString.length() > 0) {
                        try {
                            JSONObject responseObject = new JSONObject(decodedString);


                            nextTone(responseObject);
                        } catch (JSONException e) {
                            scheduleDecode();
                            System.gc();
                            lastAudio = currentAudio;
                            currentAudio = new short[]{};
                        }
                    } else {
                        scheduleDecode();
                        System.gc();
                        lastAudio = currentAudio;
                        currentAudio = new short[]{};
                    }
                } catch (Exception ignore) {

                }
            }
        }).start();

    }


    private short[] amplify(short[] recordedShorts, short factor) {
        for (int i = 0; i < recordedShorts.length; i++) {
            recordedShorts[i] = (short) (recordedShorts[i] * factor);
        }
        return recordedShorts;

    }

    private String decodedString(short[] bandPassFilteredShorts) {
        String decodeTryString = BlackBox.instance().decode(bandPassFilteredShorts, factor, 1);
        if (decodeTryString.length() == 0 && invert == 0) {
            decodeTryString = BlackBox.instance().decode(bandPassFilteredShorts, factor, -1);

        }

        return decodeTryString;

    }

    private float input(short v) {
        return (float) (v * 0.80);

    }

    private short output(float x) {
        short v;
        if (x > 32767) x = 32767;
        if (x < -32767) x = -32767;
        v = (short) x;
        return v;
    }

    private int NZEROS = 6;
    private int NPOLES = 6;
    private float GAIN = 8.888383633e+03f;

    private float[] xv = new float[NZEROS + 1];
    private float[] yv = new float[NPOLES + 1];

    private short[] bandPassFilter(short[] recordedShorts) {
        for (int i = 0; i < recordedShorts.length; i++) {
            xv[0] = xv[1];
            xv[1] = xv[2];
            xv[2] = xv[3];
            xv[3] = xv[4];
            xv[4] = xv[5];
            xv[5] = xv[6];
            xv[6] = input(recordedShorts[i]) / GAIN;
            yv[0] = yv[1];
            yv[1] = yv[2];
            yv[2] = yv[3];
            yv[3] = yv[4];
            yv[4] = yv[5];
            yv[5] = yv[6];
            yv[6] = (float) ((xv[6] - xv[0]) + 3 * (xv[2] - xv[4])
                    + (-0.8191000226 * yv[0]) + (4.9270176437 * yv[1])
                    + (-12.5050374980 * yv[2]) + (17.1341116900 * yv[3])
                    + (-13.3654231520 * yv[4]) + (5.6282413381 * yv[5]));
            recordedShorts[i] = output(yv[6]);

        }
        return recordedShorts;
    }

    private void setProgressYellowImagesWithString(final int yellow) {

        mActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {

                        Activity activity = getActivity();
                        Display display;
                        if (activity != null)
                            display = activity.getWindowManager().getDefaultDisplay();
                        else
                            return;

                        if (display == null)
                            return;

                        int constant = getArguments().getBoolean(CHANGING_WIFI, false) ? 4 : 6;

                        Point size = new Point();
                        display.getSize(size);
                        int width = size.x;
                        final int barWidth = width / constant * yellow;


                        ResizeWidthAnimation anim = new ResizeWidthAnimation(binding.progressBar, barWidth);
                        int duration;

                        if (yellow == constant)
                            duration = 500;
                        else if (yellow == 2)
                            duration = 1000;
                        else if (yellow == 4)
                            duration = 1000;
                        else
                            duration = 30000;

                        anim.setDuration(duration);
                        binding.progressBar.startAnimation(anim);
                    }
                });
    }

    private void stopRecording() {
        try {
            if (isRecording && null != recorder) {
                isRecording = false;
                recorder.stop();
                recorder.release();
                recorder = null;
                recordingThread = null;
                lastAudio = new short[]{};
                currentAudio = new short[]{};
                decodeHandler.removeCallbacks(decodeRunnable);
                retryHandler.removeCallbacks(retryRunnable);
                retryHandler.removeCallbacks(firstRetryRunnable);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_AUDIO_SETUP;
    }
}

