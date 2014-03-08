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
import rejasupotaro.rebuild.views.AppDescriptionView;
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
        ViewUtils.addHeaderView(aboutItemListView, new AppDescriptionView(this));

        List<AboutItem> aboutItemList = new ArrayList<AboutItem>();
        aboutItemList.add(new AboutItem.AboutItemHeader(
                getString(R.string.about_item_header_developer)));
        aboutItemList.add(new AboutItem.AboutItemContent(Developer.REJASUPOTARO));

        aboutItemList.add(new AboutItem.AboutItemHeader(
                getString(R.string.about_item_header_contributors)));
        aboutItemList.add(new AboutItem.AboutItemContent(Developer.HOTCHEMI));
        aboutItemList.add(new AboutItem.AboutItemContent(Developer.MOOTOH));
        aboutItemList.add(new AboutItem.AboutItemContent(Developer.HAK));

        AboutItemListAdapter adapter = new AboutItemListAdapter(this, aboutItemList);
        aboutItemListView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
            case android.R.id.home:
                menuDelegate.pressHome();
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }
}
