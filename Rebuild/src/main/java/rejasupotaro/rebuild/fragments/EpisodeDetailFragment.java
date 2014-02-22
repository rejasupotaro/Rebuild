package rejasupotaro.rebuild.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.inject.Inject;
import com.squareup.otto.Subscribe;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.adapters.ShowNoteListAdapter;
import rejasupotaro.rebuild.api.EpisodeDownloadClient;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.DownloadEpisodeCompleteEvent;
import rejasupotaro.rebuild.events.LoadEpisodeListCompleteEvent;
import rejasupotaro.rebuild.events.ReceivePauseActionEvent;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.models.Link;
import rejasupotaro.rebuild.notifications.PodcastPlayerNotification;
import rejasupotaro.rebuild.services.EpisodeDownloadService;
import rejasupotaro.rebuild.tools.OnContextExecutor;
import rejasupotaro.rebuild.utils.DateUtils;
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.utils.ToastUtils;
import rejasupotaro.rebuild.utils.UiAnimations;
import rejasupotaro.rebuild.views.FontAwesomeTextView;
import rejasupotaro.rebuild.views.StateFrameLayout;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EpisodeDetailFragment extends RoboFragment {

    @InjectView(R.id.state_frame_layout)
    private StateFrameLayout mStateFrameLayout;

    @Inject
    private EpisodeDownloadClient mEpisodeDownloadClient;

    private TextView mEpisodeTitleTextView;

    private View mMediaStartButtonOnImageCover;

    private TextView mMediaCurrentTimeTextView;

    private TextView mMediaDurationTextView;

    private CheckBox mMediaStartAndPauseButton;

    private SeekBar mSeekBar;

    private TextView mEpisodeDescriptionTextView;

    private FontAwesomeTextView mEpisodeDownloadButton;

    @InjectView(R.id.show_note_list)
    private ListView mShowNoteListView;

    private Episode mEpisode;

    @Inject
    private OnContextExecutor mOnContextExecutor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BusProvider.getInstance().register(this);
        return inflater.inflate(R.layout.fragment_episode_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEpisodeDescriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void findViews(View view) {
        mEpisodeTitleTextView = (TextView) view.findViewById(R.id.episode_title);
        mMediaStartButtonOnImageCover = view.findViewById(R.id.episode_detail_header_cover);
        mMediaCurrentTimeTextView = (TextView) view.findViewById(R.id.media_current_time);
        mMediaDurationTextView = (TextView) view.findViewById(R.id.media_duration);
        mMediaStartAndPauseButton = (CheckBox) view.findViewById(R.id.media_start_and_pause_button);
        mSeekBar = (SeekBar) view.findViewById(R.id.media_seekbar);
        mEpisodeDescriptionTextView = (TextView) view.findViewById(R.id.episode_description);
        mEpisodeDownloadButton = (FontAwesomeTextView) view.findViewById(R.id.episode_download_button);
    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }

    public void setup(final Episode episode) {
        if (episode == null) getActivity().finish();
        mEpisode = episode;

        View headerView = View.inflate(getActivity(), R.layout.header_episode_detail, null);
        findViews(headerView);
        setupListView(episode, headerView);

        setupDownloadButton(episode);

        setupMediaPlayAndPauseButton(episode);
        setupSeekBar(episode);

        setTitle(episode);
        mEpisodeDescriptionTextView.setText(
                Html.fromHtml(StringUtils.buildTwitterLinkText(episode.getDescription())));
    }

    private void setupListView(Episode episode, View headerView) {
        mShowNoteListView.addHeaderView(headerView, null, false);
        List<Link> linkList = Link.Parser.toLinkList(episode.getShowNotes());
        final ShowNoteListAdapter adapter = new ShowNoteListAdapter(
                getActivity(), linkList, mItemClickListener);
        mShowNoteListView.setAdapter(adapter);
    }

    private ShowNoteListAdapter.ItemClickListener mItemClickListener = new ShowNoteListAdapter.ItemClickListener() {
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
        mEpisodeTitleTextView.setText(originalTitle.substring(startIndex + 2));
    }

    private void setupSeekBar(final Episode episode) {
        mMediaDurationTextView.setText(episode.getDuration());

        PodcastPlayer.getInstance().setCurrentTimeListener(
                new PodcastPlayer.CurrentTimeListener() {
                    @Override
                    public void onTick(int currentPosition) {
                        if (PodcastPlayer.getInstance().isPlayingEpisode(episode)) {
                            updateCurrentTime(currentPosition);
                        } else {
                            updateCurrentTime(0);
                        }
                    }
                });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!PodcastPlayer.getInstance().isPlaying())
                    return;
                PodcastPlayer.getInstance().seekTo(seekBar.getProgress());
            }
        });

        mSeekBar.setMax(DateUtils.durationToInt(episode.getDuration()));

        if (PodcastPlayer.getInstance().isPlayingEpisode(episode)) {
            mSeekBar.setEnabled(true);
        } else {
            mSeekBar.setEnabled(false);
        }
    }

    private void updateCurrentTime(int currentPosition) {
        mMediaCurrentTimeTextView.setText(DateUtils.formatCurrentTime(currentPosition));
        mSeekBar.setProgress(currentPosition);
    }

    private void setupDownloadButton(final Episode episode) {
        mEpisodeDownloadButton.setEnabled(true);
        if (episode.isDownloaded()) {
            mEpisodeDownloadButton.setText(getString(R.string.clear_cache));
            mEpisodeDownloadButton.prepend(FontAwesomeTextView.Icon.MINUS);
            mEpisodeDownloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    episode.clearCache();
                    setupDownloadButton(episode);
                }
            });
        } else if (EpisodeDownloadService.isDownloading(episode)) {
            mEpisodeDownloadButton.setEnabled(false);
            mEpisodeDownloadButton.setText(getString(R.string.downloading));
            mEpisodeDownloadButton.prepend(FontAwesomeTextView.Icon.SPINNER);
        } else {
            mEpisodeDownloadButton.setText(getString(R.string.download));
            mEpisodeDownloadButton.prepend(FontAwesomeTextView.Icon.DOWNLOAD);
            mEpisodeDownloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEpisodeDownloadButton.setEnabled(false);
                    Intent intent = EpisodeDownloadService.createIntent(getActivity(), episode);
                    getActivity().startService(intent);
                    mEpisodeDownloadButton.setText(getString(R.string.downloading));
                    mEpisodeDownloadButton.prepend(FontAwesomeTextView.Icon.SPINNER);
                }
            });
        }
    }

    private void setupMediaPlayAndPauseButton(final Episode episode) {
        if (PodcastPlayer.getInstance().isPlayingEpisode(episode)) {
            mMediaStartAndPauseButton.setChecked(true);
            mMediaStartButtonOnImageCover.setVisibility(View.GONE);
        } else {
            mMediaStartAndPauseButton.setChecked(false);
            mMediaStartButtonOnImageCover.setVisibility(View.VISIBLE);
            mMediaStartButtonOnImageCover.setAlpha(1);
        }

        mMediaStartAndPauseButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            start(episode);
                        } else {
                            pause();
                        }
                    }
                });
    }

    private void start(final Episode episode) {
        final PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        if (shouldRestart(episode)) {
            podcastPlayer.start();
            mSeekBar.setEnabled(true);
            PodcastPlayerNotification.notity(getActivity(), episode);
        } else {
            mStateFrameLayout.showProgress();
            mMediaStartAndPauseButton.setEnabled(false);
            podcastPlayer.start(getActivity(), episode, new PodcastPlayer.StateChangedListener() {
                @Override
                public void onStart() {
                    if (getActivity() == null) {
                        pause();
                    } else {
                        mStateFrameLayout.showContent();
                        mSeekBar.setEnabled(true);
                        mMediaStartAndPauseButton.setEnabled(true);
                        PodcastPlayerNotification.notity(getActivity(), episode);
                    }
                }
            });

            UiAnimations.fadeOut(mMediaStartButtonOnImageCover, 300, 1000);
        }
    }

    private boolean shouldRestart(Episode episode) {
        PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        return (podcastPlayer.isPlayingEpisode(episode)
                && podcastPlayer.getCurrentPosition() > 0);
    }

    private void pause() {
        final PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        podcastPlayer.pause();
        mSeekBar.setEnabled(false);

        PodcastPlayerNotification.cancel(getActivity());
    }

    @Subscribe
    public void onEpisodeDownloadComplete(final DownloadEpisodeCompleteEvent event) {
        mOnContextExecutor.execute(getActivity(), new Runnable() {
            @Override
            public void run() {
                Episode episode = event.getEpisode();
                ToastUtils.show(getActivity(),
                        getString(R.string.episode_download_completed, episode.getTitle()));
                setupDownloadButton(episode);
            }
        });
    }

    @Subscribe
    public void onReceivePauseAction(ReceivePauseActionEvent event) {
        mMediaStartAndPauseButton.setChecked(false);
    }

    @Subscribe
    public void onLoadEpisodeListComplete(LoadEpisodeListCompleteEvent event) {
        if (mEpisode != null) return;
        List<Episode> episodeList = event.getEpisodeList();

        if (episodeList == null || episodeList.size() == 0) return;
        Episode episode = episodeList.get(0);

        if (PodcastPlayer.getInstance().isPlaying()) return;

        setup(episode);
    }
}
