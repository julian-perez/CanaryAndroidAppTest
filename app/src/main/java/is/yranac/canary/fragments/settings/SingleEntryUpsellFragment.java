package is.yranac.canary.fragments.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsSingleEntryUpsellBinding;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.api.SubscriptionAPIService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.ui.WebActivity;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ADD_MEMBERSHIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_SINGLE_ENTRY_DOWNLOAD_CTA_CANCEL;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MEMBERSHIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_SINGLE_ENTRY_DOWNLOAD_CTA;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_ADD_MEMBERSHIP_DOWNLOADS;

/**
 * Created by michaelschroeder on 6/27/17.
 */

public class SingleEntryUpsellFragment extends SettingsFragment implements View.OnClickListener {


    public static SingleEntryUpsellFragment newInstance(int locationId) {
        SingleEntryUpsellFragment fragment = new SingleEntryUpsellFragment();

        Bundle args = new Bundle();
        args.putInt(location_id, locationId);
        fragment.setArguments(args);

        return fragment;
    }

    private FragmentSettingsSingleEntryUpsellBinding binding;

    private int locationId;
    private Location location;
    private Subscription subscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsSingleEntryUpsellBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.activateMembershipBtn.setOnClickListener(this);
        locationId = getArguments().getInt(location_id);
        location =  LocationDatabaseService.getLocationFromId(locationId);
        RotateAnimation rotation = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.clockwise_rotation);
        rotation.setDuration(1500);
        binding.progressIndication.startAnimation(rotation);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.showHeader(false);

        binding.rootView.setVisibility(View.GONE);
        SubscriptionAPIService.getSubscription(locationId, new Callback<Subscription>() {
            @Override
            public void success(Subscription subscription, Response response) {
                SingleEntryUpsellFragment.this.subscription = subscription;
                if (subscription.hasMembership) {
                    getActivity().onBackPressed();
                } else {
                    binding.rootView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_ADD_MEMBERSHIP_DOWNLOADS;
    }

    @Override
    public void onRightButtonClick() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.activate_membership_btn:
                String url = Constants.autoLoginUrlWithPromoCodes(location, getContext(), false);
                String title = getString(R.string.activate_membership);
                Intent intent = WebActivity.newInstance(getContext(), url, title, true);
                startActivity(intent);
                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_ADD_MEMBERSHIP, PROPERTY_SINGLE_ENTRY_DOWNLOAD_CTA, null, locationId, 0);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.hasMembership) {
            GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_SINGLE_ENTRY_DOWNLOAD_CTA_CANCEL, null, null, locationId, 0);
        }
    }
}
