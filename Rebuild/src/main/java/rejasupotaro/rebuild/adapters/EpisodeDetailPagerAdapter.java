package rejasupotaro.rebuild.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.BuildConfig;
import rejasupotaro.rebuild.fragments.EpisodeDescriptionFragment;
import rejasupotaro.rebuild.fragments.EpisodeTranscriptFragment;
import rejasupotaro.rebuild.fragments.ShowNotesFragment;
import rejasupotaro.rebuild.fragments.TimelineFragment;
import rejasupotaro.rebuild.models.Episode;

public class EpisodeDetailPagerAdapter extends FragmentStatePagerAdapter {

    private PagerFragmentList pagerFragmentList = new PagerFragmentList();

    public EpisodeDetailPagerAdapter(FragmentManager fragmentManager, Episode episode) {
        super(fragmentManager);
        pagerFragmentList.add("DETAILS", EpisodeDescriptionFragment.newInstance(episode));
        pagerFragmentList.add("SHOW NOTES", ShowNotesFragment.newInstance(episode));
        pagerFragmentList.add("TIME LINE", TimelineFragment.newInstance(episode));
        if (BuildConfig.DEBUG) {
            pagerFragmentList.add("TRANSCRIPT", EpisodeTranscriptFragment.newInstance());
        }
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
