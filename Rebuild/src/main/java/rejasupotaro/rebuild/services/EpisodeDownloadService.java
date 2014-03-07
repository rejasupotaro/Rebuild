package rejasupotaro.rebuild.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.api.EpisodeDownloadClient;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.DownloadEpisodeCompleteEvent;
import rejasupotaro.rebuild.models.Episode;

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
        for (Episode e : downloadingEpisodeList) {
            if (episode.isSameEpisode(e)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Episode episode = intent.getParcelableExtra(EXTRA_EPISODE);
        if (isDownloading(episode)) {
            return;
        }
        downloadingEpisodeList.add(episode);

        episodeDownloadClient.download(getApplicationContext(), episode);

        episode.save();
        downloadingEpisodeList.remove(episode);
        BusProvider.getInstance().post(new DownloadEpisodeCompleteEvent(episode));
    }
}
