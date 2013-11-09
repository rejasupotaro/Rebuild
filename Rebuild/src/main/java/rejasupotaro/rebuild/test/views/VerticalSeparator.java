package rejasupotaro.rebuild.test.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import rejasupotaro.rebuild.R;

public class VerticalSeparator extends View {

    public VerticalSeparator(Context context) {
        super(context);
    }

    public VerticalSeparator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(R.color.dark_gray);
    }
}
