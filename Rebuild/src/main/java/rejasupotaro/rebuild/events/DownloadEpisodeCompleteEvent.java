package rejasupotaro.rebuild.events;

import rejasupotaro.rebuild.models.Episode;

public class DownloadEpisodeCompleteEvent {

    private Episode mEpisode;

    public Episode getEpisode() {
        return mEpisode;
    }

    public DownloadEpisodeCompleteEvent(Episode episode) {
        mEpisode = episode;
    }

}
