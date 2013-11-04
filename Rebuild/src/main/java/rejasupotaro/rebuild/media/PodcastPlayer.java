package rejasupotaro.rebuild.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

import rejasupotaro.rebuild.utils.Timer;

public class PodcastPlayer extends MediaPlayer implements MediaPlayer.OnPreparedListener {

    public static final String TAG = PodcastPlayer.class.getSimpleName();

    private static PodcastPlayer sInstance;

    private Timer mTimer;

    private StateChangedListener mStateChangedListener;

    private PodcastPlayer() {
        super();
    }

    public static PodcastPlayer getInstance() {
        if (sInstance == null) {
            sInstance = new PodcastPlayer();
        }
        return sInstance;
    }

    public void setCurrentTimeListener(final CurrentTimeListener currentTimeListener) {
        mTimer = new Timer(new Timer.Callback() {
            @Override
            public void tick(long timeMillis) {
                currentTimeListener.onTick(getCurrentPosition());
            }
        });
        mTimer.start();
    }

    public void play(Context context, Uri uri, StateChangedListener stateChangedListener) {
        mStateChangedListener = stateChangedListener;

        reset();
        try {
            Context applicationContext = context.getApplicationContext();
            setDataSource(applicationContext, uri);
            prepareAsync();
            setOnPreparedListener(this);
        } catch (IOException e) {
            Log.e(TAG, "An error occurred while preparing data source: " + uri.toString());
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mStateChangedListener.onStart();
        start();
    }

    public static interface StateChangedListener {
        public void onStart();
    }

    public static interface CurrentTimeListener {
        public void onTick(int currentPosition);
    }
}
