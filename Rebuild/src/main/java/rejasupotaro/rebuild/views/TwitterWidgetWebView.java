package rejasupotaro.rebuild.views;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import rejasupotaro.rebuild.utils.FileUtils;

public class TwitterWidgetWebView extends WebView {

    public static interface LoadListener {
        public void onStart();
        public void onError(int errorCode);
        public void onFinish();
    }

    private static final String TAG = TwitterWidgetWebView.class.getSimpleName();

    private static final String CONTENT_FILE_NAME = "twitter_widget.html";

    private Context mContext;

    private LoadListener mLoadListener;

    public TwitterWidgetWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Context context, LoadListener loadListener) {
        mContext = context;
        mLoadListener = loadListener;
        setupWebView();
        load();
    }

    private void setupWebView() {
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);
        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d("Rebuild", cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                return true;
            }
        });

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mLoadListener.onStart();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return overrideUrlLoadingHandler(url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                                        String failingUrl) {
                mLoadListener.onError(errorCode);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            public void onPageFinished(WebView view, String url) {
                mLoadListener.onFinish();
                super.onPageFinished(view, url);
            }
        });
    }

    private boolean overrideUrlLoadingHandler(String url) {
        return true;
    }

    private void load() {
        loadDataWithBaseURL("https://twitter.com", loadContent(), "text/html", "UTF-8", null);
    }

    private String loadContent() {
        String content = "";
        InputStream in = null;
        BufferedReader reader = null;

        try {
            AssetManager as = mContext.getResources().getAssets();
            in = as.open(CONTENT_FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(in, HTTP.UTF_8));

            StringBuffer stringBuffer = new StringBuffer();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }

            content = stringBuffer.toString();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            FileUtils.close(reader);
            FileUtils.close(in);

            return content;
        }
    }
}
