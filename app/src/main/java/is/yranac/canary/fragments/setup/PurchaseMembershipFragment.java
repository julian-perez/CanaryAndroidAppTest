package is.yranac.canary.fragments.setup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupMembershipPreviewBinding;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.api.SubscriptionAPIService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.ui.WebActivity;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.JSONUtil;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ACTIVATE_MEMBERSHIP_ADD;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ACTIVATE_MEMBERSHIP_SKIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ACTIVATE_TRIAL_ADD_RETRY;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ACTIVATE_TRIAL_SKIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MEMBERSHIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_SETUP_COMPLETE_ADD_MEMBERSHIP;

public class PurchaseMembershipFragment extends SetUpBaseFragment implements OnClickListener {

    private static final int SUBSCRIPTION_UPDATE = 8390;

    private static final String LOG_TAG = "PurchaseMembershipFragment";

    private Device device;
    private Location location;

    public static PurchaseMembershipFragment newInstance(Device device) {
        PurchaseMembershipFragment fragment = new PurchaseMembershipFragment();
        Bundle args = new Bundle();
        args.putString(key_deviceJSON, JSONUtil.getJSONString(device));
        fragment.setArguments(args);

        return fragment;
    }

    private FragmentSetupMembershipPreviewBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetupMembershipPreviewBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        device = JSONUtil.getObject(getArguments().getString(key_deviceJSON), Device.class);
        location = LocationDatabaseService.getLocationFromId(device.getLocationId());

        binding.setLocation(location);

        binding.setDevice(device);

        binding.addMembershipBtn.setOnClickListener(this);
        binding.noThanksBtn.setOnClickListener(this);

        if (device.isHSNDevice()) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) binding.addMembershipBtn.getLayoutParams();
            layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.standard_margin);
            binding.headerTextView.setText(R.string.your_purchase_includes_three_months);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.disableBackButton();
        fragmentStack.setHeaderTitle(R.string.membership);
    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_SETUP_COMPLETE_ADD_MEMBERSHIP;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_membership_btn:
                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_ACTIVATE_MEMBERSHIP_ADD, null, device.uuid, device.getLocationId(), 0);
                String url;

                if (device.isHSNDevice()) {
                    url = Constants.subscriptionSelect(location, getContext(), "view");
                } else {
                    url = Constants.subscriptionSelect(location, getContext(), null);
                }
                Log.i(LOG_TAG, url);

                String title = getString(R.string.membership);
                Intent intent = WebActivity.newInstance(getContext(), url, title, true);
                startActivityForResult(intent, SUBSCRIPTION_UPDATE);
                break;
            case R.id.no_thanks_btn:
                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_ACTIVATE_MEMBERSHIP_SKIP, null, device.uuid, device.getLocationId(), 0);
                addFragmentToStack(
                        getInstance(StartFreeMembershipFragment.class),
                        Utils.SLIDE_FROM_RIGHT);
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
                    } else if (device.isHSNDevice()) {
                        mDialog = AlertUtils.activateMembershipFreeTrail(getContext(), true, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_ACTIVATE_TRIAL_ADD_RETRY,
                                        null, device.uuid, device.getLocationId(), 0);
                                String url = Constants.subscriptionSelect(location, getContext(), "view");
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
