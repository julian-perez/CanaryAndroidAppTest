package is.yranac.canary.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentMembershipModesUpsellBinding;
import is.yranac.canary.fragments.settings.SettingsFragment;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.api.SubscriptionAPIService;
import is.yranac.canary.ui.WebActivity;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.cache.location.GetLocation;
import is.yranac.canary.util.cache.location.GotLocationData;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ADD_MEMBERSHIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_HOME_MODE_SETTINGS_CTA_CANCEL;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_NIGHT_MODE_CTA_CANCEL;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_NIGHT_MODE_SETTINGS_CTA_CANCEL;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MEMBERSHIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_HOME_MODE_SETTINGS_CTA;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_NIGHT_MODE_CTA;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_NIGHT_MODE_SETTINGS_CTA;

/**
 * Created by michaelschroeder on 7/27/17.
 */

public class MembershipUpsellFragment extends SettingsFragment implements View.OnClickListener {


    public enum UpsellType {
        MODE_UPSELL_TYPE_HOME_SETTINGS,
        MODE_UPSELL_TYPE_NIGHT_SETTINGS,
        MODE_UPSELL_TYPE_NIGHT
    }

    private static final String upsell = "upsell";

    public static MembershipUpsellFragment newInstance(UpsellType mode, int locationId) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(upsell, mode);
        bundle.putInt(location_id, locationId);

        MembershipUpsellFragment fragment = new MembershipUpsellFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    private int locationId;

    private FragmentMembershipModesUpsellBinding binding;
    private UpsellType type;
    private Subscription subscription;
    public Location location;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMembershipModesUpsellBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RotateAnimation rotation = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.clockwise_rotation);
        rotation.setDuration(1500);
        binding.progressIndication.startAnimation(rotation);

        binding.addMembershipBtn.setOnClickListener(this);
        type = (UpsellType) getArguments().getSerializable(upsell);
        binding.membershipDetailsLayout.headerTextView.setText(R.string.canary_membership_includes);

        locationId = getArguments().getInt(location_id);
        if (type != null) {
            switch (type) {
                case MODE_UPSELL_TYPE_HOME_SETTINGS:
                    binding.modeTypeUpsell.setText(R.string.membership_upsell_home);
                    break;
                case MODE_UPSELL_TYPE_NIGHT_SETTINGS:
                case MODE_UPSELL_TYPE_NIGHT:
                    binding.modeTypeUpsell.setText(R.string.membership_upsell_night);
                    break;
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        TinyMessageBus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        TinyMessageBus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        TinyMessageBus.post(new GetLocation(locationId));
        binding.rootView.setVisibility(View.GONE);
        SubscriptionAPIService.getSubscription(locationId, new Callback<Subscription>() {
            @Override
            public void success(Subscription subscription, Response response) {
                MembershipUpsellFragment.this.subscription = subscription;
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

    @Subscribe
    public void gotLocationData(GotLocationData locationData) {
        if (locationData.location.id == locationId) {
            binding.membershipDetailsLayout.setLocation(locationData.location);
        }
        this.location = locationData.location;
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

                String addMembershipProperty;
                switch (type) {
                    case MODE_UPSELL_TYPE_HOME_SETTINGS:
                        addMembershipProperty = PROPERTY_HOME_MODE_SETTINGS_CTA;
                        break;
                    case MODE_UPSELL_TYPE_NIGHT_SETTINGS:
                        addMembershipProperty = PROPERTY_NIGHT_MODE_SETTINGS_CTA;
                        break;
                    case MODE_UPSELL_TYPE_NIGHT:
                        addMembershipProperty = PROPERTY_NIGHT_MODE_CTA;
                        break;
                    default:
                        return;
                }


                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_ADD_MEMBERSHIP, addMembershipProperty, null, locationId, 0);

                String url = Constants.autoLoginUrlWithPromoCodes(location, getContext(), false);
                String title = getString(R.string.membership);
                Intent intent = WebActivity.newInstance(getContext(), url, title, true);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (subscription != null && !subscription.hasMembership) {

            String cancelType;
            switch (type) {
                case MODE_UPSELL_TYPE_HOME_SETTINGS:
                    cancelType = ACTION_HOME_MODE_SETTINGS_CTA_CANCEL;
                    break;
                case MODE_UPSELL_TYPE_NIGHT_SETTINGS:
                    cancelType = ACTION_NIGHT_MODE_SETTINGS_CTA_CANCEL;
                    break;
                case MODE_UPSELL_TYPE_NIGHT:
                    cancelType = ACTION_NIGHT_MODE_CTA_CANCEL;
                    break;
                default:
                    return;
            }


            GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, cancelType, null, null, locationId, 0);
        }

    }
}
