package is.yranac.canary;

import android.app.Application;
import android.content.Context;

/**
 * Created by michaelschroeder on 9/27/16.
 */
public class CanaryWearApplication extends Application {


    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

    }

    public static Context getContext() {
        return context;
    }

}
