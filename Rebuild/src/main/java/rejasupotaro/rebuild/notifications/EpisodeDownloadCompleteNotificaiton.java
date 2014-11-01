package rejasupotaro.rebuild.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.data.models.Episode;

public class EpisodeDownloadCompleteNotificaiton {
    public static void notify(Context context, Episode episode) {
        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(
                getNotificationId(),
                build(context, episode));
    }

    private static Notification build(Context context, Episode episode) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.icon);
        builder.setContentTitle(context.getString(R.string.episode_download_completed));
        builder.setContentText(episode.getTitle());

        PendingIntent launchIntent = NotificationDelegate.getLauchEpisodeDetailIntent(context,
                episode.getEpisodeId());
        builder.setContentIntent(launchIntent);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        return notification;
    }

    public static int getNotificationId() {
        return NotificationId.EpisodeDownloadComplete;
    }
}

