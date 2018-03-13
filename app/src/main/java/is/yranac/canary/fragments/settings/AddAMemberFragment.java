package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentAddAMemberBinding;
import is.yranac.canary.messages.ModalPopped;
import is.yranac.canary.model.invitation.Invitation;
import is.yranac.canary.services.api.InvitationsAPIService;
import is.yranac.canary.services.database.InvitationDatabaseService;
import is.yranac.canary.ui.views.CanaryTextWatcher;
import is.yranac.canary.ui.views.EditTextWithLabel;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.TouchTimeUtil;
import is.yranac.canary.util.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_ADD_A_MEMBER;

public class AddAMemberFragment extends SettingsFragment {
    private int locationId;

    private boolean popOnResume = false;

    private FragmentAddAMemberBinding binding;
    private List<EditTextWithLabel> editTextList;

    public static AddAMemberFragment newInstance(int locationId) {
        AddAMemberFragment fragment = new AddAMemberFragment();

        Bundle args = new Bundle();
        args.putInt("locationId", locationId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddAMemberBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationId = getArguments().getInt("locationId");

        final WeakReference<AddAMemberFragment> reference = new WeakReference<>(this);
        binding.inviteButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reference.get().hideSoftKeyboard();
                        reference.get().invite();
                    }
                }
        );

        editTextList = new ArrayList<>();
        editTextList.add(binding.firstNameLayout);
        editTextList.add(binding.lastNameLayout);
        editTextList.add(binding.emailLayout);
        editTextList.add(binding.confirmEmailLayout);

        setupViewWatcher(binding.firstNameLayout, CanaryTextWatcher.ANY_TEXT, editTextList, stackFragmentCallback);
        setupViewWatcher(binding.lastNameLayout, CanaryTextWatcher.ANY_TEXT, editTextList, stackFragmentCallback);
        setupViewWatcher(binding.emailLayout, CanaryTextWatcher.VALID_EMAIL, editTextList, stackFragmentCallback);
        setupViewWatcher(binding.confirmEmailLayout, CanaryTextWatcher.VALID_EMAIL, editTextList, stackFragmentCallback);
    }

    private StackFragmentCallback stackFragmentCallback = new StackFragmentCallback() {
        @Override
        public void onAllEditTextHasChanged(boolean allValid) {
            binding.inviteButton.setEnabled(allValid);
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        showLoading(false, getString(R.string.sending_elpise));
        binding.inviteButton.setEnabled(textIsGood(editTextList));
        if (popOnResume)
            getActivity().onBackPressed();
    }

    private void invite() {

        if (TouchTimeUtil.dontAllowTouch())
            return;

        String email = binding.emailLayout.getText().trim();
        String confirmEmail = binding.confirmEmailLayout.getText().trim();

        if (!email.equalsIgnoreCase(confirmEmail)) {
            AlertUtils.showGenericAlert(getActivity(), getString(R.string.emails_dont_match), getString(R.string.emails_dont_match_dsc));
            binding.emailLayout.showErrorState();
            binding.confirmEmailLayout.showErrorState();
            return;
        }

        showLoading(true, getString(R.string.sending_elpise));
        final Invitation invitation = new Invitation();

        invitation.email = email;
        invitation.firstName = binding.firstNameLayout.getText().trim();
        invitation.lastName = binding.lastNameLayout.getText().trim();
        invitation.locationUri = Utils.buildResourceUri(Constants.LOCATIONS_URI, locationId);
        invitation.status = "pending";
        invitation.userType = "primary";

        InvitationsAPIService.createInvitation(invitation, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {

                int invitationId = 0;
                for (Header header : response.getHeaders()) {
                    if ("location".equalsIgnoreCase(header.getName())) {
                        invitationId = Utils.getIntFromResourceUri(
                                header.getValue()
                                        .substring(Constants.BASE_URL.length()));
                        break;
                    }
                }

                InvitationDatabaseService.insertInvitation(invitationId, invitation);

                FragmentActivity activity = getActivity();
                if (activity != null && isVisible()) {
                    activity.onBackPressed();
                    showLoading(false, getString(R.string.sending_elpise));
                } else {
                    popOnResume = true;
                }


            }

            @Override
            public void failure(RetrofitError error) {
                showLoading(false, getString(R.string.sending_elpise));
                try {
                    AlertUtils.showGenericAlert(getActivity(), Utils.getErrorMessageFromRetrofit(getActivity(), error));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_ADD_A_MEMBER;
    }

    @Override
    public void onDestroy() {
        TinyMessageBus.post(new ModalPopped());
        super.onDestroy();
    }
}
