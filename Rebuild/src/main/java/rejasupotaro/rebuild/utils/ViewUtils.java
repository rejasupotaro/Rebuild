package rejasupotaro.rebuild.utils;

import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public final class ViewUtils {

    public static void setTweetText(TextView textView, String text) {
        String tweetText = StringUtils.buildTwitterLinkText(text);
        if (TextUtils.isEmpty(tweetText)) {
            textView.setText(text);
        } else {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(Html.fromHtml(tweetText));
            textView.setFocusable(false);
        }
    }

    public static void addHeaderView(ListView listView, View view) {
        if (listView == null || listView.getHeaderViewsCount() != 0 || view == null) {
            return;
        }
        listView.addHeaderView(view, null, false);
    }

    public static void addFooterView(ListView listView, View view) {
        if (listView == null || listView.getFooterViewsCount() != 0 || view == null) {
            return;
        }
        listView.addFooterView(view, null, false);
    }

    public static void disable(View view) {
        view.setEnabled(false);
        view.setClickable(false);
    }

    private ViewUtils() {}
}
