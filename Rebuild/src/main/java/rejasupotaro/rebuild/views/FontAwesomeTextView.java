package rejasupotaro.rebuild.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontAwesomeTextView extends TextView {

    private static final String FONT_FILE_NAME = "fontawesome-webfont.ttf";

    // See {@linktourl http://fortawesome.github.io/Font-Awesome/icons/}
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

    public void prepend(Icon icon) {
        prepend(icon.getUnicode());
    }

    public void prepend(int unicode) {
        String icon = String.valueOf((char) unicode);
        setText(icon + " " + getText().toString());
    }

    private static Typeface getTypefaceFromAssets(Context context, String path) {
        return Typeface.createFromAsset(context.getAssets(), path);
    }

    public static enum Icon {
        HOME(0xF015),
        TWITTER(0xF099),
        DOWNLOAD(0xF019),
        TRASH(0xF014),
        MINUS(0xF146),
        SPINNER(0xF110),
        SETTINGS(0xF013),
        SHARE(0xF045);

        private int unicode;

        public int getUnicode() {
            return unicode;
        }

        private Icon(int unicode) {
            this.unicode = unicode;
        }
    }
}
