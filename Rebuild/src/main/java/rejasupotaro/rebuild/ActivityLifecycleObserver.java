package rejasupotaro.rebuild;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.notifications.PodcastPlayerNotification;

public class ActivityLifecycleObserver implements Application.ActivityLifecycleCallbacks {
    private static Object LOCK = new Object();
    private static ActivityLifecycleObserver INSTANCE = null;

    private int activities = 0;
    private boolean isInBackground = false;
    private OnActivityStoppedListener onActivityStoppedListener;

    public static interface OnActivityStoppedListener {
        public void onAllStop();
    }

    public static void initialize(Application application, OnActivityStoppedListener onActivityStoppedListener) {
        synchronized (LOCK) {
            if (INSTANCE == null) {
                INSTANCE = new ActivityLifecycleObserver(onActivityStoppedListener);
                application.registerActivityLifecycleCallbacks(INSTANCE);
            }
        }
    }

    public static void terminate(Application application) {
        synchronized (LOCK) {
            if (INSTANCE != null) {
                application.unregisterActivityLifecycleCallbacks(INSTANCE);
                INSTANCE = null;
            }
        }
    }

    private ActivityLifecycleObserver(OnActivityStoppedListener onActivityStoppedListener) {
        this.onActivityStoppedListener = onActivityStoppedListener;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        activities--;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (isInBackground) {
            isInBackground = false;
            PodcastPlayerNotification.setIsInBackground(false);
            PodcastPlayerNotification.cancel(activity);
        }
        activities++;
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activities == 0) {
            isInBackground = true;
            onActivityStoppedListener.onAllStop();
        }
    }
}
