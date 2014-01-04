package rejasupotaro.rebuild.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Episode;

public class EpisodeListAdapter extends ArrayAdapter<Episode> {

    private static class ViewHolder {
        private TextView postedAtTextView;
        private TextView titleTextView;
        private TextView subtitleTextView;

        public ViewHolder(View root) {
            this.postedAtTextView = (TextView) root.findViewById(R.id.episode_posted_at);
            this.titleTextView = (TextView) root.findViewById(R.id.episode_title);
            this.subtitleTextView = (TextView) root.findViewById(R.id.episode_subtitle);
        }
    }

    private Context mContext;

    public EpisodeListAdapter(Context context, int resource, List<Episode> episodeList) {
        super(context, resource, episodeList);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item_episode, null);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        Episode episode = getItem(position);

        holder.postedAtTextView.setText(episode.getPostedAt());
        holder.titleTextView.setText(episode.getTitle());
        holder.subtitleTextView.setText(episode.getDescription());

        Animation slideInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in);
        convertView.startAnimation(slideInAnimation);

        return convertView;
    }
}
