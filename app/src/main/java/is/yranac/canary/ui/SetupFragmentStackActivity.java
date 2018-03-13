package is.yranac.canary.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.fragments.StackFragment;
import is.yranac.canary.fragments.setup.AddADeviceFragment;
import is.yranac.canary.fragments.setup.GetHelpFragment;
import is.yranac.canary.fragments.setup.LocationPrimerFragment;
import is.yranac.canary.fragments.setup.OTAFragment;
import is.yranac.canary.fragments.setup.SelectNewDeviceLocationFragment;
import is.yranac.canary.fragments.setup.SetAddressFragment;
import is.yranac.canary.fragments.setup.SplashScreenFragment;
import is.yranac.canary.messages.OnBackBlocked;
import is.yranac.canary.messages.PushReceived;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.LocationUtil;
import is.yranac.canary.util.LogoutUtil;
import is.yranac.canary.util.PushUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.keystore.KeyStoreHelper;

public class SetupFragmentStackActivity extends BaseBTLEActivity implements OnBackStackChangedListener {
    private static final String LOG_TAG = "SetupFragmentStackActivity";

    private View header;
    private TextView titleTextView;
    public static final String LOCATION_ID = "locationIdExtra";
    public static final String NEW_SETUP = "NEW_SETUP";

    private boolean backButtonDisabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setup_fragment_stack);

        header = findViewById(R.id.header);
        titleTextView = header.findViewById(R.id.header_title_text_view);
        rightButton = header.findViewById(R.id.header_view_right_button);
        leftButton = header.findViewById(R.id.header_view_left_button);


        showRightButton(0);
        showLogoutButton(false);


        if (!KeyStoreHelper.hasGoodOauthToken()) {
            addFragmentAndResetStack(new SplashScreenFragment(), Utils.NO_ANIMATION);
            return;
        }


        String action = getIntent().getAction();
        if (action != null) {
            switch (action) {
                case NEW_SETUP:
                    if (LocationUtil.doesNotHaveLocationPermission(getBaseContext())) {
                        addFragmentAndResetStack(new LocationPrimerFragment(), Utils.SLIDE_FROM_RIGHT);
                    } else {
                        addFragmentAndResetStack(SetAddressFragment.newInstance(new Bundle(), true), Utils.SLIDE_FROM_RIGHT);
                    }
                    break;
            }
            return;

        }

        if (getIntent().hasExtra("deviceUriExtra")) {
            String deviceUri = getIntent().getStringExtra("deviceUriExtra");
            OTAFragment otaFragment = OTAFragment.newInstance(deviceUri);
            addFragmentAndResetStack(otaFragment, Utils.NO_ANIMATION);
            return;
        }

        addFragmentAndResetStack(SelectNewDeviceLocationFragment.newInstance(UserUtils.getLastViewedLocationId()), Utils.NO_ANIMATION);


    }

    @Override
    protected String getAnalyticsTag() {
        return null;
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

    //TODO - Refactor this and SettingsFragmentStackActivity to not duplicate so much code
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
                            LogoutUtil.logout(SetupFragmentStackActivity.this);
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

    @Override
    public void enableRightButton(final Fragment fragment, boolean enable) {
        if (enable) {
            rightButton.setEnabled(true);
            rightButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (fragment != null)
                                ((StackFragment) fragment).onRightButtonClick();
                        }
                    }
            );
        } else {
            rightButton.setEnabled(false);
            rightButton.setOnClickListener(null);
        }
    }

    @Override
    public void onBackStackChanged() {
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
    public void rightButtonTextColor(int white) {
        rightButton.setTextColor(
                ContextCompat.getColor(this, white));
    }


    @Override
    public void rightButtonBackground(int white) {
        rightButton.setBackground(
                ContextCompat.getDrawable(this, white));

    }

    @Override
    public void rightButtonBackgroundColor(int color) {
        rightButton.setBackgroundColor(color);

    }

    @Override
    public void popBackToWifiAndHelpMessage() {
        popBackToWifi();

        String unableToConnect = getString(R.string.cannot_connect_canary);
        String unableToConnectDsc = getString(R.string.cannot_hear_canary_dsc);
        String viewHelp = getString(R.string.get_help);
        String okay = getString(R.string.okay);

        AlertDialog alertDialog = AlertUtils.showGenericAlert(this, unableToConnect, unableToConnectDsc, 0, viewHelp, okay, 0, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.addModalFragmentToStack(getSupportFragmentManager(), GetHelpFragment.newInstance(GetHelpFragment.GetHelpType.GET_HELP_TYPE_AUDIO),
                        GetHelpFragment.class.getSimpleName());

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        if (alertDialog != null) {
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
        }
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


    @Subscribe(mode = de.halfbit.tinybus.Subscribe.Mode.Main)
    public void onPushReceived(PushReceived pushReceived) {
        PushUtils.showPush(this, pushReceived);

    }

    /**
     * Pop the user back to the selected wifi screen
     */
    @Override
    public void popBackToWifi() {

        enableBackButton();
        getSupportFragmentManager().popBackStack("SetCanaryConnectionTypeFragment", 0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}