package is.yranac.canary.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.TimeZone;

import is.yranac.canary.R;
import is.yranac.canary.util.DensityUtil;

/**
 * Created by Schroeder on 8/31/16.
 */
public class CustomTimelineClock extends View {

    private final ShapeDrawable hourHand;
    private final ShapeDrawable minuteHand;
    private GradientDrawable dial;

    private int dialWidth;
    private int dialHeight;
    private int handWidth;
    private int minuteHandLength;
    private int hourHandLength;

    private float minutes;
    private float hour;

    private int color;
    private final float hourOffset = 2.0f;

    public CustomTimelineClock(Context context) {
        this(context, null);
    }

    public CustomTimelineClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTimelineClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dial = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.dial);
        dialWidth = dial.getIntrinsicWidth();
        dialHeight = dial.getIntrinsicHeight();


        handWidth = DensityUtil.dip2px(getContext(), 2);
        hourHandLength = DensityUtil.dip2px(getContext(), 7);

        minuteHandLength = DensityUtil.dip2px(getContext(), 11);

        hourHand = new ShapeDrawable();
        hourHand.getPaint().setStrokeCap(Paint.Cap.ROUND);
        hourHand.getPaint().setStrokeWidth(handWidth);
        minuteHand = new ShapeDrawable();
        minuteHand.getPaint().setStrokeCap(Paint.Cap.ROUND);
        minuteHand.getPaint().setStrokeWidth(handWidth);

        color = ContextCompat.getColor(context, R.color.black);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        float hScale = 1.0f;
        float vScale = 1.0f;

        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < dialWidth) {
            hScale = (float) widthSize / (float) dialWidth;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < dialHeight) {
            vScale = (float) heightSize / (float) dialHeight;
        }

        float scale = Math.min(hScale, vScale);

        setMeasuredDimension(resolveSizeAndState((int) (dialWidth * scale), widthMeasureSpec, 0),
                resolveSizeAndState((int) (dialHeight * scale), heightMeasureSpec, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int availableWidth = getRight() - getLeft();
        int availableHeight = getBottom() - getTop();

        int x = availableWidth / 2;
        int y = availableHeight / 2;

        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();

        if (availableWidth < w || availableHeight < h) {
            float scale = Math.min((float) availableWidth / (float) w,
                    (float) availableHeight / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }

        dial.setStroke(handWidth, color);
        dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        dial.draw(canvas);

        canvas.save();
        canvas.rotate(hour / 2.0f * 360.0f, x, y);
        hourHand.setBounds(x - (handWidth / 2), y - hourHandLength, x + (handWidth / 2), y);
        hourHand.getPaint().setColor(color);
        hourHand.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.rotate(minutes / 30.0f * 360.0f, x, y);



        minuteHand.setBounds(x - (handWidth / 2), y - minuteHandLength, x + (handWidth / 2), y);
        minuteHand.getPaint().setColor(color);
        minuteHand.draw(canvas);


        minuteHand.draw(canvas);
        canvas.restore();

    }


    public void setTime(long time, int color) {
        this.color = color;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(time * 600);

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        float minutes = minute + second / 60.0f;
        float hours = (hour + this.minutes / 60.0f) + hourOffset;


        if (this.minutes == minutes && this.hour == hours)
            return;


        this.hour = hours;
        this.minutes = minutes;
        invalidate();
    }


}