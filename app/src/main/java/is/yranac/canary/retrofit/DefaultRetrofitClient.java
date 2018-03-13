package is.yranac.canary.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import is.yranac.canary.BuildConfig;
import is.yranac.canary.CanaryApplication;
import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.model.authentication.OauthResponse;
import is.yranac.canary.services.api.OathAuthenticationAPIService;
import is.yranac.canary.services.oauth.OauthServiceLock;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ga.AnalyticsConstants;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import is.yranac.canary.util.keystore.KeyStoreHelper;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.OkClient;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * Created by sergeymorozov on 9/15/15.
 */
@SuppressLint("DefaultLocale")
public class DefaultRetrofitClient extends OkClient {

    public static Client getClient(boolean pinning) {
        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.setConnectTimeout(4, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        if (pinning) {

            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    HostnameVerifier hv =
                            HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify(Constants.BASE_URL_ENDPOINT, session);
                }
            });
            if (!Utils.isDev()) {
                okHttpClient.setSslSocketFactory(getCertificatePinner());
            }
        }

        okHttpClient.interceptors().add(interceptor);
        okHttpClient.setAuthenticator(authenticator);
        return new DefaultRetrofitClient(okHttpClient);
    }

    private static final String LOG_TAG = "DefaultRetrofitClient";
    private static Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            Request requestWithUserAgent = request.newBuilder()
                    .header("User-Agent", getUserAgent())
                    .build();
            Response response = chain.proceed(requestWithUserAgent);

            if (response.isSuccessful()) {
                tries = 0;
            }

            return response;
        }
    };

    private static int tries = 3;

    private static final String OAUTH_LOCK = "OAUTH_LOCK";
    private static Authenticator authenticator = new Authenticator() {
        @Override
        public Request authenticate(Proxy proxy, Response response) throws IOException {
            OauthServiceLock lock = new OauthServiceLock(OAUTH_LOCK);
            lock.lock();
            try {


                String header = response.request().header("Authorization");
                String currentOauth = "Bearer " + KeyStoreHelper.getToken();


                if (currentOauth.equalsIgnoreCase(header) && response.code() == HTTP_UNAUTHORIZED) {

                    try {

                        if (!KeyStoreHelper.hasRefreshToken()) {
                            return null;
                        }
                        OauthResponse oauthResponse = OathAuthenticationAPIService.oauthAuthenticationRefresh(KeyStoreHelper.getRefreshToken());
                        KeyStoreHelper.saveToken(oauthResponse.accessToken);
                        KeyStoreHelper.saveRefreshToken(oauthResponse.refreshToken);

                        tries = 0;

                    } catch (RetrofitError error) {


                        URL url = response.request().url();
                        if (error.getResponse().getStatus() == HttpURLConnection.HTTP_BAD_REQUEST) {
                            tries++;
                            String oauthCheck = "Bearer " + KeyStoreHelper.getToken();
                            if (oauthCheck.equalsIgnoreCase(currentOauth) && tries > 3) {
                                GoogleAnalyticsHelper.trackEvent(AnalyticsConstants.OUATH_CATEGORY, AnalyticsConstants.OUATH_ACTION + error.getResponse().getStatus(), url.getPath());
                                PreferencesUtils.removeUser();
                                return null;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ignored) {
                            }
                        }
                    }
                }

                return response.request().newBuilder()
                        .removeHeader("Authorization")
                        .addHeader("Authorization", "Bearer " + KeyStoreHelper.getToken())
                        .build();

            } finally {
                lock.release();
            }
        }

        @Override
        public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
            // Null indicates no attempt to authenticate.
            return null;
        }
    };

    public static String getUserAgent() {
        return "Android " + Build.VERSION.RELEASE + " " + BuildConfig.VERSION_NAME + " " + Build.MODEL;
    }

    public DefaultRetrofitClient(OkHttpClient httpClient) {
        super(httpClient);
    }

    private static SSLSocketFactory getCertificatePinner() {

        try {
            Context context = CanaryApplication.getContext();
            InputStream in1 = context.getResources().openRawResource(R.raw.canary_cert);
            InputStream in2 = context.getResources().openRawResource(R.raw.canary_cert_2);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            Certificate ca1;
            Certificate ca2;
            try {
                ca1 = cf.generateCertificate(in1);
                ca2 = cf.generateCertificate(in2);
            } finally {
                in1.close();
                in2.close();
            }


            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca1", ca1);
            keyStore.setCertificateEntry("ca2", ca2);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
