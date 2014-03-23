package rejasupotaro.rebuild.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import rejasupotaro.rebuild.R;

public final class UiAnimations {

    public static void slideUp(Context context, View view) {
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up_enter);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        view.startAnimation(animation);
    }

    public static void slideDown(Context context, View view) {
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_down_exit);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        view.startAnimation(animation);
    }

    public static void slideLeft(final View view, long delay, long duration) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "x", -10);
        animation.setStartDelay(delay);
        animation.setDuration(duration);
        animation.start();
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

    private UiAnimations() {
    }
}
