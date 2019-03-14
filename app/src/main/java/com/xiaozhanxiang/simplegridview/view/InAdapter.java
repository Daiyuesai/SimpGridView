package com.xiaozhanxiang.simplegridview.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * author: dai
 * date:2019/1/24
 */
public abstract class InAdapter<T>  {

    private int layoutId = -1;
    protected Context mContext;
    private List<T> mDatas;


    public InAdapter(int layoutId, Context context) {
        this.layoutId = layoutId;
        mContext = context;
        mDatas = new ArrayList<>();
    }

    public InViewHodler onCreateViewHolder(ViewGroup viewGroup, int position) {
        //这种inflate的方式会导致 布局文件的跟布局参数失效
       // View contentView = LayoutInflater.from(mContext).inflate(layoutId,null);
        View contentView = getView(viewGroup,position);
        if (contentView == null) {
            contentView = LayoutInflater.from(mContext).inflate(layoutId,viewGroup,false);
        }
        InViewHodler viewHodler = new InViewHodler(mContext,contentView,position);
        return viewHodler;
    }


    public void onBindViewHolder(InViewHodler viewHolder, int position) {
        viewHolder.setPosition(position);
        convert(viewHolder,mDatas.get(position));
    }


    protected abstract void convert(InViewHodler hodler,T bean);

    protected View getView(ViewGroup viewGroup,int position){
        return null;
    }

    public int getItemCount() {
        return mDatas.size();
    }

    public void addData(List<T> datas) {
        if (datas != null && datas.size() >0) {
            mDatas.addAll(datas);
            notifyDataChange();
        }
    }

    public void addData(T t){
        if (t != null){
            mDatas.add(t);
            notifyDataChange();
        }
    }

    public void addData(int index,T t){
        if (t != null && index >= 0) {
            mDatas.add(index,t);
            notifyDataChange();
        }
    }

    public void remove(int index) {
        if (index >= 0 && index < mDatas.size()) {
            mDatas.remove(index);
            notifyDataChange();
        }
    }

    public void clearData() {
        if (mDatas.size() >0) {
            mDatas.clear();
            notifyDataChange();
        }
    }

    public void replaceData(List<T> datas) {
        if (datas == null){
            datas = new ArrayList<>();
        }
        mDatas = datas;
        notifyDataChange();
    }


    public void notifyDataChange(){
        for (OnDataChangeListener listener : mOnDataChangeListener) {
            listener.onDataChange();
        }
    }
    private List<OnDataChangeListener> mOnDataChangeListener = new ArrayList<>();

    public void addOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        if (onDataChangeListener != null) {
            mOnDataChangeListener.add(onDataChangeListener);
        }
    }

    public void removeOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        if (onDataChangeListener != null) {
            mOnDataChangeListener.remove(onDataChangeListener);
        }
    }

    public interface OnDataChangeListener{
        void onDataChange();
    }


}
