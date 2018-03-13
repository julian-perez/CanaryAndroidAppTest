package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsAccountBinding;
import is.yranac.canary.util.LogoutUtil;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_ACCOUNT_SETTINGS;

/**
 * Created by michaelschroeder on 5/8/17.
 */

public class AccountFragment extends SettingsFragment implements View.OnClickListener {

    private FragmentSettingsAccountBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsAccountBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.profileBtn.setOnClickListener(this);
        binding.preferencesBtn.setOnClickListener(this);
        binding.securityBtn.setOnClickListener(this);
        binding.logoutBtn.setOnClickListener(this);


    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_ACCOUNT_SETTINGS;
    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.setHeaderTitle(R.string.account);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_btn:
                addFragmentToStack(new ProfileFragment(), Utils.SLIDE_FROM_RIGHT);
                break;
            case R.id.preferences_btn:
                addFragmentToStack(new PreferencesFragment(), Utils.SLIDE_FROM_RIGHT);
                break;
            case R.id.security_btn:
                addFragmentToStack(new SecurityFragment(), Utils.SLIDE_FROM_RIGHT);
                break;
            case R.id.logout_btn:
                LogoutUtil.logout(getActivity());
                break;
        }

    }
}
