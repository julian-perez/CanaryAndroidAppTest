package is.yranac.canary.fragments.setup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.adapter.SelectionDeviceLocationAdapter;
import is.yranac.canary.databinding.FragmentSetupSelectLocationBinding;
import is.yranac.canary.databinding.SelectDeviceLocationHeaderBinding;
import is.yranac.canary.fragments.settings.LocationOverviewFragment;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.database.SubscriptionPlanDatabaseService;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.LocationUtil;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ADD_DEVICE;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_SETTINGS;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_EXISTING_LOCATION;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_NEW_LOCATION;

/**
 * Created by Schroeder on 7/18/16.
 */
public class SelectNewDeviceLocationFragment extends SetUpBaseFragment implements View.OnClickListener {

    private SelectionDeviceLocationAdapter adapter;

    public static SelectNewDeviceLocationFragment newInstance(int locationId) {
        Bundle bundle = new Bundle();
        bundle.putInt(location_id, locationId);
        SelectNewDeviceLocationFragment f = new SelectNewDeviceLocationFragment();
        f.setArguments(bundle);
        return f;
    }

    private FragmentSetupSelectLocationBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetupSelectLocationBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        final SelectDeviceLocationHeaderBinding headerBinding = DataBindingUtil.inflate(getLayoutInflater(savedInstanceState),
                R.layout.select_device_location_header, binding.listView, false);


        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int deivceThumbnailHeight = (int) ((float) view.getWidth() * 200.0f / 375.0f);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) headerBinding.headerImageView.getLayoutParams();
                params.height = deivceThumbnailHeight;
                headerBinding.headerImageView.setLayoutParams(params);
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
        int locationId = getArguments().getInt(location_id);

        List<Location> locationList = LocationDatabaseService.getLocationList();
        Location empty = new Location();
        empty.address = getString(R.string.new_address);
        empty.id = -1;
        locationList.add(empty);
        adapter = new SelectionDeviceLocationAdapter(getContext(), locationList, R.layout.setting_row_radio);
        binding.listView.addHeaderView(headerBinding.getRoot(), null, false);
        binding.listView.setAdapter(adapter);
        int i = 0;
        for (Location location : locationList) {
            if (location.id == locationId) {
                break;
            }
            i++;
        }

        if (i >= locationList.size()) {
            i = locationList.size() - 1;
        }
        binding.listView.setItemChecked(i + 1, true);
        binding.nextBtn.setOnClickListener(this);

    }

    @Override
    public void onRightButtonClick() {

    }

    private void checkLocationDevices(final int locationId) {
        String header = getString(R.string.get_more_with_membership);
        String desciption = getString(R.string.add_membership_to_add_devices);
        String cancel = getString(R.string.cancel);
        String membership = getString(R.string.add_membership);

        AlertUtils.showGenericAlert(getContext(), header, desciption, 0, cancel,
                membership, 0, ContextCompat.getColor(getContext(), R.color.azure),
                null, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addFragmentToStack(LocationOverviewFragment.newInstance(locationId), Utils.SLIDE_FROM_RIGHT);
                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.showLogoutButton(true);
        fragmentStack.setHeaderTitle(R.string.select_address);

        fragmentStack.resetButtonStyle();
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentStack.showLogoutButton(false);

    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_btn:
                int item = binding.listView.getCheckedItemPosition();
                if (item != ListView.INVALID_POSITION) {

                    if (item == adapter.getCount()) {
                        GoogleAnalyticsHelper.trackEvent(CATEGORY_SETTINGS, ACTION_ADD_DEVICE, PROPERTY_NEW_LOCATION, null, 0, 0);

                        if (LocationUtil.doesNotHaveLocationPermission(getContext())) {
                            LocationPrimerFragment fragment = new LocationPrimerFragment();
                            addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
                        } else {
                            SetAddressFragment fragment = SetAddressFragment.newInstance(getArguments(), false);
                            addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
                        }

                    } else {
                        Location location = adapter.getItem(item - 1);
                        GoogleAnalyticsHelper.trackEvent(CATEGORY_SETTINGS, ACTION_ADD_DEVICE, PROPERTY_EXISTING_LOCATION, null, location.id, 0);
                        Subscription subscription = SubscriptionPlanDatabaseService.getServicePlanForLocation(location.id);
                        List<Device> devices = DeviceDatabaseService.getActivatedDevicesAtLocation(location.id);
                        if (devices.size() >= 4 && !subscription.hasMembership) {

                            checkLocationDevices(location.id);
                        } else {
                            AddADeviceFragment fragment = AddADeviceFragment.newInstance(location.resourceUri);
                            addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
                        }
                    }
                }
                break;
        }
    }
}
