package rejasupotaro.rebuild.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.ReceivePauseActionEvent;
import rejasupotaro.rebuild.events.ReceiveResumeActionEvent;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.services.PodcastPlayerService;
import rejasupotaro.rebuild.utils.DateUtils;
import rejasupotaro.rebuild.utils.StringUtils;

public class PodcastPlayerNotification {
    private static final String ACTION_TOGGLE_PLAYBACK = "action_toggle_playback";
    private static final String ACTION_STOP = "action_stop";
    private static boolean isInBackground = false;

    public static void setIsInBackground(boolean isinb) {
        isInBackground = isinb;
    }

    public static void notify(Context context, Episode episode) {
        notify(context, episode, 0);
    }

    public static void notify(Context context, Episode episode, int currentPosition) {
        if (!shouldNotify(context, episode)) {
            return;
        }

        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(
                getNotificationId(),
                build(context, episode, currentPosition));
    }

    private static boolean shouldNotify(Context context, Episode episode) {
        PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        return (isInBackground
                && episode != null
                && context != null
                && (podcastPlayer.isPlaying() || podcastPlayer.isPaused()));
    }

    private static Notification build(Context context, Episode episode, int currentPosition) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.icon);
        builder.setContentTitle(episode.getTitle());
        builder.setContentText(StringUtils.removeHtmlTags(episode.getDescription()));
        builder.setProgress(DateUtils.durationToInt(episode.getDuration()), currentPosition, false);

        if (PodcastPlayer.getInstance().isPlaying()) {
            builder.addAction(
                    android.R.drawable.ic_media_pause,
                    context.getString(R.string.notification_pause),
                    getToggleIntent(context));
        } else {
            builder.addAction(
                    android.R.drawable.ic_media_play,
                    context.getString(R.string.notification_resume),
                    getToggleIntent(context));
        }

        builder.addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                context.getString(R.string.notification_stop),
                getStopIntent(context));

        PendingIntent launchIntent = NotificationDelegate.getLauchEpisodeDetailIntent(context,
                episode.getEpisodeId());
        builder.setContentIntent(launchIntent);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;

        return notification;
    }

    private static PendingIntent getToggleIntent(Context context) {
        Intent intent = new Intent(context, PodcastPlayerService.class);
        intent.setAction(ACTION_TOGGLE_PLAYBACK);
        return PendingIntent.getService(
                context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private static PendingIntent getStopIntent(Context context) {
        Intent intent = new Intent(context, PodcastPlayerService.class);
        intent.setAction(ACTION_STOP);
        return PendingIntent.getService(
                context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public static void cancel(Context context) {
        if (context == null) {
            return;
        }

        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(getNotificationId());
    }

    public static void handleAction(Context context, String action) {
        if (TextUtils.isEmpty(action)) {
            return;
        }

        if (action.equals(ACTION_TOGGLE_PLAYBACK)) {
            if (PodcastPlayer.getInstance().isPlaying()) {
                PodcastPlayer.getInstance().pause();
                BusProvider.getInstance().post(new ReceivePauseActionEvent());
            } else {
                PodcastPlayer.getInstance().restart();
                BusProvider.getInstance().post(new ReceiveResumeActionEvent());
            }
            // Update the notification itself
            PodcastPlayerNotification.notify(
                    context,
                    PodcastPlayer.getInstance().getEpisode(),
                    PodcastPlayer.getInstance().getCurrentPosition());
        } else if (action.equals(ACTION_STOP)) {
            PodcastPlayer.getInstance().stop();
            cancel(context);
        }
    }

    public static int getNotificationId() {
        return NotificationId.PodcastPlayer;
    }
}
