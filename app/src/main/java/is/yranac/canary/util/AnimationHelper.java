package is.yranac.canary.util;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import is.yranac.canary.R;
import is.yranac.canary.model.mode.ModeCache;

/**
 * Created by Schroeder on 3/3/15.
 */
public class AnimationHelper {

    private static final String LOG_TAG = "AnimationHelper";

    public static void slideViewInFromTop(final View view, long duration, float tention) {
        slideViewInFromTop(view, duration, tention, null);
    }

    public static void slideViewInFromTop(final View view, long duration, float tention, final AnimationCompletion animationCompletion) {

        if (view == null)
            return;

        view.clearAnimation();

        TranslateAnimation slideOut = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        slideOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (animationCompletion != null)
                    animationCompletion.onComplete();
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

        });

        slideOut.setInterpolator(new OvershootInterpolator(tention));
        slideOut.setDuration(duration);
        view.setAnimation(slideOut);
        slideOut.start();
    }


    public static void slideViewOutToTop(final View view, long duration, float tension) {
        slideViewOutToTop(view, duration, tension, null);
    }

    public static void slideViewOutToTop(final View view, long duration, float tension, final AnimationCompletion animationCompletion) {

        if (view == null)
            return;
        view.clearAnimation();


        TranslateAnimation slideOut = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f);
        slideOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                if (animationCompletion != null)
                    animationCompletion.onComplete();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

        });
        slideOut.setInterpolator(new AnticipateInterpolator(tension));
        slideOut.setDuration(duration);
        view.setAnimation(slideOut);
        slideOut.start();
    }

    public static void expandHeight(final View view, Context context, long duration) {

        if (context == null || view == null)
            return;

        Animation slideOut = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.expand_height);
        slideOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

        });

        slideOut.setDuration(duration);
        view.startAnimation(slideOut);
    }

    public static void collapseHeight(final View view, Context context, long duration) {

        if (context == null || view == null)
            return;

        Animation slideOut = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.collapse_height);
        slideOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

        });

        slideOut.setDuration(duration);
        view.startAnimation(slideOut);

    }

    public static void fadeViewIn(final View view, long duration) {
        fadeViewInAfterDelay(view, duration, 0);
    }

    public static void fadeViewOut(final View view, long duration) {
        fadeViewOutAfterDelay(view, duration, 0);
    }

    public static void fadeFromAlphaToAlpha(final View view, float fromAlpha, final float toAlpha, long duration) {
        fadeFromAlphaToAlphaAfterDelay(view, fromAlpha, toAlpha, duration, 0);
    }

    public static void fadeViewInAfterDelay(final View view, long duration, long delay) {
        fadeFromAlphaToAlphaAfterDelay(view, 0.0f, 1.0f, duration, delay);
    }

    public static void fadeViewOutAfterDelay(final View view, long duration, long delay) {
        fadeFromAlphaToAlphaAfterDelay(view, 1.0f, 0.0f, duration, delay);
    }

    public static void fadeFromAlphaToAlphaAfterDelay(final View view, float fromAlpha, final float toAlpha, long duration, long delay) {
        if (view == null) {
            return;
        }

        AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setStartOffset(delay);
        view.setAlpha(1.0f);
        view.setVisibility(View.VISIBLE);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setAlpha(toAlpha);
                if (toAlpha == 0.0f) {
                    view.setVisibility(View.INVISIBLE);
                } else {
                    view.setVisibility(View.VISIBLE);
                }
                view.clearAnimation();

                view.invalidate();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

        });
        view.startAnimation(alphaAnimation);
        alphaAnimation.start();
    }

    public static void animationBackgroundColor(final View view, int fromColor, int toColor, long duration) {

        if (fromColor == toColor)
            return;
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        valueAnimator.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        int value = (int) animator.getAnimatedValue();

                        view.setBackgroundColor(value);
                    }

                });
        valueAnimator.setDuration(duration);
        valueAnimator.start();

    }

    public static void animationTextColor(final TextView view, int fromColor, int toColor, long duration) {
        if (fromColor == toColor)
            return;
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        valueAnimator.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        int value = (int) animator.getAnimatedValue();

                        view.setTextColor(value);
                    }

                });
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public static void viewGoneAfterDelay(final View view, long duration) {
        Runnable mRunnable;
        Handler mHandler = new Handler();

        mRunnable = new Runnable() {

            @Override
            public void run() {
                view.setVisibility(View.GONE); //This will remove the View. and free s the space occupied by the View
            }
        };


        mHandler.postDelayed(mRunnable, duration);

    }

    public static void viewInvisblieAfterDelay(final View view, long duration) {
        Runnable mRunnable;
        Handler mHandler = new Handler();

        mRunnable = new Runnable() {

            @Override
            public void run() {
                view.setVisibility(View.INVISIBLE); //This will remove the View. and free s the space occupied by the View
            }
        };
        mHandler.postDelayed(mRunnable, duration);

    }

    public static void viewVisibleAfterDelay(final View view, long duration) {
        Runnable mRunnable;
        Handler mHandler = new Handler();

        mRunnable = new Runnable() {

            @Override
            public void run() {
                view.setVisibility(View.VISIBLE); //This will remove the View. and free s the space occupied by the View
            }
        };
        mHandler.postDelayed(mRunnable, duration);

    }

    public static void startPulsing(final View view, boolean up, float distance, int duration) {
        view.clearAnimation();

        TranslateAnimation bounceAnimation = new TranslateAnimation(
                Animation.ABSOLUTE, 0.0f,
                Animation.ABSOLUTE, 0.0f,
                Animation.ABSOLUTE, 0.0f,
                Animation.ABSOLUTE, up ? -distance : distance);
        bounceAnimation.setRepeatMode(Animation.REVERSE);
        bounceAnimation.setRepeatCount(Animation.INFINITE);
        bounceAnimation.setDuration(duration);
        bounceAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        bounceAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation.setStartOffset(0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                animation.setStartOffset(0);
            }
        });

        view.startAnimation(bounceAnimation);
    }

    public static void startShaking(final View view, int distance, int duration) {
        view.clearAnimation();

        TranslateAnimation bounceAnimation = new TranslateAnimation(
                Animation.ABSOLUTE, -distance,
                Animation.ABSOLUTE, distance,
                Animation.ABSOLUTE, 0.0f,
                Animation.ABSOLUTE, 0);
        bounceAnimation.setRepeatMode(Animation.REVERSE);
        bounceAnimation.setRepeatCount(Animation.INFINITE);
        bounceAnimation.setDuration(duration);
        bounceAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        bounceAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation.setStartOffset(0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                animation.setStartOffset(0);
            }
        });

        view.startAnimation(bounceAnimation);
    }

    public static void slideViewInFromBottom(final View view, int duration) {
        slideViewInFromBottom(view, duration, 0);
    }

    public static void slideViewInFromBottom(final View view, int duration, int delay) {
        if (view == null)
            return;
        view.clearAnimation();


        TranslateAnimation slideIn = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        slideIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

        });
        slideIn.setStartOffset(delay);
        slideIn.setDuration(duration);
        view.setAnimation(slideIn);
        view.startAnimation(slideIn);
    }

    public static void fadeViewOutToCenter(final View view, long duration) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0, 1.0f, 0, view.getWidth() / 2, view.getHeight() / 2);


        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);

        AnimationSet as = new AnimationSet(true);
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }


        });

        as.setDuration(duration);
        as.addAnimation(scaleAnimation);
        as.addAnimation(alphaAnimation);
        view.startAnimation(as);
    }

    public static void fadeViewInAndOutAfterDelay(final View view, long duration, long delay) {

        view.clearAnimation();
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        final AlphaAnimation alphaAnimation2 = new AlphaAnimation(1.0f, 0f);

        view.setVisibility(View.VISIBLE);

        alphaAnimation.setDuration(duration);
        alphaAnimation2.setDuration(duration);
        alphaAnimation2.setStartOffset(delay);

        alphaAnimation.setFillAfter(true);
        alphaAnimation2.setFillAfter(true);
        view.setAlpha(1.0f);

        alphaAnimation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setAlpha(0.0f);
                view.clearAnimation();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setAnimation(alphaAnimation2);
                alphaAnimation2.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.setAnimation(alphaAnimation);
        alphaAnimation.start();

    }


    public interface ModeChanging {
        boolean modeChanging();
    }

    public static void pulseModeView(Activity activity, final ImageView firstImage, String mode, @NonNull final ModeChanging modeChanging) {
        if (mode == null)
            return;

        int drawable;
        final int paddingBottom;
        switch (mode) {
            case ModeCache.away:
                drawable = R.drawable.mode_away_icon;
                paddingBottom = 0;
                break;
            case ModeCache.home:
                drawable = R.drawable.mode_home_icon;
                paddingBottom = DensityUtil.dip2px(activity, 3);
                break;
            case ModeCache.night:
                drawable = R.drawable.mode_night_icon;
                paddingBottom = DensityUtil.dip2px(activity, 3);
                break;
            case ModeCache.privacy:
                drawable = R.drawable.mode_privacy_icon;
                paddingBottom = DensityUtil.dip2px(activity, 3);
                break;
            default:
                return;
        }

        final Animation pulse = AnimationUtils.loadAnimation(activity, R.anim.pulse);

        final int firstPaddingBottom = firstImage.getPaddingBottom();

        final Drawable firstDrawable = firstImage.getDrawable();
        final Drawable newDrawable = activity.getResources().getDrawable(drawable);
        final Animation fadeOut = AnimationUtils.loadAnimation(activity, R.anim.pulse_reverse);

        fadeOut.setAnimationListener(
                new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        firstImage.clearAnimation();


                        if (modeChanging.modeChanging()) {
                            firstImage.startAnimation(pulse);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

        pulse.setAnimationListener(
                new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        firstImage.clearAnimation();
                        if (modeChanging.modeChanging()) {
                            if (firstImage.getDrawable()
                                    .equals(firstDrawable)) {
                                firstImage.setImageDrawable(newDrawable);
                                firstImage.setPadding(0, 0, 0, paddingBottom);
                            } else {
                                firstImage.setImageDrawable(firstDrawable);
                                firstImage.setPadding(0, 0, 0, firstPaddingBottom);
                            }
                            firstImage.startAnimation(fadeOut);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
        firstImage.startAnimation(pulse);

    }


    public interface AnimationCompletion {
        void onComplete();
    }

    public static void slideDownAndOut(final View view, long duration) {
        view.clearAnimation();
        AnimationSet as = new AnimationSet(true);
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }


        });

        as.setDuration(duration);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        as.addAnimation(alphaAnimation);
        TranslateAnimation slideOut = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        as.addAnimation(slideOut);


        view.setAlpha(1.0f);
        view.setVisibility(View.VISIBLE);

        as.setDuration(duration);
        view.setAnimation(as);
        as.start();
    }

    public static void slideUpAndIn(final View view, long duration) {
        view.clearAnimation();
        AnimationSet as = new AnimationSet(true);
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }


        });

        as.setDuration(duration);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);

        as.addAnimation(alphaAnimation);
        TranslateAnimation slideOut = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        as.addAnimation(slideOut);


        view.setAlpha(1.0f);
        view.setVisibility(View.VISIBLE);


        view.setAnimation(as);
        as.start();
    }

    public static void animateAvatars(LinearLayout avatarLayout, int currentId, boolean animated) {

        int childcount = avatarLayout.getChildCount();
        for (int i = 0; i < childcount; i++) {
            View v = avatarLayout.getChildAt(i);


            int tag = (int) v.getTag();


            if (v.getAnimation() != null && !v.getAnimation().hasEnded()) {
                v.getAnimation().cancel();
                if (tag == currentId || currentId == 0) {
                    v.setAlpha(1.0f);
                } else {
                    v.setAlpha(0.5f);
                }
                continue;
            }
            if (tag == currentId || currentId == 0) {
                if (animated)
                    AnimationHelper.fadeFromAlphaToAlpha(v, v.getAlpha(), 1.0f, 250);
                else
                    v.setAlpha(1.0f);
            } else {
                if (animated)
                    AnimationHelper.fadeFromAlphaToAlpha(v, v.getAlpha(), 0.5f, 250);
                else
                    v.setAlpha(0.5f);
            }
        }
    }
}
