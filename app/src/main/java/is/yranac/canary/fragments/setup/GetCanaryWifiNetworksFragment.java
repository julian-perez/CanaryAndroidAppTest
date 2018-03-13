package is.yranac.canary.fragments.setup;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.adapter.WifiNetworkAdapter;
import is.yranac.canary.databinding.FragmentSetupWifiNetworksBinding;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.nativelibs.models.messages.setup.BLESetupSSIDResponse;
import is.yranac.canary.services.bluetooth.BluetoothSingleton;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.TouchTimeUtil;
import is.yranac.canary.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 */

public class GetCanaryWifiNetworksFragment extends BTLEBaseFragment {

    private WifiNetworkAdapter wifiNetworkAdapter;
    private ArrayList<HashMap<String, String>> WifiNetworkList = new ArrayList<>();
    private FragmentSetupWifiNetworksBinding binding;

    public static GetCanaryWifiNetworksFragment newInstance(Bundle args, DeviceType deviceType) {

        Bundle arguments = new Bundle();

        if (args != null) {
            arguments.putAll(args);
        }

        args.putInt(device_type, deviceType.id);
        GetCanaryWifiNetworksFragment fragment = new GetCanaryWifiNetworksFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetupWifiNetworksBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        wifiNetworkAdapter = new WifiNetworkAdapter(getActivity(), WifiNetworkList, SSID);
        ListView listView = binding.list;
        listView.setAdapter(wifiNetworkAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (TouchTimeUtil.dontAllowTouch())
                    return;

                if (position == wifiNetworkAdapter.getCount() - 1) {
                    Bundle args = getArguments();
                    args.remove(SSID);
                    args.remove(BSSID);
                    args.remove(WIFI_PASSWORD);
                    GetWifiPasswordAfterBluetoothFragment fragment = new GetWifiPasswordAfterBluetoothFragment();
                    fragment.setArguments(args);
                    addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
                    return;
                }

                String ssid = WifiNetworkList.get(position).get(SSID);
                GetWifiPasswordAfterBluetoothFragment fragment = GetWifiPasswordAfterBluetoothFragment.newInstance(ssid, getArguments());
                addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);

            }
        });

        int deviceType = getArguments().getInt(device_type, 0);

        switch (deviceType) {
            case DeviceType.CANARY_AIO:
            case DeviceType.CANARY_VIEW:
                binding.chooseWifiHeader.setText(R.string.choose_a_two_four_network);
                break;
            case DeviceType.FLEX:
                binding.chooseWifiHeader.setText(R.string.choose_a_wifi_network);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        fragmentStack.setHeaderTitle(R.string.connect);

        fragmentStack.showHelpButton();
        fragmentStack.enableRightButton(this, true);

        if (BluetoothSingleton.getBluetoothHelperService().getCurrentDevice() != null) {
            BluetoothSingleton.getBluetoothSetupHelper().sendRequestForWifiList();
            showLoading(true, getString(R.string.loading_dialog), false);
        }
    }

    @Override
    public void onRightButtonClick() {
        GetHelpFragment fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_INTERNET);
        addModalFragment(fragment);
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Subscribe
    public void onSsidReceived(BLESetupSSIDResponse response) {

        Log.i("BLE Response", response.toJSONString());
        WifiNetworkList.clear();

        fragmentStack.enableRightButton(GetCanaryWifiNetworksFragment.this, true);
        Set<HashMap<String, String>> resultSet = new HashSet<>();

        for (String network : response.getNetworks()) {
            HashMap<String, String> item = new HashMap<>();
            item.put(SSID, network);
            resultSet.add(item);

        }

        WifiNetworkList.addAll(resultSet);
        wifiNetworkAdapter.notifyDataSetChanged();

        showLoading(false, null);
    }
}
