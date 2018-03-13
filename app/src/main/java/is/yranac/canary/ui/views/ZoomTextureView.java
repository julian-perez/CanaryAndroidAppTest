package is.yranac.canary.ui.views;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.ScaleAnimation;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.messages.ZoomOut;
import is.yranac.canary.util.DensityUtil;

/**
 * Created by Schroeder on 4/23/15.
 */
public class ZoomTextureView extends TextureView {

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    // Remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    float oldDist = 1f;

    // We can be in one of these 3 states
    private static final int NONE = 0;
    private static final int PAN = 1;
    private static final int ZOOM = 2;
    int mode = NONE;

    private ZoomTextureGestures zoomTextureGestures;
    private TextureViewCallbacks textureViewCallbacks;
    private boolean panning = false;

    private float totalX = 0f;
    private float totalY = 0f;
    private Matrix originalMatrix = new Matrix();
    private GestureDetector gestureDetector;
    private boolean allZoomWithBounds = false;
    private boolean allowVerticalPaning = false;


    public int media_width = 16;
    public int media_height = 9;

    public void setOriginalMatrix(Matrix originalMatrix) {
        this.originalMatrix = originalMatrix;
    }

    public void setAllZoomWithBounds(boolean allZoomWithBounds) {
        this.allZoomWithBounds = allZoomWithBounds;
    }

    public void setAllowVerticalPaning(boolean allowVerticalPaning) {
        this.allowVerticalPaning = allowVerticalPaning;
    }

    private float getZoomCenter() {
        float[] m2 = new float[9];
        matrix.getValues(m2);
        float left = -m2[Matrix.MTRANS_X];


        float maxLeft = (getWidth() * m2[Matrix.MSCALE_X]) - getWidth();

        return (left / maxLeft) * getWidth();
    }

    private float getZoomCenterY() {
        float[] m2 = new float[9];
        matrix.getValues(m2);
        float left = -m2[Matrix.MTRANS_Y];


        float maxLeft = (getHeight() * m2[Matrix.MSCALE_Y]) - getHeight();

        return (left / maxLeft) * getHeight();
    }

    public boolean isMaxedZoom() {
        float[] m2 = new float[9];
        matrix.getValues(m2);
        return m2[Matrix.MSCALE_Y] == 1;
    }

    public float getMatrixScaleY() {
        float[] m2 = new float[9];
        matrix.getValues(m2);
        return m2[Matrix.MSCALE_Y];
    }

    public interface ZoomTextureGestures {
        void onSingleTap(MotionEvent event);

        void onDoubleTap(MotionEvent event);
    }

    public interface TextureViewCallbacks {

        void setScrollingEnabled(boolean enabled);

        void pastGesture(MotionEvent event);

        boolean pagerIsMoving();

        void zoomLevelCallback(float zoom);

        void zoomStopped();

        void zoomStarted();

    }


    public float getMatrixTop() {
        float[] m2 = new float[9];
        matrix.getValues(m2);
        return m2[Matrix.MTRANS_Y];
    }

    public ZoomTextureView(Context context) {
        super(context);
        init(context);
    }

    public ZoomTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setZoomTextureGestures(ZoomTextureGestures zoomTextureGesures1) {
        this.zoomTextureGestures = zoomTextureGesures1;
    }

    public void setTextureViewCallbacks(TextureViewCallbacks textureViewCallbacks) {
        this.textureViewCallbacks = textureViewCallbacks;
    }

    private void init(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());

    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            boolean moving = false;
            if (textureViewCallbacks != null)
                moving = textureViewCallbacks.pagerIsMoving();

            if (zoomTextureGestures != null && !moving) {
                zoomTextureGestures.onSingleTap(e);
            }

            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            boolean moving = false;
            if (textureViewCallbacks != null)
                moving = textureViewCallbacks.pagerIsMoving();


            textureViewCallbacks.zoomStarted();
            if (moving)
                return true;

            if (zoomTextureGestures != null)
                zoomTextureGestures.onDoubleTap(e);


            float[] m2 = new float[9];
            matrix.getValues(m2);

            float scaleFactor;
            float scaleFactorX = m2[Matrix.MSCALE_X];

            float[] m3 = new float[9];
            originalMatrix.getValues(m3);

            float x, y;
            if (!allowVerticalPaning) {
                if (scaleFactorX < m3[Matrix.MSCALE_X] * 2) {
                    scaleFactor = 2.0f / m3[Matrix.MSCALE_X];
                    x = getWidth() / 2.0f;
                } else if (scaleFactorX < (1.0f / m3[Matrix.MSCALE_Y])) {
                    scaleFactor = (1.0f / m3[Matrix.MSCALE_Y]) / scaleFactorX;
                    x = getWidth() / 2.0f;
                } else {
                    scaleFactor = 1 / scaleFactorX;
                    x = getZoomCenter();
                }

                y = getHeight() / 2;
            } else {
                scaleFactor = m2[Matrix.MSCALE_X];
                scaleFactor += 1f;
                scaleFactor = scaleFactor > 3f ? 1f : scaleFactor;

                scaleFactor /= scaleFactorX;
                float offsetX = m2[Matrix.MTRANS_X] * -1;
                float offsetY = m2[Matrix.MTRANS_Y] * -1;

                x = (offsetX + e.getX()) / m2[Matrix.MSCALE_X];
                y = (offsetY + e.getY()) / m2[Matrix.MSCALE_Y];
                if (scaleFactor < 1) {
                    x = getZoomCenter();
                    y = getZoomCenterY();
                }
            }

            start.set(e.getX(), e.getY());

            setMatrixScaleFactor(scaleFactor, x, y);
            return true;
        }
    }


    @Subscribe(mode = Subscribe.Mode.Main)
    public void zoomOut(ZoomOut zoomOut) {

        float[] m2 = new float[9];
        originalMatrix.getValues(m2);


        float[] m = new float[9];
        matrix.getValues(m);


        float scaleFactor;
        scaleFactor = m2[Matrix.MSCALE_X] / m[Matrix.MSCALE_X];

        float x = getWidth() / 2.0f;
        float y = getHeight() / 2.0f;

        setMatrixScaleFactor(scaleFactor, x, y);


    }


    public void setMaxZoom(float deviceWidth, float deviceHeight) {
        float[] m2 = new float[9];
        matrix.getValues(m2);

        float[] m3 = new float[9];
        originalMatrix.getValues(m3);

        float scaleFactor;
        scaleFactor = deviceHeight / (deviceWidth * 9.0f / 16.0f);

        setMatrixScaleFactor(scaleFactor, deviceWidth / 2.0f, deviceHeight / 2.0f, false);

    }


    public void setMaxZoom() {
        setMaxZoom(getWidth(), getHeight());
    }

    private boolean animationCanceled;

    @Override
    public void clearAnimation() {
        animationCanceled = true;
        super.clearAnimation();
    }

    private void setMatrixScaleFactor(final float scaleFactor, final float x, final float y) {
        setMatrixScaleFactor(scaleFactor, x, y, true);
    }

    private void setMatrixScaleFactor(final float scaleFactor, final float x, final float y, boolean animated) {
        animationCanceled = false;
        ScaleAnimation animator = new ScaleAnimation(1.0f, scaleFactor, 1.0f, scaleFactor, x, y);
        if (animated) {
            animator.setDuration(500);
        } else {
            animator.setDuration(0);

        }
        animator.setFillBefore(true);
        animator.setInterpolator(new AnticipateOvershootInterpolator());
        animator.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (animationCanceled)
                    return;

                matrix.postScale(scaleFactor, scaleFactor, x, y);
                setMatrix(matrix);
                clearAnimation();

                invalidate();
                textureViewCallbacks.zoomStopped();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        startAnimation(animator);
    }

    public void reset() {
        setMatrix(new Matrix(originalMatrix));
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getAnimation() != null) {
            return true;
        }

        if (textureViewCallbacks != null && textureViewCallbacks.pagerIsMoving()) {

            if (event.getPointerCount() >= 2)
                gestureDetector.onTouchEvent(event);

            if (event.getPointerCount() > 0) {
                MotionEvent motionEvent = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), event.getRawX()
                        , event.getRawY(), event.getMetaState());

                textureViewCallbacks.pastGesture(motionEvent);
            }
            return true;
        }

        panZoomWithTouch(event);

        invalidate();//necessary to repaint the canvas
        return true;
    }

    /**
     * panZoomWithTouch function
     * /*      Listen to touch actions and perform Zoom or Pan respectively
     * /*
     */
    private void panZoomWithTouch(MotionEvent event) {

        gestureDetector.onTouchEvent(event);


        float[] m = new float[9];
        matrix.getValues(m);


        float[] m4 = new float[9];
        originalMatrix.getValues(m4);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN://when first finger down, get first point

                if (textureViewCallbacks != null)
                    textureViewCallbacks.setScrollingEnabled(m[Matrix.MSCALE_X] == m4[Matrix.MSCALE_X]);

                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = PAN;
                break;
            case MotionEvent.ACTION_POINTER_DOWN://when 2nd finger down, get second point
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event); //then get the mide point as centre for zoom
                    mode = ZOOM;
                    textureViewCallbacks.zoomStarted();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:       //when both fingers are released, do nothing

                if (mode == ZOOM) {
                    float size = m[Matrix.MSCALE_Y] * getHeight();
                    float sizeDiff = getHeight() - size;
                    if (sizeDiff < DensityUtil.dip2px(getContext(), 100) && allZoomWithBounds && sizeDiff != 0) {
                        setMaxZoom();
                    } else {
                        mode = NONE;
                        textureViewCallbacks.zoomStopped();
                    }
                }
                mode = NONE;
                panning = false;
                totalX = totalY = 0;
                if (textureViewCallbacks != null)
                    textureViewCallbacks.setScrollingEnabled(true);


                break;
            case MotionEvent.ACTION_MOVE:     //when fingers are dragged, transform matrix for panning
                if (mode == PAN) {
                    // ...
                    float deltaX = event.getX() - start.x;
                    float deltaY = event.getY() - start.y;
                    float offsetX = m[Matrix.MTRANS_X] + m[Matrix.MSCALE_X] * getWidth();
                    float offsetY = m[Matrix.MTRANS_Y] + m[Matrix.MSCALE_Y] * getHeight();

                    if (deltaX - totalX < 0.0f && offsetX - getWidth() < 5.0f) {
                        deltaX = totalX;
                    } else if (deltaX > 0.0f && m[Matrix.MTRANS_X] > -5.0f) {
                        deltaX = totalX;
                    }

                    if (deltaY - totalY < 0.0f && offsetY - getHeight() < 5.0f) {
                        deltaY = totalY;
                    } else if (deltaY - totalY > 0.0f && m[Matrix.MTRANS_Y] > -5.0f) {
                        deltaY = totalY;
                    }
                    if (!allowVerticalPaning) {
                        deltaY = 0;
                    }


                    panning = panning || deltaX != totalX;

                    totalX = deltaX;

                    totalY = deltaY;

                    matrix.set(savedMatrix);
                    matrix.postTranslate(totalX, totalY);


                    float[] m2 = new float[9];
                    matrix.getValues(m2);
                    offsetX = m2[Matrix.MTRANS_X] + m2[Matrix.MSCALE_X] * getWidth();
                    offsetY = m2[Matrix.MTRANS_Y] + m2[Matrix.MSCALE_Y] * getHeight();

                    if (m2[Matrix.MTRANS_X] > 0.0f) {
                        m2[Matrix.MTRANS_X] = 0.0f;
                    } else if (offsetX - getWidth() < 0.0f && allZoomWithBounds) {
                        m2[Matrix.MTRANS_X] = -((m[Matrix.MSCALE_X] * getWidth()) - getWidth());

                    }

                    if (!allZoomWithBounds) {
                        if (m2[Matrix.MTRANS_Y] > 0.0f) {
                            m2[Matrix.MTRANS_Y] = 0.0f;
                        } else if (offsetY - getHeight() < 0.0f) {
                            m2[Matrix.MTRANS_Y] = -((m[Matrix.MSCALE_Y] * getHeight()) - getHeight());
                        }
                    }

                    matrix.setValues(m2);


                } else if (mode == ZOOM) { //if pinch_zoom, calculate distance ratio for zoom
                    if (textureViewCallbacks != null)
                        textureViewCallbacks.setScrollingEnabled(false);
                    float newDist = spacing(event);
                    float m2[] = new float[9];
                    if (newDist > 10f) {
                        matrix.getValues(m2);
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;

                        matrix.postScale(scale, scale, getWidth() / 2.0f, getHeight() / 2.0f);

                    }

                    matrix.getValues(m);

                    float[] originalM = new float[9];
                    originalMatrix.getValues(originalM);

                    if (allZoomWithBounds) {
                        if (m[Matrix.MSCALE_Y] > 1.0f) {
                            m[Matrix.MSCALE_Y] = 1.0f;
                            m[Matrix.MTRANS_X] = m2[Matrix.MTRANS_X];
                            m[Matrix.MTRANS_Y] = 0;
                        } else if (m[Matrix.MSCALE_Y] < originalM[Matrix.MSCALE_Y]) {
                            m[Matrix.MSCALE_Y] = originalM[Matrix.MSCALE_Y];
                            m[Matrix.MTRANS_X] = 0;
                            m[Matrix.MTRANS_Y] = originalM[Matrix.MTRANS_Y];
                        }

                        if (m[Matrix.MSCALE_X] < 1.0f) {
                            m[Matrix.MSCALE_X] = 1.0f;
                            m[Matrix.MTRANS_X] = 0;
                            m[Matrix.MTRANS_Y] = originalM[Matrix.MTRANS_Y];
                        } else if (m[Matrix.MSCALE_X] > (1 / originalM[Matrix.MSCALE_Y])) {
                            m[Matrix.MSCALE_X] = (1 / originalM[Matrix.MSCALE_Y]);
                            m[Matrix.MTRANS_X] = m2[Matrix.MTRANS_X];
                        }

                        float offsetX = m[Matrix.MTRANS_X] + m[Matrix.MSCALE_X] * getWidth();

                        if (m[Matrix.MTRANS_X] > 0.0f) {
                            m[Matrix.MTRANS_X] = 0.0f;
                        } else if (offsetX - getWidth() < 0.0f) {
                            m[Matrix.MTRANS_X] = -((m[Matrix.MSCALE_X] * getWidth()) - getWidth());
                        }

                        if (m[Matrix.MTRANS_Y] < 0.0f) {
                            m[Matrix.MTRANS_Y] = 0.0f;
                        }
                    } else {

                        if (m[Matrix.MSCALE_X] > 3.0 || m[Matrix.MSCALE_Y] < 1.0) {
                            m[Matrix.MSCALE_X] = m2[Matrix.MSCALE_X];
                            m[Matrix.MSCALE_Y] = m2[Matrix.MSCALE_Y];

                            m[Matrix.MTRANS_X] = m2[Matrix.MTRANS_X];
                            m[Matrix.MTRANS_Y] = m2[Matrix.MTRANS_Y];
                        } else {
                            float offsetX = m[Matrix.MTRANS_X] + m[Matrix.MSCALE_X] * getWidth();
                            float offsetY = m[Matrix.MTRANS_Y] + m[Matrix.MSCALE_Y] * getHeight();
                            if (m[Matrix.MTRANS_X] > 0.0f) {
                                m[Matrix.MTRANS_X] = 0.0f;
                            } else if (offsetX - getWidth() < 0.0f) {
                                m[Matrix.MTRANS_X] = -((m[Matrix.MSCALE_X] * getWidth()) - getWidth());

                            }
                            if (m[Matrix.MTRANS_Y] > 0.0f) {
                                m[Matrix.MTRANS_Y] = 0.0f;
                            } else if (offsetY - getHeight() < 0.0f) {
                                m[Matrix.MTRANS_Y] = -((m[Matrix.MSCALE_Y] * getHeight()) - getHeight());
                            }
                        }
                    }

                    matrix.setValues(m);

                }
                break;
        }
        setTransform(matrix);

        if (!panning && textureViewCallbacks != null && mode != ZOOM && mode != NONE) {
            MotionEvent motionEvent = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), event.getRawX()
                    , event.getRawY(), event.getMetaState());

            textureViewCallbacks.pastGesture(motionEvent);
        }

        if (textureViewCallbacks != null) {
            float m2[] = new float[9];

            matrix.getValues(m2);
            textureViewCallbacks.zoomLevelCallback(m2[Matrix.MSCALE_X]);
        }

    }


    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
        setTransform(matrix);

    }


    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        if (event.getPointerCount() < 2)
            return oldDist;
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        if (event.getPointerCount() < 2)
            return;
        // ...
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }


}
