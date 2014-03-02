package rejasupotaro.rebuild.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.adapters.AboutItemListAdapter;
import rejasupotaro.rebuild.models.AboutItem;
import rejasupotaro.rebuild.models.Developer;
import rejasupotaro.rebuild.tools.MenuDelegate;
import rejasupotaro.rebuild.utils.ViewUtils;
import rejasupotaro.rebuild.views.RecentChangesView;
import roboguice.inject.InjectView;

public class AboutActivity extends RoboActionBarActivity {

    @InjectView(R.id.about_item_list)
    private ListView aboutItemListView;

    @Inject
    private MenuDelegate menuDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setupActionBar();
        setupAboutListItemView();
    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void setupAboutListItemView() {
        ViewUtils.addFooterView(aboutItemListView, new RecentChangesView(this));

        List<AboutItem> aboutItemList = new ArrayList<AboutItem>();
        aboutItemList.add(new AboutItem.AboutItemHeader(
                getString(R.string.about_item_header_about)));
        aboutItemList.add(new AboutItem.AboutItemContent(
                "",
                "",
                "https://github.com/rejasupotaro/Rebuild"));

        aboutItemList.add(new AboutItem.AboutItemHeader(
                getString(R.string.about_item_header_developer)));
        aboutItemList.add(new AboutItem.AboutItemContent(Developer.REJASUPOTARO));

        aboutItemList.add(new AboutItem.AboutItemHeader(
                getString(R.string.about_item_header_contributors)));
        aboutItemList.add(new AboutItem.AboutItemContent(Developer.HOTCHEMI));
        aboutItemList.add(new AboutItem.AboutItemContent(Developer.MOOTOH));
        aboutItemList.add(new AboutItem.AboutItemHeader(
                getString(R.string.about_item_header_recent_changes)));

        AboutItemListAdapter adapter = new AboutItemListAdapter(this, aboutItemList);
        aboutItemListView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuDelegate.onItemSelect(item);
    }
}
