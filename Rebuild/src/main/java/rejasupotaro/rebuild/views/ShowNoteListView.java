package rejasupotaro.rebuild.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.data.models.Link;
import rejasupotaro.rebuild.utils.IntentUtils;

public class ShowNoteListView extends LinearLayout {

    public ShowNoteListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public ShowNoteListView(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        setOrientation(LinearLayout.VERTICAL);
    }

    public void setEpisode(Episode episode) {
        List<Link> linkList = Link.Parser.toLinkList(episode.getShowNotes());

        View row = null;
        ShowNoteView itemView;
        for (int i = 0; i < linkList.size(); i++) {
            final Link link = linkList.get(i);

            if (i % 2 == 0) {
                row = View.inflate(getContext(), R.layout.list_item_show_note_pair, null);
                itemView = (ShowNoteView) row.findViewById(R.id.left_item);
            } else {
                itemView = (ShowNoteView) row.findViewById(R.id.right_item);
            }

            itemView.setLink(link);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtils.openBrowser(getContext(), link.getUrl());
                }
            });

            if (i % 2 == 1 || i == linkList.size() - 1) {
                addView(row);
            }
        }
    }
}
