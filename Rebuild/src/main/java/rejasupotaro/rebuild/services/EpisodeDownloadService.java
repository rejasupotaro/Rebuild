package rejasupotaro.rebuild.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.api.EpisodeDownloadClient;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.DownloadEpisodeCompleteEvent;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.ToastUtils;

public class EpisodeDownloadService extends IntentService {

    private static final String TAG = EpisodeDownloadService.class.getSimpleName();

    private static final String EXTRA_EPISODE = "extra_episode";

    private static EpisodeDownloadClient sEpisodeDownloadClient = new EpisodeDownloadClient();

    public static Intent createIntent(Context context, Episode episode) {
        Intent intent = new Intent(context, EpisodeDownloadService.class);
        intent.putExtra(EXTRA_EPISODE, episode);
        return intent;
    }

    public EpisodeDownloadService() {
        super(TAG);
    }

    public EpisodeDownloadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Episode episode = intent.getParcelableExtra(EXTRA_EPISODE);
        sEpisodeDownloadClient.download(getApplicationContext(), episode);
        ToastUtils.show(this, getString(R.string.episode_download_completed, episode.getTitle()));
        episode.save();
        BusProvider.getInstance().post(new DownloadEpisodeCompleteEvent(episode));
    }
}
