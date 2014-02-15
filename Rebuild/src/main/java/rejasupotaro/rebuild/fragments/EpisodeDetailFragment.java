package rejasupotaro.rebuild.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.inject.Inject;
import com.squareup.otto.Subscribe;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.api.EpisodeDownloadClient;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.LoadEpisodeListCompleteEvent;
import rejasupotaro.rebuild.events.PodcastPlayButtonClickEvent;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.services.EpisodeDownloadService;
import rejasupotaro.rebuild.utils.DateUtils;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.utils.UiAnimations;
import rejasupotaro.rebuild.views.ShowNotesView;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EpisodeDetailFragment extends RoboFragment {

    public static final String TAG = EpisodeDetailFragment.class.getSimpleName();

    @Inject
    private EpisodeDownloadClient mEpisodeDownloadClient;

    @InjectView(R.id.episode_detail_header_cover)
    private View mMediaStartButtonOnImageCover;

    @InjectView(R.id.media_current_time)
    private TextView mMediaCurrentTimeTextView;

    @InjectView(R.id.media_duration)
    private TextView mMediaDurationTextView;

    @InjectView(R.id.media_seekbar)
    private SeekBar mSeekBar;

    @InjectView(R.id.episode_description)
    private TextView mEpisodeDescriptionTextView;

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
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }

    public void setup(final Episode episode) {
        mEpisode = episode;

        setTitle(episode.getTitle());

        setupMediaStartButtonOnImageCover(episode);
        setupSeekBar(episode);

        mEpisodeDescriptionTextView.setText(
                Html.fromHtml(StringUtils.buildTwitterLinkText(episode.getDescription())));
        mShowNotesView.setEpisode(episode);
    }

    private void setTitle(String title) {
        getActivity().getActionBar().setTitle(title);
    }

    private void setupSeekBar(final Episode episode) {
        mMediaDurationTextView.setText(episode.getDuration());

        PodcastPlayer.getInstance().setCurrentTimeListener(
                new PodcastPlayer.CurrentTimeListener() {
                    @Override
                    public void onTick(int currentPosition) {
                        if (PodcastPlayer.getInstance().isPlayingEpisode(episode)) {
                            updateCurrentTime(currentPosition);
                        } else {
                            updateCurrentTime(0);
                        }
                    }
                });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!PodcastPlayer.getInstance().isPlaying()) return;
                PodcastPlayer.getInstance().seekTo(seekBar.getProgress());
            }
        });

        mSeekBar.setMax(DateUtils.durationToInt(episode.getDuration()));
        mSeekBar.setEnabled(false);
    }

    private void updateCurrentTime(int currentPosition) {
        mMediaCurrentTimeTextView.setText(DateUtils.formatCurrentTime(currentPosition));
        mSeekBar.setProgress(currentPosition);
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
        start(getActivity(), episode);

        BusProvider.getInstance().post(new PodcastPlayButtonClickEvent(episode));
        UiAnimations.fadeOut(mMediaStartButtonOnImageCover, 300, 1000);
        if (!mEpisode.hasMediaDataInLocal()) {
            getActivity().startService(
                    EpisodeDownloadService.createIntent(getActivity(), episode));
        }
    }

    public void start(Context context, Episode episode) {
        final PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        podcastPlayer.start(context, episode, new PodcastPlayer.StateChangedListener() {
            @Override
            public void onStart() {
                mSeekBar.setEnabled(true);
            }
        });
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
