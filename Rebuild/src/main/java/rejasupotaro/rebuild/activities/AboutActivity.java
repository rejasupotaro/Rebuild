package rejasupotaro.rebuild.activities;

import android.app.ActionBar;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.adapters.AboutItemListAdapter;
import rejasupotaro.rebuild.listener.NotificationEpisodesCheckBoxChangeListener;
import rejasupotaro.rebuild.models.AboutItem;
import rejasupotaro.rebuild.tools.MenuDelegate;
import rejasupotaro.rebuild.utils.PreferenceUtils;
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
        aboutItemList.add(new AboutItem.AboutItemContent(
                "https://pbs.twimg.com/profile_images/424554842367852544/jRoDtV1R.jpeg",
                "rejasupotaro",
                "http://twitter.com/rejasupotaro"));
        aboutItemList.add(new AboutItem.AboutItemHeader(
                getString(R.string.about_item_header_contributors)));
        aboutItemList.add(new AboutItem.AboutItemContent(
                "https://pbs.twimg.com/profile_images/424955294616023040/aco9m_GJ.png",
                "hotchemi",
                "http://twitter.com/hotchemi"));
        aboutItemList.add(new AboutItem.AboutItemHeader(
                getString(R.string.about_item_header_recent_changes)));

        AboutItemListAdapter adapter = new AboutItemListAdapter(this, aboutItemList);
        aboutItemListView.setAdapter(adapter);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT >= 19) {
            //In SDK4.4~, it has translucent navigation bar and status bar
            aboutItemListView.setPadding(0, getStatusbarHeight() + getActionbarHeight(), 0, 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuDelegate.onItemSelect(item);
    }

    private int getStatusbarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private int getActionbarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }
}
