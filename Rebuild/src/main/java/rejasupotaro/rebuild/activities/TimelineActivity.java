package rejasupotaro.rebuild.activities;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.data.adapters.TweetListAdapter;
import rejasupotaro.rebuild.listener.MoreLoadListener;
import rejasupotaro.rebuild.data.loaders.TweetLoader;
import rejasupotaro.rebuild.data.models.Tweet;
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.views.StateFrameLayout;

public class TimelineActivity extends ActionBarActivity {
    private static final int REQUEST_TWEET_LIST = 1;

    @InjectView(R.id.state_frame_layout)
    StateFrameLayout stateFrameLayout;
    @InjectView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.tweet_list)
    ListView tweetListView;

    private TweetListAdapter tweetListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.inject(this);

        setupActionBar();
        setupTweetListView();
        setupSwipeRefreshLayout();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        String title = StringUtils.capitalize(getString(R.string.label_timeline));
        actionBar.setTitle(title);
    }

    public void setupTweetListView() {
        tweetListView.setOnScrollListener(new MoreLoadListener(this, tweetListView) {
            @Override
            public void onLoadMore() {
                requestTweetList();
            }
        });

        tweetListAdapter = new TweetListAdapter(this);
        tweetListView.setAdapter(tweetListAdapter);

        tweetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Tweet item = tweetListAdapter.getItem(i);
                IntentUtils.openTwitter(TimelineActivity.this, item.getTweetId(),
                        item.getUserName());
            }
        });

        requestTweetList();
    }

    private void setupSwipeRefreshLayout() {
        swipeRefreshLayout.setColorScheme(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        isFirstRequest = true;
        requestTweetList();
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
                        addTweetList(tweetList, isFirstRequest);
                        swipeRefreshLayout.setRefreshing(false);
                        isFirstRequest = false;
                    }

                    @Override
                    public void onLoaderReset(Loader<List<Tweet>> listLoader) {
                        // nothing to do
                    }
                }
        );
    }

    private void addTweetList(List<Tweet> tweetList, boolean isFirstRequest) {
        if (tweetList == null || tweetList.isEmpty()) {
            stateFrameLayout.showError();
            return;
        }

        if (isFirstRequest) {
            tweetListAdapter.clear();
        }

        tweetListAdapter.addAll(tweetList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_settings:
                startActivity(SettingsActivity.createIntent(this));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
