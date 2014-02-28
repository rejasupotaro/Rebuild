package rejasupotaro.rebuild.events;

import rejasupotaro.rebuild.models.Episode;

public class DownloadEpisodeCompleteEvent {

    private Episode episode;

    public Episode getEpisode() {
        return episode;
    }

    public DownloadEpisodeCompleteEvent(Episode episode) {
        this.episode = episode;
    }

}
