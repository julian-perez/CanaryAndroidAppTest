package is.yranac.canary.fragments.setup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupConfirmNetworkBinding;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_WIFI_CONNECTION_SETUP;

public class GetWifiPasswordFragment extends SetUpBaseFragment {

    private String wifiSSID;

    private FragmentSetupConfirmNetworkBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSetupConfirmNetworkBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle args = getArguments();
        wifiSSID = args.getString(SSID);
        String enterWifiPassword;
        if (TextUtils.isEmpty(wifiSSID)) {
            binding.wifiNameContainer.setVisibility(View.VISIBLE);
            enterWifiPassword = getString(R.string.enter_wifi_name_and_password);
            binding.wifiNameContainer.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    binding.nextBtn.setEnabled(!TextUtils.isEmpty(s.toString()));

                }
            });

        } else {
            binding.wifiNameContainer.setVisibility(View.GONE);
            enterWifiPassword = String.format(getString(R.string.enter_password), wifiSSID);
            binding.nextBtn.setEnabled(true);
        }
        binding.wifiNameTextView.setText(enterWifiPassword);

        onClickFocusListener(binding.wifiPasswordContainer, binding.wifiPasswordContainer.getEditText());
        PasswordOnClickListener passwordOnClickListener = new PasswordOnClickListener(binding.showWifiPasswordCheckBox, binding.wifiPasswordContainer.getEditText());
        binding.showPasswordContainer.setOnClickListener(passwordOnClickListener);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wifiPassword = binding.wifiPasswordContainer.getText();


                if (StringUtils.isNullOrEmpty(wifiSSID)) {
                    wifiSSID = binding.wifiNameContainer.getText();
                    getArguments().putString(SSID, wifiSSID);
                }

                if (TextUtils.isEmpty(wifiPassword)) {
                    showWarning();
                    return;
                }
                showNextFragment();
            }
        });
    }


    private void showWarning() {
        AlertUtils.showGenericAlert(getContext(), getString(R.string.continue_without_password),
                getString(R.string.recommend_secure_network), 0, getString(R.string.cancel),
                getString(R.string.continue_text), 0, 0, null, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showNextFragment();
                    }
                });
    }

    @Override
    public void onRightButtonClick() {


        GetHelpFragment fragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_INTERNET);
        addModalFragment(fragment);

    }

    private void showNextFragment() {

        Bundle args = getArguments();

        args.putString(WIFI_PASSWORD, binding.wifiPasswordContainer.getText());

        ConnectCableSetUpFragment fragment = ConnectCableSetUpFragment.newInstance(args);
        addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
    }


    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.setHeaderTitle(R.string.connect);
        fragmentStack.showHelpButton();
        fragmentStack.enableRightButton(this, true);

    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_WIFI_CONNECTION_SETUP;
    }
}
