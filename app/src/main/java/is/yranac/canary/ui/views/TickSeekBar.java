package is.yranac.canary.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.SeekBar;

import is.yranac.canary.R;
import is.yranac.canary.util.DensityUtil;

/**
 * Created by Schroeder on 10/1/15.
 */
public class TickSeekBar extends SeekBar {

    private Paint p = new Paint();
    private int initialValue;

    public TickSeekBar(Context context) {
        super(context);
    }

    public TickSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TickSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setInitialValue(int value) {
        this.initialValue = 10 - value;
    }

    public int getInitialValue() {
        return 10 - this.initialValue;
    }

    @Override
    public void setProgress(int progress) {
        if (progress < 4)
            progress = 4;

        if (progress > 96)
            progress = 96;

        super.setProgress(progress);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        float interval = (float) ((width - (getPaddingLeft() + getPaddingRight())) / 11);
        for (int i = 1; i < 12; i++) {

            if (i == initialValue + 1) {
                p.setStrokeWidth(DensityUtil.dip2px(getContext(), 1));
                p.setColor(ContextCompat.getColor(getContext(), R.color.black));
            } else {
                p.setStrokeWidth(DensityUtil.dip2px(getContext(), 0.5f));
                p.setColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
            }
            float x = (i * interval) + getPaddingLeft() - interval / 2;
            int middleTop = (height / 2) - DensityUtil.dip2px(getContext(), 4);
            int middleBottom = height - ((height / 2) - DensityUtil.dip2px(getContext(), 4));
            canvas.drawLine(x, middleTop - DensityUtil.dip2px(getContext(), 3), x, middleTop, p);
            canvas.drawLine(x, middleBottom, x, middleBottom + DensityUtil.dip2px(getContext(), 3), p);
        }
        super.onDraw(canvas);
    }
}
