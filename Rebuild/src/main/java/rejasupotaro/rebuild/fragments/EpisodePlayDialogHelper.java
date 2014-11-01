package rejasupotaro.rebuild.fragments;

import android.content.Context;

import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.ClearEpisodeCacheEvent;
import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.services.EpisodeDownloadService;

public class EpisodePlayDialogHelper {
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

    public void startDownload(Context context, Episode episode) {
        EpisodeDownloadService.startDownload(context, episode);
    }
}
