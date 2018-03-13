package is.yranac.canary.ui.views;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class DraggableHeightLayout extends RelativeLayout {

    private int startY = 0;
    private boolean mIsDragging = false;
    private float mTouchSlop;
    private boolean draggable = true;

    public DraggableHeightLayout(Context context) {
        super(context);
    }

    public DraggableHeightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DraggableHeightLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setTouchSlop(float touchSlop) {
        mTouchSlop = touchSlop;
    }

    public void setIsDragging(boolean isDragging) {
        mIsDragging = isDragging;
    }

    public boolean getIsDragging() {
        return mIsDragging;
    }


    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public boolean isDraggable() {
        return draggable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!draggable)
            return false;

//        Log.d("DraggableHeightLayout", "mIsDragging: " + mIsDragging +
//              " startY: " + startY + " currentY: " + ev.getY());
        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onTouchEvent will be called and we do the actual
         * scrolling there.
         */
        final int action = MotionEventCompat.getActionMasked(ev);

        // Always handle the case of the touch gesture being complete.
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            // Release the scroll
            mIsDragging = false;
            return false; // Do not intercept touch event, let the child handle it
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                startY = (int) ev.getY();
            }
            case MotionEvent.ACTION_MOVE: {
                if (mIsDragging) {
                    // We're currently scrolling, so intercept the touch event!
                    return true;
                }

                // If the user has dragged her finger vertically more than
                // the touch slop, start the scroll
                final int yDiff = Math.abs((int) ev.getY() - startY);

//                Log.d("DraggableHeightLayout", "DragTesting: startY: " + startY + " currentY: " + ev.getY() + " yDiff: " + yDiff + " mTouchSlop: " + mTouchSlop);

                if (yDiff > mTouchSlop) {
                    // Start scrolling!
                    mIsDragging = true;
                    return true;
                }
                break;
            }
        }

        // In general, we don't want to intercept touch events. They should be
        // handled by the child view.
        return false;
    }
}