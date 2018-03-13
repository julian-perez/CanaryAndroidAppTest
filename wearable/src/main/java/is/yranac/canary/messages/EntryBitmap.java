package is.yranac.canary.messages;

import android.graphics.Bitmap;

/**
 * Created by narendramanoharan on 6/30/16.
 */
public class EntryBitmap {

    Bitmap bitmap;
    public EntryBitmap(Bitmap entryBitmap) {
        this.bitmap = entryBitmap;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }
}
