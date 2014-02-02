package rejasupotaro.rebuild.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.fragments.EpisodeDetailFragment;
import rejasupotaro.rebuild.models.Episode;
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = false;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }
}
