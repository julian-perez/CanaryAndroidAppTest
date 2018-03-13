package is.yranac.canary.nativelibs.models.messages.setup;

/**
 * Created by sergeymorozov on 10/27/15.
 */
public class BLESetupRequest extends BaseChirpMessage {
    public BLESetupRequest(String stepID) {
        super(stepID);
    }

    @Override
    public Class<? extends BaseChirpMessage> getTypeClass() {
        return this.getClass();
    }
}
