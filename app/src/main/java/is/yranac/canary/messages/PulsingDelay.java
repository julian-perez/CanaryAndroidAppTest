package is.yranac.canary.messages;

import android.view.View;

/**
 * Created by Schroeder on 12/16/15.
 */
public class PulsingDelay {

    public final View view;

    public final boolean up;

    public PulsingDelay(View view, boolean up){
        this.view = view;
        this.up = up;
    }
}
