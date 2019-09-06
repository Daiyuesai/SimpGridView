package com.xiaozhanxiang.simplegridview.ui;

import android.content.Context;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.xiaozhanxiang.simplegridview.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: dai
 * date:2019/8/14
 */
public class LayoutManagerActivity extends BaseActivity {

    @BindView(R.id.tv_test)
    TextView tvTest;
    @BindView(R.id.tv_custom)
    TextView tvCustom;

    public static void getInstance(Context context) {
        Intent intent = new Intent(context, LayoutManagerActivity.class);

        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_manager);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_test, R.id.tv_custom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_test:
                TestLayoutManagerActivity.getInstance(this);
                break;
            case R.id.tv_custom:
                CustomLayoutManagerActivity.getInstance(this);
                break;
        }
    }
}
