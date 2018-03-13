package is.yranac.canary.services.api;

import is.yranac.canary.Constants;
import is.yranac.canary.model.devicetoken.ChangeDeviceToken;
import is.yranac.canary.model.devicetoken.CreateDeviceToken;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.keystore.KeyStoreHelper;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Schroeder on 8/13/14.
 */
public class DeviceTokenAPIServices {

    private static final String LOG_TAG = "DeviceTokenAPIServices";

    public static void changeDeviceToken(final String token, final String channel, Callback<Void> callback) {
        if (!KeyStoreHelper.hasGoodOauthToken()) {
            return;
        }
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);

        final DeviceTokenService deviceTokenService = restAdapter.create(DeviceTokenService.class);


        CreateDeviceToken createDeviceToken = new CreateDeviceToken(token, channel);
        deviceTokenService.createDeviceToken(createDeviceToken,callback);

    }


    public static void deactivateDeviceToken(final String channel, Callback<Void> callback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        ChangeDeviceToken changeDeviceToken = new ChangeDeviceToken(false, channel);

        DeviceTokenService deviceTokenService = restAdapter.create(DeviceTokenService.class);


        deviceTokenService.changeDeviceToken(changeDeviceToken, callback);
    }

    public interface DeviceTokenService {
        @POST(Constants.DEVICE_TOKEN_URI)
        void changeDeviceToken(
                @Body ChangeDeviceToken changeDeviceToken,
                Callback<Void> createCallback
        );

        @POST(Constants.DEVICE_TOKEN_URI)
        void createDeviceToken(
                @Body CreateDeviceToken createDeviceToken,
                Callback<Void> createCallback
        );
    }

}
