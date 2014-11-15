package rejasupotaro.rebuild.data.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.IconTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.data.models.Link;
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.utils.UiAnimations;
import rejasupotaro.rebuild.views.ShowNoteView;
import rejasupotaro.rebuild.views.SimpleGuestListView;

public class EpisodeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Episode> episodes = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
        notifyDataSetChanged();
    }

    public EpisodeListAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (ViewType.isFooter(viewType)) {
            return FooterViewHolder.create(parent);
        } else if (ViewType.isHeader(viewType)) {
            return HeaderViewHolder.create(parent, onItemClickListener);
        } else {
            return ItemViewHolder.create(parent, onItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case ViewType.FOOTER: {
                ((FooterViewHolder) viewHolder).bind();
                break;
            }
            case ViewType.HEADER: {
                Episode episode = episodes.get(position);
                ((HeaderViewHolder) viewHolder).bind(episode);
                break;
            }
            default: {
                Episode episode = episodes.get(position);
                ((ItemViewHolder) viewHolder).bind(episode);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return episodes.size() + (episodes.size() <= 0 ? 0 : 1); // items + footer (if needed)
    }

    @Override
    public int getItemViewType(int position) {
        if (episodes.size() == 0) {
            return ViewType.FOOTER;
        }
        if (position == 0) {
            return ViewType.HEADER;
        }
        if (position >= episodes.size()) {
            return ViewType.FOOTER;
        }
        return ViewType.ITEM;
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.show_note)
        ShowNoteView showNoteView;
        @InjectView(R.id.episode_title)
        TextView titleTextView;
        @InjectView(R.id.episode_subtitle)
        TextView subtitleTextView;
        @InjectView(R.id.episode_download_button)
        IconTextView episodeDownloadButton;
        @InjectView(R.id.simple_guest_list)
        SimpleGuestListView simpleGuestListView;
        @InjectView(R.id.episode_posted_at)
        IconTextView postedAtTextView;
        @InjectView(R.id.download_state_text)
        TextView downloadStateText;

        private View view;
        private OnItemClickListener onItemClickListener;

        public static HeaderViewHolder create(ViewGroup parent, OnItemClickListener onItemClickListener) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_latest_episode, parent, false);
            return new HeaderViewHolder(view, onItemClickListener);
        }

        private HeaderViewHolder(View view, OnItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.inject(this, view);
            this.view = view;
            this.onItemClickListener = onItemClickListener;
        }

        public void bind(final Episode episode) {
            final List<Link> linkList = Link.Parser.toLinkList(episode.getShowNotes());
            if (linkList == null || linkList.size() <= 1) {
                showNoteView.setVisibility(View.GONE);
            } else {
                Link link = linkList.get(1);
                showNoteView.setLink(link, false);
            }

            titleTextView.setText(episode.getTitle());
            subtitleTextView.setText(StringUtils.fromHtml(episode.getDescription()).toString());
            simpleGuestListView.setup(StringUtils.getGuestNamesFromTitle(episode.getTitle()));
            postedAtTextView.setText(String.format("{fa-calendar}  %s", episode.getPostedAtAsString()));
            if (episode.isDownloaded()) {
                episodeDownloadButton.setText("{fa-minus}");
            } else {
                episodeDownloadButton.setText("{fa-download}");
            }
            episodeDownloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UiAnimations.bounceUp(view.getContext(), v);
                    onItemClickListener.onDownloadButtonClick(episode);
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(episode);
                }
            });
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public static FooterViewHolder create(ViewGroup parent) {
            final Context context = parent.getContext();
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_footer_episode, parent, false);
            view.findViewById(R.id.miyagawa_text).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtils.openMiyagawaProfile(context);
                }
            });
            return new FooterViewHolder(view);
        }

        public FooterViewHolder(View view) {
            super(view);
        }

        public void bind() {
            // do nothing
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.episode_title)
        TextView titleTextView;
        @InjectView(R.id.episode_subtitle)
        TextView subtitleTextView;
        @InjectView(R.id.episode_download_button)
        IconTextView episodeDownloadButton;
        @InjectView(R.id.episode_posted_at)
        IconTextView postedAtTextView;
        @InjectView(R.id.download_state_text)
        TextView downloadStateText;

        private View view;
        private OnItemClickListener onItemClickListener;

        public static ItemViewHolder create(ViewGroup parent, OnItemClickListener onItemClickListener) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_episode, parent, false);
            return new ItemViewHolder(view, onItemClickListener);
        }

        public ItemViewHolder(View view, OnItemClickListener onItemClickListener) {
            super(view);
            this.view = view;
            this.onItemClickListener = onItemClickListener;
            ButterKnife.inject(this, view);
        }

        public void bind(final Episode episode) {
            titleTextView.setText(episode.getTitle());
            subtitleTextView.setText(StringUtils.fromHtml(episode.getDescription()).toString());
            postedAtTextView.setText(String.format("{fa-calendar}  %s", episode.getPostedAtAsString()));
            if (episode.isDownloaded()) {
                episodeDownloadButton.setText("{fa-minus}");
            } else {
                episodeDownloadButton.setText("{fa-download}");
            }
            episodeDownloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UiAnimations.bounceUp(view.getContext(), v);
                    onItemClickListener.onDownloadButtonClick(episode);
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(episode);
                }
            });
        }
    }

    public static interface OnItemClickListener {
        public void onClick(Episode episode);

        public void onDownloadButtonClick(Episode episode);
    }
}
