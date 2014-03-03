package rejasupotaro.rebuild.activities;

import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.adapters.TweetListAdapter;
import rejasupotaro.rebuild.listener.EndlessScrollListener;
import rejasupotaro.rebuild.loaders.TweetLoader;
import rejasupotaro.rebuild.models.Tweet;
import rejasupotaro.rebuild.tools.MenuDelegate;
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.utils.ViewUtils;
import rejasupotaro.rebuild.views.StateFrameLayout;
import roboguice.inject.InjectView;

public class TimelineActivity extends RoboActionBarActivity {

    private static final int REQUEST_TWEET_LIST = 1;

    @InjectView(R.id.state_frame_layout)
    private StateFrameLayout stateFrameLayout;

    @InjectView(R.id.tweet_list)
    private ListView tweetListView;

    private TweetListAdapter tweetListAdapter;

    @Inject
    private MenuDelegate menuDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        setupActionBar();
        setupTweetListView();
    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        String title = StringUtils.capitalize(getString(R.string.label_timeline));
        actionBar.setTitle(title);
    }

    public void setupTweetListView() {
        View footer = View.inflate(this, R.layout.list_item_progress, null);
        ViewUtils.addFooterView(tweetListView, footer);

        tweetListAdapter = new TweetListAdapter(this);
        tweetListView.setAdapter(tweetListAdapter);

        tweetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Tweet item = tweetListAdapter.getItem(i);
                IntentUtils.openTwitter(TimelineActivity.this, item.getId(), item.getUserName());
            }
        });

        tweetListView.setOnScrollListener(new EndlessScrollListener(tweetListView) {
            @Override
            public void onLoadMore() {
                requestTweetList();
            }
        });

        requestTweetList();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT >= 19) {
            //In SDK4.4~, it has translucent navigation bar and status bar
            tweetListView.setPadding(0, getStatusbarHeight() + getActionbarHeight(), 0, 0);
        }
    }

    private boolean isFirstRequest = true;

    private void requestTweetList() {
        getLoaderManager().restartLoader(REQUEST_TWEET_LIST, null,
                new LoaderManager.LoaderCallbacks<List<Tweet>>() {
                    @Override
                    public Loader<List<Tweet>> onCreateLoader(int i, Bundle bundle) {
                        return new TweetLoader(TimelineActivity.this, isFirstRequest);
                    }

                    @Override
                    public void onLoadFinished(Loader<List<Tweet>> listLoader,
                            List<Tweet> tweetList) {
                        isFirstRequest = false;
                        addTweetList(tweetList);
                    }

                    @Override
                    public void onLoaderReset(Loader<List<Tweet>> listLoader) {
                        // nothing to do
                    }
                });
    }

    private void addTweetList(List<Tweet> tweetList) {
        if (tweetList == null || tweetList.isEmpty()) {
            return;
        }
        tweetListAdapter.addAll(tweetList);
    }

    private int getStatusbarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private int getActionbarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }
}
