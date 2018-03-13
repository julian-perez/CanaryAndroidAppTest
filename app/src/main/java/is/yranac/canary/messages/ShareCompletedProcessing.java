package is.yranac.canary.messages;

/**
 * Created by Schroeder on 4/4/16.
 */
public class ShareCompletedProcessing {

    public final long entryId;

    public ShareCompletedProcessing(long entryId){
        this.entryId = entryId;
    }
}
