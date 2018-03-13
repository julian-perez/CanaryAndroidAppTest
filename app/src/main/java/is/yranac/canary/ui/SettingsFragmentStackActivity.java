package is.yranac.canary.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.fragments.CountryCodeSelectFragment;
import is.yranac.canary.fragments.MembershipUpsellFragment;
import is.yranac.canary.fragments.StackFragment;
import is.yranac.canary.fragments.settings.AboutCanaryFragment;
import is.yranac.canary.fragments.settings.AccountFragment;
import is.yranac.canary.fragments.settings.AddAMemberFragment;
import is.yranac.canary.fragments.settings.ConfigureModeSettingsFragment;
import is.yranac.canary.fragments.settings.DataOptinFragment;
import is.yranac.canary.fragments.settings.FailedDeviceOTAFragment;
import is.yranac.canary.fragments.settings.FlexConnectivityInfoFragment;
import is.yranac.canary.fragments.settings.GetHelpFragment;
import is.yranac.canary.fragments.settings.HomeHealthNotificationSettingsFragment;
import is.yranac.canary.fragments.settings.LocationEmergencyContactFragment;
import is.yranac.canary.fragments.settings.LocationOverviewFragment;
import is.yranac.canary.fragments.settings.ManageDevicesFragment;
import is.yranac.canary.fragments.settings.ManageMembersFragment;
import is.yranac.canary.fragments.settings.MemberPreviewOverFragment;
import is.yranac.canary.fragments.settings.ModeSettingsFragment;
import is.yranac.canary.fragments.settings.PresenceNotificationFragment;
import is.yranac.canary.fragments.settings.SingleEntryUpsellFragment;
import is.yranac.canary.fragments.setup.AddADeviceFragment;
import is.yranac.canary.fragments.setup.SelectNewDeviceLocationFragment;
import is.yranac.canary.fragments.setup.SetCanaryConnectionTypeFragment;
import is.yranac.canary.fragments.tutorials.masking.MaskingTutorialStartFragment;
import is.yranac.canary.messages.OnBackBlocked;
import is.yranac.canary.messages.PushReceived;
import is.yranac.canary.services.bluetooth.BluetoothSingleton;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.LogoutUtil;
import is.yranac.canary.util.PushUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.fragments.MembershipUpsellFragment.UpsellType.MODE_UPSELL_TYPE_HOME_SETTINGS;
import static is.yranac.canary.fragments.MembershipUpsellFragment.UpsellType.MODE_UPSELL_TYPE_NIGHT;
import static is.yranac.canary.fragments.MembershipUpsellFragment.UpsellType.MODE_UPSELL_TYPE_NIGHT_SETTINGS;
import static is.yranac.canary.fragments.setup.GetHelpFragment.GetHelpType.GET_HELP_MASKING;

public class SettingsFragmentStackActivity extends BaseBTLEActivity {
    public static final String LOG_TAG = "SettingsFragmentStackActivity";
    public static final String extra_locationId = "extra_locationId";
    public static final String canary_help = "canary_help";
    public static final String extra_homehealth_settings = "extra_homehealth_settings";
    public static final String extra_masking_tutorial = "extra_masking_tutorial";
    public static final String location_plan = "location_plan";
    public static final String preview_over = "preview_over";
    public static final String modal = "modal";
    public static final String get_masking_help = "get_masking_help";
    public static final String edit_profile = "edit_profile";
    public static final String about_canary = "about_canary";
    public static final String location_details = "location_details";
    public static final String manage_devices = "manage_devices";
    public static final String manage_members = "manage_members";
    public static final String location_emergency_contact = "location_emergency_contact";
    public static final String add_a_location = "add_a_location";
    public static final String invite_members = "invite_members";
    public static final String single_entry_upsell = "single_entry_upsell";
    public static final String home_mode_settings_upsell = "home_mode_settings_upsell";
    public static final String night_mode_settings_upsell = "night_mode_settings_upsell";
    public static final String night_mode_upsell = "night_mode_upsell";
    public static final String retry_setup = "retry_setup";
    public static final String night_mode_settings = "night_mode_settings";
    public static final String home_mode_settings = "home_mode_settings";
    public static final String flex_connectivity = "flex_connectivity";
    public static final String presence_notifications = "presence_notifications";

    private View header;
    private TextView titleTextView;


    public static final String LOCATION_ID = "locationIdExtra";
    public static final String DEVICE_URI_EXTRA = "deviceUriExtra";
    public static final String DEVICE = "DEVICE";

    private boolean backButtonDisabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setup_fragment_stack);

        header = findViewById(R.id.header);
        titleTextView = (TextView) header.findViewById(R.id.header_title_text_view);
        rightButton = (Button) header.findViewById(R.id.header_view_right_button);
        leftButton = (Button) header.findViewById(R.id.header_view_left_button);
        showRightButton(0);
        showLogoutButton(false);


        if (getIntent().getAction() != null) {


            int locationId = getIntent().getIntExtra(extra_locationId, 0);

            if (locationId == 0)
                locationId = UserUtils.getLastViewedLocationId();

            switch (getIntent().getAction()) {
                case "locale": {
                    CountryCodeSelectFragment countryCodeSelectFragment = CountryCodeSelectFragment.newInstance(null, CountryCodeSelectFragment.LIST_TYPE.LOCALE, false, false);
                    addFragmentAndResetStack(countryCodeSelectFragment, Utils.NO_ANIMATION);
                    break;
                }
                case extra_masking_tutorial:
                    MaskingTutorialStartFragment maskingTutorialStartFragment = MaskingTutorialStartFragment.newInstance(UserUtils.getLastViewedLocationId());
                    addFragmentAndResetStack(maskingTutorialStartFragment, Utils.NO_ANIMATION);
                    break;
                case edit_profile:
                    addFragmentAndResetStack(new AccountFragment(), Utils.NO_ANIMATION);
                    break;
                case night_mode_settings:
                    ConfigureModeSettingsFragment nightModeSettingsFragment = ConfigureModeSettingsFragment.newInstance(locationId, false);
                    addFragmentAndResetStack(nightModeSettingsFragment, Utils.NO_ANIMATION);
                    break;
                case home_mode_settings:
                    ConfigureModeSettingsFragment homeModeSettingsFragment = ConfigureModeSettingsFragment.newInstance(locationId, true);
                    addFragmentAndResetStack(homeModeSettingsFragment, Utils.NO_ANIMATION);
                    break;
                case "data_opt_in":
                    DataOptinFragment dataOptinFragment = new DataOptinFragment();
                    header.setVisibility(View.GONE);
                    addFragmentAndResetStack(dataOptinFragment, Utils.SLIDE_FROM_RIGHT);
                    break;
                case location_plan:
                    addFragmentAndResetStack(LocationOverviewFragment.newInstance(locationId), Utils.NO_ANIMATION);
                    break;
                case preview_over:
                    addFragmentAndResetStack(MemberPreviewOverFragment.newInstance(locationId), Utils.NO_ANIMATION);
                    break;
                case canary_help:
                    addFragmentAndResetStack(new GetHelpFragment(), Utils.NO_ANIMATION);
                    break;
                case about_canary:
                    addFragmentAndResetStack(new AboutCanaryFragment(), Utils.NO_ANIMATION);
                    break;
                case location_details:
                    addFragmentAndResetStack(LocationOverviewFragment.newInstance(locationId), Utils.NO_ANIMATION);
                    break;
                case manage_devices:
                    addFragmentAndResetStack(ManageDevicesFragment.newInstance(locationId), Utils.NO_ANIMATION);
                    break;
                case manage_members:
                    addFragmentAndResetStack(ManageMembersFragment.newInstance(locationId), Utils.NO_ANIMATION);
                    break;
                case location_emergency_contact:
                    addFragmentAndResetStack(LocationEmergencyContactFragment.newInstance(locationId), Utils.NO_ANIMATION);
                    break;
                case add_a_location:
                    addFragmentAndResetStack(SelectNewDeviceLocationFragment.newInstance(-1), Utils.NO_ANIMATION);
                    break;
                case invite_members:
                    ManageMembersFragment manageMembersFragment = ManageMembersFragment.newInstance(locationId);
                    addFragmentAndResetStack(manageMembersFragment, Utils.NO_ANIMATION);
                    Utils.addModalFragmentToStack(getSupportFragmentManager(), AddAMemberFragment.newInstance(locationId), "AddAMemberFragment", false);
                    break;
                case single_entry_upsell:
                    SingleEntryUpsellFragment singleEntryUpsellFragment = SingleEntryUpsellFragment.newInstance(locationId);
                    addFragmentAndResetStack(singleEntryUpsellFragment, Utils.NO_ANIMATION);
                    break;
                case home_mode_settings_upsell:
                    MembershipUpsellFragment membershipUpsellFragment = MembershipUpsellFragment.newInstance(MODE_UPSELL_TYPE_HOME_SETTINGS, locationId);
                    addFragmentAndResetStack(membershipUpsellFragment, Utils.NO_ANIMATION);
                    header.setVisibility(View.GONE);
                    break;
                case night_mode_settings_upsell:
                    MembershipUpsellFragment membershipUpsellFragment1 = MembershipUpsellFragment.newInstance(MODE_UPSELL_TYPE_NIGHT_SETTINGS, locationId);
                    addFragmentAndResetStack(membershipUpsellFragment1, Utils.NO_ANIMATION);
                    header.setVisibility(View.GONE);
                    break;
                case night_mode_upsell:
                    MembershipUpsellFragment membershipUpsellFragment2 = MembershipUpsellFragment.newInstance(MODE_UPSELL_TYPE_NIGHT, locationId);
                    addFragmentAndResetStack(membershipUpsellFragment2, Utils.NO_ANIMATION);
                    header.setVisibility(View.GONE);
                    break;
                case retry_setup:
                    String deviceId = getIntent().getStringExtra(DEVICE_URI_EXTRA);
                    if (TextUtils.isEmpty(deviceId)) {
                        finish();
                        return;
                    }
                    header.setVisibility(View.GONE);
                    FailedDeviceOTAFragment failedDeviceOTAFragmnet = FailedDeviceOTAFragment.newInstance(deviceId);
                    addFragmentAndResetStack(failedDeviceOTAFragmnet, Utils.NO_ANIMATION);
                    break;
                case flex_connectivity:
                    String device = getIntent().getStringExtra(DEVICE);
                    if (TextUtils.isEmpty(device)) {
                        finish();
                        return;
                    }
                    FlexConnectivityInfoFragment flexConnectivityInfoFragment = FlexConnectivityInfoFragment.newIntance(device);
                    addFragmentAndResetStack(flexConnectivityInfoFragment, Utils.NO_ANIMATION);
                    break;
                case presence_notifications:
                    addFragmentAndResetStack(PresenceNotificationFragment.newInstance(locationId), Utils.NO_ANIMATION);
                    break;

            }
        } else if (getIntent().hasExtra("mode_settings")) {
            int locationId = UserUtils.getLastViewedLocationId();
            addFragmentAndResetStack(ModeSettingsFragment.newInstance(locationId), Utils.NO_ANIMATION);
        } else if (getIntent().hasExtra(get_masking_help)) {
            is.yranac.canary.fragments.setup.GetHelpFragment f =
                    is.yranac.canary.fragments.setup.GetHelpFragment.newInstance(GET_HELP_MASKING);
            addFragmentAndResetStack(f, Utils.NO_ANIMATION);
        } else if (getIntent().hasExtra(extra_homehealth_settings)) {
            int locationId = getIntent().getIntExtra(extra_homehealth_settings, -1);
            if (locationId < 0)
                return;
            HomeHealthNotificationSettingsFragment f = HomeHealthNotificationSettingsFragment.newInstance(locationId);
            addFragmentAndResetStack(f, Utils.SLIDE_FROM_RIGHT);
        }

        showRightButton(0);
    }

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();
        BluetoothSingleton.reset();
    }

    @Override
    public void enableRightButton(final Fragment fragment, boolean enable) {
        if (enable) {
            rightButton.setEnabled(true);
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment != null)
                        ((StackFragment) fragment).onRightButtonClick();
                }
            });
        } else {
            rightButton.setEnabled(false);
            rightButton.setOnClickListener(null);
        }
    }

    @Override
    public void showHeader(boolean show) {
        if (show) {
            header.setVisibility(View.VISIBLE);
        } else {
            header.setVisibility(View.GONE);
        }
    }

    @Override
    public void setHeaderTitle(String title) {
        titleTextView.setText(title);
    }

    @Override
    public void setHeaderTitle(int titleId) {
        titleTextView.setText(titleId);
    }


    //TODO - Refactor this and SetupFragmentStackActivity to not duplicate so much code
    @Override
    public void showLogoutButton(boolean show) {
        if (show && DeviceDatabaseService.getAllActivatedDevices().size() > 0) {
            show = false;
        }
        if (!show) {

            if (leftButton.getVisibility() != View.VISIBLE)
                return;
            leftButton.setVisibility(View.INVISIBLE);
            leftButton.setOnClickListener(null);
            if (rightButton.getVisibility() == View.INVISIBLE) {
                rightButton.setVisibility(View.GONE);
                leftButton.setVisibility(View.GONE);
            }
            leftButton.setOnClickListener(null);

        } else {

            leftButton.setVisibility(View.VISIBLE);
            leftButton.setText(R.string.logout);
            if (rightButton.getVisibility() == View.GONE) {
                rightButton.setVisibility(View.INVISIBLE);
                rightButton.setText(R.string.logout);
            }
            leftButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LogoutUtil.logout(SettingsFragmentStackActivity.this);
                        }
                    }
            );
        }
    }


    @Override
    public void showRightButton(int string) {
        if (string != 0) {
            rightButton.setVisibility(View.VISIBLE);
            rightButton.setText(string);
            if (leftButton.getVisibility() == View.GONE) {
                leftButton.setVisibility(View.INVISIBLE);
                leftButton.setText(string);
            }
        } else {

            rightButton.setVisibility(View.INVISIBLE);
            if (leftButton.getVisibility() != View.VISIBLE) {
                leftButton.setVisibility(View.GONE);
                rightButton.setVisibility(View.GONE);
            } else {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) leftButton.getLayoutParams();
                layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                leftButton.setLayoutParams(layoutParams);
                leftButton.requestLayout();
            }
        }
    }

    /**
     * Pop the user back to the selected wifi screen
     */
    @Override
    public void popBackToWifi() {
        popBackStack(SetCanaryConnectionTypeFragment.class);
    }

    @Override
    public void disableBackButton() {
        backButtonDisabled = true;
    }

    @Override
    public void enableBackButton() {
        backButtonDisabled = false;
    }

    @Override
    public void resetSetup(String location) {
        AddADeviceFragment fragment = AddADeviceFragment.newInstance(location);
        addFragmentAndResetStack(fragment, Utils.FADE_IN_DISAPPEAR);
        enableBackButton();
    }

    @Override
    public void rightButtonTextColor(int color) {
        rightButton.setTextColor(ContextCompat.getColor(this, color));
    }

    @Override
    public void rightButtonBackground(int white) {
        rightButton.setBackground(ContextCompat.getDrawable(this, white));

    }

    @Override
    public void rightButtonBackgroundColor(int color) {
        rightButton.setBackgroundColor(color);

    }

    @Override
    public void popBackToWifiAndHelpMessage() {
        popBackToWifi();


    }


    @Override
    public void showHelpButton() {
        showRightButton(R.string.question_mark);
        rightButtonBackground(R.drawable.white_circle_gray_ring);
        setButtonsWidth(DensityUtil.dip2px(this, 25));
        setButtonsHeight(DensityUtil.dip2px(this, 25));
        rightButtonTextColor(R.color.dark_gray);
    }


    @Override
    public void onBackPressed() {
        if (backButtonDisabled) {
            TinyMessageBus.post(new OnBackBlocked());
            return;
        }

        super.onBackPressed();

    }

    @Subscribe
    public void onPushReceived(PushReceived pushReceived) {
        PushUtils.showPush(this, pushReceived);

    }

    @Override
    public void finish() {
        super.finish();
        if (getIntent().getBooleanExtra(modal, false)) {
            overridePendingTransition(R.anim.scale_alpha_pop, R.anim.slide_out_bottom);
        } else {
            overridePendingTransition(R.anim.scale_alpha_pop, R.anim.slide_out_right);
        }
    }

    @Override
    protected void onDestroy() {
        BluetoothSingleton.reset();
        super.onDestroy();
    }
}
