package is.yranac.canary.fragments.setup;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import is.yranac.canary.R;
import is.yranac.canary.adapter.WifiNetworkAdapter;
import is.yranac.canary.databinding.FragmentSetupWifiNetworksBinding;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.TouchTimeUtil;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_LOCATION_WIFI_NETWORKS;

public class LocalWifiNetworksFragment extends SetUpBaseFragment implements
        OnItemClickListener {
    private static final String LOG_TAG = "LocalWifiNetworksFragment";
    private WifiManager wifi;
    private List<ScanResult> results;

    private ArrayList<HashMap<String, String>> arraylist = new ArrayList<>();
    private WifiNetworkAdapter adapter;
    private FragmentSetupWifiNetworksBinding binding;

    /* Called when the activity is first created. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_setup_wifi_networks,
                container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.list .setOnItemClickListener(this);

        wifi = (WifiManager) getActivity().getSystemService(
                Context.WIFI_SERVICE);

        binding.chooseWifiHeader.setText(R.string.choose_a_two_four_network);
        adapter = new WifiNetworkAdapter(getActivity(), arraylist,
                SSID);
        binding.list .setAdapter(adapter);

    }

    private void showWifiAlertMessage() {

        AlertUtils.showGenericAlert(getActivity(), getString(R.string.oops_wifi), getString(R.string.turn_wifi_on_to_connect), 0, getString(R.string.cancel), getString(R.string.go_to_settings), 0, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(
                        Settings.ACTION_WIFI_SETTINGS));

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.setHeaderTitle(R.string.connect);
        fragmentStack.showLogoutButton(false);
        fragmentStack.enableRightButton(this, false);
        fragmentStack.showRightButton(0);

        if (!wifi.isWifiEnabled()) {
            showWifiAlertMessage();
        } else {
            startScan();
        }
    }

    private void startScan() {

        results = wifi.getScanResults();
        arraylist.clear();
        wifi.startScan();
        Set<HashMap<String, String>> resultSet = new HashSet<>();

        Set<String> currentSsids = new HashSet<>();


        try {
            for (ScanResult scanResult : results) {
                if (scanResult.SSID != null && !currentSsids.contains(scanResult.SSID) && scanResult.SSID.length() > 0) {
                    if (scanResult.frequency < 5000) {
                        if (scanResult.SSID.startsWith("CanaryLTE-")){
                            continue;
                        }
                        currentSsids.add(scanResult.SSID);
                        HashMap<String, String> item = new HashMap<>();
                        item.put(SSID, scanResult.SSID);
                        item.put(BSSID, scanResult.BSSID);
                        resultSet.add(item);
                    }
                }
            }


            arraylist.addAll(resultSet);
            adapter.notifyDataSetChanged();
        } catch (Exception ignored) {
        }
    }

    private boolean isHiddenNetwork(String ssid) {
        List<WifiConfiguration> wifiConfigurations = wifi.getConfiguredNetworks();

        if (wifiConfigurations == null)
            return false;


        for (WifiConfiguration wifiConfiguration : wifiConfigurations) {
            if (wifiConfiguration.SSID == null)
                continue;

            String trimmedSSID = wifiConfiguration.SSID.replaceAll("^\"|\"$", "");
            if (trimmedSSID.equalsIgnoreCase(ssid) && wifiConfiguration.hiddenSSID)
                return true;
        }
        return false;
    }

    @Override
    public void onRightButtonClick() {
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (TouchTimeUtil.dontAllowTouch())
            return;
        if (position == adapter.getCount() - 1){
            Bundle args = getArguments();
            args.remove(SSID);
            args.remove(BSSID);
            args.remove(WIFI_PASSWORD);
            GetWifiPasswordFragment fragment = new GetWifiPasswordFragment();
            fragment.setArguments(args);
            addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
            return;
        }
        String ssid = arraylist.get(position).get(SSID);
        boolean hiddenNetwork = isHiddenNetwork(ssid);


        String bssid = arraylist.get(position).get(BSSID);

        GetWifiPasswordFragment fragment = new GetWifiPasswordFragment();
        Bundle args = getArguments();
        args.putString(SSID, ssid);
        args.putString(BSSID, bssid);
        args.putBoolean(WIFI_HIDDEN, hiddenNetwork);
        fragment.setArguments(args);
        addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
    }


    @Override
    protected String getAnalyticsTag() {
        return SCREEN_LOCATION_WIFI_NETWORKS;
    }

    public static LocalWifiNetworksFragment newInstance(Bundle arguments) {
        Bundle args = new Bundle(arguments);
        LocalWifiNetworksFragment fragment = new LocalWifiNetworksFragment();
        fragment.setArguments(args);
        return fragment;
    }
}