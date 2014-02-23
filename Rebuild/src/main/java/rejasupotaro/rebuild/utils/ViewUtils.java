package rejasupotaro.rebuild.utils;

import android.view.View;
import android.widget.ListView;

public final class ViewUtils {

    public static final void addHeaderView(ListView listView, View headerView) {
        if (listView == null || listView.getHeaderViewsCount() != 0 || headerView == null) {
            return;
        }
        listView.addHeaderView(headerView, null, false);
    }

    private ViewUtils() {}
}
