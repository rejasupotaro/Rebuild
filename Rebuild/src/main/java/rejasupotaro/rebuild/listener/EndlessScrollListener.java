package rejasupotaro.rebuild.listener;

import android.widget.AbsListView;
import android.widget.ListView;

public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {

    private ListView listView;

    public EndlessScrollListener(ListView listView) {
        this.listView = listView;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        if (isEndOfList()) {
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
}


