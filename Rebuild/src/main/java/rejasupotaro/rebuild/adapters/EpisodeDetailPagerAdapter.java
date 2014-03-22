package rejasupotaro.rebuild.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.fragments.CommentsFragment;
import rejasupotaro.rebuild.fragments.EpisodeDescriptionFragment;
import rejasupotaro.rebuild.fragments.ShowNotesFragment;
import rejasupotaro.rebuild.models.Episode;

public class EpisodeDetailPagerAdapter extends FragmentPagerAdapter {

    public static final String FRAGMENT_TITLE_DETAILS = "DETAILS";

    public static final String FRAGMENT_TITLE_SHOW_NOTES = "SHOW NOTES";

    public static final String FRAGMENT_TITLE_COMMENTS = "COMMENTS";

    private PagerFragmentList pagerFragmentList = new PagerFragmentList();

    public EpisodeDetailPagerAdapter(FragmentManager fragmentManager, Episode episode) {
        super(fragmentManager);

        pagerFragmentList.add(FRAGMENT_TITLE_DETAILS,
                EpisodeDescriptionFragment.newInstance(episode));
        pagerFragmentList.add(FRAGMENT_TITLE_SHOW_NOTES,
                ShowNotesFragment.newInstance(episode));
        pagerFragmentList.add(FRAGMENT_TITLE_COMMENTS,
                CommentsFragment.newInstance(episode));
    }

    public void removeByTitle(String title) {
        pagerFragmentList.removeByTitle(title);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return pagerFragmentList.getFragment(position);
    }

    @Override
    public int getCount() {
        return pagerFragmentList.getSize();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagerFragmentList.getTitle(position);
    }

    private static class PagerFragmentList {

        private List<String> titleList = new ArrayList<String>();

        private List<Fragment> fragmentList = new ArrayList<Fragment>();

        public void add(String title, Fragment fragment) {
            titleList.add(title);
            fragmentList.add(fragment);
        }

        public void removeByTitle(String title) {
            if (!titleList.contains(title)) {
                return;
            }

            int index = titleList.indexOf(title);
            titleList.remove(index);
            fragmentList.remove(index);
        }

        public int getSize() {
            return fragmentList.size();
        }

        public String getTitle(int position) {
            return titleList.get(position);
        }

        public Fragment getFragment(int position) {
            return fragmentList.get(position);
        }
    }
}
