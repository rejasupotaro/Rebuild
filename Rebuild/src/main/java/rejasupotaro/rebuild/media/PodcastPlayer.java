package rejasupotaro.rebuild.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.NetworkUtils;
import rejasupotaro.rebuild.utils.Timer;

public class PodcastPlayer extends MediaPlayer implements MediaPlayer.OnPreparedListener {

    public static final String TAG = PodcastPlayer.class.getSimpleName();

    private static PodcastPlayer instance;

    private Timer timer;

    private Episode episode;

    private StateChangedListener stateChangedListener;

    private PlayerStatus playerStatus = PlayerStatus.STOPPED;

    @Override
    public boolean isPlaying() {
        return (playerStatus == PlayerStatus.PLAYING);
    }

    public boolean isStopped() {
        return (playerStatus == PlayerStatus.STOPPED);
    }

    private PodcastPlayer() {
        super();
    }

    public static PodcastPlayer getInstance() {
        if (instance == null) {
            instance = new PodcastPlayer();
        }
        return instance;
    }

    public Episode getEpisode() {
        return episode;
    }

    public boolean isPlayingEpisode(Episode other) {
        if (other == null) return false;
        return other.isSameEpisode(episode);
    }

    public void setCurrentTimeListener(final CurrentTimeListener currentTimeListener) {
        timer = new Timer(new Timer.Callback() {
            @Override
            public void tick(long timeMillis) {
                currentTimeListener.onTick(getCurrentPosition());
            }
        });
        timer.start();
    }

    public void start(Context context, Episode episode, StateChangedListener stateChangedListener) {
        playerStatus = playerStatus.PREPARING;

        this.episode = episode;
        this.stateChangedListener = stateChangedListener;

        reset();
        try {
            if (episode.isDownloaded()) {
                setDataSource(episode.getMediaLocalPath());
            } else {
                Context applicationContext = context.getApplicationContext();
                setDataSource(
                        applicationContext,
                        episode.getEnclosure(),
                        NetworkUtils.createUserAgentHeader(applicationContext));
            }
            prepareAsync();
            setOnPreparedListener(this);
        } catch (IOException e) {
            Log.e(TAG, "An error occurred while preparing data source: " + episode.getEnclosure().toString());
        }
    }

    @Override
    public void pause() {
        super.pause();
        playerStatus = PlayerStatus.PAUSED;
    }

    @Override
    public void stop() {
        if (isPlaying()) {
            super.pause();
        }
        super.seekTo(0);
        playerStatus = PlayerStatus.STOPPED;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playerStatus = PlayerStatus.PREPARED;
        start();
        stateChangedListener.onStart();
        playerStatus = PlayerStatus.PLAYING;
    }

    public static interface StateChangedListener {
        public void onStart();
    }

    public static interface CurrentTimeListener {
        public void onTick(int currentPosition);
    }

    public enum PlayerStatus {
        PREPARING,
        PAUSED,
        PLAYING,
        STOPPED,
        PREPARED,
    }
}
