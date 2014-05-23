package rejasupotaro.rebuild.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.models.Link;
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.utils.PicassoHelper;

public class ShowNotesView extends LinearLayout {

    public ShowNotesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public ShowNotesView(Context context, AttributeSet attrs,
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
        View itemView = null;
        for (int i = 0; i < linkList.size(); i++) {
            final Link link = linkList.get(i);

            if (i % 2 == 0) {
                row = View.inflate(getContext(), R.layout.list_item_show_note_pair, null);
                itemView = row.findViewById(R.id.left_item);
            } else {
                itemView = row.findViewById(R.id.right_item);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtils.openBrowser(getContext(), link.getUrl());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    IntentUtils.sendPostIntent(getContext(), link.getUrl());
                    return false;
                }
            });
            ImageView siteThumbnailImage = (ImageView) itemView.findViewById(R.id.site_thumbnail);
            PicassoHelper.loadThumbnail(getContext(), siteThumbnailImage, link.getUrl());
            TextView showNoteTitleTextView = (TextView) itemView.findViewById(R.id.show_note_title);
            showNoteTitleTextView.setText(link.getTitle());

            if (i % 2 == 1 || i == linkList.size() - 1) {
                addView(row);
            }
        }
    }
}
