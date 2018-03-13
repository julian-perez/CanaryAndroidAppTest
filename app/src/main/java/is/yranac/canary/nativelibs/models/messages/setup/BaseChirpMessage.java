package is.yranac.canary.nativelibs.models.messages.setup;

import com.google.gson.annotations.SerializedName;

import is.yranac.canary.util.JSONUtil;

/**
 * Created by sergeymorozov on 10/8/15.
 */
public abstract class BaseChirpMessage {

    @SerializedName("ID")
    String ID;

    public BaseChirpMessage(String stepID) {
        this.ID = stepID;
    }

    public String toJSONString() {
        return JSONUtil.getJSONString(this);
    }

    public abstract Class<? extends BaseChirpMessage> getTypeClass();
}
