package rejasupotaro.rebuild.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Episode;

public class SlidingUpPanelDragView extends RelativeLayout {

    private TextView mEpisodeTextView;

    public SlidingUpPanelDragView(Context context) {
        super(context);
        setupView(context);
    }

    public SlidingUpPanelDragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    private void setupView(Context context) {
        LayoutParams params =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        View view = inflate(context, R.layout.view_sliding_up_panel_drag, null);

        mEpisodeTextView = (TextView) view.findViewById(R.id.sliding_up_panel_drag_view_episode_title);
        view.findViewById(R.id.sliding_up_panel_drag_view_comment_button).setOnClickListener(
                new OnClickListener() {
            @Override
            public void onClick(View v) {
                // impl me
            }
        });

        addView(view, params);
    }

    public void setEpisode(Episode episode) {
        if (episode == null) return;
        mEpisodeTextView.setText(episode.getTitle());
    }
}
