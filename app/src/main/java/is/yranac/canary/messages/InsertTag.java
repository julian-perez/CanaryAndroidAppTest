package is.yranac.canary.messages;

/**
 * Created by Schroeder on 1/13/16.
 */
public class InsertTag {
    public String tag;
    public boolean override;

    public InsertTag(String tag) {
        this.tag = tag;
        this.override = false;
    }

    public InsertTag(String tag, boolean override) {

        this.tag = tag;
        this.override = override;
    }
}
