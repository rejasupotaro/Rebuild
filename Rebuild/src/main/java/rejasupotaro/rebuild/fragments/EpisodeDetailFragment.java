package rejasupotaro.rebuild.fragments;

import com.google.inject.Inject;

import com.squareup.otto.Subscribe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.adapters.ShowNoteListAdapter;
import rejasupotaro.rebuild.api.EpisodeDownloadClient;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.LoadEpisodeListCompleteEvent;
import rejasupotaro.rebuild.listener.LoadListener;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.models.Link;
import rejasupotaro.rebuild.tools.OnContextExecutor;
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.utils.ViewUtils;
import rejasupotaro.rebuild.views.EpisodeDetailHeaderView;
import rejasupotaro.rebuild.views.StateFrameLayout;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EpisodeDetailFragment extends RoboFragment {

    @InjectView(R.id.state_frame_layout)
    private StateFrameLayout mStateFrameLayout;

    @Inject
    private EpisodeDownloadClient mEpisodeDownloadClient;

    @InjectView(R.id.show_note_list)
    private ListView mShowNoteListView;

    private Episode mEpisode;

    private EpisodeDetailHeaderView mEpisodeDetailHeaderView;

    @Inject
    private OnContextExecutor mOnContextExecutor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mEpisodeDetailHeaderView = new EpisodeDetailHeaderView(getActivity(), new LoadListener() {
            @Override
            public void showProgress() {
                mStateFrameLayout.showProgress();
            }

            @Override
            public void showError() {
                mStateFrameLayout.showError();

            }

            @Override
            public void showContent() {
                mStateFrameLayout.showContent();
            }
        });
        BusProvider.getInstance().register(this);
        return inflater.inflate(R.layout.fragment_episode_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEpisodeDetailHeaderView.onActivityCreated();
    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        mEpisodeDetailHeaderView.onDestroy();
        super.onDestroyView();
    }

    public void setup(final Episode episode) {
        if (episode == null) {
            getActivity().finish();
        }

        mEpisode = episode;
        mEpisodeDetailHeaderView.setEpisode(episode);
        setupListView(episode);
        setTitle(episode);
    }

    private void setupListView(Episode episode) {
        List<Link> linkList = Link.Parser.toLinkList(episode.getShowNotes());
        final ShowNoteListAdapter adapter = new ShowNoteListAdapter(
                getActivity(), linkList, mItemClickListener);

        ViewUtils.addHeaderView(mShowNoteListView, mEpisodeDetailHeaderView);
        mShowNoteListView.setAdapter(adapter);
    }

    private ShowNoteListAdapter.ItemClickListener mItemClickListener
            = new ShowNoteListAdapter.ItemClickListener() {
        @Override
        public void onClick(Link item) {
            IntentUtils.openBrowser(getActivity(), item.getUrl());
        }

        @Override
        public void onLongClick(Link item) {
            IntentUtils.sendPostIntent(getActivity(), item.getUrl());
        }
    };

    private void setTitle(Episode episode) {
        String originalTitle = episode.getTitle();
        int startIndex = originalTitle.indexOf(':');
        getActivity().getActionBar().setTitle("Episode " + originalTitle.substring(0, startIndex));
    }

    @Subscribe
    public void onLoadEpisodeListComplete(final LoadEpisodeListCompleteEvent event) {
        if (mEpisode != null) {
            return;
        }

        mOnContextExecutor.execute(getActivity(), new Runnable() {
            @Override
            public void run() {
                List<Episode> episodeList = event.getEpisodeList();

                if (episodeList == null || episodeList.size() == 0) {
                    return;
                }
                Episode episode = episodeList.get(0);

                if (PodcastPlayer.getInstance().isPlaying()) {
                    return;
                }

                setup(episode);
            }
        });
    }
}
