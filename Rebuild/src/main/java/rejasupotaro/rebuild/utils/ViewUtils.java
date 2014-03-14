package rejasupotaro.rebuild.utils;

import android.text.util.Linkify;
import android.util.Patterns;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ViewUtils {

    private static final Linkify.TransformFilter defaultFilter = new Linkify.TransformFilter() {
        public final String transformUrl(final Matcher match, String url) {
            return match.group();
        }
    };

    public static void setTweetText(TextView textView, String text) {
        textView.setText(StringUtils.fromHtml(text));

        Pattern rebuildPattern = Pattern.compile("rebuild.fm([/A-Za-z0-9_-]*)");
        String httpScheme = "http://";
        Linkify.addLinks(textView, rebuildPattern, httpScheme, null, defaultFilter);

        Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9_-]+)");
        String mentionScheme = "http://www.twitter.com/";
        Linkify.addLinks(textView, mentionPattern, mentionScheme, null, defaultFilter);

        Pattern hashtagPattern = Pattern.compile("#([A-Za-z0-9_-]+)");
        String hashtagScheme = "http://www.twitter.com/search/";
        Linkify.addLinks(textView, hashtagPattern, hashtagScheme, null, new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                String hashtag = match.group();
                return hashtag.substring(1, hashtag.length());
            }
        });

        Linkify.addLinks(textView, Patterns.WEB_URL, null, null, defaultFilter);
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
