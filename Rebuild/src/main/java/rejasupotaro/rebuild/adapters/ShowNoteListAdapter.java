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

    private static class ViewHolderPair {
        ViewHolder left;
        ViewHolder right;

        public ViewHolderPair(View view) {
            left = new ViewHolder(view.findViewById(R.id.left_item));
            right = new ViewHolder(view.findViewById(R.id.right_item));
        }

        private static class ViewHolder {
            View root;
            TextView showNoteTitleTextView;
            ImageView siteThumbnail;

            public ViewHolder(View view) {
                root = view;
                showNoteTitleTextView = (TextView) view.findViewById(R.id.show_note_title);
                siteThumbnail = (ImageView) view.findViewById(R.id.site_thumbnail);
            }
        }
    }

    private ItemClickListener itemClickListener;

    public ShowNoteListAdapter(Context context,
            List<Link> episodeList, ItemClickListener itemClickListener) {
        super(context, episodeList);
        this.itemClickListener = itemClickListener;
    }

    @Override
    public View newView(LayoutInflater inflater, int position, ViewGroup container) {
        View view = inflater.inflate(R.layout.list_item_show_note_pair, null);
        ViewHolderPair holderPair = new ViewHolderPair(view);
        view.setTag(holderPair);
        return view;
    }

    @Override
    public void bindView(Link item, int position, View view) {
        ViewHolderPair holderPair = (ViewHolderPair) view.getTag();
        bindView(holderPair.left, position * 2);
        bindView(holderPair.right, position * 2 + 1);
    }

    private void bindView(ViewHolderPair.ViewHolder holder, int position) {
        if (position >= super.getCount()) {
            holder.root.setVisibility(View.INVISIBLE);
            return;
        } else {
            holder.root.setVisibility(View.VISIBLE);
        }
        final Link item = getItem(position);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(item);
            }
        });
        holder.root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemClickListener.onLongClick(item);
                return false;
            }
        });
        PicassoHelper.loadThumbnail(getContext(), holder.siteThumbnail, item.getUrl());
        holder.showNoteTitleTextView.setText(item.getTitle());
    }

    @Override
    public int getCount() {
        return super.getCount() / 2 + super.getCount() % 2;
    }

    public static interface ItemClickListener {
        public void onClick(Link item);
        public void onLongClick(Link item);
    }
}
