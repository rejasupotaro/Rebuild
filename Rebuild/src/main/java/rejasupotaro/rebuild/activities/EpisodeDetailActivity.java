package rejasupotaro.rebuild.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.fragments.EpisodeDetailFragment;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.tools.MenuDelegate;
import roboguice.inject.InjectExtra;

public class EpisodeDetailActivity extends RoboActionBarActivity {

    private static final String EXTRA_EPISODE_ID = "extra_episode_id";

    @InjectExtra(value = EXTRA_EPISODE_ID)
    private int episodeId;

    @Inject
    private MenuDelegate menuDelegate;

    public static Intent createIntent(Context context, int episodeId) {
        Intent intent = new Intent(context, EpisodeDetailActivity.class);
        intent.putExtra(EXTRA_EPISODE_ID, episodeId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);

        setupActionBar();

        EpisodeDetailFragment episodeDetailFragment =
                (EpisodeDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_episode_detail);
        episodeDetailFragment.setup(Episode.findById(episodeId));
    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuDelegate.onItemSelect(item);
    }
}
