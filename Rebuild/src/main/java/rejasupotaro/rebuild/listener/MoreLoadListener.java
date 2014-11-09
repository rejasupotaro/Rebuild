package rejasupotaro.rebuild.listener;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.utils.ViewUtils;

public abstract class MoreLoadListener implements AbsListView.OnScrollListener {
    private ListView listView;
    private View footerView;
    private boolean isFinish = false;

    public MoreLoadListener(Context context, ListView listView) {
        this(context, listView, View.inflate(context, R.layout.list_footer_progress, null));
    }

    public MoreLoadListener(Context context, ListView listView, View footerView) {
        this.listView = listView;
        this.footerView = footerView;
        ViewUtils.addFooterView(listView, footerView);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        if (isEndOfList() && !isFinish) {
            onLoadMore();
        }
    }

    private boolean isEndOfList() {
        if (listView.getAdapter() == null || listView.getChildCount() == 0) {
            return false;
        }

        int totalItemCount = listView.getAdapter().getCount() - 1;
        int lastItemBottomPosition = listView.getChildAt(listView.getChildCount() - 1).getBottom();
        return (listView.getLastVisiblePosition() == totalItemCount)
                && (lastItemBottomPosition <= listView.getHeight());
    }

    public abstract void onLoadMore();

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public void finish() {
        isFinish = true;

        if (listView.getFooterViewsCount() > 0 || footerView != null) {
            listView.removeFooterView(footerView);
        }
    }
}


