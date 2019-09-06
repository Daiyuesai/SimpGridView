package com.xiaozhanxiang.simplegridview.view;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * author: dai
 * date:2019/8/14
 * 练习自定义layoutmananger , 好难写啊
 */
public class CustomLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "CustomLayoutManager";

    /**
     * 用于临时缓存
     */
    private SparseArray<View> mCacheView = new SparseArray<>();

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }

        if (getChildCount() == 0 && state.isPreLayout()) {
            return;
        }
        int topOffset;
        int firstPosition;
        int lastPosition = getItemCount() - 1;
        //notifyDataSetChanged()也会走这里
        if (getChildCount() > 0) {  //childCount 大于0 说明是通过notifyDataSetChanged()触发 应保持滑动位置
            View child = getChildAt(0);
            firstPosition = getPosition(child); //记住刷新前停留的position
            if (firstPosition > getItemCount() - 1) {  //大于新数据的总长度，从零开始
                firstPosition = 0;
                topOffset = getPaddingTop();
            } else {
                topOffset = getDecoratedTopWithMargins(child); //记录有效的偏移量
                if (topOffset > 0) { //第一个可见view 的top大于0 说明上面有留白，需要修正
                    topOffset = getPaddingTop();
                }
            }
        } else {
            firstPosition = 0;
            topOffset = getPaddingTop();
        }

        //onLayoutChildren方法在RecyclerView 初始化时 会执行两遍 ,
       detachAndScrapAttachedViews(recycler);

        //修正布局位置
        int lastPositionBottom = topOffset;
        View childView;
        for (int i = firstPosition; i <= lastPosition; i++) {
            childView = recycler.getViewForPosition(i);
            mCacheView.put(i, childView);  //暂时缓存，同一个位置避免多次调用 recycler.getViewForPosition(i)， 多次调用会导致重新创建
            measureChildWithMargins(childView, 0, 0);
            lastPositionBottom += getDecoratedMeasuredHeightWithMargins(childView);
            if (lastPositionBottom >= getBottom()) {
                // 最后一个View的 bottom 大于 recyclerView的bottom 说明已经排满
                lastPosition = i;
            }
        }

        if (lastPositionBottom < getBottom() && (firstPosition != 0 || topOffset < getPaddingTop())) { //说明下面会有留白，需要修正
            lastPositionBottom -= topOffset;  //这里剩余的就是已有的子view排版的高度
            if (lastPositionBottom >= getVerticalSpace()) {  //已有的子View 已经可以排满RecyclerView，不需要再取子View ,只需要偏移位置
                topOffset = getBottom() - lastPositionBottom;
            }else {
                for (int i = firstPosition - 1; i >= 0; i--) {
                    childView = recycler.getViewForPosition(i);
                    mCacheView.put(i, childView);
                    measureChildWithMargins(childView,0,0);
                    lastPositionBottom += getDecoratedMeasuredHeightWithMargins(childView);
                    if (lastPositionBottom >= getVerticalSpace()) {
                        firstPosition = i;
                        topOffset = getBottom() - lastPositionBottom;
                        break;
                    }
                }

                if (lastPositionBottom < getVerticalSpace()) { //说明所有子View排完都排不满
                    firstPosition = 0;
                    topOffset = getPaddingTop();
                }
            }

        }


        //开始layout
        for (int i = firstPosition; i <= lastPosition; i++) {
            childView = mCacheView.get(i);
            addView(childView);
            layoutDecoratedWithMargins(childView, getPaddingLeft(), topOffset, getPaddingLeft() + getDecoratedMeasuredWidthWithMargins(childView), topOffset + getDecoratedMeasuredHeightWithMargins(childView));
            topOffset += getDecoratedMeasuredHeightWithMargins(childView);
        }
        mCacheView.clear();



    }


    /**
     * 考虑分割线和margins
     *
     * @return
     */
    private int getDecoratedMeasuredHeightWithMargins(View childView) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) childView.getLayoutParams();
        return getDecoratedMeasuredHeight(childView) + params.topMargin + params.bottomMargin;
    }

    public int getDecoratedMeasuredWidthWithMargins(View childView) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) childView.getLayoutParams();
        return getDecoratedMeasuredWidth(childView) + params.leftMargin + params.rightMargin;
    }

    private int getDecoratedTopWithMargins(View childView) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) childView.getLayoutParams();
        return getDecoratedTop(childView) - params.topMargin;
    }


    private int getDecoratedBottomWithMargins(View childView) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) childView.getLayoutParams();
        return getDecoratedBottom(childView) + params.bottomMargin;
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    public int getBottom() {
        return getHeight() - getPaddingBottom();
    }


    @Override
    public boolean canScrollVertically() {
        return true;  //允许垂直滑动
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }


    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //if (state.isPreLayout())return 0;

        if (dy == 0 || state.getItemCount() == 0 || getChildCount() == 0) {
            return 0;
        }
        int realOffset = dy;
        View childView = null;
        boolean isBorder = false;  //是否到边界了
        int topOffset;
        int firstPosition;
        int lastPosition = 0;
        int childCount = getChildCount();

        // //<editor-fold desc="检测滑动边界">
        if (realOffset > 0) {  // realOffset > 0 滑动方向 ⬆
            childView = getChildAt(childCount - 1);
            if (getPosition(childView) == state.getItemCount() - 1) {  //检测滑动下边界
                if (getDecoratedBottomWithMargins(childView) < getHeight() - getPaddingBottom()) {
                    realOffset = 0;
                } else {
                    realOffset = Math.min(getDecoratedBottomWithMargins(childView) - (getHeight() - getPaddingBottom()), realOffset);
                }
                isBorder = true;
            }
        } else {
            childView = getChildAt(0);
            if (getPosition(childView) == 0) {  //检测滑动上边界  getDecoratedTopWithMargins 肯定小于 getPaddingToP ,
                realOffset = -Math.min(getPaddingTop() - getDecoratedTopWithMargins(childView), -realOffset);
                isBorder = true;
            }
        }
        //</editor-fold>

        // //<editor-fold desc="回收滑出屏幕的View">
        for (int i = 0; i < getChildCount(); i++) {
            if (realOffset > 0) {  //滑动方向 ⬆
                childView = getChildAt(i);
                if (getDecoratedBottom(childView) - realOffset < getPaddingTop()) { //超过上边界，需要回收
                    removeAndRecycleView(childView, recycler);
                    i--;
                }else {
                    break;
                }
            } else {//向下滑动
                childView = getChildAt(getChildCount() - i - 1);
                if (getDecoratedTop(childView) - realOffset > getHeight() - getPaddingBottom()) { //超过下边界，需要回收
                    removeAndRecycleView(childView, recycler);
                    i--;
                }else {
                    break;
                }
            }
        }
        //</editor-fold>

        if (realOffset == 0) {
            return realOffset;
        }

        if (!isBorder) {  //如果已经到边界了不需要添加View  直接滑动就行
            if (realOffset > 0) {
                childView = getChildAt(getChildCount() - 1);
                int lastPositionBottom = topOffset = getDecoratedBottomWithMargins(childView) - realOffset;
                if (lastPositionBottom >= getBottom()) {
                    firstPosition = -1;
                } else {
                    firstPosition = getPosition(childView) + 1;
                    lastPosition = getItemCount() - 1;

                    for (int i = firstPosition; i <= lastPosition; i++) {
                        childView = recycler.getViewForPosition(i);
                        mCacheView.put(i, childView);
                        measureChildWithMargins(childView, 0, 0);
                        lastPositionBottom += getDecoratedMeasuredHeightWithMargins(childView);
                        if (lastPositionBottom >= getBottom()) {
                            lastPosition = i;
                        }
                    }
                    if (lastPositionBottom < getBottom()) {  //说明已经到边界，并且有留白，修改修正
                        realOffset -= getBottom() - lastPositionBottom;
                        topOffset += getBottom() - lastPositionBottom;
                    }
                }
            } else {
                childView = getChildAt(0);
                topOffset = getDecoratedTopWithMargins(childView) - realOffset;

                if (topOffset <= getPaddingTop()) {
                    firstPosition = -1;
                } else {

                    lastPosition = getPosition(childView) - 1;
                    firstPosition = 0;

                    for (int i = lastPosition; i >= firstPosition; i--) {
                        childView = recycler.getViewForPosition(i);
                        mCacheView.put(i, childView);
                        measureChildWithMargins(childView, 0, 0);
                        topOffset -= getDecoratedMeasuredHeightWithMargins(childView);
                        if (topOffset <= getPaddingTop()) {
                            firstPosition = i;
                            break;
                        }
                    }

                    if (topOffset > getPaddingTop()) {
                        realOffset = realOffset + ( topOffset - getPaddingTop());
                        topOffset = getPaddingTop();
                    }

                }

            }
            topOffset += realOffset; //这里要先加回去，因为排版得时候还没有滑动

            if (firstPosition != -1) {
                //开始layout
                for (int i = firstPosition; i <= lastPosition; i++) {
                    childView = mCacheView.get(i);
                    if (realOffset >0) {  //这里要特别注意  向上滑动
                        addView(childView);
                    }else {
                        addView(childView,i - firstPosition);
                    }
                    layoutDecoratedWithMargins(childView, getPaddingLeft(), topOffset, getPaddingLeft() + getDecoratedMeasuredWidthWithMargins(childView), topOffset + getDecoratedMeasuredHeightWithMargins(childView));
                    topOffset += getDecoratedMeasuredHeightWithMargins(childView);
                }
                mCacheView.clear();
            }
        }
        offsetChildrenVertical(-realOffset);
        return realOffset;
    }

}
