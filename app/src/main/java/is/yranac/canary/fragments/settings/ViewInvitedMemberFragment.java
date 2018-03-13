package is.yranac.canary.fragments.settings;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.adapter.SettingsAdapter;
import is.yranac.canary.databinding.FragmentSettingsSimpleListBinding;
import is.yranac.canary.model.SettingsObject;
import is.yranac.canary.model.invitation.Invitation;
import is.yranac.canary.services.api.InvitationsAPIService;
import is.yranac.canary.services.database.InvitationDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_PENDING_INVITATION;

public class ViewInvitedMemberFragment extends SettingsFragment {

    private static final String LOG_TAG = "EditUserDetailsFragment";


    private Invitation invitation;

    public static ViewInvitedMemberFragment newInstance(String invitationJson) {
        ViewInvitedMemberFragment fragment = new ViewInvitedMemberFragment();

        Bundle args = new Bundle();
        args.putString("invitationJson", invitationJson);
        fragment.setArguments(args);

        return fragment;
    }


    private FragmentSettingsSimpleListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings_simple_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        invitation = new Gson().fromJson(getArguments().getString("invitationJson"), Invitation.class);

        List<SettingsObject> settingsObjects = getSettings();

        SettingsAdapter adapter = new SettingsAdapter(
                getActivity(), settingsObjects, null, null);

        View header = getLayoutInflater(savedInstanceState).inflate(R.layout.setting_invite_header,
                binding.listView, false);
        binding.listView.addHeaderView(header, null, false);
        binding.listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.setHeaderTitle(R.string.invitation_details);
        fragmentStack.enableRightButton(this, true);
        fragmentStack.showRightButton(R.string.delete);

        fragmentStack.rightButtonBackgroundColor(Color.WHITE);
        fragmentStack.rightButtonTextColor(R.color.gray);
    }

    @Override
    public void onPause() {
        super.onPause();

        fragmentStack.rightButtonTextColor(R.color.white);
        fragmentStack.rightButtonBackground(R.drawable.button_header_selector);

    }

    private List<SettingsObject> getSettings() {
        List<SettingsObject> settingsArray = new ArrayList<>();
        settingsArray.add(SettingsObject.photoWithTitle(null,
                invitation.getFullName(), invitation.getInitials())
        );
        settingsArray.add(SettingsObject.prompt(
                invitation.email)
        );

        return settingsArray;
    }

    @Override
    public void onRightButtonClick() {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (!baseActivity.hasInternetConnection())
            return;
        AlertUtils.showDeleteInvitationAlert(getActivity(), invitation.firstName, onClickListener);
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_PENDING_INVITATION;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InvitationsAPIService.deleteInvitation(invitation.id, new Callback<Void>() {
                @Override
                public void success(Void aVoid, Response response) {
                    InvitationDatabaseService.deleteInvitation(invitation.id);

                    if (getActivity() != null)
                        getActivity().getSupportFragmentManager().popBackStack();
                }

                @Override
                public void failure(RetrofitError error) {
                    try {
                        AlertUtils.showGenericAlert(getActivity(), Utils.getErrorMessageFromRetrofit(getActivity(), error));
                    } catch (JSONException e) {
                    }
                }
            });
        }
    };
}

