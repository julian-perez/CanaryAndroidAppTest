package is.yranac.canary.util;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

import java.lang.ref.WeakReference;

public class CustomAsyncHandler extends AsyncQueryHandler {

    private static final String LOG_TAG = "CustomAsyncHandler";
    private WeakReference<AsyncQueryListener> mListener;

    public interface AsyncQueryListener {
        void onQueryComplete(int token, Object cookie, Cursor cursor);
    }

    public CustomAsyncHandler(ContentResolver cr, AsyncQueryListener listener) {
        super(cr);
        mListener = new WeakReference<>(listener);
    }

    /** {@inheritDoc} */
    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        final AsyncQueryListener listener = mListener.get();
        if (listener != null) {
            listener.onQueryComplete(token, cookie, cursor);
        } else if (cursor != null) {
            cursor.close();
        }
    }

}