package is.yranac.canary.messages;

/**
 * Created by sergeymorozov on 11/29/16.
 */

public class RefreshOptionButtons {
    public Boolean enableDelete;
    public Boolean enableSave;
    public Boolean enableAdd;

    public RefreshOptionButtons(Boolean enableDelete, Boolean enableSave, Boolean enableAdd) {
        this.enableDelete = enableDelete;
        this.enableSave = enableSave;
        this.enableAdd = enableAdd;
    }
}
