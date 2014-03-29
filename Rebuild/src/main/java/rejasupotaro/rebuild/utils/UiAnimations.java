package rejasupotaro.rebuild.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import rejasupotaro.rebuild.R;

public final class UiAnimations {

    private UiAnimations() {
    }

    public static void slideUp(Context context, View view) {
        slideUp(context, view, 0, 0);
    }

    public static void slideUp(Context context, View view, long delay, long duration) {
        view.setVisibility(View.VISIBLE);
        Animation slideUpAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_up_enter);
        slideUpAnimation.setStartOffset(delay);
        slideUpAnimation.setDuration(duration);
        view.startAnimation(slideUpAnimation);
    }

    public static void slideDown(Context context, View view) {
        slideDown(context, view, 0, 0);
    }

    public static void slideDown(Context context, View view, int delay, int duration) {
        view.setVisibility(View.VISIBLE);
        Animation slideDownAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_down_exit);
        slideDownAnimation.setStartOffset(delay);
        slideDownAnimation.setDuration(duration);
        view.startAnimation(slideDownAnimation);
    }

    public static void slideLeft(Context context, final View view, long delay, long duration) {
        Animation slideLeftAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_left);
        slideLeftAnimation.setStartOffset(delay);
        slideLeftAnimation.setDuration(duration);
        view.startAnimation(slideLeftAnimation);
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
