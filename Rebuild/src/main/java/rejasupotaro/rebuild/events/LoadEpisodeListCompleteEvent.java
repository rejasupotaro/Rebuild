package rejasupotaro.rebuild.events;

import java.util.List;

import rejasupotaro.rebuild.models.Episode;

public class LoadEpisodeListCompleteEvent {

    private List<Episode> mEpisodeList;

    public List<Episode> getEpisodeList() {
        return mEpisodeList;
    }

    public LoadEpisodeListCompleteEvent(List<Episode> episodeList) {
        mEpisodeList = episodeList;
    }
}
