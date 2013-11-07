package rejasupotaro.rebuild.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.squareup.otto.Subscribe;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.activities.MainActivity;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.PodcastPauseButtonClickEvent;
import rejasupotaro.rebuild.events.PodcastPlayButtonClickEvent;
import rejasupotaro.rebuild.models.Episode;

public class PodcastPlayerService extends Service {

    private static final int PLAYING_EPISODE_NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        BusProvider.getInstance().register(this);
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
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(
                PLAYING_EPISODE_NOTIFICATION_ID,
                buildPlayingEpisodeNotification(event.getEpisode()));
    }

    @Subscribe
    public void onPodcastPauseButtonClick(PodcastPauseButtonClickEvent event) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(PLAYING_EPISODE_NOTIFICATION_ID);
    }

    private Notification buildPlayingEpisodeNotification(Episode episode) {
        Intent notificationIntent = MainActivity.createIntent(this, episode);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext());
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle(episode.getTitle());
        builder.setContentText(episode.getDescription());
        builder.setLargeIcon(largeIcon);

        return builder.build();
    }
}
