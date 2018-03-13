package is.yranac.canary.fragments.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.json.JSONException;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsLocationMemberBinding;
import is.yranac.canary.model.avatar.Avatar;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.services.api.MembershipAPIService;
import is.yranac.canary.services.database.AvatarDatabaseService;
import is.yranac.canary.services.database.CustomerDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.ImageUtils;
import is.yranac.canary.util.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_SINGLE_LOCATION_MEMBER;

public class ViewBackupMemberFragment extends SettingsFragment {

    private int locationId;

    private Customer customer;

    private FragmentSettingsLocationMemberBinding binding;


    public static ViewBackupMemberFragment newInstance(String customerJson, int locationId, boolean isOwner) {
        ViewBackupMemberFragment fragment = new ViewBackupMemberFragment();

        Bundle args = new Bundle();
        args.putString(customer_json, customerJson);
        args.putInt(location_id, locationId);
        args.putBoolean(IS_OWNER, isOwner);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings_location_member, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationId = getArguments().getInt(location_id);

        customer = new Gson().fromJson(getArguments().getString(customer_json), Customer.class);

        binding.nameLayout.setText(customer.getFullName());
        binding.emailLayout.setText(customer.email);
        binding.phoneLayout.setText(customer.phone);

        Avatar avatar = AvatarDatabaseService.getAvatarFromCustomerId(customer.id);


        if (avatar != null) {
            ImageUtils.loadAvatar(binding.memberImageView, avatar.thumbnailUrl());
        } else {
            binding.memberImageView.setVisibility(View.GONE);
            binding.customerInitials.setText(customer.getInitials());
        }
        boolean isOwner = getArguments().getBoolean(IS_OWNER);
        if (isOwner) {
            binding.removeMemberBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertUtils.showRemoveAlert(getContext(), getString(R.string.are_you_sure_remove, customer.getFullName()), onClickListener);
                }
            });
        } else {
            binding.removeMemberBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        fragmentStack.setHeaderTitle(R.string.location_member);
        fragmentStack.enableRightButton(this, false);
        fragmentStack.showRightButton(0);
    }


    @Override
    public void onRightButtonClick() {

        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (!baseActivity.hasInternetConnection())
            return;

        AlertUtils.showDeleteMemberAlert(getActivity(), customer.firstName, onClickListener);
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_SINGLE_LOCATION_MEMBER;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showLoading(true, getString(R.string.removing));
            MembershipAPIService.deleteMembership(locationId, customer.id, new Callback<Void>() {
                @Override
                public void success(Void aVoid, Response response) {
                    showLoading(false, getString(R.string.removing));

                    CustomerDatabaseService.deleteCustomerLocationLink(locationId, customer.id);

                    if (getActivity() != null)
                        getActivity().getSupportFragmentManager().popBackStack();

                }

                @Override
                public void failure(RetrofitError error) {
                    showLoading(false, getString(R.string.removing));
                    if (isVisible()) {
                        try {
                            AlertUtils.showGenericAlert(getActivity(), Utils.getErrorMessageFromRetrofit(getContext(), error));
                        } catch (JSONException ignore) {
                        }
                    }
                }
            });
        }
    };
}

