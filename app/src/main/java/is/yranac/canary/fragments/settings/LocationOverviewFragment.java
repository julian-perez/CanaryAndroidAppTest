package is.yranac.canary.fragments.settings;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.adapter.LocationSettingsAdapter;
import is.yranac.canary.databinding.FragmentSettingsLocationOverviewBinding;
import is.yranac.canary.fragments.setup.GetHelpFragment;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.insurance.InsurancePolicy;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.TouchUtil;
import is.yranac.canary.util.cache.location.GetLocation;
import is.yranac.canary.util.cache.location.GotLocationData;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_MEMBERSHIP;
import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_TYPE_NO_MEMBERSHIP;
import static is.yranac.canary.util.ColorUtil.brightBlueSky;
import static is.yranac.canary.util.ColorUtil.darkGray;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_HELP;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_SETTINGS;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_MEMBERSHIP;

public class LocationOverviewFragment extends SettingsFragment implements View.OnClickListener, View.OnTouchListener,
        ViewPager.OnPageChangeListener, MembershipFragment.FirstPageChangeListener {

    private static final String LOG_TAG = "LocationOverviewFragment";
    private int locationId;

    private Location location;

    private InsurancePolicy insurancePolicy;

    private FragmentSettingsLocationOverviewBinding binding;
    private Subscription subscription;

    private LocationSettingsAdapter adapter;
    private float leftPosition;
    private float rightPosition;
    private int page;

    public static LocationOverviewFragment newInstance(int locationId) {
        LocationOverviewFragment fragment = new LocationOverviewFragment();

        Bundle args = new Bundle();
        args.putInt(location_id, locationId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsLocationOverviewBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        locationId = getArguments().getInt(location_id);


        adapter = new LocationSettingsAdapter(getChildFragmentManager(), locationId);
        binding.locationSettingsPager.setAdapter(adapter);
        adapter.getMembershipFragment().setFirstPageChangeListener(this);

        binding.mapHeader.membershipLayout.setOnClickListener(this);
        binding.mapHeader.settingsLayout.setOnClickListener(this);

        binding.locationSettingsPager.setClipChildren(false);
        binding.locationSettingsPager.setOnTouchListener(this);


        binding.locationSettingsPager.addOnPageChangeListener(this);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (leftPosition == 0) {

                    leftPosition = binding.mapHeader.membershipLayout.getX() +
                            (binding.mapHeader.membershipLayout.getWidth() / 2) -
                            (binding.mapHeader.selectedView.getWidth() / 2);
                    rightPosition = binding.mapHeader.settingsLayout.getX() +
                            (binding.mapHeader.settingsLayout.getWidth() / 2) -
                            (binding.mapHeader.selectedView.getWidth() / 2);
                    binding.mapHeader.selectedView.setX(leftPosition);
                }

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        TinyMessageBus.post(new GetLocation(locationId));
        fragmentStack.showHelpButton();
        fragmentStack.enableRightButton(this, true);

    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentStack.resetButtonStyle();
    }


    @Override
    public void onStart() {
        super.onStart();
        TinyMessageBus
                .register(this);
    }

    @Override
    public void onStop() {
        TinyMessageBus
                .unregister(this);
        super.onStop();
    }


    private void initView(Subscription sub, List<Device> devices) {
        if (location == null)
            return;

        if (subscription == null) {

            subscription = sub;

            if (subscription == null) {
                binding.locationSettingsPager.setCurrentItem(1, false);
                return;
            }
            if (subscription.hasMembership) {
                binding.locationSettingsPager.setCurrentItem(1, false);
            } else {
                binding.locationSettingsPager.setCurrentItem(0, false);

            }
        } else {
            subscription = sub;
        }


        fragmentStack.setHeaderTitle(location.name);

        binding.mapHeader.locationAddress.setText(location.address);


        binding.locationSettingsPager.setVisibility(View.VISIBLE);

        if (this.subscription.hasMembership) {
            binding.mapHeader.membershipStatus.setText(R.string.membership_active);
            binding.mapHeader.membershipStatus.setTextColor(brightBlueSky(getContext()));
        } else if (this.subscription.onTrial) {
            binding.mapHeader.membershipStatus.setText(getString(R.string.days_left_membership_preview,
                    this.subscription.remainingDays()));
            binding.mapHeader.membershipStatus.setTextColor(brightBlueSky(getContext()));
            binding.mapHeader.selectedView.setBackgroundColor(brightBlueSky(getContext()));
        } else {
            binding.mapHeader.membershipStatus.setText(R.string.membership_inactive);
            binding.mapHeader.membershipStatus.setTextColor(darkGray(getContext()));
            binding.mapHeader.selectedView.setBackgroundColor(darkGray(getContext()));
        }


        binding.mapHeader.selectedView.setVisibility(View.VISIBLE);
        adapter.getMembershipFragment().setupLocationAndSubscription(location, subscription);
        adapter.getLocationSettingsFragment().setupLocationData(devices, location, subscription, insurancePolicy);

    }


    @Subscribe
    public void onGotLocationData(GotLocationData locationData) {
        if (locationData.location == null || locationData.location.id != locationId) {
            return;
        }

        List<Device> devices = locationData.deviceList;
        location = locationData.location;

        insurancePolicy = locationData.insurancePolicy;

        initView(locationData.subscription, devices);


    }


    @Override
    public void onRightButtonClick() {
        is.yranac.canary.fragments.setup.GetHelpFragment fragment;
        GetHelpFragment.GetHelpType type;
        if (subscription != null && subscription.hasMembership) {
            type = GET_HELP_TYPE_MEMBERSHIP;
        } else {
            type = GET_HELP_TYPE_NO_MEMBERSHIP;
        }
        fragment = is.yranac.canary.fragments.setup.GetHelpFragment.newInstance(type);

        GoogleAnalyticsHelper.trackEvent(CATEGORY_SETTINGS, ACTION_HELP, PROPERTY_MEMBERSHIP, null, location.id, 0);

        addModalFragment(fragment);
    }

    @Override
    protected String getAnalyticsTag() {
        return null;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.membership_layout:
                binding.locationSettingsPager.setCurrentItem(0, true);
                break;
            case R.id.settings_layout:
                binding.locationSettingsPager.setCurrentItem(1, true);
                break;
        }

    }

    private View inside;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float x = event.getRawX();
        float y = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {


                if (TouchUtil.insideButton(binding.mapHeader.settingsLayout, x, y)) {
                    inside = binding.mapHeader.settingsLayout;
                }

                if (TouchUtil.insideButton(binding.mapHeader.membershipLayout, x, y)) {
                    inside = binding.mapHeader.membershipLayout;
                }

                break;
            }
            case MotionEvent.ACTION_UP: {
                if (inside != null) {
                    inside.performClick();
                    inside = null;
                }
            }
            case MotionEvent.ACTION_MOVE: {
                if (inside != null) {
                    if (!TouchUtil.insideButton(inside, x, y)) {
                        inside = null;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 1) {
            binding.mapHeader.selectedView.setX(rightPosition);
            binding.mapHeader.selectedView.setAlpha(1.0f);
            binding.mapHeader.bottomButtons.setAlpha(1.0f);
        } else {
            binding.mapHeader.selectedView.setX(leftPosition + ((rightPosition - leftPosition) * positionOffset));

            if (page != 0) {
                binding.mapHeader.selectedView.setAlpha(positionOffset);
                binding.mapHeader.bottomButtons.setAlpha(positionOffset);
                if (positionOffset == 0.0) {
                    binding.mapHeader.bottomButtons.setVisibility(View.INVISIBLE);
                    binding.mapHeader.selectedView.setVisibility(View.INVISIBLE);
                } else {
                    binding.mapHeader.bottomButtons.setVisibility(View.VISIBLE);
                    binding.mapHeader.selectedView.setVisibility(View.VISIBLE);
                }
            }
        }


    }

    @Override
    public void onPageSelected(int position) {
        if (position == 1) {
            binding.mapHeader.selectedView.setX(rightPosition);
            binding.mapHeader.selectedView.setAlpha(1.0f);
            binding.mapHeader.bottomButtons.setAlpha(1.0f);
            binding.mapHeader.bottomButtons.setVisibility(View.VISIBLE);
            binding.mapHeader.selectedView.setVisibility(View.VISIBLE);
        } else {
            binding.mapHeader.selectedView.setX(leftPosition);
            if (page != 0) {
                binding.mapHeader.selectedView.setAlpha(0.0f);
                binding.mapHeader.bottomButtons.setAlpha(0.0f);
                binding.mapHeader.bottomButtons.setVisibility(View.INVISIBLE);
                binding.mapHeader.selectedView.setVisibility(View.INVISIBLE);
            }

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onFirstPageChange(float change, int page) {
        if (binding.locationSettingsPager.getCurrentItem() != 0)
            return;

        this.page = page;
        binding.mapHeader.bottomButtons.setAlpha(1.0f - change);
        binding.mapHeader.selectedView.setAlpha(1.0f - change);

        if (change == 1.0f) {
            binding.mapHeader.bottomButtons.setVisibility(View.INVISIBLE);
            binding.mapHeader.selectedView.setVisibility(View.INVISIBLE);
        } else {
            binding.mapHeader.bottomButtons.setVisibility(View.VISIBLE);
            binding.mapHeader.selectedView.setVisibility(View.VISIBLE);
        }
    }
}
