package rejasupotaro.rebuild;

import com.activeandroid.ActiveAndroid;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;

import android.app.Application;
import android.util.Log;

import rejasupotaro.rebuild.api.RssFeedClient;
import rejasupotaro.rebuild.notifications.PodcastPlayerNotification;

import static rejasupotaro.rebuild.ActivityLifecycleObserver.OnActivityStoppedListener;

public class RebuildApplication extends Application {

    private static RebuildApplication instance;

    private JobManager jobManager;

    public RebuildApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        RssFeedClient.init(this);
        setupActivityLifecycleObserver();
        setupJobManager();
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

    private void setupJobManager() {
        Configuration configuration = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";

                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }
                })
                .minConsumerCount(1)    //always keep at least one consumer alive
                .maxConsumerCount(3)    //up to 3 consumers at a time
                .loadFactor(3)          //3 jobs per consumer
                .consumerKeepAlive(120) //wait 2 minute
                .build();
        jobManager = new JobManager(this, configuration);
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public static RebuildApplication getInstance() {
        return instance;
    }
}
