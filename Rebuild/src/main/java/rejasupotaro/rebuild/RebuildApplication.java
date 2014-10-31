package rejasupotaro.rebuild;

import com.activeandroid.ActiveAndroid;

import android.app.Application;

import rejasupotaro.rebuild.api.RssFeedClient;
import rejasupotaro.rebuild.notifications.PodcastPlayerNotification;

import static rejasupotaro.rebuild.ActivityLifecycleObserver.OnActivityStoppedListener;

public class RebuildApplication extends Application {
    private static RebuildApplication instance;

    public RebuildApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        RssFeedClient.init(this);
        setupActivityLifecycleObserver();
    }

    @Override
    public void onTerminate() {
        ActiveAndroid.dispose();
        ActivityLifecycleObserver.terminate(this);
        super.onTerminate();
    }

    private void setupActivityLifecycleObserver() {
        ActivityLifecycleObserver.initialize(this, new OnActivityStoppedListener() {
            @Override
            public void onAllStop() {
                PodcastPlayerNotification.setIsInBackground(true);
            }
        });
    }

    public static RebuildApplication getInstance() {
        return instance;
    }
}
