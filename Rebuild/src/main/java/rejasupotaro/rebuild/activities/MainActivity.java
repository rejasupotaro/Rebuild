package rejasupotaro.rebuild.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.views.SlidingUpPanelLayout;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

public class MainActivity extends RoboFragmentActivity {

    private static final float SLIDING_PANEL_ANCHOR_POINT = 0.3f;

    private static final float SLIDING_PANEL_SLIDE_OFFSET = 0.2f;

    @InjectView(R.id.drag_view)
    private View mDragView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSlidingPanel();
    }

    private void setupSlidingPanel() {
        SlidingUpPanelLayout slidingPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_panel_layout);
        slidingPanelLayout.setShadowDrawable(getResources().getDrawable(R.drawable.above_shadow));
        slidingPanelLayout.setAnchorPoint(SLIDING_PANEL_ANCHOR_POINT);
        slidingPanelLayout.setPanelHeight(getResources().getDimensionPixelSize(R.dimen.sliding_panel_height));
        slidingPanelLayout.setPanelSlideListener(mPanelSlideListener);
        slidingPanelLayout.setDragView(mDragView);
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

}
