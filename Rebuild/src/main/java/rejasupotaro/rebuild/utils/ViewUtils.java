package rejasupotaro.rebuild.utils;

import android.view.View;
import android.widget.ListView;

public final class ViewUtils {

    public static void addHeaderView(ListView listView, View view) {
        if (listView == null || listView.getHeaderViewsCount() != 0 || view == null) {
            return;
        }
        listView.addHeaderView(view, null, false);
    }

    public static void addFooterView(ListView listView, View view) {
        if (listView == null || listView.getFooterViewsCount() != 0 || view == null) {
            return;
        }
        listView.addFooterView(view, null, false);
    }

    public static void disable(View view) {
        view.setEnabled(false);
        view.setClickable(false);
    }

    private ViewUtils() {}
}
