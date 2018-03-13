package is.yranac.canary.model.thumbnail;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 8/15/14.
 */
public class Thumbnail {


    public long id;

    @SerializedName("image_url")
    public String imageUrl;

    @SerializedName("device")
    public String device;

    @SerializedName("resource_uri")
    public String resourceUri;

    public  int deviceId;

    @SerializedName("entry")
    public  String entry;

    public String imageUrl() {

        if (Utils.isDemo()) {

            Context context = CanaryApplication.getContext();

            int resourceId = context.getResources().getIdentifier(imageUrl, "drawable",
                    context.getPackageName());


            return "drawable://" + resourceId;
        }
        return imageUrl;
    }

    public long getEntryId() {
        return Utils.getLongFromResourceUri(entry);
    }
}
