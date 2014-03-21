package rejasupotaro.rebuild.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.api.EpisodeTweetClient;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.models.Tweet;
import roboguice.fragment.RoboFragment;

public class TimelineFragment extends RoboFragment {

    private Episode episode;

    private static final EpisodeTweetClient episodeTweetClient = new EpisodeTweetClient();

    public static TimelineFragment newInstance(Episode episode) {
        TimelineFragment fragment = new TimelineFragment();
        fragment.setEpisode(episode);
        return fragment;
    }

    private TimelineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestEpisodeTweetList();
    }

    private void requestEpisodeTweetList() {
        episodeTweetClient.fetch(new EpisodeTweetClient.EpisodeTweetResponseHandler() {
            @Override
            public void onSuccess(List<Tweet> tweetList) {
                // TODO: show tweet list
            }

            @Override
            public void onError() {
                // TODO: show error view
            }
        });
    }

    public void setEpisode(final Episode episode) {
        this.episode = episode;
    }
}

