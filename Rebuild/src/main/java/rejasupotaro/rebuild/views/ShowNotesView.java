package rejasupotaro.rebuild.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.models.Link;

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
        mLinkTextList.removeAllViews();
        List<Link> linkList = Link.Parser.toLinkList(episode.getShowNotes());
        List<LinkTextView> linkTextViewList = LinkTextView.toLinkTextViewList(mContext, linkList);
        for (LinkTextView linkTextView : linkTextViewList) {
            mLinkTextList.addView(linkTextView.getView());
        }
    }
}
