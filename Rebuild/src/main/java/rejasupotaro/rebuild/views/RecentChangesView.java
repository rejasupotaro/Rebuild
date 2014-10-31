package rejasupotaro.rebuild.views;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class RecentChangesView extends WebView {
    private static final String ABOUT_FILE_PATH = "file:///android_asset/recent_changes.html";

    public RecentChangesView(Context context) {
        super(context);
        setupView();
    }

    public RecentChangesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    private void setupView() {
        getSettings().setJavaScriptEnabled(false);
        loadUrl(ABOUT_FILE_PATH);
    }
}
