package is.yranac.canary.model.authentication;

import com.google.gson.annotations.SerializedName;

import is.yranac.canary.Constants;

/**
 * Created by cumisnic on 8/28/14.
 */
public class Oauth {

    @SerializedName("client_id")
    public String clientId = Constants.OAUTH_CLIENT_ID;         // a183323eab0544d83808

    @SerializedName("client_secret")
    public String clientSecret = Constants.OAUTH_CLIENT_SECRET; // ba883a083b2d45fa7c6a6567ca7a01e473c3a269

    @SerializedName("grant_type")
    public String grant_type = Constants.OAUTH_GRANT_TYPE;      // password

    @SerializedName("username")
    public String username;

    @SerializedName("password")
    public String password;

    @SerializedName("scope")
    public String scope = Constants.OAUTH_SCOPE;                // write

    public Oauth(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
