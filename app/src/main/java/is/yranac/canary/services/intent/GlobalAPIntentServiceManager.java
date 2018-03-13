package is.yranac.canary.services.intent;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;

import is.yranac.canary.services.jobs.APIEntryJobService;
import is.yranac.canary.services.jobs.APILocationJobService;
import is.yranac.canary.services.jobs.APIReadingJobService;

/**
 * Created by Schroeder on 10/8/14.
 */

public class GlobalAPIntentServiceManager {

    public static void cancelAlarms(@NonNull Context context) {
        Context appContext = context.getApplicationContext();
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(appContext));
        dispatcher.cancelAll();
    }

    public static void resetAlarms(@NonNull Context context) {
        Context appContext = context.getApplicationContext();
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(appContext));
        APILocationJobService.rescheduleIntent(appContext);
        APIEntryJobService.rescheduleIntent(dispatcher);
        APIReadingJobService.rescheduleIntent(dispatcher);
        APIIntentServiceAppOpen.refresh(context);
    }
}
