package rejasupotaro.rebuild.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.fragments.EpisodeDetailFragment;
import rejasupotaro.rebuild.fragments.EpisodeListFragment;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.services.PodcastPlayerService;
import rejasupotaro.rebuild.views.SlidingUpPanelLayout;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

public class MainActivity extends RoboFragmentActivity implements EpisodeListFragment.OnEpisodeSelectListener {

    private static final float SLIDING_PANEL_SLIDE_OFFSET = 0.2f;

    @InjectView(R.id.sliding_up_panel_drag_view)
    private View mDragView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSlidingPanel();
        setupPodcastPlayerService();
    }

    private void setupSlidingPanel() {
        SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_up_panel_layout);
        slidingUpPanelLayout.setShadowDrawable(getResources().getDrawable(R.drawable.above_shadow));
        slidingUpPanelLayout.setPanelHeight(getResources().getDimensionPixelSize(R.dimen.sliding_up_panel_height));
        slidingUpPanelLayout.setPanelSlideListener(mPanelSlideListener);
        slidingUpPanelLayout.setDragView(mDragView);
    }

    private void setupPodcastPlayerService() {
        startService(new Intent(this, PodcastPlayerService.class));
    }

    private SlidingUpPanelLayout.PanelSlideListener mPanelSlideListener = new SlidingUpPanelLayout.PanelSlideListener() {

        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            if (slideOffset < SLIDING_PANEL_SLIDE_OFFSET) {
                if (getActionBar().isShowing()) {
                    getActionBar().hide();
                }
            } else {
                if (!getActionBar().isShowing()) {
                    getActionBar().show();
                }
            }
        }

        @Override
        public void onPanelExpanded(View panel) {
        }

        @Override
        public void onPanelCollapsed(View panel) {
        }

        @Override
        public void onPanelAnchored(View panel) {
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onSelect(Episode episode) {
        SlidingUpPanelLayout slidingUpPanelLayout =
                (SlidingUpPanelLayout) findViewById(R.id.sliding_up_panel_layout);
        slidingUpPanelLayout.expandPane();

        FragmentManager fragmentManager = getSupportFragmentManager();

        EpisodeDetailFragment episodeDetailFragment =
                (EpisodeDetailFragment) fragmentManager.findFragmentById(R.id.fragment_episode_detail);
        episodeDetailFragment.setup(episode);
    }

    @Override
    public void onBackPressed() {
        SlidingUpPanelLayout slidingUpPanelLayout =
                (SlidingUpPanelLayout) findViewById(R.id.sliding_up_panel_layout);
        if (slidingUpPanelLayout.isExpanded()) {
            slidingUpPanelLayout.collapsePane();
        } else {
            super.onBackPressed();
        }
    }
}
