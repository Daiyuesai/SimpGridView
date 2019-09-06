package com.xiaozhanxiang.simplegridview.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaozhanxiang.simplegridview.R;

import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: dai
 * date:2019/8/7
 */
public class MaxMinLayoutActivity extends BaseActivity {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;



    public static void getInstance(Context context) {
        Intent intent = new Intent(context,MaxMinLayoutActivity.class);

        context.startActivity(intent);
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_min_layout);
        ButterKnife.bind(this);

        MyAdapter  adapter = new MyAdapter();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);


        List<String> data = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            data.add("" +i);
        }

        adapter.replaceData(data);
    }




    static class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder>{



        public MyAdapter() {
            super(R.layout.item_list_max_min_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.textView2,"哈哈哈哈：" + item);
        }
    }
}
