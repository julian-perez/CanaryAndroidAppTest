package is.yranac.canary.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by michaelschroeder on 7/6/17.
 */

public class RoundedCornerRelativeLayout extends RelativeLayout {

    private static final float CORNER_RADIUS = 5.0f;
    private Bitmap windowFrame;

    //this constructer is needed by a tool in explipse to render the layout, you can not define it
    public RoundedCornerRelativeLayout(Context context, AttributeSet attr) {
        super(context, attr);
    }

    //this
    public RoundedCornerRelativeLayout(Context context, AttributeSet attr, View view) {
        super(context, attr);
        init(view);
    }

    private void init(View view) {

        view.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        view.setClickable(true);
        addView(view);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (windowFrame == null)
            createWindowFrame();// Lazy creation of the window frame, this is needed as we don't know the width & height of the screen until draw time

        canvas.drawBitmap(windowFrame, 0, 0, null);
    }

    private void createWindowFrame() {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        float cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CORNER_RADIUS, metrics);

        windowFrame = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);// Create a new image we will draw over the map
        Canvas osCanvas = new Canvas(windowFrame);// Create a    canvas to draw onto the new image

        RectF outerRectangle = new RectF(0, 0, getWidth(), getHeight());
        RectF innerRectangle = new RectF(cornerRadius, cornerRadius, getWidth() - cornerRadius, getHeight() - cornerRadius);

        float radiusCorner = cornerRadius;// The angle of your corners

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);// Anti alias allows for smooth corners
        paint.setColor(Color.WHITE);// This is the color of your activity background
        osCanvas.drawRect(outerRectangle, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));// A out B http://en.wikipedia.org/wiki/File:Alpha_compositing.svg
        osCanvas.drawRoundRect(innerRectangle, radiusCorner, radiusCorner, paint);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);

        windowFrame = null;// If the layout changes null our frame so it can be recreated with the new width and height
    }

}

