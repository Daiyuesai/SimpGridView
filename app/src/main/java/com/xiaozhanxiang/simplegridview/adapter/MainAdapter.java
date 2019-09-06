package com.xiaozhanxiang.simplegridview.adapter;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.preference.PreferenceActivity;
import android.view.View;

import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.ui.AudioActivity;
import com.xiaozhanxiang.simplegridview.ui.AutoCompleteTextViewActivity;
import com.xiaozhanxiang.simplegridview.ui.CoordinatorLayoutActivity;
import com.xiaozhanxiang.simplegridview.ui.CustomLayoutManagerActivity;
import com.xiaozhanxiang.simplegridview.ui.DragDropRecyclerViewActivity;
import com.xiaozhanxiang.simplegridview.ui.FlowLayoutViewActivity;
import com.xiaozhanxiang.simplegridview.ui.ForegroundServiceActivity;
import com.xiaozhanxiang.simplegridview.ui.JINActivity;
import com.xiaozhanxiang.simplegridview.ui.LayoutManagerActivity;
import com.xiaozhanxiang.simplegridview.ui.MaxMinLayoutActivity;
import com.xiaozhanxiang.simplegridview.ui.RecyclerVeiwActivtiy;
import com.xiaozhanxiang.simplegridview.ui.RemoteProcessActivity;
import com.xiaozhanxiang.simplegridview.ui.SlideBarActivity;
import com.xiaozhanxiang.simplegridview.ui.TestActivity;
import com.xiaozhanxiang.simplegridview.ui.TransitionActivity;
import com.xiaozhanxiang.simplegridview.ui.ViodeTestActivity;
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
                }else if (hodler.getPosition() == 4){
                    ViodeTestActivity.getInstance(mContext);
                }else if (hodler.getPosition() == 5){
                    AudioActivity.getInstance(mContext);
                }else if (hodler.getPosition() == 6) {
                    TestActivity.getInstance(mContext);
                }else if (hodler.getPosition() == 7){
                    AutoCompleteTextViewActivity.getInstance(mContext);
                }else if (hodler.getPosition() == 8) {
                    RemoteProcessActivity.getInstance(mContext);
                }else if (hodler.getPosition() == 9) {
                    SlideBarActivity.getInstance(mContext);
                }else if (hodler.getPosition() == 10) {
                    TransitionActivity.getInstance(mContext);
                }else if (hodler.getPosition() == 11) {
                    MaxMinLayoutActivity.getInstance(mContext);
                }else if (hodler.getPosition() == 12){
                    LayoutManagerActivity.getInstance(mContext);
                }else if (hodler.getPosition() == 13){
                    CoordinatorLayoutActivity.getInstance(mContext);
                }else if (hodler.getPosition() == 14) {
                    ForegroundServiceActivity.getInstance(mContext);
                }
            }
        });
    }
}
