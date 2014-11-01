package rejasupotaro.rebuild.views;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.IconTextView;
import android.widget.TextView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.data.models.Link;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.utils.UiAnimations;
import rx.subjects.PublishSubject;

public class LatestEpisodeListItemView extends FrameLayout {
    private ShowNoteView showNoteView;
    private TextView titleTextView;
    private TextView subtitleTextView;
    private IconTextView episodeDownloadButton;
    private SimpleGuestListView simpleGuestListView;
    private IconTextView postedAtTextView;
    private TextView downloadStateText;

    public LatestEpisodeListItemView(Context context, Episode episode,
                                     PublishSubject<Episode> downloadButtonEvent) {
        super(context);
        createView();
        setup(episode, downloadButtonEvent);
    }

    private void createView() {
        View root = View.inflate(getContext(), R.layout.list_item_latest_episode, null);
        showNoteView = (ShowNoteView) root.findViewById(R.id.show_note);
        titleTextView = (TextView) root.findViewById(R.id.episode_title);
        subtitleTextView = (TextView) root.findViewById(R.id.episode_subtitle);
        episodeDownloadButton = (IconTextView) root.findViewById(R.id.episode_download_button);
        simpleGuestListView = (SimpleGuestListView) root.findViewById(R.id.simple_guest_list);
        postedAtTextView = (IconTextView) root.findViewById(R.id.episode_posted_at);
        downloadStateText = (TextView) root.findViewById(R.id.download_state_text);
        addView(root);
    }

    private void setup(final Episode episode, final PublishSubject<Episode> downloadButtonEvent) {
        List<Link> linkList = Link.Parser.toLinkList(episode.getShowNotes());
        if (linkList == null || linkList.size() <= 1) {
            showNoteView.setVisibility(View.GONE);
        } else {
            Link link = linkList.get(1);
            showNoteView.setLink(link, false);
        }


        titleTextView.setText(episode.getTitle());
        subtitleTextView.setText(StringUtils.fromHtml(episode.getDescription()).toString());
        simpleGuestListView.setup(StringUtils.getGuestNames(episode.getDescription()));
        postedAtTextView.setText(String.format("{fa-calendar}  %s", episode.getPostedAtAsString()));
        if (episode.isDownloaded()) {
            episodeDownloadButton.setText("{fa-minus}");
        } else {
            episodeDownloadButton.setText("{fa-download}");
        }
        episodeDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiAnimations.bounceUp(getContext(), v);
                downloadButtonEvent.onNext(episode);
            }
        });
    }
}
