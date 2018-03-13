package is.yranac.canary.messages;

/**
 * Created by Schroeder on 6/8/16.
 */
public class ShareStartedProcessing {

    public final long entryId;
    public ShareStartedProcessing(long entryId) {
        this.entryId = entryId;
    }
}
