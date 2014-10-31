package rejasupotaro.rebuild.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import rejasupotaro.rebuild.R;

public class HorizontalSeparator extends View {
    public HorizontalSeparator(Context context) {
        super(context);
        setup();
    }

    public HorizontalSeparator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        setBackgroundResource(R.color.light_gray);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
    }
}

