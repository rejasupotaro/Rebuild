package rejasupotaro.rebuild.media;

import android.media.MediaPlayer;

public class PodcastPlayer extends MediaPlayer {

    private static PodcastPlayer sInstance;

    private PodcastPlayer() {
        super();
    }

    public static PodcastPlayer getInstance() {
        if (sInstance == null) {
            sInstance = new PodcastPlayer();
        }
        return sInstance;
    }
}
