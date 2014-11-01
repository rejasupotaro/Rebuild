package rejasupotaro.rebuild.data.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.api.TwitterApiClient;
import rejasupotaro.rebuild.data.models.Tweet;

public class TweetLoader extends AsyncTaskLoader<List<Tweet>> {
    private List<Tweet> tweetList = new ArrayList<Tweet>();
    private boolean isFirstRequest = true;

    public TweetLoader(Context context, boolean isFirstRequest) {
        super(context);
        this.isFirstRequest = isFirstRequest;
    }

    @Override
    protected void onStartLoading() {
        if (!tweetList.isEmpty()) {
            deliverResult(tweetList);
        } else if (takeContentChanged() || tweetList.isEmpty()) {
            forceLoad();
        }
    }

    @Override
    public List<Tweet> loadInBackground() {
        return TwitterApiClient.getInstance().search("rebuildfm", isFirstRequest);
    }

    @Override
    public void deliverResult(List<Tweet> data) {
        if (isReset()) {
            if (!tweetList.isEmpty()) {
                tweetList.clear();
            }
            return;
        }

        tweetList = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
    }
}

