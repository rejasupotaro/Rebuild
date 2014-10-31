package rejasupotaro.rebuild.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.notifications.PodcastPlayerNotification;

public class PodcastPlayerService extends Service {
    @Override
    public void onCreate() {
        BusProvider.getInstance().register(this);
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
