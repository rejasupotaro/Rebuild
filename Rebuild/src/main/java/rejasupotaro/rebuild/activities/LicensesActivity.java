package rejasupotaro.rebuild.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import javax.inject.Inject;

import rejasupotaro.rebuild.ErrorReporter;
import rejasupotaro.rebuild.R;
import roboguice.inject.InjectView;

public class LicensesActivity extends RoboActionBarActivity {

    private static final String LICENSES_FILE_PATH = "file:///android_asset/licenses.html";

    @Inject
    private ErrorReporter mErrorReporter;

    @InjectView(R.id.licenses_view)
    private WebView mLicensesView;

    @Override
    public void onDestroy() {
        mErrorReporter.unregisterActivity();
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mErrorReporter.registerActivity(this);
        setContentView(R.layout.activity_licenses);
        setupActionBar();
        setupLicensesView();
    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void setupLicensesView() {
        mLicensesView.getSettings().setJavaScriptEnabled(false);
        mLicensesView.loadUrl(LICENSES_FILE_PATH);
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
