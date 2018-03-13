package is.yranac.canary.nativelibs.models.messages;

import is.yranac.canary.nativelibs.models.messages.setup.BaseChirpMessage;

/**
 * Created by sergeymorozov on 10/9/15.
 * This class wraps around messages going *TO* a bluetooth device.
 * Need this class because TinyBus doesn't support message inheritance.
 */
public class BLEMessageRequest {
    BaseChirpMessage message;
    public BLEMessageRequest(BaseChirpMessage message){
        this.message = message;
    }
    public BaseChirpMessage getMessage(){
        if (message == null)
            return  null;
        return message;
    }
    public String getJsonString(){
        if(message == null)
            return null;
        return message.toJSONString();
    }
}
