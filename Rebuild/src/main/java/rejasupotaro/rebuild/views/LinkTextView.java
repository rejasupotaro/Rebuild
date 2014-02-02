package rejasupotaro.rebuild.views;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Link;
import rejasupotaro.rebuild.utils.IntentUtils;

public class LinkTextView {

    private TextView mView;

    public TextView getView() {
        return mView;
    }

    public LinkTextView(final Context context, final Link link) {
        mView = (TextView) View.inflate(context, R.layout.link_text_view, null);
        mView.setText("▶ " + link.getTitle());
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openBrowser(context, link.getUrl());
            }
        });
    }

    public static List<LinkTextView> toLinkTextViewList(Context context, List<Link> linkList) {
        List<LinkTextView> linkTextViewList = new ArrayList<LinkTextView>();
        for (Link link : linkList) {
            linkTextViewList.add(new LinkTextView(context, link));
        }
        return linkTextViewList;
    }
}
