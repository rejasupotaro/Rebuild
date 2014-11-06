package rejasupotaro.rebuild.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.dialogs.EpisodePlayDialog;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.EpisodePlayStartEvent;
import rejasupotaro.rebuild.events.LoadEpisodeListCompleteEvent;
import rejasupotaro.rebuild.events.ReceivePauseActionEvent;
import rejasupotaro.rebuild.events.ReceiveResumeActionEvent;
import rejasupotaro.rebuild.listener.LoadListener;
import rejasupotaro.rebuild.listener.OnPlayerSeekListener;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.notifications.PodcastPlayerNotification;
import rejasupotaro.rebuild.tools.OnContextExecutor;
import rejasupotaro.rebuild.utils.DateUtils;
import rejasupotaro.rebuild.utils.UiAnimations;
import rejasupotaro.rebuild.views.StateFrameLayout;
import uk.me.lewisdeane.ldialogs.CustomDialog;

public class EpisodeMediaFragment extends Fragment {
    @InjectView(R.id.episode_title)
    TextView episodeTitleTextView;
    @InjectView(R.id.episode_detail_header_cover)
    View mediaStartButtonOnImageCover;
    @InjectView(R.id.media_current_time)
    TextView mediaCurrentTimeTextView;
    @InjectView(R.id.media_duration)
    TextView mediaDurationTextView;
    @InjectView(R.id.media_play_and_pause_button)
    CheckBox mediaPlayAndPauseButton;
    @InjectView(R.id.media_seekbar)
    SeekBar seekBar;
    @InjectView(R.id.state_frame_layout)
    StateFrameLayout stateFrameLayout;

    private Episode episode;
    private LoadListener loadListener;
    private OnContextExecutor onContextExecutor = new OnContextExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BusProvider.getInstance().register(this);
        View view = inflater.inflate(R.layout.fragment_episode_media, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    public void setup(final Episode episode) {
        if (episode == null) {
            getActivity().finish();
            return;
        }

        this.episode = episode;
        episodeTitleTextView.setText(episode.getTitle());

        loadListener = new LoadListener() {
            @Override
            public void showProgress() {
                stateFrameLayout.showProgress();
            }

            @Override
            public void showError() {
            }

            @Override
            public void showContent() {
                stateFrameLayout.showContent();
            }
        };

        setupMediaPlayAndPauseButton(episode);
        setupSeekBar(episode);
    }

    private void setupMediaPlayAndPauseButton(final Episode episode) {
        if (episode.getEnclosure() == null) {
            return;
        }

        PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        if (podcastPlayer.isPlayingEpisode(episode)
                && (podcastPlayer.isPlaying() || podcastPlayer.isPaused())) {
            mediaStartButtonOnImageCover.setVisibility(View.GONE);

            if (podcastPlayer.isPlaying()) {
                mediaPlayAndPauseButton.setChecked(true);
            } else {
                mediaPlayAndPauseButton.setChecked(false);
            }
        } else {
            if (podcastPlayer.isPlaying()) {
                mediaStartButtonOnImageCover.setVisibility(View.VISIBLE);
                mediaStartButtonOnImageCover.setAlpha(1);
            }
        }

        mediaPlayAndPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayAndPauseButton.setChecked(!mediaPlayAndPauseButton.isChecked());

                if (!mediaPlayAndPauseButton.isChecked()) {
                    if (shouldRestart(episode)) {
                        restart(episode);
                        mediaPlayAndPauseButton.setChecked(!mediaPlayAndPauseButton.isChecked());
                    } else {
                        showEpisodePlayDialog(episode);
                    }
                } else {
                    pause(episode);
                }
            }
        });
    }

    private void showEpisodePlayDialog(Episode episode) {
        CustomDialog dialog = EpisodePlayDialog.newInstance(getActivity(), episode, new EpisodePlayDialog.OnSelectListener() {
            @Override
            public void playNow(Episode episode) {
                start(episode);
                mediaPlayAndPauseButton.setChecked(!mediaPlayAndPauseButton.isChecked());
            }

            @Override
            public void startStreaming(Episode episode) {
                start(episode);
                mediaPlayAndPauseButton.setChecked(!mediaPlayAndPauseButton.isChecked());
            }
        });
        dialog.show();
    }

    private void start(final Episode episode) {
        if (shouldRestart(episode)) {
            restart(episode);
            return;
        }

        loadListener.showProgress();
        mediaPlayAndPauseButton.setEnabled(false);

        final PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        podcastPlayer.start(getActivity(), episode, new PodcastPlayer.StateChangedListener() {
            @Override
            public void onStart() {
                if (getActivity() == null) {
                    pause(episode);
                } else {
                    loadListener.showContent();
                    UiAnimations.fadeOut(mediaStartButtonOnImageCover, 300, 1000);

                    seekBar.setEnabled(true);
                    mediaPlayAndPauseButton.setEnabled(true);
                    PodcastPlayerNotification.notify(getActivity(), episode);
                    BusProvider.getInstance()
                            .post(new EpisodePlayStartEvent(episode.getEpisodeId()));
                }
            }
        });
    }

    private boolean shouldRestart(Episode episode) {
        PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        return (podcastPlayer.isPlayingEpisode(episode)
                && (podcastPlayer.isPlaying() || podcastPlayer.isPaused()));
    }

    private void restart(Episode episode) {
        PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        podcastPlayer.restart();
        seekBar.setEnabled(true);
        PodcastPlayerNotification.notify(getActivity(), episode);
    }

    private void pause(final Episode episode) {
        mediaPlayAndPauseButton.setChecked(!mediaPlayAndPauseButton.isChecked());

        final PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        podcastPlayer.pause();
        seekBar.setEnabled(false);
        PodcastPlayerNotification
                .notify(getActivity(), episode, PodcastPlayer.getInstance().getCurrentPosition());
    }

    private void setupSeekBar(final Episode episode) {
        mediaDurationTextView.setText(episode.getDuration());

        if (PodcastPlayer.getInstance().isPlaying()) {
            updateCurrentTime(PodcastPlayer.getInstance().getCurrentPosition());
        } else {
            updateCurrentTime(0);
        }

        seekBar.setOnSeekBarChangeListener(new OnPlayerSeekListener());
        seekBar.setMax(DateUtils.durationToInt(episode.getDuration()));
        if (PodcastPlayer.getInstance().isPlayingEpisode(episode)) {
            seekBar.setEnabled(true);
        } else {
            seekBar.setEnabled(false);
        }

        PodcastPlayer.getInstance().setCurrentTimeListener(
                new PodcastPlayer.CurrentTimeListener() {
                    @Override
                    public void onTick(int currentPosition) {
                        if (PodcastPlayer.getInstance().isPlayingEpisode(episode)) {
                            updateCurrentTime(currentPosition);
                            PodcastPlayerNotification
                                    .notify(getActivity(), episode, currentPosition);
                        } else {
                            updateCurrentTime(0);
                        }
                    }
                }
        );
    }

    private void updateCurrentTime(int currentPosition) {
        if (mediaCurrentTimeTextView == null || seekBar == null) {
            return;
        }
        mediaCurrentTimeTextView.setText(DateUtils.formatCurrentTime(currentPosition));
        seekBar.setProgress(currentPosition);
    }

    @Subscribe
    public void onReceivePauseAction(ReceivePauseActionEvent event) {
        onContextExecutor.execute(getActivity(), new Runnable() {
            @Override
            public void run() {
                mediaPlayAndPauseButton.setChecked(false);
            }
        });
    }

    @Subscribe
    public void onReceivePauseAction(ReceiveResumeActionEvent event) {
        onContextExecutor.execute(getActivity(), new Runnable() {
            @Override
            public void run() {
                mediaPlayAndPauseButton.setChecked(true);
            }
        });
    }

    @Subscribe
    public void onLoadEpisodeListComplete(final LoadEpisodeListCompleteEvent event) {
        if (episode != null) {
            return;
        }

        onContextExecutor.execute(getActivity(), new Runnable() {
            @Override
            public void run() {
                List<Episode> episodeList = event.getEpisodeList();

                if (episodeList == null || episodeList.size() == 0) {
                    return;
                }
                Episode episode = episodeList.get(0);

                if (PodcastPlayer.getInstance().isPlaying()) {
                    return;
                }

                setup(episode);
            }
        });
    }
}
