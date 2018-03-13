package is.yranac.canary.model.authentication;

import com.google.gson.annotations.SerializedName;

import is.yranac.canary.Constants;

/**
 * Created by Schroeder on 8/16/16.
 */
public class OauthRefresh {

    @SerializedName("client_id")
    public String clientId = Constants.OAUTH_CLIENT_ID;

    @SerializedName("client_secret")
    public String clientSecret = Constants.OAUTH_CLIENT_SECRET;

    @SerializedName("grant_type")
    public String grant_type = Constants.OAUTH_GRANT_TYPE_REFRESH;
}
