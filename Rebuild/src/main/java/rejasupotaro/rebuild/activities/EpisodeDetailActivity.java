package rejasupotaro.rebuild.activities;

import com.squareup.otto.Subscribe;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.DownloadEpisodeCompleteEvent;
import rejasupotaro.rebuild.fragments.EpisodeDetailFragment;
import rejasupotaro.rebuild.fragments.EpisodeMediaFragment;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.tools.MainThreadExecutor;
import rejasupotaro.rebuild.tools.MenuDelegate;
import roboguice.inject.InjectExtra;

public class EpisodeDetailActivity extends RoboActionBarActivity {

    private static final String EXTRA_EPISODE_ID = "extra_episode_id";

    @InjectExtra(value = EXTRA_EPISODE_ID)
    private int episodeId;

    private Episode episode;

    private EpisodeMediaFragment episodeMediaFragment;

    private EpisodeDetailFragment episodeDetailFragment;

    @Inject
    private MenuDelegate menuDelegate;

    @Inject
    private MainThreadExecutor mainThreadExecutor;

    public static Intent createIntent(Context context, int episodeId) {
        Intent intent = new Intent(context, EpisodeDetailActivity.class);
        intent.putExtra(EXTRA_EPISODE_ID, episodeId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);

        BusProvider.getInstance().register(this);

        episode = Episode.findById(episodeId);
        setupActionBar(episode);

        episodeMediaFragment = (EpisodeMediaFragment) getSupportFragmentManager().findFragmentById(
                R.id.fragment_episode_media);
        episodeMediaFragment.setup(episode);

        episodeDetailFragment = (EpisodeDetailFragment) getSupportFragmentManager()
                .findFragmentById(
                        R.id.fragment_episode_detail);
        episodeDetailFragment.setup(episode);
    }

    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    private void setupActionBar(Episode episode) {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        String originalTitle = episode.getTitle();
        int startIndex = originalTitle.indexOf(':');
        actionBar.setTitle("Episode " + originalTitle.substring(0, startIndex));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.episode_detail, menu);
        return true;
    }

    public void close() {
        finish();
        overridePendingTransition(R.anim.zoom_in, R.anim.slide_down_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                close();
                return true;
            case R.id.action_settings:
                menuDelegate.pressSettings();
                return true;
            case R.id.action_share:
                menuDelegate.pressShare(episode);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onEpisodeDownloadComplete(final DownloadEpisodeCompleteEvent event) {
        final Episode episode = Episode.findById(event.getEpisodeId());
        if (!this.episode.isSameEpisode(episode)) {
            return;
        }

        this.episode = episode;

        mainThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                episodeMediaFragment.setup(EpisodeDetailActivity.this.episode);
            }
        });
    }
}
