package is.yranac.canary.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import is.yranac.canary.util.DensityUtil;

public class LineTriangleView extends View {

    private Paint paint = new Paint();
    private Paint red = new Paint();

    public LineTriangleView(Context context) {
        super(context);
    }

    public LineTriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineTriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = canvas.getWidth();
        canvas.drawLine(0, 0, width, 0, paint);


        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);

        int triangleWidth = DensityUtil.dip2px(getContext(), 18);
        int rightMargin = DensityUtil.dip2px(getContext(), 24);


        red.setColor(Color.WHITE);
        red.setStyle(Paint.Style.FILL_AND_STROKE);

        Path path = new Path();
        path.moveTo(width - (triangleWidth + rightMargin), 0);
        path.lineTo(width - rightMargin, 0);
        path.lineTo(width - (rightMargin + (triangleWidth * 0.5f)), canvas.getHeight());
        path.close();
        canvas.drawPath(path, red);

        canvas.drawLine(width - (triangleWidth + rightMargin), 0, width - (rightMargin + (triangleWidth * 0.5f)), canvas.getHeight(), paint);
        canvas.drawLine(width - (rightMargin + (triangleWidth * 0.5f)), canvas.getHeight(), width - rightMargin, 0, paint);


    }
}
