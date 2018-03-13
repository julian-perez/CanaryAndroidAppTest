package is.yranac.canary.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.services.api.DeviceAPIService;
import is.yranac.canary.services.database.DeviceDatabaseService;

public class SirenUtil extends BaseSirenUtil {

    private static final String LOG_TAG = "SirenUtil";
    private final View emergencyOptions;
    private final View soundSirenBtn;
    private final View soundSirenLayout;
    private final View sirenSoundingLayout;
    private final boolean hideWhenFinished;
    private int location;
    private boolean hidden = false;

    private static final int ANIMATION_DURATION = 250;


    public SirenUtil(Context context, View emergencyOptions, int locationId) {
        this(context, emergencyOptions, locationId, false);
    }

    public SirenUtil(Context context, View emergencyOptions, int locationId, boolean hideWhenFinished) {
        this.context = context;
        this.emergencyOptions = emergencyOptions;
        this.sirenCountdownProgressWheel = emergencyOptions.findViewById(R.id.siren_countdown_progress_wheel);
        this.soundSirenBtn = emergencyOptions.findViewById(R.id.sound_siren_btn);
        this.soundSirenLayout = emergencyOptions.findViewById(R.id.sound_siren_layout);
        this.sirenSoundingLayout = emergencyOptions.findViewById(R.id.siren_sounding_layout);
        this.hideWhenFinished = hideWhenFinished;
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
        soundSirenLayout.setAlpha(0.0f);
        soundSirenLayout.setVisibility(View.GONE);
        sirenSoundingLayout.setAlpha(1.0f);
        sirenSoundingLayout.setVisibility(View.VISIBLE);

        int lightRed = ContextCompat.getColor(context, R.color.light_red);
        soundSirenBtn.setBackgroundColor(lightRed);

        GlobalSirenTimeout.getInstance().addSirenListener(sirenListener);
    }


    public void dismissSiren() {
        int locationId = GlobalSirenTimeout.getInstance()
                .getLocationId();

        GlobalSirenTimeout.getInstance()
                .stopTimeOut();

        if (hideWhenFinished) {
            AnimationHelper.fadeFromAlphaToAlpha(emergencyOptions, 1.0f, 0.0f, ANIMATION_DURATION);
        } else {
            int veryLightGray = ContextCompat.getColor(context, R.color.very_light_gray);

            AnimationHelper.fadeFromAlphaToAlpha(soundSirenLayout, soundSirenLayout.getAlpha(), 1.0f, ANIMATION_DURATION);
            AnimationHelper.fadeFromAlphaToAlpha(sirenSoundingLayout, sirenSoundingLayout.getAlpha(), 0.0f, ANIMATION_DURATION);
            AnimationHelper.animationBackgroundColor(soundSirenBtn, ((ColorDrawable) soundSirenBtn.getBackground()).getColor(), veryLightGray, ANIMATION_DURATION);
        }

        List<Device> devices = DeviceDatabaseService.getActivatedDevicesAtLocation(locationId);

        for (Device device : devices) {
            if (device.hasSiren()) {
                DeviceAPIService.changeSirenState(device.resourceUri, false);
            }
        }

    }

    protected void soundSiren(Device device) {


        int lightRed = ContextCompat.getColor(context, R.color.light_red);
        AnimationHelper.animationBackgroundColor(soundSirenBtn, ((ColorDrawable) soundSirenBtn.getBackground()).getColor(), lightRed, ANIMATION_DURATION);

        GlobalSirenTimeout.getInstance()
                .setLocationId(location);
        GlobalSirenTimeout.getInstance()
                .restartTimeout(sirenListener);


        AnimationHelper.fadeFromAlphaToAlpha(soundSirenLayout, soundSirenLayout.getAlpha(), 0.0f, ANIMATION_DURATION);
        AnimationHelper.fadeFromAlphaToAlpha(sirenSoundingLayout, sirenSoundingLayout.getAlpha(), 1.0f, ANIMATION_DURATION);
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
}
