package rejasupotaro.rebuild.fragments;

import com.google.inject.Inject;

import com.squareup.otto.Subscribe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.activities.TimelineActivity;
import rejasupotaro.rebuild.adapters.EpisodeListAdapter;
import rejasupotaro.rebuild.api.RssFeedClient;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.ClearEpisodeCacheEvent;
import rejasupotaro.rebuild.events.DownloadEpisodeCompleteEvent;
import rejasupotaro.rebuild.events.LoadEpisodeListCompleteEvent;
import rejasupotaro.rebuild.loaders.TweetLoader;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.models.Tweet;
import rejasupotaro.rebuild.tools.MainThreadExecutor;
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.utils.ToastUtils;
import rejasupotaro.rebuild.utils.UiAnimations;
import rejasupotaro.rebuild.views.RecentlyTweetView;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EpisodeListFragment extends RoboFragment {

    private static final int REQUEST_TWEET_LIST = 1;

    @Inject
    private RssFeedClient rssFeedClient;

    @InjectView(R.id.app_title_text)
    private View appTitleTextView;

    private EpisodeListAdapter episodeListAdapter;

    @InjectView(R.id.episode_list_view)
    private ListView episodeListView;

    @InjectView(R.id.recently_tweet_view)
    private RecentlyTweetView recentlyTweetView;

    private OnEpisodeSelectListener listener;

    @Inject
    private MainThreadExecutor mainThreadExecutor;

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
        BusProvider.getInstance().register(this);
        return inflater.inflate(R.layout.fragment_episode_list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupListView();
        requestFeed();
        requestTweetList();
    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
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
        recentlyTweetView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), TimelineActivity.class));
                    }
                }
        );
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
        rssFeedClient.request(new RssFeedClient.EpisodeClientResponseHandler() {
            @Override
            public void onSuccess(List<Episode> episodeList) {
                BusProvider.getInstance().post(new LoadEpisodeListCompleteEvent(episodeList));
                setupEpisodeListView(episodeList);
            }

            @Override
            public void onFailure() {
                if (shouldShowError()) {
                    ToastUtils.showNetworkError(getActivity());
                }
            }
        });
    }

    private void requestTweetList() {
        getLoaderManager().restartLoader(REQUEST_TWEET_LIST, null,
                new LoaderManager.LoaderCallbacks<List<Tweet>>() {
                    @Override
                    public Loader<List<Tweet>> onCreateLoader(int i, Bundle bundle) {
                        Context context = getActivity();
                        if (context == null) {
                            return null;
                        }

                        UiAnimations.slideUp(context, recentlyTweetView, 0, 500);
                        return new TweetLoader(context, true);
                    }

                    @Override
                    public void onLoadFinished(Loader<List<Tweet>> listLoader,
                            List<Tweet> tweetList) {
                        recentlyTweetView.setTweetList(tweetList);
                    }

                    @Override
                    public void onLoaderReset(Loader<List<Tweet>> listLoader) {
                        // nothing to do
                    }
                }
        );
    }

    private boolean shouldShowError() {
        return (episodeListView == null
                && episodeListView.getCount() == 0);
    }

    public void setupEpisodeListView(List<Episode> episodeList) {
        episodeListAdapter = new EpisodeListAdapter(getActivity(), episodeList);
        episodeListView.setAdapter(episodeListAdapter);
    }

    @Subscribe
    public void onEpisodeDownloadComplete(final DownloadEpisodeCompleteEvent event) {
        mainThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (episodeListAdapter.includeEpisode(event.getEpisodeId())) {
                    episodeListAdapter.notifyDataSetChanged();
                } else {
                    requestFeed();
                }
            }
        });
    }

    @Subscribe
    public void onEpisodeCacheCleared(final ClearEpisodeCacheEvent event) {
        mainThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                episodeListAdapter.notifyDataSetChanged();
            }
        });
    }
}
