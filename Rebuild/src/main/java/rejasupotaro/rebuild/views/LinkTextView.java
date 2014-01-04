package rejasupotaro.rebuild.views;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.utils.IntentUtils;

public class LinkTextView {

    private TextView mView;

    public TextView getView() {
        return mView;
    }

    public LinkTextView(final Context context, String text, final String link) {
        mView = (TextView) View.inflate(context, R.layout.link_text_view, null);
        mView.setText("▶ " + text);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openBrowser(context, link);
            }
        });
    }
}
