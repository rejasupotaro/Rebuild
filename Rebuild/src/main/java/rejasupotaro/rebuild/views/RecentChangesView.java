package rejasupotaro.rebuild.views;

import android.content.Context;
import android.webkit.WebView;

public class RecentChangesView extends WebView {

    private static final String ABOUT_FILE_PATH = "file:///android_asset/about.html";

    public RecentChangesView(Context context) {
        super(context);
        setupView();
    }

    private void setupView() {
        getSettings().setJavaScriptEnabled(false);
        loadUrl(ABOUT_FILE_PATH);
    }
}
