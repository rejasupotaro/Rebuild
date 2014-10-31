package rejasupotaro.rebuild.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import rejasupotaro.rebuild.R;

public class VerticalSeparator extends View {
    public VerticalSeparator(Context context) {
        super(context);
        setup();
    }

    public VerticalSeparator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        setBackgroundResource(R.color.light_gray);
        setLayoutParams(new ViewGroup.LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
