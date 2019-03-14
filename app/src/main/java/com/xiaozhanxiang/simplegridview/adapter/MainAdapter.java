package com.xiaozhanxiang.simplegridview.adapter;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.view.View;

import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.ui.DragDropRecyclerViewActivity;
import com.xiaozhanxiang.simplegridview.ui.FlowLayoutViewActivity;
import com.xiaozhanxiang.simplegridview.ui.JINActivity;
import com.xiaozhanxiang.simplegridview.ui.RecyclerVeiwActivtiy;
import com.xiaozhanxiang.simplegridview.view.InAdapter;
import com.xiaozhanxiang.simplegridview.view.InViewHodler;

/**
 * author: dai
 * date:2019/1/28
 */
public class MainAdapter extends InAdapter<String> {
    public MainAdapter(Context context) {
        super(R.layout.item_list_main, context);
    }

    @Override
    protected void convert(final InViewHodler hodler, String bean) {
        hodler.setText(R.id.tv_name,bean);

        hodler.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hodler.getPosition() == 0) {
                    RecyclerVeiwActivtiy.getInstance(mContext);
                }else if (hodler.getPosition() == 1) {
                    FlowLayoutViewActivity.getInstance(mContext);
                }else if (hodler.getPosition() == 2){
                    DragDropRecyclerViewActivity.getInstance(mContext);
                }else if (hodler.getPosition() == 3){
                    JINActivity.getInstance(mContext);
                }
            }
        });
    }
}
