package rejasupotaro.rebuild.fragments;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.LoadEpisodeListCompleteEvent;
import rejasupotaro.rebuild.events.PodcastPlayButtonClickEvent;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.utils.UiAnimations;
import rejasupotaro.rebuild.views.MediaControllerView;
import rejasupotaro.rebuild.views.ShowNotesView;
import rejasupotaro.rebuild.views.SlidingUpPanelDragView;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EpisodeDetailFragment extends RoboFragment {

    @InjectView(R.id.sliding_up_panel_drag_view)
    private SlidingUpPanelDragView mSlidingUpPanelDragView;

    @InjectView(R.id.episode_title)
    private TextView mEpisodeTitleTextView;

    @InjectView(R.id.episode_description)
    private TextView mEpisodeDescriptionTextView;

    @InjectView(R.id.media_controller_view)
    private MediaControllerView mMediaControllerView;

    @InjectView(R.id.media_start_button_on_image_cover)
    private View mMediaStartButtonOnImageCover;

    @InjectView(R.id.episode_show_notes)
    private ShowNotesView mShowNotesView;

    private Episode mEpisode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BusProvider.getInstance().register(this);
        return inflater.inflate(R.layout.fragment_episode_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEpisodeDescriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusProvider.getInstance().unregister(this);
    }

    public void setup(final Episode episode) {
        mEpisode = episode;

        setupMediaStartButtonOnImageCover(episode);
        mEpisodeTitleTextView.setText(episode.getTitle());
        mEpisodeDescriptionTextView.setText(
                Html.fromHtml(StringUtils.buildTwitterLinkText(episode.getDescription())));
        mSlidingUpPanelDragView.setEpisode(episode);
        mMediaControllerView.setEpisode(episode);
        mShowNotesView.setEpisode(episode);
    }

    private void setupMediaStartButtonOnImageCover(final Episode episode) {
        if (PodcastPlayer.getInstance().isPlayingEpisode(episode)) {
            mMediaStartButtonOnImageCover.setVisibility(View.GONE);
        } else {
            mMediaStartButtonOnImageCover.setVisibility(View.VISIBLE);
            mMediaStartButtonOnImageCover.setAlpha(1);
            mMediaStartButtonOnImageCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPodcastPlayButtonClick(episode);
                }
            });
        }
    }

    private void onPodcastPlayButtonClick(Episode episode) {
        BusProvider.getInstance().post(new PodcastPlayButtonClickEvent(episode));
        mMediaControllerView.setEpisode(episode);
        mMediaControllerView.start(getActivity(), episode);
        UiAnimations.fadeOut(mMediaStartButtonOnImageCover, 300, 1000);
    }

    @Subscribe
    public void onLoadEpisodeListComplete(LoadEpisodeListCompleteEvent event) {
        if (mEpisode != null) return;
        List<Episode> episodeList = event.getEpisodeList();

        if (episodeList == null || episodeList.size() == 0) return;
        Episode episode = episodeList.get(0);

        if (PodcastPlayer.getInstance().isPlaying()) return;
        mSlidingUpPanelDragView.setEpisode(episode);

        setup(episode);
    }
}
