package is.yranac.canary.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.BuildConfig;
import is.yranac.canary.CanaryApplication;
import is.yranac.canary.messages.BlockViewPagerDrag;
import is.yranac.canary.messages.ClosePanelRequest;
import is.yranac.canary.messages.ResetTimelineFilterRequest;
import is.yranac.canary.messages.TutorialRequest;
import is.yranac.canary.messages.tutorial.SetUpDeviceViewsForTutorialRequest;
import is.yranac.canary.messages.tutorial.StartEntryOptionsTutorial;
import is.yranac.canary.messages.tutorial.StartHomeTutorial;
import is.yranac.canary.messages.tutorial.StartSecondDeviceTutorial;
import is.yranac.canary.messages.tutorial.StartTimelineFilterTutorial;
import is.yranac.canary.messages.tutorial.StartTimelineTutorial;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.tutorial.TutorialDetails;
import is.yranac.canary.model.tutorial.TutorialsState;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;

import static is.yranac.canary.util.TutorialUtil.TutorialType.ENTRY_MORE_OPTIONS;
import static is.yranac.canary.util.TutorialUtil.TutorialType.HOME;
import static is.yranac.canary.util.TutorialUtil.TutorialType.HOME_FOR_SECOND_DEVICE;
import static is.yranac.canary.util.TutorialUtil.TutorialType.NONE;
import static is.yranac.canary.util.TutorialUtil.TutorialType.SECOND_DEVICE;
import static is.yranac.canary.util.TutorialUtil.TutorialType.TIMELINE;
import static is.yranac.canary.util.TutorialUtil.TutorialType.TIMELINE_FILTER;

/**
 * Created by sergeymorozov on 7/12/16.
 */
public class TutorialUtil {

    private static final String LOG_TAG = "TutorialUtil";
    private static volatile TutorialUtil tutorialUtil;
    private static volatile TutorialsState currentTutorialState;
    private static volatile TutorialType tutorialInProgress;
    private static final int totalHomeTutorialSteps = 4;

    private static final int tutorialDelayMlscs = 600;
    private static final int defaultFragmentTransitionAnimation = 400;

    private static volatile Queue<TutorialType> tutorialQueue;

    public enum TutorialType {
        HOME,
        HOME_FOR_SECOND_DEVICE,
        TIMELINE,
        TIMELINE_FILTER,
        ENTRY_MORE_OPTIONS,
        SECOND_DEVICE,
        NONE
    }

    @Subscribe(mode = Subscribe.Mode.Background)
    public void checkIfTutorialNeeded(TutorialRequest tutorialRequest) {
        if (userHasFinishedAllTutorials() || isTutorialInProgress())
            return;

        TutorialType nextTutorial = getNextTutorial();
        if (nextTutorial == null)
            return;

        switch (nextTutorial) {
            case HOME:
                startHometutorial();
                break;
            case HOME_FOR_SECOND_DEVICE:
                startHomeForSecondDevice();
                break;
            case TIMELINE:
                startTimelineTutorial();
                break;
            case TIMELINE_FILTER:
                startTimeLineFilterTutorial();
                break;
            case ENTRY_MORE_OPTIONS:
                startEntryMoreOptionsTutorial();
                break;
            case SECOND_DEVICE:
                startSecondDeviceTutorial();
                break;

        }
    }

    private void startHometutorial() {
        if (!canStartTutorial(HOME))
            return;

        TutorialDetails tutorialDetails = getDetailsForHomeTutorial(HOME);
        setTutorialInProgress(HOME);

        TinyMessageBus.post(new ClosePanelRequest());
        TinyMessageBus.postDelayed(new SetUpDeviceViewsForTutorialRequest(tutorialDetails.getPageToStart()), defaultFragmentTransitionAnimation);
        TinyMessageBus.postDelayed(new StartHomeTutorial(tutorialDetails), tutorialDelayMlscs + defaultFragmentTransitionAnimation);
    }

    private void startHomeForSecondDevice() {
        if (!canStartTutorial(HOME_FOR_SECOND_DEVICE))
            return;

        TutorialDetails tutorialDetails = getDetailsForHomeTutorial(HOME_FOR_SECOND_DEVICE);
        setTutorialInProgress(HOME_FOR_SECOND_DEVICE);

        TinyMessageBus.post(new ClosePanelRequest());
        TinyMessageBus.postDelayed(new SetUpDeviceViewsForTutorialRequest(tutorialDetails.getPageToStart() + 1), defaultFragmentTransitionAnimation);
        TinyMessageBus.postDelayed(new StartHomeTutorial(tutorialDetails), tutorialDelayMlscs + defaultFragmentTransitionAnimation);
    }

    private void startTimelineTutorial() {
        if (!canStartTutorial(TIMELINE))
            return;

        setTutorialInProgress(TIMELINE);

        TinyMessageBus.post(new ClosePanelRequest());
        TinyMessageBus.postDelayed(new ResetTimelineFilterRequest(), defaultFragmentTransitionAnimation);
        TinyMessageBus.postDelayed(new StartTimelineTutorial(), tutorialDelayMlscs);
        TinyMessageBus.postDelayed(new BlockViewPagerDrag(true), tutorialDelayMlscs
                + defaultFragmentTransitionAnimation);
    }

    private void startTimeLineFilterTutorial() {
        if (!canStartTutorial(TIMELINE_FILTER))
            return;

        setTutorialInProgress(TIMELINE_FILTER);
        TinyMessageBus.post(new StartTimelineFilterTutorial());
        TinyMessageBus.postDelayed(new BlockViewPagerDrag(true), tutorialDelayMlscs
                + defaultFragmentTransitionAnimation);

    }

    private void startEntryMoreOptionsTutorial() {
        if (!canStartTutorial(ENTRY_MORE_OPTIONS))
            return;

        setTutorialInProgress(ENTRY_MORE_OPTIONS);
        TinyMessageBus.post(new StartEntryOptionsTutorial());
    }

    private void startSecondDeviceTutorial() {
        if (!canStartSecondDeviceTutorial())
            return;

        setTutorialInProgress(SECOND_DEVICE);
        TutorialDetails tutorialDetails = getDetailsForHomeTutorial(SECOND_DEVICE);
        TinyMessageBus.postDelayed(new ClosePanelRequest(), defaultFragmentTransitionAnimation);
        TinyMessageBus.postDelayed(new StartSecondDeviceTutorial(tutorialDetails), tutorialDelayMlscs);
    }


    public static void register() {
        TinyMessageBus.register(getTutorialUnil());
    }

    public static void unregister() {
        TinyMessageBus.unregister(getTutorialUnil());
        setTutorialInProgress(NONE);
    }


    public static boolean userHasFinishedAllTutorials() {
        return getCurrentTutorialState().didCompleteHomeTutorial
                && getCurrentTutorialState().didCompleteTimelineTutorial
                && getCurrentTutorialState().didCompleteSecondDeviceTutorial
                && getCurrentTutorialState().didCompleteSingleEntryMoreOptionsTutorial
                && getCurrentTutorialState().didCompleteTimelineFilterTutorial
                && getCurrentTutorialState().didCompleteHomeForSecondDeviceTutorial;
    }

    public static void finishTutorials(TutorialType[] typesToFinish) {
        if (typesToFinish == null)
            return;

        for (TutorialType type : typesToFinish) {
            finishTutorial(type);
        }
    }

    public static void finishTutorial(TutorialType type) {
        Queue<TutorialType> queue = getTutorialQueue();

        TutorialType currentTutorial = getTutorialQueue().peek();
        if (currentTutorial != null && currentTutorial == type) {
            queue.poll();
        }

        setHasFinishedTutorial(type, true);
    }

    public static void queueTutorials(TutorialType[] tutorialTypes, boolean emptyQueue) {
        if (tutorialTypes == null)
            return;

        if (emptyQueue)
            getTutorialQueue().clear();

        for (TutorialType type : tutorialTypes) {
            queueTutorial(type);
        }
    }

    public static void queueTutorial(TutorialType type) {
        queueTutorial(type, false);
    }

    public static void queueTutorial(TutorialType type, boolean resetQueue) {

        Queue<TutorialType> queue = getTutorialQueue();
        if (resetQueue)
            queue.clear();

        setHasFinishedTutorial(type, false);
        queue.add(type);
    }

    private static void setHasFinishedTutorial(TutorialType tutorialType, boolean hasFinishedTutorial) {

        TutorialsState currentState = getCurrentTutorialState();

        switch (tutorialType) {
            case HOME:
                currentState.didCompleteHomeTutorial = hasFinishedTutorial;
                break;
            case HOME_FOR_SECOND_DEVICE:
                currentState.didCompleteHomeForSecondDeviceTutorial = hasFinishedTutorial;
                break;
            case TIMELINE:
                currentState.didCompleteTimelineTutorial = hasFinishedTutorial;
                break;
            case TIMELINE_FILTER:
                currentState.didCompleteTimelineFilterTutorial = hasFinishedTutorial;
                break;
            case ENTRY_MORE_OPTIONS:
                currentState.didCompleteSingleEntryMoreOptionsTutorial = hasFinishedTutorial;
                break;
            case SECOND_DEVICE:
                currentState.didCompleteSecondDeviceTutorial = hasFinishedTutorial;
                break;
        }

        PreferencesUtils.saveTutorialsState(currentState);

        if (hasFinishedTutorial && getTutorialInProgress() == tutorialType)
            tutorialInProgress = NONE;
    }

    public static void clearTutorialQueue() {
        getTutorialQueue().clear();
    }

    public static boolean getHasFinishedTutorial(TutorialType tutorialType) {
        if (Utils.isDemo())
            return true;

        switch (tutorialType) {
            case HOME:
                return getCurrentTutorialState().didCompleteHomeTutorial;
            case HOME_FOR_SECOND_DEVICE:
                return getCurrentTutorialState().didCompleteHomeForSecondDeviceTutorial;
        }

        try {
            Context context = CanaryApplication.getContext();
            long installed = context
                    .getPackageManager()
                    .getPackageInfo(BuildConfig.APPLICATION_ID, 0)
                    .firstInstallTime;


            if (new Date().getTime() - installed < TimeUnit.DAYS.toMillis(1)) {
                return true;
            }

        } catch (PackageManager.NameNotFoundException ignored) {

        }

        switch (tutorialType) {
            case TIMELINE:
                return getCurrentTutorialState().didCompleteTimelineTutorial;
            case TIMELINE_FILTER:
                return getCurrentTutorialState().didCompleteTimelineFilterTutorial;
            case ENTRY_MORE_OPTIONS:
                return getCurrentTutorialState().didCompleteSingleEntryMoreOptionsTutorial;
            case SECOND_DEVICE:
                return getCurrentTutorialState().didCompleteSecondDeviceTutorial;
            default:
                return true;
        }
    }

    public static void setCompletedTutorialStep(TutorialType tutorialType, int i) {
        i += 1;
        switch (tutorialType) {
            case HOME_FOR_SECOND_DEVICE:
            case HOME:
                if (i > totalHomeTutorialSteps)
                    i = 0;
                getCurrentTutorialState().currentHomeStep = i;
                break;
            default:
                return;
        }
        PreferencesUtils.saveTutorialsState(getCurrentTutorialState());
    }

    public static int getTutorialStep(TutorialType tutorialType) {
        switch (tutorialType) {
            case HOME_FOR_SECOND_DEVICE:
            case HOME:
                return getCurrentTutorialState().currentHomeStep;
            default:
                return -1;
        }
    }

    public static boolean isTutorialInProgress() {
        return tutorialInProgress != null && tutorialInProgress != NONE;
    }

    public static TutorialType getTutorialInProgress() {
        if (tutorialInProgress == null)
            return NONE;
        return tutorialInProgress;
    }

    public static void checkNewDeviceTutorials(Device newDevice) {
        if (newDevice == null)
            return;

        boolean isDeviceActivated =
                !TextUtils.isEmpty(newDevice.activationStatus) && newDevice.activationStatus.equalsIgnoreCase("activated");

        if (!isDeviceActivated) {
            return;
        }

        boolean addingFlex = newDevice.deviceType != null && newDevice.deviceType.id == DeviceType.FLEX;

        Location location =
                LocationDatabaseService
                        .getLocationFromResourceUri(newDevice.location);

        List<Device> devices =
                DeviceDatabaseService
                        .getActivatedDevicesAtLocation(location.id);

        boolean userHasActivatedFlex = false;
        for (Device device : devices) {
            if (device.deviceType.id == DeviceType.FLEX && device.id != newDevice.id)
                userHasActivatedFlex = true;
        }

        //checking if we need a second device tutorial
        boolean addingSecondDevice = devices.size() == 2 && !PreferencesUtils.getUserSwipedBetweenDevices();
        if (addingSecondDevice) {
            queueTutorial(SECOND_DEVICE, true);
        }

        //checking if we need restart home tutorial for Flex
        if (addingFlex && !userHasActivatedFlex) {
            queueTutorial(HOME_FOR_SECOND_DEVICE);
        }

        if (addingSecondDevice || addingFlex && !userHasActivatedFlex)
            TinyMessageBus.post(new TutorialRequest());
    }

    public static void saveTutorialDeviceIndex(int tutorialDeviceIndex) {
        if (tutorialDeviceIndex < 0)
            return;

        TutorialsState currentState = getCurrentTutorialState();
        currentState.startDeviceIndex = tutorialDeviceIndex;
        PreferencesUtils.saveTutorialsState(currentState);
    }

    public static void resetTutorialDeviceIndex() {
        TutorialsState currentState = getCurrentTutorialState();
        currentState.startDeviceIndex = null;
        PreferencesUtils.saveTutorialsState(currentState);
    }

    private static Integer getTutorialDeviceIndex() {
        TutorialsState currentState = getCurrentTutorialState();

        return currentState.startDeviceIndex;
    }

    private static void setTutorialInProgress(TutorialType typeInProgress) {
        if (typeInProgress == null)
            typeInProgress = NONE;
        tutorialInProgress = typeInProgress;
    }

    public static TutorialsState getCurrentTutorialState() {
        if (currentTutorialState == null) {
            currentTutorialState = PreferencesUtils.getTutorialsState();
        }
        return currentTutorialState;
    }

    private static boolean canStartTutorial(TutorialType type) {
        return (getTutorialInProgress() == TutorialType.NONE) || (type == getTutorialInProgress());
    }

    private static boolean canStartSecondDeviceTutorial() {
        if (PreferencesUtils.getUserSwipedBetweenDevices())
            return false;

        switch (getTutorialInProgress()) {
            case TIMELINE:
            case HOME:
            case TIMELINE_FILTER:
            case SECOND_DEVICE:
                return false;
        }
        return true;
    }

    private static TutorialUtil getTutorialUnil() {
        if (tutorialUtil == null)
            tutorialUtil = new TutorialUtil();
        return tutorialUtil;
    }

    private static List<Device> getDevicesAtCurrentLocation() {
        return DeviceDatabaseService.getActivatedDevicesAtLocation(UserUtils.getLastViewedLocationId());
    }

    private static TutorialDetails getDetailsForHomeTutorial(TutorialType tutorialType) {
        List<Device> deviceList = getDevicesAtCurrentLocation();
        int pageToStart = 0;
        boolean multipleDeviceTypeTutorial = false;
        int totalNumOfActivatedDvices = deviceList.size();
        if (deviceList.size() > 1) {
            Integer prevDeviceType = null;
            for (int i = 0; i < deviceList.size(); i++) {
                Device device = deviceList.get(i);
                if (prevDeviceType == null) {
                    prevDeviceType = device.getDeviceType();
                } else if (prevDeviceType != device.getDeviceType()) {
                    if ((prevDeviceType == DeviceType.FLEX && device.getDeviceType() != DeviceType.FLEX) ||
                            (prevDeviceType != DeviceType.FLEX && device.getDeviceType() == DeviceType.FLEX)) {
                        pageToStart = i - 1;
                        multipleDeviceTypeTutorial = true;
                        break;
                    }
                }
            }

            Integer lastDeviceIndex = getTutorialDeviceIndex();
            if (tutorialType != SECOND_DEVICE && lastDeviceIndex != null) {
                pageToStart = lastDeviceIndex + 1;
            } else if (tutorialType == HOME_FOR_SECOND_DEVICE) {
                pageToStart = pageToStart += 1;
            }
        }
        return new TutorialDetails(pageToStart, tutorialType, multipleDeviceTypeTutorial, totalNumOfActivatedDvices);
    }

    private static Queue<TutorialType> getTutorialQueue() {
        if (tutorialQueue == null) {
            tutorialQueue = new ConcurrentLinkedQueue<>();
        }
        return tutorialQueue;
    }

    private static void buildTutorialFlow() {
        TutorialsState state = getCurrentTutorialState();
        Queue<TutorialType> queue = getTutorialQueue();
        if (!state.didCompleteHomeTutorial) {
            queue.add(HOME);
            TutorialDetails tutorialDetails = getDetailsForHomeTutorial(HOME);
            if (tutorialDetails.totalNumOfActivatedDvices > 1 && !PreferencesUtils.getUserSwipedBetweenDevices()) {
                queue.add(SECOND_DEVICE);
                if (tutorialDetails.isMultiDeviceTypeTutorial())
                    tutorialQueue.add(HOME_FOR_SECOND_DEVICE);

                tutorialQueue.add(HOME);
            }
        }
        if (!state.didCompleteTimelineTutorial) {
            queue.add(TIMELINE);
        }
        if (!state.didCompleteTimelineFilterTutorial)
            queue.add(TIMELINE_FILTER);
        if (!state.didCompleteSingleEntryMoreOptionsTutorial)
            queue.add(ENTRY_MORE_OPTIONS);
    }

    private static TutorialType getNextTutorial() {
        TutorialType nextTutorial = getTutorialQueue().peek();
        if (nextTutorial == null)
            buildTutorialFlow();

        return getTutorialQueue().peek();
    }
}
