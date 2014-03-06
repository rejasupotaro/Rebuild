package rejasupotaro.rebuild.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.fragments.EpisodeListFragment;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.services.PodcastPlayerService;
import rejasupotaro.rebuild.tools.MenuDelegate;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActionBarActivity
        implements EpisodeListFragment.OnEpisodeSelectListener {

    private static final String EXTRA_EPISODE = "extra_episode";

    @Inject
    private MenuDelegate menuDelegate;

    @InjectView(R.id.media_bar)
    private View mediaBar;

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

        mediaBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: implement later...
                openEpisodeDetailFragment(34);
            }
        });
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
        openEpisodeDetailFragment(episode.getEpisodeId());
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
        openEpisodeDetailFragment(episode.getEpisodeId());
    }

    private void openEpisodeDetailFragment(int episodeId) {
        startActivity(EpisodeDetailActivity.createIntent(this, episodeId));
        overridePendingTransition(R.anim.slide_up_enter, R.anim.zoom_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
            case android.R.id.home: {
                menuDelegate.pressHome();
                break;
            }
            case R.id.action_settings: {
                menuDelegate.pressSettings();
                break;
            }
            default: {
                result = super.onOptionsItemSelected(item);
                break;
            }
        }
        return result;
    }
}
