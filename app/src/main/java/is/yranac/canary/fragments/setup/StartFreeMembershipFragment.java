package is.yranac.canary.fragments.setup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupStartMembershipBinding;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.api.SubscriptionAPIService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.ui.WebActivity;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.JSONUtil;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ACTIVATE_TRIAL_ADD;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ACTIVATE_TRIAL_ADD_RETRY;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ACTIVATE_TRIAL_SKIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MEMBERSHIP;

/**
 * Created by michaelschroeder on 5/30/17.
 */

public class StartFreeMembershipFragment extends SetUpBaseFragment implements View.OnClickListener {

    private static final int SUBSCRIPTION_UPDATE = 2141;

    private FragmentSetupStartMembershipBinding binding;
    private Device device;
    private Location location;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSetupStartMembershipBinding.inflate(inflater);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        device = JSONUtil.getObject(getArguments().getString(key_deviceJSON), Device.class);
        location = LocationDatabaseService.getLocationFromId(device.getLocationId());
        binding.addMembershipBtn.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.setHeaderTitle(R.string.membership);
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_membership_btn:
                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_ACTIVATE_TRIAL_ADD, null, device.uuid, device.getLocationId(), 0);

                String url = Constants.subscriptionSelect(location, getContext(), "trial");

                String title = getString(R.string.membership);
                Intent intent = WebActivity.newInstance(getContext(), url, title, true);
                startActivityForResult(intent, SUBSCRIPTION_UPDATE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SUBSCRIPTION_UPDATE) {
            showLoading(true, getString(R.string.checking_dots));
            SubscriptionAPIService.getSubscription(device.getLocationId(), new Callback<Subscription>() {
                @Override
                public void success(Subscription subscription, Response response) {
                    showLoading(false, getString(R.string.checking_dots));

                    if (subscription.hasMembership) {
                        fragmentStack.addFragmentAndResetStack(
                                getInstance(StartMembershipFragment.class),
                                Utils.SLIDE_FROM_RIGHT);
                    } else {
                        mDialog = AlertUtils.activateMembershipFreeTrail(getContext(), false, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_ACTIVATE_TRIAL_ADD_RETRY,
                                        null, device.uuid, device.getLocationId(), 0);
                                String url = Constants.subscriptionSelect(location, getContext(), "trial");
                                String title = getString(R.string.membership);
                                Intent intent = WebActivity.newInstance(getContext(), url, title, true);
                                startActivityForResult(intent, SUBSCRIPTION_UPDATE);
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_ACTIVATE_TRIAL_SKIP,
                                        null, device.uuid, device.getLocationId(), 0);

                                fragmentStack.addFragmentAndResetStack(
                                        getInstance(StartMembershipFragment.class),
                                        Utils.SLIDE_FROM_RIGHT);
                            }
                        });
                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    showLoading(false, getString(R.string.checking_dots));

                }
            });
        }
    }
}
