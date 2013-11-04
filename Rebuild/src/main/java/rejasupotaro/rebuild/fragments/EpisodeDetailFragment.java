package rejasupotaro.rebuild.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.DateUtils;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EpisodeDetailFragment extends RoboFragment {

    @InjectView(R.id.episode_title)
    private TextView mEpisodeTitleTextView;

    @InjectView(R.id.episode_description)
    private TextView mEpisodeDescriptionTextView;

    @InjectView(R.id.media_current_time)
    private TextView mMediaCurrentTimeTextView;

    @InjectView(R.id.media_duration)
    private TextView mMediaDurationTextView;

    @InjectView(R.id.media_play_button)
    private ImageView mMediaPlayButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_episode_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setup(Episode episode) {
        mEpisodeTitleTextView.setText(episode.getTitle());
        mEpisodeDescriptionTextView.setText(episode.getDescription());
        mMediaDurationTextView.setText(episode.getDuration());

        final PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        podcastPlayer.play(getActivity(), episode.getEnclosure(), new PodcastPlayer.StateChangedListener() {
            @Override
            public void onStart() {
                mMediaPlayButton.setBackgroundResource(android.R.drawable.ic_media_pause);
            }
        });

        podcastPlayer.setCurrentTimeListener(new PodcastPlayer.CurrentTimeListener() {
            @Override
            public void onTick(int currentPosition) {
                mMediaCurrentTimeTextView.setText(DateUtils.formatCurrentTime(currentPosition));
            }
        });

        mMediaPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (podcastPlayer.isPlaying()) {
                    mMediaPlayButton.setBackgroundResource(android.R.drawable.ic_media_play);
                    podcastPlayer.pause();
                } else {
                    mMediaPlayButton.setBackgroundResource(android.R.drawable.ic_media_pause);
                    podcastPlayer.start();
                }
            }
        });
    }
}
