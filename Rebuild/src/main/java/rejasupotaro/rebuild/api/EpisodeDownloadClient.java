package rejasupotaro.rebuild.api;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import rejasupotaro.rebuild.media.MediaFileManager;
import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.utils.NetworkUtils;

public class EpisodeDownloadClient extends AbstractHttpClient {
    private static final String TAG = EpisodeDownloadClient.class.getSimpleName();

    protected String getTag() {
        return TAG;
    }

    public void download(Context context, final Episode episode) {
        final Context applicationContext = context.getApplicationContext();
        try {
            Uri enclosure = episode.getEnclosure();
            if (enclosure == null) {
                return;
            }

            URL url = new URL(enclosure.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            NetworkUtils.setUserAgent(context, urlConnection);
            String mediaLocalPath = MediaFileManager.saveMediaToFile(
                    applicationContext,
                    urlConnection.getInputStream(),
                    episode);
            if (TextUtils.isEmpty(mediaLocalPath)) {
                Log.e(TAG, "Download failed: " + episode.getEnclosure());
            } else {
                episode.insertMediaLocalPath(mediaLocalPath);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Download failed: " + episode.getEnclosure(), e);
        } catch (IOException e) {
            Log.e(TAG, "Download failed: " + episode.getEnclosure(), e);
        }
    }
}
