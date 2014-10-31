package rejasupotaro.rebuild.views;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.ScrollView;

import rx.subjects.PublishSubject;

public class ObservableScrollView extends ScrollView {
    private final PublishSubject<ScrollPosition> scrollEvent = PublishSubject.create();

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PublishSubject<ScrollPosition> getScrollEvent() {
        return scrollEvent;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        scrollEvent.onNext(new ScrollPosition(x, y, oldX, oldY));
    }

    public static class ScrollPosition {

        public Point current = new Point();

        public Point previous = new Point();

        public ScrollPosition(int currentX, int currentY, int previousX, int previousY) {
            current.x = currentX;
            current.y = currentY;
            previous.x = previousX;
            previous.y = previousY;
        }
    }
}
