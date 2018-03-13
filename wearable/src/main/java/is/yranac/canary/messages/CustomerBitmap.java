package is.yranac.canary.messages;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by narendramanoharan on 6/28/16.
 */
public class CustomerBitmap {

    public Bitmap bitmap;
    public ImageView imageView;

    public CustomerBitmap(Bitmap bitmap, ImageView imageView) {
        this.bitmap = bitmap;
        this.imageView = imageView;
    }

}
