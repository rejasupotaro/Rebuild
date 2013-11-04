package rejasupotaro.rebuild.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

public class PodcastPlayer extends MediaPlayer implements MediaPlayer.OnPreparedListener {

    public static final String TAG = PodcastPlayer.class.getSimpleName();

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

    public void play(Context context, Uri uri) {
        if (isPlaying()) {
            reset();
        }

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
        start();
    }
}
