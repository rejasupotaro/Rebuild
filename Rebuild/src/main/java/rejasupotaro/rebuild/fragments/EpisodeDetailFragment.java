package rejasupotaro.rebuild.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.inject.Inject;
import com.squareup.otto.Subscribe;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.api.EpisodeDownloadClient;
import rejasupotaro.rebuild.dialogs.ShareEpisodeDialog;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.LoadEpisodeListCompleteEvent;
import rejasupotaro.rebuild.events.PodcastPlayButtonClickEvent;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.services.EpisodeDownloadService;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.utils.UiAnimations;
import rejasupotaro.rebuild.views.MediaControllerView;
import rejasupotaro.rebuild.views.ShowNotesView;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EpisodeDetailFragment extends RoboFragment {

    public static final String TAG = EpisodeDetailFragment.class.getSimpleName();

    private static final String EXTRA_EPISODE = "extra_episode";

    @Inject
    private EpisodeDownloadClient mEpisodeDownloadClient;

    @InjectView(R.id.episode_title)
    private TextView mEpisodeTitleTextView;

    @InjectView(R.id.episode_description)
    private TextView mEpisodeDescriptionTextView;

    @InjectView(R.id.media_controller_view)
    private MediaControllerView mMediaControllerView;

    @InjectView(R.id.episode_detail_header_cover)
    private View mMediaStartButtonOnImageCover;

    @InjectView(R.id.episode_show_notes)
    private ShowNotesView mShowNotesView;

    @InjectView(R.id.episode_share_button)
    private View mEpisodeShareButton;

    private Episode mEpisode;

    public static EpisodeDetailFragment newInstance(Episode episode) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_EPISODE, episode);

        EpisodeDetailFragment episodeDetailFragment = new EpisodeDetailFragment();
        episodeDetailFragment.setArguments(args);

        return episodeDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BusProvider.getInstance().register(this);
        return inflater.inflate(R.layout.fragment_episode_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEpisodeDescriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());

        Episode episode = getArguments().getParcelable(EXTRA_EPISODE);
        setup(episode);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusProvider.getInstance().unregister(this);
    }

    private void setup(final Episode episode) {
        mEpisode = episode;

        setupMediaStartButtonOnImageCover(episode);
        setTitle(episode.getTitle());
        mEpisodeTitleTextView.setText(episode.getTitle());
        mEpisodeDescriptionTextView.setText(
                Html.fromHtml(StringUtils.buildTwitterLinkText(episode.getDescription())));
        mMediaControllerView.setEpisode(episode);
        mShowNotesView.setEpisode(episode);

        mEpisodeShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = buildPostMessage(episode);
                ShareEpisodeDialog dialog = ShareEpisodeDialog.newInstance(message);
                FragmentActivity activity = getActivity();
                dialog.show(activity.getSupportFragmentManager(), TAG);
            }
        });
    }

    private String buildPostMessage(Episode episode) {
        return " / " + episode.getTitle() + " " + episode.getLink() + " #rebuildfm";
    }

    private void setTitle(String title) {
        getActivity().getActionBar().setTitle(title);
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

        if (!mEpisode.hasMediaDataInLocal()) {
            getActivity().startService(
                    EpisodeDownloadService.createIntent(getActivity(), episode));
        }
    }

    @Subscribe
    public void onLoadEpisodeListComplete(LoadEpisodeListCompleteEvent event) {
        if (mEpisode != null) return;
        List<Episode> episodeList = event.getEpisodeList();

        if (episodeList == null || episodeList.size() == 0) return;
        Episode episode = episodeList.get(0);

        if (PodcastPlayer.getInstance().isPlaying()) return;

        setup(episode);
    }
}
