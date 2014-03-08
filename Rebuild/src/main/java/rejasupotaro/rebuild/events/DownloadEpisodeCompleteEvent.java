package rejasupotaro.rebuild.events;

public class DownloadEpisodeCompleteEvent {

    private int episodeId;

    public int getEpisodeId() {
        return episodeId;
    }

    public DownloadEpisodeCompleteEvent(int episodeId) {
        this.episodeId = episodeId;
    }

}
