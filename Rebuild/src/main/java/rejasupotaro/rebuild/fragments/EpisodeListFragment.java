package rejasupotaro.rebuild.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.inject.Inject;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.activities.TimelineActivity;
import rejasupotaro.rebuild.adapters.EpisodeListAdapter;
import rejasupotaro.rebuild.api.RssFeedClient;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.LoadEpisodeListCompleteEvent;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.utils.ToastUtils;
import rejasupotaro.rebuild.utils.ViewUtils;
import rejasupotaro.rebuild.views.FontAwesomeTextView;
import rejasupotaro.rebuild.views.StateFrameLayout;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EpisodeListFragment extends RoboFragment {

    @Inject
    private RssFeedClient mClient;

    @InjectView(R.id.state_frame_layout)
    StateFrameLayout stateFrameLayout;

    @InjectView(R.id.episode_list_view)
    private ListView episodeListView;

    private OnEpisodeSelectListener listener;

    public static interface OnEpisodeSelectListener {
        public void onSelect(Episode episode);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (OnEpisodeSelectListener) activity;
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
        setupListViewHeader();
        setupListViewFooter();

        episodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Episode episode = (Episode) episodeListView.getItemAtPosition(position);
                listener.onSelect(episode);
            }
        });
    }

    private void setupListViewHeader() {
        View header = View.inflate(getActivity(), R.layout.header_episode_list, null);

        FontAwesomeTextView websiteLinkText = (FontAwesomeTextView) header.findViewById(R.id.link_text_website);
        websiteLinkText.prepend(FontAwesomeTextView.Icon.HOME);
        websiteLinkText.findViewById(R.id.link_text_website).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openRebuildWeb(getActivity());
            }
        });

        FontAwesomeTextView twitterLinkText = (FontAwesomeTextView) header.findViewById(R.id.link_text_twitter);
        twitterLinkText.prepend(FontAwesomeTextView.Icon.TWITTER);
        twitterLinkText.findViewById(R.id.link_text_twitter).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), TimelineActivity.class));
                    }
                });

        ViewUtils.addHeaderView(episodeListView, header);
    }

    private void setupListViewFooter() {
        View footer = View.inflate(getActivity(), R.layout.footer_episode_list, null);

        footer.findViewById(R.id.miyagawa_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openMiyagawaProfile(getActivity());
            }
        });

        episodeListView.addFooterView(footer, null, false);
    }

    private void requestFeed() {
        stateFrameLayout.showProgress();
        mClient.request(new RssFeedClient.EpisodeClientResponseHandler() {
            @Override
            public void onSuccess(List<Episode> episodeList) {
                setupEpisodeListView(episodeList);
                BusProvider.getInstance().post(new LoadEpisodeListCompleteEvent(episodeList));
                stateFrameLayout.showContent();
            }

            @Override
            public void onFailure() {
                if (shouldShowError()) {
                    ToastUtils.showNetworkError(getActivity());
                    stateFrameLayout.showError();
                }
            }
        });
    }

    private boolean shouldShowError() {
        return (episodeListView == null
                && episodeListView.getCount() == 0);
    }

    public void setupEpisodeListView(List<Episode> episodeList) {
        EpisodeListAdapter episodeListAdapter = new EpisodeListAdapter(getActivity(), episodeList);
        episodeListView.setAdapter(episodeListAdapter);
    }
}
