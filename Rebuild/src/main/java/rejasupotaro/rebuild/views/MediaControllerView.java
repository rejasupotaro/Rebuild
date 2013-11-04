package rejasupotaro.rebuild.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import rejasupotaro.rebuild.R;

public class MediaControllerView extends LinearLayout {

    public MediaControllerView(Context context) {
        super(context);
        setupView(context);
    }

    public MediaControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    private void setupView(Context context) {
        LayoutParams params =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        View child = inflate(context, R.layout.view_media_controller, null);
        addView(child, params);
    }
}
