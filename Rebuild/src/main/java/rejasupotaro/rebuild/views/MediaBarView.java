package rejasupotaro.rebuild.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.notifications.PodcastPlayerNotification;

public class MediaBarView extends FrameLayout {

    private View rootView;

    private TextView episodeTitleTextView;

    private CheckBox mediaPlayAndPauseButton;

    private View mediaStopButton;

    public MediaBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public void setup() {
        rootView = View.inflate(getContext(), R.layout.media_bar_view, null);
        findViews(rootView);
        setupViews();
        addView(rootView);
    }

    private void findViews(View view) {
        episodeTitleTextView = (TextView) view.findViewById(R.id.episode_title_text);
        mediaPlayAndPauseButton = (CheckBox) view.findViewById(R.id.media_play_and_pause_button);
        mediaStopButton = view.findViewById(R.id.media_stop_button);
    }

    private void setupViews() {
        mediaStopButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PodcastPlayer.getInstance().stop();
                hide();
            }
        });
    }

    private void setupMediaPlayAndPauseButton(final Episode episode) {
        if (PodcastPlayer.getInstance().isPlayingEpisode(episode)) {
            mediaPlayAndPauseButton.setChecked(true);
        } else {
            mediaPlayAndPauseButton.setChecked(false);
        }

        mediaPlayAndPauseButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
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
            PodcastPlayerNotification.notify(getContext(), episode);
        } else {
            mediaPlayAndPauseButton.setEnabled(false);
            podcastPlayer.start(getContext(), episode, new PodcastPlayer.StateChangedListener() {
                @Override
                public void onStart() {
                    if (getContext() == null) {
                        pause(episode);
                    } else {
                        PodcastPlayerNotification.notify(getContext(), episode);
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
        PodcastPlayerNotification
                .notify(getContext(), episode, PodcastPlayer.getInstance().getCurrentPosition());
    }


    public void show(Episode episode) {
        episodeTitleTextView.setText(episode.getTitle());
        setupMediaPlayAndPauseButton(episode);
        rootView.setVisibility(VISIBLE);
    }

    public void hide() {
        rootView.setVisibility(GONE);
    }
}
