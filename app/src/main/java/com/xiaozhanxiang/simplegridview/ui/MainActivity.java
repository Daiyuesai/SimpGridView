package com.xiaozhanxiang.simplegridview.ui;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.adapter.MainAdapter;
import com.xiaozhanxiang.simplegridview.callback.PermissionResultListener;
import com.xiaozhanxiang.simplegridview.view.SimpleGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private String [] persiomm = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimpleGridView simpleGrid = findViewById(R.id.simple_grid);
        MainAdapter adapter = new MainAdapter(this);

        simpleGrid.setAdapter(adapter);

        List<String> data = new ArrayList<>();
        data.add("RecyclerVeiwActivtiy");
        data.add("FlowLayoutActivity");
        data.add("可拖拽recuclerView");
        data.add("jinActivity");
        data.add("ffmpeg测试");
        data.add("音频练习");
        data.add("测试界面");
        data.add("AutoCompleteTextViewActivity");
        data.add("RemoteProcessActivity");
        data.add("SlideBarActivity");
        data.add("TransitionActivity");
        data.add("MaxMinLayoutActivity");
        data.add("LayoutManagerActivity");
        data.add("CoordinatorLayoutActivity");
        data.add("ForegroundServiceActivity");
        for (int i = 0; i < 20; i++) {
            data.add("这是");
        }

        Log.e(TAG, "onCreate: 只是测试一下" );
        adapter.addData(data);

        requestPermissionsd(persiomm, 100, new PermissionResultListener() {
            @Override
            public void permissionResult(HashMap<String, Boolean> permission, boolean isAllAgree) {

            }
        });

    }
}
