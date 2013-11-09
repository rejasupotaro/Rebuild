package rejasupotaro.rebuild.test.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.IntentUtils;

public class SlidingUpPanelDragView extends RelativeLayout {

    private TextView mEpisodeTextView;

    private Button mPostButton;

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
        mPostButton = (Button) view.findViewById(R.id.sliding_up_panel_drag_view_comment_button);
        addView(view, params);
    }

    public View getDragView() {
        return mEpisodeTextView;
    }

    public void setEpisode(final Episode episode) {
        if (episode == null) return;

        mEpisodeTextView.setText(episode.getTitle());
        mPostButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtils.sendPostIntent(getContext(), buildPostMessage(episode));
                    }
                });
    }

    private String buildPostMessage(Episode episode) {
        return " / " + episode.getTitle() + " " + episode.getLink();
    }
}
