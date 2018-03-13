package is.yranac.canary.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.R;

/**
 * Created by sergeymorozov on 11/8/16.
 */

public class PlaybarNotificationIndicator extends View {

    private Paint indicatorPaint;
    private Integer indicatorColor;
    private List<Point> points;
    private Path indicatorPath;


    public PlaybarNotificationIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int getIndicatorColor() {
        if (indicatorColor == null)
            indicatorColor = ContextCompat.getColor(getContext(), R.color.dark_moderate_cyan);
        return indicatorColor;
    }

    private Paint getIndicatorPaint() {
        if (indicatorPaint == null) {
            indicatorPaint = new Paint();
            indicatorPaint.setColor(getIndicatorColor());
            indicatorPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            indicatorPaint.setStyle(Paint.Style.FILL);
        }
        return indicatorPaint;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (points == null)
            points = new ArrayList<>();
        else
            points.clear();

        for (int i = 0; i <= 4; i++) {

            float x, y;
            Point indicatorPoint = new Point();

            //need 5 points to draw the shape
            switch (i) {
                case 0:
                    x = 0;
                    y = 0;
                    break;
                case 1:
                    x = 0;
                    y = h / 2;
                    break;
                case 2:
                    x = w / 2;
                    y = h;
                    break;
                case 3:
                    x = w;
                    y = h / 2;
                    break;
                case 4:
                    x = w;
                    y = 0;
                    break;
                default:
                    return;
            }

            indicatorPoint.x = x;
            indicatorPoint.y = y;

            points.add(indicatorPoint);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {

        indicatorPath = new Path();

        for (int i = 0; i < points.size(); i++) {
            Point drawPoint = points.get(i);
            if (i == 0)
                indicatorPath.moveTo(drawPoint.x, drawPoint.y);
            else
                indicatorPath.lineTo(drawPoint.x, drawPoint.y);
        }
        indicatorPath.close();
        canvas.drawPath(indicatorPath, getIndicatorPaint());

    }

    private class Point {
        float x, y;
    }
}
