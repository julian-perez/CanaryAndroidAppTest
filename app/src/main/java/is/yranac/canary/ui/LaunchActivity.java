package is.yranac.canary.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.messages.LocationTableUpdated;
import is.yranac.canary.messages.PurgeDatabase;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.geofence.SetUpGeofence;
import is.yranac.canary.util.MyLifecycleHandler;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.TutorialUtil;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import is.yranac.canary.util.keystore.KeyStoreHelper;

import static is.yranac.canary.ui.SettingsFragmentStackActivity.LOCATION_ID;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_APP_ENTRY_HOMSCREEN;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_APP_ENTRY;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_APP_ENTRY_BACKGROUNDED;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_APP_ENTRY_TERMINATED;

public class LaunchActivity extends BaseActivity {
    private static final String LOG_TAG = "LaunchActivity";
    public static final String ENTRY = "entry";
    public static final String ENTRY_ID = "entryId";
    private VideoView videoView;

    @Override
    protected String getAnalyticsTag() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleAnalyticsHelper.trackEvent(CATEGORY_APP_ENTRY, ACTION_APP_ENTRY_HOMSCREEN, MyLifecycleHandler.appTerminated() ? PROPERTY_APP_ENTRY_BACKGROUNDED : PROPERTY_APP_ENTRY_TERMINATED);

        if (Utils.isDemo()) {
            setContentView(R.layout.demo_intro_layout);
            videoView = (VideoView) findViewById(R.id.intro_video_view);
            videoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    loadDemo();
                    return true;
                }
            });
            return;
        }


        setContentView(R.layout.activity_launch);
        disableInAppNotifications();

    }

    private void checkForData() {
        if (!KeyStoreHelper.hasGoodOauthToken()) {

            TinyMessageBus.post(new PurgeDatabase());
            showAccountSetup();
        }

        if (Utils.isDemo())
            return;

        if (LocationDatabaseService.getLocationList().size() > 0 && DeviceDatabaseService.getAllActivatedDevices().size() > 0)
            startMainActivityOrSetup();

    }


    @Override
    public void onResume() {
        boolean startLock = getIntent().getBooleanExtra("start_lock", false);

        super.onResume();
        if (Utils.isDemo()) {

            int resourceId = getResources().getIdentifier("demo_app_resting_video", "raw",
                    getPackageName());

            String uri = "android.resource://" + getPackageName() + "/" + resourceId;
            videoView.setVideoURI(Uri.parse(uri));
            videoView.setOnPreparedListener(
                    new MediaPlayer.OnPreparedListener() {

                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(true);

                        }
                    });
            videoView.setOnErrorListener(null);
            videoView.start();
        }

        unregisterReceivers();

        if (startLock)
            return;
        if (KeyStoreHelper.hasGoodOauthToken()) {

            checkForData();

        } else {
            showAccountSetup();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoView != null)
            videoView.stopPlayback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startMainActivityOrSetup() {

        boolean startLock = getIntent().getBooleanExtra("start_lock", false);
        if (startLock)
            return;

        List<Device> deviceList1 = DeviceDatabaseService.getAllDevices();
        Device device = null;
        List<Device> deviceList2 = DeviceDatabaseService.getAllActivatedDevices();


        Customer customer = CurrentCustomer.getCurrentCustomer();
        if (customer != null) {
            GoogleAnalyticsHelper.setUserId(customer.id);
        }

        if (deviceList1.size() == 1) {
            device = deviceList1.get(0);
        }

        if (deviceList1.size() == 0 || (device == null && deviceList2.isEmpty())) {
            showAccountSetup();
        } else if (device != null && !device.deviceActivated) {
            showOTAScreen(device.resourceUri);
        } else {
            if (getIntent().getAction() != null) {
                checkAction(getIntent().getAction());
            } else {
                showMainActivity(null);
                long entryId = getIntent().getLongExtra("entryId", 0);
                if (entryId != 0) {
                    showEntry(entryId);
                }
            }
        }
    }

    private void checkAction(String action) {
        switch (action) {
            case "watchlive":
                showMainActivity(null);
                showWatchLive();
                break;

            case ENTRY:
                showMainActivity(null);
                long entryId = getIntent().getLongExtra(ENTRY_ID, 0);

                if (entryId != 0) {
                    showEntry(entryId);
                }

                break;
            case "homehealth":
            case "timeline":
            case "bookmark":
                showMainActivity(action);
                break;
            default:
                showMainActivity(null);
                break;
        }
    }

    private void showWatchLive() {
        List<Device> devices = DeviceDatabaseService.getAllActivatedDevices();
        if (devices.size() == 0)
            return;

        Intent i = new Intent(this, WatchLiveActivity.class);
        i.putExtras(getIntent());
        startActivity(i);

    }


    private void showEntry(long entryId) {
        Intent intent = new Intent(this, EntryDetailActivity.class);
        intent.putExtra("entryId", entryId);
        startActivity(intent);
        overridePendingTransition(0, 0);
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
        i.putExtra(LOCATION_ID, KeyStoreHelper.hasGoodOauthToken() ? UserUtils.getLastViewedLocationId() : 0);
        startActivity(i);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }


    @Subscribe
    public void onLocationTableUpdated(LocationTableUpdated message) {
        if (Utils.isDemo())
            return;
        boolean startLock = getIntent().getBooleanExtra("start_lock", false);
        if (startLock)
            return;
        // There are no locations so prompt the user for one
        List<Location> locations = LocationDatabaseService.getLocationList();
        if (locations.size() == 0) {
            showAccountSetup();
        } else {
            startMainActivityOrSetup();
        }
    }

    private void showMainActivity(String action) {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setAction(action);
        if (getIntent() != null && getIntent().getExtras() != null)
            i.putExtras(getIntent().getExtras());

        startActivity(i);

        if (TutorialUtil.userHasFinishedAllTutorials()) {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        SetUpGeofence.startService(getApplicationContext());
    }


    public void loadDemo() {

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
