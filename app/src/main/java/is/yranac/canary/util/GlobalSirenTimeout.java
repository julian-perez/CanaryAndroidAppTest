package is.yranac.canary.util;

import android.os.Handler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Schroeder on 10/6/14.
 */
public class GlobalSirenTimeout {

    private static final String LOG_TAG = "GlobalSirenTimeout";

    public interface SirenListener {
        void onTimeUpdate(int intervalsLeft);
    }

    private List<WeakReference<SirenListener>> sirenListeners = new ArrayList<>();
    private int locationId;

    private static int intervalsRemaining = 0;

    private final Handler sirenHandler = new Handler();

    private Runnable sirenRunnable = new Runnable() {
        @Override
        public void run() {

            intervalsRemaining--;

            for (WeakReference<SirenListener> sirenListenerWeakReference : sirenListeners) {
                SirenListener sirenListener = sirenListenerWeakReference.get();
                if (sirenListener != null)
                    sirenListener.onTimeUpdate(intervalsRemaining >= 0 ? intervalsRemaining : 0);
            }

            if (intervalsRemaining > 0) {
                sirenHandler.postDelayed(sirenRunnable, (long) ((1.0f / 12.0f) * 1000));
            } else {
                sirenListeners.clear();
            }

        }
    };

    private static GlobalSirenTimeout globalSirenTimeout;

    public static GlobalSirenTimeout getInstance() {

        if (globalSirenTimeout == null)
            globalSirenTimeout = new GlobalSirenTimeout();

        return globalSirenTimeout;
    }

    public boolean isSirenCountingDown() {
        return intervalsRemaining > 0;
    }

    public void restartTimeout(SirenListener sirenListener) {

        if (sirenListeners == null) {
            sirenListeners = new ArrayList<>();
        } else {
            sirenListeners.clear();
        }

        sirenListeners.add(new WeakReference<>(sirenListener));
        intervalsRemaining = 360;

        sirenHandler.postDelayed(sirenRunnable, 0);
    }

    public void setLocationId(int locationId){
        this.locationId = locationId;
    }

    public int getLocationId(){
        return locationId;
    }
    public void stopTimeOut() {
        intervalsRemaining = 0;
        sirenHandler.removeCallbacks(sirenRunnable);
    }

    public void addSirenListener(SirenListener sirenListener) {
        sirenListeners.add(new WeakReference<>(sirenListener));
    }

    public void removeSirenListener(SirenListener sirenListener) {
        for (Iterator<WeakReference<SirenListener>> iterator = sirenListeners.iterator();
             iterator.hasNext(); ) {
            WeakReference<SirenListener> weakRef = iterator.next();
            if (weakRef.get() == sirenListener) {
                iterator.remove();
            }
        }
    }

}
