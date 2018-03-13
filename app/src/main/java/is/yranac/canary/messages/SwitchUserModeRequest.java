package is.yranac.canary.messages;

import is.yranac.canary.model.MaskingViewController.UserMode;

/**
 * Created by sergeymorozov on 11/29/16.
 */

public class SwitchUserModeRequest {
    public UserMode requestedMode;

    public SwitchUserModeRequest(UserMode requestedMode) {
        this.requestedMode = requestedMode;
    }
}
