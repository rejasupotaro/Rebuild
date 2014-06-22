package rejasupotaro.rebuild.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.IconTextView;
import android.widget.TextView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.utils.UiAnimations;
import rx.subjects.PublishSubject;

public class EpisodeListAdapter extends BindableAdapter<Episode> {

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

    private final PublishSubject<Episode> downloadButtonEvent = PublishSubject.create();

    public PublishSubject<Episode> getDownloadButtonEvent() {
        return downloadButtonEvent;
    }

    public EpisodeListAdapter(Context context, List<Episode> episodeList) {
        super(context, episodeList);
    }

    public boolean includeEpisode(String episodeId) {
        for (int i = 0; i < getCount(); i++) {
            Episode episode = getItem(i);
            if (episode != null && TextUtils.equals(episode.getEpisodeId(), episodeId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public View newView(LayoutInflater inflater, final int position, ViewGroup container) {
        View view = inflater.inflate(R.layout.list_item_episode, container, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
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
                downloadButtonEvent.onNext(item);
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
