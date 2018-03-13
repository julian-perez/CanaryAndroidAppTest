package is.yranac.canary.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import is.yranac.canary.R;

public class TriangleView extends View {

    int customRotation = 0;
    private Paint paint = new Paint();

    public TriangleView(Context context) {
        super(context);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TriangleView,
                0, 0);


        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);


        try {
            customRotation = a.getInteger(R.styleable.TriangleView_customRotation, 0);
            paint.setColor(a.getColor(R.styleable.TriangleView_triangle_color, Color.WHITE));

        } finally {
            a.recycle();
        }
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


        Path path = new Path();
        float mid = getWidth() / 2;
        float min = Math.min(getWidth(), getHeight());
        float fat = min / 17;

        paint.setStrokeWidth(fat);
        paint.setStyle(Paint.Style.STROKE);

        path.reset();
        paint.setStyle(Paint.Style.FILL);

        switch (customRotation) {
            //implement other rotations as needed
            default:
                // top
                path.moveTo(mid, getHeight());
                //  right
                path.lineTo(getWidth(), 0);
                //  left
                path.lineTo(0, 0);
                break;
            case 1:
                // top
                path.moveTo(mid, 0);
                //  right
                path.lineTo(getWidth(), getHeight());
                //  left
                path.lineTo(0, getHeight());
        }

        path.close();
        canvas.drawPath(path, paint);

    }

    public void centerXover(View target) {
        float left = target.getX();
        float measuredWidth = target.getMeasuredWidth();
        float width = target.getWidth();
        float leftPad = target.getPaddingLeft();
        float rightPad = target.getPaddingRight();


        float x = target.getX() + ((target.getWidth() - target.getPaddingLeft() - target.getPaddingRight()) / 2);

        setX(x);
    }

}
