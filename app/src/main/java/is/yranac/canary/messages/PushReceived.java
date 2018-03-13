package is.yranac.canary.messages;

/**
 * Created by Schroeder on 4/8/15.
 */
public class PushReceived {

    private static final String LOG_TAG = "PushReceived";
    public String entryId;
    public String title;
    public String entryType;
    public int videoExportDuration;
    public int videoExportSize;

    public PushReceived(String entryId, String title, String entryType) {
        this.entryId = entryId;
        this.title = title;
        this.entryType = entryType;
    }

    public PushReceived(String entryId, String title, String entryType, int videoExportDuration, int videoExportSize) {
        this.entryId = entryId;
        this.title = title;
        this.entryType = entryType;
        this.videoExportDuration = videoExportDuration;
        this.videoExportSize = videoExportSize;
    }


}
