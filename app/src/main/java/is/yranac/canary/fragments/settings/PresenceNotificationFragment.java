package is.yranac.canary.fragments.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.adapter.ManageMembersAdapter;
import is.yranac.canary.databinding.FragmentSettingsPresenceNotificationsBinding;
import is.yranac.canary.databinding.ListrowAddMemberFooterBinding;
import is.yranac.canary.fragments.setup.GetHelpFragment;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.membership.Member;
import is.yranac.canary.model.membership.MembershipPresence;
import is.yranac.canary.services.api.MembershipAPIService;
import is.yranac.canary.services.database.MembershipDatabaseService;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.cache.location.GetLocation;
import is.yranac.canary.util.cache.location.GotLocationData;

/**
 * Created by michaelschroeder on 1/9/18.
 */

public class PresenceNotificationFragment extends SettingsFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener {


    private FragmentSettingsPresenceNotificationsBinding binding;
    private int locationId;
    private ManageMembersAdapter adapter;

    public static PresenceNotificationFragment newInstance(int locationId) {
        PresenceNotificationFragment fragment = new PresenceNotificationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(location_id, locationId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsPresenceNotificationsBinding.inflate(inflater);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationId = getArguments().getInt(location_id);


        binding.presencesCheckbox.setOnCheckedChangeListener(this);
        binding.presencesToggleLayout.setOnClickListener(this);
        binding.listView.setOnItemClickListener(this);

        adapter = new ManageMembersAdapter(getContext());

        binding.listView.setAdapter(adapter);
        ListrowAddMemberFooterBinding footerBinding = DataBindingUtil.inflate(getLayoutInflater(savedInstanceState),
                R.layout.listrow_add_member_footer, binding.listView, false);

        footerBinding.footerText.setText(R.string.only_you_wil_recieve_notifcations_presence);

        binding.listView.addFooterView(footerBinding.getRoot(), null, false);


    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.setHeaderTitle(R.string.presence);
        fragmentStack.showHelpButton();
        fragmentStack.enableRightButton(this, true);
        TinyMessageBus.post(new GetLocation(locationId));

    }

    @Override
    public void onPause() {
        super.onPause();
        MembershipAPIService.updateMembershipPresence(getContext().getApplicationContext(), locationId, CurrentCustomer.getCurrentCustomer().id, binding.presencesCheckbox.isChecked());
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onRightButtonClick() {
        is.yranac.canary.fragments.setup.GetHelpFragment getHelpFragment = GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_PRESENCE_NOTIFICATIONS);
        addModalFragment(getHelpFragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        TinyMessageBus.register(this);
    }

    @Override
    public void onStop() {
        TinyMessageBus.unregister(this);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.presences_toggle_layout:
                binding.presencesCheckbox.toggle();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        binding.listView.setVisibility(isChecked ? View.VISIBLE : View.GONE);

    }

    @Subscribe
    public void getLocationData(GotLocationData locationData) {

        List<Member> members = new ArrayList<>();
        int customerId = CurrentCustomer.getCurrentCustomer().id;

        MembershipPresence presence = MembershipDatabaseService.getMembershipsForLocation(getContext(), locationId, customerId);

        if (presence != null) {
            binding.presencesCheckbox.setChecked(presence.sendPresenceNotifications);
            onCheckedChanged(null, presence.sendPresenceNotifications);
        } else {
            onCheckedChanged(null, false);
        }

        for (Customer customer : locationData.customers) {
            if (customer.id != customerId) {
                members.add(new Member(customer));
            }
        }

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

        switch (member.memberType) {
            case Member.ADD_NEW_MEMBER:
                addModalFragment(
                        AddAMemberFragment.newInstance(locationId));
                break;
        }

    }


}
