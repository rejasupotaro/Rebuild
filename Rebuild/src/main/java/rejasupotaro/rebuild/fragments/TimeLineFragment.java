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
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.models.Tweet;
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.views.ExtendedListView;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class TimelineFragment extends RoboFragment {

    private Episode episode;

    @InjectView(R.id.episode_tweet_list)
    private ExtendedListView episodeTweetListView;

    private EpisodeTweetListAdapter episodeTweetListAdapter;

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
                if (getActivity() == null) {
                    return;
                }

                setupEpisodeTweetList(tweetList);
            }

            @Override
            public void onError() {
                // TODO: show error view
            }
        });
    }

    private void setupEpisodeTweetList(List<Tweet> tweetList) {
        episodeTweetListAdapter = new EpisodeTweetListAdapter(getActivity(), tweetList);
        episodeTweetListView.setAdapter(episodeTweetListAdapter);

        episodeTweetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Tweet item = episodeTweetListAdapter.getItem(i);
                IntentUtils.openTwitter(getActivity(), item.getId(), item.getUserName());
            }
        });

        episodeTweetListView.setOnPositionChangedListener(new ExtendedListView.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(ExtendedListView listView, int firstVisiblePosition, View scrollBarPanel) {
                Tweet tweet = episodeTweetListAdapter.getItem(firstVisiblePosition);
                ((TextView) scrollBarPanel).setText(tweet.getTweetTimeText());
            }
        });
    }

    public void setEpisode(final Episode episode) {
        this.episode = episode;
    }
}

