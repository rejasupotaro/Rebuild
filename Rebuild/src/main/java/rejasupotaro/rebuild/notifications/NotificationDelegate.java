package rejasupotaro.rebuild.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import rejasupotaro.rebuild.activities.MainActivity;

public final class NotificationDelegate {

    public static PendingIntent getLauchEpisodeDetailIntent(Context context, int episodeId) {
        return PendingIntent.getActivity(context, 0,
                MainActivity.createIntent(context, episodeId), Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    private NotificationDelegate() {
    }
}
