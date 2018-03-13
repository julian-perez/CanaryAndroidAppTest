package is.yranac.canary.ui.views;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import is.yranac.canary.R;

/**
 * Created by Schroeder on 9/1/16.
 */
public class TimelineGradient extends RelativeLayout {

    private final int white;
    private final int blue;
    private final int blueTwo;


    public TimelineGradient(Context context) {
        this(context, null);
    }

    public TimelineGradient(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimelineGradient(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        white = ContextCompat.getColor(getContext(), R.color.white_transparent_90);
        blue = ContextCompat.getColor(getContext(), R.color.bright_sky_blue_two);
        blueTwo = ContextCompat.getColor(getContext(), R.color.water_blue);
    }


    public void setPostion(boolean full) {

        int colors[] = new int[2];

        if (!full) {
            colors[0] = white;
            colors[1] = white;
        } else if (full) {
            colors[0] = blue;
            colors[1] = blueTwo;
        }

        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);

        setBackground(g);
    }


}
