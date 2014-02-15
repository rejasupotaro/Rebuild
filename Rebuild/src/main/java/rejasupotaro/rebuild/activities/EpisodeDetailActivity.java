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

public class EpisodeDetailActivity extends RoboActionBarActivity{

    private static final String EXTRA_EPISODE = "extra_episode";

    @InjectExtra(value = EXTRA_EPISODE)
    private Episode mEpisode;

    public static Intent createIntent(Context context, Episode episode) {
        Intent intent = new Intent(context, EpisodeDetailActivity.class);
        intent.putExtra(EXTRA_EPISODE, episode);
        return intent;
    }

    @Inject
    private MenuDelegate mMenuDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);

        setupActionBar();

        EpisodeDetailFragment episodeDetailFragment =
                (EpisodeDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_episode_detail);
        episodeDetailFragment.setup(mEpisode);
    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.episode_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle params = new Bundle();
        params.putParcelable(MenuDelegate.PARAM_EPISODE, mEpisode);
        return mMenuDelegate.onItemSelect(item, params);
    }
}
