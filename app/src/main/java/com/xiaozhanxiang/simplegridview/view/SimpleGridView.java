package com.xiaozhanxiang.simplegridview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.internal.BaselineLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import com.xiaozhanxiang.simplegridview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author: dai
 * date:2019/1/25
 * 简单的网格布局，不可以滑动，主要是针对嵌套的情况
 */
public class SimpleGridView extends BaseListViewLayout {

    private static final String TAG = "InvestigationView";


    private int mBoundSpace;//边距
    private int mMiddleSpace;
    private int mColumn;
    private int mChildWidth;

    public SimpleGridView(Context context) {
        super(context);
        init(context,null);
    }

    public SimpleGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public SimpleGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SimpleGridView);
            mBoundSpace = array.getDimensionPixelSize(R.styleable.SimpleGridView_boundSpace,0);
            mMiddleSpace = array.getDimensionPixelOffset(R.styleable.SimpleGridView_middleSpace,0);
            mColumn = array.getInteger(R.styleable.SimpleGridView_column,1);
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int childCount = getChildCount();

        if (childCount == 0) {
            setMeasuredDimension(resolveSizeAndState(0,widthMeasureSpec,0),resolveSizeAndState(0,heightMeasureSpec,0));
        }else {
            if (mColumn <= 0) {
                throw new IllegalArgumentException("列数不能小于1");
            }
            int line = childCount / mColumn;
            if (childCount %mColumn != 0) {
                line = line + 1;
            }
            int resultHeight = getPaddingBottom() + getPaddingTop();

            //每个子view的宽度
            mChildWidth = (widthSize -(mColumn - 1)* mMiddleSpace - getPaddingLeft() - getPaddingRight() - 2*mBoundSpace) / mColumn;

            for (int i = 0; i < line; i++) { //一行一行测量
                int childHeight = 0;
                for (int j = 0; j < mColumn; j++) {
                    int index = i * mColumn + j;
                    if (index < childCount) {
                        //这里不考虑直接子view 为View.GONE的状态
                        View childView = getChildAt(index);
                        if (childView != null) {
                            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
                            //默认父控件为 MATCH_PARENT
                            int childWidthMeasureSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(mChildWidth,
                                    MeasureSpec.EXACTLY),layoutParams.leftMargin+ layoutParams.rightMargin,layoutParams.width);
                            int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,0,layoutParams.height);
                            childView.measure(childWidthMeasureSpec,childHeightMeasureSpec);
                            int childMeasuredHeight = childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
                            childHeight = Math.max(childMeasuredHeight,childHeight);
                        }
                    }else {
                        //已经测量完成  结束循环
                        break;
                    }
                }
                resultHeight = resultHeight + childHeight;
//                //重新给子view测量高度，保证每一行的子view高度相等
                for (int j = 0; j < mColumn; j++) {
                    int index = i * mColumn + j;
                    if (index < childCount) {
                        //这里不考虑直接子view 为View.GONE的状态
                        View childView = getChildAt(index);
                        if (childView != null) {
                            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
                            //默认父控件为 MATCH_PARENT
                            int childWidthMeasureSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(mChildWidth,
                                    MeasureSpec.EXACTLY),layoutParams.leftMargin+ layoutParams.rightMargin,layoutParams.width);
                            //这里保证每一行的字view 的高度为这一行字view的最高的高度
                            childView.measure(childWidthMeasureSpec,MeasureSpec.makeMeasureSpec(childHeight - layoutParams.bottomMargin - layoutParams.topMargin,MeasureSpec.EXACTLY));
                        }
                    }
                }
            }
            setMeasuredDimension(widthSize,
                    resolveSizeAndState(resultHeight,heightMeasureSpec,0));

      /*     //* 瀑布流可以采用这种测量方式
            int tempChildHeight = 0;
            for (int i = 0; i < childCount; i++) {
               //这里不考虑直接子view 为View.GONE的状态
                View childView = getChildAt(i);
                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
                //默认父控件为 MATCH_PARENT
                int childWidthMeasureSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(mChildWidth,
                        MeasureSpec.EXACTLY),layoutParams.leftMargin+ layoutParams.rightMargin,layoutParams.width);
                int childHeightMeasureSpec = getChildViewMeasureSpec(layoutParams.height,heightSize);
                childView.measure(childWidthMeasureSpec,childHeightMeasureSpec);
                int childMeasuredHeight = childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
                if (i%mColumn == 0) {
                    tempChildHeight = childMeasuredHeight;
                }else {
                    tempChildHeight = Math.max(tempChildHeight,childMeasuredHeight);
                }

                if (i % mColumn == mColumn - 1 || i == childCount - 1) {
                    resultHeight = resultHeight + tempChildHeight;
                }
            }
            setMeasuredDimension(widthSize,
                    resolveSizeAndState(resultHeight,heightMeasureSpec,0));*/
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int top = getPaddingTop();
        int left ;
        int line = childCount / mColumn;
        if (childCount %mColumn != 0) {
            line = line + 1;
        }

        for (int i = 0; i < line; i++) { //一行一行的排版
            int childHeight = 0;
            for (int j = 0; j < mColumn; j++) {
                int index = i * mColumn + j;
                if (index < childCount) {
                    View childView = getChildAt(index);
                    if (childView != null) {
                        MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
                        left = getPaddingLeft() + mBoundSpace + j*(mMiddleSpace + mChildWidth) + layoutParams.leftMargin;
                        int childMeasuredHeight = childView.getMeasuredHeight();
                        childView.layout(left,top+ layoutParams.topMargin,left + childView.getMeasuredWidth(),top+ layoutParams.topMargin+ childMeasuredHeight);
                        childHeight = Math.max(childHeight,childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
                    }
                }
            }
            top = top + childHeight;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //todo 处理滑动事件

        return super.onTouchEvent(event);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) { //支持marginlayoutParams
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    }



//    /**
//     * 重新设置adapter
//     * @param adapter
//     */
//    public void setAdapter(InAdapter adapter){
//        if (mAdapter != null) {
//            mAdapter.removeOnDataChangeListener(this);
//        }
//        mAdapter = adapter;
//        if (mAdapter != null) {
//            mAdapter.addOnDataChangeListener(this);
//        }
//
//        //removeAllViews();
////        mViewHodlers.clear();
////        recyleViewHodlers.clear();
//        updateUi();
//
//
//    }
//
//    private void updateUi() {
//        if (mAdapter != null) {
//            int itemCount = mAdapter.getItemCount();
//            if (mViewHodlers.size() > itemCount) {
//                for (int i = mViewHodlers.size() -1 ; i >= itemCount; i--) {//把多余得view 放入待回收集合中
//                    recyleViewHodlers.add(mViewHodlers.remove(i));
//                }
//            }else {
//                for (int i = mViewHodlers.size(); i < itemCount; i++) {
//                    InViewHodler viewHolder = getRecyleViewHolder();
//                    if (viewHolder == null) {
//                        viewHolder = mAdapter.onCreateViewHolder(this,i);
//                    }
//                    mViewHodlers.add(viewHolder);
//                }
//            }
//
//            for (int i = 0; i < mViewHodlers.size(); i++) {
//                mAdapter.onBindViewHolder(mViewHodlers.get(i),i);
//            }
//            checkView();
//        }
//    }
//
//    private void checkView() {
//        int childCount = getChildCount();
//        int minCount = Math.min(mViewHodlers.size(),childCount);
//        for (int i = 0; i < minCount; i++) {
//            View childAt = getChildAt(i);
//            if (childAt != mViewHodlers.get(i).getContentView()) {
//                removeViewAt(i);
//                addView(mViewHodlers.get(i).getContentView(),i);
//            }
//        }
//        if (childCount < mViewHodlers.size()) {
//            for (int i = childCount; i < mViewHodlers.size(); i++) {
//                addView(mViewHodlers.get(i).getContentView());
//            }
//        }else {
//            for (int i = childCount-1; i >= mViewHodlers.size(); i--) {
//                removeViewAt(i);
//            }
//        }
//    }
//
//    public InAdapter getAdapter() {
//        return mAdapter;
//    }
//
//
//    private InViewHodler getRecyleViewHolder(){
//        if (recyleViewHodlers.size() == 0)return null;
//        return recyleViewHodlers.remove(0);
//    }
//
//    @Override
//    public void onDataChange() {
//        updateUi();
//    }
}
