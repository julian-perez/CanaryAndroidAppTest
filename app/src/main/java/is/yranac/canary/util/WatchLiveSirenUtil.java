package is.yranac.canary.util;

import android.app.ActivityManager;
import android.content.Context;
import android.view.View;

import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.database.DeviceDatabaseService;

/**
 * Created by michaelschroeder on 4/28/17.
 */

public class WatchLiveSirenUtil extends BaseSirenUtil {

    private final View emergencyOptions;
    private final View soundSirenBtn;
    private boolean hidden = false;

    private static final int ANIMATION_DURATION = 250;

    public WatchLiveSirenUtil(Context context, View emergencyOptions) {
        this(context, emergencyOptions, GlobalSirenTimeout.getInstance().getLocationId());
    }

    public WatchLiveSirenUtil(Context context, View emergencyOptions, int locationId) {
        this.context = context;
        this.emergencyOptions = emergencyOptions;
        this.sirenCountdownProgressWheel = emergencyOptions.findViewById(R.id.siren_countdown_progress_wheel);
        this.soundSirenBtn = emergencyOptions.findViewById(R.id.siren_click_able);
        if (locationId == 0) {
            this.location = GlobalSirenTimeout.getInstance().getLocationId();
        } else {
            this.location = locationId;
        }

        boolean hasSiren = false;

        List<Device> devices = DeviceDatabaseService.getActivatedDevicesAtLocation(location);

        for (Device device : devices) {
            if (device.hasSiren()) {
                hasSiren = true;
                break;
            }
        }

        View emergencyCallLayout = this.emergencyOptions.findViewById(R.id.emergency_call_layout);
        View sirenLayout = this.emergencyOptions.findViewById(R.id.siren_layout);

        emergencyCallLayout.setVisibility(Utils.canDial(context) ? View.VISIBLE : View.GONE);
        sirenLayout.setVisibility(hasSiren ? View.VISIBLE : View.GONE);

        if (!hasSiren && !Utils.canDial(context)) {
            this.hidden = true;
            this.emergencyOptions.setVisibility(View.GONE);
        }
    }


    public void showSirenView() {

        AnimationHelper.fadeViewIn(sirenCountdownProgressWheel, ANIMATION_DURATION);
        AnimationHelper.fadeViewOut(soundSirenBtn, ANIMATION_DURATION);

        GlobalSirenTimeout.getInstance().addSirenListener(sirenListener);
    }

    public void dismissSiren() {
        int locationId = GlobalSirenTimeout.getInstance()
                .getLocationId();

        GlobalSirenTimeout.getInstance()
                .stopTimeOut();

        AnimationHelper.fadeFromAlphaToAlpha(sirenCountdownProgressWheel, sirenCountdownProgressWheel.getAlpha(), 0.0f, ANIMATION_DURATION);
        AnimationHelper.fadeFromAlphaToAlpha(soundSirenBtn, soundSirenBtn.getAlpha(), 1.0f, ANIMATION_DURATION);

        List<Device> devices = DeviceDatabaseService.getActivatedDevicesAtLocation(locationId);

        for (Device device : devices) {
            if (device.hasSiren()) {
                DeviceAPIService.changeSirenState(device.resourceUri, false);
            }
        }

    }

    protected void soundSiren(Device device) {

        GlobalSirenTimeout.getInstance()
                .setLocationId(location);
        GlobalSirenTimeout.getInstance()
                .restartTimeout(sirenListener);

        if (ActivityManager.isUserAMonkey())
            return;


        if (device == null) {
            List<Device> devices = DeviceDatabaseService.getActivatedDevicesAtLocation(location);
            for (Device device1 : devices) {
                if (device1.hasSiren()) {
                    DeviceAPIService.changeSirenState(device1.resourceUri, true);
                }
            }
        } else {
            DeviceAPIService.changeSirenState(device.resourceUri, true);

        }

        showSirenView();
    }

    public void removeSirenListener() {
        GlobalSirenTimeout.getInstance()
                .removeSirenListener(sirenListener);
    }


    public void setVisible(int visible) {
        if (this.hidden)
            return;
        emergencyOptions.setVisibility(visible);
    }

    public void slideViewInFromBottom(int duration, int delay) {
        if (this.hidden)
            return;
        AnimationHelper.slideViewInFromBottom(emergencyOptions, duration, delay);
    }
}
