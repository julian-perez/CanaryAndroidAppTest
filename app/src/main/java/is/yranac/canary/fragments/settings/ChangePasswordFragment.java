package is.yranac.canary.fragments.settings;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.databinding.ChangePasswordFragmentBinding;
import is.yranac.canary.fragments.StackFragment;
import is.yranac.canary.messages.PushToken;
import is.yranac.canary.model.authentication.OauthResponse;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.CustomerChangePassword;
import is.yranac.canary.services.api.CustomerAPIService;
import is.yranac.canary.services.api.OathAuthenticationAPIService;
import is.yranac.canary.services.database.DeviceTokenDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.ui.views.CanaryTextWatcher;
import is.yranac.canary.ui.views.EditTextWithLabel;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.keystore.KeyStoreHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_CHANGE_PASSWORD;

public class ChangePasswordFragment extends SettingsFragment implements View.OnClickListener, StackFragment.StackFragmentCallback {

    private static final String LOG_TAG = "EditUserDetailsFragment";

    private boolean popOnResume = false;

    private ChangePasswordFragmentBinding binding;
    private List<EditTextWithLabel> editTextList = new ArrayList<>();

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = ChangePasswordFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.changePasswordButton.setOnClickListener(this);
        binding.showPasswordContainer.setOnClickListener(this);

        editTextList.add(binding.oldPasswordTextView);
        editTextList.add(binding.newPasswordTextView);

        setupViewWatcher(binding.oldPasswordTextView, CanaryTextWatcher.VALID_PASSWORD, editTextList, this);
        setupViewWatcher(binding.newPasswordTextView, CanaryTextWatcher.VALID_PASSWORD, editTextList, this);

    }

    @Override
    public void onResume() {
        super.onResume();

        fragmentStack.setHeaderTitle(R.string.change_password_title);
        fragmentStack.showLogoutButton(false);
        fragmentStack.showRightButton(0);

        onAllEditTextHasChanged(textIsGood(editTextList));

        showLoading(false, null);

        if (popOnResume) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onRightButtonClick() {
    }


    private void changePassword() {
        hideSoftKeyboard();

        if (!Utils.goodPassword(PreferencesUtils.getUserName(), binding.newPasswordTextView.getText(), getContext())) {
            return;
        }
        showLoading(true, getString(R.string.saving));
        CustomerAPIService.changeCustomerPassword(
                CurrentCustomer.getCurrentCustomer().id,
                new CustomerChangePassword(binding.newPasswordTextView.getText(), binding.oldPasswordTextView.getText()),
                new Callback<Void>() {
                    @Override
                    public void success(Void aVoid, Response response) {
                        DeviceTokenDatabaseService.deleteTokens();
                        getNewToken();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        BaseActivity activity = (BaseActivity) getActivity();
                        if (activity != null && !activity.isPaused() && isVisible()) {
                            try {
                                AlertUtils.showGenericAlert(getActivity(), Utils.getErrorMessageFromRetrofit(getActivity(), error));
                            } catch (JSONException ignored) {
                            }
                            showLoading(false, null);
                        }
                    }
                });
    }

    private void getNewToken() {
        String username = PreferencesUtils.getUserName();
        String password = binding.newPasswordTextView.getText();

        OathAuthenticationAPIService.oauthAuthentication(username, password, new Callback<OauthResponse>() {
            @Override
            public void success(OauthResponse oauthResponse, Response response) {
                showLoading(false, null);
                KeyStoreHelper.saveToken(oauthResponse.accessToken);
                KeyStoreHelper.saveRefreshToken(oauthResponse.refreshToken);
                BaseActivity activity = (BaseActivity) getActivity();
                if (activity == null || activity.isPaused() || !isVisible()) {
                    popOnResume = true;
                } else {
                    getActivity().onBackPressed();
                }
                TinyMessageBus.post(new PushToken());
            }

            @Override
            public void failure(RetrofitError error) {
                BaseActivity activity = (BaseActivity) getActivity();
                if (activity != null && !activity.isPaused() && isVisible()) {
                    showLoading(false, null);
                    try {
                        AlertUtils.showGenericAlert(getActivity(), Utils.getErrorMessageFromRetrofit(getActivity(), error));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_CHANGE_PASSWORD;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_password_container:
                Typeface typeface = binding.oldPasswordTextView.getEditText().getTypeface();

                binding.showWifiPasswordCheckBox.toggle();

                if (binding.showWifiPasswordCheckBox.isChecked()) {
                    binding.oldPasswordTextView.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                    binding.newPasswordTextView.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                } else {
                    binding.oldPasswordTextView.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    binding.newPasswordTextView.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

                binding.newPasswordTextView.getEditText().setTypeface(typeface);
                binding.oldPasswordTextView.getEditText().setTypeface(typeface);

                break;
            case R.id.change_password_button:
                changePassword();
                break;


        }
    }

    @Override
    public void onAllEditTextHasChanged(boolean allValid) {
        binding.changePasswordButton.setEnabled(allValid);
    }
}

