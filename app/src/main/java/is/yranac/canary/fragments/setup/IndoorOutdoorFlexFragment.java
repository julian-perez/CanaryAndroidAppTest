package is.yranac.canary.fragments.setup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentIndoorOutdoorFlexBinding;
import is.yranac.canary.fragments.settings.DeviceNamingFragment;
import is.yranac.canary.util.Utils;

public class IndoorOutdoorFlexFragment extends SetUpBaseFragment {


    public static IndoorOutdoorFlexFragment newInstance(Bundle args, int deviceType, int deviceID, String resourceUri, boolean isSetup, String currentDeviceName) {

        Bundle bundle = new Bundle();
        bundle.putAll(args);
        IndoorOutdoorFlexFragment f = new IndoorOutdoorFlexFragment();
        bundle.putInt(device_type, deviceType);
        bundle.putBoolean(key_isSetup, isSetup);
        bundle.putInt(key_deviceID, deviceID);
        bundle.putString(key_currentDeviceName, currentDeviceName);
        bundle.putString(device_uri, resourceUri);
        f.setArguments(bundle);

        return f;
    }

    private FragmentIndoorOutdoorFlexBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_indoor_outdoor_flex, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final boolean isSetup = getArguments().getBoolean(key_isSetup, false);
        if (isSetup)
            fragmentStack.disableBackButton();


        binding.indoorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceNamingFragment fragment = DeviceNamingFragment.newInstance(getArguments(), false);
                addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
            }
        });

        binding.outdoorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceNamingFragment fragment = DeviceNamingFragment.newInstance(getArguments(), true);
                addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.setHeaderTitle(R.string.name);
        fragmentStack.showHelpButton();
        fragmentStack.enableRightButton(this, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentStack.resetButtonStyle();

    }

    @Override
    public void onRightButtonClick() {
        PlacementSuggestionsFragment fragment = PlacementSuggestionsFragment.newInstance(getArguments(), true);
        addModalFragment(fragment);
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }


}
