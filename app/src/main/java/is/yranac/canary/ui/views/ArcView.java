package is.yranac.canary.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;

import is.yranac.canary.R;
import is.yranac.canary.util.DensityUtil;

/**
 * Created by michaelschroeder on 5/2/17.
 */

public class ArcView extends View {
    private Paint drawPaint;
    private RectF rectF;

    public ArcView(Context context) {
        super(context);
        setupPaint();
    }

    public ArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
    }

    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupPaint();
    }


    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 4));
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        drawPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {


        rectF = new RectF(0, 0, w, h);
        rectF.inset(drawPaint.getStrokeWidth(), drawPaint.getStrokeWidth());

    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawArc(rectF, 270, 90, false, drawPaint);

    }

    public void startSpin() {

        clearAnimation();

        setVisibility(VISIBLE);
        setAlpha(0.0f);
        RotateAnimation rotation = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.clockwise_rotation);
        rotation.setDuration(1500);

        AlphaAnimation fade = new AlphaAnimation(0.0f, 1.0f);
        fade.setDuration(500);


        AnimationSet setAnimation = new AnimationSet(false);
        setAnimation.addAnimation(rotation);
        setAnimation.addAnimation(fade);
        startAnimation(setAnimation);
    }

    public void cancelSpin() {
        if (getAlpha() == 0.0)
            return;

        final AnimationSet setAnimation = new AnimationSet(false);

        RotateAnimation rotation = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.counter_clockwise_rotation);
        rotation.setDuration(500);

        AlphaAnimation fade = new AlphaAnimation(getAlpha(), 0.0f);

        fade.setDuration(500);
        fade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clearAnimation();
                setAlpha(0.0f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        setAnimation.addAnimation(rotation);
        setAnimation.addAnimation(fade);
        startAnimation(setAnimation);
    }

    public void stopSpin() {

        AlphaAnimation fade = new AlphaAnimation(getAlpha(), 0.0f);
        fade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clearAnimation();
                setAlpha(0.0f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        startAnimation(fade);
    }
}
