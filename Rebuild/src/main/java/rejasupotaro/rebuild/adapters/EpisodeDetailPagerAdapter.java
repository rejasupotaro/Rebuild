package rejasupotaro.rebuild.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.fragments.EpisodeDescriptionFragment;
import rejasupotaro.rebuild.fragments.ShowNotesFragment;
import rejasupotaro.rebuild.models.Episode;

public class EpisodeDetailPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> titleList = new ArrayList<String>();

    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    public EpisodeDetailPagerAdapter(FragmentManager fragmentManager, Episode episode) {
        super(fragmentManager);
        titleList.add("DETAILS");
        fragmentList.add(EpisodeDescriptionFragment.newInstance(episode));

        titleList.add("SHOW NOTES");
        fragmentList.add(ShowNotesFragment.newInstance(episode));
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
