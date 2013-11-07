package rejasupotaro.rebuild.events;

import rejasupotaro.rebuild.models.Episode;

public class PodcastPlayButtonClickEvent {

    private Episode mEpisode;

    public Episode getEpisode() {
        return mEpisode;
    }

    public PodcastPlayButtonClickEvent(Episode episode) {
        mEpisode = episode;
    }
}
