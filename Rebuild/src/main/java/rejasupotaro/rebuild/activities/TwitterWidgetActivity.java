package rejasupotaro.rebuild.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.tools.MenuDelegate;
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.views.StateFrameLayout;
import rejasupotaro.rebuild.views.TwitterWidgetWebView;
import roboguice.inject.InjectView;

import static rejasupotaro.rebuild.views.TwitterWidgetWebView.*;

public class TwitterWidgetActivity extends RoboActionBarActivity {

    @InjectView(R.id.state_frame_layout)
    private StateFrameLayout stateFrameLayout;

    @InjectView(R.id.twitter_widget)
    private TwitterWidgetWebView twitterWidgetWebView;

    @Inject
    private MenuDelegate menuDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_widget);
        setupActionBar();
        twitterWidgetWebView.init(getApplicationContext(), mLoadListener);
    }

    private LoadListener mLoadListener = new LoadListener() {
        @Override
        public void onStart() {
            stateFrameLayout.showProgress();
        }

        @Override
        public void onError(int errorCode) {
            stateFrameLayout.showError();
        }

        @Override
        public void onFinish() {
            stateFrameLayout.showContent();
        }

        @Override
        public void onSearch(String url) {
            IntentUtils.openBrowser(TwitterWidgetActivity.this, url);
            finish();
        }
    };

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        String title = StringUtils.capitalize(getString(R.string.label_timeline));
        actionBar.setTitle(title);
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
