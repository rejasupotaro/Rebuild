package rejasupotaro.rebuild.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.tools.MenuDelegate;
import rejasupotaro.rebuild.utils.IntentUtils;
import roboguice.inject.InjectView;

public class AboutActivity extends RoboActionBarActivity {

    private static final String ABOUT_FILE_PATH = "file:///android_asset/about.html";

    @InjectView(R.id.about_view)
    private WebView mAboutView;

    @InjectView(R.id.rebuildfm)
    private View mListItemRebuild;

    @InjectView(R.id.rejasupotaro)
    private View mListItemRejasupotaro;

    @InjectView(R.id.hotchemi)
    private View mListItemHotchemi;

    @Inject
    private MenuDelegate mMenuDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setupActionBar();
        setupProfileView();
        setupLicensesView();
    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void setupProfileView() {
        mListItemRebuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openGitHubRepository(AboutActivity.this);
            }
        });
        mListItemRejasupotaro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openMyTwitter(AboutActivity.this);
            }
        });
        mListItemHotchemi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openTwitterProfile(AboutActivity.this, "hotchemi");
            }
        });
    }

    private void setupLicensesView() {
        mAboutView.getSettings().setJavaScriptEnabled(false);
        mAboutView.loadUrl(ABOUT_FILE_PATH);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mMenuDelegate.onItemSelect(item);
    }
}
