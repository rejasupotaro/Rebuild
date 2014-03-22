package rejasupotaro.rebuild.activities;

import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.adapters.TweetListAdapter;
import rejasupotaro.rebuild.listener.MoreLoadListener;
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

    @InjectView(R.id.compose_tweet_button)
    private View composeTweetButton;

    private TweetListAdapter tweetListAdapter;

    @Inject
    private MenuDelegate menuDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        setupActionBar();
        setupTweetListView();
        setupComposeTweetButton();
    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
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
                IntentUtils.openTwitter(TimelineActivity.this, item.getEpisodeId(), item.getUserName());
            }
        });

        requestTweetList();
    }

    private void setupComposeTweetButton() {
        composeTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.sendPostIntent(TimelineActivity.this, "#rebuildfm");
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
            case android.R.id.home: {
                menuDelegate.pressHome();
                break;
            }
            case R.id.action_settings: {
                menuDelegate.pressSettings();
                break;
            }
            default: {
                result = super.onOptionsItemSelected(item);
                break;
            }
        }
        return result;
    }
}
