package com.xiaozhanxiang.simplegridview.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.bean.RecyclerVeiwListBean;
import com.xiaozhanxiang.simplegridview.view.InAdapter;
import com.xiaozhanxiang.simplegridview.view.InViewHodler;
import com.xiaozhanxiang.simplegridview.view.SimpleGridView;

import java.util.List;

/**
 * author: dai
 * date:2019/1/28
 */
public class RecyclerVeiwAdapter extends BaseQuickAdapter<RecyclerVeiwListBean, BaseViewHolder> {
    public RecyclerVeiwAdapter() {
        super(R.layout.item_list_recycler);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecyclerVeiwListBean item) {

        SimpleGridView simpleGrid = helper.getView(R.id.simple_grid);
        SimpeAdapter adapter = (SimpeAdapter) simpleGrid.getAdapter();
        if (adapter == null) {
            adapter = new SimpeAdapter(mContext);
            simpleGrid.setAdapter(adapter);
        }
        adapter.replaceData(item.resIds);
    }




    public static class SimpeAdapter extends InAdapter<Integer> {

        public SimpeAdapter( Context context) {
            super(R.layout.item_list_image, context);
        }

        @Override
        protected void convert(InViewHodler hodler, Integer bean) {
            hodler.setImageResource(R.id.iv_icon,bean);
        }
    }
}
