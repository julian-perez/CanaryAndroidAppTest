package is.yranac.canary.fragments.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupPlacementSuggestionsBinding;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_PLACEMENT_SUGGESTIONS;

/**
 * Created by Schroeder on 9/19/14.
 */
public class PlacementSuggestionsFragment extends SetUpBaseFragment implements View.OnClickListener {


    private static final String show_header = "showHeader";
    private boolean showHeader;
    private FragmentSetupPlacementSuggestionsBinding binding;

    public static PlacementSuggestionsFragment newInstance(Bundle args,
                                                           boolean showHeader) {
        PlacementSuggestionsFragment fragment = new PlacementSuggestionsFragment();
        Bundle bundle = new Bundle();
        bundle.putAll(args);
        bundle.putBoolean(show_header, showHeader);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PlacementSuggestionsFragment newInstance(Bundle args, int deviceType, int deviceID,
                                                           String resourceUri, boolean isSetup,
                                                           String currentDeviceName, boolean showHeader) {
        PlacementSuggestionsFragment fragment = new PlacementSuggestionsFragment();
        Bundle bundle = new Bundle();
        bundle.putAll(args);
        bundle.putInt(device_type, deviceType);
        bundle.putBoolean(key_isSetup, isSetup);
        bundle.putInt(key_deviceID, deviceID);
        bundle.putString(key_currentDeviceName, currentDeviceName);
        bundle.putString(device_uri, resourceUri);
        bundle.putBoolean(show_header, showHeader);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetupPlacementSuggestionsBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.header.headerTitleTextView.setText(R.string.placement_guidelines);
        binding.header.headerViewLeftButton.setVisibility(View.GONE);
        binding.header.headerViewRightButton.setVisibility(View.GONE);


        showHeader = getArguments().getBoolean(show_header, true);

        binding.header.getRoot().setVisibility(showHeader ? View.VISIBLE : View.GONE);
        binding.nextBtn.setVisibility(showHeader ? View.GONE : View.VISIBLE);

        int deviceType = getArguments().getInt(device_type, 0);
        int drawable;
        int[] titles;
        int[] descriptions;
        switch (deviceType) {
            case DeviceType.CANARY_AIO:
            case DeviceType.CANARY_VIEW:
                drawable = R.drawable.device_lifestyle_aio;
                titles = new int[2];
                descriptions = new int[2];
                titles[0] = R.string.field_of_view;
                titles[1] = R.string.strong_internet;
                descriptions[0] = R.string.position_device_traffic;
                descriptions[1] = R.string.keep_in_range_of_wifi_or_ethernet;
                break;
            case DeviceType.FLEX:
                drawable = R.drawable.device_lifestyle_flex;
                titles = new int[3];
                descriptions = new int[3];
                titles[0] = R.string.outdoor_use;
                titles[1] = R.string.strong_internet;
                titles[2] = R.string.avoid_direct_sunlight;
                descriptions[0] = R.string.position_device_flex;
                descriptions[1] = R.string.keep_in_range_of_wifi;
                descriptions[2] = R.string.if_outdoor_protect_sunlight;
                break;
            default:
                return;
        }

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());


        for (int i = 0; i < titles.length; i++) {

            View placementView = layoutInflater.inflate(R.layout.layout_placement_row, binding.placementGuidelineLayout, false);

            TextView placementTitle = placementView.findViewById(R.id.placement_guideline_title);
            TextView placementDsc = placementView.findViewById(R.id.placement_guideline_dsc);
            placementTitle.setText(titles[i]);
            placementDsc.setText(descriptions[i]);

            binding.placementGuidelineLayout.addView(placementView);

            if (deviceType == DeviceType.FLEX && i == 2) {

                ImageView todoImageView = placementView.findViewById(R.id.to_do_icon);
                todoImageView.setImageResource(R.drawable.do_4);

            }

        }

        binding.heroImage.setImageResource(drawable);
        binding.nextBtn.setOnClickListener(this);


    }

    @Override
    public void onResume() {
        super.onResume();

        if (!showHeader) {
            fragmentStack.setHeaderTitle(R.string.canary_flex_tips);
            fragmentStack.disableBackButton();
            fragmentStack.showRightButton(0);
        }
    }


    @Override
    protected String getAnalyticsTag() {
        return SCREEN_PLACEMENT_SUGGESTIONS;
    }

    @Override
    public void onRightButtonClick() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_btn:
                IndoorOutdoorFlexFragment fragment = getInstance(IndoorOutdoorFlexFragment.class);
                addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
                break;
        }
    }
}
