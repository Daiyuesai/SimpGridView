package com.xiaozhanxiang.simplegridview.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.adapter.MainAdapter;
import com.xiaozhanxiang.simplegridview.view.SimpleGridView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimpleGridView simpleGrid = findViewById(R.id.simple_grid);
        MainAdapter adapter = new MainAdapter(this);

        simpleGrid.setAdapter(adapter);

        List<String> data = new ArrayList<>();
        data.add("RecyclerVeiwActivtiy");
        for (int i = 0; i < 20; i++) {
            data.add("这是测试测试啊");
        }
        adapter.addData(data);
    }
}
