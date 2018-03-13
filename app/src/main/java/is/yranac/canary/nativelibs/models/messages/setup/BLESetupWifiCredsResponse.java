package is.yranac.canary.nativelibs.models.messages.setup;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.R;
import is.yranac.canary.util.StringUtils;

/**
 * Created by sergeymorozov on 10/8/15.
 */
public class BLESetupWifiCredsResponse extends BLESetupResponce {

    @SerializedName("VALUE")
    public String VALUE;

    @SerializedName("ERROR_CODE")
    public String ERROR_CODE;

    public BLESetupWifiCredsResponse() {
        super("04");
    }

    public boolean isSuccess() {
        if (StringUtils.isNullOrEmpty(ERROR_CODE) || StringUtils.isNullOrEmpty(VALUE))
            return false;
        return VALUE.equals("S");
    }

    public String getMeaningfulErrorMessage() {
        Context context = CanaryApplication.getContext();
        if (context == null)
            return "Canary could not connect to Wi-Fi, please try again. If the problem continues, contact customer support.";

        if (StringUtils.isNullOrEmpty(ERROR_CODE))
            return context.getString(R.string.btle_wifi_unknown_error);

        if (ERROR_CODE.equals("invalid-key"))
            return context.getString(R.string.btle_wifi_bad_password);

        if (ERROR_CODE.equals("timeout"))
            return context.getString(R.string.btle_wifi_timeout);

        if (ERROR_CODE.equals("wifi-not-found"))
            return context.getString(R.string.btle_wifi_bad_ssid);

        return context.getString(R.string.btle_wifi_unknown_error);
    }
}
