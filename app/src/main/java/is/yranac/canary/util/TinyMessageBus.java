package is.yranac.canary.util;

import de.halfbit.tinybus.TinyBus;
import is.yranac.canary.CanaryApplication;

/**
 * Created by Schroeder on 8/3/15.
 */
public class TinyMessageBus {

    private static TinyBus tinyBus;

    public static TinyBus getTinyBus() {
        if (tinyBus == null)
            tinyBus = TinyBus.from(CanaryApplication.getContext());
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

    public static void postDelayed(Object object, long i) {
        getTinyBus().postDelayed(object, i);
    }

    public static void cancel(Class<?> eventClass) {
        getTinyBus().cancelDelayed(eventClass);
    }
}
