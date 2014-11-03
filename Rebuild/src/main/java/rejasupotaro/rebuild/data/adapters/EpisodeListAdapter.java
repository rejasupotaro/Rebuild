package rejasupotaro.rebuild.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.IconTextView;
import android.widget.TextView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.listener.OnDownloadButtonClickListener;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.utils.UiAnimations;
import rejasupotaro.rebuild.views.LatestEpisodeListItemView;

public class EpisodeListAdapter extends ArrayAdapter<Episode> {
    private static class ViewHolder {
        TextView titleTextView;
        TextView subtitleTextView;
        IconTextView episodeDownloadButton;
        IconTextView postedAtTextView;
        TextView downloadStateText;

        public ViewHolder(View view) {
            titleTextView = (TextView) view.findViewById(R.id.episode_title);
            subtitleTextView = (TextView) view.findViewById(R.id.episode_subtitle);
            episodeDownloadButton = (IconTextView) view.findViewById(R.id.episode_download_button);
            postedAtTextView = (IconTextView) view.findViewById(R.id.episode_posted_at);
            downloadStateText = (TextView) view.findViewById(R.id.download_state_text);
        }
    }

    private LayoutInflater inflater;
    private OnDownloadButtonClickListener listener;

    public void setDownloadButtonClickListener(OnDownloadButtonClickListener listener) {
        this.listener = listener;
    }

    public EpisodeListAdapter(Context context, List<Episode> episodeList) {
        super(context, -1, episodeList);
        setup();
    }

    private void setup() {
        inflater = LayoutInflater.from(getContext());
    }

    @Override
    public final View getView(int position, View view, ViewGroup container) {
        if (position == 0) {
            return createLatestEpisodeView(position);
        }

        view = newView(inflater, container);
        bindView(getItem(position), position, view);
        return view;
    }

    private View createLatestEpisodeView(int position) {
        return new LatestEpisodeListItemView(getContext(), getItem(position), listener);
    }

    public View newView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.list_item_episode, container, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    public void bindView(final Episode item, final int position, View view) {
        final ViewHolder holder = (ViewHolder) view.getTag();

        holder.titleTextView.setText(item.getTitle());
        holder.subtitleTextView.setText(StringUtils.fromHtml(item.getDescription()).toString());
        holder.postedAtTextView.setText(String.format("{fa-calendar}  %s", item.getPostedAtAsString()));
        if (item.isDownloaded()) {
            holder.episodeDownloadButton.setText("{fa-minus}");
        } else {
            holder.episodeDownloadButton.setText("{fa-download}");
        }
        holder.episodeDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiAnimations.bounceUp(getContext(), v);
                listener.onClick(item);
            }
        });
//        disable temporary...
//        if (EpisodeDownloadService.isDownloading(item)) {
//            holder.downloadStateText.setVisibility(View.VISIBLE);
//        } else {
//            holder.downloadStateText.setVisibility(View.GONE);
//        }
    }
}
