package is.yranac.canary.messages;

import android.widget.ImageView;

import com.google.android.gms.wearable.Asset;

/**
 * Created by narendramanoharan on 6/28/16.
 */
public class CustomerAsset {

    public Asset asset;
    public ImageView imageView;

    public CustomerAsset(Asset asset, ImageView imageView) {
        this.asset = asset;
        this.imageView = imageView;
    }


}
