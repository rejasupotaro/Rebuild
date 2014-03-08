package rejasupotaro.rebuild.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.activities.MainActivity;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.ReceivePauseActionEvent;
import rejasupotaro.rebuild.events.ReceiveResumeActionEvent;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.services.PodcastPlayerService;
import rejasupotaro.rebuild.utils.DateUtils;

public class PodcastPlayerNotification {

    private static final String ACTION_TOGGLE_PLAYBACK = "action_toggle_playback";

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

        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle(episode.getTitle());
        builder.setContentText(episode.getDescription());

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

        builder.setProgress(DateUtils.durationToInt(episode.getDuration()), currentPosition, false);

        builder.setContentIntent(getLaunchIntent(context, episode.getEpisodeId()));

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;

        return notification;
    }

    private static PendingIntent getToggleIntent(Context context) {
        Intent toggleIntent = new Intent(context, PodcastPlayerService.class);
        toggleIntent.setAction(ACTION_TOGGLE_PLAYBACK);
        return PendingIntent.getService(
                context, 0, toggleIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private static PendingIntent getLaunchIntent(Context context, int episodeId) {
        return PendingIntent.getActivity(context, 0,
                MainActivity.createIntent(context, episodeId), Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public static void cancel(Context context) {
        if (context == null) return;

        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(getNotificationId());
    }

    public static void handleAction(Context context, String action) {
        if (TextUtils.isEmpty(action)) return;

        if (action.equals(ACTION_TOGGLE_PLAYBACK)) {
            if (PodcastPlayer.getInstance().isPlaying()) {
                PodcastPlayer.getInstance().pause();
                BusProvider.getInstance().post(new ReceivePauseActionEvent());
            } else {
                PodcastPlayer.getInstance().start();
                BusProvider.getInstance().post(new ReceiveResumeActionEvent());
            }
            // Update the notification itself
            PodcastPlayerNotification.notify(context, PodcastPlayer.getInstance().getEpisode(), PodcastPlayer.getInstance().getCurrentPosition());
        }
    }

    public static int getNotificationId() {
        return NotificationId.PodcastPlayer;
    }
}
