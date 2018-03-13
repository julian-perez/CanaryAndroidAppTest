package is.yranac.canary.messages.tutorial;

/**
 * Created by sergeymorozov on 9/15/16.
 */
public class SetUpDeviceViewsForTutorialRequest {
    private int positionToSelect;

    public SetUpDeviceViewsForTutorialRequest(int positionToSelect) {
        this.positionToSelect = positionToSelect;
    }

    public int getPositionToSelect() {
        return positionToSelect; 
    }
}
