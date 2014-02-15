package rejasupotaro.rebuild.adapters;

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
        return inflater.inflate(R.layout.list_item_show_note_pair, null);
    }

    @Override
    public void bindView(Link item, int position, View view) {
        bindView(view.findViewById(R.id.left_item), position * 2);
        bindView(view.findViewById(R.id.right_item), position * 2 + 1);
    }

    private void bindView(View view, int position) {
        if (position >= super.getCount()) {
            view.setVisibility(View.INVISIBLE);
            return;
        } else {
            view.setVisibility(View.VISIBLE);
        }
        Link item = getItem(position);

        ImageView siteThumbnail = (ImageView) view.findViewById(R.id.site_thumbnail);
        PicassoHelper.load(getContext(), siteThumbnail, item.getUrl());

        TextView showNoteTitle = (TextView) view.findViewById(R.id.show_note_title);
        showNoteTitle.setText(item.getTitle());
    }

    @Override
    public int getCount() {
        return super.getCount() / 2 + super.getCount() % 2;
    }
}
