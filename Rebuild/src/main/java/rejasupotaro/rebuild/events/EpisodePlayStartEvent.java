package rejasupotaro.rebuild.events;

public class EpisodePlayStartEvent {

    private int episodeId;

    public int getEpisodeId() {
        return episodeId;
    }

    public EpisodePlayStartEvent(int episodeId) {
        this.episodeId = episodeId;
    }
}
