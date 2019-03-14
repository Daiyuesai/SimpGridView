package com.xiaozhanxiang.simplegridview.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * author: dai
 * date:2019/2/19
 */
public abstract class BaseListViewLayout extends ViewGroup implements InAdapter.OnDataChangeListener {

    private InAdapter mAdapter;

    public List<InViewHodler> mViewHodlers = new ArrayList<>();
    public List<InViewHodler> recyleViewHodlers = new ArrayList<>();

    public BaseListViewLayout(Context context) {
        super(context);
    }

    public BaseListViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseListViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseListViewLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    /**
     * 重新设置adapter
     * @param adapter
     */
    public void setAdapter(InAdapter adapter){
        if (mAdapter != null) {
            mAdapter.removeOnDataChangeListener(this);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.addOnDataChangeListener(this);
        }

        //removeAllViews();
//        mViewHodlers.clear();
//        recyleViewHodlers.clear();
        updateUi();


    }

    private void updateUi() {
        if (mAdapter != null) {
            int itemCount = mAdapter.getItemCount();
            if (mViewHodlers.size() > itemCount) {
                for (int i = mViewHodlers.size() -1 ; i >= itemCount; i--) {//把多余得view 放入待回收集合中
                    recyleViewHodlers.add(mViewHodlers.remove(i));
                }
            }else {
                for (int i = mViewHodlers.size(); i < itemCount; i++) {
                    InViewHodler viewHolder = getRecyleViewHolder();
                    if (viewHolder == null) {
                        viewHolder = mAdapter.onCreateViewHolder(this,i);
                    }
                    mViewHodlers.add(viewHolder);
                }
            }

            for (int i = 0; i < mViewHodlers.size(); i++) {
                mAdapter.onBindViewHolder(mViewHodlers.get(i),i);
            }
            checkView();
        }
    }

    private void checkView() {
        int childCount = getChildCount();
        int minCount = Math.min(mViewHodlers.size(),childCount);
        for (int i = 0; i < minCount; i++) {
            View childAt = getChildAt(i);
            if (childAt != mViewHodlers.get(i).getContentView()) {
                removeViewAt(i);
                addView(mViewHodlers.get(i).getContentView(),i);
            }
        }
        if (childCount < mViewHodlers.size()) {
            for (int i = childCount; i < mViewHodlers.size(); i++) {
                addView(mViewHodlers.get(i).getContentView());
            }
        }else {
            for (int i = childCount-1; i >= mViewHodlers.size(); i--) {
                removeViewAt(i);
            }
        }
    }

    public InAdapter getAdapter() {
        return mAdapter;
    }


    private InViewHodler getRecyleViewHolder(){
        if (recyleViewHodlers.size() == 0)return null;
        return recyleViewHodlers.remove(0);
    }

    @Override
    public void onDataChange() {
        updateUi();
    }
}
