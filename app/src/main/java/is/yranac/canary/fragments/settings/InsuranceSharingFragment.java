package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.fragments.settings.InsuranceSelectorFragment.InsuranceType;
import is.yranac.canary.messages.UpdateInsurancePolicyComplete;
import is.yranac.canary.model.insurance.InsurancePolicy;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.location.LocationInsurancePolicy.InsurancePolicyPatch;
import is.yranac.canary.services.api.LocationAPIService;
import is.yranac.canary.services.database.InsuranceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ZendeskUtil;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_INSURANCE_SAVE_SETTINGS;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_INSURANCE;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_INSURANCE_WILL_NOT_SHARE;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_INSURANCE_WILL_SHARE;

/**
 * Created by Schroeder on 6/1/16.
 */
public class InsuranceSharingFragment extends SettingsFragment {
    private static final String LOG_TAG = "InsuranceSharingFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_insurance_sharing, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        InsuranceType insuranceType = (InsuranceType) getArguments().getSerializable("insuranceType");

        String insuranceUri;
        switch (insuranceType) {
            case STATE_FARM:
                insuranceUri = Utils.buildResourceUri(Constants.INSURANCE_URI, 1);
                break;
            case LIBERTY_MUTUAL:
                insuranceUri = Utils.buildResourceUri(Constants.INSURANCE_URI, 3);
                break;
            default:
                getActivity().onBackPressed();
                return;
        }

        final InsurancePolicyPatch policy = new InsurancePolicyPatch();
        policy.insuranceProvider = insuranceUri;

        View moreAboutInsurance = view.findViewById(R.id.more_about_date_sharing_text_view);
        moreAboutInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.CANARY_DATA_SHARE();
                ZendeskUtil.loadHelpCenter(getContext(), url);
            }
        });


        Button notNowBtn = (Button) view.findViewById(R.id.not_now_btn);
        notNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int locationId = getArguments().getInt("locationId", 0);


                policy.shareEnabled = false;

                sharePolicy(policy);


                GoogleAnalyticsHelper.trackEvent(CATEGORY_INSURANCE, ACTION_INSURANCE_SAVE_SETTINGS,
                        PROPERTY_INSURANCE_WILL_NOT_SHARE, null, locationId, 0);

            }
        });
        Button okayBtn = (Button) view.findViewById(R.id.okay_btn);
        okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int locationId = getArguments().getInt("locationId", 0);
                GoogleAnalyticsHelper.trackEvent(CATEGORY_INSURANCE, ACTION_INSURANCE_SAVE_SETTINGS,
                        PROPERTY_INSURANCE_WILL_SHARE, null, locationId, 0);

                policy.shareEnabled = true;
                sharePolicy(policy);
            }
        });
    }

    private void sharePolicy(InsurancePolicyPatch policy) {
        int locationId = getArguments().getInt("locationId", 0);

        if (locationId != 0) {
            InsurancePolicy newPolicy = new InsurancePolicy();
            newPolicy.insuranceProvider = InsuranceDatabaseService.insuranceProvider(policy.insuranceProvider);
            TinyMessageBus.post(new UpdateInsurancePolicyComplete(newPolicy, locationId));
            LocationAPIService.updateInsurancePolicy(locationId, policy);
        } else {
            List<Location> locations = LocationDatabaseService.getLocationList();
            for (Location location : locations) {
                LocationAPIService.updateInsurancePolicy(location.id, policy);
            }
        }

        if (getActivity().getSupportFragmentManager().findFragmentByTag(EditInsuranceFragment.class.getSimpleName()) != null) {
            fragmentStack.popBackStack(EditInsuranceFragment.class);
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onRightButtonClick() {

    }
}
