package rejasupotaro.rebuild.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import rejasupotaro.rebuild.media.MediaFileManager;
import rejasupotaro.rebuild.models.Episode;

public class EpisodeDownloadClient extends AbstractHttpClient {

    private static final String TAG = EpisodeDownloadClient.class.getSimpleName();

    protected String getTag() {
        return TAG;
    }

    private static final AsyncHttpClient sAsyncHttpClient = new AsyncHttpClient();

    public void download(final Context context, final Episode episode) {
        final Context applicationContext = context.getApplicationContext();
        sAsyncHttpClient.get(episode.getEnclosure().toString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String mediaLocalPath = MediaFileManager.saveMediaToFile(applicationContext, responseBody, episode);
                if (TextUtils.isEmpty(mediaLocalPath)) {
                    Log.e(TAG, "Download failed: " + episode.getEnclosure());
                } else {
                    episode.setMediaLocalPath(mediaLocalPath);
                    episode.save();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dumpError(headers, responseBody, error);
            }
        });
    }
}
