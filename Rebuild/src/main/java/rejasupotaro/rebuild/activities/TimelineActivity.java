package rejasupotaro.rebuild.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.listener.LoadListener;
import rejasupotaro.rebuild.tools.MenuDelegate;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.views.StateFrameLayout;
import rejasupotaro.rebuild.views.TwitterWidgetWebView;
import roboguice.inject.InjectView;

public class TimelineActivity extends RoboActionBarActivity {

    @InjectView(R.id.state_frame_layout)
    private StateFrameLayout stateFrameLayout;

    @Inject
    private MenuDelegate menuDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_widget);
        setupActionBar();
    }

    private LoadListener mLoadListener = new LoadListener() {
        @Override
        public void showProgress() {
            stateFrameLayout.showProgress();
        }

        @Override
        public void showError() {
            stateFrameLayout.showError();
        }

        @Override
        public void showContent() {
            stateFrameLayout.showContent();
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
