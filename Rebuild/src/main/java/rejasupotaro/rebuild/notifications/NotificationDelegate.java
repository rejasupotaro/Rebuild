package rejasupotaro.rebuild.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import rejasupotaro.rebuild.activities.EpisodeListActivity;

public final class NotificationDelegate {
    public static PendingIntent getLauchEpisodeDetailIntent(Context context, String episodeId) {
        return PendingIntent.getActivity(context, 0,
                EpisodeListActivity.createIntent(context, episodeId), Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    private NotificationDelegate() {
    }
}
