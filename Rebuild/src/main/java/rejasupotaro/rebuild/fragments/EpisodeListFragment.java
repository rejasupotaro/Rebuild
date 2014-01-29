package rejasupotaro.rebuild.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.inject.Inject;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.activities.TwitterWidgetActivity;
import rejasupotaro.rebuild.adapters.EpisodeListAdapter;
import rejasupotaro.rebuild.api.RssFeedClient;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.LoadEpisodeListCompleteEvent;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.ToastUtils;
import rejasupotaro.rebuild.views.FontAwesomeTextView;
import rejasupotaro.rebuild.views.StateFrameLayout;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EpisodeListFragment extends RoboFragment {

    @Inject
    private RssFeedClient mClient;

    @InjectView(R.id.state_frame_layout)
    StateFrameLayout mStateFrameLayout;

    @InjectView(R.id.episode_list_view)
    private ListView mEpisodeListView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episode_list, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupListView();
        requestFeed();
    }

    private void setupListView() {
        View header = View.inflate(getActivity(), R.layout.header_episode_list, null);
        setupHeaderLinkText(header);
        mEpisodeListView.addHeaderView(header);

        mEpisodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Episode episode = (Episode) mEpisodeListView.getItemAtPosition(position);
                mListener.onSelect(episode);
            }
        });
    }

    private void setupHeaderLinkText(View header) {
        FontAwesomeTextView websiteLinkText = (FontAwesomeTextView) header.findViewById(R.id.link_text_website);
        websiteLinkText.prepend(0xF015);
        websiteLinkText.findViewById(R.id.link_text_website).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://rebuild.fm"));
                startActivity(intent);
            }
        });

        FontAwesomeTextView twitterLinkText = (FontAwesomeTextView) header.findViewById(R.id.link_text_twitter);
        twitterLinkText.prepend(0xF099);
        twitterLinkText.findViewById(R.id.link_text_twitter).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), TwitterWidgetActivity.class));
                    }
                });
    }

    private void requestFeed() {
        mStateFrameLayout.showProgress();
        mClient.request(new RssFeedClient.EpisodeClientResponseHandler() {
            @Override
            public void onSuccess(List<Episode> episodeList) {
                setupEpisodeListView(episodeList);
                BusProvider.getInstance().post(new LoadEpisodeListCompleteEvent(episodeList));
                mStateFrameLayout.showContent();
            }

            @Override
            public void onFailure() {
                ToastUtils.show(getActivity(), "An error occurred while requesting rss feed.");
                mStateFrameLayout.showError();
            }
        });
    }

    public void setupEpisodeListView(List<Episode> episodeList) {
        EpisodeListAdapter episodeListAdapter = new EpisodeListAdapter(getActivity(), 0, episodeList);
        mEpisodeListView.setAdapter(episodeListAdapter);
    }
}
