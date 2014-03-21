package rejasupotaro.rebuild.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;

import java.util.List;

import rejasupotaro.rebuild.models.Tweet;

public class EpisodeTweetClient extends AbstractHttpClient {

    private static final String TAG = EpisodeTweetClient.class.getSimpleName();

    private static final AsyncHttpClient ASYNC_HTTP_CLIENT = new AsyncHttpClient();

    private static final TwitterApiClient TWITTER_API_CLIENT = TwitterApiClient.getInstance();

    public static interface EpisodeTweetResponseHandler {

        public void onSuccess(List<Tweet> tweetList);

        public void onError();
    }

    @Override
    protected String getTag() {
        return TAG;
    }

    public void fetch(final EpisodeTweetResponseHandler responseHandler) {
        ASYNC_HTTP_CLIENT.get(
                "https://gist.githubusercontent.com/rejasupotaro/9683475/raw/f0ce55ee888e5859f7e79760836038528887a5dc/timeline.json",
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                            final byte[] responseBody) {
                        if (responseBody == null || responseBody.length <= 0) {
                            responseHandler.onError();
                        }

                        new AsyncTask<Void, Void, List<Tweet>>() {

                            @Override
                            protected List<Tweet> doInBackground(Void... params) {
                                try {
                                    JSONArray jsonArray = new JSONArray(new String(responseBody));
                                    return TWITTER_API_CLIENT.findTweetById(jsonArray);
                                } catch (JSONException e) {
                                    responseHandler.onError();
                                    return null;
                                }
                            }

                            @Override
                            protected void onPostExecute(List<Tweet> tweetList) {
                                if (tweetList == null || tweetList.size() == 0) {
                                    responseHandler.onError();
                                } else {
                                    responseHandler.onSuccess(tweetList);
                                }
                            }
                        }.execute();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                            Throwable error) {
                        responseHandler.onError();
                    }
                });
    }
}
