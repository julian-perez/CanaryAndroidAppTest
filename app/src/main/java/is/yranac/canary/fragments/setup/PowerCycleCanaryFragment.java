package is.yranac.canary.fragments.setup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.adapter.ScreenSlidePagerAdapter;
import is.yranac.canary.model.SetupPagerContent;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_POWER_CANARY;

public class PowerCycleCanaryFragment extends SetUpBaseFragment {

    private static final String LOG_TAG = "PowerCycleCanaryFragment";

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction()
                    .equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                int micState = intent.getIntExtra("microphone", -1);
                if (state == 1 && micState == 1)
                    fragmentStack.enableRightButton(PowerCycleCanaryFragment.this, true);
                else
                    fragmentStack.enableRightButton(PowerCycleCanaryFragment.this, false);

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setup_place_canary, container, false);
    }

    private List<SetupPagerContent> pagerContent() {
        List<SetupPagerContent> setupPagerContentList = new ArrayList<>();

        SetupPagerContent setupPagerContent = new SetupPagerContent();
        setupPagerContent.string2 = R.string.power_cycle_canary;
        setupPagerContentList.add(setupPagerContent);


        return setupPagerContentList;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);

        ImageView contentImage1 = (ImageView) view.findViewById(R.id.content_image_2);
        contentImage1.setImageResource(R.drawable.prepare_power_cycle);

        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager(), pagerContent());
        pager.setAdapter(pagerAdapter);
        pager.setPageMargin(0);

    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.showRightButton(R.string.next);
        fragmentStack.enableRightButton(this, true);
        fragmentStack.setHeaderTitle(R.string.prepare_canary);
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
        DeviceAudioSetupFragment fragment = getInstance(DeviceAudioSetupFragment.class);
        addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
    }


    @Override
    protected String getAnalyticsTag() {
        return SCREEN_POWER_CANARY;
    }

    public static PowerCycleCanaryFragment newInstance(Bundle arguments) {
        PowerCycleCanaryFragment fragment = new PowerCycleCanaryFragment();
        fragment.setArguments(arguments);
        return fragment;
    }
}
