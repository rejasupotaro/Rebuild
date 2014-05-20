package rejasupotaro.rebuild.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.IconTextView;
import android.widget.TextView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.StringUtils;

public class EpisodeListAdapter extends BindableAdapter<Episode> {

    private static class ViewHolder {
        TextView postedAtTextView;
        TextView titleTextView;
        TextView subtitleTextView;
        IconTextView downloadStateTextView;

        public ViewHolder(View view) {
            postedAtTextView = (TextView) view.findViewById(R.id.episode_posted_at);
            titleTextView = (TextView) view.findViewById(R.id.episode_title);
            subtitleTextView = (TextView) view.findViewById(R.id.episode_subtitle);
            downloadStateTextView = (IconTextView) view.findViewById(R.id.episode_download_state);
        }
    }

    public EpisodeListAdapter(Context context, List<Episode> episodeList) {
        super(context, episodeList);
    }

    @Override
    public View newView(LayoutInflater inflater, int position, ViewGroup container) {
        View view = inflater.inflate(R.layout.list_item_episode, container, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(Episode item, int position, View view) {
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.postedAtTextView.setText(item.getPostedAt());
        holder.titleTextView.setText(item.getTitle());
        holder.subtitleTextView.setText(StringUtils.fromHtml(item.getDescription()).toString());

        if (item.isDownloaded()) {
            holder.downloadStateTextView.setVisibility(View.VISIBLE);
        } else {
            holder.downloadStateTextView.setVisibility(View.GONE);
        }
    }
}
