package rejasupotaro.rebuild.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.google.inject.Inject;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.adapters.EpisodeListAdapter;
import rejasupotaro.rebuild.api.EpisodeClient;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.ToastUtils;
import roboguice.fragment.RoboListFragment;

public class EpisodeListFragment extends RoboListFragment {

    @Inject
    private LayoutInflater mLayoutInflater;

    @Inject
    private EpisodeClient mClient;

    private OnEpisodeSelectListener mListener;

    public static interface OnEpisodeSelectListener {
        public void onSelect(Episode episode);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnEpisodeSelectListener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setDivider(null);
        getListView().setFadingEdgeLength(0);
        getListView().addHeaderView(mLayoutInflater.inflate(R.layout.header_episode_list, null));

        mClient.request(new EpisodeClient.EpisodeClientResponseHandler() {
            @Override
            public void onSuccess(List<Episode> episodeList) {
                setupEpisodeListView(episodeList);
            }

            @Override
            public void onFailure() {
                ToastUtils.show(getActivity(), "An error occurred while requesting rss feed.");
            }
        });
    }

    public void setupEpisodeListView(List<Episode> episodeList) {
        EpisodeListAdapter episodeListAdapter = new EpisodeListAdapter(getActivity(), 0, episodeList);
        setListAdapter(episodeListAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener.onSelect((Episode) l.getItemAtPosition(position));
    }
}
