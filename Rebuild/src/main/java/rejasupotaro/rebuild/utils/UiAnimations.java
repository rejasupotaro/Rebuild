package rejasupotaro.rebuild.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

public final class UiAnimations {

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
