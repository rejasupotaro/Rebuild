package rejasupotaro.rebuild.events;

public class DownloadEpisodeCompleteEvent {
    private String episodeId;

    public String getEpisodeId() {
        return episodeId;
    }

    public DownloadEpisodeCompleteEvent(String episodeId) {
        this.episodeId = episodeId;
    }

}
