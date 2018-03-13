package is.yranac.canary.util;

import android.graphics.Bitmap;

import com.google.android.gms.wearable.Asset;

import org.junit.Assert;
import org.junit.Test;

import static is.yranac.canary.util.WearableUtil.getResizedBitmap;
import static is.yranac.canary.util.WearableUtil.toAsset;

/**
 * Created by michaelschroeder on 1/3/17.
 */

public class WearableUtilTest {

    @Test
    public void testToAsset(){
        int w = 200, h = 2000;

        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bitmap = Bitmap.createBitmap(w, h, conf);

        Asset asset = toAsset(bitmap);
        Assert.assertNotNull(asset);

    }

    @Test
    public void testGetResizedBitmap(){
        int w = 200, h = 2000;

        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bitmap = Bitmap.createBitmap(w, h, conf);

        Bitmap newBitmap = getResizedBitmap(bitmap, 100);
        Assert.assertNotNull(newBitmap);

        Assert.assertEquals(newBitmap.getWidth(), 100);


    }
}
