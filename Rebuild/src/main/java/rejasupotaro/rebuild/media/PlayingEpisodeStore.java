package rejasupotaro.rebuild.media;

import android.content.Context;

import rejasupotaro.rebuild.constants.PrefsKey;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.PreferenceUtils;

public class PlayingEpisodeStore {

    public static void save(Context context) {
        PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        Episode episode = podcastPlayer.getEpisode();
        if (episode == null) {
            return;
        }

        int currentPosition = podcastPlayer.getCurrentPosition();
        PreferenceUtils
                .saveInt(context, PrefsKey.PLAYING_EPISODE, episode.getEpisodeId());
        PreferenceUtils.saveInt(context, PrefsKey.CURRENT_POSITION, currentPosition);
    }

    public static StoredEpisode load(Context context) {
        int episodeId = PreferenceUtils.loadInt(context, PrefsKey.PLAYING_EPISODE);
        int currentPosition = PreferenceUtils
                .loadInt(context, PrefsKey.CURRENT_POSITION);
        remove(context);

        return new StoredEpisode(episodeId, currentPosition);
    }

    public static void remove(Context context) {
        PreferenceUtils.remove(context, PrefsKey.PLAYING_EPISODE);
        PreferenceUtils.remove(context, PrefsKey.CURRENT_POSITION);
    }

    public static class StoredEpisode {

        private int episodeId = -1;

        private int currentPosition = -1;

        public int getEpisodeId() {
            return episodeId;
        }

        public int getCurrentPosition() {
            return currentPosition;
        }

        public boolean isEmpty() {
            return (episodeId == -1 || currentPosition == -1);
        }

        public StoredEpisode(int episodeId, int currentPosition) {
            this.episodeId = episodeId;
            this.currentPosition = currentPosition;
        }
    }
}
