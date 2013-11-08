package rejasupotaro.rebuild.views;

import android.content.Context;
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
        List<TextView> showNoteLinkTextList = buildShowNotes(episode.getShowNotes());
        for (TextView linkText : showNoteLinkTextList) {
            mLinkTextList.addView(linkText);
        }
    }

    private List<TextView> buildShowNotes(String source) {
        List<TextView> linkTextList = new ArrayList<TextView>();
        String[] links = substringDescription(source).split("<li>");
        for (String link : links) {
            if (!link.startsWith("<a href=")) continue;
            LinkTextView linkText = new LinkTextView(mContext, getText(link), getHref(link));
            linkTextList.add(linkText);
        }
        return linkTextList;
    }

    private String substringDescription(String source) {
        int startIndex = source.indexOf("<h3>");
        if (startIndex == -1) return "";
        return source.substring(startIndex, source.length());
    }

    private String getHref(String source) {
        return source.substring(source.indexOf("href=\"") + 6, source.indexOf(">") - 1);
    }

    private String getText(String source) {
        return source.substring(source.indexOf(">") + 1, source.indexOf("</"));
    }
}
