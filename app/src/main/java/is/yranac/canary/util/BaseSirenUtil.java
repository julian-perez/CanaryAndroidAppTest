package is.yranac.canary.util;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.services.database.DeviceDatabaseService;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.ui.views.ProgressWheel;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_SIREN_CONFIRM_ALL;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_SIREN_CONFIRM_ONE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_SIREN_PRESS;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_SIREN_STOP;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_EMERGENCY_OPTIONS;

/**
 * Created by michaelschroeder on 4/28/17.
 */

public abstract class BaseSirenUtil {

    protected static final String LOG_TAG = "BaseSirenUtil";

    protected ProgressWheel sirenCountdownProgressWheel;
    protected Context context;
    protected int location;

    public abstract void dismissSiren();

    protected abstract void soundSiren(Device device);

    public void showDeviceCountdown(final String view, final Device device) {
        if (Utils.isDemo()) {

            if (context instanceof BaseActivity) {
                BaseActivity activity = (BaseActivity) context;

                if (!activity.isPaused()) {
                    AlertUtils.showGenericAlert(activity, activity.getString(R.string.take_action), activity.getString(R.string.take_action_dsc));
                }
            }
            return;
        }
        GoogleAnalyticsHelper.trackEvent(CATEGORY_EMERGENCY_OPTIONS, ACTION_SIREN_PRESS);

        this.location = device.getLocationId();

        final List<Device> devices = DeviceDatabaseService.getActivatedDevicesAtLocation(location);


        final List<Device> sirenDevices = new ArrayList<>();

        for (Device device1 : devices) {
            if (device1.hasSiren()) {
                sirenDevices.add(device1);
            }
        }
        final View.OnClickListener connectivityClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = context.getString(R.string.connectivity_url);
                ZendeskUtil.loadHelpCenter(context, url);
            }
        };


        AlertUtils.showSirenAlert(context, device.hasSiren() || devices.size() <= 1, sirenDevices.size(), device.getDeviceType(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!canSoundSiren(sirenDevices)) {
                    String alertTitle = context.getString(R.string.devices_offline);
                    if (sirenDevices.size() == 1) {
                        alertTitle = context.getString(R.string.device_offline);
                    }
                    String alertDsc = context.getString(R.string.device_offline_dsc);
                    String leftBtnTitle = context.getString(R.string.get_help);
                    String rightBtnTitle = context.getString(R.string.okay);
                    AlertUtils.showGenericAlert(context, alertTitle, alertDsc, 0, leftBtnTitle, rightBtnTitle, 0, 0, connectivityClickListener, null);
                    return;
                }

                GoogleAnalyticsHelper.trackEvent(CATEGORY_EMERGENCY_OPTIONS, ACTION_SIREN_CONFIRM_ALL, view, device.uuid, location, 0);
                soundSiren(null);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Device> deviceList = new ArrayList<>();
                deviceList.add(device);
                if (!canSoundSiren(deviceList)) {
                    String alertTitle = context.getString(R.string.device_offline);
                    String alertDsc = context.getString(R.string.device_offline_dsc);
                    String leftBtnTitle = context.getString(R.string.get_help);
                    String rightBtnTitle = context.getString(R.string.okay);
                    AlertUtils.showGenericAlert(context, alertTitle, alertDsc, 0, leftBtnTitle, rightBtnTitle, 0, 0, connectivityClickListener, null);

                    return;
                }

                GoogleAnalyticsHelper.trackEvent(CATEGORY_EMERGENCY_OPTIONS, ACTION_SIREN_CONFIRM_ONE, view, device.uuid, location, 0);
                soundSiren(device);

            }
        });

    }

    protected final boolean canSoundSiren(List<Device> devices) {
        boolean online = false;

        for (Device device1 : devices) {
            if (device1.isOnline && device1.hasSiren()) {
                online = true;
            }
        }

        return online;
    }

    public boolean dismissSirenFromScreen(String view) {

        if (!GlobalSirenTimeout.getInstance().isSirenCountingDown()) {
            return false;
        }

        if (view != null)
            GoogleAnalyticsHelper.trackEvent(CATEGORY_EMERGENCY_OPTIONS, ACTION_SIREN_STOP, view);

        dismissSiren();

        return true;
    }

    protected GlobalSirenTimeout.SirenListener sirenListener = new GlobalSirenTimeout.SirenListener() {
        @Override
        public void onTimeUpdate(int intervalsLeft) {

            Log.i(LOG_TAG, "intervalsLeft " + intervalsLeft);
            sirenCountdownProgressWheel.setProgress(360 - intervalsLeft);
            sirenCountdownProgressWheel.setText(String.valueOf(Math.round(((float) intervalsLeft / 360) * 30)));

            if (intervalsLeft <= 0) {
                dismissSiren();
            }

        }
    };

    public void removeSirenListener() {
        GlobalSirenTimeout.getInstance()
                .removeSirenListener(sirenListener);
    }
}
