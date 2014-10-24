package rejasupotaro.rebuild.activities;

import com.squareup.otto.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.EpisodePlayStartEvent;
import rejasupotaro.rebuild.fragments.EpisodeListFragment;
import rejasupotaro.rebuild.media.PodcastPlayer;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.services.PodcastPlayerService;
import rejasupotaro.rebuild.tools.MainThreadExecutor;
import rejasupotaro.rebuild.tools.MenuDelegate;
import rejasupotaro.rebuild.views.MediaBarView;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActionBarActivity
        implements EpisodeListFragment.OnEpisodeSelectListener {

    private static final String EXTRA_EPISODE_ID = "extra_episode_id";

    @InjectView(R.id.media_bar)
    private MediaBarView mediaBar;

    @Inject
    private MenuDelegate menuDelegate;

    @Inject
    private MainThreadExecutor mainThreadExecutor;

    public static Intent createIntent(Context context, String episodeId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_EPISODE_ID, episodeId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BusProvider.getInstance().register(this);
        setupActionBar();
        startServices();
        parseIntent(getIntent());
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        int transparent = getResources().getColor(android.R.color.transparent);
        actionBar.setBackgroundDrawable(new ColorDrawable(transparent));
        actionBar.setIcon(new ColorDrawable(transparent));
    }

    @Override
    public void onResume() {
        super.onResume();
        setupMediaBar(PodcastPlayer.getInstance().getEpisode());
    }

    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    private void parseIntent(Intent intent) {
        if (intent == null) {
            return;
        }

        String episodeId = intent.getStringExtra(EXTRA_EPISODE_ID);
        if (TextUtils.isEmpty(episodeId)) {
            return;
        }

        openEpisodeDetailActivity(episodeId);
    }

    private void startServices() {
        startService(new Intent(this, PodcastPlayerService.class));
    }

    private void setupMediaBar(Episode episode) {
        mediaBar.setEpisode(
                episode,
                new MediaBarView.OnMediaBarClickListener() {
                    @Override
                    public void onClick(Episode episode) {
                        openEpisodeDetailActivity(episode.getEpisodeId());
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onSelect(Episode episode) {
        String episodeId = episode.getEpisodeId();
        if (TextUtils.isEmpty(episodeId)) {
            return;
        }

        openEpisodeDetailActivity(episodeId);
    }

    private void openEpisodeDetailActivity(String episodeId) {
        startActivity(EpisodeDetailActivity.createIntent(this, episodeId));
        overridePendingTransition(R.anim.slide_up_enter, R.anim.zoom_out);
    }

    @Subscribe
    public void onEpisodePlayStart(final EpisodePlayStartEvent event) {
        final Episode episode = Episode.findById(event.getEpisodeId());
        mainThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                setupMediaBar(episode);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                menuDelegate.pressSettings();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
