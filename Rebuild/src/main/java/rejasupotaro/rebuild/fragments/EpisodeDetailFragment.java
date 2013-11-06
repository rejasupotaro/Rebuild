package rejasupotaro.rebuild.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.LoadEpisodeListCompleteEvent;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.UiAnimations;
import rejasupotaro.rebuild.views.MediaControllerView;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EpisodeDetailFragment extends RoboFragment {

    @InjectView(R.id.episode_title)
    private TextView mEpisodeTitleTextView;

    @InjectView(R.id.episode_description)
    private TextView mEpisodeDescriptionTextView;

    @InjectView(R.id.media_controller_view)
    private MediaControllerView mMediaControllerView;

    @InjectView(R.id.media_play_button_on_image_cover)
    private View mMediaPlayButtonOnImageCover;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BusProvider.getInstance().register(this);
        return inflater.inflate(R.layout.fragment_episode_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusProvider.getInstance().unregister(this);
    }

    public void setup(final Episode episode) {
        PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        if (podcastPlayer.isSameEpisode(episode)) {
            mMediaPlayButtonOnImageCover.setVisibility(View.GONE);
            return;
        }

        mEpisodeTitleTextView.setText(episode.getTitle());
        mEpisodeDescriptionTextView.setText(episode.getDescription());

        mMediaPlayButtonOnImageCover.setVisibility(View.VISIBLE);
        mMediaPlayButtonOnImageCover.setAlpha(1);
        mMediaPlayButtonOnImageCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaControllerView.setEpisode(episode);
                mMediaControllerView.play(getActivity(), episode);
                UiAnimations.fadeOut(mMediaPlayButtonOnImageCover, 300, 1000);
            }
        });
    }

    @Subscribe
    public void onLoadEpisodeListComplete(LoadEpisodeListCompleteEvent event) {
        // impl me
    }
}
