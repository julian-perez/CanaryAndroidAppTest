package is.yranac.canary.util;

import android.content.Context;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.Constants;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.entry.Entry;
import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.util.keystore.KeyStoreHelper;

/**
 * Created by Schroeder on 12/21/15.
 */
public class UrlUtils {
    private static final String LOG_TAG = "UrlUtils";


    public static String buildEntryDeviceClipUrl(Entry entry, String deviceId, Thumbnail thumbnail) {


        if (Utils.isDemo()) {

            Context context = CanaryApplication.getContext();
            Log.i(LOG_TAG, thumbnail.imageUrl);
            int resourceId = context.getResources().getIdentifier(thumbnail.imageUrl, "raw",
                    context.getPackageName());
            String path = "android.resource://" + context.getPackageName() + "/" + resourceId;
            Log.i(LOG_TAG, path);

            return path;
        }
        StringBuilder builder = new StringBuilder(Constants.BASE_URL_HTTP);
        builder.append(Constants.BASE_URL_ENDPOINT);
        builder.append(Constants.PATH_ENTRY_MANIFEST);
        builder.append(entry.id);
        builder.append("&oauth_consumer_key=");
        builder.append(KeyStoreHelper.getToken());

        if (deviceId != null) {
            builder.append("&device=");
            builder.append(deviceId);
        }

        builder.append("&format=m3u8");

        Log.i(LOG_TAG, builder.toString());

        return builder.toString();
    }

    public static String buildStreamingUrl(Device device) {

        if (Utils.isDemo()) {
            Context context = CanaryApplication.getContext();
            int resourceId = context.getResources().getIdentifier(device.imageUrl, "raw",
                    context.getPackageName());
            return "android.resource://" + context.getPackageName() + "/" + resourceId;
        }
        StringBuilder builder = new StringBuilder(Constants.BASE_URL_HTTP);
        builder.append(Constants.BASE_URL_ENDPOINT);
        builder.append(Constants.PATH_MANIFEST);
        builder.append(device.id);
        builder.append("&oauth_consumer_key=");
        builder.append(KeyStoreHelper.getToken());
        builder.append("&format=m3u8");
        Log.i(LOG_TAG, builder.toString());

        return builder.toString();
    }
}
