package is.yranac.canary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import is.yranac.canary.util.BaseConstants;

public class Constants extends BaseConstants {

    public static final String BASE_URL_ENDPOINT = "api.canaryis.com";
    public static final String BASE_URL          = BASE_URL_HTTP + BASE_URL_ENDPOINT;

    public static final String OAUTH_CLIENT_ID = "a183323eab0544d83808";
    public static final String OAUTH_CLIENT_SECRET = "ba883a083b2d45fa7c6a6567ca7a01e473c3a269";

    public static final String URBAN_AIRSHIP_DEV_KEY = "8xEzfcPPQ2uvs0jBUE45GQ";
    public static final String URBAN_AIRSHIP_DEV_SECRET = "1fxZKL72RvSqIClzIt1W3Q";

    public static final String URBAN_AIRSHIP_PRODUCTION_KEY = "RVuZibfzQkemxzvPaOr_DQ";
    public static final String URBAN_AIRSHIP_PRODUCTION_SECRET = "DHZ5lpQlSp6K995pYbec8g";

    public static final String GCM_SENDER_ID = "825944461512";

    public static final String GOOGLE_API_KEY = "AIzaSyAVeR4MaqUeQM0qa1ARYJtcDfFg1DqDK8U";

    public static final boolean ZENDESK_TESTING = false;

    public static final String ZENDESK_URL  = "https://canary.zendesk.com";
    public static final String ZENDESK_TOKEN  = "b174a593dd778e66469a22b9ab21bf4839263bbd03318b3b";
    public static final String ZENDESK_CLIENT = "mobile_sdk_client_2bb404b045b8bf9859f5";

    public static final List<String> extraTags = Collections.unmodifiableList(
            new ArrayList<String>() {{
            }});
}
