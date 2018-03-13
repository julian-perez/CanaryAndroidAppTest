package is.yranac.canary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import is.yranac.canary.util.BaseConstants;

public class Constants extends BaseConstants {

    public static final String BASE_URL_ENDPOINT = "api-stage.canaryis.com";

    public static final String BASE_URL = BASE_URL_HTTP + BASE_URL_ENDPOINT;


    public static final String OAUTH_CLIENT_ID = "";
    public static final String OAUTH_CLIENT_SECRET = "";


    public static final String API_VERSION = "/v1";

    public static final String URBAN_AIRSHIP_DEV_KEY = "7GuVKjnHSQyI1rn1UjXtjg";
    public static final String URBAN_AIRSHIP_DEV_SECRET = "MoFwITIlQ52uLyeaxjYO9g";

    public static final String URBAN_AIRSHIP_PRODUCTION_KEY = "ETiwaWs7T--Ozr5dt2Qq3w";
    public static final String URBAN_AIRSHIP_PRODUCTION_SECRET = "-367K5xESW22ED1Yi_PIEw";

    public static final String GCM_SENDER_ID = "272710921317";

    public static final boolean REMOTE_LOGGING_ENABLED = true;

    public static final String CANARY_MANAGE_PLAN = "https://webapp.stage.canaryis.com/";
    public static final String CANARY_WEBSITE = "https://website.stage.canaryis.com ";

    public static final String GOOGLE_API_KEY = "AIzaSyAq0abZ2pL5kRQ8hMIO10KPcLDMfHGXs_g";

    public static final boolean ZENDESK_TESTING = true;

    public static final List<String> extraTags = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("No motion");
                add("Night vision false alarm");
            }});
}
