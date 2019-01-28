package com.xiaozhanxiang.simplegridview.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * author: dai
 * date:2019/1/24
 */
public class InViewHodler {
    private View contentView ;
    private int position;
    private SparseArray<View> mViews;
    private Context mContext;


    public InViewHodler(Context context ,View contentView,int position) {
        mContext = context;
        this.contentView = contentView;
        this.position = position;
        mViews = new SparseArray<>();
    }

    public View getContentView(){
        return contentView;
    }

    public int getPosition() {
        return position;
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = contentView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }


    public InViewHodler setText(int viewId, CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public InViewHodler setSelected(int viewId,boolean isSelected){
        View v = getView(viewId);
        v.setSelected(isSelected);
        return this;
    }

    public InViewHodler setImageResource(int viewId,int resId){
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    public InViewHodler setOnClickListener(int viewId,View.OnClickListener listener) {
        getView(viewId).setOnClickListener(listener);
        return this;
    }

    public InViewHodler showView(int ... viewIds) {
        for (int viewId : viewIds) {
            getView(viewId).setVisibility(View.VISIBLE);
        }
        return this;
    }

    public InViewHodler goneView(int ... viewIds) {
        for (int viewId : viewIds) {
            getView(viewId).setVisibility(View.GONE);
        }
        return this;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
