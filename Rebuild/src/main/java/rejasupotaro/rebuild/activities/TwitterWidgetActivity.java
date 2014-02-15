package rejasupotaro.rebuild.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.tools.MenuDelegate;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.views.StateFrameLayout;
import rejasupotaro.rebuild.views.TwitterWidgetWebView;
import roboguice.inject.InjectView;

import static rejasupotaro.rebuild.views.TwitterWidgetWebView.*;

public class TwitterWidgetActivity extends RoboActionBarActivity {

    @InjectView(R.id.state_frame_layout)
    private StateFrameLayout mStateFrameLayout;

    @InjectView(R.id.twitter_widget)
    private TwitterWidgetWebView mTwitterWidgetWebView;

    @Inject
    private MenuDelegate mMenuDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_widget);
        setupActionBar();
        mTwitterWidgetWebView.init(getApplicationContext(), mLoadListener);
    }

    private LoadListener mLoadListener = new LoadListener() {
        @Override
        public void onStart() {
            mStateFrameLayout.showProgress();
        }

        @Override
        public void onError(int errorCode) {
            mStateFrameLayout.showError();
        }

        @Override
        public void onFinish() {
            mStateFrameLayout.showContent();
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
        return mMenuDelegate.onItemSelect(item);
    }
}
