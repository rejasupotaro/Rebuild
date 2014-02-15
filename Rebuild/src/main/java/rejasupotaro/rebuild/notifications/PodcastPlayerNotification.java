package rejasupotaro.rebuild.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.services.PodcastPlayerService;

public class PodcastPlayerNotification {

    private static final int NOTIFICATION_ID = 1;

    private static final String ACTION_PAUSE = "action_pause";

    private Context mContext;

    private NotificationManager mNotificationManager;

    public PodcastPlayerNotification(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
    }

    public void notity(Episode episode) {
        if (episode == null) return;
        mNotificationManager.notify(NOTIFICATION_ID, build(episode));
    }

    private Notification build(Episode episode) {
        Intent pauseIntent = new Intent(mContext, PodcastPlayerService.class);
        pauseIntent.setAction(ACTION_PAUSE);
        PendingIntent piPause = PendingIntent.getService(mContext, 0, pauseIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle(episode.getTitle());
        builder.setContentText(episode.getDescription());
        builder.addAction(android.R.drawable.ic_media_pause, mContext.getString(R.string.notification_pause), piPause);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;

        return notification;
    }

    public void cancel() {
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    public void handleAction(String action) {
        if (TextUtils.isEmpty(action)) return;

        if (action.equals(ACTION_PAUSE)) {
            PodcastPlayer.getInstance().stop();
            cancel();
        }
    }
}
