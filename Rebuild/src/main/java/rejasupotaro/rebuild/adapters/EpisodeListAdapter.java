package rejasupotaro.rebuild.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Episode;

public class EpisodeListAdapter extends ArrayAdapter<Episode> {

    private static class ViewHolder {
        TextView titleTextView;
        TextView postedAtTextView;
    }

    private LayoutInflater mLayoutInflater;

    public EpisodeListAdapter(Context context, int resource, List<Episode> episodeList) {
        super(context, resource, episodeList);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_episode, null);

            holder = new ViewHolder();
            holder.titleTextView = (TextView) convertView.findViewById(R.id.episode_title);
            holder.postedAtTextView = (TextView) convertView.findViewById(R.id.episode_posted_at);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Episode episode = getItem(position);

        holder.titleTextView.setText(episode.getTitle());
        holder.postedAtTextView.setText(episode.getPostedAt());

        return convertView;
    }
}
