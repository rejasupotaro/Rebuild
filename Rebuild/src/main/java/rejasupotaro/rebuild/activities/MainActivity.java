package rejasupotaro.rebuild.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.fragments.EpisodeListFragment;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.services.PodcastPlayerService;

public class MainActivity extends RoboActionBarActivity
        implements EpisodeListFragment.OnEpisodeSelectListener {

    private static final String EXTRA_EPISODE = "extra_episode";

    public static Intent createIntent(Context context, Episode episode) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_EPISODE, episode);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startServices();
        parseIntent(getIntent());
    }

    private void parseIntent(Intent intent) {
        if (intent == null) {
            return;
        }

        Episode episode = intent.getParcelableExtra(EXTRA_EPISODE);
        if (episode == null) {
            return;
        }

        getActionBar().hide();
        openEpisodeDetailFragment(episode);
    }

    private void startServices() {
        startService(new Intent(this, PodcastPlayerService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onSelect(Episode episode) {
        openEpisodeDetailFragment(episode);
    }

    private void openEpisodeDetailFragment(Episode episode) {
        startActivity(EpisodeDetailActivity.createIntent(this, episode));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(SettingsActivity.createIntent(this));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
