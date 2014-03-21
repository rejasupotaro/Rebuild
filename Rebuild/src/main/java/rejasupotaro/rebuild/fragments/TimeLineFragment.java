package rejasupotaro.rebuild.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Episode;
import roboguice.fragment.RoboFragment;

public class TimelineFragment extends RoboFragment {

    private Episode episode;


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
    }

    public void setEpisode(final Episode episode) {
        this.episode = episode;
    }
}

