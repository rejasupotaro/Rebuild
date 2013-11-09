package rejasupotaro.rebuild.views;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import rejasupotaro.rebuild.utils.IntentUtils;

public class LinkTextView extends TextView {

    public LinkTextView(Context context) {
        super(context);
    }

    public LinkTextView(final Context context, String text, final String link) {
        super(context);
        setText("▶ " + text);
        setTypeface(null, Typeface.ITALIC);
        setTextColor(context.getResources().getColor(android.R.color.holo_blue_light));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openBrowser(context, link);
            }
        });
    }
}
