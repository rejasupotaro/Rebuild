package rejasupotaro.rebuild.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.utils.ViewUtils;
import rejasupotaro.rebuild.views.GuestListView;
import rejasupotaro.rebuild.views.SectionHeaderView;
import rejasupotaro.rebuild.views.ShowNotesView;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EpisodeDescriptionFragment extends RoboFragment {

    private Episode episode;

    @InjectView(R.id.section_header_description)
    private SectionHeaderView sectionHeaderDescriptionView;

    @InjectView(R.id.episode_description_text)
    private TextView episodeDescriptionTextView;

    @InjectView(R.id.guest_list)
    private GuestListView guestListView;

    @InjectView(R.id.section_header_show_notes)
    private SectionHeaderView sectionHeaderShowNotesView;

    @InjectView(R.id.show_notes)
    private ShowNotesView showNotesView;

    public static EpisodeDescriptionFragment newInstance(Episode episode) {
        EpisodeDescriptionFragment fragment = new EpisodeDescriptionFragment();
        fragment.setEpisode(episode);
        return fragment;
    }

    public void setEpisode(final Episode episode) {
        this.episode = episode;
    }

    private EpisodeDescriptionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_episode_description, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSectionHeaders();
        ViewUtils.setTweetText(episodeDescriptionTextView, episode.getDescription());
        guestListView.setup(StringUtils.getGuestNames(episode.getDescription()));
        showNotesView.setEpisode(episode);
    }

    private void setupSectionHeaders() {
        sectionHeaderDescriptionView.setText("Description");
        sectionHeaderShowNotesView.setText("Show Notes");
    }
}
