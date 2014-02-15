package rejasupotaro.rebuild.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import javax.inject.Inject;

import rejasupotaro.rebuild.ErrorReporter;
import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.tools.MenuDelegate;
import rejasupotaro.rebuild.utils.IntentUtils;
import roboguice.inject.InjectView;

public class AboutActivity extends RoboActionBarActivity {

    private static final String LICENSES_FILE_PATH = "file:///android_asset/licenses.html";

    @Inject
    private ErrorReporter mErrorReporter;

    @InjectView(R.id.licenses_view)
    private WebView mLicensesView;

    @InjectView(R.id.rejasupotaro)
    private View mMyImageView;

    @Inject
    private MenuDelegate mMenuDelegate;

    @Override
    public void onDestroy() {
        mErrorReporter.unregisterActivity();
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mErrorReporter.registerActivity(this);
        setContentView(R.layout.activity_about);
        setupActionBar();
        setupLicensesView();
    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void setupLicensesView() {
        mMyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openMyProfile(AboutActivity.this);
            }
        });

        mLicensesView.getSettings().setJavaScriptEnabled(false);
        mLicensesView.loadUrl(LICENSES_FILE_PATH);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mMenuDelegate.onItemSelect(item);
    }
}
