package is.yranac.canary.ui.views.spotlightview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Shimmer
 * User: romainpiel
 * Date: 06/03/2014
 * Time: 10:19
 *
 * Shimmering Button
 * Dumb class wrapping a ShimmerViewHelper
 */
public class SpotlightImageView extends ImageView implements SpotlightViewBase {

    private SpotlightViewHelper spotlightViewHelper;

    public SpotlightImageView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SpotlightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SpotlightImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        Paint paint = new Paint();

        spotlightViewHelper = new SpotlightViewHelper(this, paint, attrs);
    }

    @Override
     public float getGradientX() {
        return spotlightViewHelper.getGradientX();
    }

    @Override
    public void setGradientX(float gradientX) {
        spotlightViewHelper.setGradientX(gradientX);
    }

    @Override
    public boolean isShimmering() {
        return spotlightViewHelper.isShimmering();
    }

    @Override
    public void setShimmering(boolean isShimmering) {
        spotlightViewHelper.setShimmering(isShimmering);
    }

    @Override
    public boolean isSetUp() {
        return spotlightViewHelper.isSetUp();
    }

    @Override
    public void setAnimationSetupCallback(SpotlightViewHelper.AnimationSetupCallback callback) {
        spotlightViewHelper.setAnimationSetupCallback(callback);
    }

    @Override
    public int getPrimaryColor() {
        return spotlightViewHelper.getPrimaryColor();
    }

    @Override
    public void setPrimaryColor(int primaryColor) {
        spotlightViewHelper.setPrimaryColor(primaryColor);
    }

    @Override
    public int getReflectionColor() {
        return spotlightViewHelper.getReflectionColor();
    }

    @Override
    public void setReflectionColor(int reflectionColor) {
        spotlightViewHelper.setReflectionColor(reflectionColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (spotlightViewHelper != null) {
            spotlightViewHelper.onSizeChanged();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (spotlightViewHelper != null) {
            spotlightViewHelper.onDraw();
        }
        super.onDraw(canvas);
    }
}
