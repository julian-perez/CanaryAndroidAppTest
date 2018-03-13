package is.yranac.canary.fragments.setup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupMembershipBinding;
import is.yranac.canary.fragments.settings.ManageDevicesFragment;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.ui.MainActivity;
import is.yranac.canary.util.JSONUtil;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.cache.location.GetLocation;
import is.yranac.canary.util.cache.location.GotLocationData;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_SETUP_COMPLETE_ADD;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_SETUP_COMPLETE_SKIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MEMBERSHIP;


/**
 * Created by michaelschroeder on 5/31/17.
 */

public class StartMembershipFragment extends SetUpBaseFragment {

    private List<Device> devices = new ArrayList<>();
    private Subscription subscription;
    private Device device;


    private FragmentSetupMembershipBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSetupMembershipBinding.inflate(inflater);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        device = JSONUtil.getObject(getArguments().getString(key_deviceJSON), Device.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.disableBackButton();
        fragmentStack.setHeaderTitle(R.string.setup_complete);
        TinyMessageBus.register(this);
        TinyMessageBus.post(new GetLocation(device.getLocationId()));
        fragmentStack.enableRightButton(this, true);
        fragmentStack.showRightButton(R.string.done);
    }

    @Override
    public void onPause() {
        super.onPause();
        TinyMessageBus.unregister(this);
    }


    @Subscribe
    public void gotLocation(GotLocationData gotLocationData) {


        subscription = gotLocationData.subscription;
        devices = gotLocationData.deviceList;

        boolean subscribed = false;
        if (subscription != null) {
            subscribed = subscription.hasMembership;
        }
        if (subscribed) {
            binding.membershipDetails.setText(R.string.youre_all_set_with_membership_thanks_for_trusting);
        } else {
            binding.membershipDetails.setText(R.string.youre_all_set_thanks_for_trusting);
        }
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onRightButtonClick() {

        if (subscription != null) {
            if (subscription.hasMembership) {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_SETUP_COMPLETE_ADD, null, device.uuid, device.getLocationId(), 0);
            } else {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_SETUP_COMPLETE_SKIP, null,  device.uuid, device.getLocationId(), 0);
            }
        }
        if (devices.size() > 1) {
            fragmentStack.addFragmentAndResetStack(ManageDevicesFragment.newInstance(device.getLocationId()), Utils.SLIDE_FROM_RIGHT);
            fragmentStack.enableBackButton();
        } else {
            Intent i = new Intent(getActivity(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.fade_in_activity_delay, R.anim.fade_out_activity);
        }

    }

}
