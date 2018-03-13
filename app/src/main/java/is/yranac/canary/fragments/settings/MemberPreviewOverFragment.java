package is.yranac.canary.fragments.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSettingsMembershipPreviewOverBinding;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.util.AnimationHelper;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_CONTINUE_PREVIEW;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_ADD_MEMBERSHIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MEMBERSHIP;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_MEMBERSHIP_TRIAL_COMPLETE;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_MEMBERSHIP_TRIAL_COMPLETE;

/**
 * Created by Schroeder on 9/13/16.
 */
public class MemberPreviewOverFragment extends SettingsFragment {

    private static final String LOG_TAG = "MemberPreviewOverFragment";
    private List<Device> devices;
    private static final String LOCATION_ID_KEY = "locationId";

    private static final int MAX_DEVICES = 4;

    public static MemberPreviewOverFragment newInstance(int locationId) {
        MemberPreviewOverFragment fragment = new MemberPreviewOverFragment();
        Bundle args = new Bundle();
        args.putInt(LOCATION_ID_KEY, locationId);
        fragment.setArguments(args);

        return fragment;
    }


    private static final int DURATION = 250;
    private static final int OFF_SET = 2000;
    private FragmentSettingsMembershipPreviewOverBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings_membership_preview_over, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final int locationId = getArguments().getInt(LOCATION_ID_KEY);
        Location location = LocationDatabaseService.getLocationFromId(locationId);
        devices = DeviceDatabaseService.getActivatedDevicesAtLocation(locationId);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setUpViews();
                beginAnimation();
            }
        });


        int deviceNum = getDeviceNum();

        String previewOverDsc;
        if (location.isUnitedStates()) {
            previewOverDsc = getString(R.string.your_preview_ended_dsc);
        } else {
            previewOverDsc = getString(R.string.your_preview_ended_dsc_international);
        }

        if (deviceNum > 1) {
            previewOverDsc += " ";
            previewOverDsc += getString(R.string.hours_per_device, deviceNum, location.name, 24 / deviceNum);
        } else {
            previewOverDsc += ".";
        }

        binding.previewEndedDscTextView.setText(previewOverDsc);


        int hours = 24 / deviceNum;

        binding.deviceHoursOne.setText(getString(R.string.hours, hours));
        binding.deviceHoursTwo.setText(getString(R.string.hours, hours));
        binding.deviceHoursThree.setText(getString(R.string.hours, hours));
        binding.deviceHoursFour.setText(getString(R.string.hours, hours));

        for (int i = 0; i < 4; i++) {
            if (i >= devices.size()) {
                switch (i) {
                    case 1:
                        binding.deviceTwoImage.setVisibility(View.GONE);
                        binding.deviceLayoutTwo.setVisibility(View.GONE);
                        break;
                    case 2:
                        binding.deviceThreeImage.setVisibility(View.GONE);
                        binding.deviceLayoutThree.setVisibility(View.GONE);
                        break;
                    case 3:
                        binding.deviceFourImage.setVisibility(View.GONE);
                        binding.deviceLayoutFour.setVisibility(View.GONE);
                        break;
                }
            } else {
                Device device = devices.get(i);
                switch (i) {
                    case 0:
                        binding.deviceOneImage.setImageResource(getDeviceImage(device));
                        break;
                    case 1:
                        binding.deviceTwoImage.setImageResource(getDeviceImage(device));
                        break;
                    case 2:
                        binding.deviceThreeImage.setImageResource(getDeviceImage(device));
                        break;
                    case 3:
                        binding.deviceFourImage.setImageResource(getDeviceImage(device));
                        break;
                }

            }
        }


        binding.locationAddressLabel.setText(location.address);

        binding.addAMembersipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationOverviewFragment fragment = LocationOverviewFragment.newInstance(locationId);
                addFragmentToStack(fragment, Utils.SLIDE_FROM_RIGHT);
                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_ADD_MEMBERSHIP, PROPERTY_MEMBERSHIP_TRIAL_COMPLETE, null, locationId, 0);
            }
        });
        binding.continuePreviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
                GoogleAnalyticsHelper.trackEvent(CATEGORY_MEMBERSHIP, ACTION_CONTINUE_PREVIEW, PROPERTY_MEMBERSHIP_TRIAL_COMPLETE, null, locationId, 0);
            }
        });
    }


    private int getDeviceImage(Device device) {
        switch (device.getDeviceType()) {
            case DeviceType.CANARY_AIO:
            case DeviceType.CANARY_PLUS:
                return R.drawable.plus_1;
            case DeviceType.FLEX:
                return R.drawable.flex_1;
        }
        return R.drawable.plus_1;
    }

    @Override
    public void onResume() {
        super.onResume();

        fragmentStack.showRightButton(0);
        fragmentStack.showHeader(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentStack.showLogoutButton(false);
    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_MEMBERSHIP_TRIAL_COMPLETE;

    }

    private void setUpViews() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.deviceLayout.getLayoutParams();
        int width = binding.getRoot().getWidth() - params.leftMargin - params.rightMargin;

        float center = width / 2.0f;


        int padding = DensityUtil.dip2px(getContext(), 10);

        switch (devices.size()) {
            case 1:
                binding.deviceOneImage.setX(center - binding.deviceOneImage.getWidth() / 2);
                break;
            case 2:
                binding.deviceTwoImage.setX(center + padding / 2 - binding.deviceTwoImage.getWidth() / 2);
                binding.deviceOneImage.setX(binding.deviceTwoImage.getX() - padding - binding.deviceOneImage.getWidth());
                break;
            case 3:
                binding.deviceTwoImage.setX(center - padding - binding.deviceTwoImage.getWidth() / 2);
                binding.deviceOneImage.setX(binding.deviceTwoImage.getX() - padding - binding.deviceOneImage.getWidth());
                binding.deviceThreeImage.setX(center + padding + binding.deviceOneImage.getWidth());
            default:
                binding.deviceTwoImage.setX(center - padding / 2 - binding.deviceTwoImage.getWidth());
                binding.deviceOneImage.setX(binding.deviceTwoImage.getX() - padding - binding.deviceOneImage.getWidth());
                binding.deviceThreeImage.setX(center + padding / 2);
                binding.deviceFourImage.setX(binding.deviceThreeImage.getX() + padding + binding.deviceThreeImage.getWidth());
                break;
        }


    }

    private void beginAnimation() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.deviceLayout.getLayoutParams();
        int width = binding.getRoot().getWidth() - params.leftMargin - params.rightMargin;

        int deviceNum = getDeviceNum();
        float blocks = width / deviceNum;

        float centerOne = blocks * 0.5f - binding.deviceOneImage.getWidth() / 2.0f;
        float centerTwo = blocks * 1.5f - binding.deviceTwoImage.getWidth() / 2.0f;
        float centerThree = blocks * 2.5f - binding.deviceThreeImage.getWidth() / 2.0f;
        float centerFour = blocks * 3.5f - binding.deviceFourImage.getWidth() / 2.0f;

        setAnimation(binding.deviceOneImage, centerOne);
        setAnimation(binding.deviceTwoImage, centerTwo);
        setAnimation(binding.deviceThreeImage, centerThree);
        setAnimation(binding.deviceFourImage, centerFour);


        if (deviceNum > 1) {
            AnimationHelper.fadeFromAlphaToAlphaAfterDelay(binding.bigBlueLine, 1.0f, 0.0f, DURATION, OFF_SET);
            AnimationHelper.fadeFromAlphaToAlphaAfterDelay(binding.deviceHourLayout, 0.0f, 1.0f, DURATION, OFF_SET);
            setSlideAndFade();
        }

    }


    private void setSlideAndFade() {
        float endPosition = DensityUtil.dip2px(getContext(), 30);

        AnimationSet as = new AnimationSet(true);
        as.setFillEnabled(true);

        AlphaAnimation aa = new AlphaAnimation(1.0f, 0.5f);
        aa.setDuration(DURATION);
        aa.setStartOffset(OFF_SET);
        as.addAnimation(aa);


        TranslateAnimation ta = new TranslateAnimation(0, 0, 0, endPosition);
        ta.setDuration(DURATION);
        ta.setStartOffset(OFF_SET);
        as.addAnimation(ta);

        as.setFillAfter(true);
        aa.setFillBefore(true);

        binding.twentyFourHours.startAnimation(as);


    }

    private void setAnimation(View view, float endPosition) {

        if (view.getVisibility() != View.VISIBLE)
            return;

        float delta = endPosition - view.getX();

        Animation animation = new TranslateAnimation(0, delta, 0, 0);
        animation.setDuration(DURATION);
        animation.setStartOffset(OFF_SET);
        animation.setInterpolator(new AnticipateOvershootInterpolator());
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    private int getDeviceNum() {
        return devices.size() >= MAX_DEVICES ? MAX_DEVICES : devices.size();

    }
}

