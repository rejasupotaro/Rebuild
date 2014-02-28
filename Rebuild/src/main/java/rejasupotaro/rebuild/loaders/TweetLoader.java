package rejasupotaro.rebuild.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.api.TwitterApiClient;
import twitter4j.TweetEntity;

public class TweetLoader extends AsyncTaskLoader<List<TweetEntity>> {

    private List<TweetEntity> tweetEntityList = new ArrayList<TweetEntity>();

    public TweetLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (!tweetEntityList.isEmpty()) {
            deliverResult(tweetEntityList);
        } else if (takeContentChanged() || tweetEntityList.isEmpty()) {
            forceLoad();
        }
    }

    @Override
    public List<TweetEntity> loadInBackground() {
        return TwitterApiClient.getInstance().search("rebuildfm");
    }

    @Override
    public void deliverResult(List<TweetEntity> data) {
        if (isReset()) {
            if (!tweetEntityList.isEmpty()) {
                tweetEntityList.clear();
            }
            return;
        }

        tweetEntityList = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
    }
}

