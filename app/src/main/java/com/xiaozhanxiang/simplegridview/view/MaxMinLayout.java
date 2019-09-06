package com.xiaozhanxiang.simplegridview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import com.xiaozhanxiang.simplegridview.R;

/**
 * author: dai
 * date:2019/8/7
 */
public class MaxMinLayout extends FrameLayout implements NestedScrollingParent2 {

    private static final String TAG = "MaxMinLayout";

    private int maxHeight;
    private int minHeight;
    private int currHeight;
    private NestedScrollingParentHelper mHelper;
    private float mILastY;
    private float mIDownY;
    private int mTouchSlop;
    private boolean mIsTouch;
    private boolean mIsIntercpt;


    public MaxMinLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public MaxMinLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MaxMinLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaxMinLayout);
            maxHeight = typedArray.getDimensionPixelSize(R.styleable.MaxMinLayout_max_height, 0);
            minHeight = typedArray.getDimensionPixelSize(R.styleable.MaxMinLayout_min_Height, 0);
            typedArray.recycle();
        }
        currHeight = minHeight;
        mHelper = new NestedScrollingParentHelper(this);
        // setScrollY(minHeight - maxHeight);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mIsIntercpt = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsTouch = false;
                mILastY = ev.getRawY();
                mIDownY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float y = ev.getRawY();
                if (!mIsTouch && Math.abs(y - mIDownY) > mTouchSlop) {
                    mIsTouch = true;
                }
                if (mIsTouch) {

                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                break;
        }

        if (mIsIntercpt) {
            return mIsIntercpt;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        //不让子控件可以取消拦截
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (maxHeight == 0) {
            maxHeight = height;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(currHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * @param child  直接得子类
     * @param target 产生滚动得view
     * @param axes   滑动方向
     * @param type
     * @return
     */
    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return ((ViewCompat.SCROLL_AXIS_VERTICAL & axes) != 0); //需要垂直滑动
    }


    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        mHelper.onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        Log.i(TAG, "onStopNestedScroll: ");
        mHelper.onStopNestedScroll(target, type);

    }


    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {

    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        // Log.i(TAG, "onNestedPreScroll: dy : " + dy  + "canChildScrollUp " + canChildScrollUp(target));
        int deff = 0;
        if (dy > 0) {
            if (currHeight < maxHeight) {
                deff = maxHeight - currHeight;
                if (deff > dy) {
                    deff = dy;
                }
                consumed[1] = deff;
                currHeight += deff;
                requestLayout();
                // scrollBy(0,deff);
            }
        } else {
            if (!canChildScrollUp(target) && currHeight > minHeight) {
                deff = currHeight - minHeight;
                if (deff > -dy) {
                    deff = -dy;
                }
                consumed[1] = -deff;
                currHeight -= deff;

                //scrollBy(0,-deff);
                requestLayout();
            }

        }

        Log.i(TAG, "onNestedPreScroll: dy : " + dy + "   currHeight " + currHeight);

    }


    public boolean canChildScrollUp(View mTarget) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
                //return mTarget.canScrollHorizontally(-1);
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }


}
