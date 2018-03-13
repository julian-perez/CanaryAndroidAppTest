package is.yranac.canary.services.api;

import is.yranac.canary.Constants;
import is.yranac.canary.model.authentication.Oauth;
import is.yranac.canary.model.authentication.OauthRefresh;
import is.yranac.canary.model.authentication.OauthResponse;
import is.yranac.canary.model.authentication.OauthUpdate;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Schroeder on 8/8/14.
 */
public class OathAuthenticationAPIService {


    public static void oauthAuthentication(String username, String password, Callback<OauthResponse> callback) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getAuthenticationAdapter();

        OauthService oauthService = restAdapter.create(OauthService.class);
        oauthService.authenticate(Constants.OAUTH_GRANT_TYPE, Constants.OAUTH_CLIENT_ID,
                Constants.OAUTH_CLIENT_SECRET, username, password, Constants.OAUTH_SCOPE, callback);
    }


    public static OauthResponse oauthAuthenticationRefresh(String refreshToken) {
        RestAdapter restAdapter = RetroFitAdapterFactory.getAuthenticationAdapter();

        OauthService oauthService = restAdapter.create(OauthService.class);
        return oauthService.reAuthenticate(Constants.OAUTH_GRANT_TYPE_REFRESH, Constants.OAUTH_CLIENT_ID,
                Constants.OAUTH_CLIENT_SECRET, refreshToken);
    }

    public static OauthResponse oauthAuthenticationUpdate(String oldToken) throws RetrofitError {
        RestAdapter restAdapter = RetroFitAdapterFactory.getAuthenticationAdapter();

        OauthService oauthService = restAdapter.create(OauthService.class);
        return oauthService.updateAuthenticate(Constants.OAUTH_GRANT_TYPE_UPDATE, Constants.OAUTH_CLIENT_ID,
                Constants.OAUTH_CLIENT_SECRET, oldToken);
    }


    public interface OauthService {
        @FormUrlEncoded
        @POST("/access_token/")
        void authenticate(
                @Field("grant_type") String grant_type,
                @Field("client_id") String client_id,
                @Field("client_secret") String client_secret,
                @Field("username") String username,
                @Field("password") String password,
                @Field("scope") String scope,
                Callback<OauthResponse> callback);

        @FormUrlEncoded
        @POST("/access_token/")
        OauthResponse reAuthenticate(
                @Field("grant_type") String grant_type,
                @Field("client_id") String client_id,
                @Field("client_secret") String client_secret,
                @Field("refresh_token") String password);

        @FormUrlEncoded
        @POST("/access_token/")
        OauthResponse updateAuthenticate(
                @Field("grant_type") String grant_type,
                @Field("client_id") String client_id,
                @Field("client_secret") String client_secret,
                @Field("access_token") String password);

    }


}
