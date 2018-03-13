package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sergeymorozov on 12/4/15.
 */
public class EncryptionTokenResponse {
    @SerializedName("meta")
    public Meta meta;

    @SerializedName("objects")
    public List<EncryptionToken> memberships;
}
