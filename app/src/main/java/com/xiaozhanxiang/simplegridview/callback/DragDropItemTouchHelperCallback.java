package com.xiaozhanxiang.simplegridview.callback;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.Collections;
import java.util.List;

/**
 * author: dai
 * date:2019/2/19
 */
public class DragDropItemTouchHelperCallback extends ItemTouchHelper.Callback {


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        //得到当拖拽的viewHolder的Position
        int fromPosition = viewHolder.getAdapterPosition();
        //拿到当前拖拽到的item的viewHolder
        int toPosition = target.getAdapterPosition();

        BaseQuickAdapter adapter = (BaseQuickAdapter) recyclerView.getAdapter();
        List mDatas = adapter.getDatasSource();
        if (toPosition == mDatas.size() - 1){
            return true;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mDatas, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mDatas, i, i - 1);
            }
        }
        adapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
       // Log.e("hsjkkk", "拖拽完成 方向" + direction);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        //Log.e("hsjkkk", "onSelectedChanged()");
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE)
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
       // Log.e("hsjkkk", "clearView()");
        viewHolder.itemView.setBackgroundColor(0);
    }

    //重写拖拽不可用
    @Override
    public boolean isLongPressDragEnabled() {
        //Log.e("hsjkkk", "isLongPressDragEnabled()");
        return false;
    }
}
