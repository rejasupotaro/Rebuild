package rejasupotaro.rebuild.services;

import com.squareup.otto.Subscribe;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.PodcastPauseButtonClickEvent;
import rejasupotaro.rebuild.events.PodcastPlayButtonClickEvent;
import rejasupotaro.rebuild.notifications.PodcastPlayerNotification;

public class PodcastPlayerService extends Service {

    private PodcastPlayerNotification mPodcastPlayerNotification;

    @Override
    public void onCreate() {
        BusProvider.getInstance().register(this);
        mPodcastPlayerNotification = new PodcastPlayerNotification(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPodcastPlayerNotification.handleAction(intent.getAction());
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

    @Subscribe
    public void onPodcastPlayButtonClick(PodcastPlayButtonClickEvent event) {
        mPodcastPlayerNotification.notity(event.getEpisode());
    }

    @Subscribe
    public void onPodcastPauseButtonClick(PodcastPauseButtonClickEvent event) {
        mPodcastPlayerNotification.cancel();
    }
}
