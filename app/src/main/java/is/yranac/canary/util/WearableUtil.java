package is.yranac.canary.util;

import android.app.Activity;
import android.graphics.Bitmap;

import com.google.android.gms.wearable.Asset;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by narendramanoharan on 7/13/16.
 */
public class WearableUtil extends Activity {

    private static final String TAG = "WearableUtil";

    public static Asset toAsset(Bitmap bitmap) {
        ByteArrayOutputStream byteStream = null;
        try {
            byteStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 25, byteStream);
            return Asset.createFromBytes(byteStream.toByteArray());
        } finally {
            if (null != byteStream) {
                try {
                    byteStream.close();
                } catch (IOException e) {
                    Log.i(TAG, "toAsset: IO Exception occured");
                }
            }
        }
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
