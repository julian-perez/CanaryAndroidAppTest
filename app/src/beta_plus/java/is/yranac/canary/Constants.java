package is.yranac.canary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import is.yranac.canary.util.BaseConstants;

public class Constants extends BaseConstants {



    public static final String OAUTH_CLIENT_ID = "53d9c8b131892d41f02c";
    public static final String OAUTH_CLIENT_SECRET = "865f5bd097fe955df49722a94019bf9758b09210";

    public static final String BASE_URL_ENDPOINT = "api-dev2.canaryis.com";
    public static final String BASE_URL          = BASE_URL_HTTP + BASE_URL_ENDPOINT;


    public static final String URBAN_AIRSHIP_DEV_KEY = "5tDRVBQGQJeWBuY53w3CWw";
    public static final String URBAN_AIRSHIP_DEV_SECRET = "Uu1SeuOBR1SCzzPozyYWHQ";

    public static final String URBAN_AIRSHIP_PRODUCTION_KEY = "aEodHyQUQYK_AMOvbVPrww";
    public static final String URBAN_AIRSHIP_PRODUCTION_SECRET = "FyIG3U3wSdCuhHjkT_ecDg";

    public static final String GCM_SENDER_ID = "996512258821";

    public static final String GOOGLE_API_KEY = "AIzaSyAVeR4MaqUeQM0qa1ARYJtcDfFg1DqDK8U";

    public static final boolean ZENDESK_TESTING = true;

    public static final String ZENDESK_URL  = "https://canarybeta.zendesk.com";
    public static final String ZENDESK_TOKEN  = "8d88c2d54a6c45b1d091920eec33feb2aeb507e195e65fe1";
    public static final String ZENDESK_CLIENT = "mobile_sdk_client_fe748849477569851f90";

    public static final List<String> extraTags = Collections.unmodifiableList(
            new ArrayList<String>() {{
            }});
}
