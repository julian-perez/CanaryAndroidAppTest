package is.yranac.canary.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import is.yranac.canary.R;
import is.yranac.canary.util.DensityUtil;

/**
 * Created by michaelschroeder on 2/22/17.
 */
public class ArrowView extends View {

    private Paint paint = new Paint();
    private Path path = new Path();
    private Point a = new Point();
    private Point b = new Point();
    private Point c = new Point();

    public ArrowView(Context context) {
        super(context);
    }

    public ArrowView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ArrowView,
                0, 0);


        paint.setAntiAlias(true);

        paint.setStrokeWidth(DensityUtil.dip2px(getContext(), 1));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        paint.setColor(a.getColor(R.styleable.ArrowView_arrow_color, Color.GRAY));
        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        a = new Point(0, 0);
        b = new Point(w / 2, h);
        c = new Point(w, 0);
    }

    @Override
    public void draw(Canvas canvas) {

        path.setFillType(Path.FillType.EVEN_ODD);
        path.lineTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);

        canvas.drawPath(path, paint);

        super.draw(canvas);

    }


}