package is.yranac.canary.nativelibs.models.messages;

import org.json.JSONException;
import org.json.JSONObject;

import is.yranac.canary.nativelibs.models.messages.setup.BLESetupResponce;
import is.yranac.canary.nativelibs.models.messages.setup.BaseChirpMessage;
import is.yranac.canary.util.JSONUtil;
import is.yranac.canary.util.StringUtils;

/**
 * Created by sergeymorozov on 10/9/15.
 * This class wraps around messages going *FROM* a bluetooth device
 */
public class BLEMessageResponse {
    BaseChirpMessage message;

    public BLEMessageResponse(String jsonString) throws JSONException {
        this.message = createSetupMessageFromJSON(jsonString);
    }

    public BaseChirpMessage createSetupMessageFromJSON(String jsonString) throws JSONException {
        BaseChirpMessage message = null;
        JSONObject temp = new JSONObject(jsonString);
        String messageType = temp.get("ID").toString();

        if(StringUtils.isNullOrEmpty(messageType)) {
            throw new JSONException("This JSON object should contain a valid ID field");
        }

        message = new BLESetupResponce(messageType);

        Class<? extends BaseChirpMessage> classType = message.getTypeClass();
        message = classType.cast(JSONUtil.getObject(jsonString, classType));
        return message;
    }

    public BaseChirpMessage getMessage() {
        return message;
    }
}
