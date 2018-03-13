package is.yranac.canary.util.cache.location;

/**
 * Created by Schroeder on 5/10/16.
 */
public class SyncLocationMode {
    public final String mode;
    public final int id;
    public boolean isPrivate;

    public SyncLocationMode(int id, String mode, boolean isPrivate) {
        this.id = id;
        this.mode = mode;
        this.isPrivate = isPrivate;
    }

}
