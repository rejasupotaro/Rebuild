package rejasupotaro.rebuild.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.activities.TimelineActivity;
import rejasupotaro.rebuild.data.adapters.EpisodeListAdapter;
import rejasupotaro.rebuild.api.RssFeedClient;
import rejasupotaro.rebuild.dialogs.EpisodeDownloadDialog;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.ClearEpisodeCacheEvent;
import rejasupotaro.rebuild.events.DownloadEpisodeCompleteEvent;
import rejasupotaro.rebuild.events.LoadEpisodeListCompleteEvent;
import rejasupotaro.rebuild.data.loaders.TweetLoader;
import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.data.models.Tweet;
import rejasupotaro.rebuild.tools.MainThreadExecutor;
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.utils.ToastUtils;
import rejasupotaro.rebuild.views.RecentTweetView;
import rx.functions.Action1;

public class EpisodeListFragment extends Fragment {
    private static final int REQUEST_TWEET_LIST = 1;

    @InjectView(R.id.episode_list_view)
    ListView episodeListView;
    @InjectView(R.id.recent_tweet_view)
    RecentTweetView recentTweetView;

    private RssFeedClient rssFeedClient = new RssFeedClient();
    private MainThreadExecutor mainThreadExecutor = new MainThreadExecutor();
    private EpisodeListAdapter episodeListAdapter;
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
        BusProvider.getInstance().register(this);
        View view = inflater.inflate(R.layout.fragment_episode_list, null);
        ButterKnife.inject(this, view);
        return view;
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
        recentTweetView.setOnClickListener(
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
            public void onSuccess(final List<Episode> episodeList) {
                mainThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        BusProvider.getInstance()
                                .post(new LoadEpisodeListCompleteEvent(episodeList));
                        setupEpisodeListView(episodeList);
                    }
                });
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

                        return new TweetLoader(context, true);
                    }

                    @Override
                    public void onLoadFinished(Loader<List<Tweet>> listLoader,
                                               List<Tweet> tweetList) {
                        recentTweetView.setTweetList(tweetList);
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

        episodeListAdapter.getDownloadButtonEvent().subscribe(new Action1<Episode>() {
            @Override
            public void call(Episode episode) {
                AlertDialog dialog
                        = EpisodeDownloadDialog.newInstance(getActivity(), episode);
                dialog.show();
            }
        });
    }

    @Subscribe
    public void onEpisodeDownloadComplete(final DownloadEpisodeCompleteEvent event) {
        mainThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                requestFeed();
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
