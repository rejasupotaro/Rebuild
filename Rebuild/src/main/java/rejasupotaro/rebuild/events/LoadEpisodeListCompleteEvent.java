package rejasupotaro.rebuild.events;

import java.util.List;

import rejasupotaro.rebuild.data.models.Episode;

public class LoadEpisodeListCompleteEvent {
    private List<Episode> episodeList;

    public List<Episode> getEpisodeList() {
        return episodeList;
    }

    public LoadEpisodeListCompleteEvent(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }
}
