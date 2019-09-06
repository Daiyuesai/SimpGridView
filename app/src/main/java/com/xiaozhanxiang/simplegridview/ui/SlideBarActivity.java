package com.xiaozhanxiang.simplegridview.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaozhanxiang.simplegridview.R;

/**
 * author: dai
 * date:2019/7/31
 */
public class SlideBarActivity extends BaseActivity {


    public static void getInstance(Context context) {
        Intent intent = new Intent(context,SlideBarActivity.class);

        context.startActivity(intent);
    }




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidbar);
    }
}
