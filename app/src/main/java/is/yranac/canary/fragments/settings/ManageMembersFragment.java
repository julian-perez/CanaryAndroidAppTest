package is.yranac.canary.fragments.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.adapter.ManageMembersAdapter;
import is.yranac.canary.databinding.ListrowAddMemberFooterBinding;
import is.yranac.canary.messages.ModalPopped;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.invitation.Invitation;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.membership.Member;
import is.yranac.canary.services.database.CustomerDatabaseService;
import is.yranac.canary.services.database.InvitationDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_MEMBER;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_LOCATION_MEMBER_SETTINGS;

public class ManageMembersFragment extends SettingsFragment implements OnItemClickListener {
    private static final String LOG_TAG = "ManageMembersFragment";


    private int locationId;


    private ManageMembersAdapter adapter;
    private ListView listView;
    private boolean isOwner;


    public static ManageMembersFragment newInstance(int locationId) {
        ManageMembersFragment fragment = new ManageMembersFragment();

        Bundle args = new Bundle();
        args.putInt("locationId", locationId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings_simple_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationId = getArguments().getInt("locationId");

        listView = (ListView) view.findViewById(R.id.list_view);

        Location location = LocationDatabaseService.getLocationFromId(locationId);
        String customerUri = CurrentCustomer.getCurrentCustomer().resourceUri;

        isOwner = customerUri.equalsIgnoreCase(location.locationOwner);
        adapter = new ManageMembersAdapter(getContext());
        listView.setOnItemClickListener(this);

        ListrowAddMemberFooterBinding footerBinding = DataBindingUtil.inflate(getLayoutInflater(savedInstanceState),
                R.layout.listrow_add_member_footer, listView, false);

        footerBinding.footerText.setText(R.string.canary_works_best);

        listView.addFooterView(footerBinding.getRoot(), null, false);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);


    }

    private List<Member> setCustomerContacts() {
        List<Member> customerContacts = new ArrayList<>();
        int customerId = CurrentCustomer.getCurrentCustomer().id;

        List<Customer> customers = CustomerDatabaseService.getCustomersAtLocation(locationId);

        for (Customer customer : customers) {
            if (customer.id == customerId) {
                customerContacts.add(0, new Member(customer));
            } else {
                customerContacts.add(new Member(customer));
            }
        }

        return customerContacts;
    }

    private List<Member> setCustomerInvitedContacts() {
        List<Member> customerInvitedContacts = new ArrayList<>();
        List<Invitation> invitations = InvitationDatabaseService.getInvitationsAtLocation(locationId);

        for (Invitation invitation : invitations) {
            customerInvitedContacts.add(new Member(invitation));
        }

        return customerInvitedContacts;
    }


    @Override
    public void onStart() {
        super.onStart();
        fragmentStack.showLogoutButton(false);
        buildMemberArray();
        TinyMessageBus.register(this);
    }


    @Override
    public void onResume() {
        super.onResume();

        fragmentStack.setHeaderTitle(R.string.members);
        fragmentStack.showHelpButton();
        fragmentStack.enableRightButton(this, true);
    }

    @Override
    public void onPause() {
        super.onPause();

        fragmentStack.resetButtonStyle();

    }


    @Override
    public void onStop() {
        TinyMessageBus.unregister(this);
        super.onStop();
    }


    @Override
    public void onRightButtonClick() {

        is.yranac.canary.fragments.setup.GetHelpFragment fragment;
        fragment = is.yranac.canary.fragments.setup.GetHelpFragment.newInstance(GET_HELP_TYPE_MEMBER);
        addModalFragment(fragment);
    }

    private void buildMemberArray() {
        List<Member> members = new ArrayList<>();
        members.addAll(setCustomerContacts());
        members.addAll(setCustomerInvitedContacts());
        members.add(new Member(Member.ADD_NEW_MEMBER));
        adapter.clear();
        adapter.addAll(members);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Member member = adapter.getItem(position);
        if (member == null)
            return;


        BaseActivity baseActivity = (BaseActivity) getActivity();
        switch (member.memberType) {
            case Member.ADD_NEW_MEMBER:
                if (!baseActivity.hasInternetConnection())
                    return;
                addModalFragment(
                        AddAMemberFragment.newInstance(locationId));
                break;
            case Member.MEMBER:
                if (PreferencesUtils.getUserName().equalsIgnoreCase(member.customer.email)) {
                    if (!baseActivity.hasInternetConnection())
                        return;
                    addFragmentToStack(
                            new ProfileFragment(), Utils.SLIDE_FROM_RIGHT);
                } else {
                    addFragmentToStack(
                            ViewBackupMemberFragment.newInstance(new Gson().toJson(member.customer), locationId, isOwner), Utils.SLIDE_FROM_RIGHT);
                }
                break;
            case Member.INVITATION:
                addFragmentToStack(
                        ViewInvitedMemberFragment.newInstance(new Gson().toJson(member.invitation)), Utils.SLIDE_FROM_RIGHT);
                break;
        }
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_LOCATION_MEMBER_SETTINGS;
    }


    @Subscribe
    public void onModalPopped(ModalPopped modalPopped) {
        buildMemberArray();
    }
}
