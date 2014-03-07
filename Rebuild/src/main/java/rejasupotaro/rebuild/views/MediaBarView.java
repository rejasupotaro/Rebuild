package rejasupotaro.rebuild.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Episode;

public class MediaBarView extends FrameLayout {

    private View rootView;

    public MediaBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public void setup() {
        rootView = View.inflate(getContext(), R.layout.media_bar_view, null);
        addView(rootView);
    }

    public void show(Episode episode) {
        rootView.setVisibility(VISIBLE);
    }

    public void hide() {
        rootView.setVisibility(GONE);
    }
}
