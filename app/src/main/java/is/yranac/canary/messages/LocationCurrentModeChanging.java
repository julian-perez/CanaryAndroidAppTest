package is.yranac.canary.messages;

/**
 * Created by Schroeder on 5/18/16.
 */

public class LocationCurrentModeChanging {
    public boolean isChanging;
    public String mode;

    public LocationCurrentModeChanging(boolean isChanging, String mode) {
        this.isChanging = isChanging;
        this.mode = mode;
    }
}