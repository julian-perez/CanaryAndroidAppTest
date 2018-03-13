package is.yranac.canary.fragments.setup;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupForgotPasswordBinding;
import is.yranac.canary.fragments.StackFragment;
import is.yranac.canary.services.api.CustomerAPIService;
import is.yranac.canary.ui.views.CanaryTextWatcher;
import is.yranac.canary.ui.views.EditTextWithLabel;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.LogoutUtil;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.keystore.KeyStoreHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_RESET_PASSWORD;

public class ForgotPasswordFragment extends SetUpBaseFragment implements StackFragment.StackFragmentCallback {

    private static final String LOG_TAG = "ForgotPasswordFragment";


    private FragmentSetupForgotPasswordBinding binding;

    public static ForgotPasswordFragment newInstance(String emailText) {
        ForgotPasswordFragment f = new ForgotPasswordFragment();
        Bundle args = new Bundle();
        args.putString("emailText", emailText);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetupForgotPasswordBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.forgotPasswordEmail.setText(getArguments().getString("emailText"));
        binding.forgotPasswordEmail.getEditText().requestFocus();

        List<EditTextWithLabel> editTextList = new ArrayList<>();
        editTextList.add(binding.forgotPasswordEmail);
        setupViewWatcher(binding.forgotPasswordEmail, CanaryTextWatcher.VALID_EMAIL, editTextList, this);

        fragmentStack.showRightButton(R.string.okay);
        fragmentStack.enableRightButton(this, textIsGood(editTextList));
    }

    @Override
    public void onRightButtonClick() {
        hideSoftKeyboard();
        showLoading(true, getString(R.string.loading_dialog));
        resetPassword();
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.setHeaderTitle(R.string.reset_password);
        fragmentStack.showHeader(true);
        fragmentStack.showLogoutButton(false);
        showSoftKeyboard();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    public void enableSave(boolean enable) {
        fragmentStack.enableRightButton(this, enable);
    }

    @Override
    public void onAllEditTextHasChanged(boolean allValid) {
        enableSave(allValid);
    }


    private void resetPassword() {
        String email = binding.forgotPasswordEmail.getText();
        CustomerAPIService.resetCustomerPassword(email, new Callback<Void>() {

            @Override
            public void success(Void aVoid, Response response) {
                showLoading(false, null);
                AlertUtils.showResetPasswordSuccessDialog(getActivity(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (KeyStoreHelper.hasGoodOauthToken())
                            LogoutUtil.logoutSilent(getActivity());
                        else
                            getActivity().getSupportFragmentManager().popBackStack();
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                showLoading(false, null);
                try {
                    AlertUtils.showGenericAlert(getActivity(), Utils.getErrorMessageFromRetrofit(getActivity(), error));
                } catch (JSONException ignore) {

                }
            }
        });
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_RESET_PASSWORD;
    }
}
