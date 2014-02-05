package rejasupotaro.rebuild.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Episode;

public class EpisodeListAdapter extends BindableAdapter<Episode> {

    public EpisodeListAdapter(Context context, List<Episode> episodeList) {
        super(context, episodeList);
    }

    @Override
    public View newView(LayoutInflater inflater, int position, ViewGroup container) {
        return inflater.inflate(R.layout.list_item_episode, container, false);
    }

    @Override
    public void bindView(Episode item, int position, View view) {
        TextView postedAtTextView = (TextView) view.findViewById(R.id.episode_posted_at);
        postedAtTextView.setText(item.getPostedAt());

        TextView titleTextView = (TextView) view.findViewById(R.id.episode_title);
        titleTextView.setText(item.getTitle());

        TextView subtitleTextView = (TextView) view.findViewById(R.id.episode_subtitle);
        subtitleTextView.setText(item.getDescription());
    }
}
