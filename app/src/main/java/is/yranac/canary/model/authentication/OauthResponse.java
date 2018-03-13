package is.yranac.canary.model.authentication;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cumisnic on 8/28/14. <-- Fuck that guy
 */
public class OauthResponse {

    @SerializedName("access_token")
    public String accessToken;  // 88cd2f3d716f98708adb9c3c6a29b74e86985cbe

    @SerializedName("token_type")
    public String tokenType;    // Bearer

    @SerializedName("expires_in")
    public long expiresIn;      // 2591999

    @SerializedName("scope")
    public String scope;        // "read write read+write"

    @SerializedName("refresh_token")
    public String refreshToken;
}
