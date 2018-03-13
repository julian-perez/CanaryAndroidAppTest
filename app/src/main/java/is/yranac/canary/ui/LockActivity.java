package is.yranac.canary.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.security.KeyStore;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.messages.LocationTableUpdated;
import is.yranac.canary.messages.OpenLockScreenEntry;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.intent.GlobalAPIntentServiceManager;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.LogoutUtil;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.keystore.KeyStoreHelper;

import static is.yranac.canary.ui.SettingsFragmentStackActivity.LOCATION_ID;

/**
 * Created by Schroeder on 2/18/15.
 */
public class LockActivity extends BaseActivity implements TextWatcher {

    private static final String LOG_TAG = "LockActivity";
    public static final String FROM_LAUNCH = "from_launch";
    private View passCodeCharOne;
    private View passCodeCharTwo;
    private View passCodeCharThree;
    private View passCodeCharFour;
    private EditText passCodeEditText;
    private View passCodeLayout;

    private boolean locationTableUpdated = false;
    private boolean validPasscode = false;
    private boolean fromLaunch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }
        setContentView(R.layout.lock_activity);

        Log.i(LOG_TAG, "LAUNCHED LOCK");
        passCodeCharOne = findViewById(R.id.passcode_char_one);
        passCodeCharTwo = findViewById(R.id.passcode_char_two);
        passCodeCharThree = findViewById(R.id.passcode_char_three);
        passCodeCharFour = findViewById(R.id.passcode_char_four);
        passCodeEditText = (EditText) findViewById(R.id.passcode_edit_text);
        passCodeEditText.addTextChangedListener(this);
        passCodeLayout = findViewById(R.id.passcode_layout);
        fromLaunch = getIntent().getBooleanExtra(FROM_LAUNCH, false);
        PreferencesUtils.setShowingPassCode(true);

        disableInAppNotifications();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            if (fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints() ) {
                Log.i(LOG_TAG, "finger print start");
                fingerprintManager.authenticate(null, null, 0, new AuthenticationCallback(), null);
            }
        }
    }


    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        passCodeEditText.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(passCodeEditText, 0);

        if (KeyStoreHelper.hasGoodOauthToken()) {

            checkForData();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void checkForData() {
        if (hasInternetConnection() && fromLaunch) {
            locationTableUpdated = LocationDatabaseService.getLocationList().size() > 0 && DeviceDatabaseService.getAllActivatedDevices().size() > 0;
        } else {
            locationTableUpdated = true;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        switch (s.length()) {
            case 4:
                passCodeCharFour.setVisibility(View.VISIBLE);
            case 3:
                passCodeCharThree.setVisibility(View.VISIBLE);
            case 2:
                passCodeCharTwo.setVisibility(View.VISIBLE);
            case 1:
                passCodeCharOne.setVisibility(View.VISIBLE);
                break;
            default:
                passCodeCharFour.setVisibility(View.INVISIBLE);
                passCodeCharThree.setVisibility(View.INVISIBLE);
                passCodeCharTwo.setVisibility(View.INVISIBLE);
                passCodeCharOne.setVisibility(View.INVISIBLE);
                break;

        }

        if (count <= 0) {
            switch (s.length()) {
                case 1:
                    passCodeCharTwo.setVisibility(View.INVISIBLE);
                case 2:
                    passCodeCharThree.setVisibility(View.INVISIBLE);
                case 3:
                    passCodeCharFour.setVisibility(View.INVISIBLE);
                    break;
            }
        }


    }


    @Subscribe
    public void onOpenLockScreenEntry(OpenLockScreenEntry openLockScreenEntry) {
        fromLaunch = true;
        getIntent().putExtra("entryId", openLockScreenEntry.entryId);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() == 4) {

            String passCode = s.toString();
            String myPassCode = PreferencesUtils.getPassCode();
            if (passCode.equals(myPassCode)) {

                PreferencesUtils.setShowingPassCode(false);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        passCodeEditText
                                .getWindowToken(), 0);
                if (!fromLaunch) {


                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    return;
                }
                if (locationTableUpdated)
                    checkForNxtActivity();
                validPasscode = true;
            } else {

                shakeView();

            }
        }

    }

    private int shakeCount = 0;

    private void shakeView() {
        shakeCount++;

        if (shakeCount >= 5) {
            LogoutUtil.continueLogout(this);
            return;
        }

        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                passCodeEditText.setText("");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        passCodeLayout.startAnimation(shake);

    }


    private void startMainActivityOrSetup() {

        List<Device> deviceList1 = DeviceDatabaseService.getAllDevices();
        Device device = null;

        if (deviceList1.size() == 1) {
            device = deviceList1.get(0);
        }

        if (deviceList1.size() == 0) {
            showAccountSetup();
        } else if (device != null && !device.deviceActivated) {
            showOTAScreen(device.resourceUri);
        } else {
            showMainActivity();
        }
    }

    private void showOTAScreen(String deviceUri) {
        Intent i = new Intent(this, SetupFragmentStackActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("deviceUriExtra", deviceUri);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void showAccountSetup() {
        Intent i = new Intent(this, SetupFragmentStackActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra(LOCATION_ID, UserUtils.getLastViewedLocationId());
        startActivity(i);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Subscribe
    public void onLocationTableUpdated(LocationTableUpdated message) {
        ;
        if (validPasscode)
            checkForNxtActivity();

        locationTableUpdated = true;
    }

    private void checkForNxtActivity() {


        List<Location> locations = LocationDatabaseService.getLocationList();
        if (locations.size() == 0) {
            showAccountSetup();
        } else {
            startMainActivityOrSetup();

            long entryId = getIntent().getLongExtra("entryId", 0);

            if (entryId != 0) {
                showEntry(entryId);
            }
        }

    }

    private void showEntry(long entryId) {
        Intent intent = new Intent(this, EntryDetailActivity.class);
        intent.putExtra("entryId", entryId);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LockActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void showMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void finish() {
        PreferencesUtils.setShowingPassCode(false);
        super.finish();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private class AuthenticationCallback extends FingerprintManager.AuthenticationCallback {

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            Log.i(LOG_TAG, "onAuthenticationError : " + errString);
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            Log.i(LOG_TAG, "onAuthenticationHelp : " + helpString);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            Log.i(LOG_TAG, "onAuthenticationSucceeded");
            startMainActivityOrSetup();
        }

        @Override
        public void onAuthenticationFailed() {
            Log.i(LOG_TAG, "onAuthenticationFailed");
        }
    }
}
