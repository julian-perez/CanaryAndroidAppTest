package is.yranac.canary.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import is.yranac.canary.R;
import is.yranac.canary.util.DensityUtil;

/**
 * Created by michael on 2/9/17.
 */

public class CustomProgressView extends View {
    // setup initial color
    // defines paint and canvas
    private Paint drawPaint;
    private int green;
    private int gray;
    private RectF rectF;

    public CustomProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }

    // Setup paint with color and stroke styles
    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 2));
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        gray = ContextCompat.getColor(getContext(), R.color.gray);
        green = ContextCompat.getColor(getContext(), R.color.dark_moderate_cyan);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {


        rectF = new RectF(0, 0, w, h);
        rectF.inset(drawPaint.getStrokeWidth(), drawPaint.getStrokeWidth());

    }

    // ...variables and setting up paint...
    // Let's draw three circles
    @Override
    protected void onDraw(Canvas canvas) {
        drawPaint.setColor(green);
        canvas.drawArc(rectF, 270, 90, false, drawPaint);
        drawPaint.setColor(gray);
        canvas.drawArc(rectF, 0, 270, false, drawPaint);
    }
}