package is.yranac.canary.fragments;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.databinding.HomeScreenFragmentBinding;
import is.yranac.canary.messages.ChangeModeMsg;
import is.yranac.canary.messages.HomeNode;
import is.yranac.canary.model.CurrentCustomer;
import is.yranac.canary.model.Device;
import is.yranac.canary.model.DeviceType;
import is.yranac.canary.model.Location;
import is.yranac.canary.model.LocationAndEntry;
import is.yranac.canary.model.Mode;
import is.yranac.canary.model.Reading;
import is.yranac.canary.services.AnalyticService;
import is.yranac.canary.utils.Constants;
import is.yranac.canary.utils.JSONUtil;
import is.yranac.canary.utils.TinyMessageBus;
import is.yranac.canary.utils.Utils;

/**
 * Created by narendramanoharan on 6/24/16.
 */
public class HomeScreenFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DataApi.DataListener {

    private static final String LOG_TAG = "HomeScreenFragment";
    public LocationAndEntry locationAndEntry;
    public Location location;
    private GoogleApiClient mGoogleApiClient;
    private HomeScreenFragmentBinding binding;
    private static final String LOCATION = "location";

    private char degreeSymbol = '\u00B0';
    private char percentSymbol = '\u0025';

    public static HomeScreenFragment newInstance(LocationAndEntry location) {
        Bundle args = new Bundle();
        args.putString(LOCATION, JSONUtil.getJSONString(location));
        HomeScreenFragment homeScreenFragment = new HomeScreenFragment();
        homeScreenFragment.setArguments(args);
        return homeScreenFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_screen_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        String loc = args.getString(LOCATION);

        locationAndEntry = JSONUtil.getObject(loc, LocationAndEntry.class);

        location = locationAndEntry.location;
        setupView();
        setupGoogleAPI();
        binding.pulsator.start();
        setHomeScreenData();
        binding.modeNight.setVisibility(locationAndEntry.subscription.doesNotHaveModeConfigs() ? View.GONE : View.VISIBLE);

    }

    private void setupGoogleAPI() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void setupView() {

        binding.modeImage.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        setupModeViews();
                        destroyNormalViews();
                        AnalyticService.sendViewIntent(getActivity(), "HomeScreenFragment", "Mode Change screen");
                        return false;
                    }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        TinyMessageBus.register(this);
        AnalyticService.sendViewIntent(getActivity(), "HomeScreen", "Started");
    }

    @Override
    public void onPause() {
        super.onPause();
        if ((mGoogleApiClient != null) && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        TinyMessageBus.unregister(this);
        destroyModeViews();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        TinyMessageBus.post(new HomeNode());
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED && event.getDataItem().getUri().getPath().equals(Constants.PATH_MODE_FAIL)) {
                DataMapItem modeFail = DataMapItem.fromDataItem(event.getDataItem());
                String modeChangeURI = modeFail.getDataMap().getString("modeURI");
                switch (modeChangeURI) {
                    case Mode.MODE_AWAY:
                        binding.modeImage.setImageResource(R.drawable.mode_away_on);
                        break;
                    case Mode.MODE_HOME:
                        binding.modeImage.setImageResource(R.drawable.mode_home_on);
                        break;
                    case Mode.MODE_NIGHT:
                        binding.modeImage.setImageResource(R.drawable.mode_night_on);
                        break;
                    case Mode.MODE_PRIVACY:
                        binding.modeImage.setImageResource(R.drawable.mode_privacy_on);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void setHomeScreenData() {
        visibleNormalViews();
        binding.locationTextView.setText(location.name);
        binding.locationTextView.setVisibility(View.VISIBLE);
        binding.offlineText.setVisibility(View.VISIBLE);

        if (location.isPrivate) {
            binding.modeImage.setImageResource(R.drawable.mode_privacy_on);
        } else {
            switch (location.currentMode.name) {
                case Mode.MODE_AWAY:
                    binding.modeImage.setImageResource(R.drawable.mode_away_on);
                    break;
                case Mode.MODE_HOME:
                    binding.modeImage.setImageResource(R.drawable.mode_home_on);
                    break;
                case Mode.MODE_NIGHT:
                    binding.modeImage.setImageResource(R.drawable.mode_night_on);
                    break;
                case Mode.MODE_PRIVACY:
                    break;
                default:
                    break;
            }
        }

        if (!location.devices.isEmpty()) {

            Device device = location.devices.get(0);
            if (device.deviceType.id == DeviceType.FLEX) {
                updateWifiAndBattery();
            } else {
                updateHomeHealth();
            }

            if (!device.isOnline) {
                setupOffline();
            }


        } else {
            binding.deviceInfoLayout.getRoot().setVisibility(View.GONE);
        }
    }

    private void setupOffline() {
        binding.deviceInfoLayout.statisticsWifiStatus.setText(getString(R.string.em_dash));
        binding.deviceInfoLayout.statisticsBatteryStatus.setText(getString(R.string.em_dash));
        binding.deviceInfoLayout.sensorDataLayout.deviceTempTextview.setText(getString(R.string.em_dash));
        binding.deviceInfoLayout.sensorDataLayout.deviceHumidityTextview.setText(getString(R.string.em_dash));
        binding.deviceInfoLayout.sensorDataLayout.deviceAirQualityOfflineIcon.setVisibility(View.VISIBLE);
        binding.deviceInfoLayout.sensorDataLayout.deviceAirQualityIcon.setVisibility(View.GONE);
    }

    private void updateHomeHealth() {

        binding.deviceInfoLayout.sensorDataLayout.getRoot().setVisibility(View.VISIBLE);
        binding.deviceInfoLayout.statisticsFlexContainer.setVisibility(View.GONE);

        Reading temp = locationAndEntry.temp;
        Reading humidity = locationAndEntry.humidity;
        Reading air = locationAndEntry.air;
        DecimalFormat format = new DecimalFormat("#,##0");

        if (temp != null) {
            format.setRoundingMode(RoundingMode.HALF_UP);
            String temperature = format.format(Utils.getTemperatureInPreferredUnits(temp.value));
            temperature += degreeSymbol;

            if (CurrentCustomer.getCurrentCustomer().celsius) {
                temperature += "C";

            } else {
                temperature += "F";
            }
            binding.deviceInfoLayout.sensorDataLayout.deviceTempTextview.setText(temperature);
        }

        if (humidity != null) {
            String humidityS = format.format(humidity.value);
            humidityS += percentSymbol;
            binding.deviceInfoLayout.sensorDataLayout.deviceHumidityTextview.setText(humidityS);

        }

        if (air != null) {
            binding.deviceInfoLayout.sensorDataLayout.deviceAirQualityOfflineIcon.setVisibility(View.GONE);
            binding.deviceInfoLayout.sensorDataLayout.deviceAirQualityIcon.setVisibility(View.VISIBLE);

            if (air.value > 0.6f) {
                binding.deviceInfoLayout.sensorDataLayout.deviceAirQualityIcon.setImageResource(R.drawable.aq_float_icon_normal);
            } else if (air.value > 0.4f) {
                binding.deviceInfoLayout.sensorDataLayout.deviceAirQualityIcon.setImageResource(R.drawable.aq_float_icon_abnormal);
            } else {
                binding.deviceInfoLayout.sensorDataLayout.deviceAirQualityIcon.setImageResource(R.drawable.aq_float_icon_very_abnormal);

            }
        }
    }

    private void updateWifiAndBattery() {

        Reading wifiReading = locationAndEntry.wifi;
        Reading batteryReading = locationAndEntry.battery;
        binding.deviceInfoLayout.sensorDataLayout.getRoot().setVisibility(View.GONE);
        binding.deviceInfoLayout.statisticsFlexContainer.setVisibility(View.VISIBLE);

        if (batteryReading != null) {
            binding.deviceInfoLayout.statisticsBatteryStatus.setText(Float.valueOf(batteryReading.value).intValue() + "%");
        } else {
            binding.deviceInfoLayout.statisticsBatteryStatus.setText(null);
        }

        if (wifiReading != null) {
            binding.deviceInfoLayout.statisticsWifiStatus.setText(wifiReading.getWifiLevelLabel(getActivity()));
        } else {
            wifiReading = new Reading();
            binding.deviceInfoLayout.statisticsWifiStatus.setText(wifiReading.getWifiLevelLabel(getActivity()));
        }


    }


    private void setupModeViews() {
        modeViewSetup();
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroyModeViews();
                visibleNormalViews();
            }
        });
        binding.modeHome.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preSetupImage(Mode.MODE_HOME);
                        binding.modeImage.setImageResource(R.drawable.mode_home_on);
                    }
                }
        );
        binding.modeAway.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preSetupImage(Mode.MODE_AWAY);
                        binding.modeImage.setImageResource(R.drawable.mode_away_on);
                    }
                }
        );
        binding.modeNight.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preSetupImage(Mode.MODE_NIGHT);
                        binding.modeImage.setImageResource(R.drawable.mode_night_on);
                    }
                }
        );
        binding.modePrivacy.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preSetupImage(Mode.MODE_PRIVACY);
                        binding.modeImage.setImageResource(R.drawable.mode_privacy_on);
                    }
                }
        );
    }

    private void preSetupImage(String mode) {
        destroyModeViews();
        visibleNormalViews();
        TinyMessageBus.post(new ChangeModeMsg(mode));
    }

    @Subscribe(mode = Subscribe.Mode.Background)
    public void ChangeMode(ChangeModeMsg changeMode) {
        NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        List<Node> nodes = result.getNodes();
        if (nodes.size() > 0) {
            String nodeId = nodes.get(0).getId();
            String sendData = changeMode.getModeURI() + "|" + location.resourceUri;
            byte[] data = sendData.getBytes();
            Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, Constants.PATH_CHANGE_MODE, data);
        }
    }

    private void modeViewSetup() {
        binding.modeHome.setImageResource(R.drawable.mode_home_on);
        binding.modeHome.setVisibility(View.VISIBLE);
        binding.modeAway.setImageResource(R.drawable.mode_away_on);
        binding.modeAway.setVisibility(View.VISIBLE);
        binding.modeNight.setImageResource(R.drawable.mode_night_on);
        binding.modeNight.setVisibility(View.VISIBLE);
        binding.modePrivacy.setImageResource(R.drawable.mode_privacy_on);
        binding.modePrivacy.setVisibility(View.VISIBLE);
    }

    private void destroyNormalViews() {
        binding.locationTextView.setVisibility(View.GONE);
        binding.deviceInfoLayout.getRoot().setVisibility(View.GONE);
        binding.modeImage.setVisibility(View.GONE);
        binding.offlineText.setVisibility(View.GONE);
        binding.loadingData.setVisibility(View.GONE);
        binding.pulsator.stop();
    }

    private void visibleNormalViews() {
        binding.locationTextView.setVisibility(View.VISIBLE);
        binding.deviceInfoLayout.getRoot().setVisibility(View.VISIBLE);
        binding.modeImage.setVisibility(View.VISIBLE);
        binding.pulsator.setVisibility(View.VISIBLE);
        binding.pulsator.setCount(1);
        binding.pulsator.start();
    }

    private void destroyModeViews() {
        binding.modeHome.setVisibility(View.GONE);
        binding.modeAway.setVisibility(View.GONE);
        binding.modeNight.setVisibility(View.GONE);
        binding.modePrivacy.setVisibility(View.GONE);
    }
}
