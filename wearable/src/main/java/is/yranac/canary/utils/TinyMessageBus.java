package is.yranac.canary.utils;

import de.halfbit.tinybus.TinyBus;
import is.yranac.canary.CanaryWearApplication;
import is.yranac.canary.ui.MainActivity;

/**
 * Created by narendramanoharan on 6/28/16.
 */
public class TinyMessageBus {

    private static TinyBus tinyBus;

    public static TinyBus getTinyBus() {
        if (tinyBus == null)
            tinyBus = TinyBus.from(CanaryWearApplication.getContext());
        return tinyBus;
    }

    public static void post(Object event) {
        getTinyBus().post(event);
    }

    public static void register(Object object) {
        if (!hasRegistered(object)) {
            getTinyBus().register(object);
        }
    }

    public static void unregister(Object object) {
        if (hasRegistered(object)) {
            getTinyBus().unregister(object);
        }
    }

    public static boolean hasRegistered(Object object) {
        return getTinyBus().hasRegistered(object);
    }

    public static void postDelayed(Object object, int i) {
        getTinyBus().postDelayed(object, i);
    }

    public static void cancel(Class<?> eventClass) {
        getTinyBus().cancelDelayed(eventClass);
    }
}
