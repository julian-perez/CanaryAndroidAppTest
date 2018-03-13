package is.yranac.canary.model.devicetoken;

import android.content.Context;
import android.os.Build;

import com.google.android.gms.iid.InstanceID;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

import is.yranac.canary.BuildConfig;
import is.yranac.canary.CanaryApplication;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 8/13/14.
 */
public class CreateDeviceToken {


    @SerializedName("channel")
    public String channel;


    @SerializedName("token")
    public String token;

    @SerializedName("phone_type")
    public String phoneType;

    @SerializedName("build_number")
    public int buildNumber = BuildConfig.VERSION_CODE;

    @SerializedName("app_version")
    public String appVersion = BuildConfig.VERSION_NAME;

    @SerializedName("device_name")
    public String deviceName;

    @SerializedName("os")
    public String os = Build.VERSION.RELEASE;

    @SerializedName("uuid")
    public String uuid;

    @SerializedName("beta")
    public boolean beta;

    public CreateDeviceToken(String token, String channel) {
        this.phoneType = "android";
        this.token = token;
        this.channel = channel;

        final String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;

        if (model.startsWith(manufacturer)) {
            deviceName = model.toUpperCase();
        } else if (manufacturer.equalsIgnoreCase("HTC")) {
            // make sure "HTC" is fully capitalized.
            deviceName = "HTC " + model;
        } else {
            deviceName = manufacturer.toUpperCase(Locale.getDefault()) + " " + model;
        }


        Context context = CanaryApplication.getContext();

        beta = Utils.isBeta();

        uuid = InstanceID.getInstance(context).getId();

    }
}
