package com.xiaozhanxiang.simplegridview.ui;

import android.content.Context;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.adapter.RecyclerVeiwAdapter;
import com.xiaozhanxiang.simplegridview.bean.RecyclerVeiwListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * author: dai
 * date:2019/1/28
 */
public class RecyclerVeiwActivtiy extends AppCompatActivity {


    public static void getInstance(Context context) {
        Intent intent = new Intent(context,RecyclerVeiwActivtiy.class);

        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        RecyclerView recycler = findViewById(R.id.recycler);
        RecyclerVeiwAdapter adapter = new RecyclerVeiwAdapter();
        adapter.bindToRecyclerView(recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        List<RecyclerVeiwListBean> datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            RecyclerVeiwListBean bean = new RecyclerVeiwListBean();
            bean.resIds.add(R.mipmap.ic_launcher);
            bean.resIds.add(R.mipmap.ic_launcher_round);
            bean.resIds.add(R.mipmap.img);
            bean.resIds.add(R.mipmap.img_1);
            int index = i %4;
            for (int j = 0; j < index; j++) {
                bean.resIds.add(R.mipmap.img);
                bean.resIds.add(R.mipmap.img_1);
            }
            datas.add(bean);
        }

        adapter.addData(datas);
    }
}
