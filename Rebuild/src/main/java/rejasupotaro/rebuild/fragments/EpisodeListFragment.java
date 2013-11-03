package rejasupotaro.rebuild.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.google.inject.Inject;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import rejasupotaro.asyncrssclient.AsyncRssClient;
import rejasupotaro.asyncrssclient.AsyncRssResponseHandler;
import rejasupotaro.asyncrssclient.models.RssFeed;
import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.adapters.EpisodeListAdapter;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.ToastUtils;
import roboguice.fragment.RoboListFragment;

public class EpisodeListFragment extends RoboListFragment {

    private static final String TAG = EpisodeListFragment.class.getSimpleName();

    @Inject
    private LayoutInflater mLayoutInflater;

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

        AsyncRssClient client = new AsyncRssClient();
        client.read(
                "http://feeds.rebuild.fm/rebuildfm",
                new AsyncRssResponseHandler() {
            @Override
            public void onSuccess(RssFeed rssFeed) {
                setupEpisodeListView();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] body, Throwable throwable) {
                ToastUtils.show(getActivity(), "An error occurred while requesting rss feed.");
                Log.e(TAG, "statusCode: " + statusCode);
                for (Header header : headers) {
                    Log.e(TAG, header.getName() + " => " + header.getValue());
                }
                try {
                    Log.e(TAG, new String(body, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, throwable.toString());
            }
        });
    }

    public void setupEpisodeListView() {
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener.onSelect((Episode) l.getItemAtPosition(position));
    }
}
