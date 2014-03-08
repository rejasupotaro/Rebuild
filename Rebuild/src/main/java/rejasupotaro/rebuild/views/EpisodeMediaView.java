package rejasupotaro.rebuild.views;

import com.squareup.otto.Subscribe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.EpisodePlayStartEvent;
import rejasupotaro.rebuild.events.ReceivePauseActionEvent;
import rejasupotaro.rebuild.events.ReceiveResumeActionEvent;
import rejasupotaro.rebuild.listener.LoadListener;
import rejasupotaro.rebuild.listener.OnPlayerSeekListener;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.notifications.PodcastPlayerNotification;
import rejasupotaro.rebuild.tools.OnContextExecutor;
import rejasupotaro.rebuild.utils.DateUtils;
import rejasupotaro.rebuild.utils.UiAnimations;

public class EpisodeMediaView extends LinearLayout {

    private LoadListener loadListener;

    private OnContextExecutor onContextExecutor = new OnContextExecutor();

    private TextView episodeTitleTextView;

    private View mediaStartButtonOnImageCover;

    private TextView mediaCurrentTimeTextView;

    private TextView mediaDurationTextView;

    private CheckBox mediaPlayAndPauseButton;

    private SeekBar seekBar;

    public EpisodeMediaView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setup(Episode episode, LoadListener loadListener) {
        BusProvider.getInstance().register(this);
        View view = inflate(getContext(), R.layout.episode_media_view, null);
        findViews(view);
        addView(view);
        setEpisode(episode);
        this.loadListener = loadListener;
    }

    private void findViews(View view) {
        episodeTitleTextView = (TextView) view.findViewById(R.id.episode_title);
        mediaStartButtonOnImageCover = view.findViewById(R.id.episode_detail_header_cover);
        mediaCurrentTimeTextView = (TextView) view.findViewById(R.id.media_current_time);
        mediaDurationTextView = (TextView) view.findViewById(R.id.media_duration);
        mediaPlayAndPauseButton = (CheckBox) view.findViewById(R.id.media_play_and_pause_button);
        seekBar = (SeekBar) view.findViewById(R.id.media_seekbar);
    }

    public void setEpisode(Episode episode) {
        String originalTitle = episode.getTitle();
        int startIndex = originalTitle.indexOf(':');
        episodeTitleTextView.setText(originalTitle.substring(startIndex + 2));

        setupMediaPlayAndPauseButton(episode);
        setupSeekBar(episode);
    }

    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
    }

    private void setupMediaPlayAndPauseButton(final Episode episode) {
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

        mediaPlayAndPauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayAndPauseButton.isChecked()) {
                    start(episode);
                } else {
                    pause(episode);
                }

            }
        });
    }

    private void start(final Episode episode) {
        final PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        if (shouldRestart(episode)) {
            podcastPlayer.start();
            seekBar.setEnabled(true);
            PodcastPlayerNotification.notify(getContext(), episode);
        } else {
            loadListener.showProgress();
            mediaPlayAndPauseButton.setEnabled(false);
            podcastPlayer.start(getContext(), episode, new PodcastPlayer.StateChangedListener() {
                @Override
                public void onStart() {
                    if (getContext() == null) {
                        pause(episode);
                    } else {
                        loadListener.showContent();
                        UiAnimations.fadeOut(mediaStartButtonOnImageCover, 300, 1000);

                        seekBar.setEnabled(true);
                        mediaPlayAndPauseButton.setEnabled(true);
                        PodcastPlayerNotification.notify(getContext(), episode);
                        BusProvider.getInstance().post(new EpisodePlayStartEvent(episode.getEpisodeId()));
                    }
                }
            });
        }
    }

    private boolean shouldRestart(Episode episode) {
        PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        return (podcastPlayer.isPlayingEpisode(episode)
                && podcastPlayer.getCurrentPosition() > 0);
    }

    private void pause(final Episode episode) {
        final PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        podcastPlayer.pause();
        seekBar.setEnabled(false);
        PodcastPlayerNotification
                .notify(getContext(), episode, PodcastPlayer.getInstance().getCurrentPosition());
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
                                    .notify(getContext(), episode, currentPosition);
                        } else {
                            updateCurrentTime(0);
                        }
                    }
                });
    }

    private void updateCurrentTime(int currentPosition) {
        mediaCurrentTimeTextView.setText(DateUtils.formatCurrentTime(currentPosition));
        seekBar.setProgress(currentPosition);
    }

    @Subscribe
    public void onReceivePauseAction(ReceivePauseActionEvent event) {
        onContextExecutor.execute(getContext(), new Runnable() {
            @Override
            public void run() {
                mediaPlayAndPauseButton.setChecked(false);
            }
        });
    }

    @Subscribe
    public void onReceivePauseAction(ReceiveResumeActionEvent event) {
        onContextExecutor.execute(getContext(), new Runnable() {
            @Override
            public void run() {
                mediaPlayAndPauseButton.setChecked(true);
            }
        });
    }
}

