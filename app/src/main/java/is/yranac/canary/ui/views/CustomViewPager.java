package is.yranac.canary.ui.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

    private boolean isPagingEnabled;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isPagingEnabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 0) {
            return false;
        }
        boolean valid = false;
        try {
            valid = this.isPagingEnabled && super.onTouchEvent(event);
        } catch (Exception ignored) {
        }
        return valid;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        if (this.isPagingEnabled) {
            boolean valid = false;
            try {
                valid = super.onInterceptTouchEvent(event);
            } catch (Exception ignored) {
            }
            return valid;
        }

        return false;
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}