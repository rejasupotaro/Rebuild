package rejasupotaro.rebuild.views;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.dialogs.ShareEpisodeDialog;
import rejasupotaro.rebuild.models.Episode;

public class SlidingUpPanelDragView extends RelativeLayout {

    private static final String TAG = SlidingUpPanelDragView.class.getSimpleName();

    private TextView mEpisodeTextView;

    private View mPostButton;

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
        mPostButton = view.findViewById(R.id.sliding_up_panel_drag_view_comment_button);
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
                        String message = buildPostMessage(episode);
                        ShareEpisodeDialog dialog = ShareEpisodeDialog.newInstance(message);
                        FragmentActivity activity = (FragmentActivity) getContext();
                        dialog.show(activity.getSupportFragmentManager(), TAG);
                    }
                });
    }

    private String buildPostMessage(Episode episode) {
        return " / " + episode.getTitle() + " " + episode.getLink() + " #rebuildfm";
    }
}
