package rejasupotaro.rebuild.services;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.notifications.PodcastPlayerNotification;

public class PodcastPlayerService extends Service {

    @Override
    public void onCreate() {
        BusProvider.getInstance().register(this);

        Application app = (Application)this.getApplicationContext();
        if (app != null) {
            app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                int activities = 1;
                boolean isInBackground = false;

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
                public void onActivityDestroyed(Activity activity) {}

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

                @Override
                public void onActivityStarted(Activity activity) {}

                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

                @Override
                public void onActivityStopped(Activity activity) {
                    if (activities == 0) {
                        isInBackground = true;
                        PodcastPlayerNotification.setIsInBackground(true);
                    }
                }
            });
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PodcastPlayerNotification.handleAction(this, intent.getAction());
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
    }
}
