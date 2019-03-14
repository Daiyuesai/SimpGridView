package com.xiaozhanxiang.simplegridview.ui;

import android.content.Context;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.adapter.DragDropRecyclerViewAdapter;
import com.xiaozhanxiang.simplegridview.bean.DragDropRecyclerBean;
import com.xiaozhanxiang.simplegridview.callback.DragDropItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * author: dai
 * date:2019/2/19
 */
public class DragDropRecyclerViewActivity extends AppCompatActivity {


    private DragDropRecyclerViewAdapter mAdapter;


    public static void getInstance(Context context) {
        Intent intent = new Intent(context,DragDropRecyclerViewActivity.class);

        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_drop_recycler);
        RecyclerView recyclerView = findViewById(R.id.recycler);



        ItemTouchHelper mItemHelper = new ItemTouchHelper(new DragDropItemTouchHelperCallback());
        mAdapter = new DragDropRecyclerViewAdapter(mItemHelper);
        mAdapter.bindToRecyclerView(recyclerView);
        mItemHelper.attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this,4));


        List<DragDropRecyclerBean> datas = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            datas.add(new DragDropRecyclerBean("这是内容" + i));
        }
        mAdapter.addData(datas);

    }
}
