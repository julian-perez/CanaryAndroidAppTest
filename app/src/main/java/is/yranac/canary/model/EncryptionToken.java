package is.yranac.canary.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sergeymorozov on 10/27/15.
 */
public class EncryptionToken {
    @SerializedName("value")
    public String value;

    @SerializedName("ecdh_symkey")
    public String symKey;

    @SerializedName("ecdh_temppub")
    public String tempPub;
}
