package is.yranac.canary.fragments.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsLocationBinding;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.insurance.InsurancePolicy;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.api.LocationAPIService;
import is.yranac.canary.ui.WebActivity;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_MANAGE_MEMBERSHIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MEMBERSHIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_MEMBERSHIP_SETTINGS;

/**
 * Created by michaelschroeder on 6/7/17.
 */

public class LocationSettingsFragment extends SettingsFragment implements View.OnClickListener {


    private FragmentSettingsLocationBinding binding;


    private Location location;
    private Subscription subscription;

    private InsurancePolicy insurancePolicy;
    private List<Device> devices;

    public static LocationSettingsFragment newInstance(int locationId) {
        LocationSettingsFragment fragment = new LocationSettingsFragment();

        Bundle args = new Bundle();
        args.putInt(location_id, locationId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsLocationBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.manageMembership.setOnClickListener(this);
        binding.editAddress.setOnClickListener(this);
        binding.editGeofence.setOnClickListener(this);
        binding.emergencyNumbers.setOnClickListener(this);
        binding.insurance.setOnClickListener(this);
        setUpLocationDelete(devices, location, subscription);

    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onRightButtonClick() {

    }

    public void setupLocationData(List<Device> devices,
                                  Location location,
                                  Subscription subscription,
                                  InsurancePolicy insurancePolicy) {

        this.location = location;
        this.subscription = subscription;
        this.insurancePolicy = insurancePolicy;
        this.devices = devices;

        setUpLocationDelete(devices, location, subscription);
    }

    private void setUpLocationDelete(List<Device> devices, final Location location, Subscription subscription) {
        if (binding == null)
            return;

        if (location == null) {
            binding.removeLocationBtn.setVisibility(View.GONE);
            return;
        }
        Customer customer = CurrentCustomer.getCurrentCustomer();

        boolean isOwner = false;

        if (customer != null) {
            isOwner = customer.resourceUri.equalsIgnoreCase(location.locationOwner);
        }

        boolean hasMembership = false;

        if (subscription != null) {
            hasMembership = subscription.hasMembership;
        }

        if (!location.isUnitedStates() || !isOwner) {
            binding.insurance.setVisibility(View.GONE);
        }

        if (!hasMembership) {
            binding.manageMembership.setVisibility(View.GONE);
        }

        if (devices.isEmpty() && isOwner && !hasMembership) {
            binding.removeLocationBtn.setVisibility(View.VISIBLE);
            binding.removeLocationBtn.setOnClickListener(this);
        } else {
            binding.removeLocationBtn.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manage_membership:
                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_MANAGE_MEMBERSHIP, PROPERTY_MEMBERSHIP_SETTINGS, null, location.id, 0);
                String url = Constants.autoLoginAccountUrl(location, getContext());

                String title = getString(R.string.membership);
                Intent intent = WebActivity.newInstance(getContext(), url, title, true);
                startActivity(intent);
                break;

            case R.id.edit_address:
                addFragmentToStack(getInstance(EditLocationFragment.class), Utils.SLIDE_FROM_RIGHT);
                break;

            case R.id.edit_geofence:
                addFragmentToStack(getInstance(GeofenceFragment.class), Utils.SLIDE_FROM_RIGHT);
                break;

            case R.id.emergency_numbers:
                addFragmentToStack(getInstance(LocationEmergencyContactFragment.class), Utils.SLIDE_FROM_RIGHT);
                break;

            case R.id.insurance:
                if (insurancePolicy == null) {
                    addModalFragment(getInstance(DataOptinFragment.class));
                } else {
                    addFragmentToStack(getInstance(EditInsuranceFragment.class), Utils.SLIDE_FROM_RIGHT);
                }
                break;
            case R.id.remove_location_btn:
                if (location == null)
                    return;
                AlertUtils.showRemoveAlert(getContext(), getString(R.string.are_you_sure_remove_location, location.name), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoading(true, getString(R.string.removing));
                        LocationAPIService.deleteLocation(location.id, new Callback<Void>() {
                            @Override
                            public void success(Void aVoid, Response response) {
                                showLoading(false, getString(R.string.removing));
                                getActivity().finish();
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                showLoading(false, getString(R.string.removing));
                            }
                        });
                    }
                });
                break;
        }
    }

}
