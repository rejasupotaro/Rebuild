package rejasupotaro.rebuild.activities;

import android.app.ActionBar;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.tools.MenuDelegate;
import roboguice.inject.InjectView;

public class LicensesActivity extends RoboActionBarActivity {

    private static final String LICENSES_FILE_PATH = "file:///android_asset/licenses.html";

    @InjectView(R.id.licenses_view)
    private WebView licensesView;

    @Inject
    private MenuDelegate menuDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        licensesView.getSettings().setJavaScriptEnabled(false);
        licensesView.loadUrl(LICENSES_FILE_PATH);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT >= 19) {
            //In SDK4.4~, it has translucent navigation bar and status bar
            View view = findViewById(R.id.licenses_view_container);
            view.setPadding(0, getStatusbarHeight() + getActionbarHeight(), 0, 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuDelegate.onItemSelect(item);
    }

    private int getStatusbarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private int getActionbarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

}
