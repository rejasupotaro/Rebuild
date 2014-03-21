package rejasupotaro.rebuild.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import java.util.List;

import rejasupotaro.rebuild.models.Tweet;

public class EpisodeTweetClient extends AbstractHttpClient {

    private static final String TAG = EpisodeTweetClient.class.getSimpleName();

    private static final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public static interface EpisodeTweetResponseHandler {

        public void onSuccess(List<Tweet> tweetList);

        public void onError();
    }

    @Override
    protected String getTag() {
        return TAG;
    }

    public void fetch(final EpisodeTweetResponseHandler responseHandler) {
        asyncHttpClient.get(
                "https://gist.githubusercontent.com/rejasupotaro/9683475/raw/b5b849d1ae0aca47c126d8c0a9b224d618b089d6/timeline.json",
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            JSONArray jsonArray = new JSONArray(new String(responseBody));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                long tweetId = jsonArray.getLong(i);
                                Log.e("debugging", "tweetId: " + tweetId);
                            }
                        } catch (JSONException e) {
                            responseHandler.onError();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                            Throwable error) {
                        responseHandler.onError();
                    }
                });
    }
}
