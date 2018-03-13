package is.yranac.canary.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created by Nick on 9/29/14.
 */
public class CanaryHorizontalScrollView extends HorizontalScrollView {
    private boolean isScrollEnabled = true;

    public CanaryHorizontalScrollView(Context context) {
        super(context);
    }

    public CanaryHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CanaryHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public interface ScrollListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);

        void onScrollStart();

        void onScrollEnd();
    }

    ScrollListener scrollListener;

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (lastScrollUpdate == -1 && scrollListener != null) {
            scrollListener.onScrollStart();
            postDelayed(new ScrollStateHandler(), 100);
        }

        if (scrollListener != null) {
            scrollListener.onScrollChanged(l, t, oldl, oldt);
        }


        lastScrollUpdate = System.currentTimeMillis();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return !isScrollEnabled || super.onTouchEvent(ev);

    }

    public void setScrollEnabled(boolean isScrollEnabled) {
        this.isScrollEnabled = isScrollEnabled;
    }

    private long lastScrollUpdate = -1;

    private class ScrollStateHandler implements Runnable {

        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastScrollUpdate) > 100) {
                lastScrollUpdate = -1;
                scrollListener.onScrollEnd();
            } else {
                postDelayed(this, 100);
            }
        }
    }
}
