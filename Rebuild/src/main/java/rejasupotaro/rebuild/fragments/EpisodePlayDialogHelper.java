package rejasupotaro.rebuild.fragments;

import com.google.inject.Inject;

import android.content.Context;

import rejasupotaro.rebuild.Injectable;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.ClearEpisodeCacheEvent;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.services.EpisodeDownloadService;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class EpisodePlayDialogHelper implements Injectable {

    @Inject
    private Context context;

    private OnSelectListener onSelectListener;

    public interface OnSelectListener {
        public void playNow(Episode episode);
        public void startStreaming(Episode episode);
    }

    public void setSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public void playNow(Episode episode) {
        onSelectListener.playNow(episode);
    }

    public void clearCache(Episode episode) {
        episode.clearCache();
        episode.save();
        BusProvider.getInstance().post(new ClearEpisodeCacheEvent());
    }

    public void startStreaming(Episode episode) {
        onSelectListener.startStreaming(episode);
    }

    public void startDownload(Episode episode) {
        EpisodeDownloadService.startDownload(context, episode);
    }
}
