package rejasupotaro.rebuild.activities;

import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.api.TwitterApiClient;
import rejasupotaro.rebuild.loaders.TweetLoader;
import rejasupotaro.rebuild.tools.MenuDelegate;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.views.StateFrameLayout;
import roboguice.inject.InjectView;
import twitter4j.TweetEntity;

public class TimelineActivity extends RoboActionBarActivity {

    private static final int REQUEST_TWEET_LIST = 1;

    @InjectView(R.id.state_frame_layout)
    private StateFrameLayout stateFrameLayout;

    @InjectView(R.id.tweet_list)
    private ListView tweetListView;

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
        getLoaderManager().initLoader(REQUEST_TWEET_LIST, null, new LoaderManager.LoaderCallbacks<List<TweetEntity>>() {
            @Override
            public Loader<List<TweetEntity>> onCreateLoader(int i, Bundle bundle) {
                return new TweetLoader(TimelineActivity.this);
            }

            @Override
            public void onLoadFinished(Loader<List<TweetEntity>> listLoader,
                    List<TweetEntity> tweetEntities) {
                // TODO: impl me
            }

            @Override
            public void onLoaderReset(Loader<List<TweetEntity>> listLoader) {
                // nothing to do
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuDelegate.onItemSelect(item);
    }
}