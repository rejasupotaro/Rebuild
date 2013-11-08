package rejasupotaro.rebuild.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

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
                Uri uri = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
    }
}
