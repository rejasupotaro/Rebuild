package rejasupotaro.rebuild.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.adapters.ShowNoteListAdapter;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.models.Link;
import rejasupotaro.rebuild.tools.OnContextExecutor;
import rejasupotaro.rebuild.utils.IntentUtils;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class ShowNotesFragment extends RoboFragment {

    private Episode episode;

    @InjectView(R.id.show_note_list)
    private ListView showNoteListView;

    @Inject
    private OnContextExecutor onContextExecutor;

    public static ShowNotesFragment newInstance(Episode episode) {
        ShowNotesFragment fragment = new ShowNotesFragment();
        fragment.setEpisode(episode);
        return fragment;
    }

    private ShowNotesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_notes, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupListView(episode);
    }

    public void setEpisode(final Episode episode) {
        this.episode = episode;
    }

    private void setupListView(Episode episode) {
        List<Link> linkList = Link.Parser.toLinkList(episode.getShowNotes());
        final ShowNoteListAdapter adapter = new ShowNoteListAdapter(
                getActivity(), linkList, itemClickListener);
        showNoteListView.setAdapter(adapter);
    }

    private ShowNoteListAdapter.ItemClickListener itemClickListener
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
}
