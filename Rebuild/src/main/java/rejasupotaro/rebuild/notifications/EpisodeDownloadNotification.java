package rejasupotaro.rebuild.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.activities.MainActivity;
import rejasupotaro.rebuild.models.Episode;

public class EpisodeDownloadNotification {

    private static final String ACTION_CANCEL = "action_cancel";

    public static void notify(Context context, Episode episode) {
        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(
                getNotificationId(episode),
                build(context, episode));
    }

    private static Notification build(Context context, Episode episode) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(android.R.drawable.stat_sys_download);
        builder.setContentTitle(context.getString(R.string.downloading));
        builder.setContentText(episode.getTitle());

        PendingIntent launchDetail = PendingIntent.getActivity(context, 0,
                MainActivity.createIntent(context, episode.getEpisodeId()), Intent.FLAG_ACTIVITY_NEW_TASK);
        builder.setContentIntent(launchDetail);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;

        return notification;
    }

    public static void cancel(Context context, Episode episode) {
        if (context == null) {
            return;
        }

        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(getNotificationId(episode));
    }

    public static int getNotificationId(Episode episode) {
        return NotificationId.EpisodeDownload + episode.getEpisodeId();
    }
}
