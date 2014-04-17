package rejasupotaro.rebuild.fragments;

import com.squareup.otto.Subscribe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.LoadEpisodeListCompleteEvent;
import rejasupotaro.rebuild.listener.LoadListener;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.tools.OnContextExecutor;
import rejasupotaro.rebuild.views.EpisodeMediaView;
import rejasupotaro.rebuild.views.StateFrameLayout;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EpisodeMediaFragment extends RoboFragment {

    @InjectView(R.id.state_frame_layout)
    private StateFrameLayout stateFrameLayout;

    @InjectView(R.id.episode_media_view)
    private EpisodeMediaView episodeMediaView;

    private Episode episode;

    @Inject
    private OnContextExecutor onContextExecutor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        BusProvider.getInstance().register(this);
        return inflater.inflate(R.layout.fragment_episode_media, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        episodeMediaView.onDestroy();
        super.onDestroyView();
    }

    public void setup(final Episode episode) {
        if (episode == null) {
            getActivity().finish();
        }
        this.episode = episode;

        episodeMediaView.setup(episode, new LoadListener() {
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
