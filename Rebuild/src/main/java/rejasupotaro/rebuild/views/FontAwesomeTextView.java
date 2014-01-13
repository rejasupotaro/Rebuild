package rejasupotaro.rebuild.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontAwesomeTextView extends TextView {

    private static final String FONT_FILE_NAME = "fontawesome-webfont.ttf";

    private static Typeface sTypeface;

    public FontAwesomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeface();
    }

    private synchronized void initTypeface() {
        if (sTypeface == null) {
            sTypeface = getTypefaceFromAssets(getContext(), FONT_FILE_NAME);
        }
        setTypeface(sTypeface);
    }

    public void prepend(int unicode) {
        String icon = String.valueOf((char) unicode);
        setText(icon + " " + getText().toString());
    }

    private static Typeface getTypefaceFromAssets(Context context, String path) {
        return Typeface.createFromAsset(context.getAssets(), path);
    }
}
