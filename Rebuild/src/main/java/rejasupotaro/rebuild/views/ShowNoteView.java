package rejasupotaro.rebuild.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.models.Link;
import rejasupotaro.rebuild.utils.PicassoHelper;

public class ShowNoteView extends FrameLayout {

    public ShowNoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowNoteView(Context context, AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setEpisode(Episode episode) {
        View view = View.inflate(getContext(), R.layout.list_item_show_note, null);
        ImageView siteThumbnailImage = (ImageView) view.findViewById(R.id.site_thumbnail);
        TextView showNoteTitleTextView = (TextView) view.findViewById(R.id.show_note_title);

        List<Link> linkList = Link.Parser.toLinkList(episode.getShowNotes());
        if (linkList == null || linkList.size() <= 1) {
            Drawable bg = getResources().getDrawable(R.drawable.bg_on_the_air);
            siteThumbnailImage.setImageDrawable(bg);
        } else {
            Link link = linkList.get(1);
            PicassoHelper.loadThumbnail(getContext(), siteThumbnailImage, link.getUrl());
            showNoteTitleTextView.setText(link.getTitle());
        }

        addView(view);
    }

    public void setLink(Link link) {
        View view = View.inflate(getContext(), R.layout.list_item_show_note, null);

        ImageView siteThumbnailImage = (ImageView) view.findViewById(R.id.site_thumbnail);
        PicassoHelper.loadThumbnail(getContext(), siteThumbnailImage, link.getUrl());

        TextView showNoteTitleTextView = (TextView) view.findViewById(R.id.show_note_title);
        showNoteTitleTextView.setText(link.getTitle());

        addView(view);
    }
}
