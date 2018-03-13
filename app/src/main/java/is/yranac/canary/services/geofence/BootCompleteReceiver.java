package is.yranac.canary.services.geofence;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import is.yranac.canary.util.keystore.KeyStoreHelper;

public class BootCompleteReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (KeyStoreHelper.hasGoodOauthToken()) {
            SetUpGeofence.startService(context.getApplicationContext());
        }
    }
}
