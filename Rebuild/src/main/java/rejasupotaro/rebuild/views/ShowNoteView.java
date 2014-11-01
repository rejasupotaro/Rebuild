package rejasupotaro.rebuild.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.data.models.Link;
import rejasupotaro.rebuild.utils.PicassoHelper;

public class ShowNoteView extends FrameLayout {

    public ShowNoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowNoteView(Context context, AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setLink(Link link) {
        setLink(link, true);
    }

    public void setLink(Link link, boolean isShowText) {
        View view = View.inflate(getContext(), R.layout.list_item_show_note, null);

        ImageView siteThumbnailImage = (ImageView) view.findViewById(R.id.site_thumbnail);
        PicassoHelper.loadThumbnail(getContext(), siteThumbnailImage, link.getUrl());

        TextView showNoteTitleTextView = (TextView) view.findViewById(R.id.show_note_title);
        if (isShowText) {
            showNoteTitleTextView.setText(link.getTitle());
            showNoteTitleTextView.setVisibility(View.VISIBLE);
        } else {
            showNoteTitleTextView.setVisibility(View.GONE);
        }

        addView(view);
    }
}
