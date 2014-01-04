package rejasupotaro.rebuild.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Episode;

public class ShowNotesView extends LinearLayout {

    private Context mContext;

    private LinearLayout mLinkTextList;

    public ShowNotesView(Context context) {
        super(context);
        setup(context);
    }

    public ShowNotesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    private void setup(Context context) {
        mContext = context;
        LayoutParams params =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        View view = inflate(context, R.layout.view_show_notes, null);
        mLinkTextList = (LinearLayout) view.findViewById(R.id.link_text_list);
        addView(view, params);
    }

    public void setEpisode(Episode episode) {
        List<TextView> showNoteLinkTextList = LinkParser.buildShowNotes(mContext, episode.getShowNotes());
        for (TextView linkText : showNoteLinkTextList) {
            mLinkTextList.addView(linkText);
        }
    }

    public static class LinkParser {

        public static List<TextView> buildShowNotes(Context context, String source) {
            List<TextView> linkTextList = new ArrayList<TextView>();
            String[] links = substringDescription(source).split("<li>");
            for (String link : links) {
                if (!link.startsWith("<a href=")) continue;
                LinkTextView linkText = new LinkTextView(context, getText(link), getHref(link));
                linkTextList.add(linkText.getView());
            }
            return linkTextList;
        }

        public static String substringDescription(String source) {
            if (TextUtils.isEmpty(source)) return "";
            int startIndex = source.indexOf("<h3>");
            if (startIndex < 0) return "";
            return source.substring(startIndex, source.length());
        }

        public static String getHref(String source) {
            if (TextUtils.isEmpty(source)) return "";
            int startIndex = source.indexOf("href=\"");
            int endIndex = source.indexOf(">");
            if (startIndex < 0 || endIndex < 0) return "";
            return source.substring(startIndex + 6, endIndex - 1);
        }

        public static String getText(String source) {
            if (TextUtils.isEmpty(source)) return "";
            int startIndex = source.indexOf(">");
            int endIndex = source.indexOf("</");
            if (startIndex < 0 || endIndex < 0) return "";
            return source.substring(startIndex + 1, endIndex);
        }
    }
}
