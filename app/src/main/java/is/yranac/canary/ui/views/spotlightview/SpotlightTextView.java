package is.yranac.canary.ui.views.spotlightview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.util.AttributeSet;

import is.yranac.canary.ui.views.TextViewPlus;

/**
 * Shimmer
 * User: romainpiel
 * Date: 06/03/2014
 * Time: 10:19
 *
 * Shimmering TextView
 * Dumb class wrapping a ShimmerViewHelper
 */
public class SpotlightTextView extends TextViewPlus implements SpotlightViewBase {

    private SpotlightViewHelper spotlightViewHelper;

    public SpotlightTextView(Context context) {
        super(context);
        spotlightViewHelper = new SpotlightViewHelper(this, getPaint(), null);
        spotlightViewHelper.setPrimaryColor(getCurrentTextColor());
    }

    public SpotlightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        spotlightViewHelper = new SpotlightViewHelper(this, getPaint(), attrs);
        spotlightViewHelper.setPrimaryColor(getCurrentTextColor());
    }

    public SpotlightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        spotlightViewHelper = new SpotlightViewHelper(this, getPaint(), attrs);
        spotlightViewHelper.setPrimaryColor(getCurrentTextColor());
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
    public void setTextColor(int color) {
        super.setTextColor(color);
        if (spotlightViewHelper != null) {
            spotlightViewHelper.setPrimaryColor(getCurrentTextColor());
        }
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
        if (spotlightViewHelper != null) {
            spotlightViewHelper.setPrimaryColor(getCurrentTextColor());
        }
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
