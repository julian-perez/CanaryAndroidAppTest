package is.yranac.canary.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import java.util.Locale;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.fragments.HomeHealthDataFragment;
import is.yranac.canary.messages.BlockViewPagerDrag;
import is.yranac.canary.messages.InvalidCert;
import is.yranac.canary.messages.LoggedOut;
import is.yranac.canary.messages.OnBackPressed;
import is.yranac.canary.messages.ResumeFragments;
import is.yranac.canary.messages.ShowModalFragment;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.services.database.CustomerDatabaseService;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.intent.GlobalAPIntentServiceManager;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.AnimationHelper;
import is.yranac.canary.util.BaseExportUtil;
import is.yranac.canary.util.GlobalSirenTimeout;
import is.yranac.canary.util.LocaleHelper;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.SirenUtil;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.ui.SettingsFragmentStackActivity.DEVICE_URI_EXTRA;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.retry_setup;

/**
 * Base Activity that contains shared functionality that is use across all activities
 */
public abstract class BaseActivity extends FragmentActivity implements BaseExportUtil.ExportInterface {

    protected final String LOG_TAG = getClass().getSimpleName();

    public static final String key_device = "key_device";
    public static final String key_thumbnail = "key_device_thumbnail";
    public static final String key_thumbnail_id = "key_thumbnail_id";
    public static final String key_masks = "key_device_masks";

    private View offlineModeView;
    private View sirenView;
    private boolean paused = true;
    private SirenUtil sirenUtil;
    private View offlineBar;
    private AlertDialog alert;
    private CountDownTimer timer;

    /**
     * Return the Screen tag for google Analytics
     *
     * @return Null if no tagging otherwise the corresponding tag
     */
    protected abstract String getAnalyticsTag();


    protected void addTouchInterceptor() {
        timer = new CountDownTimer(120000, 60000) {
            public void onTick(long millisUntilFinished) {
                //Some code
                //
            }

            public void onFinish() {
                if (isPaused())
                    return;
                Intent i = new Intent(getBaseContext(), LaunchActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        };
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        View child = getLayoutInflater().inflate(layoutResID, null);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.main_content_view);
        frameLayout.addView(child);
        setupViews();

    }

    @Override
    public void setContentView(View view) {
        View parent = getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = (FrameLayout) parent.findViewById(R.id.main_content_view);
        frameLayout.addView(view);

        super.setContentView(parent);
        setupViews();
    }


    private void setupViews() {
        offlineModeView = findViewById(R.id.offine_view);
        sirenView = findViewById(R.id.base_bottom_btns_layout);

        offlineModeView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        offlineBar = findViewById(R.id.offline_bar);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.setLocale(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Utils.isDemo()) {
            addTouchInterceptor();
        }

        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
                finish();
            }
        }
    }


    private AlertDialog invalidCertDialog;

    @Subscribe
    public void onInvalidCert(InvalidCert invalidCert) {
        if ((invalidCertDialog != null && invalidCertDialog.isShowing()) || !PreferencesUtils.trustsConnection())
            return;

        String title = getString(R.string.untrusted);
        String dsc = getString(R.string.untrusted_dsc);
        String left = getString(R.string.allow_untrusted);
        String right = getString(R.string.okay);

        invalidCertDialog = AlertUtils.showGenericAlert(this, title, dsc, 0, left, right, 0, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invalidCertDialog = null;
                PreferencesUtils.setTrustsConnection(false);
            }
        }, null);
    }

    @Override
    protected void onStart() {
        super.onStart();


        TinyMessageBus.register(this);


        // Set screen name.
        // Where path is a String representing the screen name.
        String path = getAnalyticsTag();
        if (path == null)
            return;

        GoogleAnalyticsHelper.trackScreenEvent(path);

        if (Utils.hasInternetConnection(getBaseContext())) {
            offlineModeView.setVisibility(View.GONE);
            offlineBar.setVisibility(View.GONE);
        } else {
            offlineModeView.setVisibility(View.VISIBLE);
            offlineBar.setVisibility(View.VISIBLE);
        }
        offlineModeView.clearAnimation();
        offlineBar.clearAnimation();
    }

    public void showHomehealthFragment(String deviceUuid) {

        Device device = DeviceDatabaseService.getDeviceFromResourceUri(deviceUuid);
        if (device == null)
            return;

        HomeHealthDataFragment homeHealthDataFragment = HomeHealthDataFragment.newInstance(device.resourceUri, device.name);
        showFragement(homeHealthDataFragment);
    }


    @Subscribe
    public void onShowModalFragment(ShowModalFragment message) {

        TinyMessageBus
                .post(new BlockViewPagerDrag(true));

        showFragement(message.fragment);
    }

    protected void showFragement(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.none, R.anim.none, R.anim.slide_out_bottom)
                .add(R.id.main_content_view, fragment, null)
                .addToBackStack(null)
                .commit();
    }

    private void checkForSiren() {
        if (GlobalSirenTimeout.getInstance().isSirenCountingDown()) {
            if (!hasSirenFooter()) {
                AnimationHelper.fadeFromAlphaToAlpha(sirenView, 0.0f, 1.0f, 250);
                sirenUtil = new SirenUtil(this, sirenView, 0, true);
                sirenUtil.showSirenView();

                View emergencyCallBtn = findViewById(R.id.emergency_call_btn);
                emergencyCallBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!hasInternetConnection()) {
                            return;
                        }
                        AlertUtils.showPhoneNumberAlertDialog(BaseActivity.this, "Other");
                    }
                });

                View soundSirenBtn = findViewById(R.id.sound_siren_btn);
                soundSirenBtn.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (sirenUtil != null) {
                                    sirenUtil.dismissSirenFromScreen("Other");
                                }
                            }
                        });
            }
        } else {
            sirenView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!LocaleHelper.getLocale(getResources()).getLanguage().equals(LocaleHelper.getLanguage(this))) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        TinyMessageBus.register(this);

        checkForLock();
        checkForSiren();

        if (Utils.isDemo()) {
            if (!(this instanceof LaunchActivity))
                timer.start();
        }
        setPaused(false);
        if (!Utils.isDemo()) {

            IntentFilter internetFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(mNetworkChangedReceiver, internetFilter);
            IntentFilter filter2 = new IntentFilter(DeviceDatabaseService.DEVICE_UPDATED_FAILED);
            registerReceiver(mDeviceUpdateFailed, filter2);
            IntentFilter localeFilter = new IntentFilter(CustomerDatabaseService.LOCALE_CHANGED);
            registerReceiver(mLocaleUpdate, localeFilter);

        }
        final int rotationState = android.provider.Settings.System.getInt(
                getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0
        );

        if (rotationState == 0) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            try {
                ActivityInfo ai = getPackageManager()
                        .getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
                setRequestedOrientation(ai.screenOrientation);
            } catch (PackageManager.NameNotFoundException ignored) {

            }
        }
    }

    private void checkForLock() {
        boolean startLock = getIntent().getBooleanExtra("start_lock", false);
        Log.i(LOG_TAG, "startLock " + String.valueOf(startLock));
        if (startLock) {
            getIntent().putExtra("start_lock", this instanceof LaunchActivity);
            Intent intent = new Intent(this, LockActivity.class);
            intent.putExtra("entryId", getIntent().getIntExtra("entryId", 0));
            intent.putExtra("from_launch", getIntent().getBooleanExtra("from_launch", true));
            startActivity(intent);

        }
    }

    public void unregisterForOTAFailedReceive() {
        try {
            unregisterReceiver(mDeviceUpdateFailed);
        } catch (Exception ignore) {

        }
    }

    public void registerForOTBFailedReceive() {
        try {
            unregisterReceiver(mDeviceUpdateFailed);
        } catch (Exception ignore) {

        }
        IntentFilter filter2 = new IntentFilter(DeviceDatabaseService.DEVICE_UPDATED_FAILED);
        registerReceiver(mDeviceUpdateFailed, filter2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setPaused(true);
        if (Utils.isDemo()) {
            if (!(this instanceof LaunchActivity))
                timer.cancel();
        }

        TinyMessageBus.unregister(this);

        try {
            if (!Utils.isDemo()) {
                unregisterReceivers();
            }
        } catch (Exception ignore) {

        }

        if (sirenUtil != null) {
            sirenUtil.removeSirenListener();
        }

    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        if (Utils.isDemo()) {
            timer.cancel();
            timer.start();
        }
    }

    public boolean hasSirenFooter() {
        return false;
    }

    private void setPaused(boolean b) {
        this.paused = b;
    }

    public boolean isPaused() {
        return paused;
    }

    @Override
    protected void onStop() {
        if (sirenUtil != null) {
            sirenUtil.removeSirenListener();
        }

        if (Utils.hasInternetConnection(getBaseContext())) {
            offlineModeView.setVisibility(View.GONE);
            offlineBar.setVisibility(View.GONE);
        } else {
            offlineModeView.setVisibility(View.VISIBLE);
            offlineBar.setVisibility(View.VISIBLE);
        }

        offlineModeView.clearAnimation();
        offlineBar.clearAnimation();

        handler.removeCallbacks(animationDelayRunnable);
        TinyMessageBus.unregister(this);
        super.onStop();
    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();


        TinyMessageBus.post(new ResumeFragments());

    }

    public boolean hasInternetConnection() {
        boolean internet = Utils.hasInternetConnection(getBaseContext());
        if (!internet)
            bounceNoInternetView();

        return internet;
    }


    private void showNoInternetView() {
        offlineModeView.clearAnimation();
        offlineModeView.setVisibility(View.VISIBLE);

        Animation translateAnimation = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.slide_in_top);
        translateAnimation.setDuration(200);
        translateAnimation.setFillAfter(true);

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                offlineModeView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                retractNoInternetView();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        offlineModeView.setAnimation(translateAnimation);
        offlineModeView.startAnimation(translateAnimation);
    }

    private void hideNoInternetView() {

        offlineBar.clearAnimation();
        Animation translateAnimation = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.slide_out_top);
        translateAnimation.setDuration(200);
        translateAnimation.setFillAfter(false);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                offlineModeView.setVisibility(View.GONE);
                offlineBar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        offlineBar.setAnimation(translateAnimation);
        offlineBar.startAnimation(translateAnimation);
    }

    private void bounceNoInternetView() {
        offlineModeView.clearAnimation();
        offlineModeView.setVisibility(View.VISIBLE);
        Animation translateAnimation = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.slide_in_top);
        translateAnimation.setDuration(200);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                retractNoInternetView();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        offlineModeView.setAnimation(translateAnimation);
        offlineModeView.startAnimation(translateAnimation);
    }

    private Handler handler = new Handler();
    private Runnable animationDelayRunnable = new Runnable() {
        @Override
        public void run() {

            offlineModeView.clearAnimation();
            Animation translateAnimation = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.slide_out_top);
            translateAnimation.setDuration(200);
            translateAnimation.setFillAfter(false);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    offlineModeView.setVisibility(View.GONE);
                    offlineBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            offlineModeView.setAnimation(translateAnimation);
            offlineModeView.startAnimation(translateAnimation);

        }
    };

    private void retractNoInternetView() {
        handler.removeCallbacks(animationDelayRunnable);
        handler.postDelayed(animationDelayRunnable, 2500);

    }

    protected final BroadcastReceiver mNetworkChangedReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            if (Utils.hasInternetConnection(getBaseContext()) && offlineBar.getVisibility() == View.VISIBLE) {
                hideNoInternetView();
                GlobalAPIntentServiceManager.resetAlarms(getApplicationContext());
            } else if (!Utils.hasInternetConnection(getBaseContext())) {
                showNoInternetView();
            }
        }
    };

    protected final BroadcastReceiver mLocaleUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if (alert != null)
                alert.dismiss();


            Locale newLocale = Locale.getDefault();
            Locale currentLocale = Locale.getDefault();

            alert = AlertUtils.showGenericAlert(BaseActivity.this, getString(R.string.language_updated), getString(R.string.your_language_will_update, newLocale.getDisplayLanguage(currentLocale)), 0, getString(R.string.update), null, getResources().getColor(R.color.light_red), 0, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = LocaleHelper.setLocale(getBaseContext());
                    Intent i = new Intent(context, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }, null);
            alert.setCancelable(false);
        }
    };

    protected final BroadcastReceiver mDeviceUpdateFailed = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showDeviceUpdateFailedDialog(intent.getStringExtra("device_uri"));

        }
    };


    private void showDeviceUpdateFailedDialog(String deviceId) {
        Intent intent = new Intent(BaseActivity.this, SettingsFragmentStackActivity.class);
        intent.setAction(retry_setup);
        intent.putExtra(DEVICE_URI_EXTRA, deviceId);
        startActivity(intent);
    }

    private boolean pushEnabled = true;

    public void enableInAppNotifications() {
        pushEnabled = true;
    }

    public void disableInAppNotifications() {
        pushEnabled = false;

    }

    public boolean isPushEnabled() {

        return pushEnabled;
    }


    /**
     * Hides the soft keyboard if it present
     */
    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()) {
            View focusView = getCurrentFocus();
            if (focusView != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        if (alert != null && alert.isShowing()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public void setAlert(AlertDialog alert) {
        if (this.alert != null && this.alert.isShowing()) {
            this.alert.dismiss();
            this.alert = null;
        }
        this.alert = alert;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onBackPressed() {
        TinyMessageBus.post(new OnBackPressed());
        super.onBackPressed();
    }


    @Subscribe
    public void onLoggedOut(LoggedOut loggedOut) {
        if (alert != null && alert.isShowing())
            return;

        alert = AlertUtils.showLoggedOut(this);
    }

    @Override
    public void showDialog(AlertDialog dialog) {
        if (this.alert != null && this.alert.isShowing()) {
            this.alert.dismiss();
            this.alert = null;
        }

        this.alert = dialog;

        if (!isFinishing() && !isPaused()) {
            dialog.show();
        }
    }

    protected void unregisterReceivers() {
        try {
            unregisterReceiver(mNetworkChangedReceiver);
            unregisterReceiver(mDeviceUpdateFailed);
            unregisterReceiver(mLocaleUpdate);
        } catch (Exception ignore) {
        }
    }

    @Override
    public void hideMoreOptionsLayout() {

    }

    @Override
    public void handlePermissionIssue() {

    }
}

