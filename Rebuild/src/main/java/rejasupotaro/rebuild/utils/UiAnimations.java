package rejasupotaro.rebuild.utils;

import android.animation.ObjectAnimator;
import android.view.View;

public final class UiAnimations {

    private UiAnimations() {
    }

    public static void fadeOut(View view, long delay, long duration) {
        view.setAlpha(1);
        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "alpha", 0);
        animation.setStartDelay(delay);
        animation.setDuration(duration);
        animation.start();

    }
}
