package rejasupotaro.rebuild.fragments;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import rejasupotaro.rebuild.events.LoadEpisodeListCompleteEvent;
import rejasupotaro.rebuild.events.PodcastPlayButtonClickEvent;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.models.Link;
import rejasupotaro.rebuild.services.EpisodeDownloadService;
import rejasupotaro.rebuild.utils.DateUtils;
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.utils.UiAnimations;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EpisodeDetailFragment extends RoboFragment {

    public static final String TAG = EpisodeDetailFragment.class.getSimpleName();

    @Inject
    private EpisodeDownloadClient mEpisodeDownloadClient;

    private TextView mEpisodeTitleTextView;

    private View mMediaStartButtonOnImageCover;

    private TextView mMediaCurrentTimeTextView;

    private TextView mMediaDurationTextView;

    private CheckBox mMediaStartAndPauseButton;

    private SeekBar mSeekBar;

    private TextView mEpisodeDescriptionTextView;

    @InjectView(R.id.show_note_list)
    private ListView mShowNoteListView;

    private Episode mEpisode;

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

        setupMediaPlayAndPauseButton(episode);
        setupSeekBar(episode);

        setTitle(episode);
        mEpisodeDescriptionTextView.setText(
                Html.fromHtml(StringUtils.buildTwitterLinkText(episode.getDescription())));
    }

    private void setupListView(Episode episode, View headerView) {
        mShowNoteListView.addHeaderView(headerView, null, false);
        List<Link> linkList = Link.Parser.toLinkList(episode.getShowNotes());
        final ShowNoteListAdapter adapter = new ShowNoteListAdapter(getActivity(), linkList);
        mShowNoteListView.setAdapter(adapter);
        mShowNoteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Link link = adapter.getItem(position);
                IntentUtils.openBrowser(getActivity(), link.getUrl());
            }
        });
    }

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
                if (!PodcastPlayer.getInstance().isPlaying()) return;
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

    private void setupMediaPlayAndPauseButton(final Episode episode) {
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

        if (PodcastPlayer.getInstance().isPlayingEpisode(episode)) {
            mMediaStartButtonOnImageCover.setVisibility(View.GONE);
        } else {
            mMediaStartButtonOnImageCover.setVisibility(View.VISIBLE);
            mMediaStartButtonOnImageCover.setAlpha(1);
        }

    }

    private void start(Episode episode) {
        final PodcastPlayer podcastPlayer = PodcastPlayer.getInstance();
        if (shouldRestart(episode)) {
            podcastPlayer.start();
            mSeekBar.setEnabled(true);
        } else {
            podcastPlayer.start(getActivity(), episode, new PodcastPlayer.StateChangedListener() {
                @Override
                public void onStart() {
                    mSeekBar.setEnabled(true);
                }
            });

            BusProvider.getInstance().post(new PodcastPlayButtonClickEvent(episode));
            UiAnimations.fadeOut(mMediaStartButtonOnImageCover, 300, 1000);
            if (!mEpisode.hasMediaDataInLocal()) {
                getActivity().startService(
                        EpisodeDownloadService.createIntent(getActivity(), episode));
            }
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
