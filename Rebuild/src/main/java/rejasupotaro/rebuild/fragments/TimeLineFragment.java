package rejasupotaro.rebuild.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.adapters.EpisodeTweetListAdapter;
import rejasupotaro.rebuild.api.EpisodeTweetClient;
import rejasupotaro.rebuild.listener.MoreLoadListener;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.models.Tweet;
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.views.ExtendedListView;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class TimelineFragment extends RoboFragment {

    private static final String ARGS_KEY_EPISODE_ID = "episode_id";

    private int episodeId;

    @InjectView(R.id.episode_tweet_list)
    private ExtendedListView episodeTweetListView;

    private EpisodeTweetListAdapter episodeTweetListAdapter;

    private EpisodeTweetClient episodeTweetClient = new EpisodeTweetClient();

    private int page = 1;

    private static final int PER_PAGE = 10;

    private MoreLoadListener moreLoadListener;

    public static TimelineFragment newInstance(Episode episode) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_KEY_EPISODE_ID, episode.getEpisodeId());
        fragment.setArguments(args);
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
        episodeId = getEpisodeId();
        page = 1;
        setupEpisodeTweetList();
    }

    private int getEpisodeId() {
        Bundle args = getArguments();
        return args.getInt(ARGS_KEY_EPISODE_ID);
    }

    private void setupEpisodeTweetList() {
        moreLoadListener = new MoreLoadListener(getActivity(), episodeTweetListView) {
            @Override
            public void onLoadMore() {
                requestEpisodeTweetList(page, PER_PAGE);
                page++;
            }
        };
        episodeTweetListView.setOnScrollListener(moreLoadListener);

        episodeTweetListAdapter = new EpisodeTweetListAdapter(getActivity());
        episodeTweetListView.setAdapter(episodeTweetListAdapter);

        episodeTweetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Tweet item = episodeTweetListAdapter.getItem(i);
                IntentUtils.openTwitter(getActivity(), item.getEpisodeId(), item.getUserName());
            }
        });

        episodeTweetListView.setOnPositionChangedListener(
                new ExtendedListView.OnPositionChangedListener() {
                    @Override
                    public void onPositionChanged(ExtendedListView listView,
                            int firstVisiblePosition, View scrollBarPanel) {
                        Tweet tweet = episodeTweetListAdapter.getItem(firstVisiblePosition);
                        ((TextView) scrollBarPanel).setText(tweet.getTweetTimeText());
                    }
                });
    }

    private void requestEpisodeTweetList(int page, int perPage) {
        episodeTweetClient.fetch(episodeId, page, perPage,
                new EpisodeTweetClient.EpisodeTweetResponseHandler() {
                    @Override
                    public void onSuccess(List<Tweet> tweetList) {
                        if (getActivity() == null) {
                            return;
                        }

                        episodeTweetListAdapter.addAll(tweetList);
                    }

                    @Override
                    public void onError() {
                        moreLoadListener.finish();
                    }
                });
    }
}

