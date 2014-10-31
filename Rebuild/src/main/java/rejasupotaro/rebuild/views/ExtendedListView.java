package rejasupotaro.rebuild.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import rejasupotaro.rebuild.R;

public class ExtendedListView extends ListView implements OnScrollListener {
    public static interface OnPositionChangedListener {
        public void onPositionChanged(ExtendedListView listView, int position, View scrollBarPanel);
    }

    private OnScrollListener onScrollListener = null;
    private View scrollBarPanel = null;
    private int scrollBarPanelPosition = 0;
    private OnPositionChangedListener positionChangedListener;
    private int lastPosition = -1;
    private Animation inAnimation = null;
    private Animation outAnimation = null;
    private final Handler handler = new Handler();
    private int widthMeasureSpec;
    private int heightMeasureSpec;

    private final Runnable scrollBarPanelFadeRunnable = new Runnable() {

        @Override
        public void run() {
            if (outAnimation != null) {
                scrollBarPanel.startAnimation(outAnimation);
            }
        }
    };

    public ExtendedListView(Context context) {
        this(context, null);
    }

    public ExtendedListView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.listViewStyle);
    }

    public ExtendedListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        super.setOnScrollListener(this);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendedListView);
        final int scrollBarPanelLayoutId = a
                .getResourceId(R.styleable.ExtendedListView_scrollBarPanel, -1);
        final int scrollBarPanelInAnimation = a
                .getResourceId(R.styleable.ExtendedListView_scrollBarPanelInAnimation,
                        R.anim.scroll_bar_panel_in);
        final int scrollBarPanelOutAnimation = a
                .getResourceId(R.styleable.ExtendedListView_scrollBarPanelOutAnimation,
                        R.anim.scroll_bar_panel_out);
        a.recycle();

        if (scrollBarPanelLayoutId != -1) {
            setScrollBarPanel(scrollBarPanelLayoutId);
        }

        final int scrollBarPanelFadeDuration = ViewConfiguration.getScrollBarFadeDuration();

        if (scrollBarPanelInAnimation > 0) {
            inAnimation = AnimationUtils.loadAnimation(getContext(), scrollBarPanelInAnimation);
        }

        if (scrollBarPanelOutAnimation > 0) {
            outAnimation = AnimationUtils.loadAnimation(getContext(), scrollBarPanelOutAnimation);
            outAnimation.setDuration(scrollBarPanelFadeDuration);

            outAnimation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (scrollBarPanel != null) {
                        scrollBarPanel.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (onScrollListener != null) {
            onScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
        if (null != positionChangedListener && null != scrollBarPanel) {

            // Don't do anything if there is no itemviews
            if (totalItemCount > 0) {
                /*
                 * from android source code (ScrollBarDrawable.java)
                 */
                final int thickness = getVerticalScrollbarWidth();
                int height = Math.round((float) getMeasuredHeight() * computeVerticalScrollExtent()
                        / computeVerticalScrollRange());
                int thumbOffset = Math.round((float) (getMeasuredHeight() - height)
                        * computeVerticalScrollOffset() / (computeVerticalScrollRange()
                        - computeVerticalScrollExtent()));
                final int minLength = thickness * 2;
                if (height < minLength) {
                    height = minLength;
                }
                thumbOffset += height / 2;

                /*
                 * find out which itemviews the center of thumb is on
                 */
                final int count = getChildCount() - getFooterViewsCount();
                for (int i = 0; i < count; ++i) {
                    final View childView = getChildAt(i);
                    if (childView != null) {
                        if (thumbOffset > childView.getTop() && thumbOffset < childView
                                .getBottom()) {
                            if (lastPosition != firstVisibleItem + i) {
                                lastPosition = firstVisibleItem + i;

                                positionChangedListener
                                        .onPositionChanged(this, lastPosition, scrollBarPanel);

                                measureChild(scrollBarPanel, widthMeasureSpec,
                                        heightMeasureSpec);
                            }
                            break;
                        }
                    }
                }

                scrollBarPanelPosition = thumbOffset - scrollBarPanel.getMeasuredHeight() / 2;
                final int x = getMeasuredWidth() - scrollBarPanel.getMeasuredWidth()
                        - getVerticalScrollbarWidth();
                scrollBarPanel
                        .layout(x, scrollBarPanelPosition, x + scrollBarPanel.getMeasuredWidth(),
                                scrollBarPanelPosition + scrollBarPanel.getMeasuredHeight());
            }
        }

        if (onScrollListener != null) {
            onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public void setOnPositionChangedListener(OnPositionChangedListener onPositionChangedListener) {
        positionChangedListener = onPositionChangedListener;
    }

    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void setScrollBarPanel(View scrollBarPanel) {
        this.scrollBarPanel = scrollBarPanel;
        this.scrollBarPanel.setVisibility(View.GONE);
        requestLayout();
    }

    public void setScrollBarPanel(int resId) {
        setScrollBarPanel(LayoutInflater.from(getContext()).inflate(resId, this, false));
    }

    public View getScrollBarPanel() {
        return scrollBarPanel;
    }

    @Override
    protected boolean awakenScrollBars(int startDelay, boolean invalidate) {
        final boolean isAnimationPlayed = super.awakenScrollBars(startDelay, invalidate);

        if (isAnimationPlayed == true && scrollBarPanel != null) {
            if (scrollBarPanel.getVisibility() == View.GONE) {
                scrollBarPanel.setVisibility(View.VISIBLE);
                if (inAnimation != null) {
                    scrollBarPanel.startAnimation(inAnimation);
                }
            }

            handler.removeCallbacks(scrollBarPanelFadeRunnable);
            handler.postAtTime(scrollBarPanelFadeRunnable,
                    AnimationUtils.currentAnimationTimeMillis() + startDelay);
        }

        return isAnimationPlayed;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (scrollBarPanel != null && getAdapter() != null) {
            this.widthMeasureSpec = widthMeasureSpec;
            this.heightMeasureSpec = heightMeasureSpec;
            measureChild(scrollBarPanel, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (scrollBarPanel != null) {
            final int x = getMeasuredWidth() - scrollBarPanel.getMeasuredWidth()
                    - getVerticalScrollbarWidth();
            scrollBarPanel
                    .layout(x, scrollBarPanelPosition, x + scrollBarPanel.getMeasuredWidth(),
                            scrollBarPanelPosition + scrollBarPanel.getMeasuredHeight());
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (scrollBarPanel != null && scrollBarPanel.getVisibility() == View.VISIBLE) {
            drawChild(canvas, scrollBarPanel, getDrawingTime());
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        handler.removeCallbacks(scrollBarPanelFadeRunnable);
    }
}
