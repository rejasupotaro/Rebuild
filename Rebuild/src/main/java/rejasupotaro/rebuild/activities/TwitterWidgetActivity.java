package rejasupotaro.rebuild.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.views.StateFrameLayout;
import rejasupotaro.rebuild.views.TwitterWidgetWebView;
import roboguice.inject.InjectView;

public class TwitterWidgetActivity extends RoboActionBarActivity {

    @InjectView(R.id.state_frame_layout)
    private StateFrameLayout mStateFrameLayout;

    @InjectView(R.id.twitter_widget)
    private TwitterWidgetWebView mTwitterWidgetWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_widget);
        setupActionBar();
        mTwitterWidgetWebView.init(getApplicationContext());
    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        String title = StringUtils.capitalize(getString(R.string.label_timeline));
        actionBar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = false;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }
}
