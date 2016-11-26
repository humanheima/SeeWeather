package com.humanheima.customviewdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/11/18.
 */
public class ScrollerLayout extends ViewGroup {

    private Scroller scroller;

    //判定为滑动的最小移动像素
    private int mTouchSlop;
    /**
     * 手机按下时的屏幕坐标
     */
    private float mXDown;

    /**
     * 手机当时所处的屏幕坐标
     */
    private float mXMove;

    /**
     * 上次触发ACTION_MOVE事件时的屏幕坐标
     */
    private float mXLastMove;

    /**
     * 界面可滚动的左边界
     */
    private int leftBorder;

    /**
     * 界面可滚动的右边界
     */
    private int rightBorder;
    //检测move事件的速率
    private VelocityTracker mVelocityTracker;
    private int targetIndex;

    public ScrollerLayout(Context context) {
        this(context, null);
    }

    public ScrollerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledPagingTouchSlop();
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //测量子view
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                // 为ScrollerLayout中的每一个子控件在水平方向上进行布局
                childView.layout(i * childView.getMeasuredWidth(), 0, (i + 1) * childView.getMeasuredWidth(), childView.getMeasuredHeight());
                // 初始化左右边界值
                leftBorder = getChildAt(0).getLeft();
                rightBorder = getChildAt(getChildCount() - 1).getRight();
            }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXDown = ev.getRawX();
                mXLastMove = mXDown;
                break;
            case MotionEvent.ACTION_MOVE:

                mXMove = ev.getRawX();
                float diff = Math.abs(mXMove - mXDown);
                mXLastMove = mXMove;
                // 当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                if (diff > mTouchSlop) {
                    return true;
                }
                //Log.e("ScrollerLayout", "onInterceptTouchEvent ACTION_MOVE mXLastMove=" + mXLastMove);
                break;
        }
        return super.onInterceptTouchEvent(ev);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mXMove = event.getRawX();
                int scrolledX = (int) (mXLastMove - mXMove);
                // Log.e("ScrollerLayout", "onTouchEvent getScrollX()=" + getScrollX() + ",getWidth()=" + getWidth());
                //Log.e("ScrollerLayout", "onTouchEvent scrolledX" + scrolledX);
                if (getScrollX() + scrolledX <= leftBorder) {
                    scrollTo(leftBorder, 0);
                    break;
                } else if (getScrollX() + getWidth() + scrolledX >= rightBorder) {
                    //getWidth()返回的只是可见部分的宽度
                    scrollTo(rightBorder - getWidth(), 0);
                    break;
                }
                scrollBy(scrolledX, 0);
                mXLastMove = mXMove;
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();

                if (Math.abs(xVelocity) >= 150) {
                    targetIndex = xVelocity > 0 ? targetIndex - 1 : targetIndex + 1;
                    targetIndex = Math.max(0, Math.min(targetIndex, getChildCount() - 1));
                } else {
                    //如果水平速度滑动的很小，就判断是否划过view的一半宽度
                    targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                }
                int dx = targetIndex * getWidth() - getScrollX();
                Log.e("ScrollerLayout", "onTouchEvent ACTION_UP getScrollX()=" + getScrollX() + ",dx=" + dx);
                // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面,正值表示向左滚动
                /**
                 * 第一个参数是滚动开始时X的坐标，第二个参数是滚动开始时Y的坐标，第三个参数是横向滚动的距离，正值表示向左滚动，第四个参数是纵向滚动的距离，正值表示向上滚动
                 */
                scroller.startScroll(getScrollX(), 0, dx, 0);
                invalidate();
                break;
        }
        return true;

    }

    /**
     * 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
     */
    @Override
    public void computeScroll() {
        // 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }
}
