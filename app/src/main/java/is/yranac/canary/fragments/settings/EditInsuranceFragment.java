package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.messages.UpdateInsurancePolicyComplete;
import is.yranac.canary.model.insurance.InsurancePolicy;
import is.yranac.canary.model.location.LocationInsurancePolicy.InsurancePolicyPatch;
import is.yranac.canary.services.api.LocationAPIService;
import is.yranac.canary.services.database.InsuranceDatabaseService;
import is.yranac.canary.ui.views.EditTextWithLabel;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ZendeskUtil;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_INSURANCE_LINK_POLICY;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_INSURANCE;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_INSURANCE_LEARN_MORE;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_INSURANCE_SHARE_DATA;

/**
 * Created by Schroeder on 6/1/16.
 */
public class EditInsuranceFragment extends SettingsFragment {

    private static final String LOG_TAG = "EditInsuranceFragment";
    private EditTextWithLabel policyNumberTextView;
    private TextView providerTextView;
    private SwitchCompat toggleSharing;
    private InsurancePolicy insurancePolicy;
    private int locationId;
    private boolean newPolicy;

    public static EditInsuranceFragment newInstance(int locationId) {
        EditInsuranceFragment fragment = new EditInsuranceFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("locationId", locationId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_edit_insurance, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationId = getArguments().getInt(location_id);
        providerTextView = (TextView) view.findViewById(R.id.provider);
        policyNumberTextView = (EditTextWithLabel) view.findViewById(R.id.policy_number);
        policyNumberTextView.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        enableRightButton();
                    }
                }
        );

        toggleSharing = (SwitchCompat) view.findViewById(R.id.insurance_optin_switch);

        View sharingToggleLayout = view.findViewById(R.id.insure_optin_layout);
        sharingToggleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSharing.toggle();
                enableRightButton();

            }
        });
        View moreAboutInsurance = view.findViewById(R.id.more_about_insurance_text_view);
        moreAboutInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_INSURANCE, ACTION_INSURANCE_LINK_POLICY, PROPERTY_INSURANCE_LEARN_MORE);
                String url = Constants.CANARY_INSURANCE();
                ZendeskUtil.loadHelpCenter(getContext(), url);
            }
        });

        View moreAboutDataShare = view.findViewById(R.id.more_about_date_sharing_text_view);
        moreAboutDataShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_INSURANCE, ACTION_INSURANCE_LINK_POLICY, PROPERTY_INSURANCE_SHARE_DATA);
                String url = Constants.CANARY_DATA_SHARE();
                ZendeskUtil.loadHelpCenter(getContext(), url);
            }
        });

        View insuranceProviderLayout = view.findViewById(R.id.insure_provider_layout);
        insuranceProviderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                DataOptinFragment fragment = DataOptinFragment.newInstance(locationId);
                addModalFragment(fragment);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
        fragmentStack.showHeader(true);
        fragmentStack.setHeaderTitle(R.string.insurance);
        fragmentStack.showRightButton(R.string.save);
        insurancePolicy = InsuranceDatabaseService.getInsurancePolicy(locationId);


        loadPolicyInfo();

        TinyMessageBus.register(this);

    }

    @Override
    public void onPause() {
        super.onPause();

        TinyMessageBus.unregister(this);
    }

    private void enableRightButton() {
        if (insurancePolicy == null) {
            fragmentStack.enableRightButton(this, false);
            return;
        }

        if (newPolicy) {
            fragmentStack.enableRightButton(this, true);
        } else if (toggleSharing.isChecked() != insurancePolicy.shareEnabled) {
            fragmentStack.enableRightButton(this, true);
        } else if (!policyNumberTextView.getText().equalsIgnoreCase(insurancePolicy.policyNumber)) {
            fragmentStack.enableRightButton(this, true);
        } else {
            fragmentStack.enableRightButton(this, false);
        }

    }

    @Subscribe
    public void onUpdateInsurancePolicyComplete(UpdateInsurancePolicyComplete updateInsurancePolicyComplete) {
        if (locationId != updateInsurancePolicyComplete.locationId) {
            return;
        }
        newPolicy = true;

        insurancePolicy = updateInsurancePolicyComplete.newInsurancePolicy;
        loadPolicyInfo();

    }


    private void loadPolicyInfo() {

        String provider = StringUtils.capitalize(insurancePolicy.insuranceProvider.name, null);
        String trademark = getString(R.string.trademark);

        SpannableStringBuilder denSpan = new SpannableStringBuilder(provider + trademark);

        denSpan.setSpan(new SuperscriptSpan(), provider.length(), denSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        denSpan.setSpan(new RelativeSizeSpan(0.6f), provider.length(), denSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        providerTextView.setText(denSpan, TextView.BufferType.SPANNABLE);

        policyNumberTextView.setText(insurancePolicy.policyNumber);
        toggleSharing.setChecked(insurancePolicy.shareEnabled);
        enableRightButton();
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onRightButtonClick() {

        showLoading(true, getString(R.string.updating));
        hideSoftKeyboard();
        InsurancePolicyPatch insurancePolicyPatch = new InsurancePolicyPatch();
        insurancePolicyPatch.policyNumber = policyNumberTextView.getText();
        insurancePolicyPatch.shareEnabled = toggleSharing.isChecked();
        insurancePolicyPatch.insuranceProvider = Utils.buildResourceUri(Constants.INSURANCE_URI, insurancePolicy.insuranceProvider.id);
        LocationAPIService.updateInsurancePolicy(locationId, insurancePolicyPatch);
        getActivity().onBackPressed();
    }


}
