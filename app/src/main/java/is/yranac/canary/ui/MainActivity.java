package is.yranac.canary.ui;

import android.Manifest;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.adapter.VerticalFragmentPagerAdapter;
import is.yranac.canary.fragments.DeviceFragment;
import is.yranac.canary.fragments.EntryListFragment;
import is.yranac.canary.fragments.MainLayoutFragment;
import is.yranac.canary.fragments.ModeCustomerFragment;
import is.yranac.canary.fragments.tutorials.HomeTutorialOverlayFragment;
import is.yranac.canary.messages.BlockViewPagerDrag;
import is.yranac.canary.messages.ClosePanelRequest;
import is.yranac.canary.messages.LocationTableUpdated;
import is.yranac.canary.messages.OnBackPressed;
import is.yranac.canary.messages.OpenSettings;
import is.yranac.canary.messages.PushReceived;
import is.yranac.canary.messages.ResetDashboard;
import is.yranac.canary.messages.ResetTimelineFilterRequest;
import is.yranac.canary.messages.ServicePlanUpdated;
import is.yranac.canary.messages.TutorialRequest;
import is.yranac.canary.messages.tutorial.StartHomeTutorial;
import is.yranac.canary.messages.tutorial.StartTimelineFilterTutorial;
import is.yranac.canary.messages.tutorial.StartTimelineTutorial;
import is.yranac.canary.model.CountryCode;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.geofence.SetUpGeofence;
import is.yranac.canary.ui.views.VerticalViewPager;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.LocationUtil;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.PushUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.TutorialUtil;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ZendeskUtil;
import is.yranac.canary.util.cache.location.GetLocation;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.TutorialUtil.TutorialType.ENTRY_MORE_OPTIONS;
import static is.yranac.canary.util.TutorialUtil.TutorialType.NONE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_TIMELINE_CLOSE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_TIMELINE_OPEN;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_TIMELINE;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_MAIN;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_SETTINGS;
import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_TIMELINE;

public class MainActivity extends BaseActivity implements MainLayoutFragment.CallbackMethods {
    private static final String LOG_TAG = "MainActivity";

    public static final int DEVICE_STATUS_PAGE = 0;
    public static final int TIMELINE_PAGE = 1;
    private VerticalViewPager verticalViewPager;


    // the following statics are accessed from multiple sub fragments
    public static int currentVerticalViewPage = 1;

    private static final int numberOnDaysToDelayMaskingLaunch = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        verticalViewPager = (VerticalViewPager) findViewById(R.id.vertical_view_pager);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        if (TutorialUtil.getTutorialInProgress() == NONE) {
            performPreChecks();
        }
        restoreViewPager();

        if (getIntent() == null)
            return;

        String action = getIntent().getAction();

        if (action != null) {
            Log.i(LOG_TAG, action);
            switch (action) {
                case "timeline":
                    verticalViewPager.setCurrentItem(TIMELINE_PAGE);
                    break;
                case "bookmark":
                    verticalViewPager.setCurrentItem(TIMELINE_PAGE);
                    VerticalFragmentPagerAdapter verticalFragmentPagerAdapter = (VerticalFragmentPagerAdapter) verticalViewPager.getAdapter();
                    EntryListFragment entryListFragment = (EntryListFragment) verticalFragmentPagerAdapter.getItem(TIMELINE_PAGE);
                    entryListFragment.showBookmarks();
                    break;
                case "homehealth":
                    verticalViewPager.setCurrentItem(DEVICE_STATUS_PAGE);
                    String deviceUuid = getIntent().getStringExtra("event_device_uuid");
                    if (TextUtils.isEmpty(deviceUuid))
                        return;
                    showHomehealthFragment(deviceUuid);
                    break;
                case "disconnect":
                    String url = getString(R.string.connectivity_url);
                    ZendeskUtil.loadHelpCenter(this, url);
                    break;
            }
        }
    }

    @Override
    protected String getAnalyticsTag() {
        if (verticalViewPager.getCurrentItem() == 1) {
            return SCREEN_TIMELINE;
        } else {
            VerticalFragmentPagerAdapter verticalFragmentPagerAdapter = (VerticalFragmentPagerAdapter) verticalViewPager.getAdapter();
            MainLayoutFragment mainLayoutFragment = (MainLayoutFragment) verticalFragmentPagerAdapter.getItem(0);
            if (mainLayoutFragment.isPanelExpanded()) {
                return SCREEN_MAIN;
            } else {
                return SCREEN_SETTINGS;
            }
        }
    }

    @Subscribe
    public void onPushReceived(PushReceived pushReceived) {
        PushUtils.showPush(this, pushReceived);

    }


    @Override
    protected void onResume() {
        super.onResume();
        TutorialUtil.register();
        checkLocationData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TutorialUtil.unregister();

    }

    /**
     * repeated back presses will remove modal views from the screen, close the timeline, settings, and customer views to get back to the device view
     * and then will finally back out of the app
     */
    @Override
    public void onBackPressed() {
        TinyMessageBus.post(new OnBackPressed());


        String tutorialFragmentTag = HomeTutorialOverlayFragment.class.getSimpleName();
        Fragment tutorialFragment = getSupportFragmentManager().findFragmentByTag(tutorialFragmentTag);
        if (tutorialFragment != null && tutorialFragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
            tutorialFragment.getChildFragmentManager().popBackStack();
            return;

        }


        TutorialUtil.TutorialType tutorialInProgress = TutorialUtil.getTutorialInProgress();
        boolean needToClose = tutorialInProgress != NONE && tutorialInProgress != ENTRY_MORE_OPTIONS;

        if (needToClose && getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
            return;
        }

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
                TinyMessageBus.post(new BlockViewPagerDrag(false));
            }
            return;
        }

        // If the timeline page is showing then close it and get back to the Device viewpager
        if (currentVerticalViewPage == TIMELINE_PAGE) {
            if (tutorialInProgress != TutorialUtil.TutorialType.TIMELINE_FILTER)
                verticalViewPager.setCurrentItem(0, true);
            return;
        }

        VerticalFragmentPagerAdapter verticalFragmentPagerAdapter = (VerticalFragmentPagerAdapter) verticalViewPager.getAdapter();
        MainLayoutFragment mainLayoutFragment = (MainLayoutFragment) verticalFragmentPagerAdapter.getItem(0);

        if (mainLayoutFragment != null && mainLayoutFragment.isAdded()) {
            // If the Settings menu is showing then close it and get back to the Device viewpager
            if (!mainLayoutFragment.isPanelExpanded()) {
                mainLayoutFragment.expandPanel();

                TinyMessageBus.postDelayed(new TutorialRequest(), 1000);
                return;
            }

            FragmentManager mainLayoutFragmentManager = mainLayoutFragment.getChildFragmentManager();
            if (mainLayoutFragmentManager.findFragmentByTag("CustomerFragment") != null) {
                ((ModeCustomerFragment) mainLayoutFragmentManager.findFragmentByTag(
                        "CustomerFragment")).fadeOutAndBail();
                return;
            }
        }
        super.onBackPressed();
    }


    private void restoreViewPager() {
        if (isFinishing())
            return;
        currentVerticalViewPage = 0;
        MainLayoutFragment mainLayoutFragment = new MainLayoutFragment();
        EntryListFragment entryListFragment = new EntryListFragment();
        SparseArray<Fragment> fragments = new SparseArray<>();
        fragments.put(0, mainLayoutFragment);
        fragments.put(1, entryListFragment);

        PagerAdapter verticalPagerAdapter = new VerticalFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        verticalViewPager.setAdapter(verticalPagerAdapter);
        verticalViewPager.setPageMargin(-1 * (int) getResources().getDimension(R.dimen.sliding_up_panel_height));
        verticalViewPager.setOnPageChangeListener(verticalPageChangeListener);

    }

    /**
     * ViewPager listener to generate parallax scrolling and fade effects on the current DeviceFragment when dragging
     * between the device page and timeline
     */
    public ViewPager.OnPageChangeListener verticalPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            VerticalFragmentPagerAdapter verticalFragmentPagerAdapter = (VerticalFragmentPagerAdapter) verticalViewPager.getAdapter();
            MainLayoutFragment mainLayoutFragment = (MainLayoutFragment) verticalFragmentPagerAdapter.getItem(0);
            if (mainLayoutFragment.getView() == null) {
                restoreViewPager();
                return;
            }
            if (position == 0) {
                if (positionOffset == 0f) {
                    // fully scrolled to page 0
                    mainLayoutFragment.setAlphas(0f);
                    mainLayoutFragment.refreshDeviceWatchLiveHomehealthStatus();
                    mainLayoutFragment.setSlidingEnabled(true);
                    // enable device viewpager paging when maximized
                    mainLayoutFragment.setDeviceViewPagerEnabled(true);

                    //Seeing if we need any tutorials
                    TinyMessageBus.post(new TutorialRequest());
                } else {
                    // partially scrolled
                    mainLayoutFragment.setAlphas(positionOffset);
                }
            }

        }


        @Override
        public void onPageSelected(int position) {
            currentVerticalViewPage = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

            VerticalFragmentPagerAdapter verticalFragmentPagerAdapter = (VerticalFragmentPagerAdapter) verticalViewPager.getAdapter();
            MainLayoutFragment mainLayoutFragment = (MainLayoutFragment) verticalFragmentPagerAdapter.getItem(0);
            EntryListFragment entryListFragment = (EntryListFragment) verticalFragmentPagerAdapter.getItem(1);
            if (mainLayoutFragment.getView() == null) {
                restoreViewPager();
                return;
            }
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                int position = verticalViewPager.getCurrentItem();

                if (position == 0) {
                    GoogleAnalyticsHelper.trackScreenEvent(SCREEN_MAIN);
                    mainLayoutFragment.setAlphas(0.0f);
                    mainLayoutFragment.setSlidingEnabled(true);
                    mainLayoutFragment.setDeviceViewPagerEnabled(true);
                    entryListFragment.hideMoreOptionsLayout();

                    GoogleAnalyticsHelper.trackEvent(CATEGORY_TIMELINE, ACTION_TIMELINE_CLOSE,
                            null, null, UserUtils.getLastViewedLocationId(), 0);
                } else {
                    GoogleAnalyticsHelper.trackScreenEvent(SCREEN_TIMELINE);

                    GoogleAnalyticsHelper.trackEvent(CATEGORY_TIMELINE, ACTION_TIMELINE_OPEN,
                            null, null, UserUtils.getLastViewedLocationId(), 0);
                    mainLayoutFragment.setAlphas(1.0f);
                    mainLayoutFragment.setSlidingEnabled(false);
                    mainLayoutFragment.setDeviceViewPagerEnabled(false);
                    mainLayoutFragment.removeTutorial();
                    TinyMessageBus.post(new TutorialRequest());

                }
                currentVerticalViewPage = position;
            }

        }
    };


    @Override
    public void onMainLayoutDisplayed(boolean isMainLayoutDisplayed) {
        // not in tutorial
        verticalViewPager.setExternalDrag(!isMainLayoutDisplayed);
        verticalViewPager.setOnPageChangeListener(verticalPageChangeListener);

    }

    @Override
    public int getCurrentPage() {
        if (verticalViewPager != null)
            return verticalViewPager.getCurrentItem();
        return 0;
    }

    @Override
    public void showTimeline() {
        verticalViewPager.setCurrentItem(1, true);
        currentVerticalViewPage = 1;
    }

    @Subscribe
    public void onLocationTableUpdated(LocationTableUpdated message) {
        TinyMessageBus.post(new GetLocation(UserUtils.getLastViewedLocationId()));
    }

    @Subscribe
    public void onBlockViewPagerDrag(BlockViewPagerDrag message) {
        Log.i(LOG_TAG, "Block " + message.isBlocked);
        verticalViewPager.blockDragging(message.isBlocked);

        VerticalFragmentPagerAdapter verticalFragmentPagerAdapter = (VerticalFragmentPagerAdapter) verticalViewPager.getAdapter();
        MainLayoutFragment mainLayoutFragment = (MainLayoutFragment) verticalFragmentPagerAdapter.getItem(0);
        if (mainLayoutFragment != null) {
            mainLayoutFragment.setSlidingEnabledWithOverride(!message.isBlocked);
        }
    }


    @Subscribe
    public void onOpenSettings(OpenSettings openSettings) {
        VerticalFragmentPagerAdapter verticalFragmentPagerAdapter = (VerticalFragmentPagerAdapter) verticalViewPager.getAdapter();
        MainLayoutFragment mainLayoutFragment = (MainLayoutFragment) verticalFragmentPagerAdapter.getItem(0);
        if (mainLayoutFragment != null) {
            mainLayoutFragment.collapsePanel();
        }
    }

    //region Tutorial
    @Subscribe
    public void startTimelineTutorial(StartTimelineTutorial startTimelineTutorial) {
        if (!isPanelExpanded())
            closePanel(new ClosePanelRequest());
    }

    @Subscribe
    public void startHomeTutorial(StartHomeTutorial tutorial) {
        if (!isPanelExpanded())
            closePanel(new ClosePanelRequest());

        if (getDevicePagerPosition() != tutorial.getTutorialDetails().getPageToStart())
            return;

        String tag = HomeTutorialOverlayFragment.class.getSimpleName();
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {

            HomeTutorialOverlayFragment tutorialFragment = HomeTutorialOverlayFragment.newInstance(tutorial.getTutorialDetails());

            getSupportFragmentManager().beginTransaction().add(R.id.tutorial_frame, tutorialFragment, tag).
                    setCustomAnimations(0, 0).
                    addToBackStack(null).
                    commit();
        }
    }

    @Subscribe
    public void onServicePlanUpdate(ServicePlanUpdated servicePlanUpdated) {
        checkLocationData();
    }


    @Subscribe
    public void closePanel(ClosePanelRequest closePanelRequest) {
        MainLayoutFragment mainLayoutFragment = getMainLayoutFragment();

        if (mainLayoutFragment != null) {
            // If the Settings menu is showing then close it and get back to the Device viewpager
            if (!mainLayoutFragment.isPanelExpanded()) {
                mainLayoutFragment.expandPanel();

            }
        }
    }

    @Subscribe
    public void resetTimelineFilter(ResetTimelineFilterRequest resetTimelineFilterRequest) {
        if (verticalViewPager == null)
            return;

        VerticalFragmentPagerAdapter verticalFragmentPagerAdapter = (VerticalFragmentPagerAdapter) verticalViewPager.getAdapter();
        EntryListFragment entryListFragment = (EntryListFragment) verticalFragmentPagerAdapter.getItem(TIMELINE_PAGE);
        entryListFragment.showAll();
    }

    @Subscribe
    public void prepareForTimelineFilterTutorial(StartTimelineFilterTutorial event) {
        closePanel(null);
        if (verticalViewPager == null || verticalViewPager.getCurrentItem() == TIMELINE_PAGE)
            return;

        if (getMainLayoutFragment() != null) {
            getMainLayoutFragment().setAlphas(1.0f);
            getMainLayoutFragment().setSlidingEnabled(false);
            getMainLayoutFragment().setDeviceViewPagerEnabled(false);
            getMainLayoutFragment().removeTutorial();
        }
        verticalViewPager.setCurrentItem(TIMELINE_PAGE, false);
    }

    private boolean isPanelExpanded() {
        MainLayoutFragment mainLayoutFragment = getMainLayoutFragment();
        return mainLayoutFragment.isPanelExpanded();
    }

    private MainLayoutFragment getMainLayoutFragment() {
        if (verticalViewPager == null)
            return null;

        VerticalFragmentPagerAdapter verticalFragmentPagerAdapter = (VerticalFragmentPagerAdapter) verticalViewPager.getAdapter();
        MainLayoutFragment mainLayoutFragment = (MainLayoutFragment) verticalFragmentPagerAdapter.getItem(0);
        return mainLayoutFragment;
    }

    private void performPreChecks() {
        if (LocationUtil.doesNotHaveLocationPermission(this)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        }

        if (!PreferencesUtils.hasBeenPromptedForLanguage()) {
            Locale defaultLocale = Resources.getSystem().getConfiguration().locale;
            if (!defaultLocale.getLanguage().equals(getResources().getConfiguration().locale.getLanguage())) {
                String jsonString = Utils.loadJSONFromRawFile(R.raw.locale);
                Type type = new TypeToken<ArrayList<CountryCode>>() {
                }.getType();
                final List<CountryCode> countryCodes = new Gson().fromJson(jsonString, type);
                if (countryCodes != null) {

                    for (CountryCode countryCode : countryCodes) {
                        if (countryCode.code.equals(defaultLocale.getLanguage())) {

                            AlertUtils.showLocaleAlert(this, new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    PreferencesUtils.setHasSeenLanguagePrompt();
                                    Intent intent = new Intent(MainActivity.this, SettingsFragmentStackActivity.class);
                                    intent.setAction("locale");
                                    intent.putExtra("modal", true);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_bottom, R.anim.scale_alpha_push);
                                }
                            });
                            return;
                        }
                    }
                }

            }
        }

    }

    private void checkLocationData() {

        if (!TutorialUtil.userHasFinishedAllTutorials()) {
            return;
        }

        if (!Utils.hasInternetConnection(getBaseContext()))
            return;

        Location currentLocation = UserUtils.getLastViewedLocation();

        if (currentLocation == null)
            return;


        if (currentLocation.trailJustExpired) {
            Intent i = new Intent(this, SettingsFragmentStackActivity.class);
            i.setAction(SettingsFragmentStackActivity.preview_over);
            i.putExtra(SettingsFragmentStackActivity.modal, true);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.scale_alpha_push);
            LocationDatabaseService.setTrailExpired(currentLocation.id, false);
            return;
        }


        Customer customer = CurrentCustomer.getCurrentCustomer();
        if (customer == null)
            return;

        if (!CurrentCustomer.getCurrentCustomer().seenSharePrompt) {
            List<Location> locations = LocationDatabaseService.getLocationList();
            for (Location location : locations) {
                if (location.locationOwner.equalsIgnoreCase(CurrentCustomer.getCurrentCustomer().resourceUri)) {
                    if (location.isUnitedStates() && location.isEighteenDaysOld()) {
                        Intent intent = new Intent(MainActivity.this, SettingsFragmentStackActivity.class);
                        intent.setAction("data_opt_in");
                        intent.putExtra("modal", true);
                        startActivity(intent);
                        return;
                    }
                }
            }
        }

        if (shouldShowMasking(currentLocation)) {
            Intent intent = new Intent(MainActivity.this, SettingsFragmentStackActivity.class);
            intent.setAction(SettingsFragmentStackActivity.extra_masking_tutorial);
            intent.putExtra("modal", true);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.scale_alpha_push);
            PreferencesUtils.setSeenMaskingLaunch(true);
        }
    }

    public boolean shouldShowMasking(Location location) {
        if (PreferencesUtils.hasSeenMaskingLaunch())
            return false;

        if (location == null || !location.showCVMaskTutorial)
            return false;

        Integer locationIDToShowMaskingLaunch = PreferencesUtils.getFirstCreatedLocationIDForMaskingLaunch();
        if (locationIDToShowMaskingLaunch == null || !locationIDToShowMaskingLaunch.equals(location.id))
            return false;

        if (!location.isOwner(CurrentCustomer.getCurrentCustomer()))
            return false;

        Date accountCreationDate = location.created;
        if (accountCreationDate != null) {
            Date currentDate = new Date();
            long timeSinceAccountCreation = currentDate.getTime() - accountCreationDate.getTime();
            if (timeSinceAccountCreation >= TimeUnit.DAYS.toMillis(numberOnDaysToDelayMaskingLaunch)) {
                return true;
            }
        }
        return false;
    }

    @Subscribe
    public void resetDashboard(ResetDashboard dashboard) {
        TinyMessageBus.post(new GetLocation(UserUtils.getLastViewedLocationId()));
        reloadEntries();

    }

    public void reloadEntries() {
        VerticalFragmentPagerAdapter verticalFragmentPagerAdapter = (VerticalFragmentPagerAdapter) verticalViewPager.getAdapter();
        EntryListFragment entryListFragment = (EntryListFragment) verticalFragmentPagerAdapter.getItem(1);
        if (entryListFragment != null) {
            entryListFragment.refreshEntryLoader();
            entryListFragment.resetCalendar();
        }
    }


    public int getDevicePagerPosition() {
        ViewPager viewPager = getDeviceViewPager();
        if (viewPager != null)
            return viewPager.getCurrentItem();

        return 0;
    }

    private ViewPager getDeviceViewPager() {
        VerticalFragmentPagerAdapter verticalFragmentPagerAdapter = (VerticalFragmentPagerAdapter) verticalViewPager.getAdapter();
        MainLayoutFragment mainLayoutFragment = (MainLayoutFragment) verticalFragmentPagerAdapter.getItem(0);
        if (mainLayoutFragment != null) {
            ViewPager viewPager = mainLayoutFragment.getDeviceView();
            return viewPager;
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SetUpGeofence.startService(getBaseContext());

    }

    public Device currentDevice() {
        MainLayoutFragment mainLayoutFragment = getMainLayoutFragment();
        if (mainLayoutFragment == null)
            return null;
        DeviceFragment deviceFragment = mainLayoutFragment.getCurrentDeviceFragment();
        if (deviceFragment == null)
            return null;

        return deviceFragment.getDevice();
    }
}