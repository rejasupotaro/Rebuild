package rejasupotaro.rebuild.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.adapters.EpisodeListAdapter;
import rejasupotaro.rebuild.models.Episode;

public class EpsodeListFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Episode> episodeList = new ArrayList<Episode>();

        episodeList.add(new Episode());
        episodeList.add(new Episode());
        episodeList.add(new Episode());

        EpisodeListAdapter episodeListAdapter = new EpisodeListAdapter(getActivity(), 0, episodeList);
        setListAdapter(episodeListAdapter);
    }

}
