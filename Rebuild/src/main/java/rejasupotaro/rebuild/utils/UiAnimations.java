package rejasupotaro.rebuild.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import rejasupotaro.rebuild.R;

public final class UiAnimations {

    private static UiAnimations instance;

    private Animation slideUpAnimation;

    private Animation slideDownAnimation;

    private Animation slideLeftAnimation;

    public static synchronized void initialize(Context context) {
        if (instance == null) {
            instance = new UiAnimations(context);
        }
    }

    private UiAnimations(Context context) {
        slideUpAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_up_enter);
        slideDownAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_down_exit);
        slideLeftAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_left);
    }

    public static void slideUp(View view) {
        slideUp(view, 0, 0);
    }

    public static void slideUp(View view, long delay, long duration) {
        view.setVisibility(View.VISIBLE);
        instance.slideUpAnimation.setStartOffset(delay);
        instance.slideUpAnimation.setDuration(duration);
        view.startAnimation(instance.slideUpAnimation);
    }

    public static void slideDown(View view) {
        slideDown(view, 0, 0);
    }

    public static void slideDown(View view, int delay, int duration) {
        view.setVisibility(View.VISIBLE);
        instance.slideDownAnimation.setStartOffset(delay);
        instance.slideDownAnimation.setDuration(duration);
        view.startAnimation(instance.slideDownAnimation);
    }

    public static void slideLeft(final View view, long delay, long duration) {
        instance.slideLeftAnimation.setStartOffset(delay);
        instance.slideLeftAnimation.setDuration(duration);
        view.startAnimation(instance.slideLeftAnimation);
    }

    public static void fadeIn(final View view, long delay, long duration) {
        view.setAlpha(0);
        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "alpha", 1);
        animation.setStartDelay(delay);
        animation.setDuration(duration);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animation.start();
    }

    public static void fadeOut(final View view, long delay, long duration) {
        view.setAlpha(1);
        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "alpha", 0);
        animation.setStartDelay(delay);
        animation.setDuration(duration);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animation.start();
    }
}
