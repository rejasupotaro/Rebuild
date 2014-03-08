package rejasupotaro.rebuild.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.utils.IntentUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class AppDescriptionView extends LinearLayout {

    private static final String VERSION_NAME = "0.4.0";

    private TextView copyrightTextView;

    public AppDescriptionView(Context context) {
        super(context);
        setup(context);
    }

    public AppDescriptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public void setup(Context context) {
        View view = View.inflate(getContext(), R.layout.app_description_view, null);
        findViews(view);
        setupViews(context, view);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        addView(view, layoutParams);
    }

    private void findViews(View view) {
        copyrightTextView = (TextView) view.findViewById(R.id.copyright_text);
    }

    private void setupViews(final Context context, View view) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openGitHubRepository(context);
            }
        });

        copyrightTextView.setText(context.getString(R.string.copyright, VERSION_NAME));
    }
}

