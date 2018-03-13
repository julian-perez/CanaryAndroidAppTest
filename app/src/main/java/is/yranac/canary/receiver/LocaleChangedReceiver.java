package is.yranac.canary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import is.yranac.canary.services.database.BaseDatabaseService;

/**
 * Created by Schroeder on 1/29/16.
 */
public class LocaleChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        BaseDatabaseService.purgeDatabase();
    }
}
