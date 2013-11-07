package rejasupotaro.rebuild.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.PodcastPauseButtonClickEvent;
import rejasupotaro.rebuild.events.PodcastPlayButtonClickEvent;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.DateUtils;

public class MediaControllerView extends LinearLayout {

    private TextView mMediaCurrentTimeTextView;

    private TextView mMediaDurationTextView;

    private ImageView mMediaPlayButton;

    private SeekBar mMediaSeekBar;

    public MediaControllerView(Context context) {
        super(context);
        setupView(context);
    }

    public MediaControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    private void setupView(Context context) {
        LayoutParams params =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        View view = inflate(context, R.layout.view_media_controller, null);

        mMediaCurrentTimeTextView = (TextView) view.findViewById(R.id.media_current_time);
        mMediaDurationTextView = (TextView) view.findViewById(R.id.media_duration);
        mMediaPlayButton = (ImageView) view.findViewById(R.id.media_play_button);
        mMediaSeekBar = (SeekBar) view.findViewById(R.id.media_seekbar);

        addView(view, params);
    }

    public void setEpisode(final Episode episode) {
        final PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        if (podcastPlayer.isSameEpisode(episode)) return;

        mMediaDurationTextView.setText(episode.getDuration());

        podcastPlayer.setCurrentTimeListener(new PodcastPlayer.CurrentTimeListener() {
            @Override
            public void onTick(int currentPosition) {
                updateCurrentTime(currentPosition);
            }
        });

        mMediaPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (podcastPlayer.isPlaying()) {
                    mMediaPlayButton.setBackgroundResource(android.R.drawable.ic_media_play);
                    podcastPlayer.pause();
                    BusProvider.getInstance().post(new PodcastPauseButtonClickEvent());
                } else {
                    mMediaPlayButton.setBackgroundResource(android.R.drawable.ic_media_pause);
                    podcastPlayer.start();
                    BusProvider.getInstance().post(new PodcastPlayButtonClickEvent(episode));
                }
            }
        });

        mMediaSeekBar.setMax(DateUtils.durationToInt(episode.getDuration()));
        mMediaSeekBar.setEnabled(false);
    }

    public void play(Context context, Episode episode) {
        final PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        podcastPlayer.play(context, episode, new PodcastPlayer.StateChangedListener() {
            @Override
            public void onStart() {
                mMediaPlayButton.setBackgroundResource(android.R.drawable.ic_media_pause);
            }
        });

    }

    public void updateCurrentTime(int currentPosition) {
        mMediaCurrentTimeTextView.setText(DateUtils.formatCurrentTime(currentPosition));
        mMediaSeekBar.setProgress(currentPosition);
    }
}
