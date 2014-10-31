package rejasupotaro.rebuild.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import rejasupotaro.rebuild.R;

public class SectionHeaderView extends FrameLayout {
    private TextView textView;

    public SectionHeaderView(Context context) {
        super(context);
    }

    public SectionHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setText(String text) {
        View view = View.inflate(getContext(), R.layout.view_section_header, null);
        findViews(view);
        textView.setText(text);
        addView(view);
    }

    private void findViews(View view) {
        textView = (TextView) view.findViewById(R.id.section_header_text);
    }
}
