package is.yranac.canary.model.device;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.R;
import is.yranac.canary.model.mode.ModeCache;
import is.yranac.canary.model.reading.Reading;
import is.yranac.canary.services.database.ReadingDatabaseService;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.Utils;

import static is.yranac.canary.model.device.Device.DeviceColor.Black;
import static is.yranac.canary.model.device.Device.DeviceColor.Graphite;
import static is.yranac.canary.model.device.Device.DeviceColor.Unknown;
import static is.yranac.canary.model.mode.ModeCache.privacy;
import static is.yranac.canary.model.mode.ModeCache.standby;

public class Device {

    private static final String LOG_TAG = "Device";

    @SerializedName("activation_token")
    public String activationToken;

    @SerializedName("application_version")
    public String applicationVersion;

    @SerializedName("activation_status")
    public String activationStatus;

    @SerializedName("serial_number")
    public String serialNumber;

    @SerializedName("device_activated")
    public boolean deviceActivated;

    @SerializedName("device_type")
    public DeviceType deviceType;

    @SerializedName("id")
    public int id;

    @SerializedName("image_url")
    public String imageUrl;

    @SerializedName("location")
    public String location;

    @SerializedName("mode")
    public String mode;

    @SerializedName("name")
    public String name;

    @SerializedName("resource_uri")
    public String resourceUri;

    @SerializedName("online")
    public boolean isOnline;

    @SerializedName("siren_active")
    public boolean sirenActive;

    @SerializedName("uploader_active")
    public boolean uploaderActive;

    @SerializedName("uuid")
    public String uuid;

    @SerializedName("ota_status")
    public String ota_status;

    public String imageUrl() {

        if (Utils.isDemo()) {

            Context context = CanaryApplication.getContext();
            int resourceId = context.getResources().getIdentifier(imageUrl, "drawable",
                    context.getPackageName());

            Log.i(LOG_TAG, imageUrl + ", " + resourceId);

            return "drawable://" + resourceId;
        }

        return imageUrl;
    }

    /**
     * This method checks the device application version. If it is above 1.3, it means
     * Canary is BTLE enabled.
     *
     * @return true if device is ready use bluetooth, false in all other cases
     */
    public boolean isBTLECompatible() {

        if (Utils.isDev() && !TextUtils.isEmpty(this.serialNumber)) {
            DeviceType type = Utils.getDeviceTypeFromDeviceSerialNumber(this.serialNumber);
            if (type != null && type.id != DeviceType.CANARY_AIO)
                return true;
        }


        if (deviceType != null && (deviceType.id == DeviceType.CANARY_PLUS ||
                deviceType.id == DeviceType.FLEX)) {
            return true;
        }

        return isDeviceAtOrAboveVersion(1, 3, 0);
    }

    public boolean isMaskCompatible() {
        if (getDeviceType() == DeviceType.FLEX)
            return false;

        return isDeviceAtOrAboveVersion(1, 4, 5);
    }

    public boolean isDeviceAtOrAboveVersion(int major, int minor, int patch) {
        if (TextUtils.isEmpty(this.applicationVersion))
            return false;

        String versionParse = this.applicationVersion;

        int dashIndex = versionParse.indexOf("-");
        if (dashIndex >= 0) {
            versionParse = versionParse.substring(0, dashIndex);
        }
        String[] stringParts = versionParse.replace("v", "").split("\\.");
        if (stringParts.length < 3)
            return false;

        int majorVersion = Integer.valueOf(stringParts[0]);
        int minorVersion = Integer.valueOf(stringParts[1]);
        int patchVersion = Integer.valueOf(stringParts[2]);

        //only return true for Canaries with app version >= 1.4.5
        return (majorVersion > major || //if major version is more, ok
                (majorVersion == major && minorVersion > minor) || //if major version matches, and minor is more, ok
                (majorVersion >= major && minorVersion >= minor && patchVersion >= patch)); //if major and minor match, and patch is more, ok

    }

    public boolean hasSiren() {
        return deviceType.id == DeviceType.CANARY_AIO;
    }


    public boolean hasSensors() {
        return deviceType.id != DeviceType.CANARY_VIEW;
    }

    public int getDeviceType() {
        if (deviceType == null)
            return new DeviceType(DeviceType.NONE).id;
        return deviceType.id;

    }

    public boolean failedOTA() {
        if (deviceActivated)
            return false;
        switch (ota_status) {
            case "minimal":
            case "failed":
                return true;
        }
        return false;
    }

    public boolean isOtaing() {
        if (ota_status == null)
            return true;
        switch (ota_status) {
            case "inactive":
            case "downloading":
            case "downloaded":
            case "rebooting":
            case "verified":
                return true;
        }

        return false;
    }

    public int getIcon() {
        switch (getDeviceType()) {
            case DeviceType.CANARY_AIO:
                return R.drawable.device_aio_front;
            case DeviceType.FLEX:
                return R.drawable.device_flex_front;
            case DeviceType.CANARY_VIEW:
                return R.drawable.device_view_front;
            default:
                return R.drawable.device_aio_front;
        }
    }


    public int getLocationId() {
        return Utils.getIntFromResourceUri(location);
    }

    public int getLargeIcon() {
        switch (getDeviceType()) {
            case DeviceType.CANARY_AIO:
            case DeviceType.CANARY_VIEW:
                return R.drawable.select_device_canary_card;
            case DeviceType.FLEX:
                return R.drawable.select_device_flex;
        }

        return R.drawable.select_device_canary_card;
    }

    public boolean hasBattery() {
        return getDeviceType() == DeviceType.FLEX;
    }

    public boolean hasNewWatchLive() {
        return isDeviceAtOrAboveVersion(2, 0, 0);
    }

    public String getDeviceTypeName() {
        if (deviceType == null)
            return new DeviceType(DeviceType.NONE).name;

        return deviceType.name;

    }

    public boolean sendWatchLiveCommand() {

        if (!isOnline)
            return false;

        if (getDeviceType() == DeviceType.FLEX) {

            Reading batteryReading = ReadingDatabaseService.getLatestReadingForDevice(
                    resourceUri, Reading.READING_BATTERY);
            if (batteryReading == null || batteryReading.getBatteryState() == Reading.BatteryState.DISCHARGING) {
                return false;
            }
        }

        return true;
    }

    public boolean isPrivate() {
        return mode.equalsIgnoreCase(ModeCache.getMode(privacy).resourceUri);
    }

    public enum DeviceColor {
        Black("0"),
        Graphite("3"),

        Unknown(null);

        private final String text;

        /**
         * @param text
         */
        DeviceColor(final String text) {
            this.text = text;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            if (text == null) {
                return "Unknown";
            }
            return text;
        }
    }


    public DeviceColor getColor() {
        if (TextUtils.isEmpty(serialNumber)) {
            return Unknown;
        }

        if (getDeviceType() != DeviceType.CANARY_VIEW) {
            return Unknown;
        }

        String deviceType = serialNumber.substring(3, 4);
        if (deviceType.equalsIgnoreCase(Black.text))
            return Black;


        if (deviceType.equalsIgnoreCase(Graphite.text))
            return Graphite;

        return Unknown;
    }

    public boolean isHSNDevice() {
        if (TextUtils.isEmpty(serialNumber)) {
            return false;
        }


        if (getDeviceType() != DeviceType.CANARY_VIEW) {
            return false;
        }


        String month = serialNumber.substring(4, 5);
        String year = serialNumber.substring(5, 7);

        String lineNumberString = serialNumber.substring(7, 12);


        Integer lineNumber;
        try {
            lineNumber = Integer.parseInt(lineNumberString);
        } catch (Exception e) {
            return false;
        }
        if (!year.equalsIgnoreCase("17"))
            return false;


        if (month.equalsIgnoreCase("K")) {

            switch (getColor()) {
                case Graphite:
                    return lineNumber <= 10022;
                case Black:
                    return lineNumber <= 6217;
                default:
                    return false;

            }
        } else if (month.equalsIgnoreCase("J")) {
            return true;
        }

        return false;
    }

    public boolean hasStandbyMode() {

        if (TextUtils.isEmpty(serialNumber)) {
            return false;
        }


        return isDeviceAtOrAboveVersion(2, 2, 0);
    }

    public boolean hasRecordingDisabled() {
        boolean privateMode = isPrivate();
        boolean standyMode = mode.equalsIgnoreCase(ModeCache.getMode(standby).resourceUri);
        return privateMode || standyMode;
    }
}
