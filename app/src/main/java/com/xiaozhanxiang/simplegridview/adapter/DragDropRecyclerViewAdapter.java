package com.xiaozhanxiang.simplegridview.adapter;

import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.bean.DragDropRecyclerBean;

import java.net.HttpCookie;

/**
 * author: dai
 * date:2019/2/19
 */
public class DragDropRecyclerViewAdapter extends BaseQuickAdapter<DragDropRecyclerBean, BaseViewHolder> {
    private ItemTouchHelper itemTouchHelper;
    public DragDropRecyclerViewAdapter(ItemTouchHelper itemTouchHelper) {
        super(R.layout.item_list_drag_drop);
        this.itemTouchHelper = itemTouchHelper;
    }

    @Override
    protected void convert(final BaseViewHolder helper, DragDropRecyclerBean item) {
        helper.setText(R.id.tv_content,item.content);

        helper.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemTouchHelper.startDrag(helper);
                return true;
            }
        });
    }
}
