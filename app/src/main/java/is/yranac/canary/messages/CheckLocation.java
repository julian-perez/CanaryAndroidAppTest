package is.yranac.canary.messages;

/**
 * Created by michaelschroeder on 5/11/17.
 */

public class CheckLocation {

    public final int location;
    public final boolean onClick;

    public CheckLocation(int location, boolean onClick) {

        this.onClick = onClick;
        this.location = location;
    }
}
