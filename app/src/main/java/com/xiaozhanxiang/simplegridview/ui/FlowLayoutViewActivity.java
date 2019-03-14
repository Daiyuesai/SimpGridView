package com.xiaozhanxiang.simplegridview.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xiaozhanxiang.simplegridview.R;

public class FlowLayoutViewActivity extends AppCompatActivity {


    public static void getInstance(Context context) {
        Intent intent = new Intent(context,FlowLayoutViewActivity.class);

        context.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layout_view);

    }

}
