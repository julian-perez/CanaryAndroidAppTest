package is.yranac.canary.nativelibs.models.messages.setup;

/**
 * Created by sergeymorozov on 10/27/15.
 */
public class BLESetupResponce extends BaseChirpMessage {

    public BLESetupResponce(String stepID) {
        super(stepID);
    }

    @Override
    public Class<? extends BaseChirpMessage> getTypeClass() {
        if(ID.equals("6"))
            return BLESetupAuthCredsResponse.class;
        else if (ID.equals("2"))
            return BLESetupInitResponse.class;
        else if (ID.equals("11"))
            return BLESetupSSIDResponse.class;
        else if (ID.equals("4"))
            return BLESetupWifiCredsResponse.class;
        else {
            return this.getClass();
        }
    }
}
