package rejasupotaro.rebuild.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.api.EpisodeDownloadClient;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.DownloadEpisodeCompleteEvent;
import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.notifications.EpisodeDownloadCompleteNotificaiton;
import rejasupotaro.rebuild.notifications.EpisodeDownloadNotification;

public class EpisodeDownloadService extends IntentService {
    private static final String TAG = EpisodeDownloadService.class.getSimpleName();
    private static final String EXTRA_EPISODE = "extra_episode";

    private static List<Episode> downloadingEpisodeList = new ArrayList<Episode>();
    private static EpisodeDownloadClient episodeDownloadClient = new EpisodeDownloadClient();

    public static Intent createIntent(Context context, Episode episode) {
        Intent intent = new Intent(context, EpisodeDownloadService.class);
        intent.putExtra(EXTRA_EPISODE, episode);
        return intent;
    }

    public static void startDownload(Context context, Episode episode) {
        Intent intent = EpisodeDownloadService.createIntent(context, episode);
        context.startService(intent);
    }

    public EpisodeDownloadService() {
        super(TAG);
    }

    public EpisodeDownloadService(String name) {
        super(name);
    }

    public static boolean isDownloading(Episode episode) {
        int index = getEpisodeIndexFromDownloadingList(episode);
        return (index != -1);
    }

    public static void cancel(Context context, Episode episode) {
        int index = getEpisodeIndexFromDownloadingList(episode);
        if (index == -1) {
            return;
        }

        removeEpisodeFromDownloadingList(episode);
        EpisodeDownloadNotification.cancel(context, episode);
    }

    public static int getEpisodeIndexFromDownloadingList(Episode episode) {
        if (episode == null) {
            return -1;
        }

        int index = 0;
        while (index < downloadingEpisodeList.size()) {
            Episode downloadingEpisode = downloadingEpisodeList.get(index);
            if (downloadingEpisode.isSameEpisode(episode)) {
                return index;
            }
            index++;
        };

        return -1;
    }

    public static void removeEpisodeFromDownloadingList(Episode episode) {
        if (episode == null) {
            return;
        }

        int index = getEpisodeIndexFromDownloadingList(episode);
        if (index == -1) {
            return;
        }

        downloadingEpisodeList.remove(index);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Episode episode = intent.getParcelableExtra(EXTRA_EPISODE);
        if (episode == null || isDownloading(episode)) {
            return;
        }

        try {
            EpisodeDownloadNotification.notify(this, episode);

            downloadingEpisodeList.add(episode);
            episodeDownloadClient.download(getApplicationContext(), episode);

            if (isDownloading(episode)) {
                downloadingEpisodeList.remove(episode);
                episode.save();
                BusProvider.getInstance().post(new DownloadEpisodeCompleteEvent(episode.getEpisodeId()));
                EpisodeDownloadCompleteNotificaiton.notify(this, episode);
            } else {
                episode.clearCache();
            }
        } finally {
            EpisodeDownloadNotification.cancel(this, episode);
        }
    }
}
