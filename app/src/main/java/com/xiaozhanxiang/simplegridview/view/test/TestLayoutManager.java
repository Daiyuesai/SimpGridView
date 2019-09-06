package com.xiaozhanxiang.simplegridview.view.test;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xiaozhanxiang.simplegridview.utils.Utils;

/**
 * author: dai
 * date:2019/8/10
 * 用于观察 LayoutManager调用流程
 */
public class TestLayoutManager extends LinearLayoutManager {
    private static final String TAG = "TestLayoutManager";


    public TestLayoutManager(Context context) {
        super(context);
    }

    public TestLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public TestLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        Utils.logStackInfo(TAG,"addView");
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        Utils.logStackInfo(TAG,"onLayoutChildren");
    }
}
