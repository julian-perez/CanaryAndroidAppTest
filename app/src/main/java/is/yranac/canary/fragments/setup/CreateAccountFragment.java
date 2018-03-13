package is.yranac.canary.fragments.setup;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.databinding.AlertDialogTermsAndConditionsBinding;
import is.yranac.canary.databinding.FragmentCreateCurrentUserBinding;
import is.yranac.canary.fragments.GenericWebviewFragment;
import is.yranac.canary.fragments.StackFragment;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.ui.views.CanaryTextWatcher;
import is.yranac.canary.ui.views.EditTextWithLabel;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_CREATE_ACCOUNT_INITIAL;

public class CreateAccountFragment extends SetUpBaseFragment implements StackFragment.StackFragmentCallback, View.OnClickListener {

    private List<EditTextWithLabel> editTextList = new ArrayList<>();

    private FragmentCreateCurrentUserBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateCurrentUserBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowIn(true);
                getActivity().onBackPressed();
            }
        });
        binding.signInBtn.setText(StringUtils.spannableStringBuilder(getContext(), R.string.already_member, R.string.sign_in), TextView.BufferType.SPANNABLE);

        binding.nextBtn.setOnClickListener(this);
        setupValidator();
    }

    private void setupValidator() {

        editTextList.clear();
        editTextList.add(binding.emailAddress);
        editTextList.add(binding.confirmEmailAddress);
        editTextList.add(binding.password);
        editTextList.add(binding.confirmPassword);

        setupViewWatcher(binding.emailAddress, CanaryTextWatcher.VALID_EMAIL, editTextList, this);
        setupViewWatcher(binding.confirmEmailAddress, CanaryTextWatcher.VALID_EMAIL, editTextList, this);
        setupViewWatcher(binding.password, CanaryTextWatcher.ANY_TEXT, editTextList, this);
        setupViewWatcher(binding.confirmPassword, CanaryTextWatcher.ANY_TEXT, editTextList, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        fragmentStack.showHeader(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        fragmentStack.showHeader(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // check to see if we should highlight the button

        fragmentStack.showLogoutButton(false);
        fragmentStack.setHeaderTitle(R.string.account);

        if (alert != null && !alert.isShowing())
            alert.show();

        binding.nextBtn.setEnabled(textIsGood(editTextList));
    }

    private AlertDialog alert;

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRightButtonClick() {
    }

    private void checkAccountDetails() {
        hideSoftKeyboard();

        String email = binding.emailAddress.getText();
        String confirmEmail = binding.confirmEmailAddress.getText();


        String password = binding.password.getText();
        String confirmPassword = binding.confirmPassword.getText();


        if (!email.equalsIgnoreCase(confirmEmail)) {
            AlertUtils.showGenericAlert(getActivity(), getString(R.string.emails_dont_match), getString(R.string.emails_dont_match_dsc));
            binding.emailAddress.showErrorState();
            binding.confirmEmailAddress.showErrorState();
            return;
        }

        if (!password.equalsIgnoreCase(confirmPassword)) {
            AlertUtils.showGenericAlert(getActivity(), getString(R.string.passwords_dont_match), getString(R.string.passwords_dont_match_dsc));
            binding.password.showErrorState();
            binding.confirmPassword.showErrorState();
            return;
        }

        if (!Utils.checkPasswordLength(getActivity(), password)) {
            binding.password.showErrorState();
            binding.confirmPassword.showErrorState();
            return;
        }

        if (!Utils.checkPasswordCharacters(getActivity(), password)) {
            binding.password.showErrorState();
            binding.confirmPassword.showErrorState();
            return;
        }

        if (Utils.passwordUsernameMatch(getActivity(), email, password)) {
            binding.emailAddress.showErrorState();
            binding.confirmEmailAddress.showErrorState();
            binding.password.showErrorState();
            binding.confirmPassword.showErrorState();
            return;
        }


        showTermsDialog();
    }

    private void showTermsDialog() {

        LayoutInflater inflater = LayoutInflater.from(getContext());

        AlertDialogTermsAndConditionsBinding alertBinding;
        alertBinding = AlertDialogTermsAndConditionsBinding.inflate(inflater);


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                int titleId;
                switch (v.getId()) {
                    case R.id.terms_btn:
                        url = Constants.CANARY_TOS();
                        titleId = R.string.terms_of_service_label;
                        break;
                    case R.id.privacy_policy_btn:
                        url = Constants.CANARY_PP();
                        titleId = R.string.privacy_policy;
                        break;
                    case R.id.end_user_btn:
                        url = Constants.CANARY_EULA();
                        titleId = R.string.eula;
                        break;
                    default:
                        return;
                }

                addModalFragment(GenericWebviewFragment.newInstance(url, titleId));
                alert.dismiss();

            }
        };

        alertBinding.termsBtn.setOnClickListener(clickListener);
        alertBinding.privacyPolicyBtn.setOnClickListener(clickListener);
        alertBinding.endUserBtn.setOnClickListener(clickListener);
        alertBinding.iAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                alert = null;
                Customer customer = new Customer();
                customer.email = binding.emailAddress.getText();

                String password = binding.password.getText();
                EditUserDetailsFragment fragment = EditUserDetailsFragment.newInstance(new Gson().toJson(customer), password);
                addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
            }
        });

        alertBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        alert = AlertUtils.buildAlert(getContext(), alertBinding.getRoot(), true);
        alert.show();
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_CREATE_ACCOUNT_INITIAL;
    }

    @Override
    public void onAllEditTextHasChanged(boolean allValid) {
        binding.nextBtn.setEnabled(allValid);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_btn:
                checkAccountDetails();
                break;
        }
    }
}
