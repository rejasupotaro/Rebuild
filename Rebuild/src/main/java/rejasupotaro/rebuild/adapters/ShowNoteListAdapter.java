package rejasupotaro.rebuild.adapters;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Link;
import rejasupotaro.rebuild.utils.PicassoHelper;

public class ShowNoteListAdapter extends BindableAdapter<Link> {

    public ShowNoteListAdapter(Context context,
            List<Link> episodeList) {
        super(context, episodeList);
    }

    @Override
    public View newView(LayoutInflater inflater, int position, ViewGroup container) {
        return inflater.inflate(R.layout.link_text_view, null);
    }

    @Override
    public void bindView(Link item, int position, View view) {
        ImageView siteThumbnail = (ImageView) view.findViewById(R.id.site_thumbnail);
        PicassoHelper.load(getContext(), siteThumbnail, item.getUrl());

        TextView showNoteTitle = (TextView) view.findViewById(R.id.show_note_title);
        showNoteTitle.setText(item.getTitle());
    }
}
