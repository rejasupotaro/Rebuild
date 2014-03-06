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
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EpisodeDescriptionFragment extends RoboFragment {

    private Episode episode;

    @InjectView(R.id.episode_description_text)
    private TextView episodeDescriptionTextView;

    @InjectView(R.id.guest_list)
    private GuestListView guestListView;

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
        guestListView.setup(StringUtils.getGuestNames(episode.getDescription()));
        setupSectionHeaders();
        ViewUtils.setTweetText(episodeDescriptionTextView, episode.getDescription());
    }

    private void setupSectionHeaders() {
        SectionHeaderView sectionHeaderDescription = (SectionHeaderView) getActivity().findViewById(
                R.id.section_header_description);
        sectionHeaderDescription.setup("Description");
    }
}
