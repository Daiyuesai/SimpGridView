package com.xiaozhanxiang.simplegridview.adapter;

import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaozhanxiang.simplegridview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author: dai
 * date:2019/8/14
 */
public class CustomLayoutManagerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "CustomAdapter";

    private List<String> datas = new ArrayList<>();
    private int createCount = 0;
    private int bindCount = 0;
    private ItemTouchHelper mItemTouchHelper ;

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        mItemTouchHelper = itemTouchHelper;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_custom,parent,false);
        createCount ++;
        Log.i(TAG, "onCreateViewHolder:  createCount: " + createCount);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder holder, int position) {
        holder.setText(R.id.tv_test,datas.get(position));
        bindCount ++;
        Log.i(TAG, "onBindViewHolder:  bindCount: " + bindCount + "   position: " + position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mItemTouchHelper != null){
                    mItemTouchHelper.startDrag(holder);

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public void addData(List<String> datas) {
        if (datas != null) {
            this.datas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public List<String> getDatas() {
        return datas;
    }

    public void replaceData(List<String> datas) {

        this.datas.clear();

        this.datas.addAll(datas);

        notifyDataSetChanged();
    }
}
