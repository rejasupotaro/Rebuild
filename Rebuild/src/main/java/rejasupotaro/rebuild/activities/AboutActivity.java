package rejasupotaro.rebuild.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.data.adapters.AboutItemListAdapter;
import rejasupotaro.rebuild.data.models.AboutItem;
import rejasupotaro.rebuild.data.models.Developer;
import rejasupotaro.rebuild.utils.ViewUtils;
import rejasupotaro.rebuild.views.AppDescriptionView;

public class AboutActivity extends ActionBarActivity {
    @InjectView(R.id.about_item_list)
    ListView aboutItemListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.inject(this);
        setupActionBar();
        setupAboutListItemView();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
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
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
