package is.yranac.canary.model.avatar;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.util.Utils;

/**
 * Created by Schroeder on 8/8/14.
 */
public class Avatar {

    @SerializedName("customer")
    public String customer;

    @SerializedName("id")
    public int id;

    @SerializedName("image")
    public String image;

    @SerializedName("resource_uri")
    public String resourceUri;

    @SerializedName("thumbnail_url")
    public String thumbnailUrl;

    public String thumbnailUrl(){

        if (Utils.isDemo()){

            Context context = CanaryApplication.getContext();
            int resourceId = context.getResources().getIdentifier(thumbnailUrl, "drawable",
                    context.getPackageName());

            return "drawable://" + resourceId;
        }
        return thumbnailUrl;
    }
}
