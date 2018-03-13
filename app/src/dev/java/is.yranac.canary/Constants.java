package is.yranac.canary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import is.yranac.canary.util.BaseConstants;

public class Constants extends BaseConstants {
    public static String BASE_URL_ENDPOINT = "api-dev.canaryis.com";

    public static String BASE_URL = BASE_URL_HTTP + BASE_URL_ENDPOINT;

    public static final String OAUTH_CLIENT_ID = "53d9c8b131892d41f02c";
    public static final String OAUTH_CLIENT_SECRET = "865f5bd097fe955df49722a94019bf9758b09210";

    public static final String API_VERSION = "/v1";

    public static final String URBAN_AIRSHIP_DEV_KEY = "uB_51aJPTriJ_VPtIZs11Q";
    public static final String URBAN_AIRSHIP_DEV_SECRET = "s6Xhu3R2QnK8JqAuOav5lw";

    public static final String URBAN_AIRSHIP_PRODUCTION_KEY = "dcvC4qX4Rtes5p5xLN8yOA";
    public static final String URBAN_AIRSHIP_PRODUCTION_SECRET = "GT_AFk6HTUG_kV8lqUufbw";
    public static final String GCM_SENDER_ID = "985786538410";

    public static final boolean REMOTE_LOGGING_ENABLED = true;

    public static final String CANARY_MANAGE_PLAN = "https://webapp.dev.canaryis.com/";
    public static final String CANARY_WEBSITE = "https://website.dev.canaryis.com/";

    public static final String GOOGLE_API_KEY = "AIzaSyCyEHT_MYWxbYv2nXnti8AwFFwGLG5_gTI";

    public static final boolean ZENDESK_TESTING = true;


    public static final String ZENDESK_URL  = "https://canary.zendesk.com";
    public static final String ZENDESK_TOKEN  = "b174a593dd778e66469a22b9ab21bf4839263bbd03318b3b";
    public static final String ZENDESK_CLIENT = "mobile_sdk_client_2bb404b045b8bf9859f5";

    public static final List<String> extraTags = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("No motion");
                add("Night vision false alarm");
            }});


    public static void updateEndPoint(String url) {
        BASE_URL_ENDPOINT = url;
        BASE_URL = BASE_URL_HTTP + BASE_URL_ENDPOINT;
    }

}
