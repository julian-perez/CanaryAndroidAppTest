package is.yranac.canary.services.jobs;

import android.os.Bundle;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import is.yranac.canary.util.Log;

abstract public class BaseJobService extends JobService {


    private static final String SCHEDULE = "-schedule";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(getClass().getSimpleName(), "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.i(getClass().getSimpleName(), "onDestroy");
        super.onDestroy();
    }


    public static void scheduleJob(Class<? extends JobService> serviceClass,
                                   String tag,
                                   FirebaseJobDispatcher dispatcher,
                                   Bundle bundle) {

        Job.Builder myJob2 = dispatcher.newJobBuilder()
                .setService(serviceClass)
                .setRecurring(true)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setTrigger(Trigger.executionWindow(55, 65))
                .setTag(tag + SCHEDULE)
                .setExtras(bundle)
                .setReplaceCurrent(true);
        dispatcher.mustSchedule(myJob2.build());
    }


}
