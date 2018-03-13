package is.yranac.canary.services.intent;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.api.EmergencyContctsAPIService;
import is.yranac.canary.services.api.MembershipAPIService;
import is.yranac.canary.services.api.SubscriptionAPIService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.util.keystore.KeyStoreHelper;
import retrofit.RetrofitError;

/**
 * Created by Schroeder on 3/21/16.
 */
public class APIIntentServiceAppOpen extends IntentService {

    private static final String TAG = "APIIntentServiceAppOpen";
    private static final String LOG_TAG = TAG;

    public static void refresh(Context context) {

        Context appContext = context.getApplicationContext();
        Intent intent = new Intent(appContext, APIIntentServiceAppOpen.class);
        context.startService(intent);
    }


    public APIIntentServiceAppOpen() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (!KeyStoreHelper.hasGoodOauthToken()) {
            return;
        }
        for (Location location : LocationDatabaseService.getLocationList()) {

            try {
                SubscriptionAPIService.getSubscription(location.id);
            } catch (RetrofitError ignored) {
                ignored.printStackTrace();
            }

            try {
                EmergencyContctsAPIService.getEmergencyContactsForLocation(location.id);
            } catch (RetrofitError ignored) {
                ignored.printStackTrace();
            }

            try {
                MembershipAPIService.getLocationMemberships(getApplicationContext(), location.id);
            } catch (RetrofitError ignored) {
                ignored.printStackTrace();
            }


        }

        String folder = "/videos/";
        File direct = new File(getApplicationContext().getFilesDir()
                + folder);


        if (!direct.exists())
            return;

        for (File f : direct.listFiles()) {
            if (new Date().getTime() - f.lastModified() > TimeUnit.HOURS.toMillis(1)) {
                f.delete();
            }
        }

    }
}
