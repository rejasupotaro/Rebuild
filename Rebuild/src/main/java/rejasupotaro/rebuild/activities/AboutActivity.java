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
    private WebView aboutView;

    @InjectView(R.id.rebuildfm)
    private View listItemRebuild;

    @InjectView(R.id.rejasupotaro)
    private View listItemRejasupotaro;

    @InjectView(R.id.hotchemi)
    private View listItemHotchemi;

    @Inject
    private MenuDelegate menuDelegate;

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
        listItemRebuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openGitHubRepository(AboutActivity.this);
            }
        });
        listItemRejasupotaro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openMyTwitter(AboutActivity.this);
            }
        });
        listItemHotchemi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openTwitterProfile(AboutActivity.this, "hotchemi");
            }
        });
    }

    private void setupLicensesView() {
        aboutView.getSettings().setJavaScriptEnabled(false);
        aboutView.loadUrl(ABOUT_FILE_PATH);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuDelegate.onItemSelect(item);
    }
}
