package rejasupotaro.rebuild.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.utils.StringUtils;
import rejasupotaro.rebuild.utils.ViewUtils;
import rejasupotaro.rebuild.views.GuestListView;
import rejasupotaro.rebuild.views.SectionHeaderView;
import rejasupotaro.rebuild.views.ShowNoteListView;

public class EpisodeDetailFragment extends Fragment {
    @InjectView(R.id.section_header_description)
    SectionHeaderView sectionHeaderDescriptionView;
    @InjectView(R.id.episode_description_text)
    TextView episodeDescriptionTextView;
    @InjectView(R.id.guest_list)
    GuestListView guestListView;
    @InjectView(R.id.section_header_show_notes)
    SectionHeaderView sectionHeaderShowNotesView;
    @InjectView(R.id.show_notes)
    ShowNoteListView showNoteListView;

    private Episode episode;

    public void setup(final Episode episode) {
        this.episode = episode;
        showNoteListView.setEpisode(episode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episode_detail, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSectionHeaders();
        ViewUtils.setTweetText(episodeDescriptionTextView, episode.getDescription());
        guestListView.setup(StringUtils.getGuestNamesFromTitle(episode.getTitle()));
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    private void setupSectionHeaders() {
        sectionHeaderDescriptionView.setText("Description");
        sectionHeaderShowNotesView.setText("Show Notes");
    }
}
