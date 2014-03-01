package rejasupotaro.rebuild.utils;

import android.text.util.Linkify;
import android.util.Patterns;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ViewUtils {

    public static void setTweetText(TextView textView, String text) {
        textView.setText(text);

        Linkify.TransformFilter filter = new Linkify.TransformFilter() {
            public final String transformUrl(final Matcher match, String url) {
                return match.group();
            }
        };

        Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9_-]+)");
        String mentionScheme = "http://www.twitter.com/";
        Linkify.addLinks(textView, mentionPattern, mentionScheme, null, filter);

        Pattern hashtagPattern = Pattern.compile("#([A-Za-z0-9_-]+)");
        String hashtagScheme = "http://www.twitter.com/search/";
        Linkify.addLinks(textView, hashtagPattern, hashtagScheme, null, filter);

        Pattern urlPattern = Patterns.WEB_URL;
        Linkify.addLinks(textView, urlPattern, null, null, filter);
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

    private ViewUtils() {
    }
}
