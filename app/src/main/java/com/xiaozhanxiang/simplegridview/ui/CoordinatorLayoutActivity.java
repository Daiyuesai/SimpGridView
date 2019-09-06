package com.xiaozhanxiang.simplegridview.ui;

import android.content.Context;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.adapter.TestLayoutManagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: dai
 * date:2019/8/20
 */
public class CoordinatorLayoutActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing)
    CollapsingToolbarLayout collapsing;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    public static void getInstance(Context context) {
        Intent intent = new Intent(context, CoordinatorLayoutActivity.class);

        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinatorlayout);
        ButterKnife.bind(this);

        //setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle("沃诗标题");
        collapsing.setTitle("我是一个标题");
      //  toolbar.setTitle("我是一个标题");



        TestLayoutManagerAdapter adapter = new TestLayoutManagerAdapter();

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);


        List<String> datas = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            datas.add("我欸发就我欸发就");
        }

        adapter.addData(datas);
    }
}
