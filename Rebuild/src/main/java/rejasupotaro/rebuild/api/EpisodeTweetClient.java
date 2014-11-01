package rejasupotaro.rebuild.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.data.models.Tweet;
import rejasupotaro.rebuild.utils.ListUtils;

public class EpisodeTweetClient extends AbstractHttpClient {
    private static final String TAG = EpisodeTweetClient.class.getSimpleName();
    private static final String DATA_URL_BASE = "https://raw.githubusercontent.com/rejasupotaro/episode_timeline/master/data/";
    private static final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private static final TwitterApiClient twitterApiClient = TwitterApiClient.getInstance();

    private List<Long> tweetIds = new ArrayList<Long>();
    private boolean isRequesting = false;

    public static interface EpisodeTweetResponseHandler {
        public void onSuccess(int page, List<Tweet> tweetList);
        public void onError();
    }

    @Override
    protected String getTag() {
        return TAG;
    }

    public void fetch(String episodeId, final int page, final int perPage,
            final EpisodeTweetResponseHandler responseHandler) {
        if (isRequesting) {
            return;
        }
        isRequesting = true;

        if (tweetIds.size() > 0) {
            findTweetById(page, perPage, tweetIds, responseHandler);
            return;
        }
        tweetIds.clear();

        asyncHttpClient.get(
                DATA_URL_BASE + episodeId,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                            final byte[] responseBody) {
                        if (responseBody == null || responseBody.length <= 0) {
                            responseHandler.onError();
                            isRequesting = false;
                            return;
                        }

                        try {
                            JSONArray jsonArray = new JSONArray(new String(responseBody));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tweetIds.add(jsonArray.getLong(i));
                            }
                            findTweetById(page, perPage, tweetIds, responseHandler);
                        } catch (JSONException e) {
                            responseHandler.onError();
                            isRequesting = false;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                            Throwable error) {
                        responseHandler.onError();
                        isRequesting = false;
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

                return twitterApiClient.findTweetById(filteredIds);
            }

            @Override
            protected void onPostExecute(List<Tweet> tweetList) {
                if (tweetList == null || tweetList.size() == 0) {
                    responseHandler.onError();
                } else {
                    responseHandler.onSuccess(page, tweetList);
                }
                isRequesting = false;
            }
        }.execute();
    }

}
