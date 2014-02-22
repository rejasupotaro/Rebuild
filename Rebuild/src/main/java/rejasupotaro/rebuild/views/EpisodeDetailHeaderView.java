package rejasupotaro.rebuild.views;

import com.squareup.otto.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.DownloadEpisodeCompleteEvent;
import rejasupotaro.rebuild.events.ReceivePauseActionEvent;
import rejasupotaro.rebuild.listener.LoadListener;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.notifications.PodcastPlayerNotification;
import rejasupotaro.rebuild.services.EpisodeDownloadService;
import rejasupotaro.rebuild.tools.OnContextExecutor;
import rejasupotaro.rebuild.utils.DateUtils;
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.utils.ToastUtils;
import rejasupotaro.rebuild.utils.UiAnimations;

public class EpisodeDetailHeaderView extends LinearLayout {

    private LoadListener mLoadListener;

    private OnContextExecutor mOnContextExecutor = new OnContextExecutor();

    private TextView mEpisodeTitleTextView;

    private View mMediaStartButtonOnImageCover;

    private TextView mMediaCurrentTimeTextView;

    private TextView mMediaDurationTextView;

    private CheckBox mMediaStartAndPauseButton;

    private SeekBar mSeekBar;

    private TextView mEpisodeDescriptionTextView;

    private FontAwesomeTextView mEpisodeShareButton;

    private FontAwesomeTextView mEpisodeDownloadButton;

    public EpisodeDetailHeaderView(Context context, LoadListener loadListener) {
        super(context);
        mLoadListener = loadListener;
        setupView(context);
    }

    private void setupView(Context context) {
        BusProvider.getInstance().register(this);

        LayoutParams params =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        View view = inflate(context, R.layout.header_episode_detail, null);

        mEpisodeTitleTextView = (TextView) view.findViewById(R.id.episode_title);
        mMediaStartButtonOnImageCover = view.findViewById(R.id.episode_detail_header_cover);
        mMediaCurrentTimeTextView = (TextView) view.findViewById(R.id.media_current_time);
        mMediaDurationTextView = (TextView) view.findViewById(R.id.media_duration);
        mMediaStartAndPauseButton = (CheckBox) view.findViewById(R.id.media_start_and_pause_button);
        mSeekBar = (SeekBar) view.findViewById(R.id.media_seekbar);
        mEpisodeDescriptionTextView = (TextView) view.findViewById(R.id.episode_description);
        mEpisodeShareButton = (FontAwesomeTextView) view.findViewById(R.id.episode_share_button);
        mEpisodeDownloadButton = (FontAwesomeTextView) view.findViewById(R.id.episode_download_button);

        addView(view, params);
    }

    public void onActivityCreated() {
        mEpisodeDescriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
    }

    public void setEpisode(Episode episode) {
        String originalTitle = episode.getTitle();
        int startIndex = originalTitle.indexOf(':');
        mEpisodeTitleTextView.setText(originalTitle.substring(startIndex + 2));
        mEpisodeDescriptionTextView.setText(
                Html.fromHtml(StringUtils.buildTwitterLinkText(episode.getDescription())));

        setupMediaPlayAndPauseButton(episode);
        setupShareButton(episode);
        setupDownloadButton(episode);
        setupSeekBar(episode);
    }

    private void setupMediaPlayAndPauseButton(final Episode episode) {
        if (PodcastPlayer.getInstance().isPlayingEpisode(episode)) {
            mMediaStartAndPauseButton.setChecked(true);
            mMediaStartButtonOnImageCover.setVisibility(View.GONE);
        } else {
            mMediaStartAndPauseButton.setChecked(false);
            mMediaStartButtonOnImageCover.setVisibility(View.VISIBLE);
            mMediaStartButtonOnImageCover.setAlpha(1);
        }

        mMediaStartAndPauseButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            start(episode);
                        } else {
                            pause();
                        }
                    }
                });
    }

    private void start(final Episode episode) {
        final PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        if (shouldRestart(episode)) {
            podcastPlayer.start();
            mSeekBar.setEnabled(true);
            PodcastPlayerNotification.notity(getContext(), episode);
        } else {
            mLoadListener.showProgress();
            mMediaStartAndPauseButton.setEnabled(false);
            podcastPlayer.start(getContext(), episode, new PodcastPlayer.StateChangedListener() {
                @Override
                public void onStart() {
                    if (getContext() == null) {
                        pause();
                    } else {
                        mLoadListener.showContent();
                        mSeekBar.setEnabled(true);
                        mMediaStartAndPauseButton.setEnabled(true);
                        PodcastPlayerNotification.notity(getContext(), episode);
                    }
                }
            });

            UiAnimations.fadeOut(mMediaStartButtonOnImageCover, 300, 1000);
        }
    }

    private boolean shouldRestart(Episode episode) {
        PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        return (podcastPlayer.isPlayingEpisode(episode)
                && podcastPlayer.getCurrentPosition() > 0);
    }

    private void pause() {
        final PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        podcastPlayer.pause();
        mSeekBar.setEnabled(false);

        PodcastPlayerNotification.cancel(getContext());
    }

    private void setupShareButton(final Episode episode) {
        mEpisodeShareButton.prepend(FontAwesomeTextView.Icon.SHARE);
        mEpisodeShareButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.shareEpisode(getContext(), episode);
            }
        });
    }

    private void setupDownloadButton(final Episode episode) {
        mEpisodeDownloadButton.setEnabled(true);
        if (episode.isDownloaded()) {
            mEpisodeDownloadButton.setText(getContext().getString(R.string.clear_cache));
            mEpisodeDownloadButton.prepend(FontAwesomeTextView.Icon.MINUS);
            mEpisodeDownloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    episode.clearCache();
                    setupDownloadButton(episode);
                }
            });
        } else if (EpisodeDownloadService.isDownloading(episode)) {
            mEpisodeDownloadButton.setEnabled(false);
            mEpisodeDownloadButton.setText(getContext().getString(R.string.downloading));
            mEpisodeDownloadButton.prepend(FontAwesomeTextView.Icon.SPINNER);
        } else {
            mEpisodeDownloadButton.setText(getContext().getString(R.string.download));
            mEpisodeDownloadButton.prepend(FontAwesomeTextView.Icon.DOWNLOAD);
            mEpisodeDownloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEpisodeDownloadButton.setEnabled(false);
                    Intent intent = EpisodeDownloadService.createIntent(getContext(), episode);
                    getContext().startService(intent);
                    mEpisodeDownloadButton.setText(getContext().getString(R.string.downloading));
                    mEpisodeDownloadButton.prepend(FontAwesomeTextView.Icon.SPINNER);
                }
            });
        }
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
                if (!PodcastPlayer.getInstance().isPlaying())
                    return;
                PodcastPlayer.getInstance().seekTo(seekBar.getProgress());
            }
        });

        mSeekBar.setMax(DateUtils.durationToInt(episode.getDuration()));

        if (PodcastPlayer.getInstance().isPlayingEpisode(episode)) {
            mSeekBar.setEnabled(true);
        } else {
            mSeekBar.setEnabled(false);
        }
    }

    private void updateCurrentTime(int currentPosition) {
        mMediaCurrentTimeTextView.setText(DateUtils.formatCurrentTime(currentPosition));
        mSeekBar.setProgress(currentPosition);
    }

    @Subscribe
    public void onEpisodeDownloadComplete(final DownloadEpisodeCompleteEvent event) {
        mOnContextExecutor.execute(getContext(), new Runnable() {
            @Override
            public void run() {
                Episode episode = event.getEpisode();
                String message = getContext().getString(
                        R.string.episode_download_completed,
                        episode.getTitle());
                ToastUtils.show(getContext(), message);
                setupDownloadButton(episode);
            }
        });
    }

    @Subscribe
    public void onReceivePauseAction(ReceivePauseActionEvent event) {
        mOnContextExecutor.execute(getContext(), new Runnable() {
            @Override
            public void run() {
                mMediaStartAndPauseButton.setChecked(false);
            }
        });
    }
}

