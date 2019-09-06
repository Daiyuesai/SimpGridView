package com.xiaozhanxiang.simplegridview.adapter;

import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaozhanxiang.simplegridview.R;

/**
 * author: dai
 * date:2019/8/14
 */
public class TestLayoutManagerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private ItemTouchHelper mItemTouchHelper;
    public TestLayoutManagerAdapter() {
        super(R.layout.item_list_test_layout_manager);
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {

        if (helper.getAdapterPosition() == 10) {
            Log.i(TAG, "convert: " + item);
        }
        ((TextView)helper.itemView).setText(item);
        helper.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mItemTouchHelper != null) {
                    mItemTouchHelper.startDrag(helper);
                    return true;
                }
                return false;
            }
        });
    }

    public void setItemTouchHelper(ItemTouchHelper helper) {
        mItemTouchHelper = helper;
    }
}
