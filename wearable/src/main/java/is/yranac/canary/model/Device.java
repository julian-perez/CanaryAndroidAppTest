package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;


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

    @SerializedName("firmware_version")
    public String firmwareVersion;

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

}
