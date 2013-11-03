package rejasupotaro.rebuild.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.adapters.EpisodeListAdapter;
import rejasupotaro.rebuild.models.Episode;
import roboguice.fragment.RoboListFragment;

public class EpisodeListFragment extends RoboListFragment {

    @Inject
    android.view.LayoutInflater mLayoutInflater;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setDivider(null);
        getListView().setFadingEdgeLength(0);
        getListView().addHeaderView(mLayoutInflater.inflate(R.layout.header_episode_list, null));

        List<Episode> episodeList = new ArrayList<Episode>();

        episodeList.add(Episode.newDummyInstance());
        episodeList.add(Episode.newDummyInstance());
        episodeList.add(Episode.newDummyInstance());
        episodeList.add(Episode.newDummyInstance());
        episodeList.add(Episode.newDummyInstance());
        episodeList.add(Episode.newDummyInstance());
        episodeList.add(Episode.newDummyInstance());
        episodeList.add(Episode.newDummyInstance());

        EpisodeListAdapter episodeListAdapter = new EpisodeListAdapter(getActivity(), 0, episodeList);
        setListAdapter(episodeListAdapter);
    }
}
