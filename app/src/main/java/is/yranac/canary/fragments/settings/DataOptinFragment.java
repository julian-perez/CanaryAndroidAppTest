package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.fragments.GenericWebviewFragment;
import is.yranac.canary.fragments.settings.InsuranceSelectorFragment.InsuranceType;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.services.api.CustomerAPIService;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_INSURANCE_LINK_POLICY;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_INSURANCE_SKIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_INSURANCE;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_INSURANCE_OTHER_PROVIDERS;

/**
 * Created by Schroeder on 4/19/16.
 */
public class DataOptinFragment extends SettingsFragment {


    public static DataOptinFragment newInstance(int locationId) {
        DataOptinFragment fragment = new DataOptinFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("locationId", locationId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.data_onboarding_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Bundle bundle = getArguments();


        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        if (!CurrentCustomer.getCurrentCustomer().seenSharePrompt) {
            CustomerAPIService.setHasSeenSharePrompt(CurrentCustomer.getCurrentCustomer());
        }

        TextView skipBtn = (TextView) view.findViewById(R.id.skip_button);
        if (bundle == null){
            skipBtn.setText(R.string.cancel);
        }

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_INSURANCE, ACTION_INSURANCE_SKIP);
                getActivity().onBackPressed();
            }
        });

        View stateFarmBtn = view.findViewById(R.id.state_farm_btn);
        stateFarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsuranceSelectorFragment fragment = InsuranceSelectorFragment.newInstance(InsuranceType.STATE_FARM);

                if (bundle != null)
                    fragment.getArguments().putAll(bundle);

                addSecondModalFragment(fragment);
            }
        });

        View libertyMutaulBtn = view.findViewById(R.id.liberty_mutual_btn);
        libertyMutaulBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsuranceSelectorFragment fragment = InsuranceSelectorFragment.newInstance(InsuranceType.LIBERTY_MUTUAL);

                if (bundle != null)
                    fragment.getArguments().putAll(bundle);

                addSecondModalFragment(fragment);
            }
        });


        View moreAboutInsurance = view.findViewById(R.id.other_insurance_provider);
        if (moreAboutInsurance != null) {
            moreAboutInsurance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoogleAnalyticsHelper.trackEvent(CATEGORY_INSURANCE, ACTION_INSURANCE_LINK_POLICY, PROPERTY_INSURANCE_OTHER_PROVIDERS);
                    GenericWebviewFragment fragment = GenericWebviewFragment.newInstance(Constants.CANARY_OTHER_INSURANCE(), R.string.canary_insurance_url_header);
                    addModalFragment(fragment);
                }
            });
        }
    }


    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onRightButtonClick() {

        getActivity().onBackPressed();
    }

}
