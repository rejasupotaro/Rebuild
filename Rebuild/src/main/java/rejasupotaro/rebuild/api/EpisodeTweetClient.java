package rejasupotaro.rebuild.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.models.Tweet;
import rejasupotaro.rebuild.utils.ListUtils;

public class EpisodeTweetClient extends AbstractHttpClient {

    private static final String TAG = EpisodeTweetClient.class.getSimpleName();

    private static final AsyncHttpClient ASYNC_HTTP_CLIENT = new AsyncHttpClient();

    private static final TwitterApiClient TWITTER_API_CLIENT = TwitterApiClient.getInstance();

    private List<Long> tweetIds = new ArrayList<Long>();

    public static interface EpisodeTweetResponseHandler {

        public void onSuccess(List<Tweet> tweetList);

        public void onError();
    }

    @Override
    protected String getTag() {
        return TAG;
    }

    public void fetch(int episodeId, final int page, final int perPage, final EpisodeTweetResponseHandler responseHandler) {
        if (tweetIds.size() > 0) {
            findTweetById(page, perPage, tweetIds, responseHandler);
            return;
        }
        tweetIds.clear();

        ASYNC_HTTP_CLIENT.get(
                "https://raw.githubusercontent.com/rejasupotaro/episode_timeline/master/data/" + episodeId,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                            final byte[] responseBody) {
                        if (responseBody == null || responseBody.length <= 0) {
                            responseHandler.onError();
                        }

                        try {
                            JSONArray jsonArray = new JSONArray(new String(responseBody));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tweetIds.add(jsonArray.getLong(i));
                            }
                            findTweetById(page, perPage, tweetIds, responseHandler);
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

    private void findTweetById(final int page, final int perPage, final List<Long> tweetIds,
            final EpisodeTweetResponseHandler responseHandler) {
        new AsyncTask<Void, Void, List<Tweet>>() {
            @Override
            protected List<Tweet> doInBackground(Void... params) {
                List<Long> filteredIds = ListUtils.filterByPage(page, perPage, tweetIds);
                if (filteredIds == null || filteredIds.size() <= 0) {
                    return null;
                }

                return TWITTER_API_CLIENT.findTweetById(filteredIds);
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

}
