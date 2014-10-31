package rejasupotaro.rebuild.events;

public class EpisodePlayStartEvent {
    private String episodeId;

    public String getEpisodeId() {
        return episodeId;
    }

    public EpisodePlayStartEvent(String episodeId) {
        this.episodeId = episodeId;
    }
}
